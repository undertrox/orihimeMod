package de.undertrox.orihimemod;

import de.undertrox.orihimemod.config.ParsedConfigFile;
import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.mapping.ButtonMapping;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.undertrox.orihimemod.OrihimeMod.orihimeVersion;
import static de.undertrox.orihimemod.OrihimeMod.version;

public class Config {
    private static Config instance;

    private static final String[] versions = {"0.1.0", "0.1.1", "0.1.2", "0.1.3", "0.1.4", "0.1.5", "0.1.6", "0.1.7",
        "0.2.0", "0.3.0", "0.3.1", "0.3.2", "1.0.0"};
    public boolean SHOW_NUMBER_TOOLTIPS = false;
    public String GENERATED_VERSION = "error loading version";
    public boolean SHOW_KEYBIND_TOOLTIPS = true;
    public boolean SHOW_HELP_TOOLTIPS = true;
    public boolean DARK_MODE = false;
    public boolean EXPERT_MODE=false;
    public boolean AUTOSAVE = true;
    public boolean USE_NEW_SAVE_BEHAVIOR = false;
    public static boolean justUpdatedTo0_2_0 = false;
    public int AUTOSAVE_INTERVAL = 300;
    protected String filename;
    public ButtonMapping mapping;
    public int AUTOSAVE_MAX_AGE = 86400;
    private List<Pair<String, String>> parsed = new ArrayList<>();
    private List<Keybind> keybinds = new ArrayList<>();
    private ParsedConfigFile parsedFile;

    private Config() {
    }

    public static Config getInstance() {
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

    public static boolean showHelpTooltips() {
        return getInstance().SHOW_HELP_TOOLTIPS;
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

    public static boolean useNewSave() {
        return getInstance().USE_NEW_SAVE_BEHAVIOR;
    }

    public static void setUseNewSave(boolean val) {
        getInstance().USE_NEW_SAVE_BEHAVIOR = val;
        getInstance().parsedFile.setValue("orihimemod.save.newbehavior", Boolean.toString(val));
        getInstance().parsedFile.saveTo(getInstance().filename);
    }

    public static List<Keybind> keybinds() {
        return getInstance().keybinds;
    }

    public static boolean useAutosave() {
        return getInstance().AUTOSAVE;
    }

    public static int autoSaveInterval() {
        return getInstance().AUTOSAVE_INTERVAL;
    }

    public static int autoSaveMaxAge() {
        return getInstance().AUTOSAVE_MAX_AGE;
    }

    public static void load(String configFileName) {
        System.out.println("Loading Config file");
        instance = new Config();
        instance.filename = configFileName;
        instance.mapping = ButtonMapping.load(version, orihimeVersion);
        File file = new File(configFileName);
        if (!file.exists()) {
            createConfigFile(configFileName);
        }
        instance.parsedFile = ParsedConfigFile.fromFile(configFileName);
        instance.parsed = instance.parsedFile.getAllPairs();
        for (Pair<String, String> pair : instance.parsed) {
            parsePair(pair);
        }

        if (!Config.generatedVersion().equals(OrihimeMod.version)) {
            updateConfigFrom(Config.generatedVersion(), configFileName);
            System.out.println("Reloading Config file...");
            load(configFileName);
        }

        if (!instance.parsedFile.contains("orihimemod.save.newbehavior")) {
            justUpdatedTo0_2_0 = true;
            instance.parsedFile.addPair("orihimeMod.save.newBehavior", "false");
            instance.parsedFile.saveTo(configFileName);
        }
    }

    public static void updateConfigFile(String configFileName) {
        Config changed = instance;
        load(configFileName);
        List<Keybind> newKeybinds = new ArrayList<>();
        List<Keybind> removedKeybinds = new ArrayList<>();
        for (Keybind keybind : changed.keybinds) {
            if (!instance.keybinds.contains(keybind)) {
                newKeybinds.add(keybind);
            }
        }
        for (Keybind keybind : Config.keybinds()) {
            if (!changed.keybinds.contains(keybind)) {
                removedKeybinds.add(keybind);
            }
        }
        for (Keybind kb : removedKeybinds) {
            instance.parsedFile.removePair(kb.getConfigID(), kb.getConfigValue());
        }
        for (Keybind newKeybind : newKeybinds) {
            instance.parsedFile.addPair(newKeybind.getConfigID(), newKeybind.getConfigValue());
        }
        instance.parsedFile.saveTo(configFileName);
        load(configFileName);
    }


    private static void updateConfigFrom(String version, String configFileName) {
        System.out.println("Updating Config file from version " + version + " to " + OrihimeMod.version);
        ParsedConfigFile file = ParsedConfigFile.fromFile(configFileName);
        if (Arrays.asList(getVersionsBetween(version, OrihimeMod.version)).contains("0.2.0")) {
            justUpdatedTo0_2_0 = true;
        }
        if (Arrays.asList(getVersionsBetween(version, OrihimeMod.version)).contains("1.0.0")) {
            updateKeybindNames(file);
        }
        file.append(getAddedConfigSince(version));
        file.setValue("orihimeKeybinds.generatedVersion", OrihimeMod.version);
        file.saveTo(configFileName);
    }

    private static void updateKeybindNames(ParsedConfigFile file) {
        ButtonMapping oldMapping = ButtonMapping.load("0.3.2", "3.054");
        for (Pair<String, String> configPair : file.getAllPairs()) {
            System.out.println(configPair.getKey().substring(16));
            String newName = oldMapping.getKey(configPair.getKey().substring(16));
            if (newName != null) {
                file.replaceKey(configPair.getKey(), newName);
            }
        }
    }

    private static void createConfigFile(String configFileName) {
        System.out.println("No config file found, generating default config file.");
        InputStream reader = instance.getClass().getResourceAsStream("orihimeKeybinds.cfg");
        OutputStream writer;
        justUpdatedTo0_2_0 = true;
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
                if ((length = from.read(buffer)) <= 0) break;
                to.write(buffer, 0, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void parsePair(Pair<String, String> pair) {
        String key = pair.getKey().toLowerCase();
        String value = pair.getValue().toLowerCase();
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
        } else if (key.equals("orihimekeybinds.toggletype")) {
            Keybind keybind = parseKeybind(pair, Keybind.TOGGLE_TYPE);
            if (keybind != null) {
                instance.keybinds.add(keybind);
            }
        } else if (key.equals("orihimemod.autosave.enable")) {
            instance.AUTOSAVE = Boolean.parseBoolean(value);
        } else if (key.equals("orihimemod.autosave.interval")) {
            instance.AUTOSAVE_INTERVAL = Integer.parseInt(value);
        } else if (key.equals("orihimekeybinds.showhelptooltips")){
            instance.SHOW_HELP_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimemod.save.newbehavior")) {
            instance.USE_NEW_SAVE_BEHAVIOR = Boolean.parseBoolean(value);
        } else {
            Keybind keybind = parseKeybind(pair, Keybind.ABSTRACT_BUTTON);
            if (keybind != null) {
                instance.keybinds.add(keybind);
            }
        }
    }

    private static Keybind parseKeybind(Pair<String, String> pair, int type) {
        String key = pair.getKey();
        String value = pair.getValue();
        if (value.equals("")) {
            return null;
        }

        boolean ctrl = false;
        boolean alt = false;
        boolean shift = false;
        boolean ignoreMods = false;
        if (value.contains("ctrl+")) ctrl = true;
        if (value.contains("alt+")) alt = true;
        if (value.contains("shift+")) shift = true;
        if (type == Keybind.TOGGLE_TYPE) {
            ignoreMods = true;
        }

        String keyChar = value.substring(value.lastIndexOf('+') + 1);
        if (keyChar.startsWith("kc")) {
            return new Keybind(type, key, Integer.parseInt(keyChar.substring(2)), shift, ctrl, alt, ignoreMods);
        } else {
            if (keyChar.length() != 1) {
                System.err.println("Keybind Syntax Error! '" + keyChar + "' is not 1 character long.");
            } else {
                return new Keybind(type, key, keyChar, shift, ctrl, alt, ignoreMods);
            }
        }

        return null;
    }

    private static String[] getVersionsBetween(String ver1, String ver2) {
        int index1 = Arrays.asList(versions).indexOf(ver1)+1;
        int index2 = Arrays.asList(versions).indexOf(ver2)+1;

        return Arrays.copyOfRange(versions, index1, index2);
    }

    private static ParsedConfigFile getAddedConfig(String version) {
        return ParsedConfigFile.fromStream(instance.getClass().getResourceAsStream("configFiles/" + version + ".cfg"));
    }

    private static ParsedConfigFile getAddedConfigSince(String version) {
        ParsedConfigFile res = new ParsedConfigFile();
        for (String v : getVersionsBetween(version, OrihimeMod.version)) {
            res.append(getAddedConfig(v));
        }
        return res;
    }
}
