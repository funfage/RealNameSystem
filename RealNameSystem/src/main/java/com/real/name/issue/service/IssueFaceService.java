package com.real.name.issue.service;

import com.real.name.device.entity.Device;
import com.real.name.issue.entity.IssueFace;
import com.real.name.issue.entity.IssuePersonStatus;
import com.real.name.person.entity.Person;

import java.util.List;

public interface IssueFaceService {

    /**
     * 重发
     */
    void resend(int issuePersonStatus, int issueImageStatus, Person person, Device device);

    /**
     * 根据personId修改信息
     */
    int updateByPersonIdAndDeviceId(IssueFace issueFace);

    /**
     * 插入下发信息
     */
    int insertInitIssue(IssueFace issueFace);

    /**
     * 查询所有下发失败设备的id值
     */
    List<String> findAllFailDeviceIds();

    /**
     * 查询某个项目下所有下发失败的设备id
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
