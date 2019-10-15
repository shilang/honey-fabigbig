package com.honeywell.greenhouse.fbb.core;

import java.io.File;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.authForfadada.ApplyCert;
import com.fadada.sdk.client.authForfadada.ApplyNumCert;
import com.fadada.sdk.client.authForfadada.FindCertInfo;
import com.fadada.sdk.client.authForfadada.GetCompanyVerifyUrl;
import com.fadada.sdk.client.authForfadada.GetPersonVerifyUrl;
import com.fadada.sdk.client.authForfadada.model.AgentInfoINO;
import com.fadada.sdk.client.authForfadada.model.BankInfoINO;
import com.fadada.sdk.client.authForfadada.model.CompanyInfoINO;
import com.fadada.sdk.client.authForfadada.model.LegalInfoINO;
import com.fadada.sdk.client.request.ExtsignReq;
import com.honeywell.greenhouse.fbb.comm.JSONUtils;
import com.honeywell.greenhouse.fbb.nop.CompanyAuthorization;
import com.honeywell.greenhouse.fbb.nop.ExtSignWithSignatureReq;
import com.honeywell.greenhouse.fbb.nop.PushShortSmsReq;
import com.honeywell.greenhouse.fbb.nop.PushShortUrlSmsClient;
import com.honeywell.greenhouse.fbb.nop.RejectSignContract;
import com.honeywell.greenhouse.fbb.nop.SignClient;
import com.honeywell.greenhouse.fbb.nop.SignatureClient;
import com.honeywell.greenhouse.fbb.response.GenerateContractRes;
import com.honeywell.greenhouse.fbb.response.VerifyCAResponse;


import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FddManager {

    @Autowired
    private FddClientFactory fddClientFactory;
    /**
     * 注册账号
     *
     * @param openId
     * @param accountType
     * @return
     */
    public FddResponse accountRegister(String openId, String accountType) {
        log.info("AccountRegister request {} {}", openId, accountType);
        FddClientBase fddClient = fddClientFactory.create(FddClientBase.class);
        String result = fddClient.invokeregisterAccount(openId, accountType);
        log.info("AccountRegister response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeRegisterAccountSuccess(code));
    }

    /**
     * 获取企业实名认证地址
     *
     * @param customerId  客户编号
     * @param verifyedWay 实名认证套餐类型 [0：标准方案（对公打款+纸质审核）默认 0；1：对公打款；2：纸质审核]
     * @param notifyUrl   回调地址
     * @param returnUrl   同步通知url
     * @return
     */
    public FddResponse getCompanyVerifyUrl(String customerId, String verifyedWay, String notifyUrl, String returnUrl) {
        log.info("GetCompanyVerifyUrl request {} {} {} {}", customerId, verifyedWay, notifyUrl, returnUrl);
        CompanyInfoINO companyInfo = new CompanyInfoINO();//企业信息
        companyInfo.setCompany_name("");//企业名称
        companyInfo.setCredit_no("");//统一社会信用代码
        companyInfo.setCredit_image_path("");//统一社会信用代码证件照路径

        BankInfoINO bankInfo = new BankInfoINO();//对公账号信息
        bankInfo.setBank_name("");//银行名称
        bankInfo.setBank_id("");//银行帐号
        bankInfo.setSubbranch_name("");//开户支行名称

        LegalInfoINO legalInfo = new LegalInfoINO();//法人信息
        legalInfo.setLegal_name("");//法人姓名
        legalInfo.setLegal_id("");//法人证件号(身份证)
        legalInfo.setLegal_mobile("");//法人手机号(仅支持国内运营商)
        legalInfo.setLegal_id_front_path("");//法人证件正面照下载地址

        AgentInfoINO agentInfo = new AgentInfoINO();//代理人信息
        agentInfo.setAgent_name("");//代理人姓名
        agentInfo.setAgent_id("");//代理人身份证号码
        agentInfo.setAgent_mobile("");//代理人手机号
        agentInfo.setAgent_id_front_path("");//代理人证件正面照下载地址

        String page_modify = "1";//是否允许用户页面修改
        String company_principal_type = "1";//1.法人，2 代理人
        GetCompanyVerifyUrl fddClient = fddClientFactory.create(GetCompanyVerifyUrl.class);
        String result = fddClient.invokeCompanyVerifyUrl(companyInfo, bankInfo, legalInfo
                , agentInfo, customerId, verifyedWay, page_modify,
                company_principal_type, returnUrl, notifyUrl);
        log.info("GetCompanyVerifyUrl response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeCompanyVerifyUrlSuccess(code));
    }

    /**
     * 获取个人实名认证地址
     *
     * @param customerId   客户编号
     * @param customerName 姓名
     * @param idNum        证件号码
     * @param mobile       手机号码
     * @param notifyUrl    回调地址
     * @return
     */
    public FddResponse getPersonVerifyUrl(String customerId, String customerName, String idNum, String mobile, String notifyUrl) {
        log.info("GetPersonVerifyUrl request {} {} {} {} {}", customerId, customerName, idNum, mobile, notifyUrl);
        String verifyed_way = "0";//实名认证套餐类型
        String page_modify = "1";//是否允许用户页面修改
        String return_url = "";//同步通知url
        String customer_ident_type = "0";//证件类型
        String ident_front_path = "";//证件正面照下载地址
        GetPersonVerifyUrl fddClient = fddClientFactory.create(GetPersonVerifyUrl.class);
        String result = fddClient.invokePersonVerifyUrl(customerId, verifyed_way,
                page_modify, notifyUrl, return_url, customerName, customer_ident_type,
                idNum, mobile, ident_front_path);
        log.info("GetPersonVerifyUrl response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokePersonVerifyUrlSuccess(code));
    }

    /**
     * 个人实名认证（和老的CA接口一样）不需要用户参与认证
     *
     * @param customerName 用户名称
     * @param email
     * @param idNum        身份证号码
     * @param mobile       手机号码
     * @return
     */
    public VerifyCAResponse personCA(String customerName, String email, String idNum, String mobile) {
//        log.info("PersonCA request {} {} {} {}", customerName, email, idNum, mobile);
        FddClientBase fddClient = fddClientFactory.create(FddClientBase.class);
        String result = fddClient.invokeSyncPersonAuto(customerName, email, idNum, "0", mobile);
//        log.info("PersonCA response {}", result);
        return parseVerifyCAResponse(result, code -> FddHelper.invokeSyncPersonAutoSuccess(code));
    }

    /**
     * 实名证书申请
     *
     * @param customerId       客户编号
     * @param verifiedSerialNo 实名认证序列号
     * @return
     */
    public FddResponse applyCert(String customerId, String verifiedSerialNo) {
        log.info("ApplyCert request {} {}", customerId, verifiedSerialNo);
        ApplyCert applyCert = fddClientFactory.create(ApplyCert.class);
        String result = applyCert.invokeApplyCert(customerId, verifiedSerialNo);
        log.info("ApplyCert response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeApplyCertSuccess(code));
    }

    /**
     * 编号证书申请
     *
     * @param customerId       客户编号
     * @param verifiedSerialNo 实名认证序列号
     * @return
     */
    public FddResponse applyNumCert(String customerId, String verifiedSerialNo) {
        log.info("ApplyNumCert request {} {}", customerId, verifiedSerialNo);
        ApplyNumCert applyCert = fddClientFactory.create(ApplyNumCert.class);
        String result = applyCert.invokeapplyNumcert(customerId, verifiedSerialNo);
        log.info("ApplyNumCert response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeApplyNumcertSuccess(code));
    }

    /**
     * 印章上传
     *
     * @param customerId 客户编号
     * @param imgfile    签章图片
     * @return
     */
    public FddResponse addSignature(String customerId, File imgfile) {
//        log.info("AddSignature request {} {}", customerId, imgfile);
        String imgUrl = "";//签章图片公网地址imgUrl 和 imgfile2 选1
        FddClientBase fddClient = fddClientFactory.create(FddClientBase.class);
        String result = fddClient.invokeaddSignature(customerId, imgfile, imgUrl);
//        log.info("AddSignature response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeAddSignatureSuccess(code));
    }

    /**
     * 印章查询
     *
     * @param customerId 客户编号
     * @return
     */
    public FddResponse querySignature(String customerId) {
        log.info("QuerySignature request {} ", customerId);
        SignatureClient fddClient = fddClientFactory.create(SignatureClient.class);
        String result = fddClient.invokeQuerySignature(customerId, "");
        log.info("QuerySignature response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeQuerySignatureSuccess(code));
    }

    /**
     * 印章查询
     *
     * @param customerId 客户编号
     * @return
     */
    public FddResponse querySignature(String customerId, String signatureId) {
        log.info("QuerySignature request {},{} ", customerId, signatureId);
        SignatureClient fddClient = fddClientFactory.create(SignatureClient.class);
        String result = fddClient.invokeQuerySignature(customerId, signatureId);
        log.info("QuerySignature response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeQuerySignatureSuccess(code));
    }

    /**
     * 设置默认印章
     *
     * @param customerId  客户编号
     * @param signatureId 印章编号
     * @return
     */
    public FddResponse defaultSignature(String customerId, String signatureId) {
        log.info("QuerySignature request {} {} ", customerId, signatureId);
        SignatureClient fddClient = fddClientFactory.create(SignatureClient.class);
        String result = fddClient.invokeDefaultSignature(customerId, signatureId);
        log.info("QuerySignature response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeDefaultSignatureSuccess(code));
    }

    /**
     * 替换印章
     *
     * @param customerId         客户编号
     * @param signatureId        印章编号
     * @param signatureImgBase64 新印章
     * @return
     */
    public FddResponse replaceSignature(String customerId, String signatureId, String signatureImgBase64) {
        log.info("QuerySignature request {} {} {} ", customerId, signatureId, signatureImgBase64);
        SignatureClient fddClient = fddClientFactory.create(SignatureClient.class);
        String result = fddClient.invokeReplaceSignature(customerId, signatureId, signatureImgBase64);
        log.info("QuerySignature response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeReplaceSignatureSuccess(code));
    }

    /**
     * 删除印章
     *
     * @param customerId  客户编号
     * @param signatureId 印章编号
     * @return
     */
    public FddResponse removeSignature(String customerId, String signatureId) {
        log.info("QuerySignature request {} {} ", customerId, signatureId);
        SignatureClient fddClient = fddClientFactory.create(SignatureClient.class);
        String result = fddClient.invokeRemoveSignature(customerId, signatureId);
        log.info("QuerySignature response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeRemoveSignatureSuccess(code));
    }

    /**
     * 合同模板上传
     *
     * @param templateId      模板编号
     * @param templateFilePdf PDF模板
     * @param docUrl          文档地址
     * @return
     */
    public FddResponse uploadTemplate(String templateId, File templateFilePdf, String docUrl) {
        log.info("UploadTemplate request {} {} {}", templateId, templateFilePdf, docUrl);
        FddClientBase fddClient = fddClientFactory.create(FddClientBase.class);
        String result = fddClient.invokeUploadTemplate(templateId, templateFilePdf, docUrl);
        log.info("UploadTemplate response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeUploadTemplateSuccess(code));
    }


    /**
     * 通过模板生成合同
     *
     * @param templateId
     * @param contractId
     * @param docTitle
     * @param fontSize
     * @param fontType
     * @param parameter
     * @param dynamicTables
     * @return
     */
    public GenerateContractRes generateContract(String templateId, String contractId, String docTitle,
                                                String fontSize, String fontType, JSONObject parameter, JSONObject dynamicTables) {
        log.info("GenerateContract request {} {} {} {} {} {} {}", templateId, contractId, docTitle, fontSize, fontType, parameter, dynamicTables);
        String parameters = parameter.toString();
        String tabs = dynamicTables == null ? "" : dynamicTables.toString();
        FddClientBase fddClient = fddClientFactory.create(FddClientBase.class);
        String result = fddClient.invokeGenerateContract(templateId, contractId, docTitle, fontSize, fontType, parameters, tabs);
        log.info("GenerateContract response {}", result);
        return parseGenerateContractRes(result, code -> FddHelper.invokeGenerateContractSuccess(code));
    }

    /**
     * 合同上传
     *
     * @param contractId      自定义合同ID
     * @param docTitle        合同标题
     * @param contractFilePdf 合同文件
     * @return
     */
    public FddResponse uploadDocs(String contractId, String docTitle, File contractFilePdf) {
        log.info("UploadDocs request {} {} {}", contractId, docTitle, contractFilePdf);
        FddClientBase fddClient = fddClientFactory.create(FddClientBase.class);
        String result = fddClient.invokeUploadDocs(contractId, docTitle, contractFilePdf, "", ".pdf");
        log.info("UploadDocs response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeUploadDocsSuccess(code));
    }

    /**
     * 手动签署合同接口，主要是生成签署合同的URL
     *
     * @param transactionId
     * @param contractId
     * @param customerId
     * @param docTitle
     * @param signKeyword
     * @param notifyUrl
     * @return
     */
    public String extSign(String transactionId, String contractId, String customerId, String outhCustomerId, String docTitle, String signKeyword, String notifyUrl) {
        log.info("ExtSign request {} {} {} {} {} {} {}", transactionId, contractId, customerId, outhCustomerId, docTitle, signKeyword, notifyUrl);
        ExtsignReq req = new ExtsignReq();
        req.setCustomer_id(customerId);//客户编号
        req.setTransaction_id(transactionId);//交易号
        req.setContract_id(contractId);//合同编号
        req.setDoc_title(docTitle);//文档标题
        req.setReturn_url(""); //页面跳转URL（签署结果同步通知）
        req.setNotify_url(notifyUrl); //页面跳转URL（签署结果同步通知）
        req.setOuth_customer_id(outhCustomerId);
        req.setSign_keyword(signKeyword);
        FddClientBase fddClient = fddClientFactory.create(FddClientBase.class);
        String signUrl = fddClient.invokeExtSign(req);
        log.info("ExtSign response {}", signUrl);
        return signUrl;
    }

    /**
     * 手动签署合同接口，主要是生成签署合同的URL
     *
     * @param transactionId
     * @param contractId
     * @param customerId
     * @param outhCustomerId
     * @param signatureId
     * @param docTitle
     * @param signKeyword
     * @param notifyUrl
     * @return
     */
    public String extSign(String transactionId, String contractId, String customerId, String outhCustomerId, String signatureId, String docTitle, String signKeyword, String notifyUrl) {
        log.info("ExtSign request {} {} {} {} {} {} {}", transactionId, contractId, customerId, outhCustomerId, signatureId, docTitle, signKeyword, notifyUrl);
        ExtSignWithSignatureReq req = new ExtSignWithSignatureReq();
        req.setCustomer_id(customerId);//客户编号
        req.setTransaction_id(transactionId);//交易号
        req.setContract_id(contractId);//合同编号
        req.setDoc_title(docTitle);//文档标题
        req.setReturn_url(""); //页面跳转URL（签署结果同步通知）
        req.setNotify_url(notifyUrl); //页面跳转URL（签署结果同步通知）
        req.setOuth_customer_id(outhCustomerId);
        req.setSignature_id(signatureId);
        req.setSign_keyword(signKeyword);
        SignClient fddClient = fddClientFactory.create(SignClient.class);
        String signUrl = fddClient.invokeExtSign(req);
        log.info("ExtSign response {}", signUrl);
        return signUrl;
    }


    /**
     * 文档签署接口（自动签）
     *
     * @param transactionId
     * @param customerId
     * @param contractId
     * @param docTitle
     * @param signKeyword
     * @param notifyUrl
     * @return
     */
    public FddResponse extSignAuto(String transactionId, String contractId, String customerId, String docTitle, String signKeyword, String notifyUrl) {
        log.info("ExtSignAuto request {} {} {} {} {} {}", transactionId, contractId, customerId, docTitle, signKeyword, notifyUrl);
        ExtsignReq req = new ExtsignReq();
        req.setCustomer_id(customerId);//客户编号
        req.setTransaction_id(transactionId);//自定义交易号
        req.setContract_id(contractId);//上传/模板填充接口合同编号
        req.setClient_role("1");//客户角色
        req.setPosition_type("0"); //0-关键字（默认）
        req.setSign_keyword(signKeyword);//定位关键字
        req.setDoc_title(docTitle);//文档标题
        req.setNotify_url(notifyUrl);
        FddClientBase fddClient = fddClientFactory.create(FddClientBase.class);
        String result = fddClient.invokeExtSignAuto(req);
        log.info("ExtSignAuto response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeExtSignAutoSuccess(code));
    }

    /**
     * 查询企业实名认证信息
     *
     * @param verifiedSerialNo 交易号，获取认证地址时返回
     * @return
     */
    public FddResponse findCompanyCertInfo(String verifiedSerialNo) {
        log.info("FindCompanyCertInfo request {}", verifiedSerialNo);
        FindCertInfo personCertInfo = fddClientFactory.create(FindCertInfo.class);
        //注：sdk 封装如果第2 参数传1 则是查询个人，其他比如"2"就是查询企业。
        String result = personCertInfo.invokeFindPersonCert(verifiedSerialNo, "2");
        log.info("FindCompanyCertInfo response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeFindPersonCertSuccess(code));
    }

    /**
     * 查询个人实名认证信息
     *
     * @param verifiedSerialNo 交易号，获取认证地址时返回
     */
    public FddResponse findPersonCertInfo(String verifiedSerialNo) {
        log.info("FindPersonCertInfo request {}", verifiedSerialNo);
        FindCertInfo personCertInfo = fddClientFactory.create(FindCertInfo.class);
        String result = personCertInfo.invokeFindPersonCert(verifiedSerialNo, "1");
        log.info("FindPersonCertInfo response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeFindPersonCertSuccess(code));
    }

    /**
     * 企业可以授权给个人签署
     *
     * @param companyId   企业客户编号
     * @param personId    个人客户编号
     * @param operateType 操作类型 0：取消授权，1：授权
     * @return
     */
    public FddResponse companyAuthToMan(String companyId, String personId, String operateType, String signatureId) {
//        log.info("CompanyAuthToMan request {} {} {} {}", companyId, personId, operateType, signatureId);
        CompanyAuthorization companyAuthorization = fddClientFactory.create(CompanyAuthorization.class);
        String result;
        if (StringUtils.isEmpty(signatureId)) {
            result = companyAuthorization.invoke(companyId, personId, operateType);
//            log.info("CompanyAuthToMan response {}", result);
            return parseFddResponse(result, code -> FddHelper.invokeCompanyAuthorizationSuccess(code));
        } else {
            result = companyAuthorization.invoke(companyId, personId, operateType, signatureId);
//            log.info("CompanyAuthToMan response {}", result);
            return parseFddResponse(result, code -> FddHelper.invokeCompanyAuthorizationSignatureSuccess(code));
        }


    }

    /**
     * 拒绝签署合同
     *
     * @param transactionId
     * @param contractId
     * @param customerId
     * @param rejectReason
     * @return
     */
    public FddResponse rejectSign(String transactionId, String contractId, String customerId, String rejectReason) {
        log.info("RejectSign request {} {} {} {}", transactionId, contractId, customerId, rejectReason);
        RejectSignContract rejectSignContract = fddClientFactory.create(RejectSignContract.class);
        String result = rejectSignContract.invoke(transactionId, contractId, customerId, rejectReason);
        log.info("RejectSign response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokeRejectSignContractSuccess(code));
    }

    /**
     * 给签署人发送签署短链接内容到指定的手机上
     *
     * @param mobile
     * @param sourceUrl
     * @return
     */
    public FddResponse pushSignSms(String mobile, String sourceUrl) {
        log.info("PushSignSms request {} {}", mobile, sourceUrl);
        PushShortUrlSmsClient client = fddClientFactory.create(PushShortUrlSmsClient.class);
        PushShortSmsReq req = new PushShortSmsReq();
        req.setSourceUrl(sourceUrl);
        req.setMobile(mobile);
        String result = client.invokePushSms(req);
        log.info("PushSignSms response {}", result);
        return parseFddResponse(result, code -> FddHelper.invokePushSignSmsSuccess(code));
    }

    private FddResponse parseFddResponse(String result, Function<Integer, Boolean> respOkFunc) {
        FddResponse resp = null;
        if (StringUtils.isNotEmpty(result)) {
            resp = JSONUtils.fromJSONString(result, FddResponse.class);
            resp.setRespOk(respOkFunc.apply(resp.getCode()));
        }
        return resp;
    }

    private VerifyCAResponse parseVerifyCAResponse(String result, Function<String, Boolean> respOkFunc) {
        VerifyCAResponse resp = null;
        if (StringUtils.isNotEmpty(result)) {
            resp = JSONUtils.fromJSONString(result, VerifyCAResponse.class);
            resp.setRespOk(respOkFunc.apply(resp.getCode()));
        }
        return resp;
    }

    private GenerateContractRes parseGenerateContractRes(String result, Function<Integer, Boolean> respOkFunc) {
        GenerateContractRes resp = null;
        if (StringUtils.isNotEmpty(result)) {
            resp = JSONUtils.fromJSONString(result, GenerateContractRes.class);
            resp.setRespOk(respOkFunc.apply(resp.getCode()));
        }
        return resp;
    }

}
