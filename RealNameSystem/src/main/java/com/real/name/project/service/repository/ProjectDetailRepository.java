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

    @Modifying
    @Transactional
    int deleteByProjectCode(String projectCode);

}
