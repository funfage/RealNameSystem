package com.real.name.group.entity.query;

import com.real.name.group.entity.WorkerGroup;
import com.real.name.subcontractor.entity.SubContractor;
import lombok.Data;


@Data
public class GroupQuery extends WorkerGroup {

    private SubContractor subContractor;

}
