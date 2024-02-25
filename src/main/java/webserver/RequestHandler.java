package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.Service;
import util.CustomRequest;
import util.HttpRequestHandler;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private HttpRequestHandler handler;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.handler = new HttpRequestHandler(new Controller(new Service()));
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            CustomRequest req = new CustomRequest();
            this.handler.handler(req, br, new DataOutputStream(out));

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}