package com.honeywell.greenhouse.fbb.po;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Signature {
    //project_group,customer_id,signature_id,remark ,created_at
    private String projectGroup;
    private String customerId;
    private String signatureId;
    private String remark;

}
