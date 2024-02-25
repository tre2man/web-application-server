package util;

import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class HttpRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);

    Controller controller;
    public HttpRequestHandler(Controller controller) {
        this.controller = controller;
    }

    public void handler(
        CustomRequest req,
        BufferedReader br,
        DataOutputStream out
    ) throws IOException {
        setCustomRequest(req, br);
        String url = req.getTarget();

        if (url.equals("/user/create")) {
            this.controller.signup(req, new DataOutputStream(out));
        } else if (url.equals("/user/login")) {
            this.controller.login(req, new DataOutputStream(out));
        } else if (url.equals("/user/list")) {
            this.controller.list(req, new DataOutputStream(out));
        } else if (url.endsWith(".css")) {
            this.controller.css(req, new DataOutputStream(out));
        } else {
            responseResource(out, url);
        }
    }


    // setCustomRequest()
    public void setCustomRequest(CustomRequest req, BufferedReader br) {
        try {
            String line = br.readLine();
            if (line == null) {
                throw new RuntimeException("buffer is null");
            }

            setFirstLine(req, line);

            while (!line.isEmpty()) {
                line = br.readLine();
                setHeader(req, line);
            }

            if (req.getHeader("Content-Length") != null) {
                req.setBody(IOUtils.readData(br, Integer.parseInt(req.getHeader("Content-Length"))));
            }
        } catch(IOException e) {
            log.error(e.getMessage());
        }
    }

    private void setFirstLine(CustomRequest req, String line) {
        String[] startLine = line.split(" ");
        if (startLine.length < 3) {
            throw new RuntimeException("startLine is invalid");
        }
        req.setMethod(startLine[0]);
        req.setTarget(startLine[1]);
        req.setHttpVersion(startLine[2]);
        setParams(req);
    }

    private void setParams(CustomRequest req) {
        String[] split = req.getTarget().split("\\?");
        if (split.length < 2) {
            return;
        }
        req.setParams(HttpRequestUtils.parseQueryString(split[1]));
        req.setTarget(split[0]);
    }

    private void setHeader(CustomRequest req, String line) {
        String[] split = Arrays.stream(line.split(":")).map(String::trim).toArray(String[]::new);
        if (split.length < 2) {
            return;
        }
        req.setHeader(split[0], String.join(":", Arrays.copyOfRange(split, 1, split.length)));
    }
    // end of setCustomRequest()


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseResource(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp"+ url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }
}
