package com.real.name.project.service.repository;

import com.real.name.project.entity.ProjectDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectDetailRepository extends JpaRepository<ProjectDetail, Integer> {

    Page<ProjectDetail> findByProjectCode(String projectId, Pageable pageable);

    Optional<ProjectDetail> findByProjectCodeAndPersonIdAndTeamSysNo(String projectId, Integer personId, Integer teamSysNo);

    @Query("from com.real.name.project.entity.ProjectDetail")
    List<ProjectDetail> findAll();

}
