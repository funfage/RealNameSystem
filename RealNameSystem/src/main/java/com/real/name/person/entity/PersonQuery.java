package com.real.name.person.entity;

public class PersonQuery {

    private String nameOrIDCard;

    private Integer gender;

    private String nation;

    private Integer startAge;

    private Integer endAge;

    public PersonQuery(String nameOrIDCard, Integer gender, String nation, Integer startAge, Integer endAge) {
        this.nameOrIDCard = nameOrIDCard;
        this.gender = gender;
        this.nation = nation;
        this.startAge = startAge;
        this.endAge = endAge;
    }

    public PersonQuery() {
    }

    public String getNameOrIDCard() {
        return nameOrIDCard;
    }

    public void setNameOrIDCard(String nameOrIDCard) {
        this.nameOrIDCard = nameOrIDCard;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Integer getStartAge() {
        return startAge;
    }

    public void setStartAge(Integer startAge) {
        this.startAge = startAge;
    }

    public Integer getEndAge() {
        return endAge;
    }

    public void setEndAge(Integer endAge) {
        this.endAge = endAge;
    }

    @Override
    public String toString() {
        return "PersonQuery{" +
                "nameOrIDCard='" + nameOrIDCard + '\'' +
                ", gender=" + gender +
                ", nation='" + nation + '\'' +
                ", startAge=" + startAge +
                ", endAge=" + endAge +
                '}';
    }
}
