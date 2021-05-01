package de.undertrox.orihimemod.keybind;

import de.undertrox.orihimemod.mapping.ButtonMapping;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class KeybindListener implements KeyListener {
    static boolean toggle = false;
    ButtonMapping mapping;
    List<Keybind> keybinds;

    public KeybindListener(ButtonMapping mapping, List<Keybind> keybinds) {
        this.mapping = mapping;
        this.keybinds = keybinds;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Keybind keybind : keybinds) {
            if (keybind.matches(e)) {
                if (keybind.getType() == Keybind.TOGGLE_TYPE) {
                    if (!toggle) {
                        toggle = true;
                        if (mapping.get("mountain").getBackground().getRed() > 200) {
                            mapping.get("valley").doClick();
                        } else if (mapping.get("valley").getBackground().getBlue() > 200) {
                            mapping.get("mountain").doClick();
                        }
                    }
                } else {
                    mapping.get(keybind.getMappingID()).doClick();
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (Keybind keybind : keybinds) {
            if (keybind.matches(e) && keybind.getType() == Keybind.TOGGLE_TYPE) {
                if (toggle) {
                    if (mapping.get("mountain").getBackground().getRed() > 200) {
                        mapping.get("valley").doClick();
                    } else if (mapping.get("valley").getBackground().getBlue() > 200) {
                        mapping.get("mountain").doClick();
                    }
                }
                toggle = false;
            }
        }
    }
}
