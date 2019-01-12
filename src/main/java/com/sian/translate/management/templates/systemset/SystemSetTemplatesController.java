package com.sian.translate.management.templates.systemset;

import com.sian.translate.DTO.ManageResourceDTO;
import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.dictionary.enity.UserTranslateRecord;
import com.sian.translate.management.log.enity.ManageLog;
import com.sian.translate.management.systemset.service.SystemSetService;
import com.sian.translate.management.user.entity.ManageResource;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.entity.ManageUserRole;
import com.sian.translate.management.user.repository.ManageResourceRepository;
import com.sian.translate.management.user.repository.ManageUserRoleRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.user.entity.SystemConfig;
import com.sian.translate.user.entity.UserFeedback;
import com.sian.translate.user.entity.UserInfo;
import com.sian.translate.user.repository.SystemConfigRepositpory;
import com.sian.translate.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/manage")
public class SystemSetTemplatesController {

    @Autowired
    ManageUserService manageUserService;

    @Autowired
    SystemSetService systemSetService;

    @Autowired
    ManageUserRoleRepository manageUserRoleRepository;

    @Autowired
    ManageResourceRepository manageResourceRepository;

    @Autowired
    SystemConfigRepositpory systemConfigRepositpory;

    @Autowired
    UserService userService;

    @RequestMapping("/system/adminIndex")
    public ModelAndView adminIndex(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                   HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = manageUserService.geteManageUserList(page, size, session);

        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<ManageUserInfo> manageUserInfoList = (List<ManageUserInfo>) pageInfoDTO.getList();
        map.put("manageUserInfoList", manageUserInfoList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());

        return new ModelAndView("/html/admin/index.html", map);
    }

    @RequestMapping("/system/adminEdit")
    public ModelAndView adminEdit(Integer id,HttpSession session, Map<String, Object> map) {

        if (id != null){
            ResultVO resultVO = manageUserService.getManageUserInfo(id, session);
            ManageUserInfo manageUserInfo = (ManageUserInfo) resultVO.getData();

            ResultVO roleList = manageUserService.getRoleList(session);
            List<ManageUserRole> manageUserRoleList = new ArrayList<>();
            manageUserRoleList.addAll((List<ManageUserRole>) roleList.getData());

            map.put("manageUserInfo",manageUserInfo);
            map.put("manageUserRoleList",manageUserRoleList);

        }

        return new ModelAndView("/html/admin/edit.html", map);
    }

    @RequestMapping("/system/adminAdd")
    public ModelAndView adminAdd(HttpSession session, Map<String, Object> map) {

        ResultVO roleList = manageUserService.getRoleList(session);
        List<ManageUserRole> manageUserRoleList = new ArrayList<>();
        manageUserRoleList.addAll((List<ManageUserRole>) roleList.getData());

        map.put("manageUserRoleList",manageUserRoleList);


        return new ModelAndView("/html/admin/add.html", map);
    }


    @RequestMapping("/system/roleIndex")
    public ModelAndView roleIndex(HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = manageUserService.getRoleList(session);

        List<ManageUserRole> manageUserRoleList = (List<ManageUserRole>) resultVO.getData();
        map.put("manageUserRoleList", manageUserRoleList);


        return new ModelAndView("/html/role/index.html", map);
    }

    @RequestMapping("/system/roleAdd")
    public ModelAndView roleAdd(Integer id, HttpSession session, Map<String, Object> map) {


        ResultVO resultVO = manageUserService.getResource(session);
        List<ManageResourceDTO> manageResourceDTOList = (List<ManageResourceDTO>) resultVO.getData();

        map.put("manageResourceDTOList", manageResourceDTOList);


        if (id != null){
            Optional<ManageUserRole> byId = manageUserRoleRepository.findById(id);
            if (byId.isPresent()){
                ManageUserRole manageUserRole = byId.get();
                map.put("manageUserRole", manageUserRole);

                String resoureId = manageUserRole.getResoureId();
                if (!StringUtils.isEmpty(resoureId)){
                    String[] split = resoureId.split(",");
                    List<Integer> ids = new ArrayList<>();
                    for (String s : split) {
                        ids.add(Integer.valueOf(s));
                    }
                    List<ManageResource> manageResourceList = manageResourceRepository.findAllByIdIn(ids);

                    map.put("manageResourceList", manageResourceList);

                }


            }


        }

        return new ModelAndView("/html/role/add.html", map);
    }

    @RequestMapping("/system/roleEdit")
    public ModelAndView roleEdit(Integer id, HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = manageUserService.getResource(session);
        List<ManageResourceDTO> manageResourceDTOList = (List<ManageResourceDTO>) resultVO.getData();

        map.put("manageResourceDTOList", manageResourceDTOList);


        if (id != null){
            Optional<ManageUserRole> byId = manageUserRoleRepository.findById(id);
            if (byId.isPresent()){
                ManageUserRole manageUserRole = byId.get();
                map.put("manageUserRole", manageUserRole);

                String resoureId = manageUserRole.getResoureId();
                if (!StringUtils.isEmpty(resoureId)){
                    String[] split = resoureId.split(",");
                    List<Integer> ids = new ArrayList<>();
                    for (String s : split) {
                        ids.add(Integer.valueOf(s));
                    }
                    List<ManageResource> manageResourceList = manageResourceRepository.findAllByIdIn(ids);

                    map.put("manageResourceList", manageResourceList);

                }


            }


        }

        return new ModelAndView("/html/role/edit.html", map);
    }
    /****
     * 获取用户反馈列表
     * @param nickName 反馈人昵称
     * @param content 反馈内容
     * @return
     */

    @RequestMapping("/system/feedbackPending")
    public ModelAndView feedbackPending(@RequestParam(value = "status", required = false,defaultValue = "0") Integer status,
                                        @RequestParam(value = "nickName", required = false,defaultValue = "") String nickName,
                                        @RequestParam(value = "content", required = false,defaultValue = "") String content,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                        HttpSession session, Map<String, Object> map) {

        //status 0未处理 1 已处理
        ResultVO resultVO = systemSetService.getFeedbackList(status, nickName, content, page, size, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<UserFeedback> userFeedbackList = (List<UserFeedback>) pageInfoDTO.getList();

        map.put("userFeedbackList", userFeedbackList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        map.put("totalElements", pageInfoDTO.getTotalElements());
        map.put("content", content);
        map.put("nickName", nickName);
        map.put("status", status);


        return new ModelAndView("/html/feedback/pending.html", map);
    }

    @RequestMapping("/system/fileIndex")
    public ModelAndView fileIndex(@RequestParam(value = "type", required = false,defaultValue = "0") Integer type,
                                        @RequestParam(value = "field", required = false,defaultValue = "") String field,
                                        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                        HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = systemSetService.getFileList(type, field, page, size, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<SystemConfig> systemConfigList = (List<SystemConfig>) pageInfoDTO.getList();

        map.put("systemConfigList", systemConfigList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        map.put("field", field);
        map.put("type", type);


        return new ModelAndView("/html/file/index.html", map);
    }

    @RequestMapping("/system/fileAdd")
    public ModelAndView fileAdd(HttpSession session, Map<String, Object> map) {

        return new ModelAndView("/html/file/add.html", map);
    }


    @RequestMapping("/system/fileEdit")
    public ModelAndView fileEdit(@RequestParam(value = "id", required = false,defaultValue = "0") Integer id,
                                HttpSession session, Map<String, Object> map) {

        Optional<SystemConfig> byId = systemConfigRepositpory.findById(id);

        if (byId.isPresent()){
            SystemConfig systemConfig = byId.get();
            map.put("systemConfig", systemConfig);
        }

        return new ModelAndView("/html/file/edit.html", map);
    }


    @RequestMapping("/system/translateStatistical")
    public ModelAndView translateStatistical(@RequestParam(value = "type", required = false,defaultValue = "0") Integer type,
                                  @RequestParam(value = "field", required = false,defaultValue = "") String field,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                  HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = systemSetService.getTranslateList(type, field, page, size, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<UserTranslateRecord> userTranslateRecordList = (List<UserTranslateRecord>) pageInfoDTO.getList();


        for (UserTranslateRecord userTranslateRecord : userTranslateRecordList) {
            ResultVO resultVO1 = userService.getUserinfo(userTranslateRecord.getUserId(), "0");
            UserInfo userInfo = (UserInfo) resultVO1.getData();
            if (userInfo != null){
                userTranslateRecord.setHeadImage(userInfo.getHeadSmallImage());
                userTranslateRecord.setVipIcon(userInfo.getVipIcon());
            }
        }

        map.put("userTranslateRecordList", userTranslateRecordList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        map.put("totalElements", pageInfoDTO.getTotalElements());
        map.put("field", field);
        map.put("type", type);


        return new ModelAndView("/html/search/index.html", map);
    }

    @RequestMapping("/system/manageLog")
    public ModelAndView manageLog(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                             HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = systemSetService.getManageLogList(page, size, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<ManageLog> manageLogList = (List<ManageLog>) pageInfoDTO.getList();

        map.put("manageLogList", manageLogList);
        map.put("indexPage", pageInfoDTO.getPage());
        map.put("totalPage", pageInfoDTO.getTotalPages());
        map.put("totalElements", pageInfoDTO.getTotalElements());


        return new ModelAndView("/html/journal/index.html", map);
    }

    @RequestMapping("/system/about")
    public ModelAndView about(@RequestParam(value = "type", required = false,defaultValue = "0") Integer type,
                                  @RequestParam(value = "field", required = false,defaultValue = "") String field,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                  HttpSession session, Map<String, Object> map) {

        ResultVO resultVO = systemSetService.getFileList(1, field, page, size, session);
        PageInfoDTO pageInfoDTO = (PageInfoDTO) resultVO.getData();
        List<SystemConfig> systemConfigList = (List<SystemConfig>) pageInfoDTO.getList();

        if (systemConfigList != null && systemConfigList.size() > 0){
            SystemConfig systemConfig = systemConfigList.get(0);
            map.put("systemConfig", systemConfig);
        }else {
            map.put("systemConfig", new SystemConfig());

        }



        return new ModelAndView("/html/about/index.html", map);
    }


}
