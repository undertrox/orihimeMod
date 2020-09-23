package de.undertrox.orihimemod;

import de.undertrox.orihimemod.button.JButtonSaveAsCp;
import de.undertrox.orihimemod.button.JButtonSaveAsDXF;
import de.undertrox.orihimemod.button.JButtonSaveAsSVG;
import de.undertrox.orihimemod.button.TextButton;
import de.undertrox.orihimemod.keybind.JInputKeybindDialog;
import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.keybind.KeybindListener;
import de.undertrox.orihimemod.mapping.ButtonMapping;
import de.undertrox.orihimemod.traverse.Darkmode;
import jp.gr.java_conf.mt777.origami.dougu.keijiban.TextRenderer;
import jp.gr.java_conf.mt777.origami.orihime.Expose;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;
import jp.gr.java_conf.mt777.origami.orihime.ap;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static de.undertrox.orihimemod.OrihimeMod.orihimeVersion;
import static de.undertrox.orihimemod.OrihimeMod.version;

public class OrihimeModWindow {
    public List<JButton> buttons = new ArrayList<>();
    public List<JCheckBox> checkboxes = new ArrayList<>();
    public JButtonSaveAsCp btnSaveAsCp;
    public JButtonSaveAsDXF btnSaveAsDXF;
    public JButtonSaveAsSVG btnSaveAsSVG;
    public JPopupMenu rightClickMenu;
    public JMenuItem addKeybind;
    public JMenu removeKeybind;
    public String currentKeybindID;
    public JInputKeybindDialog inputKeybind;
    public boolean changed = false;
    public ButtonMapping mapping;
    public Expose expose;

    public ActionListener[] oldSaveButton;

    public JPopupMenu exportMenu;
    public OrihimeFrame frame;

    public String filename = "";
    public String fullFileName= "";

    public Config config;

    public AutosaveHandler autosaver;

    public ResourceBundle tooltips;

    public OrihimeModWindow() {
        System.out.println("OrihimeMod version " + version + " is Starting...");
        Config.load("orihimeKeybinds.cfg");
        config = Config.getInstance();
        System.out.println("Loaded "+Config.keybinds().size()+" Keybinds.");
        System.out.println("Loading Button mapping for Mod version " + version + " and Orihime version " + orihimeVersion);
        mapping = ButtonMapping.load(version, orihimeVersion);
        mapping.setButtons(buttons);
        mapping.setCheckboxes(checkboxes);
        System.out.println("Loading Tooltips");
        tooltips = ResourceBundle.getBundle("tooltips");
        System.out.println("Starting Orihime...");

        initFrame();
        expose = new Expose(frame);

        System.out.println("Indexing Buttons and Checkboxes...");
        indexButtons(frame);
        indexCheckboxes(frame);
        System.out.println("Found " + buttons.size() + " Buttons and "+ checkboxes.size() +" checkboxes for keybinds");

        System.out.println("Initializing own UI elements");
        oldSaveButton = mapping.get("save").getActionListeners();
        initCustomButtons();
        initRightClickMenu();
        fixMVButtons();
        addContextMenuToLengthsAndAngles();

        addMouseListenerToChildren(frame);

        for (ActionListener actionListener : mapping.get("save").getActionListeners()) { // Save button
            mapping.get("save").removeActionListener(actionListener);
        }
        mapping.get("save").addActionListener(e -> saveBtnNew(e, false));

        expose.setFrame(frame);
        String title = expose.getFrameTitle()+" - OrihimeMod version " + OrihimeMod.version;
        expose.setFrameTitle0(title);
        frame.setTitle(title);

        addTooltips(Config.showNumberTooltips(), Config.showKeybindTooltips(), Config.showHelpTooltips());
        KeyListener listener = new KeybindListener(buttons, checkboxes);
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
        // Remove force close on exit
        frame.removeWindowListener(frame.getWindowListeners()[0]);
        // Add own window closing listener
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveBeforeAction(() -> {
                    autosaver.stop();
                    e.getWindow().dispose();
                });
            }
        });
        ActionListener removeEverything = mapping.get("remove_everything").getActionListeners()[0];
        ActionListener newRemoveEverything = e -> {
            removeEverything.actionPerformed(e);
            frame.textRenderer = new TextRenderer(frame);
            frame.keijiban = frame.textRenderer;
        };
        mapping.get("remove_everything").removeActionListener(removeEverything);
        mapping.get("remove_everything").addActionListener(e -> saveBeforeAction(() -> newRemoveEverything.actionPerformed(e)));

        if (Config.justUpdatedTo0_2_0) {
            String[] options = {"Old", "New"};
            int result = JOptionPane.showOptionDialog(
                    frame,
                    "Important Change:\n\n This version changes how the save button works.\n " +
                            "After the initial save/open, it will no longer ask where to save, but automatically \n" +
                            "save to the last location. If you want to save to a new location, you have to use \n" +
                            "Export -> Save as. This also means that when you press the Save button after\n" +
                            "pressing the Save As button, the file will be saved in the location you selected \n" +
                            "using the Save As button.\n\n" +
                            "All Other export options will NOT affect where the Save button saves data.\n\n" +
                            "Do you want to use the new saving behavior, or do you want to keep the old one?\n" +
                            "This can always be changed by editing the config file (orihimeKeybinds.cfg), but\n" +
                            "this dialog will not be shown again.",
                    "Important Change notes",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,     //no custom icon
                    options,  //button titles
                    options[0] //default button
            );
            // use old behavior
            if (result == JOptionPane.YES_OPTION) {
                Config.setUseNewSave(false);
            } else { // use new behavior
                System.out.println("use new behavior");
                Config.setUseNewSave(true);
            }
        }
        System.out.println("Configuring autosaver");
        autosaver = new AutosaveHandler(frame, Config.useAutosave(), Config.autoSaveInterval(), Config.autoSaveMaxAge(), filename);
    }

    private void addContextMenuToLengthsAndAngles() {
        addContextMenuToMeasureField(mapping.get("measure_l1"));
        addContextMenuToMeasureField(mapping.get("measure_l2"));
        addContextMenuToMeasureField(mapping.get("measure_a1"));
        addContextMenuToMeasureField(mapping.get("measure_a2"));
        addContextMenuToMeasureField(mapping.get("measure_a3"));

    }

    private void addContextMenuToMeasureField(AbstractButton measuringButton) {
        Container parent = measuringButton.getParent();
        boolean found = false;
        for (Component component : parent.getComponents()) {
            if (found && component instanceof JLabel) {
                JLabel measuringLabel = (JLabel) component;
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem copy = new JMenuItem("Copy");
                copy.addActionListener(e -> {
                    Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection selection;

                    try {
                        selection = new StringSelection(measuringLabel.getText());
                        c.setContents(selection, selection);
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                });
                popupMenu.add(copy);
                measuringLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            popupMenu.setInvoker(e.getComponent());
                            popupMenu.setLocation(e.getLocationOnScreen());
                            popupMenu.setVisible(true);
                        }
                    }
                });
                return;
            }
            if (component == measuringButton) {
                found = true;
            }
        }
    }

    // Fixes the Colors of M/V/E/A buttons not working on mac
    private void fixMVButtons() {
        mapping.get("mountain").setOpaque(true);
        mapping.get("mountain").setBorderPainted(false);
        mapping.get("valley").setOpaque(true);
        mapping.get("valley").setBorderPainted(false);
        mapping.get("aux").setOpaque(true);
        mapping.get("aux").setBorderPainted(false);
        mapping.get("edge").setOpaque(true);
        mapping.get("edge").setBorderPainted(false);
    }

    public void show() {
        frame.setVisible(true);
        System.out.println("Starting autosaver");
        autosaver.start();
    }

    private void initRightClickMenu() {
        addKeybind = new JMenuItem("Add Keybind");
        removeKeybind = new JMenu("Remove Keybind");
        addKeybind.addActionListener(e -> {
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
    }

    private void initCustomButtons() {
        btnSaveAsSVG = new JButtonSaveAsSVG(frame);
        btnSaveAsSVG.setText("Save as SVG");
        btnSaveAsCp = new JButtonSaveAsCp(frame);
        btnSaveAsCp.setText("Save as CP");
        btnSaveAsDXF = new JButtonSaveAsDXF(frame);
        btnSaveAsDXF.setText("Save as DXF");
        inputKeybind = new JInputKeybindDialog(this::keybindDialogClose);

        btnSaveAsCp.setMargin(new Insets(0,0,0,0));
        buttons.add(btnSaveAsCp);
        btnSaveAsCp.addActionListener(btnSaveAsCp::saveAsCp);

        btnSaveAsDXF.setMargin(new Insets(0,0,0,0));
        buttons.add(btnSaveAsDXF);
        btnSaveAsDXF.addActionListener(btnSaveAsDXF::saveAsDXF);

        btnSaveAsSVG.setMargin(new Insets(0,0,0,0));
        buttons.add(btnSaveAsSVG);
        btnSaveAsSVG.addActionListener(btnSaveAsSVG::saveAsSVG);

        frame.tb = new TextButton(frame, "Text Mode");
        buttons.add(frame.tb);
        frame.tb.addActionListener(frame::textButtonClick);


        exportMenu = new JPopupMenu();
        JMenuItem exportDXF = new JMenuItem("dxf");
        exportDXF.addActionListener( e -> btnSaveAsDXF.doClick());
        JMenuItem exportSVG = new JMenuItem("svg");
        exportSVG.addActionListener( e -> btnSaveAsSVG.doClick());
        JMenuItem exportCP = new JMenuItem("cp");
        exportCP.addActionListener( e -> btnSaveAsCp.doClick());
        JMenuItem exportPng = new JMenuItem("png");
        exportPng.addActionListener( e -> mapping.get("export_image").doClick());
        JMenuItem saveAs = new JMenuItem("Save as");
        saveAs.addActionListener(e -> saveBtnNew(e, true));
        JMenuItem exportORH = new JMenuItem("orh");
        exportORH.addActionListener(e -> {
            for (ActionListener actionListener : oldSaveButton) {
                actionListener.actionPerformed(e);
            }
        });
        JButton btnSaveAs=new JButton();
        btnSaveAs.addActionListener(e -> saveAs.doClick());

        buttons.add(btnSaveAs);

        exportMenu.add(exportCP);
        exportMenu.add(exportSVG);
        exportMenu.add(exportDXF);
        exportMenu.add(exportPng);
        exportMenu.add(exportORH);
        if (Config.useNewSave() || Config.justUpdatedTo0_2_0) {
            exportMenu.add(saveAs);
        }

        JButton btnExport = new JButton("Export");
        btnExport.addActionListener(e -> {
            exportMenu.show(btnExport, 0, btnExport.getHeight());
            expose.readImageFromFile3();
            expose.setI_mouseDragged_yuukou(0);
            expose.setI_mouseReleased_yuukou(0);
        });
        btnExport.setMargin(new Insets(0,0,0,0));

        mapping.get("save").getParent().add(btnExport);
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        p.setBackground(Color.PINK);
        //p.setBounds(2, 2, 102, 55);
        p.add(frame.textField);
        p.add(frame.tb);
        mapping.get("change_circle_color").getParent().getParent().setLayout(new GridLayout(29, 1));
        mapping.get("change_circle_color").getParent().getParent().add(p);
        buttons.add(btnExport);
    }

    private void initFrame() {
        frame = new OrihimeFrame();
        frame.observers.add((newCfs, newFs) -> {
            this.filename = newFs;
            this.fullFileName = newCfs;
        });
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                changed = true;
            }
        });
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
    }

    void saveBeforeAction(Runnable action) {
        if (changed) {
            int n = JOptionPane.showOptionDialog(frame,"Would you like to save?","Save",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Yes", "No", "Cancel"}, "Yes");
            switch (n) {
                case 0:
                    mapping.get("save").doClick();
                    if (!changed) {
                        action.run();
                    }
                    break;
                case 1:
                    action.run();
                    break;
                default:
                    break;
            }
        } else {
            action.run();
        }
    }


    void addMouseListenerToChildren(Container container) {
        Component[] children = container.getComponents();
        for (Component child : children) {
            if (child instanceof Container) {
                addMouseListenerToChildren((Container) child);
            }
            if (child instanceof AbstractButton) {
                AbstractButton b = (AbstractButton) child;
                b.addMouseListener(new RightClickListener(b));
            }
        }
    }

    class RightClickListener extends MouseAdapter {

        AbstractButton b;
        String keybindId;

        public RightClickListener(AbstractButton button) {
            b = button;
            int index = buttons.indexOf(b);
            String bOrC = "button";
            if (index < 0) {
                index = checkboxes.indexOf(b);
                bOrC = "checkbox";
            }
            if (index < 0) keybindId = "";
            keybindId = "orihimeKeybinds." + bOrC + "." + index;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            changed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                currentKeybindID = keybindId;
                removeKeybind.removeAll();
                for (Keybind keybind : Config.keybinds()) {
                    if (keybind.getConfigID().equals(currentKeybindID)) {
                        JMenuItem rk = new JMenuItem(keybind.toString());
                        removeKeybind.add(rk);
                        rk.addActionListener(ev -> {
                            Config.keybinds().remove(keybind);
                            Config.updateConfigFile("orihimeKeybinds.cfg");
                            addTooltips(Config.showNumberTooltips(), Config.showKeybindTooltips(), Config.showHelpTooltips());
                        });
                    }
                }
                rightClickMenu.setInvoker(b);
                rightClickMenu.setLocation(e.getLocationOnScreen());
                rightClickMenu.setVisible(true);
            }
        }
    }

    void addKeyListenerToChildren(KeyListener listener, Container container) {
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

    void indexButtons(Container container) {
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

    void indexCheckboxes(Container container) {
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

    void enableDarkMode(ap frame) {
        frame.setBackground(Color.black);
        new Darkmode(frame).enable();
    }

    void addTooltips(boolean showNumberTooltips, boolean showKeybindTooltips, boolean showHelpTooltips) {
        for (int i = 0; i < buttons.size(); i++) {
            JButton btn = buttons.get(i);
            btn.setToolTipText(btn.getToolTipText() == null? "" : btn.getToolTipText() + "<br>");
            if (showHelpTooltips) {
                String key = mapping.getKey("button." + i);
                if ( key != null && tooltips.containsKey(key)) {
                    btn.setToolTipText(btn.getToolTipText() + tooltips.getString(key));
                }
            }
            if (showNumberTooltips) {
                btn.setToolTipText(btn.getToolTipText() + "<br>Keybind ID: orihimeKeybinds.button." + i);
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
            checkBox.setToolTipText(checkBox.getToolTipText() == null? "" : checkBox.getToolTipText() + "<br>");
            if (showHelpTooltips) {
                String key = mapping.getKey("checkbox." + i);
                if (tooltips.containsKey(key)) {
                    checkBox.setToolTipText(checkBox.getToolTipText() + tooltips.getString(key));
                }
            }
            if (showNumberTooltips) {
                checkBox.setToolTipText(checkBox.getToolTipText() + "<br>Keybind ID: orihimeKeybinds.checkbox." + i);
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

    List<Keybind> getKeybindsFor(int buttonId, int type) {
        List<Keybind> result = new ArrayList<>();
        for (Keybind keybind : Config.keybinds()) {
            if (keybind.getComponentID()==buttonId&&keybind.getType()==type){
                result.add(keybind);
            }
        }
        return result;
    }

    void saveBtnNew(ActionEvent e, boolean saveAs) {
        if (!Config.useNewSave()) {
            saveAs = true;
        }

        expose.setExplanationFileName("qqq/kaki.png");
        expose.readImageFromFile3();
        expose.Button_kyoutuu_sagyou();
        expose.setI_mouseDragged_yuukou(0);
        expose.setI_mouseReleased_yuukou(1);
        FileDialog fd = new FileDialog(frame);
        String fname = "";
        boolean success;
        if (fullFileName.isEmpty() || saveAs) {
            fd.setTitle("Save file");
            fd.setMode(FileDialog.SAVE);
            fd.setVisible(true);
            fname = fd.getDirectory() + fd.getFile();
            if (fd.getFile() == null) {
                changed = true; return;
            }
            success = SaveHelper.saveTo(frame, fname);
        } else {
            success = SaveHelper.saveTo(frame, fullFileName);
        }
        if (!success) {
            JOptionPane.showMessageDialog(null, "Error while saving the file, please try again with a different output format.",
                    "Error while saving", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (fd.getFile() != null) {
            expose.setFrameTitle(expose.getFrameTitle0() + "        " + fd.getFile());
            frame.setTitle(expose.getFrameTitle());
            expose.getEs1().set_title(expose.getFrameTitle());
            fullFileName = fname;
            setFilename(fd.getFile());
            if (filename.contains(".")) {
                setFilename(filename.substring(0, filename.lastIndexOf(".") - 1));
            }
            changed = false;
        } else if (!fullFileName.isEmpty() && !saveAs) {
            changed = false;
        } else {
            changed = true;
        }
    }

    public void setFilename(String filename) {
        this.filename = filename;
        this.autosaver.setBaseFileName(filename);
    }

    void keybindDialogClose(KeyEvent lastKeyEvent) {
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
            addTooltips(Config.showNumberTooltips(), Config.showKeybindTooltips(), Config.showHelpTooltips());
            Config.updateConfigFile("orihimeKeybinds.cfg");
        }
    }
}
