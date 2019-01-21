package com.sian.translate.management.user.controller;

import com.sian.translate.VO.ResultVO;
import com.sian.translate.management.user.service.ManageUserService;
import com.sian.translate.utlis.RandomValidateCodeUtil;
import com.sian.translate.utlis.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/manage/user")
public class ManageUserController {


    @Autowired
    ManageUserService manageUserService;


    /****
     * 获取模块列表
     * @return
     */
    @GetMapping(value = "/getResource", produces = "application/json;charset=UTF-8")
    ResultVO getResource(HttpSession session) {

        ResultVO resultVO = manageUserService.getResource(session);
        return resultVO;
    }

    /****
     * 新增角色
     * @param roleName 角色名称
     * @param resoureId 资源id，多个逗号隔开
     * @param status 0正常1禁用
     * @return
     */
    @PostMapping(value = "/addRole", produces = "application/json;charset=UTF-8")
    ResultVO addRole(@RequestParam(value = "roleName", required = false) String roleName,
                     @RequestParam(value = "resoureId", required = false) String resoureId,
                     @RequestParam(value = "status", required = false) Integer status,
                     HttpSession session) {
        ResultVO resultVO = manageUserService.addRole(roleName, resoureId, status, session);
        return resultVO;
    }

    /****
     * 修改角色
     * @param id 角色id
     * @param roleName 角色名称
     * @param resoureId 资源id，多个逗号隔开
     * @param status 0正常1禁用
     * @return
     */
    @PostMapping(value = "/editRole", produces = "application/json;charset=UTF-8")
    ResultVO editRole(@RequestParam(value = "id", required = false) Integer id,
                      @RequestParam(value = "roleName", required = false) String roleName,
                      @RequestParam(value = "resoureId", required = false) String resoureId,
                      @RequestParam(value = "status", required = false) Integer status,
                      HttpSession session) {
        ResultVO resultVO = manageUserService.editRole(id, roleName, resoureId, status, session);
        return resultVO;
    }

    /****
     * 删除角色
     * @param id 角色id
     * @return
     */
    @PostMapping(value = "/deleteRole", produces = "application/json;charset=UTF-8")
    ResultVO deleteRole(@RequestParam(value = "id", required = false) Integer id,
                        HttpSession session) {
        ResultVO resultVO = manageUserService.deleteRole(id, session);
        return resultVO;
    }

    /****
     * 获取角色列表
     * @return
     */
    @GetMapping(value = "/getRoleList", produces = "application/json;charset=UTF-8")
    ResultVO getRoleList(HttpSession session) {
        ResultVO resultVO = manageUserService.getRoleList(session);
        return resultVO;
    }

    /****
     * 新增管理员
     * @param roleId 角色id
     * @param account 用户名
     * @param userName 真实姓名
     * @param phone 手机号码
     * @param password 密码
     * @param status 0正常1禁用
     * @param image 头像
     * @return
     */
    @PostMapping(value = "/addManageUser", produces = "application/json;charset=UTF-8")
    ResultVO addManageUser(@RequestParam(value = "roleId", required = false) Integer roleId,
                           @RequestParam(value = "account", required = false) String account,
                           @RequestParam(value = "userName", required = false) String userName,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "status", required = false) Integer status,
                           @RequestParam(value = "image", required = false) String image,
                           HttpSession session) {
        ResultVO resultVO = manageUserService.addManageUser(roleId, account, userName, phone, password, status, image, session);
        return resultVO;
    }

    /****
     * 新增管理员
     * @param roleId 角色id
     * @param account 用户名
     * @param userName 真实姓名
     * @param phone 手机号码
     * @param password 密码
     * @param status 0正常1禁用
     * @param image 头像
     * @return
     */
    @PostMapping(value = "/editManageUser", produces = "application/json;charset=UTF-8")
    ResultVO editManageUser(@RequestParam(value = "id", required = false) Integer id,
                           @RequestParam(value = "roleId", required = false) Integer roleId,
                           @RequestParam(value = "account", required = false) String account,
                           @RequestParam(value = "userName", required = false) String userName,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "status", required = false) Integer status,
                           @RequestParam(value = "image", required = false) String image,
                           HttpSession session) {
        ResultVO resultVO = manageUserService.editManageUser(id,roleId, account, userName, phone, password, status, image, session);
        return resultVO;
    }

    /****
     * 删除管理员
     * @param id 用户id
     * @return
     */
    @PostMapping(value = "/deleteManageUser", produces = "application/json;charset=UTF-8")
    ResultVO deleteManageUser(@RequestParam(value = "id", required = false) Integer id,
                              HttpSession session) {
        ResultVO resultVO = manageUserService.deleteManageUser(id, session);
        return resultVO;
    }

    /****
     * 获取管理员列表
     * @return
     */
    @GetMapping(value = "/geteManageUserList", produces = "application/json;charset=UTF-8")
    ResultVO geteManageUserList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                              HttpSession session) {
        ResultVO resultVO = manageUserService.geteManageUserList(page,size, session);
        return resultVO;
    }


    /****
     *  管理员登陆
     * @return
     */
    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    ResultVO login(@RequestParam(value = "account", required = false) String account,
                   @RequestParam(value = "password", required = false) String password,
                   @RequestParam(value = "verifycode", required = false) String verifycode,
                   HttpSession session) {

        if (!checkVerify(verifycode,session)){
            return ResultVOUtil.error("验证码错误");
        }


        ResultVO resultVO = manageUserService.login(account, password, session);
        return resultVO;
    }

    /****
     *  获取用户信息
     * @return
     */
    @GetMapping(value = "/getManageUserInfo", produces = "application/json;charset=UTF-8")
    ResultVO getManageUserInfo(@RequestParam(value = "id", required = false) Integer id,HttpSession session) {

        ResultVO resultVO = manageUserService.getManageUserInfo(id, session);
        return resultVO;
    }

    /****
     *  退出登陆
     * @return
     */
    @PostMapping(value = "/quitLogin", produces = "application/json;charset=UTF-8")
    ResultVO quitLogin(HttpSession session) {

        session.removeAttribute(ManageUserService.SESSION_KEY);//清空session信息
        session.invalidate();//清除 session 中的所有信息

        return ResultVOUtil.success();
    }


    /**
     * 生成验证码
     */
    @RequestMapping(value = "/imagecode")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
        }
    }


    /**
     * 验证码效验
     */
    public boolean checkVerify(String inputStr, HttpSession session) {
        try{
            //从session中获取随机数
            String random = (String) session.getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
            if (random == null) {
                return false;
            }
            if (random.equals(inputStr)) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

}
