package com.sian.translate.management.user.service.impl;

import com.sian.translate.DTO.ManageResourceDTO;
import com.sian.translate.DTO.PageInfoDTO;
import com.sian.translate.VO.ResultVO;
import com.sian.translate.hint.enums.HintMessageEnum;
import com.sian.translate.hint.service.HintMessageService;
import com.sian.translate.management.user.entity.ManageResource;
import com.sian.translate.management.user.entity.ManageUserInfo;
import com.sian.translate.management.user.entity.ManageUserRole;
import com.sian.translate.management.user.repository.ManageResourceRepository;
import com.sian.translate.management.user.repository.ManageUserInfoRepository;
import com.sian.translate.management.user.repository.ManageUserRoleRepository;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.CommonUtlis;
import com.sian.translate.utlis.ImageUtlis;
import com.sian.translate.utlis.JsonUtil;
import com.sian.translate.utlis.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ManageUserServiceImpl implements ManageUserService {


    @Autowired
    ManageUserInfoRepository manageUserInfoRepository;

    @Autowired
    HintMessageService hintMessageService;

    @Autowired
    ManageUserRoleRepository manageUserRoleRepository;

    @Autowired
    ManageResourceRepository manageResourceRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /*******
     *
     * @param account 登录名
     * @param password 密码
     * @return
     */
    @Override
    public ResultVO login(String account, String password, HttpSession session){


        String type = "0";

        if (StringUtils.isEmpty(account))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ACCOUNT_NOT_EMPTY.getCode(), type));
        if (StringUtils.isEmpty(password))
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PASSWORD_NOT_EMPTY.getCode(), type));



        ManageUserInfo userInfo = manageUserInfoRepository.findByAccount(account);
        if (userInfo == null)
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), type));

        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(userInfo.getPassword())) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.PASSWORD_ERROR.getCode(), type));
        }
        if (userInfo.getUserStatus() == 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_PROHIBIT.getCode(), type));
        }

        Integer userID = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);
        log.info("seesion={}",userID);
        session.setAttribute(ManageUserService.SESSION_KEY, userInfo.getId());

        Optional<ManageUserRole> byId = manageUserRoleRepository.findById(userInfo.getRole());
        
        if (byId.isPresent()){
            ManageUserRole manageUserRole = byId.get();
            String resoureId = manageUserRole.getResoureId();
            if (!StringUtils.isEmpty(resoureId)){
                String[] split = resoureId.split(",");
                List<Integer> ids = new ArrayList<>();
                for (String s : split) {
                    ids.add(Integer.valueOf(s));
                }
                List<ManageResource> manageResourceList = manageResourceRepository.findAllByIdIn(ids);
                userInfo.setManageResourceList(manageResourceList);
            }
        }
        stringRedisTemplate.opsForValue().set("user_"+userID, JsonUtil.toJson(userInfo));

        return ResultVOUtil.success(userInfo);
    }

    @Override
    public ResultVO addRole(String roleName, String resoureId, Integer status, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (StringUtils.isEmpty(roleName)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_NAME_IS_NOT_EMPTY.getCode(), languageType));
        }

        if (status == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.STATUS_IS_NOT_EMPTY.getCode(), languageType));
        }

        long count = manageUserRoleRepository.countByRoleName(roleName);

        if (count > 0){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_NAME_IS_EXIST.getCode(), languageType));
        }

        ManageUserRole manageUserRole = new ManageUserRole();

        manageUserRole.setRoleName(roleName);
        if (!StringUtils.isEmpty(resoureId)){
            manageUserRole.setResoureId(resoureId);
        }
        manageUserRole.setStatus(status);
        manageUserRole.setCreateUser(userId);
        manageUserRole.setUpdateUser(userId);
        manageUserRole.setCreateTime(new Date());
        manageUserRole.setUpdateTime(new Date());

        manageUserRoleRepository.save(manageUserRole);

        String losmsg = "新增角色"+manageUserRole.getRoleName();


        return ResultVOUtil.success(manageUserRole,losmsg);
    }

    /**
     * 角色id
     *
     * @param id
     * @param roleName
     * @param resoureId
     * @param status
     * @param session
     **/
    @Override
    public ResultVO editRole(Integer id, String roleName, String resoureId, Integer status, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_ID_IS_NOT_EMPTY.getCode(), languageType));
        }

        Optional<ManageUserRole> manageUserRoleOptional = manageUserRoleRepository.findById(id);

        if (!manageUserRoleOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_IS_NOT_EXIST.getCode(), languageType));
        }


        ManageUserRole manageUserRole = manageUserRoleOptional.get();

        if (!StringUtils.isEmpty(roleName)){

            long count = manageUserRoleRepository.countByRoleName(roleName);
            boolean isSelf = roleName.equals(manageUserRole.getRoleName());

            if (count > 0 && !isSelf){
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_NAME_IS_EXIST.getCode(), languageType));
            }
            manageUserRole.setRoleName(roleName);
        }else {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_NAME_IS_NOT_EMPTY.getCode(), languageType));
        }
        if (!StringUtils.isEmpty(resoureId)){
            manageUserRole.setResoureId(resoureId);
        }else {
            manageUserRole.setResoureId("");
        }

        if (status != null){
            manageUserRole.setStatus(status);
        }
        manageUserRole.setUpdateUser(userId);
        manageUserRole.setUpdateTime(new Date());

        manageUserRoleRepository.save(manageUserRole);

        String losmsg = "编辑角色"+manageUserRole.getRoleName();


        return ResultVOUtil.success(manageUserRole,losmsg);
    }

    /**
     * 获取模块列表
     *
     * @param session
     **/
    @Override
    public ResultVO getResource(HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        List<ManageResource> manageResourceList = manageResourceRepository.findAll();

        List<ManageResourceDTO> manageResourceDTOList = new ArrayList<>();

        Map<String, List<ManageResource>> collect = manageResourceList.stream().collect(Collectors.groupingBy(ManageResource::getParentName));

        for (Map.Entry<String, List<ManageResource>> stringListEntry : collect.entrySet()) {
            ManageResourceDTO manageResourceDTO = new ManageResourceDTO();
            manageResourceDTO.setParentName(stringListEntry.getKey());
            List<HashMap<String,String>> resourceList = new ArrayList<>();

            List<ManageResource> manageResources = stringListEntry.getValue();

            for (ManageResource manageResource : manageResources) {
                HashMap<String,String> map = new HashMap<>();
                map.put("id", manageResource.getId()+"");
                map.put("resourceName", manageResource.getResourceName()+"");
                resourceList.add(map);
            }
            manageResourceDTO.setResourceList(resourceList);
            manageResourceDTOList.add(manageResourceDTO);
        }

        return ResultVOUtil.success(manageResourceDTOList);
    }

    /**
     * 删除角色
     *
     * @param id
     * @param session
     **/
    @Override
    public ResultVO deleteRole(Integer id, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_ID_IS_NOT_EMPTY.getCode(), languageType));
        }

        Optional<ManageUserRole> manageUserRoleOptional = manageUserRoleRepository.findById(id);

        if (!manageUserRoleOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_IS_NOT_EXIST.getCode(), languageType));
        }
        ManageUserRole manageUserRole = manageUserRoleOptional.get();
        manageUserRoleRepository.deleteById(id);


        String losmsg = "编辑角色"+manageUserRole.getRoleName();

        return ResultVOUtil.success(losmsg);
    }

    /**
     * 获取角色列表
     *
     * @param session
     **/
    @Override
    public ResultVO getRoleList(HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        List<ManageUserRole> all = manageUserRoleRepository.findAll();

        return ResultVOUtil.success(all);
    }

    /**
     * 新增管理员
     *
     * @param roleId
     * @param account
     * @param userName
     * @param phone
     * @param password
     * @param status
     * @param image
     * @param session
     **/
    @Override
    public ResultVO addManageUser(Integer roleId, String account, String userName, String phone, String password, Integer status, MultipartFile image, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (roleId == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_ID_IS_NOT_EMPTY.getCode(), languageType));
        }
        Optional<ManageUserRole> manageUserRoleOptional = manageUserRoleRepository.findById(roleId);

        if (!manageUserRoleOptional.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_IS_NOT_EXIST.getCode(), languageType));
        }
        if (StringUtils.isEmpty(account)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_ACCOUNT_NOT_EMPTY.getCode(), languageType));
        }

        long accountCount = manageUserInfoRepository.countByAccount(account);

        if (accountCount > 0){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_ACCOUNT_IS_EXIST.getCode(), languageType));
        }

        if (account.length() > 12 || account.length() < 4)
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_ACCOUNT_LENGTH_ERROR.getCode(), languageType));

        if (!CommonUtlis.isNumerOrLetter(account)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_ACCOUNT_FORMAT_ERROR.getCode(), languageType));
        }

        if (StringUtils.isEmpty(userName)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_USERNAME_NOT_EMPTY.getCode(), languageType));
        }

        if (status == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.STATUS_IS_NOT_EMPTY.getCode(), languageType));
        }


        String imagePath = "";

        if (image != null && !image.isEmpty()){
            try {
                imagePath = ImageUtlis.loadImage(image);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.IMG_FORMAT_ERROR.getCode(), languageType));
            }
        }

        ManageUserRole manageUserRole = manageUserRoleOptional.get();

        ManageUserInfo manageUserInfo = new ManageUserInfo();
        manageUserInfo.setAccount(account);
        manageUserInfo.setUsername(userName);
        if (!StringUtils.isEmpty(phone)){
            manageUserInfo.setPhone(phone);
        }
        if (StringUtils.isEmpty(password)){
            password = "123456";
        }
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        manageUserInfo.setPassword(md5Password);

        if (!StringUtils.isEmpty(imagePath)){
            manageUserInfo.setHeadImage(imagePath);
        }

        manageUserInfo.setRole(roleId);
        manageUserInfo.setRoleName(manageUserRole.getRoleName());
        manageUserInfo.setUserStatus(status);
        manageUserInfo.setCreateUser(userId);
        manageUserInfo.setUpdateUser(userId);
        manageUserInfo.setCreateTime(new Date());
        manageUserInfo.setUpdateTime(new Date());

        manageUserInfoRepository.save(manageUserInfo);

        String losmsg = "新增管理员"+manageUserInfo.getAccount();

        return ResultVOUtil.success(manageUserInfo,losmsg);
    }

    /**
     * 删除管理员
     *
     * @param id
     * @param session
     **/
    @Override
    public ResultVO deleteManageUser(Integer id, HttpSession session) {
        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        if (id == null){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }


        Optional<ManageUserInfo> byId = manageUserInfoRepository.findById(id);

        if (!byId.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        ManageUserInfo manageUserInfo = byId.get();

        manageUserInfoRepository.deleteById(id);

        String losmsg = "删除管理员"+manageUserInfo.getAccount();

        return ResultVOUtil.success(losmsg);
    }

    /**
     * 获取管理员列表
     *
     * @param page
     * @param size
     * @param session
     **/
    @Override
    public ResultVO geteManageUserList(Integer page, Integer size, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(userId);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }

        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");

        if (page < 1){
            page = 1;
        }
        if (size < 1){
            size = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<ManageUserInfo> manageUserInfoPage = manageUserInfoRepository.findAll(pageable);

        PageInfoDTO pageInfoDTO = new PageInfoDTO();

        pageInfoDTO.setTotalPages(manageUserInfoPage.getTotalPages());
        pageInfoDTO.setTotalElements((int)manageUserInfoPage.getTotalElements());
        pageInfoDTO.setPage(page);
        pageInfoDTO.setSize(size);
        pageInfoDTO.setList(manageUserInfoPage.getContent());

        return ResultVOUtil.success(pageInfoDTO);
    }

    @Override
    public ResultVO getManageUserInfo(Integer id, HttpSession session) {


        String userInfoString = stringRedisTemplate.opsForValue().get("user_" + id);

        ManageUserInfo manageUserInfo = null;
        if (!StringUtils.isEmpty(userInfoString)) {
            manageUserInfo = (ManageUserInfo) JsonUtil.fromJson(userInfoString, ManageUserInfo.class);
        }else {
            Optional<ManageUserInfo> manageUserInfoOptiona = manageUserInfoRepository.findById(id);

            manageUserInfo = manageUserInfoOptiona.get();

            session.setAttribute(ManageUserService.SESSION_KEY, manageUserInfo.getId());

            Optional<ManageUserRole> byId = manageUserRoleRepository.findById(manageUserInfo.getRole());

            if (byId.isPresent()){
                ManageUserRole manageUserRole = byId.get();
                String resoureId = manageUserRole.getResoureId();
                if (!StringUtils.isEmpty(resoureId)){
                    String[] split = resoureId.split(",");
                    List<Integer> ids = new ArrayList<>();
                    for (String s : split) {
                        ids.add(Integer.valueOf(s));
                    }
                    List<ManageResource> manageResourceList = manageResourceRepository.findAllByIdIn(ids);
                    manageUserInfo.setManageResourceList(manageResourceList);
                }
            }
            stringRedisTemplate.opsForValue().set("user_"+id, JsonUtil.toJson(manageUserInfo));
        }


        return ResultVOUtil.success(manageUserInfo);
    }

    @Override
    public ResultVO editManageUser(Integer id, Integer roleId, String account, String userName, String phone, String password, Integer status, String image, HttpSession session) {

        String languageType = "0";

        Integer userId = (Integer) session.getAttribute(ManageUserService.SESSION_KEY);

        if (StringUtils.isEmpty(userId)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.NOT_LOGIN.getCode(), languageType));
        }

        if (StringUtils.isEmpty(id)) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ID_NOT_EMPTY.getCode(), languageType));
        }

        Optional<ManageUserInfo> manageUserInfoOptional = manageUserInfoRepository.findById(id);

        if (!manageUserInfoOptional.isPresent()) {
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.USER_NOT_EXIST.getCode(), languageType));
        }
        if (roleId == null || roleId < 1){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_ID_IS_NOT_EMPTY.getCode(), languageType));
        }


        Optional<ManageUserRole> manageUserRoleOptiona = manageUserRoleRepository.findById(roleId);

        if (!manageUserRoleOptiona.isPresent()){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ROLE_IS_NOT_EXIST.getCode(), languageType));
        }

        if (StringUtils.isEmpty(account)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.ACCOUNT_NOT_EMPTY.getCode(), languageType));
        }

        long accountCount = manageUserInfoRepository.countByAccount(account);
        ManageUserInfo manageUserInfo = manageUserInfoOptional.get();

        if (accountCount > 0 && !account.equals(manageUserInfo.getAccount())){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_ACCOUNT_IS_EXIST.getCode(), languageType));
        }

        if (account.length() > 12 || account.length() < 4)
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_ACCOUNT_LENGTH_ERROR.getCode(), languageType));

        if (!CommonUtlis.isNumerOrLetter(account)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_ACCOUNT_FORMAT_ERROR.getCode(), languageType));
        }
        if (StringUtils.isEmpty(userName)){
            return ResultVOUtil.error(hintMessageService.getHintMessage(HintMessageEnum.MANAGE_USERNAME_NOT_EMPTY.getCode(), languageType));
        }

        ManageUserRole manageUserRole = manageUserRoleOptiona.get();


        manageUserInfo.setRoleName(manageUserRole.getRoleName());
        manageUserInfo.setRole(roleId);
        manageUserInfo.setAccount(account);
        manageUserInfo.setUsername(userName);
        manageUserInfo.setPhone(phone);

        if (!StringUtils.isEmpty(password)){

            String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
            manageUserInfo.setPassword(md5Password);

        }

        if (!StringUtils.isEmpty(image)){
            manageUserInfo.setHeadImage(image);
        }else {
            manageUserInfo.setHeadImage("");
        }

        manageUserInfo.setUserStatus(status);
        manageUserInfo.setUpdateUser(userId);
        manageUserInfo.setUpdateTime(new Date());

        String resoureId = manageUserRole.getResoureId();
        if (!StringUtils.isEmpty(resoureId)){
            String[] split = resoureId.split(",");
            List<Integer> ids = new ArrayList<>();
            for (String s : split) {
                ids.add(Integer.valueOf(s));
            }
            List<ManageResource> manageResourceList = manageResourceRepository.findAllByIdIn(ids);
            manageUserInfo.setManageResourceList(manageResourceList);
        }

        manageUserInfoRepository.save(manageUserInfo);

        String losmsg = "编辑用户"+manageUserInfo.getAccount();

        stringRedisTemplate.opsForValue().set("user_"+id, JsonUtil.toJson(manageUserInfo));


        return ResultVOUtil.success(manageUserInfo,losmsg);


    }


}
