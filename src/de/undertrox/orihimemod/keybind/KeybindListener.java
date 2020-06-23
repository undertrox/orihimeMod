package de.undertrox.orihimemod.keybind;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeMod;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeybindListener implements KeyListener {
    static boolean toggle = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Keybind keybind : Config.keybinds()) {
            if (keybind.matches(e)) {
                if (keybind.getType() == Keybind.BUTTON) {
                    OrihimeMod.buttons.get(keybind.getComponentID()).doClick();
                } else if (keybind.getType() == Keybind.CHECKBOX) {
                    OrihimeMod.checkboxes.get(keybind.getComponentID()).doClick();
                } else if (keybind.getType() == Keybind.TOGGLE_TYPE) {
                    if (!toggle) {
                        toggle = true;
                        if (OrihimeMod.buttons.get(30).getBackground().getRed() > 200) {
                            OrihimeMod.buttons.get(31).doClick();
                        } else if (OrihimeMod.buttons.get(31).getBackground().getBlue() > 200) {
                            OrihimeMod.buttons.get(30).doClick();
                        }
                    }
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (Keybind keybind : Config.keybinds()) {
            if (keybind.matches(e) && keybind.getType() == Keybind.TOGGLE_TYPE) {
                if (toggle) {
                    if (OrihimeMod.buttons.get(30).getBackground().getRed() > 200) {
                        OrihimeMod.buttons.get(31).doClick();
                    } else if (OrihimeMod.buttons.get(31).getBackground().getBlue() > 200) {
                        OrihimeMod.buttons.get(30).doClick();
                    }
                }
                toggle = false;
            }
        }
    }
}
