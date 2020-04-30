package de.undertrox.orihimemod;

import jp.gr.java_conf.mt777.origami.orihime.ap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class OrihimeMod {

    public static final String version = "0.1.1";
    static List<JButton> buttons = new ArrayList<JButton>();

    public static void main(String[] args) {
        System.out.println("OrihimeMod version " + version + " is Starting...");
        System.out.println("Loading config...");
        Config.load("orihimeKeybinds.cfg");
        System.out.println("Loaded "+Config.keybinds().size()+" Keybinds.");

        System.out.println("Starting Orihime...");
        ap frame = new ap();
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        System.out.println("Indexing Buttons...");
        indexButtons(frame);
        System.out.println("Found " + buttons.size() + " Buttons for keybinds");

        if (Config.showNumberTooltips()) {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setToolTipText("Keybind ID: "+ i);
            }
        }
        KeyListener listener = new KeybindListener();
        frame.addKeyListener(listener);
        addKeyListenerToChildren(listener, frame);
        frame.setTitle(frame.getTitle()+" - OrihimeMod version " + version);
        frame.setVisible(true);
    }

    static void addKeyListenerToChildren(KeyListener listener, Container container) {
        Component[] children = container.getComponents();
        for (Component child : children) {
            child.addKeyListener(listener);
            if (child instanceof Container) {
                addKeyListenerToChildren(listener, (Container) child);
            }
        }
    }
    static void indexButtons(Container container) {
        Component[] children = container.getComponents();
        for (Component child : children) {
            if (child instanceof JButton) {
                buttons.add((JButton) child);
            }
            if (child instanceof Container) {
                indexButtons((Container) child);
            }
        }
    }
}
