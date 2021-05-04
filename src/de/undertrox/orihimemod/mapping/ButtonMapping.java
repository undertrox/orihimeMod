package de.undertrox.orihimemod.mapping;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ButtonMapping {
    private final String modVersion;
    private final String orihimeVersion;
    private final Map<String, String> mappings;
    private List<JButton> buttons;
    private List<JCheckBox> checkboxes;

    public List<JButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<JButton> buttons) {
        this.buttons = buttons;
    }

    public List<JCheckBox> getCheckboxes() {
        return checkboxes;
    }

    public void setCheckboxes(List<JCheckBox> checkboxes) {
        this.checkboxes = checkboxes;
    }

    public ButtonMapping(String modVersion, String orihimeVersion, Properties mappings) {
        this.modVersion = modVersion;
        this.orihimeVersion = orihimeVersion;
        this.mappings = new HashMap<>();
        for (String key : mappings.stringPropertyNames()) {
            this.mappings.put(key, mappings.getProperty(key));
        }
    }

    public static ButtonMapping load(String version, String orihimeVersion) {
        InputStream fis = null;
        Properties prop = null;
        try {
            fis = ButtonMapping.class.getResourceAsStream(version + "-" + orihimeVersion + ".properties");
            prop = new Properties();
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ButtonMapping(version, orihimeVersion, prop);
    }

    public String getModVersion() {
        return modVersion;
    }

    public String getOrihimeVersion() {
        return orihimeVersion;
    }

    public AbstractButton get(String key) {
        String mapping = mappings.get(key);
        if (mapping == null) {
            System.out.println("Could not find mapping for " + key);
            return null;
        }
        if (mapping.toLowerCase().startsWith("checkbox.")) {
            return checkboxes.get(Integer.parseInt(mapping.substring(9)));
        } else if (mapping.toLowerCase().startsWith("button.")) {
            return buttons.get(Integer.parseInt(mapping.substring(7)));
        }
        return null;
    }

    public String getKey(String value) {
        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }


}
