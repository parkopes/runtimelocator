package net.jezevcik.runtimelocator;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class RegistryUtils {

    static String readRegistry(String location, String key) throws InterruptedException, IOException {
        final String command = String.format("reg query \"%s\" /v %s", location, key);
        final Process process = Runtime.getRuntime().exec(command);
        final StreamReader reader = new StreamReader(process.getInputStream());

        reader.start();
        process.waitFor();
        reader.join();

        final String output = reader.getResult();

        if (output.isEmpty())
            return null;

        final String[] parsed = output.split("\\s{2,}");
        return parsed[parsed.length-1];
    }

    static String[] list(String location) throws InterruptedException, IOException {
        final String command = String.format("reg query \"%s\"", location);
        final Process process = Runtime.getRuntime().exec(command);
        final StreamReader reader = new StreamReader(process.getInputStream());

        reader.start();
        process.waitFor();
        reader.join();

        final String output = reader.getResult();

        if (output.isEmpty())
            return new String[0];

        final List<String> list = new ArrayList<>();

        for (String o : output.split("\n")) {
            final String trim = o.trim();

            if (trim.equals(location) || !o.contains("\\")) {
                continue;
            }

            if (trim.isEmpty())
                continue;

            list.add(trim);
        }

        return list.toArray(new String[0]);
    }

    static class StreamReader extends Thread {
        private final InputStream stream;
        private final StringWriter writer = new StringWriter();

        public StreamReader(InputStream stream) {
            this.stream = stream;
        }

        public void run() {
            try {
                int c;
                while ((c = stream.read()) != -1)
                    writer.write(c);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getResult() {
            return writer.toString();
        }
    }

}
