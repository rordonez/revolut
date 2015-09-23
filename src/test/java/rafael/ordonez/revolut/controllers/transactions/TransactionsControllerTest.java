package rafael.ordonez.revolut.controllers.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import rafael.ordonez.revolut.exceptions.AccountTransferException;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.services.TransferService;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    }

    @Test
    public void testIfTransactionResourceExists() throws Exception {
        TransferRequestBody transferRequestBody = new TransferRequestBody("0", "1", 10.0);
        Mockito.when(transferService.doTransfer(transferRequestBody.getSourceAccount(), transferRequestBody.getTargetAccount(), transferRequestBody.getAmount())).thenReturn(new AccountTransfer());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testTransferServiceIsInvoked() throws Exception {
        String sourceAccount = "0";
        String targetAccount = "1";
        double amount = 10.0;
        TransferRequestBody transferRequestBody = new TransferRequestBody(sourceAccount, targetAccount, amount);
        Mockito.when(transferService.doTransfer(transferRequestBody.getSourceAccount(), transferRequestBody.getTargetAccount(), transferRequestBody.getAmount())).thenReturn(new AccountTransfer());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)));

        Mockito.verify(transferService).doTransfer(transferRequestBody.getSourceAccount(), transferRequestBody.getTargetAccount(), transferRequestBody.getAmount());
    }

    @Test
    public void testTransferResponseHasALinkToItself() throws Exception {
        String sourceAccount = "0";
        String targetAccount = "1";
        double amount = 10.0;
        TransferRequestBody transferRequestBody = new TransferRequestBody(sourceAccount, targetAccount, amount);
        Mockito.when(transferService.doTransfer(transferRequestBody.getSourceAccount(), transferRequestBody.getTargetAccount(), transferRequestBody.getAmount())).thenReturn(stubbedTransfer(sourceAccount, targetAccount, amount));

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))
                .andExpect(jsonPath("$.links", hasSize(1)))
                .andExpect(jsonPath("$.links[0].href", endsWith("/transactions/" + stubbedTransfer(sourceAccount, targetAccount, amount).getId())))
                .andExpect(jsonPath("$.links[0].rel", is("self")));
    }

    @Test
    public void testTransferWithExceptionReturnsErrorMessages() throws Exception {
        TransferRequestBody transferRequestBody = new TransferRequestBody("0", "1", 10.0);
        Mockito.when(transferService.doTransfer(transferRequestBody.getSourceAccount(), transferRequestBody.getTargetAccount(), transferRequestBody.getAmount())).thenThrow(new AccountTransferException("Unexpected error in transfer"));

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Unexpected error in transfer")));
    }

    @Test
    public void testTransferWithNullSourceAccount() throws Exception {
        TransferRequestBody transferRequestBody = new TransferRequestBody(null, "1", 10.0);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("Invalid value for argument sourceAccount")));
    }

    @Test
    public void testTransferWithBothAccountsNull() throws Exception {
        TransferRequestBody transferRequestBody = new TransferRequestBody(null, null, 10.0);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testCreateTransferWithPositiveAmount() throws Exception {
        TransferRequestBody transferRequestBody = new TransferRequestBody("1", "2", -1);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message", is("Invalid value for argument amount")));


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

    private static AccountTransfer stubbedTransfer(String sourceAccount, String targetAccount, double amount) {
        return new AccountTransfer(sourceAccount, targetAccount, amount);
    }
}
