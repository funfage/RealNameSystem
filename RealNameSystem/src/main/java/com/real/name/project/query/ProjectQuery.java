package com.real.name.project.query;

import com.real.name.project.entity.Project;
import lombok.Data;

@Data
public class ProjectQuery extends Project {

    private Integer pageNum = 0;

    private Integer pageSize = 10;

}
