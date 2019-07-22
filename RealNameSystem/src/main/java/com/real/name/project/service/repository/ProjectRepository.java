package com.real.name.project.service.repository;

import com.real.name.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {

    Optional<Project> findByName(String projectName);

    @Override
    Page<Project> findAll(Pageable pageable);

    /**
     * 删除项目
     */
    @Modifying
    @Transactional
    int deleteByProjectCode(String projectCode);

    /**
     * 根据projectCode查询出项目名称
     */
    @Query(value = "select `name` from project where project_code = ?1", nativeQuery = true)
    String findProjectName(String projectCode);

    /**
     * 查询项目名和公司名
     */
    @Query(value = "select new com.real.name.project.entity.Project(p.name, p.contractorCorpName) from Project p where p.projectCode = ?1")
    Project findProNameAndCorp(String projectCode);

    /**
     * 获取所有项目的id
     */
    @Query("select projectCode from Project")
    List<String> findAllProjectCode();

}
