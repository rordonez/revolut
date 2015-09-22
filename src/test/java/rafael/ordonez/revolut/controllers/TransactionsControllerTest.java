package rafael.ordonez.revolut.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import rafael.ordonez.revolut.RevolutApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by rafa on 21/9/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RevolutApplication.class)
@WebAppConfiguration
public class TransactionsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testIfTransactionResourceExists() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(stubPostBody("0", "1", 10.0)))
                .andExpect(status().isAccepted());
    }

    private String stubPostBody(String sourceAccount, String targetAccount, double amount) {
        return "{" +
                "\"sourceAccount\": \"" + sourceAccount + "\"," +
                "\"targetAccount\": \"" + targetAccount + "\"," +
                "\"amount\": " + amount +
                "}";
    }
}
