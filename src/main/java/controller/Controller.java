package controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Service;
import util.CustomRequest;
import util.CustomResponse;
import util.HttpRequestUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    Service service;

    public Controller(Service service) {
        this.service = service;
    }

    public void signup(CustomRequest req, DataOutputStream dos) throws IOException {
        String body = req.getBody();
        Map<String, String> bodyParams = HttpRequestUtils.parseQueryString(body);
        User user = new User(
                bodyParams.get("userId"),
                bodyParams.get("password"),
                bodyParams.get("name"),
                bodyParams.get("email")
        );
        this.service.signup(user);
        CustomResponse.setCustomResponse(dos, 302, "Found", "text/html", "/index.html", new byte[0]);
    }

    public void login(CustomRequest req, DataOutputStream dos) throws IOException {
        String body = req.getBody();
        Map<String, String> parsedBody = HttpRequestUtils.parseQueryString(body);
        String userId = parsedBody.get("userId");
        String password = parsedBody.get("password");
        log.debug(userId);
        log.debug(password);
        boolean result = this.service.login(userId, password);
        if (result) {
            CustomResponse.setCustomResponse(dos, 302, "Found", "text/html", "/index.html", new byte[0]);
        } else {
            CustomResponse.setCustomResponse(dos, 302, "Found", "text/html", "/user/login_failed.html", new byte[0]);
        }
    }

    public void list(CustomRequest req, DataOutputStream dos) throws IOException {
        String cache = req.getHeader("Set-Cookie");
        if (!cache.isEmpty()) {
            CustomResponse.setCustomResponse(dos, 302, "Found", "text/html", "/user/login.html", new byte[0]);
            return;
        }

        String userData = this.service.findUsersData();
        try {
            CustomResponse.setCustomResponse(dos, 200, "OK", "text/html", "", userData.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void css(CustomRequest req, DataOutputStream dos) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + req.getTarget()).toPath());
        CustomResponse.setCustomResponse(dos, 200, "OK", "text/css", "", body);
    }
}
