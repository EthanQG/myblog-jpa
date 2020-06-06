package com.wqg.blog.service;

import com.wqg.blog.po.User;

/**
 * @Auther: wqg
 * @Description:
 */
public interface UserService {
    User checkUser(String username, String password);
}
