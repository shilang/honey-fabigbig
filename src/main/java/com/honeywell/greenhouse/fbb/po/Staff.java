package com.honeywell.greenhouse.fbb.po;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Staff {
//    a.id,"name",mob_no,person_id,ca_customer_id,b.employee_no
    private String userId;
    private String name;
    private String mobNo;
    private String personId;
    private String caCustomerId;
    private String employeeNo;
}
