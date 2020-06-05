package de.undertrox.orihimemod.keybind;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeMod;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeybindListener implements KeyListener {
    static boolean ctrlDown = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getExtendedKeyCode() == KeyEvent.VK_CONTROL) {
            if (!ctrlDown) {
                ctrlDown = true;
                if (OrihimeMod.buttons.get(30).getBackground().getRed() > 200) {
                    OrihimeMod.buttons.get(31).doClick();
                } else if (OrihimeMod.buttons.get(31).getBackground().getBlue() > 200) {
                    OrihimeMod.buttons.get(30).doClick();
                }
            }
        }
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
        if (e.getExtendedKeyCode() == KeyEvent.VK_CONTROL) {
            if (OrihimeMod.buttons.get(30).getBackground().getRed() > 200) {
                OrihimeMod.buttons.get(31).doClick();
            } else if (OrihimeMod.buttons.get(31).getBackground().getBlue() > 200) {
                OrihimeMod.buttons.get(30).doClick();
            }
            ctrlDown = false;
        }
    }
}
