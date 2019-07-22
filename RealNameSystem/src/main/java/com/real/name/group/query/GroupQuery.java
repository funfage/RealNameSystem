package com.real.name.group.query;

import com.real.name.group.entity.WorkerGroup;
import lombok.Data;


@Data
public class GroupQuery extends WorkerGroup {

    private Integer pageNum = 0;

    private Integer pageSize = 10;

}
