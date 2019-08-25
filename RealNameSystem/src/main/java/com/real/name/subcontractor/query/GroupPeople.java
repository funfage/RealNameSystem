package com.real.name.subcontractor.query;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupPeople {

    /**
     * 班组编号
     */
    private Integer teamSysNo;

    /**
     * 班组下的人员id集合
     */
    private List<Integer> personIds;

}
