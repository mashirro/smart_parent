package com.mashirro.web.controller;

import com.mashirro.framework.utils.DatabaseUtil;
import com.mashirro.web.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {

        logger.debug("debug.......");

        String sql = "select * from user where id = ?";
        User user = DatabaseUtil.queryEntity(User.class, sql, "bb7e8835-8279-4350-a005-2e76d818a8b4");
        System.out.println(user);

    }
}
