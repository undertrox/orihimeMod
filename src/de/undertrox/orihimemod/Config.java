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
    public boolean justUpdatedTo0_2_0 = false;
    public int AUTOSAVE_INTERVAL = 300;
    protected String filename;
    public ButtonMapping mapping;
    public int AUTOSAVE_MAX_AGE = 86400;
    private List<Pair<String, String>> parsed = new ArrayList<>();
    private List<Keybind> keybinds = new ArrayList<>();
    private ParsedConfigFile parsedFile;

    private Config() {
    }

    public boolean showNumberTooltips() {
        return SHOW_NUMBER_TOOLTIPS;
    }

    public boolean showKeybindTooltips() {
        return SHOW_KEYBIND_TOOLTIPS;
    }

    public boolean showHelpTooltips() {
        return SHOW_HELP_TOOLTIPS;
    }

    public String generatedVersion() {
        return GENERATED_VERSION;
    }

    public boolean useDarkMode() {
        return DARK_MODE;
    }

    public boolean useExpertMode() {
        return EXPERT_MODE;
    }

    public boolean useNewSave() {
        return USE_NEW_SAVE_BEHAVIOR;
    }

    public void setUseNewSave(boolean val) {
        USE_NEW_SAVE_BEHAVIOR = val;
        parsedFile.setValue("orihimemod.save.newbehavior", Boolean.toString(val));
        parsedFile.saveTo(filename);
    }

    public List<Keybind> keybinds() {
        return keybinds;
    }

    public boolean useAutosave() {
        return AUTOSAVE;
    }

    public int autoSaveInterval() {
        return AUTOSAVE_INTERVAL;
    }

    public int autoSaveMaxAge() {
        return AUTOSAVE_MAX_AGE;
    }

    public static Config load(String configFileName) {
        System.out.println("Loading Config file");
        Config config = new Config();
        config.filename = configFileName;
        config.mapping = ButtonMapping.load(version, orihimeVersion);
        File file = new File(configFileName);
        if (!file.exists()) {
            config.createConfigFile(configFileName);
        }
        config.parsedFile = ParsedConfigFile.fromFile(configFileName);
        config.parsed = config.parsedFile.getAllPairs();
        List<Pair<String, String>> pairs = config.parsed;
        for (Pair<String, String> pair : pairs) {
            try {
                config.parsePair(pair);
            } catch (RuntimeException e) {
                System.err.println("Config Syntax error: " + pair);
                e.printStackTrace();
            }
        }

        if (!config.generatedVersion().equals(OrihimeMod.version)) {
            config.updateToNewestVersion(configFileName);
            System.out.println("Reloading Config file...");
            return load(configFileName);
        }

        if (!config.parsedFile.contains("orihimemod.save.newbehavior")) {
            config.justUpdatedTo0_2_0 = true;
            config.parsedFile.addPair("orihimeMod.save.newBehavior", "false");
            config.parsedFile.saveTo(configFileName);
        }
        return config;
    }

    public Config updateAndLoadConfigFile(String configFileName) {
        Config oldConfig = load(configFileName);
        List<Keybind> newKeybinds = new ArrayList<>();
        List<Keybind> removedKeybinds = new ArrayList<>();
        for (Keybind keybind : this.keybinds) {
            if (!oldConfig.keybinds.contains(keybind)) {
                newKeybinds.add(keybind);
            }
        }
        for (Keybind keybind : instance.keybinds()) {
            if (!oldConfig.keybinds.contains(keybind)) {
                removedKeybinds.add(keybind);
            }
        }
        for (Keybind kb : removedKeybinds) {
            oldConfig.parsedFile.removePair(kb.getConfigID(), kb.getConfigValue());
        }
        for (Keybind newKeybind : newKeybinds) {
            oldConfig.parsedFile.addPair(newKeybind.getConfigID(), newKeybind.getConfigValue());
        }
        oldConfig.parsedFile.saveTo(configFileName);
        return load(configFileName);
    }


    private void updateToNewestVersion(String configFileName) {
        String version = this.generatedVersion();
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

    private void updateKeybindNames(ParsedConfigFile file) {
        ButtonMapping oldMapping = ButtonMapping.load("0.3.2", "3.054");
        for (Pair<String, String> configPair : file.getAllPairs()) {
            System.out.println(configPair.getKey().substring(16));
            String newName = oldMapping.getKey(configPair.getKey().substring(16));
            if (newName != null) {
                file.replaceKey(configPair.getKey(), newName);
            }
        }
    }

    private void createConfigFile(String configFileName) {
        System.out.println("No config file found, generating default config file.");
        InputStream reader = getClass().getResourceAsStream("orihimeKeybinds.cfg");
        OutputStream writer;
        justUpdatedTo0_2_0 = true;
        try {
            writer = new FileOutputStream(configFileName);
            assert reader != null;
            copy(reader, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void copy(InputStream from, OutputStream to) {
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

    private void parsePair(Pair<String, String> pair) {
        String key = pair.getKey().toLowerCase();
        String value = pair.getValue().toLowerCase();
        if (key.equals("orihimekeybinds.generatedversion")) {
            GENERATED_VERSION = value;
        } else if (key.equals("orihimekeybinds.showkeybindidtooltips")) {
            SHOW_NUMBER_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimekeybinds.showkeybindtooltips")) {
            SHOW_KEYBIND_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimedarkmode.enable")) {
            DARK_MODE = Boolean.parseBoolean(value);
        } else if (key.equals("orihimeexpertmode.enable")) {
            EXPERT_MODE = Boolean.parseBoolean(value);
        } else if ((key.matches("orihimekeybinds.button.[0-9]+"))) {
            Keybind keybind = parseKeybind(pair, Keybind.BUTTON);
            if (keybind != null) {
                keybinds.add(keybind);
            }
        } else if (key.matches("orihimekeybinds.checkbox.[0-9]+")){
            Keybind keybind = parseKeybind(pair, Keybind.CHECKBOX);
            if (keybind != null) {
                keybinds.add(keybind);
            }
        } else if (key.equals("orihimekeybinds.toggletype")) {
            Keybind keybind = parseKeybind(pair, Keybind.TOGGLE_TYPE);
            if (keybind != null) {
                keybinds.add(keybind);
            }
        } else if (key.equals("orihimemod.autosave.enable")) {
            AUTOSAVE = Boolean.parseBoolean(value);
        } else if (key.equals("orihimemod.autosave.interval")) {
            AUTOSAVE_INTERVAL = Integer.parseInt(value);
        } else if (key.equals("orihimemod.autosave.maxage")) {
            AUTOSAVE_MAX_AGE = Integer.parseInt(value);
        } else if (key.equals("orihimemod.showhelptooltips")){
            SHOW_HELP_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimemod.save.newbehavior")) {
            USE_NEW_SAVE_BEHAVIOR = Boolean.parseBoolean(value);
        }
        else //noinspection StatementWithEmptyBody
            if (key.equals("orihimeadditionalsavebuttons.enable")) {
            // for compatibility, but ignored
        } else {
            Keybind keybind = parseKeybind(pair, Keybind.ABSTRACT_BUTTON);
            if (keybind != null) {
                keybinds.add(keybind);
            }
        }
    }

    private Keybind parseKeybind(Pair<String, String> pair, int type) {
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
                throw new RuntimeException("Keybind Syntax Error! '" + keyChar + "' is not 1 character long.");
            } else {
                return new Keybind(type, key, keyChar, shift, ctrl, alt, ignoreMods);
            }
        }
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
