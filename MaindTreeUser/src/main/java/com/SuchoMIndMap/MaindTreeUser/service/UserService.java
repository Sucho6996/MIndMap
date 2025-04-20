package com.SuchoMIndMap.MaindTreeUser.service;



import com.SuchoMIndMap.MaindTreeUser.model.Usuaria;
import com.SuchoMIndMap.MaindTreeUser.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtService jwtService;
    @Autowired
    TwilioService twilioService;



    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);



    public ResponseEntity<Usuaria> getMyData(String token) {
        String phNo= jwtService.extractUserName(token);
        return new ResponseEntity<>(userRepo.findByphNo(phNo),HttpStatus.FOUND);
    }


    public ResponseEntity<Map<String, String>> addUser(Usuaria user) {
        Map<String,String> response=new HashMap<>();
        try {
            user.setPass(encoder.encode(user.getPass()));
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
}
