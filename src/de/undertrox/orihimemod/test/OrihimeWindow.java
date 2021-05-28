package de.undertrox.orihimemod.test;

import jp.gr.java_conf.mt777.origami.orihime.Expose;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;

import javax.swing.*;
import java.awt.*;

public class OrihimeWindow extends JFrame {
    public static JPanel canvas;
    OrihimeFrame frame;

    public OrihimeWindow(OrihimeFrame parentFrame) {
        super();
        frame = parentFrame;
        canvas = new JPanel();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        System.out.println("Painting");
        Expose expose = new Expose(frame);
        expose.draw(g);
    }
}
