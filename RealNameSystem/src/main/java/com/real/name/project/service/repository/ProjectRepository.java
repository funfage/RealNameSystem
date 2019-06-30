package com.real.name.project.service.repository;

import com.real.name.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {

    Optional<Project> findByName(String projectName);

    @Override
    Page<Project> findAll(Pageable pageable);

    /**
     * 删除项目
     * @param projectCode
     * @return
     */
    @Modifying
    @Transactional
    int deleteByProjectCode(String projectCode);


}
