package com.real.name.project.service.repository;

import com.real.name.project.entity.ProjectDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Integer> {

    Optional<ProjectDetail> findByProjectCodeAndPersonId(String projectId, Integer personId);

    Optional<ProjectDetail> findByTeamSysNoAndPersonId(Integer teamSysNo, Integer personId);

    Page<ProjectDetail> findByProjectCode(String projectId, Pageable pageable);

    Optional<ProjectDetail> findByPersonId(Integer personId);

    /**
     * 根据projectCode删除
     * @param projectCode 项目编号
     * @return 影响的行数
     */
    @Modifying
    @Transactional
    int deleteByProjectCode(String projectCode);


    /**
     * 根据TeamSYSNo删除
     * @param TeamSysNo 班组编号
     * @return 影响的行数
     */
    @Modifying
    @Transactional
    int deleteByTeamSysNo(Integer TeamSysNo);

    /**
     * 根据personId删除
     * @param personId
     * @return
     */
    @Modifying
    @Transactional
    int deleteByPersonId(Integer personId);
}
