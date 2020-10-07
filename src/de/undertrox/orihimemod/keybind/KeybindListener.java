package de.undertrox.orihimemod.keybind;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeMod;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class KeybindListener implements KeyListener {
    static boolean toggle = false;
    List<AbstractButton> buttons;
    List<JCheckBox> checkBoxes;

    public KeybindListener(List<AbstractButton> buttons, List<JCheckBox> checkBoxes) {
        this.buttons = buttons;
        this.checkBoxes = checkBoxes;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Keybind keybind : Config.keybinds()) {
            if (keybind.matches(e)) {
                if (keybind.getType() == Keybind.BUTTON) {
                    buttons.get(keybind.getComponentID()).doClick();
                } else if (keybind.getType() == Keybind.CHECKBOX) {
                    checkBoxes.get(keybind.getComponentID()).doClick();
                } else if (keybind.getType() == Keybind.TOGGLE_TYPE) {
                    if (!toggle) {
                        toggle = true;
                        if (buttons.get(30).getBackground().getRed() > 200) {
                            buttons.get(31).doClick();
                        } else if (buttons.get(31).getBackground().getBlue() > 200) {
                            buttons.get(30).doClick();
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
                    if (buttons.get(30).getBackground().getRed() > 200) {
                        buttons.get(31).doClick();
                    } else if (buttons.get(31).getBackground().getBlue() > 200) {
                        buttons.get(30).doClick();
                    }
                }
                toggle = false;
            }
        }
    }
}
