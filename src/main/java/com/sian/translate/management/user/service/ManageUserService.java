package com.sian.translate.management.user.service;

import com.sian.translate.VO.ResultVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface ManageUserService {

    String SESSION_KEY = "manage_user";

    /**登陆**/
    ResultVO login(String phone, String password, HttpSession session);

    /**新增角色**/
    ResultVO addRole(String roleName, String resoureId, Integer status, HttpSession session);

    /**修改角色**/
    ResultVO editRole(Integer id, String roleName, String resoureId, Integer status, HttpSession session);

    /**获取模块列表**/
    ResultVO getResource(HttpSession session);

    /**删除角色**/
    ResultVO deleteRole(Integer id, HttpSession session);

    /**获取角色列表**/
    ResultVO getRoleList(HttpSession session);

    /**新增管理员**/
    ResultVO addManageUser(Integer roleId, String account, String userName, String phone, String password, Integer status, MultipartFile image, HttpSession session);

    /**删除管理员**/
    ResultVO deleteManageUser(Integer id, HttpSession session);

    /**获取管理员列表**/
    ResultVO geteManageUserList(Integer page, Integer size, HttpSession session);

    ResultVO getManageUserInfo(Integer id, HttpSession session);

    ResultVO editManageUser(Integer id, Integer roleId, String account, String userName, String phone, String password, Integer status, String image, HttpSession session);
}
