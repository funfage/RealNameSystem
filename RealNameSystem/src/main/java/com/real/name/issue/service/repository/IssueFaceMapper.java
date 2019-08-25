package com.real.name.issue.service.repository;

import com.real.name.issue.entity.IssueFace;
import com.real.name.person.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IssueFaceMapper {

    /**
     * 查询人员下发失败的信息
     * @return 下发失败人员信息
     */
    List<IssueFace> findAllFaceIssuePersonFailure();

    /**
     * 查询人员照片下发失败信息
     */
    List<IssueFace> findAllFaceIssueImageFailure();

    /**
     * 根据personId修改信息
     */
    int updateByPersonIdAndDeviceId(IssueFace issueFace);

    /**
     * 插入初始信息
     */
    int insertInitIssue(IssueFace issueFace);

    /**
     * 将人员在某个设备的下发标识删除
     */
    int deleteStatusByPersonInDevice(@Param("personId") Integer personId,
                                     @Param("deviceId") String deviceId);

    /**
     * 查询所有下发失败设备的id值
     */
    List<String> findAllFailDeviceIds();

    /**
     *查询某个项目下所有下发失败的设备id
     */
    List<String> findAllFailDeviceIdsByProjectCode(String projectCode);

    /**
     * 查询某个项目下所有下发失败的信息
     */
    List<IssueFace> findIssueFailPersonInfoByProjectCode(String projectCode);

    /**
     * 查某个设备所有下发失败的信息
     */
    List<IssueFace> findIssueFailPersonInfoByDeviceId(String deviceId);

    /**
     * 查询某个人员是否有存在下发失败
     */
    List<IssueFace> findIssueFailPersonByPersonId(Integer personId);

    /**
     * 查询所有更新设备的人员信息
     */
    List<IssueFace> findUpdateFailPersonByWorkRole(Integer workRole);
}
