package de.undertrox.orihimemod;

import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.keybind.KeybindListener;
import jp.gr.java_conf.mt777.origami.orihime.ExposeClasses;
import jp.gr.java_conf.mt777.origami.orihime.ap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrihimeMod {

    public static final String version = "0.1.4";
    public static List<JButton> buttons = new ArrayList<>();
    public static List<JCheckBox> checkboxes = new ArrayList<>();
    public static JButtonSaveAsCp btnSaveAsCp;

    public static void main(String[] args) {
        System.out.println("OrihimeMod version " + version + " is Starting...");
        System.out.println("Loading config...");
        Config.load("orihimeKeybinds.cfg");
        System.out.println("Loaded "+Config.keybinds().size()+" Keybinds.");

        System.out.println("Starting Orihime...");

        ap frame = new ap();
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        btnSaveAsCp = new JButtonSaveAsCp(frame);
        btnSaveAsCp.setText("Save as CP");

        System.out.println("Indexing Buttons and Checkboxes...");
        indexButtons(frame);
        indexCheckboxes(frame);
        System.out.println("Found " + buttons.size() + " Buttons and "+ checkboxes.size() +" checkboxes for keybinds");

        btnSaveAsCp.setMargin(new Insets(0,0,0,0));
        buttons.get(1).getParent().add(btnSaveAsCp);
        buttons.add(btnSaveAsCp);
        btnSaveAsCp.addActionListener(btnSaveAsCp::saveAsCp);

        ExposeClasses.setFrame(frame);
        String title = ExposeClasses.getFrameTitle()+" - OrihimeMod version " + version;
        ExposeClasses.setFrameTitle0(title);
        frame.setTitle(title);

        addTooltips(Config.showNumberTooltips(), Config.showKeybindTooltips());
        KeyListener listener = new KeybindListener();
        frame.addKeyListener(listener);
        addKeyListenerToChildren(listener, frame);
        if (Config.useDarkMode()) {
            enableDarkMode(frame);
        }
        if (Config.useExpertMode()) {
            for (Component child : frame.getComponents()) {
                frame.remove(child);
            }
        }
        frame.setVisible(true);
    }

    static void addKeyListenerToChildren(KeyListener listener, Container container) {
        Component[] children = container.getComponents();
        for (Component child : children) {
            if (child.getKeyListeners().length>0) {
                for (KeyListener keyListener : child.getKeyListeners()) {
                    child.removeKeyListener(keyListener);
                }
            }
            if (child instanceof JButton || child instanceof JCheckBox) {
                JComponent c = (JComponent) child;
                c.getInputMap(JComponent.WHEN_FOCUSED)
                        .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,false), "none");
                c.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                        .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,false), "none");
            }
            if (!(child instanceof JTextField)) {
                child.addKeyListener(listener);
            }
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

    static void indexCheckboxes(Container container) {
        Component[] children = container.getComponents();
        for (Component child : children) {
            if (child instanceof JCheckBox) {
                checkboxes.add((JCheckBox) child);
            }
            if (child instanceof Container) {
                indexCheckboxes((Container) child);
            }
        }
    }

    static void enableDarkMode(ap frame) {
        frame.setBackground(Color.black);
        enableDarkModeOn(frame);
    }

    static void enableDarkModeOn(Container container) {
        Component[] children = container.getComponents();
        for (Component child : children) {

            if (child instanceof Panel || child instanceof JPanel) {
                child.setBackground(Color.getHSBColor(0,0, 0.3f));
                child.setForeground(Color.getHSBColor(0,0,0.9f));
            } else if (child instanceof JButton) {
                ((JButton) child).setBorder(BorderFactory.createLineBorder(Color.black));
                child.setBackground(Color.getHSBColor(0,0, 0.2f));
                child.setForeground(Color.getHSBColor(0,0,0.9f));
            } else if (child instanceof  JTextField) {
                ((JTextField) child).setBorder(BorderFactory.createLineBorder(Color.black));
                child.setBackground(Color.getHSBColor(0,0, 0.4f));
                child.setForeground(Color.getHSBColor(0,0,1f));
            }else if (child instanceof JLabel) {
                ((JLabel) child).setBorder(BorderFactory.createLineBorder(Color.black));
                child.setBackground(Color.getHSBColor(0,0, 0.3f));
                child.setForeground(Color.getHSBColor(0,0,0.9f));
            } else if (child instanceof JCheckBox) {
                JCheckBox checkBox = ((JCheckBox) child);
                checkBox.setBorder(BorderFactory.createLineBorder(Color.black));
                child.setBackground(Color.getHSBColor(0,0, 0.3f));
                child.setForeground(Color.getHSBColor(0,0,0.9f));
            } else {
                System.out.println(child.getClass().getName());
            }
            if (child instanceof Container) {
                enableDarkModeOn((Container) child);
            }
        }
    }

    static void addTooltips(boolean showNumberTooltips, boolean showKeybindTooltips) {
        for (int i = 0; i < buttons.size(); i++) {
            JButton btn = buttons.get(i);
            if (showNumberTooltips) {
                btn.setToolTipText("Keybind ID: orihimeKeybinds.button." + i);
            }
            if (showKeybindTooltips) {
                StringBuilder b = new StringBuilder();
                for (Keybind keybind : getKeybindsFor(i, Keybind.BUTTON)) {
                    b.append("<br>"+keybind.toString());
                }
                btn.setToolTipText(btn.getToolTipText()+b.toString());
            }
            btn.setToolTipText("<html>"+btn.getToolTipText()+"</html>");
        }
        for (int i = 0; i < checkboxes.size(); i++) {
            JCheckBox checkBox = checkboxes.get(i);
            if (showNumberTooltips) {
                checkBox.setToolTipText("Keybind ID: orihimeKeybinds.checkbox." + i);
            }
            if (showKeybindTooltips) {
                StringBuilder b = new StringBuilder();
                for (Keybind keybind : getKeybindsFor(i, Keybind.CHECKBOX)) {
                    b.append("<br>"+keybind.toString());
                }
                checkBox.setToolTipText(checkBox.getToolTipText()+b.toString());
            }
            checkBox.setToolTipText("<html>"+checkBox.getToolTipText()+"</html>");
        }
    }

    static List<Keybind> getKeybindsFor(int buttonId, int type) {
        List<Keybind> result = new ArrayList<>();
        for (Keybind keybind : Config.keybinds()) {
            if (keybind.getComponentID()==buttonId&&keybind.getType()==type){
                result.add(keybind);
            }
        }
        return result;
    }
}
