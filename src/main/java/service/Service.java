package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Service {
    private static final Logger log = LoggerFactory.getLogger(Service.class);

    public void signup(User user) {
        log.debug("signup called");
        // 중복되는 유저 확인
        User existUser = DataBase.findUserById(user.getUserId());
        if (existUser != null) {
            throw new IllegalStateException("이미 존재하는 유저입니다.");
        }
        DataBase.addUser(user);
    }

    public User login(String userId, String password) {
        log.debug("login called");
        // 유저 존재하는지 확인
        User existUser = DataBase.findUserById(userId);
        if (existUser == null) {
            throw new IllegalStateException("존재하지 않는 유저입니다.");
        }
        if (!existUser.getPassword().equals(password)) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
        return existUser;
    }
}
