package com.real.name.nation.query;

import com.real.name.group.entity.WorkerGroup;
import lombok.Data;

@Data
public class TeamQuery extends WorkerGroup {
    private String projectCode;
}
