package com.real.name.subcontractor;

import com.real.name.others.BaseTest;
import com.real.name.project.entity.Project;
import com.real.name.subcontractor.entity.SubContractor;
import com.real.name.subcontractor.entity.query.SubContractorQuery;
import com.real.name.subcontractor.entity.search.SubContractorSearchInPro;
import com.real.name.subcontractor.service.repository.SubContractorMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubContractorTest extends BaseTest {

    @Autowired
    private SubContractorMapper subContractorMapper;

    @Test
    public void saveSubContractor() {
        SubContractor subContractor = new SubContractor();
        subContractor.setCorpCode("100");
        subContractor.setCorpName("name");
        subContractor.setBankCode("2383953957");
        subContractor.setBankName("bankName");
        subContractor.setBankLinkNumber("bankLinkNmumber");
        subContractor.setBankNumber("bankNumber");
        subContractor.setCreateTime(new Date());
        subContractor.setEntryTime(new Date());
        subContractor.setExitTime(new Date());
        subContractor.setPmIdCardNumber("pmIdCardNumber");
        subContractor.setPmName("pmName");
        subContractor.setPmPhone("pmPhone");
        Project project = new Project();
        project.setProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        subContractor.setProject(project);
        int i = subContractorMapper.saveSubContractor(subContractor);
        System.out.println(i);
    }

    @Test
    public void updateSubContractorById() {
        SubContractor subContractor = new SubContractor();
        subContractor.setSubContractorId(2);
        subContractor.setCorpCode("100dfdf");
        subContractor.setCorpName("dfdf");
        subContractor.setBankCode("dfd");
        subContractor.setBankName("bankName");
        subContractor.setBankLinkNumber("dfdf");
        subContractor.setBankNumber("dfd");
        subContractor.setCreateTime(new Date());
        subContractor.setEntryTime(new Date());
        subContractor.setExitTime(new Date());
        subContractor.setPmIdCardNumber("df");
        subContractor.setPmName("dere");
        subContractor.setPmPhone("dfd");
        Project project = new Project();
        project.setProjectCode("44010620190510008");
        subContractor.setProject(project);
        subContractor.setUploadStatus(1);
        int i = subContractorMapper.updateSubContractorById(subContractor);
        System.out.println(i);
    }

    @Test
    public void findByProjectCode() {
        List<SubContractorQuery> subContractors = subContractorMapper.findByProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        System.out.println(subContractors);
    }

    @Test
    public void judgeEmptyById() {
        Integer integer = subContractorMapper.judgeEmptyById(3);
        System.out.println(integer);
    }

    @Test
    public void findById() {
        SubContractor byId = subContractorMapper.findById(1);
        System.out.println(byId);
    }

    @Test
    public void setProSubContractorRemoveStatus() {
        int i = subContractorMapper.setProSubContractorRemoveStatus(1);
        System.out.println(i);
    }

    @Test
    public void findUnRemoveInProject() {
        List<SubContractorQuery> unRemoveInProject = subContractorMapper.findUnRemoveInProject("44010620190510008");
        System.out.println(unRemoveInProject);
    }

    @Test
    public void findCorpName() {
        List<SubContractorQuery> corpName = subContractorMapper.findCorpName("44010620190510008", 1);
        System.out.println(corpName);
    }

    @Test
    public void getPersonNumInContractor() {
        Integer personNumInContractor = subContractorMapper.getPersonNumInContractor(1);
        Integer personContractSignNum = subContractorMapper.getPersonContractSignNum(1);
        System.out.println(personNumInContractor);
        System.out.println(personContractSignNum);
    }

    @Test
    public void searchContractorInPro() {
        SubContractorSearchInPro query = new SubContractorSearchInPro();
        query.setProjectCode("36bj84W235Zgc8O78yuS32510ppMkHfe");
        List<SubContractorQuery> subContractorQueries = subContractorMapper.searchContractorInPro(query);
        System.out.println(subContractorQueries);
        query.setCorpName("re");
        List<SubContractorQuery> subContractorQueries1 = subContractorMapper.searchContractorInPro(query);
        System.out.println(subContractorQueries1);
        query.setCorpCode("100dfdf");
        List<SubContractorQuery> subContractorQueries2 = subContractorMapper.searchContractorInPro(query);
        System.out.println(subContractorQueries2);
    }


    @Test
    public void findUploadStatusById() {
        Integer uploadStatusById = subContractorMapper.findUploadStatusById(1);
        System.out.println(uploadStatusById);
    }

    @Test
    public void findByIdList() {
        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        idList.add(2);
        List<SubContractorQuery> byIdList = subContractorMapper.findByIdList(idList);
        System.out.println(byIdList);
    }



}

















