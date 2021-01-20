package cn.monkeyapp.mavd.youtubedl;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Corbett Zhang
 */
public class StreamGobbler extends Thread {

    private InputStream stream;
    private StringBuffer buffer;

    public StreamGobbler(StringBuffer buffer, InputStream stream) {
        this.stream = stream;
        this.buffer = buffer;
        start();
    }

    @Override
    public void run() {
        try {
            int nextChar;
            while((nextChar = this.stream.read()) != -1) {
                this.buffer.append((char) nextChar);
            }
        }
        catch (IOException e) {
            // ignore
        }
    }
}