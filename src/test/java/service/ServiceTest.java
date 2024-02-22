package service;

import db.DataBase;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceTest {
    Service service;

    @BeforeEach
    void set() {
        this.service = new Service();
        DataBase.clear();
    }

    @Test
    void signup_가입성공() {
        User user = new User("id", "password", "name", "id@test.com");
        Assertions.assertDoesNotThrow(() -> this.service.signup(user));
    }

    @Test
    void singup_중복가입() {
        User user = new User("id", "password", "name", "id@test.com");
        this.service.signup(user);
        User user2 = new User("id", "password", "name", "id@test.com");
        Assertions.assertThrows(IllegalStateException.class, () -> this.service.signup(user2));
    }

    @Test
    void login_로그인성공() {
        User user = new User("id", "password", "name", "id@test.com");
        this.service.signup(user);
        User loginUser = this.service.login("id", "password");
        Assertions.assertEquals(user, loginUser);
    }

    @Test
    void login_존재하지않는유저() {
        User user = new User("id", "password", "name", "id@test.com");
        Assertions.assertThrows(IllegalStateException.class, () -> this.service.login("id", "password"));
    }

    @Test
    void login_비밀번호틀림() {
        User user = new User("id", "password", "name", "id@test.com");
        Assertions.assertThrows(IllegalStateException.class, () -> this.service.login("id", "password"));
    }
}
