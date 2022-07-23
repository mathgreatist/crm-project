package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bjpowernode.crm.commons.contants.Contants.RETURN_OBJECT_CODE_SUCCESS;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        //请求转发到登录页面
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request){
        System.out.println("已提交至git,third commit");
        System.out.println("hello hot-fix");
        System.out.println("master test");

        System.out.println("hot-fix test");

        System.out.println("login.do接收到的参数是：loginAct="+loginAct+",loginPwd="+loginPwd+",isRemPwd="+isRemPwd);
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用service层方法，查询用户
        User user = userService.queryUserByLoginActAndPwd(map);
        System.out.println("loginAct的值为："+loginAct+",loginPwd的值为："+loginPwd);
        //根据查询结果，生成响应信息
        ReturnObject returnObject = new ReturnObject();
        System.out.println("");
        if(user == null){
            //登录失败，用户名或密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else { //进一步判断账号是否合法
            if(DateUils.formatDateTime(new Date()).compareTo(user.getExpireTime())>0){
                //登录失败，账号已过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                //登录失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else {
                //登录成功
                returnObject.setCode(RETURN_OBJECT_CODE_SUCCESS);
            }
        }
        System.out.println("code的值为："+returnObject.getCode()+",message的值为："+returnObject.getMessage()+",login.do接收到的参数是：loginAct="+loginAct+",loginPwd="+loginPwd+",isRemPwd="+isRemPwd);
        return returnObject;
    }
}
