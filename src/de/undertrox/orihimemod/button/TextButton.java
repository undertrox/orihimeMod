package de.undertrox.orihimemod.button;

import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;

import javax.swing.*;
import java.awt.*;

public class TextButton extends JButton {

    private final OrihimeFrame frame;

    public TextButton(OrihimeFrame parent, String text) {
        super(text);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.frame = parent;
    }

}
