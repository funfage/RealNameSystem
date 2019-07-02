package com.real.name.project.service.repository;

import com.real.name.project.entity.IssueDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssueDetailRepository extends JpaRepository<IssueDetail, Integer> {
    @Query(value = "select s.issue_id from issue_detail s where s.issue_person_status = ?1", nativeQuery = true)
    List<Integer> findIdByIssuePersonStatus(Integer issuePersonStatus);

    @Query(value = "select s.issue_id from issue_detail s where s.issue_image_status = ?1", nativeQuery = true)
    List<Integer> findIdByIssueImageStatus(Integer issueImageStatus);

    @Query(value = "select s.issue_id from issue_detail s where s.issue_image_status = ?1 and s.issue_person_status = ?2", nativeQuery = true)
    List<Integer> findIdByIssueStatus(Integer issuePersonStatus, Integer issueImageStatus);
}
