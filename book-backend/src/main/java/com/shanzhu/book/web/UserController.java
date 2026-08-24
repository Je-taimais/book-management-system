package com.shanzhu.book.web;

import com.shanzhu.book.model.User;
import com.shanzhu.book.service.UserService;
import com.shanzhu.book.utils.PageUtils;
import com.shanzhu.book.utils.R;
import com.shanzhu.book.utils.TokenProcessor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户 控制层
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 登录
     *
     * @param user 用户信息
     * @return 登录信息
     */
    @RequestMapping(value = "/login")
    public Map<String, Object> login(@RequestBody User user) {
        // 登录
        User userObj = userService.login(user);
        if (userObj == null) {
            // 账号或密码错误
            return R.getResultMap(420, "账号或密码错误");
        } else {
            // 创建token
            String token = TokenProcessor.getInstance().generateToken();
            // 保存到Redis
            userService.saveUser(token, userObj);
            // 返回结果对象
            return R.getResultMap(200, "登录成功",
                    new HashMap<String, String>() {{
                        put("token", token);
                    }});
        }
    }

    /**
     * 查看用户信息
     *
     * @param token 用户token
     * @return 用户信息
     */
    @RequestMapping(value = "/info")
    public Map<String, Object> info(String token) {
        // 从redis中取用户
        User user = userService.getUser(token);
        if (user == null) {
            // 获取失败
            return R.getResultMap(420, "获取用户信息失败");
        } else {
            // 获取成功
            return R.getResultMap(200, "获取用户信息成功", user);
        }
    }

    /**
     * 退出登录
     *
     * @param token 用户token
     * @return 结果
     */
    @RequestMapping(value = "/logout")
    public Map<String, Object> logout(String token) {
        // 从redis中移除用户
        userService.removeUser(token);
        return R.getResultMap(200, "退出登录成功");
    }

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    @RequestMapping(value = "/register")
    public Integer register(String username, String password) {
        return userService.register(username, password);
    }

    /**
     * 修改密码
     *
     * @param userid      用户id
     * @param username    用户名
     * @param isadmin     是否管理员
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果
     */
    @RequestMapping(value = {"/alterPassword", "reader/alterPassword"})
    public Integer alterPassword(
            Integer userid, String username,
            Byte isadmin, String oldPassword,
            String newPassword
    ) {
        //检查旧密码是否正确
        User userObj = new User();
        userObj.setUserid(userid);
        userObj.setUsername(username);
        userObj.setUserpassword(oldPassword);
        userObj.setIsadmin(isadmin);

        User user = userService.login(userObj);
        if (user == null) {  //旧密码不正确
            return 0;
        } else {    //旧密码正确，设置新密码
            userService.setPassword(userObj.getUserid(), newPassword);
            return 1;
        }
    }

    /**
     * 获得数量
     *
     * @return 数量
     */
    @GetMapping(value = "/getCount")
    public Integer getCount() {
        return userService.getCount();
    }

    /**
     * 查询所有用户
     * @return 用户
     */
    @GetMapping(value = "/queryUsers")
    public List<User> queryUsers() {
        return userService.queryUsers();
    }


    /**
     * 分页查询用户
     *
     * @param params {page, limit, username}
     * @return 用户信息
     */
    @GetMapping(value = "/queryUsersByPage")
    public Map<String, Object> queryUsersByPage(@RequestParam Map<String, Object> params) {
        PageUtils.parsePageParams(params);
        int count = userService.getSearchCount(params);
        List<User> users = userService.searchUsersByPage(params);
        return R.getListResultMap(0, "success", count, users);
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 结果
     */
    @PostMapping(value = "/addUser")
    public Integer addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 删除用户
     *
     * @param user 用户信息
     * @return 结果
     */
    @DeleteMapping(value = "/deleteUser")
    public Integer deleteUser(@RequestBody User user) {
        return userService.deleteUser(user);
    }

    /**
     * 批量删除用户
     *
     * @param users 用户信息
     * @return 结果
     */
    @DeleteMapping(value = "/deleteUsers")
    public Integer deleteUsers(@RequestBody List<User> users) {
        return userService.deleteUsers(users);
    }

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 结果
     */
    @RequestMapping(value = "/updateUser")
    public Integer updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

}
