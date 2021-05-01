package de.undertrox.orihimemod.config;

import de.undertrox.orihimemod.OrihimeMod;
import de.undertrox.orihimemod.Pair;
import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.mapping.ButtonMapping;

import java.io.*;
import java.util.Arrays;

import static de.undertrox.orihimemod.OrihimeMod.orihimeVersion;
import static de.undertrox.orihimemod.OrihimeMod.version;

public class ConfigFileManager {

    private Config config;
    private final String fileName;

    public ConfigFileManager(String fileName) {
        this.fileName = fileName;
    }

    private static String[] getVersionsBetween(String ver1, String ver2) {
        int index1 = Arrays.asList(Config.versions).indexOf(ver1) + 1;
        int index2 = Arrays.asList(Config.versions).indexOf(ver2) + 1;
        if (index1 == 0) { // unknown version probably means newer version
            return new String[0];
        }

        return Arrays.copyOfRange(Config.versions, index1, index2);
    }

    private static ParsedConfigFile getAddedConfig(String version) {
        return ParsedConfigFile.fromStream(Config.class.getResourceAsStream("../configFiles/" + version + ".cfg"));
    }

    private static ParsedConfigFile getAddedConfigSince(String version) {
        ParsedConfigFile res = new ParsedConfigFile();
        for (String v : getVersionsBetween(version, OrihimeMod.version)) {
            res.append(getAddedConfig(v));
        }
        return res;
    }

    public Config getConfig() {
        return config;
    }

    public void load() {
        System.out.println("Loading Config file");
        config = loadConfig(fileName);
        if (!config.version().equals(OrihimeMod.version)) {
            updateToNewestVersion();
            System.out.println("Reloading Config file...");
            load();
        }
    }

    public void saveChangesToFile() {
        Config oldConfig = loadConfig(fileName);
        ParsedConfigFile oldConfigFile = ParsedConfigFile.fromFile(fileName);
        config.keybinds.stream()
                .filter(oldConfig.keybinds::contains)
                .forEach(newKeybind ->
                        oldConfigFile.removePair(newKeybind.getConfigID(), newKeybind.getConfigValue())
                );
        oldConfig.keybinds.stream()
                .filter(config.keybinds::contains)
                .forEach(removedKeybind ->
                        oldConfigFile.addPair(removedKeybind.getConfigID(), removedKeybind.getConfigValue())
                );
        oldConfigFile.setValue("orihimemod.save.newbehavior", Boolean.toString(config.useNewSave()));
        oldConfigFile.saveTo(fileName);
        load();
    }

    private Config loadConfig(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            createConfigFile();
        }
        Config config = new Config();
        config.filename = fileName;
        config.mapping = ButtonMapping.load(version, orihimeVersion);
        parseConfigFile(fileName, config);
        return config;
    }

    private void parseConfigFile(String fileName, Config config) {
        ParsedConfigFile parsedFile = ParsedConfigFile.fromFile(fileName);
        for (Pair<String, String> pair : parsedFile.getAllPairs()) {
            try {
                parsePair(pair, config);
            } catch (RuntimeException e) {
                System.err.println("Config Syntax error: " + pair);
                e.printStackTrace();
            }
        }
        if (!parsedFile.contains("orihimemod.save.newbehavior")) {
            config.justUpdatedTo0_2_0 = true;
        }
    }

    private void updateToNewestVersion() {
        String version = config.version();
        System.out.println("Updating Config file from version " + version + " to " + OrihimeMod.version);
        ParsedConfigFile file = ParsedConfigFile.fromFile(fileName);
        if (Arrays.asList(getVersionsBetween(version, OrihimeMod.version)).contains("0.2.0")) {
            config.justUpdatedTo0_2_0 = true;
        }
        if (Arrays.asList(getVersionsBetween(version, OrihimeMod.version)).contains("1.0.0")) {
            updateKeybindNames(file);
        }
        file.append(getAddedConfigSince(version));
        file.setValue("orihimeKeybinds.generatedVersion", OrihimeMod.version);
        file.saveTo(fileName);
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

    private void createConfigFile() {
        System.out.println("No config file found, generating default config file.");
        InputStream reader = getClass().getResourceAsStream("orihimeKeybinds.cfg");
        OutputStream writer;
        config.justUpdatedTo0_2_0 = true;
        try {
            writer = new FileOutputStream(fileName);
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

    private void parsePair(Pair<String, String> pair, Config config) {
        String key = pair.getKey().toLowerCase();
        String value = pair.getValue().toLowerCase();
        if (key.equals("orihimekeybinds.generatedversion")) {
            config.GENERATED_VERSION = value;
        } else if (key.equals("orihimekeybinds.showkeybindidtooltips")) {
            config.SHOW_NUMBER_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimekeybinds.showkeybindtooltips")) {
            config.SHOW_KEYBIND_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimedarkmode.enable")) {
            config.DARK_MODE = Boolean.parseBoolean(value);
        } else if (key.equals("orihimeexpertmode.enable")) {
            config.EXPERT_MODE = Boolean.parseBoolean(value);
        } else if ((key.matches("orihimekeybinds.button.[0-9]+"))) {
            Keybind keybind = parseKeybind(pair, Keybind.BUTTON);
            if (keybind != null) {
                config.keybinds.add(keybind);
            }
        } else if (key.matches("orihimekeybinds.checkbox.[0-9]+")) {
            Keybind keybind = parseKeybind(pair, Keybind.CHECKBOX);
            if (keybind != null) {
                config.keybinds.add(keybind);
            }
        } else if (key.equals("orihimekeybinds.toggletype")) {
            Keybind keybind = parseKeybind(pair, Keybind.TOGGLE_TYPE);
            if (keybind != null) {
                config.keybinds.add(keybind);
            }
        } else if (key.equals("orihimemod.autosave.enable")) {
            config.AUTOSAVE = Boolean.parseBoolean(value);
        } else if (key.equals("orihimemod.autosave.interval")) {
            config.AUTOSAVE_INTERVAL = Integer.parseInt(value);
        } else if (key.equals("orihimemod.autosave.maxage")) {
            config.AUTOSAVE_MAX_AGE = Integer.parseInt(value);
        } else if (key.equals("orihimemod.showhelptooltips")) {
            config.SHOW_HELP_TOOLTIPS = Boolean.parseBoolean(value);
        } else if (key.equals("orihimemod.save.newbehavior")) {
            config.USE_NEW_SAVE_BEHAVIOR = Boolean.parseBoolean(value);
        } else //noinspection StatementWithEmptyBody
            if (key.equals("orihimeadditionalsavebuttons.enable")) {
                // for compatibility, but ignored
            } else {
                Keybind keybind = parseKeybind(pair, Keybind.ABSTRACT_BUTTON);
                if (keybind != null) {
                    config.keybinds.add(keybind);
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
}
