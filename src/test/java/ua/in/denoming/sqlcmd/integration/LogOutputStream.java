package ua.in.denoming.sqlcmd.integration;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

class LogOutputStream extends ByteArrayOutputStream {
    String readLine() {
        try {
            return new String(this.toByteArray(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
