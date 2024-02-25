package util;

import java.io.DataOutputStream;
import java.io.IOException;

public class CustomResponse {
    public static void setCustomResponse(
            DataOutputStream dos,
            int responseCode,
            String responseMessage,
            String contentType,
            String  location,
            byte[] body
    ) throws IOException {
        dos.writeBytes("HTTP/1.1 " + responseCode + " " + responseMessage + " \r\n");
        dos.writeBytes("Content-Type: " + contentType + "; charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("Location: " + location + " \r\n");
        dos.writeBytes("\r\n");
        dos.write(body, 0 , body.length);
        dos.flush();
    }
}
