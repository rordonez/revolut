package rafael.ordonez.revolut.controllers.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rafael.ordonez.revolut.RevolutApplication;
import rafael.ordonez.revolut.controllers.transactions.beans.TransferRequestBody;
import rafael.ordonez.revolut.model.transactions.AccountTransferStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by rafa on 24/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RevolutApplication.class)
@WebAppConfiguration
public class TransactionsControllerIntegrationTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testDoATransferAndProcessIt() throws Exception {
        TransferRequestBody transferRequestBody = new TransferRequestBody("0", "1", 10.0);

        MvcResult mvcResult = mockMvc.perform(post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))

                .andExpect(jsonPath("$._links.self.href", containsString("/transactions")))
                .andExpect(jsonPath("$.status", is(AccountTransferStatus.PENDING.toString())))
                .andReturn();


        long transactionId = findTransactionId(mvcResult.getResponse().getContentAsString());

        mockMvc.perform(put("/transactions/" + transactionId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$._links.self.href", containsString("/transactions/" + transactionId)))
                .andExpect(jsonPath("$.status", is(AccountTransferStatus.COMPLETED.toString())));
    }


    @Test
    public void testDoATransferAndDoAndSendAnInvalidTransactionId() throws Exception {
        TransferRequestBody transferRequestBody = new TransferRequestBody("0", "1", 10.0);

        mockMvc.perform(post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))

                .andExpect(jsonPath("$._links.self.href", endsWith("transactions/2")))
                .andExpect(jsonPath("$.status", is(AccountTransferStatus.PENDING.toString())));


        mockMvc.perform(put("/transactions/1000")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("The transaction with id: 1000 is not found.")));
    }

    @Test
    public void testNoMoneyProblemsIsNotPosibleBuyLottery() throws Exception {
        TransferRequestBody transferRequestBody = new TransferRequestBody("1", "0", 10.0);

        mockMvc.perform(post("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(transferRequestBody)))
                .andExpect(jsonPath("$.message", is("The source account with number: 1 does not belong to the current user")));

    }

    private long findTransactionId(String response) {
        long result = 0;
        Pattern p =
                Pattern.compile(".*/transactions/(\\d+)");
        Matcher m = p.matcher(response);
        if (m.find()) {
            result =  Long.parseLong(m.group(1));
        }
        return result;
    }
}
