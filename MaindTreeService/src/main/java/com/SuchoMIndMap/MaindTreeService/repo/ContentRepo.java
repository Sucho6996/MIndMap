package com.SuchoMIndMap.MaindTreeService.repo;

import com.SuchoMIndMap.MaindTreeService.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepo extends JpaRepository<Content,Long> {
    List<Content> findAllByTopicId(Long topicId);

    Content findByTopicIdAndContentId(long topicId, long contentId);

    boolean existsByParentId(long contentId);
}
