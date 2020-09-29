package de.undertrox.orihimemod.newui;

import de.undertrox.orihimemod.mapping.ButtonMapping;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;

import javax.swing.*;

public class NewUI {
    private OrihimeFrame frame;
    private JPanel topPanel;
    private OrihimeWindow window;

    public NewUI(OrihimeFrame frame) {
        this.frame = frame;
    }

    public void init(ButtonMapping mapping) {
        window = new OrihimeWindow(frame, mapping);
        window.setSize(1200, 700);
        window.setLocationRelativeTo(null);
        System.out.println("Starting");
        window.setVisible(true);
        frame.setVisible(false);
    }

    private void initTopPanel() {
    }
}
