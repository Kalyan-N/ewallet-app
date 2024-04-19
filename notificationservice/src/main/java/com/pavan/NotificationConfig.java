package com.pavan;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

@Configuration
public class NotificationConfig {
    private static final String TXN_COMPLETE_TOPIC="txn_complete_topic";
    Properties getConsumerConfig(){
        Properties properties1=new Properties();
        properties1.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties1.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties1.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        return properties1;
    }
    @Bean
    ConsumerFactory<String,String> consumerFactory(){
        return new DefaultKafkaConsumerFactory(getConsumerConfig());
    }
    @Bean
    SimpleMailMessage getMailMsg(){
        return new SimpleMailMessage();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    JavaMailSender getMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("enter app register mail");
        javaMailSender.setPassword("app password");

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.debug", true);

        return javaMailSender;
    }
}
