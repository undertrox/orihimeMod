package de.undertrox.orihimemod.keybind;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeMod;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeybindListener implements KeyListener {

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
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
