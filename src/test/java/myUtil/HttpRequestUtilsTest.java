package myUtil;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtilsTest;
import webserver.CustomRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);
    HttpRequestUtils httpRequestUtils;

    @BeforeEach
    public void beforeEach() {
        this.httpRequestUtils = new HttpRequestUtils();
    }

    @Test
    void getRequest_일반() {
        String rawRequest = "GET / HTTP/1.1\nHost: localhost  \n  User-Agent: Mozilla/5.0    ";
        InputStream in = new ByteArrayInputStream(rawRequest.getBytes());

        CustomRequest req = this.httpRequestUtils.getRequest(in);
        Assertions.assertEquals("GET", req.getMethod());
        Assertions.assertEquals("/", req.getLocation());
        Assertions.assertEquals("HTTP/1.1", req.getHttpVersion());
        Assertions.assertEquals("localhost", req.getHeader("Host"));
        Assertions.assertEquals("Mozilla/5.0", req.getHeader("User-Agent"));
    }
}
