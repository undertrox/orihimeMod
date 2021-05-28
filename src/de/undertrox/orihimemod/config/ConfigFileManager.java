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
    private boolean justUpdatedTo2_0 = false;

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
        System.out.println("Loading " + version + ".cfg");
        return ParsedConfigFile.fromStream(Config.class.getResourceAsStream("configFiles/" + version + ".cfg"));
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
            config.justUpdatedTo0_2_0 = this.justUpdatedTo2_0;
        }
    }

    public void saveChangesToFile() {
        Config oldConfig = loadConfig(fileName);
        ParsedConfigFile oldConfigFile = ParsedConfigFile.fromFile(fileName);
        config.keybinds.stream()
                .filter(newKeybind -> !oldConfig.keybinds.contains(newKeybind))
                .forEach(newKeybind ->
                        oldConfigFile.addPair(newKeybind.getConfigID(), newKeybind.getConfigValue())
                );
        oldConfig.keybinds.stream()
                .filter(oldKeybind -> !config.keybinds.contains(oldKeybind))
                .forEach(removedKeybind -> oldConfigFile.removePair(removedKeybind.getConfigID(), removedKeybind.getConfigValue())
                );
        oldConfigFile.setValue("orihimemod.save.newbehavior", Boolean.toString(config.useNewSave()));
        oldConfigFile.saveTo(fileName);
        load();
    }

    private Config loadConfig(String fileName) {
        Config config = new Config();
        File file = new File(fileName);
        if (!file.exists()) {
            createConfigFile();
            config.justUpdatedTo0_2_0 = true;
        }
        config.filename = fileName;
        config.mapping = ButtonMapping.load(version, orihimeVersion);
        parseConfigFile(fileName, config);
        return config;
    }

    private void parseConfigFile(String fileName, Config config) {
        ParsedConfigFile parsedFile = ParsedConfigFile.fromFile(fileName);
        for (Pair<String, String> pair : parsedFile.getAllPairs()) {
            try {
                Pair<String, String> newPair = parsePair(pair, config);
                parsedFile.replacePair(pair, newPair);
            } catch (RuntimeException e) {
                System.err.println("Config Syntax error: " + pair);
                e.printStackTrace();
            }
        }
        if (!parsedFile.contains("orihimemod.save.newbehavior")) {
            config.justUpdatedTo0_2_0 = true;
        }
        parsedFile.saveTo(fileName);
    }

    private void updateToNewestVersion() {
        String version = config.version();
        System.out.println("Updating Config file from version " + version + " to " + OrihimeMod.version);
        ParsedConfigFile file = ParsedConfigFile.fromFile(fileName);
        if (Arrays.asList(getVersionsBetween(version, OrihimeMod.version)).contains("0.2.0")) {
            config.justUpdatedTo0_2_0 = true;
            this.justUpdatedTo2_0 = true;
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

    private Pair<String, String> parsePair(Pair<String, String> pair, Config config) {
        String key = pair.getKey().toLowerCase();
        String value = pair.getValue().toLowerCase();
        Keybind keybind;
        switch (key) {
            case "orihimekeybinds.generatedversion":
                config.GENERATED_VERSION = value;
                break;
            case "orihimemod.useutf8":
                config.useUtf8 = Boolean.parseBoolean(value);
                break;
            case "orihimekeybinds.showkeybindidtooltips":
                config.SHOW_NUMBER_TOOLTIPS = Boolean.parseBoolean(value);
                break;
            case "orihimekeybinds.showkeybindtooltips":
                config.SHOW_NUMBER_TOOLTIPS = Boolean.parseBoolean(value);
                break;
            case "orihimedarkmode.enable":
                config.DARK_MODE = Boolean.parseBoolean(value);
                break;
            case "orihimeexpertmode.enable":
                config.EXPERT_MODE = Boolean.parseBoolean(value);
                break;
            case "orihimekeybinds.toggletype":
                keybind = parseKeybind(pair, Keybind.TOGGLE_TYPE);
                if (keybind != null) {
                    config.keybinds.add(keybind);
                }
                break;
            case "orihimemod.autosave.enable":
                config.AUTOSAVE = Boolean.parseBoolean(value);
                break;
            case "orihimemod.autosave.interval":
                config.AUTOSAVE_INTERVAL = Integer.parseInt(value);
                break;
            case "orihimemod.autosave.maxage":
                config.AUTOSAVE_MAX_AGE = Integer.parseInt(value);
                break;
            case "orihimemod.showhelptooltips":
                config.SHOW_HELP_TOOLTIPS = Boolean.parseBoolean(value);
                break;
            case "orihimemod.save.newbehavior":
                config.USE_NEW_SAVE_BEHAVIOR = Boolean.parseBoolean(value);
                break;
            case "orihimeadditionalsavebuttons.enable":
                break;
            case "orihimemod.default.undosteps":
                config.defaultVals.undoSteps = Integer.parseInt(value);
                break;
            case "orihimemod.default.foldedmodelundosteps":
                config.defaultVals.foldedModelUndoSteps = Integer.parseInt(value);
                break;
            case "orihimemod.default.auxlineundosteps":
                config.defaultVals.auxLineUndoSteps = Integer.parseInt(value);
                break;
            case "orihimemod.default.linethickness":
                config.defaultVals.lineThickness = Integer.parseInt(value);
                break;
            case "orihimemod.default.auxlinethickness":
                config.defaultVals.auxLineThickness = Integer.parseInt(value);
                break;
            case "orihimemod.default.pointsize":
                config.defaultVals.pointSize = Integer.parseInt(value);
                break;
            case "orihimemod.default.showhelp":
                config.defaultVals.showHelp = Boolean.parseBoolean(value);
                break;
            case "orihimemod.default.gridsize":
                config.defaultVals.gridSize = Integer.parseInt(value);
                break;
            case "orihimemod.default.griddivsize":
                config.defaultVals.gridDivSize = Integer.parseInt(value);
                break;
            case "orihimemod.default.gridmode":
                config.defaultVals.gridMode = DefaultValues.GridMode.fromId(Integer.parseInt(value));
                break;
            case "orihimemod.default.gridassist":
                config.defaultVals.gridAssist = Boolean.parseBoolean(value);
                break;
            case "orihimemod.default.antialiasing":
                config.defaultVals.antialiasing = Boolean.parseBoolean(value);
                break;
            case "orihimemod.startmaximized":
                config.startMaximized = Boolean.parseBoolean(value);
                break;
            case "orihimemod.default.foldedmodelantialiasing":
                config.defaultVals.foldedModelAntiAliasing = Boolean.parseBoolean(value);
                break;
            case "orihimemod.default.tool":
                config.defaultVals.defaultTool = value;
                break;
            case "orihimemod.smartfolding.enable":
                config.smartFolding = Boolean.parseBoolean(value);
                break;
            case "orihimemod.default.gridangle":
                config.defaultVals.gridAngle = Double.parseDouble(value);
                break;
            default:
                if ((key.matches("orihimekeybinds.button.[0-9]+"))) {
                    keybind = parseKeybind(pair, Keybind.BUTTON);
                    if (keybind != null) {
                        config.keybinds.add(keybind);
                    }
                } else if (key.matches("orihimekeybinds.checkbox.[0-9]+")) {
                    keybind = parseKeybind(pair, Keybind.CHECKBOX);
                    if (keybind != null) {
                        config.keybinds.add(keybind);
                    }
                }  else {
                    keybind = parseKeybind(pair, Keybind.ABSTRACT_BUTTON);
                    if (keybind != null) {

                        config.keybinds.add(keybind);
                        return new Pair<>(keybind.getConfigID(), keybind.getConfigValue());
                    }
                }
        }

        return pair;
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
