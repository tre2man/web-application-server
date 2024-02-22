package webserver;

import java.util.HashMap;
import java.util.Map;

public class CustomRequest {
    private String method;
    private String location;
    private String httpVersion;
    private Map<String, String> header;
    private Map<String, String> param;
    private String body;

    public String getMethod () {
        return this.method;
    }

    public String getHeader (String key) {
        return this.header.get(key);
    }

    public Map<String, String> getHeader () {
        return this.header;
    }

    public String getLocation () {
        return this.location;
    }

    public String getParam (String key) {
        return this.param.get(key);
    }

    public Map<String, String> getParam () {
        return this.param;
    }

    public String getHttpVersion () {
        return this.httpVersion;
    }

    public String getBody () {
        return this.body;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setLocation(String location) {
        if (location == null) {
            return;
        }

        String[] splited = location.split("\\?");
        this.location = splited[0];

        // param이 없으면 파싱 종료
        if (splited.length <= 1) {
            return;
        }

        String[] params = splited[1].split("&");
        this.param = new HashMap<String, String>();
        for(String param: params) {
            String[] values = param.split("=");
            String key = values[0];
            String value = values.length > 1 ? values[1] : "";
            this.param.put(key, value);
        }
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
