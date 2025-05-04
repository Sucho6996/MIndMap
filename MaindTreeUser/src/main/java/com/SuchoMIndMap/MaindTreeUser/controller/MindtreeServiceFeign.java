package com.SuchoMIndMap.MaindTreeUser.controller;

import com.SuchoMIndMap.MaindTreeUser.model.Content;
import com.SuchoMIndMap.MaindTreeUser.model.Topic;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient("MINDTREESERVICE")
public interface MindtreeServiceFeign {
    @PostMapping("/mindMap/add-topic")
    public ResponseEntity<Map<String,String>> addTopic(@RequestParam String phNo, @RequestBody Topic topic);
    @GetMapping("/mindMap/get-topics")
    public ResponseEntity<List<Topic>> getTopic(@RequestParam String phNo);
    @PostMapping("/mindMap/add-content")
    public ResponseEntity<Map<String,String>> addContent
            (@RequestParam("topicId") Long topicId,
             @RequestParam(value = "parentId",required = false) Long parentId,
             @RequestBody Content content);
    @GetMapping("/mindMap/get-contents/{topicId}")
    public ResponseEntity<List<Content>> getContent(@PathVariable Long topicId);
    @PutMapping("/mindMap/isCompleted")
    public ResponseEntity<Map<String,String>> setAsCompleted
            (@RequestParam("topicId") long topicId,@RequestParam("contentId") long contentId);
    @DeleteMapping("/mindMap/delete-content")
    public ResponseEntity<Map<String,String>> deleteContent
            (@RequestParam("topicId") long topicId,@RequestParam("contentId") long contentId);
    @DeleteMapping("/mindMap/delete-topic")
    public ResponseEntity<Map<String,String>> deleteTopic(@RequestParam long topicId);

}
