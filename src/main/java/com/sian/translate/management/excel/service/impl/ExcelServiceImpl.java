package com.sian.translate.management.excel.service.impl;

import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.Dictionary;
import com.sian.translate.dictionary.repository.DictionaryRepository;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
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
    UserInfoRepository userInfoRepository;

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Override
    public ResultVO exportmemberUserInfo(Integer isMember, HttpServletResponse response, Integer page, Integer size, HttpSession session) throws Exception {


        if (page == -1) {
            long count = userInfoRepository.count();
            page = 1;
            size = (int) count;
        }
        ResultVO memberListResultVO = manageMemberService.getMemberList(isMember, page, size, session);
        if (memberListResultVO.getCode() != 0) {
            return memberListResultVO;
        }
        PageInfoDTO pageInfoDto = (PageInfoDTO) memberListResultVO.getData();
        List<UserInfo> userInfoList = (List<UserInfo>) pageInfoDto.getList();


        String excelName = "";
        if (isMember == -1) {
            excelName = "用户信息";
        } else if (isMember == 0) {
            excelName = "非会员用户信息";
        } else {
            excelName = "会员用户信息";
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
        titles.add("是否为会员（0 不是 1是）");
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
                    && userInfo.getMemberEndTime() != null
                    && CommonUtlis.isEffectiveDate(new Date(), userInfo.getMemberBeginTime(), userInfo.getMemberEndTime())) {
                row.add(1);
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
    public ResultVO importDictionary(MultipartFile file, HttpSession session) throws IOException {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }
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
        Dictionary dictionary = null;
        List<Dictionary> dictionaryList = new ArrayList<>();

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
            Cell cell2 = row.getCell(2);
            Cell cell3 = row.getCell(3);
            Cell cell4 = row.getCell(4);

            String chinese = null;
            if (cell0 != null) {
                cell0.setCellType(CellType.STRING);
                chinese = cell0.getStringCellValue();
            }

            String zang = null;
            if (cell1 != null) {
                cell1.setCellType(CellType.STRING);
                zang = cell1.getStringCellValue();
            }

            String sanskirt = null;
            if (cell2 != null) {
                cell2.setCellType(CellType.STRING);
                sanskirt = cell2.getStringCellValue();
            }
            String japanese = null;
            if (cell3 != null) {
                cell3.setCellType(CellType.STRING);
                japanese = cell3.getStringCellValue();
            }

            String english = null;
            if (cell4 != null) {
                cell4.setCellType(CellType.STRING);
                english = cell4.getStringCellValue();
            }

            long chineseCount = 0;
            long zangCount = 0;
            long sanskirtCount = 0;
            long japaneseCount = 0;
            long englishCount = 0;

            if (chinese == null || chinese.trim().equals("")) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_CHINESE_NOT_EMPTY.getCode(), languageType), errorMap);
            }else{
                chineseCount = dictionaryRepository.countByChinese(chinese.trim());
            }

            if (zang == null || zang.trim().equals("")) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_ZANG_NOT_EMPTY.getCode(), languageType), errorMap);
            }else{
                zangCount = dictionaryRepository.countByZang(zang.trim());
            }

            if (sanskirt == null || sanskirt.trim().equals("")) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_SANSKIRT_NOT_EMPTY.getCode(), languageType), errorMap);
            }else{
                sanskirtCount = dictionaryRepository.countBySanskirt(sanskirt.trim());
            }

            if (japanese == null || japanese.trim().equals("")) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_JAPANESE_EMPTY.getCode(), languageType), errorMap);
            }else{
                japaneseCount = dictionaryRepository.countByJapanese(japanese.trim());
            }

            if (english == null || english.trim().equals("")) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_ENGLISH_NOT_EMPTY.getCode(), languageType), errorMap);
            }else{
                englishCount = dictionaryRepository.countByEnglish(english.trim());
            }


            if (chineseCount > 0) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_CHINESE_EXIST.getCode(), languageType), errorMap);
            }
            if (zangCount > 0) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_ZANG_EXIST.getCode(), languageType), errorMap);
            }
            if (sanskirtCount > 0) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_SANSKIRT_EXIST.getCode(), languageType), errorMap);
            }
            if (japaneseCount > 0) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_JAPANESE_EXIST.getCode(), languageType), errorMap);
            }
            if (englishCount > 0) {
                isError = true;
                putErrorMap((r + 1), hintMessageService.getHintMessage(HintMessageEnum.EXCEL_ENGLISH_EXIST.getCode(), languageType), errorMap);
            }


            if (!isError) {
                dictionary = new Dictionary();
                dictionary.setChinese(chinese.trim());
                dictionary.setZang(zang.trim());
                dictionary.setSanskirt(sanskirt.trim());
                dictionary.setJapanese(japanese.trim());
                dictionary.setEnglish(english.trim());
                dictionary.setUserId(userId);
                dictionary.setCreateTime(new Date());
                dictionary.setUpdateTime(new Date());
                dictionaryList.add(dictionary);
            }

        }
        List<Dictionary> result = dictionaryRepository.saveAll(dictionaryList);

        if (errorMap.isEmpty()) {
            return ResultVOUtil.success("上传成功" + result.size() + "条记录");
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

    private void putErrorMap(Integer num, String msg, HashMap<Integer, String> errorMap) {

        if (!StringUtils.isEmpty(errorMap.get(num))) {
            errorMap.put(num, errorMap.get(num) + "," + msg);
        } else {
            errorMap.put(num, msg);
        }
    }

}
