package de.undertrox.orihimemod.keybind;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeMod;
import de.undertrox.orihimemod.keybind.Keybind;

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
                OrihimeMod.buttons.get(keybind.getButtonNumber()).doClick();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
