package com.pavan;

import org.apache.kafka.common.protocol.types.Field;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final String USER_CREATE_TOPIC="user_create";
    @Autowired
    UserRepository userRepository;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    public User create(User user) {
        User user1=userRepository.save(user);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("userId",user.getId());
        jsonObject.put("email",user.getEmail());
        jsonObject.put("contact",user.getContact());

        kafkaTemplate.send(USER_CREATE_TOPIC, jsonObject.toJSONString());
        return userRepository.save(user);

    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }
}
