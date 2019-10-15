package com.honeywell.greenhouse.fbb;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = FbbApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("release")
@Slf4j
public class SSStaffManager {

    @Autowired
    protected FddManager fddManager;

    private String authListFile = "./申丝员工人列表3批.txt";

    @Test
    public void caForShensyStaff() throws IOException {
        File file = new File(authListFile);
        List<String> persons = FileUtils.readLines(file, "utf-8");
        for (String person : persons) {
            String[] p = person.split("\\t");
            String name = p[0].trim();
            String idNumber = p[1].trim();
            String phone = p[2].trim();
            String center = p.length > 3 ? p[3].trim() : "";
            String project = p.length > 4 ? p[4].trim() : "";
            VerifyCAResponse verifyCAResponse = fddManager.personCA(name, "", idNumber, phone);
            if (verifyCAResponse.isRespOk()) {
                log.info("person:{},{},{},{},{},{}", name, idNumber, phone, center, project, verifyCAResponse.getCustomerId());
            } else {
                log.info("person:{},{},{},{},{},{}", name, idNumber, phone, center, project, verifyCAResponse.getMsg());
            }
        }
    }

    @Test
    public void caSomePerson() throws IOException {
        caOnePerson("张良兵", "340123197010259172", "13956900720");
        caOnePerson("徐志芹", "340123197208105603", "18856097370");
    }

    public void caOnePerson(String name, String personId, String mobile) throws IOException {
        VerifyCAResponse verifyCAResponse = fddManager.personCA(name, "", personId, mobile);
        if (verifyCAResponse.isRespOk()) {
            log.info("ca result: name:{},mobile:{},customer_id:{}", name, mobile, verifyCAResponse.getCustomerId());
        } else {
            log.info("ca result: name:{},mobile:{},msg:{}", name, mobile, verifyCAResponse.getMsg());
        }
    }


}
