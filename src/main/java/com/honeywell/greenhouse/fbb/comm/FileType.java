package com.honeywell.greenhouse.fbb.comm;

public enum FileType {
    /**
     * 未知
     */
    NONE(0), 
    /**
     * 身份证 前
     */
    PERSON_ID_FT(1), 
    /**
     * 身份证 后
     */
    PERSON_ID_BK(2),
    /**
     * 营业执照
     */
    COMPANY_LICENSE(3),
    /**
     * 驾照
     */
    DRIVE_LICENSE(4),
    /**
     * 头像
     */
    PORTRAIT(5),
    /**
     * 名片
     */
    BUSINESS_CARD(6),
    /**
     * 行驶证
     */
    VEHICLE_LICENSE(7),
    /**
     * 人车合影照
     */
    DRIVER_VEHICLE_GROUP(8),
    /**
     * 交强险
     */
    COMPULSORY_INSURANCE(9),
    /**
     * 营运证，道路运输证
     */
    REGISTRATION(10),
    ;

    private final int value;

    /**
     * private constructor to forbidden instantiation.
     * 
     * @param value
     */
    FileType(int value) {
        this.value = value;
    }

    /**
     * @return the value which maps to database value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value
     *            The int value which represents the enum constant
     * @return The enum constant whose value is identical to given value.
     * @throws IllegalArgumentException
     *             if given value is not recognized.
     */
    public static FileType fromValue(int value) {
        for (FileType type : FileType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such enum value: " + value);
    }

    public static boolean isImage(Integer fileType) {
        FileType[] imageTypes = { PERSON_ID_FT, PERSON_ID_BK, COMPANY_LICENSE, DRIVE_LICENSE, PORTRAIT,
                BUSINESS_CARD, VEHICLE_LICENSE };
        if (fileType == null) {
            return false;
        }
        for (FileType imageType : imageTypes) {
            if (imageType.getValue() == fileType) {
                return true;
            }
        }
        return false;
    }
}
