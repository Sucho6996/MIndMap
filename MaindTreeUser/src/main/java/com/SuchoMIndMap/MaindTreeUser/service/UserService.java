package com.SuchoMIndMap.MaindTreeUser.service;



import com.SuchoMIndMap.MaindTreeUser.model.Usuaria;
import com.SuchoMIndMap.MaindTreeUser.repo.UserRepo;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.pqc.crypto.sphincs.SPHINCS256KeyGenerationParameters;
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
        StringBuilder s = new StringBuilder();
        int i=0;
        try {
            for(i=0;i<user.getPhNo().length()&&
                    i<user.getName().length()&&
                    i<user.getPass().length();i++){
                char c= (char) (user.getPhNo().charAt(i)+user.getName().charAt(i)+user.getPass().charAt(i));
                s.append(c);
            }
            if(i<user.getPass().length()) s.append(user.getPass().substring(i+1,user.getPass().length()));
            user.setPass(encoder.encode(s.toString()));
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
