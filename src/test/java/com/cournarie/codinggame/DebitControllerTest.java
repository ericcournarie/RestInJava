package com.cournarie.codinggame;

import com.cournarie.codinggame.service.FraudConfiguration;
import com.cournarie.codinggame.web.service.DebitServiceController;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DebitControllerTest {
    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DebitControllerTest.class);
    @Autowired
    private DebitServiceController reservationService;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(reservationService).build();
    }

    @Test
    public void check() throws Exception{

        reservationService = mock(DebitServiceController.class);

        // direct suspect debit..
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0001&amount=5000000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Dark side suspected...")));

        // sequence of debits that lead to a suspect transfer
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0002&amount=500000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Crystal clear...")));
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0002&amount=499999")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Crystal clear...")));
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0002&amount=20000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Dark side suspected...")));

        // be sure negative ones works
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0003&amount=500000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Crystal clear...")));
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0003&amount=-400000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Crystal clear...")));
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0003&amount=20000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Crystal clear...")));
    }

    @Test
    public void checkTimeWindow() throws Exception{

        reservationService = mock(DebitServiceController.class);

        long previous = FraudConfiguration.getElapseTime();
        // set elapse time to 1 second for test purposes
        FraudConfiguration.setElapseTime(1000);
        // half of limit
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0009&amount=500000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Crystal clear...")));

        // wait more than the delay window..
        Thread.sleep(2000);

        // half of limit .. still working
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0009&amount=500000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Crystal clear...")));

        // do not abuse...
        this.mockMvc.perform(get("/api/check/?account_id=IBAN0009&amount=500000")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString("Dark side suspected...")));

        // reset state
        FraudConfiguration.setElapseTime(previous);

    }
}
