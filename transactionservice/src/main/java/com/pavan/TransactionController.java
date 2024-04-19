package com.pavan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @PostMapping("/transact")
    String createTxn(@RequestBody TransactionCreateService transactionCreateService){
            String txnId=transactionService.createTxn(transactionCreateService.toTransaction());
        return txnId;
    }
}
