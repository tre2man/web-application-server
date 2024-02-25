package util;

import java.util.HashMap;
import java.util.Map;

public class CustomRequest {
    private String method;
    private String target;
    private String httpVersion;
    private Map<String, String> header;
    private Map<String, String> param;
    private String body;

    public CustomRequest() {
        this.header = new HashMap<>();
        this.param = new HashMap<>();
    }


    public String getMethod () {
        return this.method;
    }

    public String getTarget () {
        return this.target;
    }

    public String getHttpVersion () {
        return this.httpVersion;
    }

    public String getHeader (String key) {
        return this.header.get(key);
    }

    public Map<String, String> getHeader () {
        return this.header;
    }

    public String getParam (String key) {
        return this.param.get(key);
    }

    public Map<String, String> getParams () {
        return this.param;
    }

    public String getBody () {
        return this.body;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setTarget(String location) {
        if (location == null) {
            return;
        }

        String[] splited = location.split("\\?");
        this.target = splited[0];

        // param이 없으면 파싱 종료
        if (splited.length <= 1) {
            return;
        }

        String[] params = splited[1].split("&");
        this.param = new HashMap<>();
        for(String param: params) {
            String[] values = param.split("=");
            String key = values[0];
            String value = values.length > 1 ? values[1] : "";
            this.param.put(key, value);
        }
    }
    public void setParams(Map<String, String> params) {
        this.param = params;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public void setHeader(String key, String value) {
        this.header.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }
}
