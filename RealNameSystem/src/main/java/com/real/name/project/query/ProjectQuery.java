package com.real.name.project.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.real.name.project.entity.Project;
import lombok.Data;

@Data
public class ProjectQuery extends Project {

    @JsonIgnore
    private Integer pageNum = 0;

    @JsonIgnore
    private Integer pageSize = 10;

}
