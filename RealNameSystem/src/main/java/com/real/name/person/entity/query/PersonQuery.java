package com.real.name.person.entity.query;

import com.real.name.person.entity.Person;
import com.real.name.subcontractor.entity.SubContractor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonQuery extends Person {

    private SubContractor subContractor;

}
