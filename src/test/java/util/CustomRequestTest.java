package util;

import controller.Controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.Socket;
import java.util.HashMap;

class CustomRequestTest {
    HttpRequestHandler reqHandler;
    @BeforeEach
    void beforeEach() {
        this.reqHandler = new HttpRequestHandler(new Controller(new Service()));
    }

    @Test
    void CustomRequest_빈칸() {
        String input = " ";
        BufferedReader br = new BufferedReader(new StringReader(input));
        CustomRequest req = new CustomRequest();

        Assertions.assertThrows(RuntimeException.class, () -> reqHandler.setCustomRequest(req, br));
    }

    @Test
    void CustomRequest_일반파싱() {
        try {
            String input = "GET /questions?user=user&password=password HTTP/1.1\n" +
                    "Cookie: 8f23   \n" +
                    "Host: stackoverflow.com\n" +
                    "Connection: close\n" +
                    "User-Agent: RapidAPI/4.2.0 (Macintosh; OS X/14.3.1) GCDHTTPRequest";
            BufferedReader br = new BufferedReader(new StringReader(input));
            CustomRequest req = new CustomRequest();

            reqHandler.setCustomRequest(req, br);

            Assertions.assertEquals("GET", req.getMethod());
            Assertions.assertEquals("/questions", req.getTarget());
            Assertions.assertEquals("HTTP/1.1", req.getHttpVersion());
            Assertions.assertEquals("user", req.getParam("user"));
            Assertions.assertEquals("password", req.getParam("password"));
            Assertions.assertEquals("8f23", req.getHeader("Cookie"));
            Assertions.assertEquals("stackoverflow.com", req.getHeader("Host"));
            Assertions.assertEquals("close", req.getHeader("Connection"));
            Assertions.assertEquals("RapidAPI/4.2.0 (Macintosh; OS X/14.3.1) GCDHTTPRequest", req.getHeader("User-Agent"));
            Assertions.assertNull(req.getBody());
        } catch (Exception e) {
            Assertions.assertThrows(RuntimeException.class, () -> {
                throw e;
            });
        }
    }

    @Test
    void CustomRequest_firstLine_error() {
        String input = "GET /questions\n" +
                "Cookie: 8f23   \n" +
                "Host: stackoverflow.com\n" +
                "Connection: close\n" +
                "User-Agent: RapidAPI/4.2.0 (Macintosh; OS X/14.3.1) GCDHTTPRequest";
        BufferedReader br = new BufferedReader(new StringReader(input));
        CustomRequest req = new CustomRequest();

        Assertions.assertThrows(RuntimeException.class, () -> reqHandler.setCustomRequest(req, br));
    }

    @Test
    void CustomRequest_body() {
        try {
            String input = "POST /user/create HTTP/1.1\n" +
                    "Cookie: 8f23   \n" +
                    "Host: stackoverflow.com\n" +
                    "Connection: close\n" +
                    "User-Agent: RapidAPI/4.2.0 (Macintosh; OS X/14.3.1) GCDHTTPRequest\n" +
                    "Content-Length: 53\n" +
                    "\n" +
                    "userId=abc&password=1234&name=abc&email=abc%40abc.com";
            BufferedReader br = new BufferedReader(new StringReader(input));
            CustomRequest req = new CustomRequest();

            reqHandler.setCustomRequest(req, br);

            Assertions.assertEquals("POST", req.getMethod());
            Assertions.assertEquals("/user/create", req.getTarget());
            Assertions.assertEquals("HTTP/1.1", req.getHttpVersion());
            Assertions.assertEquals(req.getParams(), new HashMap<String, String>());
            Assertions.assertEquals("8f23", req.getHeader("Cookie"));
            Assertions.assertEquals("stackoverflow.com", req.getHeader("Host"));
            Assertions.assertEquals("close", req.getHeader("Connection"));
            Assertions.assertEquals("RapidAPI/4.2.0 (Macintosh; OS X/14.3.1) GCDHTTPRequest", req.getHeader("User-Agent"));
            Assertions.assertEquals("userId=abc&password=1234&name=abc&email=abc%40abc.com", req.getBody());
        } catch (Exception e) {
            Assertions.assertThrows(RuntimeException.class, () -> {
                throw e;
            });
        }
    }
}
