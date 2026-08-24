package com.shanzhu.book.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
@Data
public class User implements Serializable {

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String userpassword;

    /**
     * 是否管理员
     */
    private Byte isadmin;

}