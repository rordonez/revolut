package rafael.ordonez.revolut.controllers.transactions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import rafael.ordonez.revolut.RevolutApplicationTests;
import rafael.ordonez.revolut.controllers.errorhandling.RevolutControllerAdvice;
import rafael.ordonez.revolut.controllers.transactions.beans.TransferRequestBody;
import rafael.ordonez.revolut.exceptions.InternalAccountNotFoundException;
import rafael.ordonez.revolut.exceptions.ProcessTransactionException;
import rafael.ordonez.revolut.exceptions.TransactionNotImplementedException;
import rafael.ordonez.revolut.model.accounts.Account;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.model.transactions.AccountTransferStatus;
import rafael.ordonez.revolut.services.AccountService;
import rafael.ordonez.revolut.services.TransferService;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by rafa on 21/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RevolutApplicationTests.class)
@WebAppConfiguration
public class TransactionsControllerTest {

    @Autowired
    ObjectMapper mapper;

    private MockMvc mockMvc;

    @Mock
    private TransferService transferService;

    @Mock
    private AccountService accountService;

    @Mock
    private Account sourceAccount;

    @Mock
    private Account targetAccount;

    @InjectMocks
    private TransactionsController transactionsController;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(transactionsController)
                .setHandlerExceptionResolvers(createExceptionResolver())
                .build();
        when(sourceAccount.getId()).thenReturn(0L);
        when(targetAccount.getId()).thenReturn(1L);
    }

    /*
     *   -------------------------------------
     *   - CREATE TRANSACTION TESTS -
     *   -------------------------------------
     */

    @Test
    public void testCreateTransaction() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody("0", "1", 10.0);
        when(transferService.doTransfer(sourceAccount.getId(), targetAccount.getId(), transferRequestBody.getAmount())).thenReturn(new AccountTransfer());
        when(accountService.getUserAccount(transferRequestBody.getSourceAccount())).thenReturn(sourceAccount);
        when(accountService.getInternalAccount(transferRequestBody.getTargetAccount())).thenReturn(targetAccount);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)))
                .andExpect(status().isAccepted());
    }

    private String getJson(TransferRequestBody transferRequestBody) throws JsonProcessingException {
        return mapper.writeValueAsString(transferRequestBody);
    }

    @Test
    public void testCreateTransactionCheckBehaviour() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody("0", "1", 10.0);
        when(transferService.doTransfer(sourceAccount.getId(), targetAccount.getId(), transferRequestBody.getAmount())).thenReturn(new AccountTransfer());
        when(accountService.getUserAccount(transferRequestBody.getSourceAccount())).thenReturn(sourceAccount);
        when(accountService.getInternalAccount(transferRequestBody.getTargetAccount())).thenReturn(targetAccount);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)));

        InOrder order = Mockito.inOrder(accountService, transferService);
        order.verify(accountService).getUserAccount(transferRequestBody.getSourceAccount());
        order.verify(accountService).getInternalAccount(transferRequestBody.getTargetAccount());
        order.verify(transferService).doTransfer(sourceAccount.getId(), targetAccount.getId(), transferRequestBody.getAmount());
    }

    @Test
    public void testCreateTransactionResponseHasALinkToItself() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody("0", "1", 10.0);
        when(transferService.doTransfer(sourceAccount.getId(), targetAccount.getId(), transferRequestBody.getAmount())).thenReturn(stubbedTransfer(0L, 1L, transferRequestBody.getAmount()));
        when(accountService.getInternalAccount(transferRequestBody.getTargetAccount())).thenReturn(targetAccount);
        when(accountService.getUserAccount(transferRequestBody.getSourceAccount())).thenReturn(sourceAccount);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)))
                .andExpect(jsonPath("$.links", hasSize(1)))
                .andExpect(jsonPath("$.links[0].href", endsWith("/transactions/" + stubbedTransfer(sourceAccount.getId(), targetAccount.getId(), transferRequestBody.getAmount()).getId())))
                .andExpect(jsonPath("$.links[0].rel", is("self")));
    }

    /*
     *   -------------------------------------------
     *   - Create Internal Transfer Error Handling -
     *   -------------------------------------------
     */
    @Test
    public void testCreateTransactionSourceAccountDoesNotBelongToCurrentUser() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody("0", "1", 10.0);
        when(accountService.getUserAccount(transferRequestBody.getSourceAccount())).thenThrow(new InternalAccountNotFoundException("The source account with number: " + transferRequestBody.getSourceAccount() + " does not belong to the current user"));

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("The source account with number: " + transferRequestBody.getSourceAccount() + " does not belong to the current user")));
    }

    @Test
    public void testCreateTransactionForExternalAccountsIsNotImplemented() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody("0", "1", 10.0);
        when(accountService.getInternalAccount(transferRequestBody.getTargetAccount())).thenThrow(new TransactionNotImplementedException("The external transactions are not implemented yet."));

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.message", is("The external transactions are not implemented yet.")));
    }

    /*
     *   ----------------------------------------------------
     *   - Create Internal Transfer Input Validations Tests -
     *   ----------------------------------------------------
     */
    @Test
    public void testCreateTransactionWithNullSourceAccount() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody(null, "1", 10.0);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("Invalid value for argument sourceAccount and description: Source account number is null or empty")));
    }

    @Test
    public void testCreateTransactionWithBothAccountsNull() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody(null, null, 10.0);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testCreateTransactionWithNegativeAmount() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody("1", "2", -1);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("Invalid value for argument amount and description: Amount can not be negative")));
    }

    @Test
    public void testCreateTransactionSourceAndTargetAccountsMustBeDifferent() throws Exception {
        TransferRequestBody transferRequestBody = createTransferRequestBody("1", "1", 1);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(getJson(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("Invalid value for argument sourceAccount and description: Source and target account must be different")));
    }

    /*
     *   ----------------------------------------------------
     *   - PROCESS TRANSACTION TESTS -
     *   ----------------------------------------------------
     */

    @Test
    public void testProcessTransaction() throws Exception {
        long transactionId = 0L;
        when(transferService.processTransfer(transactionId)).thenReturn(processStubbedTransfer(0L, 1L, 5.0));

        mockMvc.perform(put("/transactions/0")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links", hasSize(1)))
                .andExpect(jsonPath("$.links[0].href", endsWith("/transactions/0")))
                .andExpect(jsonPath("$.links[0].rel", is("self")));
    }

    @Test
    public void testProcessTransactionInvokeServiceToProcessTheTransaction() throws Exception {
        long transactionId = 0L;
        when(transferService.processTransfer(transactionId)).thenReturn(processStubbedTransfer(0L, 1L, 5.0));

        mockMvc.perform(put("/transactions/0")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")));

        verify(transferService).processTransfer(transactionId);
    }

    @Test
    public void testProcessTransactionThrowATransactionException() throws Exception {
        long transactionId = 0L;
        when(transferService.processTransfer(transactionId)).thenThrow(new ProcessTransactionException("Error processing the transfer with id " + transactionId));

        mockMvc.perform(put("/transactions/0")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error processing the transfer with id " + transactionId)));
    }

    /**
     * @see <a href="https://jira.spring.io/browse/SPR-12751">SPR-12751</a>
     * <p>
     * Spring MVC will support the configuration of ControllerAdvice for standalone configurations in Spring 4.2
     */
    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RevolutControllerAdvice.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new RevolutControllerAdvice(), method);
            }
        };
        exceptionResolver.afterPropertiesSet();
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return exceptionResolver;
    }

    private TransferRequestBody createTransferRequestBody(String sourceAccountNumber, String targetAccountNumber, double amount) {
        return new TransferRequestBody(sourceAccountNumber, targetAccountNumber, amount);
    }

    private static AccountTransfer stubbedTransfer(Long sourceAccount, Long targetAccount, double amount) {
        return new AccountTransfer(sourceAccount, targetAccount, amount);
    }

    private static AccountTransfer processStubbedTransfer(Long sourceAccount, Long targetAccount, double amount) {
        AccountTransfer transfer = stubbedTransfer(sourceAccount, targetAccount, amount);
        transfer.setStatus(AccountTransferStatus.COMPLETED);
        return transfer;
    }
}
