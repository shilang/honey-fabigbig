package com.honeywell.greenhouse.fbb.comm;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUrlHandler {

    public static String getUserFileDownloadPublicUri(String head, Integer userId, int fileId, String fid) {
        if (StringUtils.isEmpty(fid)) {
            return fid;
        }
        if (userId == null) {
            throw new NullPointerException();
        }
        String sign = Generators.genHash(userId, fileId, fid, 0); //Signature used in url
        StringBuilder sb = new StringBuilder();
        sb.append(head).append(userId).append("/")
            .append(fileId).append("/")
            .append(sign).append("/")
            .append(fid);
        return sb.toString();
    }

    public static String getAuthUserFileDownloadPublicUri(String head, Integer userId, 
            Integer authorizedUserId, int fileId, String fid) {
        if (StringUtils.isEmpty(fid)) {
            return fid;
        }
        if (userId == null) {
            throw new NullPointerException();
        }
        String sign = Generators.genHash(userId, authorizedUserId, fileId, fid, 0); //Signature used in url
        StringBuilder sb = new StringBuilder();
        sb.append(head).append(userId).append("/")
            .append(authorizedUserId).append("/")
            .append(fileId).append("/")
            .append(sign).append("/")
            .append(fid);
        return sb.toString();
    }

    public static String getAttDownloadPublicUri(String head, int attachmentId, String fid) {
        if (StringUtils.isEmpty(fid)) {
            return fid;
        }
        String sign = Generators.genHash(attachmentId, 0);// 0 is timestamp
        StringBuilder sb = new StringBuilder();
        sb.append(head).append(attachmentId).append("/").append(sign)
            .append("/").append(fid);
        return sb.toString();
    }

    public static String getGeneralFileDownloadPublicUri(String head, Integer id, String fid) {
        if (StringUtils.isEmpty(fid)) {
            return fid;
        }
        if (id == null) {
            id = 0;
        }
        String sign = Generators.genHash(id, fid, 0);// 0 is timestamp
        StringBuilder sb = new StringBuilder();
        sb.append(head).append(id).append("/").append(sign).append("/").append(fid);
        return sb.toString();
    }

    public static String getVerDownloadPublicUri(String head, int appType, int deviceType, String fid) {
        if (StringUtils.isEmpty(fid)) {
            return fid;
        }
        String sign = Generators.genHash(appType, deviceType, 0);
        StringBuilder sb = new StringBuilder();
        sb.append(head).append(appType).append("/").append(deviceType).
            append("/").append(sign).append("/").append(fid);
        return sb.toString();
    }
}
