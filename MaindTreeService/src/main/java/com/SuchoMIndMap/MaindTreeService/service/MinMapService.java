package com.SuchoMIndMap.MaindTreeService.service;

import com.SuchoMIndMap.MaindTreeService.model.Content;
import com.SuchoMIndMap.MaindTreeService.model.Topic;
import com.SuchoMIndMap.MaindTreeService.repo.ContentRepo;
import com.SuchoMIndMap.MaindTreeService.repo.TopicRepo;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MinMapService {
    @Autowired
    TopicRepo topicRepo;
    @Autowired
    ContentRepo contentRepo;
    public ResponseEntity<Map<String, String>> addTopic(Topic topic) {
        Map<String,String> response=new HashMap<>();
        try {
            topicRepo.save(topic);
            response.put("message", "Topic added Successfully");
        }
        catch (Exception e){
            response.put("message",e.getMessage());
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public ResponseEntity<List<Topic>> getTopic(String phNo) {
        List<Topic> topics=new ArrayList<>();
        try {
            topics=topicRepo.findAllByPhNo(phNo);
            return new ResponseEntity<>(topics,HttpStatus.OK);
        }
        catch (NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Map<String, String>> addContent(Long topicId,Long parentId ,Content content) {
        Map<String,String> response=new HashMap<>();
        if(!topicRepo.existsById(topicId) || !contentRepo.existsById(parentId)) {
            response.put("message","Topic not Exist");
            return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
        }
        try {
            content.setTopicId(topicId);
            content.setParentId(parentId);
            contentRepo.save(content);
            response.put("message","Content added successfully");
        }
        catch (Exception e){
            response.put("message",e.getMessage());
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public ResponseEntity<List<Content>> getContent(Long topicId) {
        if (!topicRepo.existsById(topicId)) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<Content> contents=new ArrayList<>();
        try {
            contents=contentRepo.findAllByTopicId(topicId);
            return new ResponseEntity<>(contents,HttpStatus.OK);
        }
        catch (Exception e){
            contents.add(new Content());
            return new ResponseEntity<>(contents,HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<String, String>> deleteContent(long topicId, long contentId) {
        Content content=contentRepo.findByTopicIdAndContentId(topicId,contentId);
        Map<String,String> response=new HashMap<>();
        if(contentRepo.existsByParentId(contentId)){//A topic with sub topic can't be deleted
            response.put("message","A topic with sub topic can't be deleted");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        try {
            contentRepo.delete(content);
            response.put("message","Successfully deleted");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e){
            response.put("message",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
    }

    public ResponseEntity<Map<String, String>> deleteTopic(long topicId) {
        Optional<Topic> topic=topicRepo.findById(topicId);
        Map<String,String> response=new HashMap<>();
        if(topic.isEmpty()){
            response.put("message","No topic found");
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        try {
            List<Content> contents=getContent(topicId).getBody();
            for(Content content:contents) deleteContent(topicId,content.getContentId());
            topicRepo.delete(topic.get());
            response.put("message","Topic Successfully deleted");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        catch (Exception e){
            response.put("message",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
    }

    public ResponseEntity<Map<String, String>> setAsCompleted(long topicId, long contentId) {
        Content content=contentRepo.findByTopicIdAndContentId(topicId,contentId);
        Map<String,String> response=new HashMap<>();
        if(content.isCompleted()){
            response.put("message","Already marked as completed");
        }
        else {
            content.setCompleted(true);
            contentRepo.save(content);
            response.put("message","Marked as true");
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
