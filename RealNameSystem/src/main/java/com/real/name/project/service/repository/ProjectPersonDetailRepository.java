package com.real.name.project.service.repository;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.person.entity.Person;
import com.real.name.project.entity.Project;
import com.real.name.project.entity.ProjectPersonDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectPersonDetailRepository extends JpaRepository<ProjectPersonDetail, Integer> {
    Optional<ProjectPersonDetail> findByPerson(Person person);

    /**
     * 根据personId查询
     * @param personId
     * @return
     */
    Optional<ProjectPersonDetail> findByPerson_PersonId(Integer personId);

    /**
     * 根据person删除
     * @param person
     * @return
     */
    @Modifying
    @Transactional
    int deleteByPerson(Person person);

    /**
     * 根据project删除
     * @param project
     * @return
     */
    @Modifying
    @Transactional
    int deleteByProject(Project project);

    /**
     * 根据workerGroup删除
     * @param workerGroup 班组编号
     * @return 影响的行数
     */
    @Modifying
    @Transactional
    int deleteByWorkerGroup(WorkerGroup workerGroup);

   /* @Query("select pd.createTime, wg from ProjectPersonDetail pd, WorkerGroup wg where pd.person.personId = ")
    List<ProjectPersonDetail> findPersonAndWorkerGroupInfo(List<Integer> personIds);*/
}
