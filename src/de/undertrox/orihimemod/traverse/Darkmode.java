package de.undertrox.orihimemod.traverse;

import javax.swing.*;
import java.awt.*;

public class Darkmode {
    Container container;

    public Darkmode(Container c) {
        container = c;
    }

    public void enable() {
        enableOn(container);
    }

    public void enableOn(Container c) {
        Component[] children = c.getComponents();
        for (Component child : children) {

            if (child instanceof Panel || child instanceof JPanel) {
                child.setBackground(Color.getHSBColor(0, 0, 0.3f));
                child.setForeground(Color.getHSBColor(0, 0, 0.9f));
            } else if (child instanceof JButton) {
                ((JButton) child).setBorder(BorderFactory.createLineBorder(Color.black));
                child.setBackground(Color.getHSBColor(0, 0, 0.2f));
                child.setForeground(Color.getHSBColor(0, 0, 0.9f));
            } else if (child instanceof JTextField) {
                ((JTextField) child).setBorder(BorderFactory.createLineBorder(Color.black));
                child.setBackground(Color.getHSBColor(0, 0, 0.4f));
                child.setForeground(Color.getHSBColor(0, 0, 1f));
            } else if (child instanceof JLabel) {
                ((JLabel) child).setBorder(BorderFactory.createLineBorder(Color.black));
                child.setBackground(Color.getHSBColor(0, 0, 0.3f));
                child.setForeground(Color.getHSBColor(0, 0, 0.9f));
            } else if (child instanceof JCheckBox) {
                JCheckBox checkBox = ((JCheckBox) child);
                checkBox.setBorder(BorderFactory.createLineBorder(Color.black));
                child.setBackground(Color.getHSBColor(0, 0, 0.3f));
                child.setForeground(Color.getHSBColor(0, 0, 0.9f));
            } else {
                System.out.println(child.getClass().getName());
            }
            if (child instanceof Container) {
                enableOn((Container) child);
            }
        }
    }
}
