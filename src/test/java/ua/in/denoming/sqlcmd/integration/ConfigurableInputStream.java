package ua.in.denoming.sqlcmd.integration;

import java.io.IOException;
import java.io.InputStream;

class ConfigurableInputStream extends InputStream {
    private String line;

    @Override
    public int read() throws IOException {
        if (line.length() == 0) {
            return -1;
        }
        int cp = line.codePointAt(0);
        line = line.substring(Character.isSupplementaryCodePoint(cp) ? 2 : 1);
        return cp;
    }

    void writeLine(String line) {
        if (this.line == null) {
            this.line = line;
        } else {
            this.line += System.lineSeparator() + line;
        }
    }
}
