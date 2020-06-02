package de.undertrox.orihimemod.keybind;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeMod;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Instant;
import java.util.Date;

public class KeybindListener implements KeyListener {
    static long cooldown = 0;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getExtendedKeyCode() == KeyEvent.VK_ALT) {
            if (Instant.now().getEpochSecond()-cooldown > 1) {
                OrihimeMod.buttons.get(31).doClick();
            }
            cooldown = Instant.now().getEpochSecond();
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
        if (e.getExtendedKeyCode() == KeyEvent.VK_ALT) {
            OrihimeMod.buttons.get(30).doClick();
            cooldown = 0;
        }
    }
}
