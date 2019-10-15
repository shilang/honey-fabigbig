package com.honeywell.greenhouse.fbb;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.honeywell.greenhouse.fbb.core.FddManager;
import com.honeywell.greenhouse.fbb.core.FddResponse;
import com.honeywell.greenhouse.fbb.po.Signature;
import com.honeywell.greenhouse.fbb.po.Staff;

import lombok.extern.slf4j.Slf4j;

/**
 * 运行testUploadSignature()后，产生的数据库脚本，在数据库中执行即可
 * 完成申丝公司的所有项目组上传
 */
@SpringBootTest(classes = FbbApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@ActiveProfiles("release")
public class SignatureManager {
    //注意： 生产环境时，打开 @ActiveProfiles("release")
    //替换生产环境中的相应公司customer_id
    @Autowired
    protected FddManager fddManager;

    private static HashMap<String, String> companyMap = new HashMap<>();

    private static HashMap<String, String> companyOpenMap = new HashMap<>();

    private static HashMap<String, String> companyChainMap = new HashMap<>();

    private static HashMap<String, Staff> staffMap = new HashMap<>();

    private static HashMap<String, Signature> shensySignatureMap = new HashMap<>();

    static {
        companyMap.put("KLG", "70B5B543E4F5A09785A9B20C48723F2A"); //凯乐供应链
        companyMap.put("KLW", "64255655FF761C49E62FBC8F7E0A6423"); //凯乐物联网
        companyMap.put("MDJ", "A180B7E1DEC6A52FCB99EE9A46FD9EBD"); //牡丹江市凯乐汇
        companyMap.put("SHENSY", "0112DD57981DC4A2EC524B238BD1ACCA"); //申丝
        companyMap.put("CQK", "687143CD5DDC9473D42E45F38646D907"); //重庆市凯乐汇

        companyOpenMap.put("70B5B543E4F5A09785A9B20C48723F2A", "18721039176");
        companyOpenMap.put("64255655FF761C49E62FBC8F7E0A6423", "18512176745");
        companyOpenMap.put("0112DD57981DC4A2EC524B238BD1ACCA", "15201753245");
        companyOpenMap.put("A180B7E1DEC6A52FCB99EE9A46FD9EBD", "13840801030");
        companyOpenMap.put("687143CD5DDC9473D42E45F38646D907", "13896018856");

        //
        companyChainMap.put("70B5B543E4F5A09785A9B20C48723F2A", "凯乐供应链");
        companyChainMap.put("64255655FF761C49E62FBC8F7E0A6423", "凯乐物联网");
        companyChainMap.put("0112DD57981DC4A2EC524B238BD1ACCA", "申丝企业发展");
        companyChainMap.put("A180B7E1DEC6A52FCB99EE9A46FD9EBD", "牡丹江市凯乐汇");
        companyChainMap.put("687143CD5DDC9473D42E45F38646D907", "重庆市凯乐汇");
    }

    @Test
    public void testUploadSignature() {
        String path = "\\四期合同template\\新电子10.91";
        addSignaturesToFbb(path);
    }

    @Test
    public void testAuthSignatureToSomePerson() throws IOException {
//        A7191898D17917272B4ABC982381FFF3	1093
//        0112DD57981DC4A2EC524B238BD1ACCA	  126262918
//        687143CD5DDC9473D42E45F38646D907	126569638

        authSignatureToStaffOne("0112DD57981DC4A2EC524B238BD1ACCA","2659CF96B6448A6EBF216C53957A6A95","36993","126262949");

    }


    /**
     * 给一个员工授权印章
     *
     * @throws IOException
     */
    public void authSignatureToStaffOne(String companyCustomerId, String personCustomerId, String userId, String signatureId) throws IOException {
        String openId = companyOpenMap.get(companyCustomerId);
        FddResponse fddResponse = fddManager.companyAuthToMan(companyCustomerId, personCustomerId, "1", signatureId);
        if (fddResponse.isRespOk()) {
            log.info("INSERT INTO public.core_org_auth(open_id, company_custom_id, person_custom_id, user_id,signature_id, auth, auth_code, created_at)VALUES('{}','{}','{}',{},'{}','1',1 ,now());", openId, companyCustomerId, personCustomerId, userId, signatureId);

        } else {
            log.error("印章授权失败：{},{},{},{}", companyCustomerId, personCustomerId, signatureId, fddResponse.getMsg());
        }
    }

    @Test
    public void cancelAuthSignatureToSomePerson() throws IOException {
        String path = "./取消老的授权印章列表.txt";
        //company_customer_id	person_customer_id      user_id     signature_id
        //0112DD57981DC4A2EC524B238BD1ACCA	6B63DB0767DA13CC9798E93CE76F18A5	10541	126010324
        File file = new File(path);
        List<String> authSigns = FileUtils.readLines(file, "utf-8");
        authSigns.stream().forEach(a -> {
            String[] authStr = a.split("\\t");
            String companyCustomerId = authStr[0].trim();
            String personCustomerId = authStr[1].trim();
            String userId = authStr[2].trim();
            String signatureId = authStr[3].trim();
            try {
                cancelAuthSignatureToStaffOne(companyCustomerId,personCustomerId,userId,signatureId);
            } catch (IOException e) {

            }
        });
    }

    public void cancelAuthSignatureToStaffOne(String companyCustomerId, String personCustomerId, String userId, String signatureId) throws IOException {
        FddResponse fddResponse = fddManager.companyAuthToMan(companyCustomerId, personCustomerId, "0", signatureId);
        if (fddResponse.isRespOk()) {
            log.info("delete from public.core_org_auth where company_custom_id = '{}' and user_id={} and signature_id='{}';", companyCustomerId, userId, signatureId);
        } else {
            log.error("印章cancel授权失败：{},{},{},{}", companyCustomerId, personCustomerId, signatureId, fddResponse.getMsg());
        }
    }

    @Test
    public void testAuthSignatureToStaff() throws IOException {
        //文本文件的格式为：company_customer_id	signature_id   person_customer_id	 user_id
        //以\t 分格
        String path = "./员工授章列表.txt";
        //company_customer_id	signature_id   person_customer_id	 user_id
        File file = new File(path);
        List<String> authSigns = FileUtils.readLines(file, "utf-8");
        authSigns.stream().forEach(a -> {
            String[] authStr = a.split("\\t");
            String companyCustomerId = authStr[0].trim();
            String signatureId = authStr[1].trim();
            String personCustomerId = authStr[2].trim();
            String userId = authStr[3].trim();
            String openId = companyOpenMap.get(companyCustomerId);
            FddResponse fddResponse = fddManager.companyAuthToMan(companyCustomerId, personCustomerId, "1", signatureId);
            if (fddResponse.isRespOk()) {
                log.info("INSERT INTO public.core_org_auth(open_id, company_custom_id, person_custom_id, user_id,signature_id, auth, auth_code, created_at)VALUES('{}','{}','{}',{},'{}','1',1 ,now());", openId, companyCustomerId, personCustomerId, userId, signatureId);

            } else {
                log.error("印章授权失败：{},{},{},{}", companyCustomerId, personCustomerId, signatureId, fddResponse.getMsg());
            }
        });
    }

    @Test
    public void addOneSignatureToFbb() {
        File file = new File("新电子10.9\\新电子10.8\\东北\\牡丹江凯乐汇\\牡丹江凯乐汇.png");
        String project = "东北-牡丹江凯乐汇-";
        String remark = "牡丹江凯乐汇";
        String companyCustomerId = "A180B7E1DEC6A52FCB99EE9A46FD9EBD";
//        String companyCustomerId = "64255655FF761C49E62FBC8F7E0A6423";

        FddResponse response = fddManager.addSignature(companyCustomerId, file);
        if (response.isRespOk()) {
            Map<?, ?> dataMap = (Map<?, ?>) response.getData();
            String signatureId = dataMap.get("signature_id").toString();
            String s = "INSERT INTO public.shensy_user_signature(project_group, customer_id, signature_id, signature_type, signature_scope, created_by, created_at, updated_by, updated_at, deleted, remark)" +
                    "VALUES('" + project + "', '" + companyCustomerId + "', '" + signatureId + "', NULL, 0, 10389, now(), NULL, NULL, 0, '" + remark + "');";
            log.info(s);
        }
    }

    private void addSignaturesToFbb(String path) {
        File dir = new File(path);
        Collection<File> files = FileUtils.listFiles(dir, new String[]{"png"}, true);
        files.stream().forEach(a -> {
            String project = parseProject(path, a);
            String remark = a.getName().replace(".png", "");
            String companyCustomerId = parseCustomerId(remark);
            if (companyCustomerId == null) {
                //不上传
                System.out.println(a.getAbsolutePath());
            } else {
                FddResponse response = fddManager.addSignature(companyCustomerId, a);
                if (response.isRespOk()) {
                    //insert to db;
                    Map<?, ?> dataMap = (Map<?, ?>) response.getData();
                    String signatureId = dataMap.get("signature_id").toString();

                    String s = "INSERT INTO public.shensy_user_signature(project_group, customer_id, signature_id, signature_type, signature_scope, created_by, created_at, updated_by, updated_at, deleted, remark)" +
                            "VALUES('" + project + "', '" + companyCustomerId + "', '" + signatureId + "', NULL, 0, 10389, now(), NULL, NULL, 0, '" + remark + "');";
                    System.out.println(s);
                } else {
                    log.info("fail,project:{},remark:{},companyCustomerId:{}", project, remark, companyCustomerId);
                }
            }
        });
    }

    private String parseCustomerId(String remark) {
//    凯乐供应链
//    凯乐物联网
//    牡丹江市凯乐汇
//    申丝
//    重庆市凯乐汇
        if (remark.startsWith("凯乐供应链")) {
            return companyMap.get("KLG");
        }
        if (remark.startsWith("凯乐物联网")) {
            return companyMap.get("KLW");
        }
        if (remark.startsWith("牡丹江市凯乐汇")) {
            return companyMap.get("MDJ");
        }
        if (remark.startsWith("申丝")) {
            return companyMap.get("SHENSY");
        }
        if (remark.startsWith("重庆市凯乐汇")) {
            return companyMap.get("CQK");
        }
        return null;
    }

    private String parseProject(String path, File file) {
        String fullPath = file.getAbsolutePath();
        fullPath = fullPath.replace(path, "");
        fullPath = fullPath.replace(file.getName(), "");
        fullPath = fullPath.replaceFirst("\\\\", "");
        fullPath = fullPath.replace("\\", "-");
        return fullPath;
    }

    @Test
    public void testAuthNewSig() throws IOException {
        parseStaffCaInfo();
        parseShensySignature();
        parseStaffSignatureInfo();
    }

    private void parseStaffCaInfo() throws IOException {
        String path = "./员工CA工号.txt";
        //a.id,"name",mob_no,person_id,ca_customer_id,b.employee_no
        File file = new File(path);
        List<String> staffes = FileUtils.readLines(file, "utf-8");
        staffes.stream().forEach(a -> {
                    String[] staffStr = a.split("\\t");
                    Staff staff = new Staff();
                    staff.setUserId(staffStr[0].trim());
                    staff.setName(staffStr[1].trim());
                    staff.setMobNo(staffStr[2].trim());
                    staff.setPersonId(staffStr[3].trim());
                    staff.setCaCustomerId(staffStr[4].trim());
                    staff.setEmployeeNo(staffStr[5].trim());
                    staffMap.put(staff.getEmployeeNo(), staff);
                }
        );
    }

    private void parseShensySignature() throws IOException {
        String path = "./申丝新印章.txt";
        //project_group,customer_id,signature_id,remark ,created_at
        File file = new File(path);
        List<String> signatures = FileUtils.readLines(file, "utf-8");
        signatures.stream().forEach(a -> {
                    String[] str = a.split("\\t");
                    Signature signature = new Signature();
                    signature.setProjectGroup(str[0].trim());
                    signature.setCustomerId(str[1].trim());
                    signature.setSignatureId(str[2].trim());
                    signature.setRemark(str[3].trim());
                    shensySignatureMap.put(signature.getProjectGroup() + companyChainMap.get(str[1].trim()), signature);
                }
        );
    }


    private void parseStaffSignatureInfo() throws IOException {
        String path = "./员工印章授权列表10-10.txt";
        //101391	赵伟  	231005196404060513	18721759019	 上海	    珠海  	上海申丝企业发展有限公司业务专用章（珠海）
        File file = new File(path);
        List<String> staff_signatures = FileUtils.readLines(file, "utf-8");
        staff_signatures.stream().forEach(a -> {
            String[] str = a.split("\\t");
            //1. 通过工号找到员工信息
            String employeeNo = str[0].trim();
            String name = str[1].trim();
            String mobNo = str[3].trim();
            String project = str[4].trim();
            String group = str[5].trim();
            String signature_name = str[6].trim();

            String pg = project + "-" + group + "-";

            if (signature_name.indexOf("凯乐供应链") != -1) {
                pg = pg + "凯乐供应链";
            } else if (signature_name.indexOf("凯乐物联网") != -1) {
                pg = pg + "凯乐物联网";
            } else if (signature_name.indexOf("申丝企业发展") != -1) {
                pg = pg + "申丝企业发展";
            } else if (signature_name.indexOf("牡丹江市凯乐汇") != -1) {
                pg = pg + "牡丹江市凯乐汇";
            }
            Signature signature = shensySignatureMap.get(pg);
            Staff staff = staffMap.get(employeeNo);
            if (signature == null || staff == null) {
                    log.warn("null is:pg:{},employeeNo:{},name:{},staff:{},signature:{}",pg, employeeNo, name,  staff, signature);
            } else {
                log.info("authSignatureToStaffOne(\"{}\",\"{}\",\"{}\",\"{}\");", signature.getCustomerId(), staff.getCaCustomerId(), staff.getUserId(), signature.getSignatureId());
//                try {
//                    authSignatureToStaffOne(signature.getCustomerId(), staff.getCaCustomerId(), staff.getUserId(), signature.getSignatureId());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

}
