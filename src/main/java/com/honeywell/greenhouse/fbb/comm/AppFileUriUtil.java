package com.honeywell.greenhouse.fbb.comm;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppFileUriUtil {
    /*
     * 此工具用于生成app端访问图片的URI
     */
    
    
    private static final String USER_URI_HEAD = FileUriConsts.APP_FILE_URI_PREFIX + FileUriConsts.USER_FILE_URI + "/";
    
    private static final String AUTHUSER_URI_HEAD = FileUriConsts.APP_FILE_URI_PREFIX + FileUriConsts.AUTHUSER_FILE_URI + "/";
    
    private static final String ATTACHMENT_URI_HEAD = FileUriConsts.APP_FILE_URI_PREFIX + FileUriConsts.ATTACHMENT_FILE_URI + "/";
    
    private static final String GENERAL_URI_HEAD = FileUriConsts.APP_FILE_URI_PREFIX + FileUriConsts.GENERAL_FILE_URI + "/";
    
    private static final String NEWAPP_URI_HEAD = FileUriConsts.APP_FILE_URI_PREFIX + FileUriConsts.NEWAPP_FILE_URI + "/";
    
    /*
     * 生成查看附件图片的URI
     * attachmentId: 附件表的ID
     * fid: 图片对应的文件服务器的fid
     */
    public static String genAttDownloadPublicUri(int attachmentId, String fid) {
        return FileUrlHandler.getAttDownloadPublicUri(ATTACHMENT_URI_HEAD, attachmentId, fid);
    }

    /*
     * 生成查看用户头像图片的URI
     * userId: 头像所属用户的用户ID
     * fid: 图片对应的文件服务器的fid
     */
    public static String genAvatarDownloadPublicUri(Integer userId, String fid) {
        return genUserFileDownloadPublicUri(userId, FileType.PORTRAIT.getValue(), fid);
    }

    /*
     * 生成查看用户个人图片的URI，除了头像，其他个人图片只允许本人查看
     * userId: 图片所属用户的用户ID
     * fileId: 文件类型的ID, FileType的Enum值
     * fid: 图片对应的文件服务器的fid
     */
    public static String genUserFileDownloadPublicUri(Integer userId, int fileId, String fid) {
        return FileUrlHandler.getUserFileDownloadPublicUri(USER_URI_HEAD, userId, fileId, fid);
    }

    /*
     * 生成查看用户个人图片的URI，此接口同时允许授权用户查看他人图片
     * userId: 图片所属用户的用户ID
     * authorizedUserId: 授权用户ID, 此用户可以查看userId的个人图片
     * fileId: 文件类型的ID, FileType的Enum值
     * fid: 图片对应的文件服务器的fid
     */
    public static String genAuthUserFileDownloadPublicUri(Integer userId, Integer authorizedUserId, int fileId, String fid) {
        return FileUrlHandler.getAuthUserFileDownloadPublicUri(AUTHUSER_URI_HEAD, userId, authorizedUserId, fileId, fid);
    }

    /*
     * 生成下载新app版本的uri链接
     * appType: 应用类型, AppType
     * deviceType: 设备类型, DeviceType
     * fid: app镜像对应的文件服务器的fid
     */
    public static String genVerDownloadPublicUri(int appType, int deviceType, String fid) {
        return FileUrlHandler.getVerDownloadPublicUri(NEWAPP_URI_HEAD, appType, deviceType, fid);
    }

    /*
     * 生成通用文件查看URI。此接口生产的URI无需身份验证（无token需求）。只能用于低安文件图片
     * id: 建议值：0或者此fid在数据表web_file_item中对应的ID
     * fid: 图片对应的文件服务器的fid
     */
    public static String genGeneralDownloadPublicUri(Integer id, String fid) {
        return FileUrlHandler.getGeneralFileDownloadPublicUri(GENERAL_URI_HEAD, id, fid);
    }
}
