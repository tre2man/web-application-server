package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class Service {
    private static final Logger log = LoggerFactory.getLogger(Service.class);
    public void signup(User user) {
        User existUser = DataBase.findUserById(user.getUserId());
        if (existUser != null) {
            throw new IllegalStateException("이미 존재하는 사용자입니다.");
        }
        DataBase.addUser(user);
        Collection<User> users = findAll();
        for (User user1 : users) {
            log.debug(user1.getUserId());
        }
    }

    public boolean login(String userId, String password) {
        User user = DataBase.findUserById(userId);
        Collection<User> users = findAll();
        for (User user1 : users) {
            log.debug(user1.getUserId());
        }
        if (user == null) {
            log.error("login failed : " + userId);
            return false;
        }
        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
        return true;
    }

    public String findUsersData() {
        Collection<User> users = findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private Collection<User> findAll() {
        return DataBase.findAll();
    }
}
