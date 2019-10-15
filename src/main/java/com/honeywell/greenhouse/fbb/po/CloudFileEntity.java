package com.honeywell.greenhouse.fbb.po;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CloudFileEntity {
    private Long orderId;
    private String orderNoTpl;
    private Long attachmentId;
    private String fid;
    private String fileName;
}
