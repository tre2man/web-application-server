package controller;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Service;
import webserver.CustomRequest;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    Service service;
    public Controller(Service service) {
        this.service = service;
    }

    public void signup(CustomRequest req) {
        log.debug("signup called");
        User user = new User(req.getParam("userId"), req.getParam("password"), req.getParam("name"), req.getParam("email"));
        this.service.signup(user);
    }

    public void login(CustomRequest req) {
        log.debug("login called");
        this.service.login(req.getParam("userId"), req.getParam("password"));
    }
}
