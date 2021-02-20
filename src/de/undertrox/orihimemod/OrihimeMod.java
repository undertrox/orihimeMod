package de.undertrox.orihimemod;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class OrihimeMod {

    public static final String version = "1.0.0";
    public static final String orihimeVersion = "3.060";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        OrihimeModWindow window = new OrihimeModWindow();
        window.show();
        System.out.println("OrihimeMod is now running");
        System.out.println(MouseEvent.BUTTON2);
    }

}
