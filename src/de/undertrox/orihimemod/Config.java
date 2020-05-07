package de.undertrox.orihimemod;

import de.undertrox.orihimemod.keybind.Keybind;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    private static Config instance;

    private static final String[] versions = {"0.1.0", "0.1.1", "0.1.2", "0.1.3", "0.1.4", "0.1.5"};
    public boolean SHOW_NUMBER_TOOLTIPS = false;
    public String GENERATED_VERSION = "error loading version";
    public boolean SHOW_KEYBIND_TOOLTIPS = true;
    public boolean DARK_MODE = false;
    public boolean EXPERT_MODE=false;
    private List<Pair<String, String>> parsed = new ArrayList<>();
    private List<Keybind> keybinds = new ArrayList<>();

    private Config() {
    }

    private static Config getInstance() {
        if (instance == null) {
            throw new RuntimeException("Tried to access Config before loading Config file.");
        }
        return instance;
    }

    public static boolean showNumberTooltips() {
        return getInstance().SHOW_NUMBER_TOOLTIPS;
    }

    public static boolean showKeybindTooltips() {
        return getInstance().SHOW_KEYBIND_TOOLTIPS;
    }

    public static String generatedVersion() {
        return getInstance().GENERATED_VERSION;
    }

    public static boolean useDarkMode() {
        return getInstance().DARK_MODE;
    }

    public static boolean useExpertMode() {
        return getInstance().EXPERT_MODE;
    }

    public static List<Keybind> keybinds() {
        return getInstance().keybinds;
    }

    public static void load(String configFileName) {
        instance = new Config();
        File file = new File(configFileName);
        if (!file.exists()) {
            createConfigFile(configFileName);
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
            e.printStackTrace();
        }

        if (!Config.generatedVersion().equals(OrihimeMod.version)) {
            updateConfigFrom(Config.generatedVersion(), configFileName);
            System.out.println("Reloading Config file...");
            load(configFileName);
        }
    }

    private static void updateConfigFrom(String version, String configFileName) {
        System.out.println("Updating Config file from version " + version + " to " + OrihimeMod.version);
        try {
            String content = new String(Files.readAllBytes(Paths.get(configFileName)));
            content = content.replace("orihimeKeybinds.generatedVersion="+version,
                    "orihimeKeybinds.generatedVersion="+OrihimeMod.version);
            content += getAddedConfigSince(Config.generatedVersion());
            Files.write(Paths.get(configFileName), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createConfigFile(String configFileName) {
        System.out.println("No config file found, generating default config file.");
        InputStream reader = instance.getClass().getResourceAsStream("orihimeKeybinds.cfg");
        OutputStream writer;
        try {
            writer = new FileOutputStream(new File(configFileName));
            copy(reader, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void copy(InputStream from, OutputStream to) {
        byte[] buffer = new byte[1024];
        int length;
        while (true) {
            try {
                if (!((length = from.read(buffer)) > 0)) break;
                to.write(buffer, 0, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    private static Pair<String, String> parseLine(String line) {
        line = line.trim();
        if (line.length() == 0 || line.charAt(0) == '#') { // Comments and empty lines
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
        return new Pair<>(configName.toString(), configValue.toString());
    }

    private static void parsePair(@NotNull Pair<String, String> pair) {
        String key = pair.getKey();
        String value = pair.getValue();
        if (key.equals("orihimekeybinds.generatedversion")) {
            instance.GENERATED_VERSION = value;
        } else if (key.equals("orihimekeybinds.showkeybindidtooltips")) {
            instance.SHOW_NUMBER_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimekeybinds.showkeybindtooltips")) {
            instance.SHOW_KEYBIND_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimedarkmode.enable")) {
            instance.DARK_MODE = Boolean.parseBoolean(value);
        } else if (key.equals("orihimeexpertmode.enable")) {
            instance.EXPERT_MODE = Boolean.parseBoolean(value);
        } else if ((key.matches("orihimekeybinds.button.[0-9]+"))) {
            Keybind keybind = parseKeybind(pair, Keybind.BUTTON);
            if (keybind != null) {
                instance.keybinds.add(keybind);
            }
        } else if (key.matches("orihimekeybinds.checkbox.[0-9]+")){
            Keybind keybind = parseKeybind(pair, Keybind.CHECKBOX);
            if (keybind != null) {
                instance.keybinds.add(keybind);
            }
        }
    }

    @Nullable
    private static Keybind parseKeybind(Pair<String, String> pair, int type) {
        String key = pair.getKey();
        String value = pair.getValue();
        if (value.equals("")) {
            return null;
        }

        boolean ctrl = false;
        boolean alt = false;
        boolean shift = false;
        if (value.contains("ctrl+")) ctrl = true;
        if (value.contains("alt+")) alt = true;
        if (value.contains("shift+")) shift = true;
        int button = Integer.parseInt(key.substring(key.lastIndexOf('.') + 1));

        String keyChar = value.substring(value.lastIndexOf('+') + 1);
        if (keyChar.startsWith("kc")) {
            return new Keybind(type, button, Integer.parseInt(keyChar.substring(2)), shift, ctrl, alt);
        } else {
            if (keyChar.length() != 1) {
                System.err.println("Keybind Syntax Error! '" + keyChar + "' is not 1 character long.");
            } else {
                return new Keybind(type, button, keyChar.charAt(0), shift, ctrl, alt);
            }
        }

        return null;
    }

    private static String[] getVersionsBetween(String ver1, String ver2) {
        int index1 = Arrays.asList(versions).indexOf(ver1)+1;
        int index2 = Arrays.asList(versions).indexOf(ver2)+1;

        return Arrays.copyOfRange(versions, index1, index2);
    }

    private static String getAddedConfig(String version) {
        StringBuilder res = new StringBuilder("\n");
        InputStream input = instance.getClass().getResourceAsStream("configFiles/" + version + ".cfg");
        Reader reader = new BufferedReader(new InputStreamReader(input));
        int c = 0;
        while (true) {
            try {
                if ((c = reader.read()) == -1) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            res.append((char) c);
        }
        return res.toString();
    }

    private static String getAddedConfigSince(String version) {
        StringBuilder res = new StringBuilder("\n");
        for (String v : getVersionsBetween(version, OrihimeMod.version)) {
            res.append(getAddedConfig(v));
        }
        return res.toString();
    }
}
