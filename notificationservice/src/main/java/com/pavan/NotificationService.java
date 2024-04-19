package com.pavan;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    SimpleMailMessage simpleMailMessage;

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    RestTemplate restTemplate;

    private static final String TXN_COMPLETE_TOPIC = "txn_complete";


    @KafkaListener(topics = {TXN_COMPLETE_TOPIC}, groupId = "ewallet")
    public void sendNotif(String msg) throws Exception{

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);
        System.out.println(jsonObject.toJSONString());
        int senderId = ((Long) jsonObject.get("senderId")).intValue();
        int receiverId = ((Long) jsonObject.get("receiverId")).intValue();

        JSONObject sender = restTemplate.getForObject("http://localhost:9090/user?id=" + senderId, JSONObject.class);
        String senderEmail = (String) sender.get("email");
        System.out.println("Email of sender: " + senderEmail);

        JSONObject receiver = restTemplate.getForObject("http://localhost:9090/user?id=" + receiverId, JSONObject.class);
        String receiverEmail = (String) receiver.get("email");
        System.out.println("Email of receiver: " + receiverEmail);

        String txnId = (String) jsonObject.get("txnId");
        String status = (String) jsonObject.get("status");
        Double amount  = (Double) jsonObject.get("amount");

        // Sending mail to sender
        simpleMailMessage.setText("Hi, your txn with id " + txnId + " got " + status);
        simpleMailMessage.setTo(senderEmail);
        simpleMailMessage.setSubject("Payment Notification");
        simpleMailMessage.setFrom("pavanklayan5@gmail.com");
        javaMailSender.send(simpleMailMessage);
        if("SUCCESSFUL".equals(status)){
            // Sending mail to receiver
            simpleMailMessage.setText("Hi, you got amount " + amount + " from user " + senderEmail + " in your e-wallet");
            simpleMailMessage.setTo(receiverEmail);
            javaMailSender.send(simpleMailMessage);
        }
    }
}
