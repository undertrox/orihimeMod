package de.undertrox.orihimemod.newui;

import de.undertrox.orihimemod.OrihimeModWindow;
import de.undertrox.orihimemod.mapping.ButtonMapping;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;

import javax.swing.*;
import java.util.ResourceBundle;

public class NewUI {
    private final OrihimeFrame frame;
    private ResourceBundle toolTips;

    public NewUI(OrihimeFrame frame, ResourceBundle toolTips) {
        this.frame = frame;

        this.toolTips = toolTips;
    }

    public void close() {
        frame.dispose();
    }

    public void init(ButtonMapping mapping, OrihimeModWindow modWindow) {
        OrihimeWindow window = new OrihimeWindow(frame, mapping, toolTips, modWindow);
        window.setSize(1200, 700);
        window.setLocationRelativeTo(null);
        System.out.println("Starting");
        window.setVisible(true);
        frame.setVisible(false);
    }
}
