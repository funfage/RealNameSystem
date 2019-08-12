package com.real.name.group.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.group.entity.WorkerGroup;
import com.real.name.subcontractor.entity.SubContractor;
import lombok.Data;


@Data
public class GroupQuery extends WorkerGroup {

    private SubContractor subContractor;

    @JsonIgnore
    private Integer pageNum = 0;

    @JsonIgnore
    private Integer pageSize = 10;

}
