package com.wqg.blog.dao;

import com.wqg.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: wqg
 * @Description:
 */
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);
}
