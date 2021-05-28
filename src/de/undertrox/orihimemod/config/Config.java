package de.undertrox.orihimemod.config;

import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.mapping.ButtonMapping;

import java.util.ArrayList;
import java.util.List;

public class Config {

    static final String[] versions = {"0.1.0", "0.1.1", "0.1.2", "0.1.3", "0.1.4", "0.1.5", "0.1.6", "0.1.7",
            "0.2.0", "0.3.0", "0.3.1", "0.3.2", "1.0.0"};
    public boolean SHOW_NUMBER_TOOLTIPS = false;
    public String GENERATED_VERSION = "error loading version";
    public boolean SHOW_KEYBIND_TOOLTIPS = true;
    public boolean SHOW_HELP_TOOLTIPS = true;
    public boolean DARK_MODE = false;
    public boolean EXPERT_MODE = false;
    public boolean AUTOSAVE = true;
    public boolean USE_NEW_SAVE_BEHAVIOR = false;
    public boolean justUpdatedTo0_2_0 = false;
    public int AUTOSAVE_INTERVAL = 300;
    protected String filename;
    public ButtonMapping mapping;
    public int AUTOSAVE_MAX_AGE = 86400;
    public boolean startMaximized = true;
    public boolean useUtf8 = true;

    public boolean smartFolding = true;
    public DefaultValues defaultVals = new DefaultValues();


    List<Keybind> keybinds = new ArrayList<>();

    Config() {
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

    public String version() {
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
}
