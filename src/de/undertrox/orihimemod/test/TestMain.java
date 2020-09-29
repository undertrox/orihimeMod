package de.undertrox.orihimemod.test;

import de.undertrox.orihimemod.newui.OrihimeWindow;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;
import jp.gr.java_conf.mt777.origami.orihime.ap;

public class TestMain {
    private static ap frame;
    private static OrihimeWindow window;

    public static void main(String[] args) {
        frame = new OrihimeFrame();
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        window = new OrihimeWindow(frame, null);
        window.setSize(1200, 700);
        window.setLocationRelativeTo(null);
        System.out.println("Starting");
        window.setVisible(true);
    }
}
