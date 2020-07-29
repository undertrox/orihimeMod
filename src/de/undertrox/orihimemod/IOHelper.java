package de.undertrox.orihimemod;

import java.io.*;

public class IOHelper {
    public static String readFile(String filename) {
        try {
            InputStream stream = new FileInputStream(new File(filename));
            return readStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readStream(InputStream stream) {
        String newLine = System.getProperty("line.separator");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder result = new StringBuilder();
            boolean flag = false;
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(flag ? newLine : "").append(line);
                flag = true;
            }
            return result.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readResource(String name, Object obj) {
        return readStream(obj.getClass().getResourceAsStream(name));
    }
}
