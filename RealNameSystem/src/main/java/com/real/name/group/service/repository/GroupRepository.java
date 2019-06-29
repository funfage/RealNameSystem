package com.real.name.group.service.repository;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<WorkerGroup, Integer> {

//    Optional<Group> findByTeamName(String teamName);

      Optional<WorkerGroup> findByTeamName(String teamName);

      List<WorkerGroup> findByProjectCode(String projectCode);


      @Modifying
      @Transactional
      int deleteByTeamSysNo(Integer teamSysNo);
}