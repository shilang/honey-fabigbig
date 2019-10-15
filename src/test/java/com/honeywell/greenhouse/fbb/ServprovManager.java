package com.honeywell.greenhouse.fbb;

import java.io.File;
import java.io.IOException;
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
import com.honeywell.greenhouse.fbb.response.VerifyCAResponse;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = FbbApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("release")
@Slf4j
public class ServprovManager {

    @Autowired
    protected FddManager fddManager;

    private String caListFile = "./供应商授权人列表-CA.txt";

    private String authListFile = "./供应商授权人列表-授权.txt";

    @Test
    public void testQueryServprovInfo() {
        queryOneCompanyInfo("40ba3398bda949638370b6ec68b1f205" );
    }

    private void queryOneCompanyInfo(String serialNo){
        FddResponse companyCertInfo = fddManager.findCompanyCertInfo(serialNo);
        if(companyCertInfo.isRespOk()){
//            log.info("serialNo:{},companyName:{}",serialNo,companyCertInfo.getData());
            Map<?, ?> dataMap = (Map<?, ?>) companyCertInfo.getData();
            Map<?, ?> company = (Map<?, ?>)  dataMap.get("company");
            log.info("update core_org_verify set company_name='{}' where serial_no = '{}';",company.get("companyName").toString(),serialNo);
        }else{
            log.info("serialNo:{},fail",serialNo);
        }
    }


    /**
     * 供应商给下面的人授权
     *
     * @throws IOException
     */
    @Test
    public void servprovAuthToPersonFromFile() throws IOException {
        File file = new File(authListFile);
        List<String> persons = FileUtils.readLines(file, "utf-8");
//        id	servprov_name	servprov_customer_id	fa_name	fa_mobile	auth_name	auth_mobile	 auth_id	auth_customer_id
        for (String person : persons) {
            String[] p = person.split("\\t");
            String servprov_name = p[1];
            String servprov_customer_id = p[2];
            String auth_name = p[5];
            String auth_mobile = p[6];
            String auth_id = p[7];
            String auth_customer_id = p[8];
            FddResponse response = fddManager.companyAuthToMan(servprov_customer_id, auth_customer_id, "1", "");
            if (response.isRespOk()) {
                log.info("INSERT INTO public.shensy_servprov_authorized(servprov_gid, servprov_customer_id, person_name, person_mobile, person_customer_id, created_at, deleted)" +
                        "VALUES('{}','{}','{}','{}','{}',now(),0);", servprov_name, servprov_customer_id, auth_name, auth_mobile, auth_customer_id);
            } else {
                log.error("fail:{},{}", auth_mobile, auth_name);
            }
        }
    }

    @Test
    public void servprovAuthToSomePerson() throws IOException {
        String servprov_name = "合肥良创物流有限公司";
        String servprov_customer_id = "DEE8BA2D9D6B073ACB2F3322D92012BD";

        servprovAuthToOnePerson(servprov_name, servprov_customer_id, "张良兵", "13956900720", "25B0D92E660E4EAA1A471B1556D4A404");
        servprovAuthToOnePerson(servprov_name, servprov_customer_id, "徐志芹", "18856097370", "3522082549D1180BA5C91098360C6C45");
    }

    public void servprovAuthToOnePerson(String servprov_name, String servprov_customer_id, String auth_name, String auth_mobile, String auth_customer_id) throws IOException {
        FddResponse response = fddManager.companyAuthToMan(servprov_customer_id, auth_customer_id, "1", "");
        if (response.isRespOk()) {
            log.info("INSERT INTO public.shensy_servprov_authorized(servprov_gid, servprov_customer_id, person_name, person_mobile, person_customer_id, created_at, deleted)" +
                    "VALUES('{}','{}','{}','{}','{}',now(),0);", servprov_name, servprov_customer_id, auth_name, auth_mobile, auth_customer_id);
        } else {
            log.error("fail:{},{},{}", auth_mobile, auth_name);
            log.error("fadad:{}", response);
        }
    }

}
