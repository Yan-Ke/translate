package com.sian.translate.management.excel.service.impl;

import com.sian.translate.DTO.FinancialInfoDTO;
import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.DTO.UserRecordDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.Dictionary;
import com.sian.translate.dictionary.enity.Thesaurus;
import com.sian.translate.dictionary.repository.DictionaryRepository;
import com.sian.translate.dictionary.repository.ThesaurusRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.dictionary.service.DictionaryManageService;
import com.sian.translate.management.excel.service.ExcelService;
import com.sian.translate.management.excel.utils.ExcelData;
import com.sian.translate.management.excel.utils.ExportExcelUtils;
import com.sian.translate.management.member.service.ManageMemberService;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.UserInfoRepository;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ResultVOUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    ManageMemberService manageMemberService;

    @Autowired
    DictionaryManageService dictionaryManageService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    ThesaurusRepository thesaurusRepository;

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Autowired
    ManageUserService manageUserService;

    @Override
    public ResultVO exportmemberUserInfo(Integer isMember, HttpServletResponse response,Integer month, Integer page, Integer size, String param, HttpSession session) throws Exception {


        if (page == -1) {
            long count = userInfoRepository.count();
            page = 1;
            size = (int) count;
        }
        ResultVO memberListResultVO = manageMemberService.getMemberList(isMember, param, month,page, size, session);
        if (memberListResultVO.getCode() != 0) {
            return memberListResultVO;
        }
        PageInfoDTO pageInfoDto = (PageInfoDTO) memberListResultVO.getData();
        List<UserInfo> userInfoList = (List<UserInfo>) pageInfoDto.getList();


        String excelName = "";
        if (isMember == -1) {
            excelName = "所有用户信息";
        } else if (isMember == 0) {
            excelName = "非会员用户信息";
        } else if (isMember == 1) {
            excelName = "会员用户信息";
        } else {
            excelName = "过期会员用户信息";
        }

        ExcelData data = new ExcelData();
        data.setName(excelName);
        List<String> titles = new ArrayList();
        titles.add("id");
        titles.add("用户昵称");
        titles.add("qqOpenid");
        titles.add("weixinOpenid");
        titles.add("头像原图");
        titles.add("头像小图");
        titles.add("头像大图");
        titles.add("电话");
        titles.add("性别（1男2女3保密）");
        titles.add("年龄");
        titles.add("学历");
        titles.add("余额");
        titles.add("注册时间");
        titles.add("更新时间");
        titles.add("会员开始时间");
        titles.add("会员截止时间");
        titles.add("是否为会员（0 不是 1是 2过期会员）");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (UserInfo userInfo : userInfoList) {
            List<Object> row = new ArrayList();
            row.add(userInfo.getId() == null ? "" : userInfo.getId());
            row.add(userInfo.getNickName() == null ? "" : userInfo.getNickName());
            row.add(userInfo.getQqOpenid() == null ? "" : userInfo.getQqOpenid());
            row.add(userInfo.getWeixinOpenid() == null ? "" : userInfo.getWeixinOpenid());
            row.add(userInfo.getOrignalImage() == null ? "" : userInfo.getOrignalImage());
            row.add(userInfo.getHeadSmallImage() == null ? "" : userInfo.getHeadSmallImage());
            row.add(userInfo.getHeadBigImage() == null ? "" : userInfo.getHeadBigImage());
            row.add(userInfo.getPhone() == null ? "" : userInfo.getPhone());
            row.add(userInfo.getSex() == null ? "" : userInfo.getSex());
            row.add(userInfo.getAge() == null ? "" : userInfo.getAge());
            row.add(userInfo.getEducation() == null ? "" : userInfo.getEducation());
            row.add(userInfo.getBalance() == null ? "" : userInfo.getBalance().toString());
            row.add(userInfo.getRegistrationTime() == null ? "" : format.format(userInfo.getRegistrationTime()));
            row.add(userInfo.getUpdateTime() == null ? "" : format.format(userInfo.getUpdateTime()));
            row.add(userInfo.getMemberBeginTime() == null ? "" : format.format(userInfo.getMemberBeginTime()));
            row.add(userInfo.getMemberEndTime() == null ? "" : format.format(userInfo.getMemberEndTime()));
            if (userInfo.getMemberBeginTime() != null
                    && userInfo.getMemberEndTime() != null) {
                if (CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())) {
                    row.add(1);
                } else {
                    row.add(2);
                }
            } else {
                row.add(0);
            }
            rows.add(row);
        }


        data.setRows(rows);

        ExportExcelUtils.exportExcel(response, excelName + ".xlsx", data);
        return ResultVOUtil.success();

    }

    @Override
    public ResultVO importDictionary(Integer id, MultipartFile file, HttpSession session) throws IOException {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
        if (id == null) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(id);

        if (!dictionaryOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_NOT_EXIST.getCode(), languageType));
        }
        if (file == null || file.isEmpty()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.UPLOAD_EXCEL_EMPTY.getCode(), languageType));
        }

        Dictionary dictionary = dictionaryOptional.get();

        String fileName = file.getOriginalFilename();

        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMPORT_EXCEL_ERROR.getCode(), languageType));
        }

        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }

        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Thesaurus thesaurus = null;
        List<Thesaurus> thesaurusArrayList = new ArrayList<>();

        Sheet sheet = wb.getSheetAt(0);

        HashMap<Integer, String> errorMap = new HashMap<>();
        boolean isError = false;

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            isError = false;

            Cell cell0 = row.getCell(0);
            Cell cell1 = row.getCell(1);


            String languageContentOne = null;
            if (cell0 != null) {
                cell0.setCellType(CellType.STRING);
                languageContentOne = cell0.getStringCellValue();
            }

            String languageContentTwo = null;
            if (cell1 != null) {
                cell1.setCellType(CellType.STRING);
                languageContentTwo = cell1.getStringCellValue();
            }


            long count1 = 0;
            long count2 = 0;

            if (languageContentOne == null || languageContentOne.trim().equals("")) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_CHINESE_NOT_EMPTY.getCode(), languageType), errorMap);
            } else {
                count1 = thesaurusRepository.countByContentOne(languageContentOne.trim());
            }

            if (languageContentTwo == null || languageContentTwo.trim().equals("")) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_ZANG_NOT_EMPTY.getCode(), languageType), errorMap);
            } else {
                count2 = thesaurusRepository.countByContentTwo(languageContentTwo.trim());
            }


            if (count1 > 0) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_CHINESE_EXIST.getCode(), languageType), errorMap);
            }
            if (count2 > 0) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_ZANG_EXIST.getCode(), languageType), errorMap);
            }


            if (!isError) {
                thesaurus = new Thesaurus();
                thesaurus.setDictionaryId(dictionary.getId());
                thesaurus.setType(dictionary.getType());
                thesaurus.setContentOne(languageContentOne);
                thesaurus.setContentTwo(languageContentTwo);
                thesaurus.setCreateUser(userId);
                thesaurus.setUpdateUser(userId);
                thesaurus.setCreateTime(new Date());
                thesaurus.setUpdateTime(new Date());
                thesaurusArrayList.add(thesaurus);
            }

        }

        List<Thesaurus> result = thesaurusRepository.saveAll(thesaurusArrayList);

        if (errorMap.isEmpty()) {
            String losmsg = "导入词条至词典:"+dictionary.getName()+","+result.size()+ "条记录";
            return ResultVOUtil.success("上传成功" + result.size() + "条记录",losmsg);
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("上传成功" + result.size() + "条记录;");
            stringBuffer.append("上传失败" + errorMap.size() + "条记录;");
            for (Map.Entry<Integer, String> entry : errorMap.entrySet()) {
                Integer key = entry.getKey();
                String value = entry.getValue();
                stringBuffer.append("第" + key + "行上传失败,失败原因：" + value + ";");
            }
            if (result.size() < 1) {
                return ResultVOUtil.error(stringBuffer.toString());
            } else {
                return ResultVOUtil.success(stringBuffer.toString());
            }
        }

    }

    @Override
    public ResultVO exportThesaurus(Integer id, String name, HttpServletResponse response, Integer page, Integer size, HttpSession session) throws Exception {
        String languageType = "0";

        if (page == -1) {
            long count = thesaurusRepository.count();
            page = 1;
            size = (int) count;
        }
        ResultVO resultVO = dictionaryManageService.getThesaurusList(id, name, page, size, session);
        if (resultVO.getCode() != 0) {
            return resultVO;
        }

        PageInfoDTO pageInfoDto = (PageInfoDTO) resultVO.getData();
        List<Thesaurus> thesaurusList = (List<Thesaurus>) pageInfoDto.getList();


        if (thesaurusList != null && thesaurusList.size() > 0) {

            Optional<Dictionary> dictionaryOptional = dictionaryRepository.findById(id);

            if (!dictionaryOptional.isPresent()) {
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
            }

            Dictionary dictionary = dictionaryOptional.get();


            /***词典类型 1藏汉2 汉藏3 臧英 4英臧 5臧臧 6臧梵7 汉汉****/
            Integer type = dictionary.getType();
            String excelName = dictionary.getName();
            String columnOne = "ID";
            String columTwo = "";
            String columThree = "";

            if (type == 1) {
                columTwo = "藏语";
                columThree = "汉语";
            } else if (type == 2) {
                columTwo = "汉语";
                columThree = "藏语";
            } else if (type == 3) {
                columTwo = "藏语";
                columThree = "英语";
            } else if (type == 4) {
                columTwo = "英语";
                columThree = "藏语";
            }else if (type == 5){
                columTwo = "藏语";
                columThree = "藏语";
            }else if (type == 6) {
                columTwo = "藏语";
                columThree = "梵语";
            }else if (type == 7) {
                columTwo = "汉语";
                columThree = "汉语";
            }

            ExcelData data = new ExcelData();
            data.setName(excelName);
            List<String> titles = new ArrayList();
            titles.add(columnOne);
            titles.add(columTwo);
            titles.add(columThree);
            data.setTitles(titles);

            List<List<Object>> rows = new ArrayList();


            for (Thesaurus thesaurus : thesaurusList) {
                List<Object> row = new ArrayList();
                row.add(thesaurus.getId() == null ? "" : thesaurus.getId());
                row.add(thesaurus.getContentOne() == null ? "" : thesaurus.getContentOne());
                row.add(thesaurus.getContentTwo() == null ? "" : thesaurus.getContentTwo());

                rows.add(row);
            }


            data.setRows(rows);

            ExportExcelUtils.exportExcel(response, excelName + ".xlsx", data);
            return ResultVOUtil.success();
        } else {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.DICTIONARY_THESAURUS_IS_EMPTY.getCode(), languageType));
        }


    }

    @Override
    public ResultVO exportMemberPayRecord(String beginTime, String endTime, String orderNo, String nickName, Integer month ,Integer page, Integer size, HttpServletResponse response, HttpSession session) throws Exception {
        String languageType = "0";

        if (page == -1) {
            long count = thesaurusRepository.count();
            page = 1;
            size = (int) count;
        }
        ResultVO resultVO = manageMemberService.getAllMemberPayRecordList(beginTime, endTime, orderNo, nickName, month,page, size, session);
        if (resultVO.getCode() != 0) {
            return resultVO;
        }

        PageInfoDTO pageInfoDto = (PageInfoDTO) resultVO.getData();
        List<UserRecordDTO> userRecordDTOList = (List<UserRecordDTO>) pageInfoDto.getList();


        if (userRecordDTOList != null && userRecordDTOList.size() > 0) {


            String excelName = "会员购买流水";
            String column1 = "订单编号";
            String column2 = "购买人";
            String column3 = "VIP(0不是1是2过期会员)";
            String column4 = "类型";
            String column5 = "价格/元";
            String column6 = "优惠券";
            String column7 = "实际支付";
            String column8 = "支付方式";
            String column9 = "支付时间";


            ExcelData data = new ExcelData();
            data.setName(excelName);
            List<String> titles = new ArrayList();
            titles.add(column1);
            titles.add(column2);
            titles.add(column3);
            titles.add(column4);
            titles.add(column5);
            titles.add(column6);
            titles.add(column7);
            titles.add(column8);
            titles.add(column9);
            data.setTitles(titles);

            List<List<Object>> rows = new ArrayList();


            for (UserRecordDTO userRecordDTO : userRecordDTOList) {
                List<Object> row = new ArrayList();


                row.add(userRecordDTO.getOrderId() == null ? "" : userRecordDTO.getOrderId());
                row.add(userRecordDTO.getNickName() == null ? "" : userRecordDTO.getNickName());
                row.add(userRecordDTO.getIsMember());
                row.add(userRecordDTO.getMonth() + "个月");


                if (userRecordDTO.getReduceAmount() != null) {
                    row.add(userRecordDTO.getAmount() == null ? "" : userRecordDTO.getAmount().add(userRecordDTO.getReduceAmount()));
                } else {
                    row.add(userRecordDTO.getAmount() == null ? "" : userRecordDTO.getAmount());
                }
                row.add(userRecordDTO.getCouponName() == null ? "" : userRecordDTO.getCouponName());

                row.add(userRecordDTO.getAmount() == null ? "" : userRecordDTO.getAmount());
                if (userRecordDTO.getPayType() == 1) {
                    row.add("支付宝");
                } else {
                    row.add("微信");
                }
                row.add(userRecordDTO.getPayTime() == null ? "" : userRecordDTO.getPayTime());

                rows.add(row);
            }


            data.setRows(rows);

            ExportExcelUtils.exportExcel(response, excelName + ".xlsx", data);
            return ResultVOUtil.success();
        } else {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_RECORD_IS_EMPTY.getCode(), languageType));
        }
    }

    @Override
    public ResultVO exportFinancialInfo(String beginTime, String endTime, Integer page, Integer size, HttpServletResponse response, HttpSession session) throws Exception {
        String languageType = "0";

        if (page == -1) {
            long count = thesaurusRepository.count();
            page = 1;
            size = (int) count;
        }
        ResultVO resultVO = manageMemberService.getAllFinancialInfo(beginTime, endTime,page, size, session);
        if (resultVO.getCode() != 0) {
            return resultVO;
        }

        PageInfoDTO pageInfoDto = (PageInfoDTO) resultVO.getData();
        List<FinancialInfoDTO> financialInfoDTOS = (List<FinancialInfoDTO>) pageInfoDto.getList();


        if (financialInfoDTOS != null && financialInfoDTOS.size() > 0) {


            String excelName = "财务统计报表";
            String column1 = "时间";
            String column2 = "订单总数";
            String column3 = "今日新增";
            String column4 = "订单总金额/元";
            String column5 = "今日新增/元";


            ExcelData data = new ExcelData();
            data.setName(excelName);
            List<String> titles = new ArrayList();
            titles.add(column1);
            titles.add(column2);
            titles.add(column3);
            titles.add(column4);
            titles.add(column5);
            data.setTitles(titles);

            List<List<Object>> rows = new ArrayList();


            for (FinancialInfoDTO financialInfoDTO : financialInfoDTOS) {
                List<Object> row = new ArrayList();


                row.add(financialInfoDTO.getPayTimeString() == null ? "" : financialInfoDTO.getPayTimeString());
                row.add(financialInfoDTO.getTotalCount());
                row.add(financialInfoDTO.getDayCount());
                row.add(financialInfoDTO.getTotalAmount() == null ? "" : financialInfoDTO.getTotalAmount());
                row.add(financialInfoDTO.getDayAmount() == null ? "" : financialInfoDTO.getDayAmount());

                rows.add(row);
            }


            data.setRows(rows);

            ExportExcelUtils.exportExcel(response, excelName + ".xlsx", data);
            return ResultVOUtil.success();
        } else {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PAY_RECORD_IS_EMPTY.getCode(), languageType));
        }    }

    private void putErrorMap(Integer num, String msg, HashMap<Integer, String> errorMap) {

        if (!StringUtils.isEmpty(errorMap.get(num))) {
            errorMap.put(num, errorMap.get(num) + "," + msg);
        } else {
            errorMap.put(num, msg);
        }
    }

}
