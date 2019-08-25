/*
package com.real.name.others;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.real.name.nation.entity.BankInfo;
import com.real.name.nation.entity.CJUnit;
import com.real.name.nation.entity.forNational.*;
import com.real.name.common.constant.NationConstant;
import com.real.name.common.utils.AesUtils;
import com.real.name.nation.utils.BaseRequest;
import com.real.name.nation.utils.EncryptionHelper;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

*/
/**
 * @Desc TODO
 * @Author hp
 * @Date 2019/5/6 12:17
 **//*

public class DemoTest {

    //�ϴ���Ŀ�ν���λ��Ϣ
    @Test
    public void uploadUnitInfo() {
        String method = "ProjectSubContractor.Add";
        String appsecret = NationConstant.APPSCRECT;

        CJUnit cj = new CJUnit();
        cj.setProjectCode(NationConstant.APPID);
        cj.setCorpCode(NationConstant.CORPCODE);
        cj.setCorpName(NationConstant.CORPNAME);
        cj.setCorpType("009");
        System.out.println(cj.getCorpName());
        String cjStr = JSON.toJSONString(cj);
        System.out.println(cjStr);
        BaseRequest request = new BaseRequest(){};
        Map<String,String> dataMap = getMap(cjStr,method);

        String res = request.postData(NationConstant.URL,null,dataMap);
        dataMap.put("appsecret",appsecret);
        System.out.println(String.format("���ý����%s",res));
    }


    public Map<String,String> getMap(String data,String method) {
        String guid = UUID.randomUUID().toString().replace("-","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());

        String sortString = "appid=" + NationConstant.APPID;
        sortString += "&data=" + data;
        sortString += "&format="+ NationConstant.FORMART;
        sortString += "&method=" + method;
        sortString += "&nonce=" + guid;
        sortString += "&timestamp=" + timestamp;
        sortString += "&version=" + NationConstant.VERSION;
        sortString += "&appsecret=" + NationConstant.APPSCRECT;
        String sign = EncryptionHelper.getSHA256StrJava(sortString.toLowerCase());

        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("appid", NationConstant.APPID);
        dataMap.put("data", data);
        dataMap.put("format", NationConstant.FORMART);
        dataMap.put("method",method);
        dataMap.put("nonce",guid);
        dataMap.put("timestamp",timestamp);
        dataMap.put("version", NationConstant.VERSION);
        dataMap.put("sign",sign);
        return dataMap;
    }

    //�޸���Ŀ��Ϣ
    @Test
    public void editProject() {
        String method = "Project.PartUpdate";
        String appsecret = NationConstant.APPSCRECT;
        NationalProject pg = new NationalProject();
        pg.setBuildPlanNum(AesUtils.encrypt("1029213748403",appsecret));
        pg.setPrjPlanNum(AesUtils.encrypt("111938473",appsecret));
        pg.setProjectCode(NationConstant.APPID);
        pg.setPrjStatus("001");
        String str = JSON.toJSONString(pg);
        Map<String,String> dataMap = getMap(str,method);

        BaseRequest request = new BaseRequest(){};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(String.format("���ý����%s",res));
    }

    //��ѯ��Ŀ��Ϣ
    @Test
    public void queryProject() {
        String method = "Project.Query";
        SearchProject sp = new SearchProject(0,10);
        sp.setContractorCorpCode(NationConstant.CORPCODE);
        sp.setProjectCode(NationConstant.APPID);
        String str = JSON.toJSONString(sp);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest(){};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //�޸���Ŀ�ν���λ��Ϣ(����ͬ�ϴ���Ŀ�ν���Ϣ)
    @Test
    public void editCJProject() {
        String method = "ProjectSubContractor.Update";
        String appsecret = NationConstant.APPSCRECT;
        CJUnit cj = new CJUnit();
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        BankInfo bankInfo = new BankInfo("103","��ɫ������","10929229282827272726162782","453226789");
        bankInfo.setBankNumber(AesUtils.encrypt(bankInfo.getBankNumber(),appsecret));
        List list = new ArrayList<>();
        list.add(bankInfo);

        cj.setBankInfos(list);
        cj.setProjectCode(NationConstant.APPID);
        cj.setCorpCode(NationConstant.CORPCODE);
        cj.setCorpName(NationConstant.CORPNAME);
        cj.setCorpType("009");

        cj.setPmIDCardType("01");
        cj.setPmPhone("13234344444");
        cj.setPmIdCardNumber(AesUtils.encrypt("10101928828282",appsecret));
        cj.setPmName("����");


        cj.setEntryTime(format.format(System.currentTimeMillis()));
        cj.setExitTime(format.format(new Date().getTime()));
        String cjStr = JSON.toJSONString(cj);

        BaseRequest request = new BaseRequest(){};
        Map<String,String> dataMap = getMap(cjStr,method);

        String res = request.postData(NationConstant.URL,null,dataMap);
        dataMap.put("appsecret",appsecret);
//        WriteLogInfo(dataMap);
        System.out.println(String.format("���ý����%s",res));
    }

    //��ѯ��Ŀ�ν���λ��Ϣ
    @Test
    public void searchCJInfo() {
        String method = "ProjectSubContractor.Query";
        String appsecret = NationConstant.APPSCRECT;
        SearchProject sp = new SearchProject(0,20);
        sp.setProjectCode(NationConstant.APPID);
//        sp.setContractorCorpCode("187636953028383");
//        sp.setProjectCode("102399383829203");
        String str = JSON.toJSONString(sp);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest(){};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //�ϴ�������Ϣ (������Ҫ�첽��ȡ������)
    @Test
    public void uploadGroup() {
        String method = "Team.Add";
        String appsecret = NationConstant.APPSCRECT;
        NationalGroup ng = new NationalGroup();
        ng.setProjectCode(NationConstant.APPID);
        ng.setCorpCode(NationConstant.CORPCODE);
        ng.setCorpName("�޳ǽ����Ƽ����޹�˾");
        ng.setTeamName("������");
        String str = JSON.toJSONString(ng);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest(){};
        String res = request.postData(NationConstant.URL,null,dataMap);
        JSONObject obj = JSONObject.parseObject(res);
        System.out.println(res);
        obj = obj.getJSONObject("data");
        AsyncHandleResultQuery(obj.getString("requestSerialCode"),"AsyncHandleResult.Query");
    }

    //�첽���ýӿڲ�ѯ
    public void AsyncHandleResultQuery(String str,String method) {
        NationalGroup ng = new NationalGroup();
        ng.setRequestSerialCode(str);
        Map<String,String> dataMap = getMap(JSON.toJSONString(ng),method);
        BaseRequest request = new BaseRequest(){};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //�޸İ�����Ϣ
    @Test
    public void editGroup() {
        String method = "Team.Update";
        String appsecret = NationConstant.APPSCRECT;
        NationalGroup ng = new NationalGroup();
        ng.setTeamSysNo(1500161164);
        ng.setTeamName("������");

        String str = JSON.toJSONString(ng);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��ѯ������Ϣ
    @Test
    public void queryGroup() {
        String method = "Team.Query";
        String appsecret = NationConstant.APPSCRECT;
        SearchProject sp = new SearchProject(0,20);
        sp.setProjectCode(NationConstant.APPID);
        String str = JSON.toJSONString(sp);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //�ϴ���Ŀ������Ϣ
    @Test
    public void updateWorkerInfo() {
        String method = "ProjectWorker.Add";
        NationalWorkerInfo nwi = new NationalWorkerInfo(NationConstant.APPID, NationConstant.CORPCODE,"�޳ǽ����Ƽ����޹�˾",1500161164,"������");
        NationalPerson worker = new NationalPerson();
        worker.setWorkerName("��ĳ��");
        worker.setIsTeamLeader(1);
        worker.setIdCardType("01");
        worker.setIdCardNumber(AesUtils.encrypt("430626199701135715", NationConstant.APPSCRECT));
        worker.setWorkType("010");
        worker.setWorkRole(10);
        worker.setNation("��");
        worker.setAddress("�����г�����");
        worker.setPoliticsType("01");
        worker.setCellPhone("13143045142");
        worker.setCultureLevelType("05");
        worker.setGrantOrg("�����й�����");
        worker.setDate("");
        //ͼƬ��С����Ϊ10K
        worker.setHeadImage("https://gss1.bdstatic.com/-vo3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=571122a7b07eca8016053ee5a1229712/8d5494eef01f3a29c8f5514a9925bc315c607c71.jpg");
        List<NationalPerson> list = new LinkedList<>();
        list.add(worker);
        nwi.setWorkerList(list);
        String str = JSON.toJSONString(nwi);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        JSONObject obj = JSONObject.parseObject(res);
        System.out.println(res);
        obj = obj.getJSONObject("data");
        AsyncHandleResultQuery(obj.getString("requestSerialCode"),"AsyncHandleResult.Query");
        System.out.println(res);
    }

    //�޸���Ŀ������Ϣ
    @Test
    public void editWorkerInfo() {
        String method = "ProjectWorker.Update";
        String appsecret = NationConstant.APPSCRECT;
        WorkerWrapper wp = new WorkerWrapper();
        wp.setProjectCode(NationConstant.APPID);
        wp.setCorpCode(NationConstant.CORPCODE);
        wp.setCorpName(NationConstant.CORPNAME);
        wp.setTeamName("������");
        wp.setTeamSysNo(1500161164);

        wp.setWorkerName("��ѻ");
        wp.setIsTeamLeader(1);
        wp.setIdCardType("01");
        wp.setIdCardNumber(AesUtils.encrypt("430626199701135715",appsecret));
        wp.setWorkType("110");
        wp.setWorkRole(20);
        wp.setNation("��");
        wp.setAddress("����·");
        wp.setHeadImage("https://gss1.bdstatic.com/-vo3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=571122a7b07eca8016053ee5a1229712/8d5494eef01f3a29c8f5514a9925bc315c607c71.jpg");
        wp.setCellPhone("13112634242");
        wp.setGrantOrg("fafa");
        wp.setPoliticsType("01");
        wp.setCultureLevelType("01");
        String str = JSON.toJSONString(wp);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��ѯ��Ŀ������Ϣ
    @Test
    public void queryWorkerInfos() {
        String method = "ProjectWorker.Query";
        String appsecret = NationConstant.APPSCRECT;

        SearchProject sp = new SearchProject(0,5);
        sp.setProjectCode(NationConstant.APPID);
        String str = JSON.toJSONString(sp);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��Ŀ��Ա���˳���Ϣ
    @Test
    public void inoutInfo() {
        String method = "WorkerEntryExit.Add";
        String appScrect = NationConstant.APPSCRECT;

        NationalWorkerInfo nwi = new NationalWorkerInfo();
        nwi.setProjectCode(NationConstant.APPID);
        nwi.setCorpCode(NationConstant.CORPCODE);
        nwi.setTeamSysNo(1500161164);
        NationalPerson worker = new NationalPerson();
        worker.setIdCardType("01");
        worker.setIdCardNumber(AesUtils.encrypt("430626199701135715",appScrect));
        worker.setType(1);
        worker.setDate("2019-03-10");
        worker.setVoucher("");
        nwi.setWorkerList(Arrays.asList(worker));
        nwi.setCorpName(NationConstant.CORPNAME);

        String str = JSON.toJSONString(nwi);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��ѯ��Ŀ���˽��˳���Ϣ
    @Test
    public void workerEntryExitQuery() {
        String method = "WorkerEntryExit.Query";
        String appScrect = NationConstant.APPSCRECT;
        SearchProject sp = new SearchProject(1,20);
        sp.setProjectCode("10203923");

        */
/**
         * �����Ѱ�����ɸ�����Ҫ��������
         *//*


        String str = JSON.toJSONString(sp);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //�ϴ���Ŀ���˺�ͬ
    @Test
    public void workerContractAdd() throws UnsupportedEncodingException {
        String method = "WorkerContract.Add";
        String appScrect = NationConstant.APPSCRECT;
        NationalPerson worker = new NationalPerson();
        worker.setCorpCode(NationConstant.CORPCODE);
        worker.setCorpName(NationConstant.CORPNAME);
        worker.setIdCardType("01");
        worker.setIdCardNumber(AesUtils.encrypt("430626199701135715",appScrect));
        worker.setContractPeriodType(0);
        worker.setStartDate("2019-10-10");
        worker.setEndDate("2019-11-11");
        worker.setUnit(80);
        worker.setUnitPrice(11.0);
        worker.setAttachments(Arrays.asList(new NationalAttachment("112323","aHR0cHM6Ly9nc3MxLmJkc3RhdGljLmNvbS8tdm8zZFNhZ194STRraEdrcG9XSzFIRjZoaHkvYmFpa2UvcyUzRDIyMC9zaWduPTU3MTEyMmE3YjA3ZWNhODAxNjA1M2VlNWExMjI5NzEyLzhkNTQ5NGVlZjAxZjNhMjljOGY1NTE0YTk5MjViYzMxNWM2MDdjNzEuanBn")));

        NationalProject pi = new NationalProject();
        pi.setProjectCode(NationConstant.APPID);
        pi.setContractList(new ArrayList<>(Arrays.asList(worker)));
        String str = JSON.toJSONString(pi);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL, null,dataMap);
        System.out.println(res);
    }

    //��ѯ���˺�ͬ
    @Test
    public void WorkerContractQuery() {
        String method = "WorkerContract.Query";
        String appScrect = NationConstant.APPSCRECT;
        SearchProject sp = new SearchProject(0,20);
        sp.setProjectCode("219833");
        sp.setCorpCode("12939193");
        sp.setCorpName("��Դ��");
        sp.setIdCardNumber(AesUtils.encrypt("012931293",appScrect));
        sp.setIdCardType("001");
        String str = JSON.toJSONString(sp);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,method);
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }


    //�ϴ����˿���
    @Test
    public void WorkerAttendanceAdd() {
        NationalProject np =new NationalProject();
        np.setProjectCode(NationConstant.APPID);
        np.setTeamSysNo(1500161164);

        NationalPerson worker = new NationalPerson();
        worker.setIdCardType("1");
        worker.setIdCardNumber(AesUtils.encrypt("430626199701135715", NationConstant.APPSCRECT));
        worker.setDate("2016-01-01 12:12:12");
        worker.setDirection("01");
        worker.setImage("");
        np.setDataList(Arrays.asList(worker));

        String str = JSON.toJSONString(np);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,"WorkerAttendance.Add");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��ѯ���˿���
    @Test
    public void WorkerAttendanceQuery() {
        SearchProject sp = new SearchProject(1,20);
        sp.setProjectCode("123123");
        sp.setDate("2019-10-10");
        sp.setTeamSysNo(123123);

        String str = JSON.toJSONString(sp);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,"WorkerAttendance.Query");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //�ϴ����˹���
    @Test
    public void PayrollAdd() {
        NationalProject np = new NationalProject();
        np.setProjectCode(NationConstant.APPID);
        np.setCorpCode(NationConstant.CORPCODE);
        np.setCorpName(NationConstant.CORPNAME);
        np.setTeamSysNo(1500161164);
        np.setPayMonth("2019-01");
        NationalAttachment na = new NationalAttachment("1","1");
        np.setAttachments(Arrays.asList(na));


        NationalPerson worker = new NationalPerson();
        worker.setIdCardType("01");
        worker.setIdCardNumber(AesUtils.encrypt("430626199701135715", NationConstant.APPSCRECT));
        worker.setPayRollBankName("�㷢����");
        worker.setPayRollTopBankCode("001");
        worker.setPayRollBankCardNumber(AesUtils.encrypt("0001", NationConstant.APPSCRECT));
        worker.setBalanceDate("2019-01-01");
        worker.setPayBankCode("001");
        worker.setPayBankCardNumber(AesUtils.encrypt("101111", NationConstant.APPSCRECT));
        worker.setPayRollBankName("001");
        worker.setPayBankName("1");
        worker.setPayRollBankCode("001");
        worker.setTotalPayAmount(11.2);
        worker.setActualAmount(11.2);
        worker.setIsBackPay(0);
        worker.setDate("2019-01");
        np.setDetailList(Arrays.asList(worker));

        String str = JSON.toJSONString(np);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,"Payroll.Add");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��ѯ���˹��� //todo
    @Test
    public void PayrollQuery() {
        NationalPerson worker = new NationalPerson();
        worker.setProjectCode(NationConstant.APPID);
        worker.setCorpCode(NationConstant.CORPCODE);
        worker.setCorpName(NationConstant.CORPNAME);
        worker.setTeamSysNo(1500161144);
        worker.setPayMonth("2019-01");
        String str = JSON.toJSONString(worker);
        System.out.println(str);
        Map<String,String> dataMap = getMap(str,"Payroll.Query");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }



    //�ϴ���Ŀ��ѵ
    @Test
    public void upLoadItemTraining() {
        Training training = new Training();
        training.setProjectCode(NationConstant.APPID);
        training.setTrainingDate("1979-02-01");
        training.setTrainingDuration(1.0);
        training.setTrainingName("��Դ��");
        training.setTrainingTypeCode("003001");
        String str = JSON.toJSONString(training);
        Map<String,String> dataMap = getMap(str,"ProjectTraining.Add");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��ѯ��Ŀ��ѵ
    @Test
    public void searchItemTraining() {
        SearchProject sp = new SearchProject(1,20);
        sp.setProjectCode(NationConstant.APPID);
        sp.setTrainingDate("1979-01-01");
        sp.setTypeCode("01020313");
        String str = JSON.toJSONString(sp);
        Map<String,String> dataMap = getMap(str,"ProjectTraining.Query");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }


    //���½�Ϊ��δ���Žӿ�
    //��ѯ��ҵ������Ϊ��¼
    @Test
    public void badInfo() {
        SearchProject sp = new SearchProject(1,20);
        sp.setProjectCode("123132");
        sp.setCorpCode("12030123");
        sp.setCorpName("��Դ��");
        sp.setCreditType("001");
        String str = JSON.toJSONString(sp);
        Map<String,String> dataMap = getMap(str,"CorpBadRecord.Query");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��ѯ��ҵ���ü�¼
    @Test
    public void CorpGoodRecord() {
        SearchProject sp = new SearchProject(1,20);
        sp.setProjectCode("10203122");
        sp.setCorpCode("1232");
        sp.setCorpName("0123123");
        String str = JSON.toJSONString(sp);
        Map<String,String> dataMap = getMap(str,"CorpGoodRecord.Query");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);
    }

    //��ѯ��ҵ������
    @Test
    public void CorpBlackListQuery() {
        SearchProject sp = new SearchProject(1,20);
        sp.setCorpCode("123123");
        sp.setCorpName("��Դ��");
        String str = JSON.toJSONString(sp);
        Map<String,String> dataMap = getMap(str,"CorpBlackList.Query");
        BaseRequest request = new BaseRequest() {};
        String res = request.postData(NationConstant.URL,null,dataMap);
        System.out.println(res);

    }

    //��ѯ���˲�����Ϊ��¼
    public void WorkerBadRecordQuery() {

    }
}
*/
