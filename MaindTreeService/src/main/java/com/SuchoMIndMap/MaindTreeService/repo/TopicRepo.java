package com.SuchoMIndMap.MaindTreeService.repo;

import com.SuchoMIndMap.MaindTreeService.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepo extends JpaRepository<Topic,Long> {
    List<Topic> findAllByPhNo(String phNo);
}
