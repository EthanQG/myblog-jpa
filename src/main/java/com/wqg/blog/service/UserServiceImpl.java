package com.wqg.blog.service;

import com.wqg.blog.Util.Md5Encrypt;
import com.wqg.blog.dao.UserRepository;
import com.wqg.blog.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wqg
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User checkUser(String username, String password) {
        User user=userRepository.findByUsernameAndPassword(username, Md5Encrypt.md5(password));
        return user;
    }
}
