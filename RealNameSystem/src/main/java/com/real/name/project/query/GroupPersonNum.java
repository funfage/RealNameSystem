package com.real.name.project.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GroupPersonNum {

    private String teamName;

    private String num;

    public GroupPersonNum() {
    }

    public GroupPersonNum(String teamName, String num) {
        this.teamName = teamName;
        this.num = num;
    }
}
