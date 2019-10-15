package com.honeywell.greenhouse.fbb.comm;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseObject {

    public static final BaseObject DUMMY = new BaseObject();

    @Override
    public String toString() {
        try {
            return JSONUtils.toJSONString(this, true);
        } catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
            // fall back to safe reflection to string.
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

}
