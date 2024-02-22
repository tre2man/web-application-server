package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import myUtil.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Service;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private HttpRequestUtils httpRequestUtils;
    private Controller controller;

    public RequestHandler(Socket connectionSocket, HttpRequestUtils httpRequestUtils) {
        this.connection = connectionSocket;
        this.httpRequestUtils = httpRequestUtils;
        this.controller = new Controller(new Service());
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            CustomRequest req = httpRequestUtils.getRequest(in);
            String location = req.getLocation();
            String contentType = req.getHeader().get("Accept").split(",")[0];

            byte[] sourceFile = httpRequestUtils.getFile(System.getProperty("user.dir") + "/webapp" + location);

            // TODO: req별로 controller 매핑
            if (req.getParam() != null) {
                this.controller.signup(req);
            }

            DataOutputStream dos = new DataOutputStream(out);
            httpRequestUtils.response200Header(dos, contentType, sourceFile.length);
            httpRequestUtils.responseBody(dos, sourceFile);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}