package com.real.name.nation.entity;


//import com.sun.xml.internal.ws.api.message.Attachment;
import lombok.Data;

import java.util.List;

/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/16 3:34
 **/
@Data
public class Training {
    private String projectCode;
    private String trainingDate;
    private Double trainingDuration;
    private String trainingName;
    private String trainingTypeCode;
    private String trainer;
    private String trainingOrg;
    private String trainingAddres;
    private String description;
//    private List<Attachment> attachments;
    private List<Worker> workers;
}
