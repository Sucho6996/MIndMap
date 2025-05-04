package com.SuchoMIndMap.MaindTreeService.controller;

import com.SuchoMIndMap.MaindTreeService.model.Content;
import com.SuchoMIndMap.MaindTreeService.model.Topic;
import com.SuchoMIndMap.MaindTreeService.service.MinMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mindMap")
public class MindMapController {
    @Autowired
    MinMapService service;

    @PostMapping("/add-topic")
    public ResponseEntity<Map<String,String>> addTopic(@RequestParam String phNo, @RequestBody Topic topic){
        topic.setPhNo(phNo);
        return service.addTopic(topic);
    }
    @GetMapping("/get-topics")
    public ResponseEntity<List<Topic>> getTopic(@RequestParam String phNo){
        return service.getTopic(phNo);
    }
    @PostMapping("/add-content")
    public ResponseEntity<Map<String,String>> addContent
            (@RequestParam("topicId") Long topicId,
             @RequestParam(value = "parentId",required = false) Long parentId,
             @RequestBody Content content){
        return service.addContent(topicId,parentId,content);
    }
    @GetMapping("/get-contents/{topicId}")
    public ResponseEntity<List<Content>> getContent(@PathVariable Long topicId){
        return service.getContent(topicId);
    }
    @PutMapping("isCompleted")
    public ResponseEntity<Map<String,String>> setAsCompleted
            (@RequestParam("topicId") long topicId,@RequestParam("contentId") long contentId){
        return service.setAsCompleted(topicId,contentId);
    }
    @DeleteMapping("/delete-content")
    public ResponseEntity<Map<String,String>> deleteContent
            (@RequestParam("topicId") long topicId,@RequestParam("contentId") long contentId){
        return service.deleteContent(topicId,contentId);
    }
    @DeleteMapping("/delete-topic")
    public ResponseEntity<Map<String,String>> deleteTopic(@RequestParam long topicId){
        return service.deleteTopic(topicId);
    }
}
