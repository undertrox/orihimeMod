package de.undertrox.orihimemod;

import de.undertrox.orihimemod.button.JButtonSaveAsCp;
import de.undertrox.orihimemod.button.JButtonSaveAsDXF;
import de.undertrox.orihimemod.button.JButtonSaveAsSVG;
import de.undertrox.orihimemod.button.TextButton;
import de.undertrox.orihimemod.config.Config;
import de.undertrox.orihimemod.config.ConfigFileManager;
import de.undertrox.orihimemod.config.DefaultValues;
import de.undertrox.orihimemod.document.FoldConverter;
import de.undertrox.orihimemod.document.Point2d;
import de.undertrox.orihimemod.keybind.JInputKeybindDialog;
import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.keybind.KeybindListener;
import de.undertrox.orihimemod.mapping.ButtonMapping;
import de.undertrox.orihimemod.traverse.Darkmode;
import jp.gr.java_conf.mt777.origami.dougu.keijiban.TextRenderer;
import jp.gr.java_conf.mt777.origami.orihime.Expose;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;
import jp.gr.java_conf.mt777.origami.orihime.ap;
import jp.gr.java_conf.mt777.zukei2d.senbun.Senbun;
import jp.gr.java_conf.mt777.zukei2d.ten.Ten;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;

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

    public JProgressBar progressBar;

    public String filename = "";
    public String fullFileName = "";

    public ConfigFileManager configManager;

    public AutosaveHandler autosaver;

    public ResourceBundle tooltips;

    public OrihimeModWindow() {
        this(null);
    }

    public OrihimeModWindow(String path) {
        System.out.println("OrihimeMod version " + version + " is Starting...");
        JFrame loadingFrame = initLoadingFrame();
        initConfig();
        initButtonMapping();
        loadToolTipFile();
        progressBar.setValue(1);
        progressBar.setString("Initializing original Orihime");
        System.out.println("Starting Orihime...");
        initOrihimeFrame();
        if (path != null) {
            frame.open(path);
        }
        progressBar.setValue(2);
        progressBar.setString("adding own UI elements");
        indexOriginalUI();
        initOwnUI();
        progressBar.setValue(3);
        progressBar.setString("applying defaults");
        applyDefaults();
        progressBar.setValue(4);
        progressBar.setString("Starting autosaver");
        initAutosaver();
        progressBar.setValue(5);
        frame.setVisible(true);
        loadingFrame.setVisible(false);
        loadingFrame.dispose();
        if (configManager.getConfig().justUpdatedTo0_2_0) {
            askWhichSavingBehavior();
        }
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
                btn.setToolTipText(btn.getToolTipText() + "<br>Keybind ID: " +mapping.getKey("button." + i));
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
                if (key != null && tooltips.containsKey(key)) {
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

        FoldConverter.toVerticesCoords(exposeMethods.getEs1());
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
            if (filename.lastIndexOf(".") > 0) {
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

    private JFrame initLoadingFrame() {
        JFrame loadingFrame = new JFrame();
        loadingFrame.getContentPane().setLayout(new BoxLayout(loadingFrame.getContentPane(), BoxLayout.Y_AXIS));
        WeightedRandom<String> loadingScreens = new WeightedRandom<>();
        loadingScreens.addItem("/de/undertrox/orihimemod/config/orihimeloadingscreen.png", 1);
        loadingScreens.addItem("/de/undertrox/orihimemod/config/Orihime_logo.png", 100);
        URL img = getClass().getResource(loadingScreens.getRandomItem());
        Image image = null;
        try {
            image = ImageIO.read(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image finalImage = image;
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.white);
                g.fillRect(0,0,getWidth(),getHeight());
                g.drawImage(finalImage, 0, 0, null);
            }
        };
        loadingFrame.add(panel);
        loadingFrame.setSize(image.getWidth(null), image.getHeight(null) + 30);
        UIManager.put("ProgressBar.selectionForeground", Color.black);
        UIManager.put("ProgressBar.selectionBackground", Color.black);
        progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 5);
        progressBar.setStringPainted(true);
        progressBar.setString("Loading Config file");
        progressBar.setSize(image.getWidth(null), 3);
        progressBar.setBorderPainted(false);
        progressBar.setForeground(Color.getHSBColor(0,0,(255-45)/255f));
        progressBar.setFont(Font.getFont("Arial"));
        progressBar.setBackground(Color.white);
        loadingFrame.add(progressBar);
        loadingFrame.setUndecorated(true);
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.setVisible(true);
        return loadingFrame;
    }

    private void applyDefaults() {
        Config config = configManager.getConfig();
        DefaultValues dv = config.defaultVals;
        frame.setShowHelp(dv.showHelp);
        setTextFieldNextToButtonTo(String.valueOf(dv.undoSteps), mapping.get("set_undo_steps"));
        setTextFieldNextToButtonTo(String.valueOf(dv.foldedModelUndoSteps), mapping.get("set_undo_steps_folded"));
        setTextFieldNextToButtonTo(String.valueOf(dv.auxLineUndoSteps), mapping.get("set_undo_steps_sep_aux_line"));
        setTextFieldNextToButtonTo(String.valueOf(dv.gridSize), mapping.get("set_grid_divisions"));
        setTextFieldNextToButtonTo(String.valueOf(dv.gridDivSize), mapping.get("set_helper_grid_lines_interval"));
        setTextFieldNextToButtonTo(String.valueOf(dv.gridAngle), mapping.get("set_grid_additional_params"));
        setUsingIncDecBtns("inc_line_width", "dec_line_width", 1, dv.lineThickness);
        setUsingIncDecBtns("inc_point_size", "dec_point_size", 1, dv.pointSize);
        setUsingIncDecBtns("inc_sep_aux_line_width", "dec_sep_aux_line_width", 1, dv.auxLineThickness);
        mapping.get("grid_assist").setSelected(dv.gridAssist);
        System.out.println("gm " + dv.gridMode.getId());
        frame.getEs1().set_i_kitei_jyoutai(dv.gridMode.getId());
        frame.OZ.js.set_i_anti_alias(dv.foldedModelAntiAliasing ? 1 : 0);
        AbstractButton btn = mapping.get(dv.defaultTool);
        if (btn != null) {
            btn.doClick();
        } else {
            System.out.println("Warning: default tool '"+ dv.defaultTool + "' could not be found");
        }
        frame.setHelpImage("qqq/a__hajimeni.png");
    }

    private void setTextFieldNextToButtonTo(String val, AbstractButton button) {
        List<Component> components = Arrays.asList(button.getParent().getComponents());
        JTextField textfield = (JTextField) components.get(components.indexOf(button) - 1);
        textfield.setText(val);
        button.doClick();
    }

    private void setUsingIncDecBtns(String mappingIncBtn, String mappingDecBtn,
                                    int defaultVal, int value) {
        AbstractButton incBtn = mapping.get(mappingIncBtn);
        AbstractButton decBtn = mapping.get(mappingDecBtn);
        if (value < defaultVal) {
            for (int i = defaultVal; i > value; i--) {
                decBtn.doClick();
            }
        } else if (value > defaultVal) {
            for (int i = defaultVal; i < value; i++) {
                incBtn.doClick();
            }
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

        if (configManager.getConfig().smartFolding) {
            initSmartFolding();
        }
        fixFoldedModelButtons();
        addMouseListenerToChildren(frame);
        addMouseListenerToChildren(frame.adFncFrame());

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
        KeyListener listener = new KeybindListener(mapping, configManager);
        frame.addKeyListener(listener);
        addKeyListenerToChildren(listener, frame);
        frame.adFncFrame().addKeyListener(listener);
        addKeyListenerToChildren(listener, frame.adFncFrame());
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
                    frame.adFncFrame().dispose();
                    System.exit(0);
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

    private void fixFoldedModelButtons() {
        fixFoldedModelButton("delete_folded_shape");
        fixFoldedModelButton("flip_folded");
        fixFoldedModelButton("calculate_100_folded");
        fixFoldedModelButton("go_to_folded_shape");
        fixFoldedModelButton("modify_wireframe_folded");
        fixFoldedModelButton("modify_folded");
        fixFoldedModelButton("move_folded");
        fixFoldedModelButton("zoom_out_folded");
        fixFoldedModelButton("set_zoom_folded");
        fixFoldedModelButton("zoom_in_folded");
        fixFoldedModelButton("rotate_counterclockwise_folded");
        fixFoldedModelButton("set_rotation_folded");
        fixFoldedModelButton("rotate_clockwise_folded");
        fixFoldedModelButton("antialiasing_folded");
        fixFoldedModelButton("shadow_folded");
        fixFoldedModelButton("set_folded_front_color");
        fixFoldedModelButton("set_folded_back_color");
        fixFoldedModelButton("set_folded_line_color");
    }

    private void fixFoldedModelButton(String mappingName) {
        AbstractButton foldedModelButton = mapping.get(mappingName);
        ActionListener oldListener = foldedModelButton.getActionListeners()[0];
        foldedModelButton.removeActionListener(oldListener);
        foldedModelButton.addActionListener(e -> {
            if (frame.getI_OAZ() == 0) {
                frame.setI_OAZToMax();
            }
            oldListener.actionPerformed(e);
        });
    }

    private void initSmartFolding() {
        AbstractButton foldBtn = mapping.get("fold");
        ActionListener oldListener = foldBtn.getActionListeners()[0];
        foldBtn.removeActionListener(oldListener);
        foldBtn.addActionListener(e -> {
            if (frame.getEs1().ori_s.get_orisensuu_for_select_oritatami() == 0) { // no line is selected
                HashMap<Point2d, Set<Senbun>> pointLineAdjacency = new HashMap<>();
                HashSet<Point2d> points = new HashSet<>();
                for (int i = 1; i <= frame.getEs1().ori_s.getsousuu(); i++) {
                    Senbun s = frame.getEs1().ori_s.get(i);

                    Point2d a = new Point2d(round(s.getax()), round(s.getay()));
                    Point2d b = new Point2d(round(s.getbx()), round(s.getby()));
                    if (!pointLineAdjacency.containsKey(a)) {
                        pointLineAdjacency.put(a, new HashSet<>());
                    }
                    if (!pointLineAdjacency.containsKey(b)) {
                        pointLineAdjacency.put(b, new HashSet<>());
                    }
                    pointLineAdjacency.get(a).add(s);
                    pointLineAdjacency.get(b).add(s);
                    points.add(a);
                    points.add(b);
                }
                Ten cross = frame.camera_of_orisen_nyuuryokuzu.get_camera_ichi();
                Point2d cross2d = new Point2d(cross.getx(), cross.gety());
                HashSet<Senbun> selectedLines = new HashSet<>();
                HashSet<Point2d> traversedPoints = new HashSet<>();
                double minDist = 1000000000;
                Point2d nearest = null;
                for (Point2d point : points) {
                    double dist = cross2d.distanceSquared(point);
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = point;
                    }
                }
                if (nearest != null) {
                    Deque<Senbun> queue = new ArrayDeque<>(pointLineAdjacency.get(nearest));
                    traversedPoints.add(nearest);
                    while (!queue.isEmpty()) {
                        Senbun line = queue.pop();
                        if (!selectedLines.contains(line)) {
                            line.set_i_select(2);
                            selectedLines.add(line);
                            Point2d a = new Point2d(round(line.getax()), round(line.getay()));
                            Point2d b = new Point2d(round(line.getbx()), round(line.getby()));
                            if (!traversedPoints.contains(a)) {
                                queue.addAll(pointLineAdjacency.get(a));
                                traversedPoints.add(a);
                            }
                            if (!traversedPoints.contains(b)) {
                                queue.addAll(pointLineAdjacency.get(b));
                                traversedPoints.add(b);
                            }
                        }
                    }
                }
            }
            oldListener.actionPerformed(e);
        });
    }

    private double round(double value) {
        return Math.round(value);
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
        indexButtons(frame.adFncFrame());
        indexCheckboxes(frame);
        indexCheckboxes(frame.adFncFrame());
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
        frame = new OrihimeFrame(configManager.getConfig());
        frame.setVisible(false);
        frame.setSize(1200, 700);
        if (configManager.getConfig().startMaximized) {
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        }
        frame.setLocationRelativeTo(null);
        frame.Frame_tuika();
        frame.adFncFrame().setVisible(false);
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
                    if (keybind.getConfigID().equalsIgnoreCase(currentKeybindID)) {
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
