package de.undertrox.orihimemod.newui.button;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeModWindow;
import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.mapping.ButtonMapping;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuItemFactory {

    private ButtonMapping mapping;
    private OrihimeModWindow window;

    public MenuItemFactory(ButtonMapping mapping, OrihimeModWindow window) {
        this.window = window;
        this.mapping = mapping;
    }

    public JMenuItem createMenuItem(String mappingId, String text, ActionListener action) {
        JMenuItem menuItem = new JMenuItem(text);
        mapping.getButtons().add(menuItem);
        updateKeybinds(mappingId, menuItem);
        menuItem.addActionListener(action);
        menuItem.addMouseListener(window.createRightClickListener(menuItem, mapping));
        return menuItem;
    }

    private void updateKeybinds(String mappingId, JMenuItem menuItem) {
        Runnable r = () -> {
            menuItem.setAccelerator(null);
            for (Keybind keybind : Config.keybinds()) {
                if (keybind.getConfigID().equalsIgnoreCase("orihimeKeybinds." + mapping.getValue(mappingId))) {
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(keybind.getKeyCode(),
                            keybind.getModifiersInt()));
                }
            }
        };
        r.run();
        Config.onReload(r);
    }

    public JMenuItem createMenuItem(String mappingId, String text) {
        JMenuItem menuItem = new JMenuItem(text);
        AbstractButton btn = mapping.get(mappingId);
        updateKeybinds(mappingId, menuItem);
        menuItem.addActionListener(e -> btn.doClick());
        menuItem.addMouseListener(window.createRightClickListener(btn, mapping));
        return menuItem;
    }
}
