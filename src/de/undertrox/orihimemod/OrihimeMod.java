package de.undertrox.orihimemod;

import jp.gr.java_conf.mt777.origami.orihime.ap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class OrihimeMod {

    public static final String version = "0.1.1";
    static List<JButton> buttons = new ArrayList<JButton>();

    public static void main(String[] args) {
        System.out.println("OrihimeMod version " + version + " is Starting...");
        ap frame = new ap();

        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        System.out.println("Indexing Buttons...");
        indexButtons(frame);
        System.out.println("Found " + buttons.size() + " Buttons for keybinds");
        System.out.println("Loading config...");
        Config.load("orihimeKeybinds.cfg");
        List<Keybind> keybinds=Config.keybinds();
        if (Config.showNumberTooltips()) {
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).setToolTipText("Keybind ID: "+ i);
            }
        }
        System.out.println("Loaded "+keybinds.size()+" Keybinds.");
        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                for (Keybind keybind : keybinds) {
                    if (keybind.matches(e)) {
                        buttons.get(keybind.getButtonNumber()).doClick();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
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
