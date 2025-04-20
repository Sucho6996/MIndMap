package com.SuchoMIndMap.MaindTreeUser.controller;

import com.SuchoMIndMap.MaindTreeUser.model.Usuaria;
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
        try {
            Authentication auth= authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(user.getPhNo(),user.getPass()));
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

}
