package de.undertrox.orihimemod;

import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.keybind.KeybindListener;
import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import jp.gr.java_conf.mt777.origami.orihime.ExposeClasses;
import jp.gr.java_conf.mt777.origami.orihime.ap;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class OrihimeMod {

    public static final String version = "0.1.3";
    public static List<JButton> buttons = new ArrayList<JButton>();
    public static JButton btnSaveAsCp = new JButton();

    public static void main(String[] args) {
        System.out.println("OrihimeMod version " + version + " is Starting...");
        System.out.println("Loading config...");
        Config.load("orihimeKeybinds.cfg");
        System.out.println("Loaded "+Config.keybinds().size()+" Keybinds.");

        btnSaveAsCp.setText("Save as CP");

        System.out.println("Starting Orihime...");
        ap frame = new ap();
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        System.out.println("Indexing Buttons...");
        indexButtons(frame);
        btnSaveAsCp.setMargin(new Insets(0,0,0,0));
        buttons.get(1).getParent().add(btnSaveAsCp);
        ExposeClasses.setFrame(frame);
        String title = ExposeClasses.getFrameTitle()+" - OrihimeMod version " + version;
        ExposeClasses.setFrameTitle0(title);
        frame.setTitle(title);

        btnSaveAsCp.addActionListener(e -> {
            Egaki_Syokunin es1 = ExposeClasses.getEs1();
            ExposeClasses.setExplanationFileName("qqq/kaki.png");
            ExposeClasses.readImageFromFile3();
            ExposeClasses.Button_kyoutuu_sagyou();
            ExposeClasses.setI_mouseDragged_yuukou(0);
            ExposeClasses.setI_mouseReleased_yuukou(1);
            es1.kiroku();
            FileDialog fd = new FileDialog(frame);
            fd.setTitle("Save file as .cp");
            fd.setVisible(true);
            String fname = fd.getDirectory() + fd.getFile();
            Memo memo1;
            memo1 = es1.getMemo_for_kakidasi();

            if (!fname.endsWith(".cp")) {
                fname = fname + ".cp";
            }
            ExposeClasses.memoAndName2File(ExposeClasses.orihime2cp(memo1), fname);
            ExposeClasses.setFrameTitle(ExposeClasses.getFrameTitle0() + "        " + fd.getFile());
            frame.setTitle(ExposeClasses.getFrameTitle());
            es1.set_title(ExposeClasses.getFrameTitle());

        });
        buttons.add(btnSaveAsCp);

        System.out.println("Found " + buttons.size() + " Buttons for keybinds");
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

    static void enableDarkMode(ap frame) {
        frame.setBackground(Color.black);
        enableDarkModeOn(frame);
    }

    static void enableDarkModeOn(Container container) {
        Component[] children = container.getComponents();
        // OZ seems to be the Drawing class for the folded Shape
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

    static void addTooltips(boolean numberTooltips, boolean keybindTooltips) {

        for (int i = 0; i < buttons.size(); i++) {
            JButton btn = buttons.get(i);
            if (numberTooltips) {
                btn.setToolTipText("Keybind ID: " + i);
            }
            if (keybindTooltips) {
                StringBuilder b = new StringBuilder();
                for (Keybind keybind : getKeybindsForButton(i)) {
                    b.append("<br>"+keybind.toString());
                }
                btn.setToolTipText(btn.getToolTipText()+b.toString());
            }
            btn.setToolTipText("<html>"+btn.getToolTipText()+"</html>");
        }

    }

    static List<Keybind> getKeybindsForButton(int buttonId) {
        List<Keybind> result = new ArrayList<>();
        for (Keybind keybind : Config.keybinds()) {
            if (keybind.getButtonNumber()==buttonId){
                result.add(keybind);
            }
        }
        return result;
    }
}
