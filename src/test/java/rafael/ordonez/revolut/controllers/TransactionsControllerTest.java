package rafael.ordonez.revolut.controllers;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rafael.ordonez.revolut.RevolutApplicationTests;
import rafael.ordonez.revolut.controllers.transactions.TransactionsController;
import rafael.ordonez.revolut.controllers.transactions.beans.TransferRequestBody;
import rafael.ordonez.revolut.model.transactions.AccountTransfer;
import rafael.ordonez.revolut.services.TransferService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionsController)
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
        TransferRequestBody transferRequestBody = new TransferRequestBody("0", "1", 10.0);
        Mockito.when(transferService.doTransfer(transferRequestBody.getSourceAccount(), transferRequestBody.getTargetAccount(), transferRequestBody.getAmount())).thenReturn(new AccountTransfer());

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)));

        Mockito.verify(transferService).doTransfer(transferRequestBody.getSourceAccount(), transferRequestBody.getTargetAccount(), transferRequestBody.getAmount());
    }
}
