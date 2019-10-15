package com.honeywell.greenhouse.fbb.core;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FddHelper {

    public static final String PARTY_A_SIGN_KEYWORD = "氧"; // 甲方
    public static final String PARTY_B_SIGN_KEYWORD = "氮"; // 乙方


    public static final String FONT_TYPE = "0"; //宋体
    public static final String FONT_SIZE = "12";

    public static final int INVOKE_REGISTER_ACCOUNT_SUCCESS = 1;
    public static final int INVOKE_COMPANY_VERIFY_URL_SUCCESS = 1;
    public static final int INVOKE_REJECT_SIGN_CONTRACT_SUCCESS = 1;
    public static final int INVOKE_SYNC_PERSON_AUTO_SUCCESS = 1000;
    public static final int INVOKE_GENERATE_CONTRACT_SUCCESS = 1000;
    public static final int INVOKE_COMPANY_AUTHORIZATION_SUCCESS = 1000; // Used in MyBatis
    public static final int INVOKE_COMPANY_AUTHORIZATION_SIGNATURE_SUCCESS = 1;
    public static final int INVOKE_ADD_SIGNATURE_SUCCESS = 1;
    public static final int INVOKE_QUERY_SIGNATURE_SUCCESS = 1;
    public static final int INVOKE_REPLACE_SIGNATURE_SUCCESS = 1;
    public static final int INVOKE_DEFAULT_SIGNATURE_SUCCESS = 1;
    public static final int INVOKE_REMOVE_SIGNATURE_SUCCESS = 1;
    public static final int INVOKE_PUSH_SIGN_SMS_SUCCESS = 1;


    public static boolean invokeRegisterAccountSuccess(Integer code) {
        return Integer.valueOf(INVOKE_REGISTER_ACCOUNT_SUCCESS).equals(code);
    }

    public static boolean invokeCompanyVerifyUrlSuccess(Integer code) {
        return Integer.valueOf(INVOKE_COMPANY_VERIFY_URL_SUCCESS).equals(code);
    }

    public static boolean invokePersonVerifyUrlSuccess(Integer code) {
        return false; // pending by spec
    }

    public static boolean invokeSyncPersonAutoSuccess(String code) {
        return String.valueOf(INVOKE_SYNC_PERSON_AUTO_SUCCESS).equals(code);
    }

    public static boolean invokeApplyCertSuccess(Integer code) {
        return false; // pending by spec
    }

    public static boolean invokeApplyNumcertSuccess(Integer code) {
        return false; // pending by spec
    }

    public static boolean invokeAddSignatureSuccess(Integer code) {
        return Integer.valueOf(INVOKE_ADD_SIGNATURE_SUCCESS).equals(code); // pending by spec
    }

    public static boolean invokeUploadTemplateSuccess(Integer code) {
        return false; // pending by spec
    }

    public static boolean invokeGenerateContractSuccess(Integer code) {
        return Integer.valueOf(INVOKE_GENERATE_CONTRACT_SUCCESS).equals(code);
    }

    public static boolean invokeUploadDocsSuccess(Integer code) {
        return false; // pending by spec
    }

    public static boolean invokeExtSignAutoSuccess(Integer code) {
        return false; // pending by spec
    }

    public static boolean invokeFindPersonCertSuccess(Integer code) {
        return code.equals(1); // pending by spec
    }

    public static boolean invokeCompanyAuthorizationSuccess(Integer code) {
        return Integer.valueOf(INVOKE_COMPANY_AUTHORIZATION_SUCCESS).equals(code);
    }

    public static boolean invokeRejectSignContractSuccess(Integer code) {
        return Integer.valueOf(INVOKE_REJECT_SIGN_CONTRACT_SUCCESS).equals(code);
    }

    public static boolean invokePushSignSmsSuccess(Integer code) {
        return Integer.valueOf(INVOKE_PUSH_SIGN_SMS_SUCCESS).equals(code);
    }

    public static boolean invokeQuerySignatureSuccess(Integer code) {
        return Integer.valueOf(INVOKE_QUERY_SIGNATURE_SUCCESS).equals(code);
    }

    public static boolean invokeDefaultSignatureSuccess(Integer code) {
        return Integer.valueOf(INVOKE_DEFAULT_SIGNATURE_SUCCESS).equals(code);
    }

    public static boolean invokeReplaceSignatureSuccess(Integer code) {
        return Integer.valueOf(INVOKE_REPLACE_SIGNATURE_SUCCESS).equals(code);
    }

    public static boolean invokeRemoveSignatureSuccess(Integer code) {
        return Integer.valueOf(INVOKE_REMOVE_SIGNATURE_SUCCESS).equals(code);
    }

    public static boolean invokeCompanyAuthorizationSignatureSuccess(Integer code) {
        return Integer.valueOf(INVOKE_COMPANY_AUTHORIZATION_SIGNATURE_SUCCESS).equals(code);
    }
}
