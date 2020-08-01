package de.undertrox.orihimemod.test;

import jp.gr.java_conf.mt777.origami.orihime.Expose;
import jp.gr.java_conf.mt777.origami.orihime.ap;

import javax.swing.*;
import java.awt.*;

public class OrihimeWindow extends JFrame {
    public static JPanel canvas;
    ap frame;

    public OrihimeWindow(ap parentFrame) {
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
