package com.pavan;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class TransactionService {

    private static final String TXN_CREATE_TOPIC = "txn_create";
    private static final String TXN_COMPLETE_TOPIC = "txn_complete";
    private static final String WALLET_UPDATE_TOPIC = "wallet_update";

    @Autowired
    private TransactionRepo transactionRepository;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public String createTxn(Transaction transaction){
        transaction.setTxnId(UUID.randomUUID().toString());
        transaction.setTransactionStatus(TransactionStatus.PENDING);

        transactionRepository.save(transaction);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sender", transaction.getSenderId());
        jsonObject.put("receiver", transaction.getReceiverId());
        jsonObject.put("amount", transaction.getAmount());
        jsonObject.put("txnId", transaction.getTxnId());

        kafkaTemplate.send(TXN_CREATE_TOPIC, jsonObject.toJSONString());

        return transaction.getTxnId();
    }

    @KafkaListener(topics = {WALLET_UPDATE_TOPIC}, groupId = "ewallet")
    public void updateTxn(String msg) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);

            String txnId = (String) jsonObject.get("txnId");
            String status = (String) jsonObject.get("status");

            TransactionStatus transactionStatus;

            if("FAILED".equals(status)){
                transactionStatus = TransactionStatus.FAILED;
            } else {
                transactionStatus = TransactionStatus.SUCCESSFUL;
            }

            Transaction transaction = transactionRepository.findByTxnId(txnId);
            if (transaction != null) {
                transaction.setTransactionStatus(transactionStatus);
                transactionRepository.save(transaction);

                Integer receiverId = transaction.getReceiverId();
//                String receiverEmail=transactionRepository.findById(receiverId).;
                Integer senderId = transaction.getSenderId();

                // Rest of your code for updating sender and receiver details...

                JSONObject txnCompleteEvent = new JSONObject();
                txnCompleteEvent.put("txnId", txnId);
                txnCompleteEvent.put("status", transaction.getTransactionStatus().name());
                txnCompleteEvent.put("amount", transaction.getAmount());
                txnCompleteEvent.put("senderId",transaction.getSenderId());
                txnCompleteEvent.put("receiverId",transaction.getReceiverId());


                kafkaTemplate.send(TXN_COMPLETE_TOPIC, txnCompleteEvent.toJSONString());
            } else {
                System.err.println("Transaction not found for txnId: " + txnId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





