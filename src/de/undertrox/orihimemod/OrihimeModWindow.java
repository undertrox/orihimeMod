package de.undertrox.orihimemod;

import de.undertrox.orihimemod.button.JButtonSaveAsCp;
import de.undertrox.orihimemod.button.JButtonSaveAsDXF;
import de.undertrox.orihimemod.button.JButtonSaveAsSVG;
import de.undertrox.orihimemod.button.TextButton;
import de.undertrox.orihimemod.config.Config;
import de.undertrox.orihimemod.config.ConfigFileManager;
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
    public Expose exposeMethods;

    public ActionListener[] oldSaveButton;

    public JPopupMenu exportMenu;
    public OrihimeFrame frame;

    public String filename = "";
    public String fullFileName = "";

    public ConfigFileManager configManager;

    public AutosaveHandler autosaver;

    public ResourceBundle tooltips;

    public OrihimeModWindow() {
        System.out.println("OrihimeMod version " + version + " is Starting...");
        initConfig();
        initButtonMapping();
        loadToolTipFile();
        System.out.println("Starting Orihime...");
        initOrihimeFrame();
        indexOriginalUI();
        initOwnUI();
        if (configManager.getConfig().justUpdatedTo0_2_0) {
            askWhichSavingBehavior();
        }
        initAutosaver();
    }

    public void show() {
        frame.setVisible(true);
        System.out.println("Starting autosaver");
        autosaver.start();
    }

    public void setFilename(String filename) {
        this.filename = filename;
        this.autosaver.setBaseFileName(filename);
    }

    void saveBeforeAction(Runnable action) {
        if (changed) {
            int n = JOptionPane.showOptionDialog(frame, "Would you like to save?", "Save",
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

    void addKeyListenerToChildren(KeyListener listener, Container container) {
        Component[] children = container.getComponents();
        for (Component child : children) {
            if (child.getKeyListeners().length > 0) {
                for (KeyListener keyListener : child.getKeyListeners()) {
                    child.removeKeyListener(keyListener);
                }
            }
            if (child instanceof JButton || child instanceof JCheckBox) {
                JComponent c = (JComponent) child;
                c.getInputMap(JComponent.WHEN_FOCUSED)
                        .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "none");
                c.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                        .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "none");
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
            btn.setToolTipText(btn.getToolTipText() == null ? "" : btn.getToolTipText() + "<br>");
            if (showHelpTooltips) {
                String key = mapping.getKey("button." + i);
                if (key != null && tooltips.containsKey(key)) {
                    btn.setToolTipText(btn.getToolTipText() + tooltips.getString(key));
                }
            }
            if (showNumberTooltips) {
                btn.setToolTipText(btn.getToolTipText() + "<br>Keybind ID: " + mapping.getKey("button." + i));
            }
            if (showKeybindTooltips) {
                StringBuilder b = new StringBuilder();
                for (Keybind keybind : getKeybindsFor(i, Keybind.BUTTON)) {
                    b.append("<br>").append(keybind.toString());
                }
                btn.setToolTipText(btn.getToolTipText() + b);
            }
            btn.setToolTipText("<html>" + btn.getToolTipText() + "</html>");
        }
        for (int i = 0; i < checkboxes.size(); i++) {
            JCheckBox checkBox = checkboxes.get(i);
            checkBox.setToolTipText(checkBox.getToolTipText() == null ? "" : checkBox.getToolTipText() + "<br>");
            if (showHelpTooltips) {
                String key = mapping.getKey("checkbox." + i);
                if (tooltips.containsKey(key)) {
                    checkBox.setToolTipText(checkBox.getToolTipText() + tooltips.getString(key));
                }
            }
            if (showNumberTooltips) {
                checkBox.setToolTipText(checkBox.getToolTipText() + "<br>Keybind ID: " + mapping.getKey("checkbox." + i));
            }
            if (showKeybindTooltips) {
                StringBuilder b = new StringBuilder();
                for (Keybind keybind : getKeybindsFor(i, Keybind.CHECKBOX)) {
                    b.append("<br>").append(keybind.toString());
                }
                checkBox.setToolTipText(checkBox.getToolTipText() + b);
            }
            checkBox.setToolTipText("<html>" + checkBox.getToolTipText() + "</html>");
        }
    }

    List<Keybind> getKeybindsFor(int buttonId, int type) {
        List<Keybind> result = new ArrayList<>();
        String mappingId;
        if (type == Keybind.CHECKBOX) {
            mappingId = mapping.getKey("checkbox." + buttonId);
        } else {
            mappingId = mapping.getKey("button." + buttonId);
        }
        for (Keybind keybind : configManager.getConfig().keybinds()) {
            if (keybind.getMappingID().equals(mappingId)) {
                result.add(keybind);
            }
        }
        return result;
    }

    void saveBtnNew(ActionEvent e, boolean saveAs) {
        if (!configManager.getConfig().useNewSave()) {
            saveAs = true;
        }

        exposeMethods.setExplanationFileName("qqq/kaki.png");
        exposeMethods.readImageFromFile3();
        exposeMethods.Button_kyoutuu_sagyou();
        exposeMethods.setI_mouseDragged_yuukou(0);
        exposeMethods.setI_mouseReleased_yuukou(1);
        FileDialog fd = new FileDialog(frame);
        String fname = "";
        boolean success;
        if (fullFileName.isEmpty() || saveAs) {
            fd.setTitle("Save file");
            fd.setMode(FileDialog.SAVE);
            fd.setVisible(true);
            fname = fd.getDirectory() + fd.getFile();
            if (fd.getFile() == null) {
                changed = true;
                return;
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
            exposeMethods.setFrameTitle(exposeMethods.getFrameTitle0() + "        " + fd.getFile());
            frame.setTitle(exposeMethods.getFrameTitle());
            exposeMethods.getEs1().set_title(exposeMethods.getFrameTitle());
            fullFileName = fname;
            setFilename(fd.getFile());
            if (filename.contains(".")) {
                setFilename(filename.substring(0, filename.lastIndexOf(".") - 1));
            }
            changed = false;
        } else changed = fullFileName.isEmpty() || saveAs;
    }

    void keybindDialogClose(KeyEvent lastKeyEvent) {
        inputKeybind.reset();
        Config config = configManager.getConfig();
        if (lastKeyEvent != null) {
            config.keybinds().add(new Keybind(Keybind.BUTTON, currentKeybindID, lastKeyEvent.getExtendedKeyCode(),
                    lastKeyEvent.isShiftDown(), lastKeyEvent.isControlDown(), lastKeyEvent.isAltDown()));
            addTooltips(config.showNumberTooltips(), config.showKeybindTooltips(), config.showHelpTooltips());
            configManager.saveChangesToFile();
        }
    }

    private void initAutosaver() {
        Config config = configManager.getConfig();
        System.out.println("Configuring autosaver");
        autosaver = new AutosaveHandler(frame, config.useAutosave(), config.autoSaveInterval(), config.autoSaveMaxAge(), filename);
    }

    private void askWhichSavingBehavior() {
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
        Config config = configManager.getConfig();
        // use old behavior
        if (result == JOptionPane.YES_OPTION) {
            config.setUseNewSave(false);
        } else { // use new behavior
            System.out.println("use new behavior");
            config.setUseNewSave(true);
        }
        configManager.saveChangesToFile();
    }

    private void initOwnUI() {
        System.out.println("Initializing own UI elements");
        initCustomButtons();
        initCustomMenus();
        initRightClickMenu();
        fixMVButtons();
        addContextMenuToLengthsAndAngles();

        addMouseListenerToChildren(frame);

        for (ActionListener actionListener : mapping.get("save").getActionListeners()) { // Save button
            mapping.get("save").removeActionListener(actionListener);
        }
        mapping.get("save").addActionListener(e -> saveBtnNew(e, false));

        exposeMethods.setFrame(frame);
        String title = exposeMethods.getFrameTitle() + " - OrihimeMod version " + OrihimeMod.version;
        exposeMethods.setFrameTitle0(title);
        frame.setTitle(title);
        Config config = configManager.getConfig();
        addTooltips(config.showNumberTooltips(), config.showKeybindTooltips(), config.showHelpTooltips());
        KeyListener listener = new KeybindListener(mapping, config.keybinds());
        frame.addKeyListener(listener);
        addKeyListenerToChildren(listener, frame);
        if (config.useDarkMode()) {
            enableDarkMode(frame);
        }
        if (config.useExpertMode()) {
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
    }

    private void initCustomMenus() {
        exportMenu = new JPopupMenu();
        JMenuItem exportDXF = new JMenuItem("dxf");
        exportDXF.addActionListener(e -> btnSaveAsDXF.doClick());
        JMenuItem exportSVG = new JMenuItem("svg");
        exportSVG.addActionListener(e -> btnSaveAsSVG.doClick());
        JMenuItem exportCP = new JMenuItem("cp");
        exportCP.addActionListener(e -> btnSaveAsCp.doClick());
        JMenuItem exportPng = new JMenuItem("png");
        exportPng.addActionListener(e -> mapping.get("export_image").doClick());
        JMenuItem saveAs = new JMenuItem("Save as");
        saveAs.addActionListener(e -> saveBtnNew(e, true));
        JMenuItem exportORH = new JMenuItem("orh");
        exportORH.addActionListener(e -> {
            for (ActionListener actionListener : oldSaveButton) {
                actionListener.actionPerformed(e);
            }
        });
        JButton btnSaveAs = new JButton();
        btnSaveAs.addActionListener(e -> saveAs.doClick());
        buttons.add(btnSaveAs);

        exportMenu.add(exportCP);
        exportMenu.add(exportSVG);
        exportMenu.add(exportDXF);
        exportMenu.add(exportPng);
        exportMenu.add(exportORH);
        if (configManager.getConfig().useNewSave() || configManager.getConfig().justUpdatedTo0_2_0) {
            exportMenu.add(saveAs);
        }
    }

    private void indexOriginalUI() {
        System.out.println("Indexing Buttons and Checkboxes...");
        indexButtons(frame);
        indexCheckboxes(frame);
        System.out.println("Found " + buttons.size() + " Buttons and " + checkboxes.size() + " checkboxes for keybinds");
    }

    private void loadToolTipFile() {
        System.out.println("Loading Tooltips");
        tooltips = ResourceBundle.getBundle("tooltips");
    }

    private void initButtonMapping() {
        mapping = configManager.getConfig().mapping;
        mapping.setButtons(buttons);
        mapping.setCheckboxes(checkboxes);
    }

    private void initConfig() {
        configManager = new ConfigFileManager("orihimeKeybinds.cfg");
        configManager.load();
        Config config = configManager.getConfig();
        System.out.println("Loaded " + config.keybinds().size() + " Keybinds.");
        System.out.println("Loading Button mapping for Mod version " + version + " and Orihime version " + orihimeVersion);
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
                    } catch (Exception ex) {
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
        oldSaveButton = mapping.get("save").getActionListeners();
        btnSaveAsSVG = new JButtonSaveAsSVG(frame);
        setupButton(btnSaveAsSVG, "Save as SVG", btnSaveAsSVG::saveAsSVG);
        btnSaveAsCp = new JButtonSaveAsCp(frame);
        setupButton(btnSaveAsCp, "Save as CP", btnSaveAsCp::saveAsCp);
        btnSaveAsDXF = new JButtonSaveAsDXF(frame);
        setupButton(btnSaveAsDXF, "Save as DXF", btnSaveAsDXF::saveAsDXF);
        inputKeybind = new JInputKeybindDialog(this::keybindDialogClose);

        frame.tb = new TextButton(frame, "Text Mode");
        buttons.add(frame.tb);
        frame.tb.addActionListener(frame::textButtonClick);

        JButton btnExport = new JButton("Export");
        btnExport.addActionListener(e -> {
            exportMenu.show(btnExport, 0, btnExport.getHeight());
            exposeMethods.readImageFromFile3();
            exposeMethods.setI_mouseDragged_yuukou(0);
            exposeMethods.setI_mouseReleased_yuukou(0);
        });
        btnExport.setMargin(new Insets(0, 0, 0, 0));

        mapping.get("save").getParent().add(btnExport);
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        p.setBackground(Color.PINK);
        p.add(frame.textField);
        p.add(frame.tb);
        mapping.get("change_circle_color").getParent().getParent().setLayout(new GridLayout(29, 1));
        mapping.get("change_circle_color").getParent().getParent().add(p);
        buttons.add(btnExport);
    }

    private void setupButton(JButton button, String text, ActionListener action) {
        button.setText(text);
        button.setMargin(new Insets(0, 0, 0, 0));
        buttons.add(button);
        button.addActionListener(action);
    }

    private void initOrihimeFrame() {
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
        exposeMethods = new Expose(frame);
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
            keybindId = mapping.getKey(bOrC + "." + index);
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
                for (Keybind keybind : configManager.getConfig().keybinds()) {
                    if (keybind.getConfigID().equals(currentKeybindID)) {
                        JMenuItem rk = new JMenuItem(keybind.toString());
                        removeKeybind.add(rk);
                        rk.addActionListener(ev -> {
                            Config config = configManager.getConfig();
                            config.keybinds().remove(keybind);
                            configManager.saveChangesToFile();
                            config = configManager.getConfig();
                            addTooltips(config.showNumberTooltips(), config.showKeybindTooltips(), config.showHelpTooltips());
                        });
                    }
                }
                rightClickMenu.setInvoker(b);
                rightClickMenu.setLocation(e.getLocationOnScreen());
                rightClickMenu.setVisible(true);
            }
        }
    }
}
