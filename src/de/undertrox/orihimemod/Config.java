package de.undertrox.orihimemod;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static Config instance;

    public boolean SHOW_NUMBER_TOOLTIPS = false;
    public String GENERATED_VERSION = "error loading version";
    private List<Pair<String, String>> parsed = new ArrayList<>();
    private List<Keybind> keybinds = new ArrayList<>();

    private Config() {
    }

    public static boolean showNumberTooltips() {
        return instance.SHOW_NUMBER_TOOLTIPS;
    }

    public static String generatedVersion() {
        return instance.GENERATED_VERSION;
    }

    public static List<Keybind> keybinds() {
        return instance.keybinds;
    }

    public static void load(String config_filename) {
        instance = new Config();
        File file = new File(config_filename);
        if (!file.exists()) {
            System.out.println("No config file found, generating default config file.");
            InputStream reader = instance.getClass().getResourceAsStream("orihimeKeybinds.cfg");
            OutputStream writer = null;
            try {
                writer = new FileOutputStream(new File(config_filename));

                byte[] buffer = new byte[1024];
                int length;
                while (true) {
                    try {
                        if (!((length = reader.read(buffer)) > 0)) break;
                        writer.write(buffer, 0, length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                Pair<String, String> parsedLine = parseLine(line);
                if (parsedLine != null) {
                    instance.parsed.add(parsedLine);
                    parsePair(parsedLine);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static Pair<String, String> parseLine(String line) {
        Pair<String, String> result = null;
        line = line.trim();
        if (line.length() == 0 || line.charAt(0) == '#') {
            return null;
        }
        StringBuilder configName = new StringBuilder();
        StringBuilder configValue = new StringBuilder();
        boolean foundEquals = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (!foundEquals) {
                if (c == '=') {
                    foundEquals = true;
                } else {
                    configName.append(Character.toLowerCase(c));
                }
            } else {
                if (!Character.isWhitespace(c)) {
                    configValue.append(Character.toLowerCase(c));
                }
            }
        }
        result = new Pair<>(configName.toString(), configValue.toString());
        return result;
    }

    private static void parsePair(Pair<String, String> pair) {
        if (pair.getKey().equals("orihimekeybinds.generatedversion")) {
            instance.GENERATED_VERSION = pair.getValue();
        } else if (pair.getKey().equals("orihimekeybinds.showkeybindidtooltips")) {
            instance.SHOW_NUMBER_TOOLTIPS = Boolean.parseBoolean(pair.getValue());
        } else if ((pair.getKey().matches("orihimekeybinds.button.[0-9]+"))) {
            if (pair.getValue().equals("")) {
                return;
            }
            boolean ctrl = false;
            boolean alt = false;
            boolean shift = false;
            if (pair.getValue().contains("ctrl+")) ctrl = true;
            if (pair.getValue().contains("alt+")) alt = true;
            if (pair.getValue().contains("shift+")) shift = true;
            int button = Integer.parseInt(pair.getKey().substring(pair.getKey().lastIndexOf('.') + 1));
            String key = pair.getValue().substring(pair.getValue().lastIndexOf('+') + 1);
            Keybind bind = null;
            if (key.startsWith("kc")) {
                bind = new Keybind(button, Integer.parseInt(key.substring(2)), shift, ctrl, alt);
            } else {
                if (key.length() != 1) {
                    System.err.println("Keybind Syntax Error! '" + key + "' is not 1 character long.");
                } else {
                    bind = new Keybind(button, key.charAt(0), shift, ctrl, alt);
                }
            }
            if (bind != null) {
                instance.keybinds.add(bind);
            }
        }
    }
}
