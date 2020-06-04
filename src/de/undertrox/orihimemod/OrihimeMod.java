package de.undertrox.orihimemod;

import de.undertrox.orihimemod.button.JButtonSaveAsCp;
import de.undertrox.orihimemod.button.JButtonSaveAsDXF;
import de.undertrox.orihimemod.button.JButtonSaveAsSVG;
import de.undertrox.orihimemod.keybind.JInputKeybindDialog;
import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.keybind.KeybindListener;
import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import jp.gr.java_conf.mt777.origami.orihime.Expose;
import jp.gr.java_conf.mt777.origami.orihime.ap;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class OrihimeMod {

    public static final String version = "0.1.5";
    public static List<JButton> buttons = new ArrayList<>();
    public static List<JCheckBox> checkboxes = new ArrayList<>();
    public static JButtonSaveAsCp btnSaveAsCp;
    public static JButtonSaveAsDXF btnSaveAsDXF;
    public static JButtonSaveAsSVG btnSaveAsSVG;
    public static JPopupMenu rightClickMenu;
    public static JMenuItem addKeybind;
    public static JMenu removeKeybind;
    public static String currentKeybindID;
    public static JInputKeybindDialog inputKeybind;

    public static JPopupMenu exportMenu;
    public static ap frame;

    public static void main(String[] args) {
        System.out.println("OrihimeMod version " + version + " is Starting...");
        Config.load("orihimeKeybinds.cfg");
        System.out.println("Loaded "+Config.keybinds().size()+" Keybinds.");

        System.out.println("Starting Orihime...");

        frame = new ap();
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        btnSaveAsSVG = new JButtonSaveAsSVG(frame);
        btnSaveAsSVG.setText("Save as SVG");
        btnSaveAsCp = new JButtonSaveAsCp(frame);
        btnSaveAsCp.setText("Save as CP");
        btnSaveAsDXF = new JButtonSaveAsDXF(frame);
        btnSaveAsDXF.setText("Save as DXF");
        inputKeybind = new JInputKeybindDialog();
        addKeybind = new JMenuItem("Add Keybind");
        removeKeybind = new JMenu("Remove Keybind");
        addKeybind.addActionListener((e) -> {
            inputKeybind.setTitle("Input Keybind for " + currentKeybindID);
            inputKeybind.setSize(350, 100);
            inputKeybind.setModal(true);
            inputKeybind.reset();
            inputKeybind.setLocationRelativeTo(frame);
            inputKeybind.setVisible(true);
        });
        rightClickMenu = new JPopupMenu();
        rightClickMenu.add(addKeybind);
        rightClickMenu.add(removeKeybind);

        System.out.println("Indexing Buttons and Checkboxes...");
        indexButtons(frame);
        indexCheckboxes(frame);
        System.out.println("Found " + buttons.size() + " Buttons and "+ checkboxes.size() +" checkboxes for keybinds");

        btnSaveAsCp.setMargin(new Insets(0,0,0,0));
        //buttons.get(0).getParent().add(btnSaveAsCp);
        buttons.add(btnSaveAsCp);
        btnSaveAsCp.addActionListener(btnSaveAsCp::saveAsCp);

        btnSaveAsDXF.setMargin(new Insets(0,0,0,0));
        //buttons.get(0).getParent().add(btnSaveAsDXF);
        buttons.add(btnSaveAsDXF);
        btnSaveAsDXF.addActionListener(btnSaveAsDXF::saveAsDXF);


        btnSaveAsSVG.setMargin(new Insets(0,0,0,0));
        //buttons.get(0).getParent().add(btnSaveAsSVG);
        buttons.add(btnSaveAsSVG);
        btnSaveAsSVG.addActionListener(btnSaveAsSVG::saveAsSVG);

        exportMenu = new JPopupMenu();
        JMenuItem exportDXF = new JMenuItem("dxf");
        exportDXF.addActionListener( e -> btnSaveAsDXF.doClick());
        JMenuItem exportSVG = new JMenuItem("svg");
        exportSVG.addActionListener( e -> btnSaveAsSVG.doClick());
        JMenuItem exportCP = new JMenuItem("cp");
        exportCP.addActionListener( e -> btnSaveAsCp.doClick());

        exportMenu.add(exportCP);
        exportMenu.add(exportSVG);
        exportMenu.add(exportDXF);

        JButton btnExport = new JButton("Export");
        btnExport.addActionListener(e -> exportMenu.show(btnExport, 0, btnExport.getHeight()));
        btnExport.setMargin(new Insets(0,0,0,0));

        buttons.get(0).getParent().add(btnExport);
        buttons.add(btnExport);

        addMouseListenerToChildren(frame);

        for (ActionListener actionListener : buttons.get(1).getActionListeners()) { // Save button
            buttons.get(1).removeActionListener(actionListener);
        }
        buttons.get(1).addActionListener(OrihimeMod::saveBtnNew);

        Expose.setFrame(frame);
        String title = Expose.getFrameTitle()+" - OrihimeMod version " + version;
        Expose.setFrameTitle0(title);
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

    static void addMouseListenerToChildren(Container container) {
        Component[] children = container.getComponents();
        for (Component child : children) {
            if (child instanceof Container) {
                addMouseListenerToChildren((Container) child);
            }
            if (child instanceof JButton) {
                JButton b = (JButton) child;

                b.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            int index = buttons.indexOf(b);
                            currentKeybindID = "orihimeKeybinds.button."+index;
                            removeKeybind.removeAll();
                            for (Keybind keybind : Config.keybinds()) {
                                if (keybind.getConfigID().equals(currentKeybindID)) {
                                    JMenuItem rk = new JMenuItem(keybind.toString());
                                    removeKeybind.add(rk);
                                    rk.addActionListener((ev) -> {
                                        Config.keybinds().remove(keybind);
                                        Config.updateConfigFile("orihimeKeybinds.cfg");
                                        addTooltips(Config.showNumberTooltips(), Config.showKeybindTooltips());
                                    });
                                }
                            }
                            if (removeKeybind.getSubElements().length == 0) {
                                removeKeybind.setVisible(false);
                            } else {
                                removeKeybind.setVisible(true);
                            }
                            rightClickMenu.show(b, e.getX(), e.getY());
                        }
                    }
                });
            } else if (child instanceof JCheckBox) {
                JCheckBox c = (JCheckBox) child;

                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            int index = checkboxes.indexOf(c);
                            currentKeybindID = "orihimeKeybinds.checkbox."+index;
                            removeKeybind.removeAll();
                            for (Keybind keybind : Config.keybinds()) {
                                if (keybind.getConfigID().equals(currentKeybindID)) {
                                    JMenuItem rk = new JMenuItem(keybind.toString());
                                    removeKeybind.add(rk);
                                    rk.addActionListener((ev) -> {
                                        Config.keybinds().remove(keybind);
                                        Config.updateConfigFile("orihimeKeybinds.cfg");
                                        addTooltips(Config.showNumberTooltips(), Config.showKeybindTooltips());
                                    });
                                }
                            }
                            if (removeKeybind.getSubElements().length == 0) {
                                removeKeybind.setVisible(false);
                            } else {
                                removeKeybind.setVisible(true);
                            }
                            rightClickMenu.show(c, e.getX(), e.getY());
                        }
                    }
                });
            }
        }
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

    static void saveBtnNew(ActionEvent e) {
        Egaki_Syokunin es1 = Expose.getEs1();
        Expose.setExplanationFileName("qqq/kaki.png");
        Expose.readImageFromFile3();
        Expose.Button_kyoutuu_sagyou();
        Expose.setI_mouseDragged_yuukou(0);
        Expose.setI_mouseReleased_yuukou(1);
        es1.kiroku();
        FileDialog fd = new FileDialog(frame);
        fd.setTitle("Save file");
        fd.setVisible(true);
        String fname = fd.getDirectory() + fd.getFile();
        Memo memo1;
        memo1 = es1.getMemo_for_kakidasi();
        if (fname.endsWith(".dxf")) {
            Expose.memoAndName2File(ExportDXF.cpToDxf(Expose.orihime2cp(memo1)), fname);
        } else if (fname.endsWith(".cp")) {
            Expose.memoAndName2File(Expose.orihime2cp(memo1), fname);
        } else {
            if (!(fname.endsWith(".orh"))) {
                fname += ".orh";
            }
            Expose.memoAndName2File(memo1, fname);
        }
        if (fd.getFile()!= null) {
            Expose.setFrameTitle(Expose.getFrameTitle0() + "        " + fd.getFile());
            frame.setTitle(Expose.getFrameTitle());
            es1.set_title(Expose.getFrameTitle());
        }
    }

    public static void keybindDialogClose(KeyEvent lastKeyEvent) {
        inputKeybind.reset();
        if (lastKeyEvent != null) {
            if (currentKeybindID.contains("button")) {
                int id = Integer.parseInt(currentKeybindID.substring(23));
                Config.keybinds().add(new Keybind(Keybind.BUTTON, id, lastKeyEvent.getExtendedKeyCode(),
                        lastKeyEvent.isShiftDown(), lastKeyEvent.isControlDown(), lastKeyEvent.isAltDown()));
            } else if (currentKeybindID.contains("checkbox")) {
                int id = Integer.parseInt(currentKeybindID.substring(25));
                Config.keybinds().add(new Keybind(Keybind.CHECKBOX, id, lastKeyEvent.getExtendedKeyCode(),
                        lastKeyEvent.isShiftDown(), lastKeyEvent.isControlDown(), lastKeyEvent.isAltDown()));
            }
            addTooltips(Config.showNumberTooltips(), Config.showKeybindTooltips());
            Config.updateConfigFile("orihimeKeybinds.cfg");
        }
    }
}
