package webserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

public class CustomRequestTest {
    CustomRequest req;

    @BeforeEach
    public void setUp() {
        req = new CustomRequest();
    }

    @Test
    public void setLocation_NULL() {
        req.setLocation(null);
        Assertions.assertNull(req.getLocation());
        Assertions.assertNull(req.getParam());
    }

    @Test
    public void setLocation_공백() {
        req.setLocation("");
        Assertions.assertEquals(req.getLocation(), "");
        Assertions.assertNull(req.getParam());
    }

    @Test
    public void setLocation_파라미터없음() {
        req.setLocation("/user");
        Assertions.assertEquals("/user", req.getLocation());
        Assertions.assertNull(req.getParam());
    }

    @Test
    public void setLocation_모두파싱() {
         req.setLocation("/index.html?user=user&password=123");
         Assertions.assertEquals("/index.html", req.getLocation());
         Assertions.assertEquals("user", req.getParam("user"));
         Assertions.assertEquals("123", req.getParam("password"));
         Map<String, String> params = new HashMap<>();
         params.put("user", "user");
         params.put("password", "123");
         Assertions.assertEquals(params, req.getParam());
    }
}
