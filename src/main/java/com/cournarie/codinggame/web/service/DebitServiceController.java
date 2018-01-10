package com.cournarie.codinggame.web.service;

import com.cournarie.codinggame.service.AmountService;
import com.cournarie.codinggame.service.Debit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;
import java.util.Date;

@RestController
@RequestMapping(value="/api")
public class DebitServiceController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DebitServiceController.class);

    @Autowired
    private AmountService reservationService;

    @RequestMapping(method= RequestMethod.GET, value="/check")
    public String addAmount(@RequestParam(value="account_id")String accountId, @RequestParam(value="amount")int amount){
         boolean ok = this.reservationService.addAmount(accountId, new Debit(new Date().getTime(), amount));
         LOGGER.info ("Checking '" + accountId + "' on '" + amount);
         return ok ? "Crystal clear..." : "Dark side suspected...";
    }
}
