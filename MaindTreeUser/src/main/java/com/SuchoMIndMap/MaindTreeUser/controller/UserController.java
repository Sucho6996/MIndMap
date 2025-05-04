package com.SuchoMIndMap.MaindTreeUser.controller;

import com.SuchoMIndMap.MaindTreeUser.model.Content;
import com.SuchoMIndMap.MaindTreeUser.model.Topic;
import com.SuchoMIndMap.MaindTreeUser.model.Usuaria;
import com.SuchoMIndMap.MaindTreeUser.model.VerifyUser;
import com.SuchoMIndMap.MaindTreeUser.repo.UserRepo;
import com.SuchoMIndMap.MaindTreeUser.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    MyEncoding myEncoding;
    @Autowired
    UserRepo repo;

    @PostMapping("/signup")
    public ResponseEntity<Map<String,String>> signUp(@RequestBody Usuaria user){
        return userService.addUser(user);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> logIn(@RequestBody Usuaria user){
        Map<String,String> response=new HashMap<>();
        try {
            String s=myEncoding.encode(user.getName(), user.getPhNo(), user.getPass());
            Authentication auth= authenticationManager.
                    authenticate(new UsernamePasswordAuthenticationToken(user.getPhNo(),s));
            if(auth.isAuthenticated()){
                Usuaria u=repo.findByphNo(user.getPhNo());
                u.setLastLoginTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                //userService.addUser(user);
                repo.save(u);
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

    // From here on all the mapping are for accessing MindTreeService
    @PostMapping("/add-topic")
    public ResponseEntity<Map<String,String>> addTopic
    (@RequestHeader("Authorization") String authHeader,@RequestBody Topic topic){
        String token=authHeader.substring(7);
        return userService.addTopic(token,topic);
    }
    @GetMapping("/get-topics")
    public ResponseEntity<List<Topic>> getTopic(@RequestHeader("Authorization") String authHeader){
        String token=authHeader.substring(7);
        return userService.getTopic(token);
    }
    @PostMapping("/add-content")
    public ResponseEntity<Map<String,String>> addContent
            (@RequestParam("topicId") Long topicId,
             @RequestParam(value = "parentId",required = false) Long parentId,
             @RequestBody Content content){
        return userService.addContent(topicId,parentId,content);
    }
    @GetMapping("get-contents/{topicId}")
    public ResponseEntity<List<Content>> getContent(@PathVariable Long topicId){
        return userService.getContent(topicId);
    }
    @PutMapping("/isCompleted")
    public ResponseEntity<Map<String,String>> setAsCompleted
            (@RequestParam("topicId") long topicId,@RequestParam("contentId") long contentId){
        return userService.isComplete(topicId,contentId);
    }
    @DeleteMapping("/delete-content")
    public ResponseEntity<Map<String,String>> deleteContent
            (@RequestParam("topicId") long topicId,@RequestParam("contentId") long contentId){
        return userService.deleteContent(topicId,contentId);
    }
    @DeleteMapping("/delete-topic")
    public ResponseEntity<Map<String,String>> deleteTopic(@RequestParam long topicId){
        return userService.deleteTopic(topicId);
    }

}
