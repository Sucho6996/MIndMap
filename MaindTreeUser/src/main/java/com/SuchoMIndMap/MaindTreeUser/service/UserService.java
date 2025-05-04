package com.SuchoMIndMap.MaindTreeUser.service;

import com.SuchoMIndMap.MaindTreeUser.controller.MindtreeServiceFeign;
import com.SuchoMIndMap.MaindTreeUser.model.Content;
import com.SuchoMIndMap.MaindTreeUser.model.Topic;
import com.SuchoMIndMap.MaindTreeUser.model.Usuaria;
import com.SuchoMIndMap.MaindTreeUser.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtService jwtService;
    @Autowired
    TwilioService twilioService;
    @Autowired
    MyEncoding myEncoding;
    @Autowired
    MindtreeServiceFeign feign;


    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);



    public ResponseEntity<Usuaria> getMyData(String token) {
        String phNo= jwtService.extractUserName(token);
        return new ResponseEntity<>(userRepo.findByphNo(phNo),HttpStatus.FOUND);
    }


    public ResponseEntity<Map<String, String>> addUser(Usuaria user) {
        Map<String,String> response=new HashMap<>();
        try {
            String s=myEncoding.encode(user.getName(), user.getPhNo(), user.getPass());
            user.setPass(encoder.encode(s));
            if(!userRepo.existsById(user.getPhNo())){
                userRepo.save(user);
                response.put("message","Succesfully Account created");
                return new ResponseEntity<>(response,HttpStatus.OK);
            }
            else{
                response.put("message","Phone number already been registered");
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            response.put("message",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
    }

    public ResponseEntity<Map<String, String>> addTopic(String token, Topic topic) {
        String phNo=jwtService.extractUserName(token);
        return feign.addTopic(phNo,topic);
    }

    public ResponseEntity<List<Topic>> getTopic(String token) {
        String phNo= jwtService.extractUserName(token);
        return feign.getTopic(phNo);
    }

    public ResponseEntity<Map<String, String>> addContent
            (Long topicId, Long parentId, Content content) {
        return feign.addContent(topicId,parentId,content);
    }

    public ResponseEntity<List<Content>> getContent(Long topicId) {
        return feign.getContent(topicId);
    }

    public ResponseEntity<Map<String, String>> isComplete(long topicId, long contentId) {
        return feign.setAsCompleted(topicId,contentId);
    }

    public ResponseEntity<Map<String, String>> deleteContent(long topicId, long contentId) {
        return feign.deleteContent(topicId,contentId);
    }

    public ResponseEntity<Map<String, String>> deleteTopic(long topicId) {
        return feign.deleteTopic(topicId);
    }
}
