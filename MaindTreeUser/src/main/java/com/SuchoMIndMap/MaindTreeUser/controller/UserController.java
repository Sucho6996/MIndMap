package com.SuchoMIndMap.MaindTreeUser.controller;

import com.SuchoMIndMap.MaindTreeUser.model.Usuaria;
import com.SuchoMIndMap.MaindTreeUser.model.VerifyUser;
import com.SuchoMIndMap.MaindTreeUser.service.JwtService;
import com.SuchoMIndMap.MaindTreeUser.service.MyUserDetailsService;
import com.SuchoMIndMap.MaindTreeUser.service.TwilioService;
import com.SuchoMIndMap.MaindTreeUser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;
    @Autowired
    TwilioService twilioService;
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<Map<String,String>> signUp(@RequestBody Usuaria user){
        return userService.addUser(user);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> logIn(@RequestBody Usuaria user){
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
            Authentication auth= authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(user.getPhNo(),s.toString()));
            if(auth.isAuthenticated()){
                response.put("message", jwtService.generateToken(user.getPhNo()));
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            }
        }
        catch (BadCredentialsException e){
            response.put("message","Invalid Credential");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            response.put("message","Some error occur");
            return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        }
        response.put("message","Authentication Failed!!!");
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout
            (@RequestHeader("Authorization") String authHeader){
        Map<String,String> response=new HashMap<>();
        String token=authHeader.substring(7);
        if(jwtService.invalidateToken(token)){
            response.put("message","Logout successfully");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
            response.put("message","Invalid Token you hakor");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Usuaria> getMyData(@RequestHeader("Authorization") String authHeader){
        String token=authHeader.substring(7);
        return userService.getMyData(token);
    }

    @PostMapping("/sendOtpSecure")
    public ResponseEntity<Map<String,String>> sendOtpSecure(@RequestHeader("Authorization") String authHeader){
        String token=authHeader.substring(7);
        String phNo= jwtService.extractUserName(token);
        return twilioService.sendOtp(phNo);
    }

    @PostMapping("/sendOtp")
    public ResponseEntity<Map<String,String>> sendOtp(@RequestParam("phNo") String phNo){
        return twilioService.sendOtp(phNo);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String,String>> verifyOtp(@RequestBody VerifyUser verifyUser){
        return twilioService.verifyOtp(verifyUser);
    }
    @PutMapping("/resetPassword")
    public ResponseEntity<Map<String,String>> resetPassword(@RequestBody VerifyUser verifyUser){
        return twilioService.resetPass(verifyUser);
    }

}
