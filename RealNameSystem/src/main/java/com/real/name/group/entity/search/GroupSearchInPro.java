package com.real.name.group.entity.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.group.entity.WorkerGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupSearchInPro extends WorkerGroup {

    @JsonIgnore
    private Integer pageNum = 0;

    @JsonIgnore
    private Integer pageSize = 10;
}
