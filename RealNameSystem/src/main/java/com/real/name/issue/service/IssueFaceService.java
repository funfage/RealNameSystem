package com.real.name.issue.service;

import com.real.name.issue.entity.IssueFace;

import java.util.List;

public interface IssueFaceService {

    /**
     * 根据personId修改信息
     */
    public int updateByPersonIdAndDeviceId(IssueFace issueFace);

    public int insertInitIssue(IssueFace issueFace);

    /**
     * 查询所有下发失败设备的id值
     * @return
     */
    List<String> findAllFailDeviceIds();

    /**
     * 查询某个项目下所有下发失败的设备id
     * @param projectCode
     * @return
     */
    List<String> findAllFailDeviceIdsByProjectCode(String projectCode);

    /**
     * 查询某个项目下所有下发失败的信息
     * @param projectCode
     * @return
     */
    List<IssueFace> findIssueFailPersonInfoByProjectCode(String projectCode);

    /**
     * 查某个设备所有下发失败的信息
     * @param deviceId
     * @return
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
