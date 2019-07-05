package com.real.name.project.service.repository;

import com.real.name.project.entity.IssueDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IssueDetailRepository extends JpaRepository<IssueDetail, Integer> {
    @Query(value = "select s.issue_id from issue_detail s where s.issue_person_status = ?1", nativeQuery = true)
    List<Integer> findIdByIssuePersonStatus(Integer issuePersonStatus);

    @Query(value = "select s.issue_id from issue_detail s where s.issue_image_status = ?1", nativeQuery = true)
    List<Integer> findIdByIssueImageStatus(Integer issueImageStatus);

    @Query(value = "select s.issue_id from issue_detail s where s.issue_image_status = ?1 and s.issue_person_status = ?2", nativeQuery = true)
    List<Integer> findIdByIssueStatus(Integer issuePersonStatus, Integer issueImageStatus);

    @Query(value = "select s.issue_id, s.person_id, s.project_code, s.issue_person_status, s.issue_image_status, d.device_id, d.ip, d.out_port from issue_detail s, device d " +
            "where s.person_id = ?1 and s.device_id = ?2 and s.project_code = ?3 and s.device_id = d.device_id", nativeQuery = true)
    List<IssueDetail> findByCondition(Integer personId, String deviceId, String projectCode);

    @Query(value = "select s.issue_id, s.person_id, s.project_code, s.issue_person_status, s.issue_image_status, d.device_id, d.ip, d.out_port, d.project_code from issue_detail s, device d " +
            "where s.device_id = ?1 and s.project_code = ?2 " +
            "and s.device_id = d.device_id and (s.issue_image_status != ?3 or s.issue_person_status != ?4)", nativeQuery = true)
    List<IssueDetail> findByCondition(String deviceId, String projectCode, Integer issuePersonStatus, Integer issueImageStatus);

    @Modifying
    @Transactional
    @Query("update IssueDetail i set i.issuePersonStatus = ?1, i.issueImageStatus = ?2 where i.issueId = ?3")
    int updateIssueStatus(Integer issuePersonStatus, Integer issueImageStatus, Long issueId);

    @Modifying
    @Transactional
    int deleteByIssueIdIn(List<Long> issueIds);

}
