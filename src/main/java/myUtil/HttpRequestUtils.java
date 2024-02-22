package myUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.CustomRequest;
import webserver.RequestHandler;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public void response200Header(DataOutputStream dos, String contentType, int contentLength) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType +  ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + contentLength + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public CustomRequest getRequest(InputStream in) {
        try {
            CustomRequest req = new CustomRequest();
            Map<String, String> header = new HashMap<>();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            String body = "";
            int idx = 0;
            boolean isBody = false;

            while (line != null && !line.isEmpty()) {
                if (idx == 0) {
                    // API 정보 파싱
                    String[] startLine = Arrays.stream(line.split(" ")).map(String::trim).toArray(String[]::new);
                    req.setMethod(startLine[0]);
                    req.setLocation(startLine[1]);
                    req.setHttpVersion(startLine[2]);
                } else if (!isBody) {
                    // header 파싱
                    String[] headers = Arrays.stream(line.split(":")).map(String::trim).toArray(String[]::new);
                    header.put(headers[0], headers[1]);
                } else {
                    // TODO: body 처리하기
                    body = body + line;
                }
                line = br.readLine();
                idx++;
            }

            req.setHeader(header);
            req.setBody(body);
            return req;
        } catch(IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    public byte[] getFile(String location) throws IOException {
        try (FileInputStream fis = new FileInputStream(location); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new byte[0];
    }
}
