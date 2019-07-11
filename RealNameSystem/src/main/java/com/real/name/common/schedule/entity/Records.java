package com.real.name.common.schedule.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Records {

    /**
     * 识别记录 id，由设备自动生成并管理。用户无须使用
     */
    private Integer id;

    /**
     * //现场抓拍照片存储在设备内的路径。当设备内现场照存储满
     * 3G 时，会自动删除较早的 1G 现场照；若有需要请及时存储
     */
    private String path;

    /**
     * 人员 id。陌生人/识别失败显示 id 为 STRANGERBABY
     */
    private String personId;

    /**
     * /回调结果。0：回调失败，1：回调成功或未设置回调地址
     */
    private Integer state;

    /**
     * 识别成功时的设备时间，Unix 毫秒时间戳
     */
    private Long time;

    /**
     * 识别出的人员类型，0：时间段内，1：时间段外，2：陌生人/识别失败
     */
    private Integer type;

    public Records() {
    }

    public Records(Integer id, String path, String personId, Integer state, Long time, Integer type) {
        this.id = id;
        this.path = path;
        this.personId = personId;
        this.state = state;
        this.time = time;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Records{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", personId='" + personId + '\'' +
                ", state=" + state +
                ", time=" + time +
                ", type=" + type +
                '}';
    }
}
