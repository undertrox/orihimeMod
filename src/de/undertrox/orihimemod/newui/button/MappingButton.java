package de.undertrox.orihimemod.newui.button;

import de.undertrox.orihimemod.mapping.ButtonMapping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MappingButton extends JButton {

    String mappingID;
    ButtonMapping mapping;
    AbstractButton button;

    public ActionListener getListener() {
        return listener;
    }

    public AbstractButton getButton() {
        return button;
    }

    private ActionListener listener;

    public MappingButton(String mappingID, ButtonMapping mapping, boolean hasIcon) {
        super("");
        this.mapping = mapping;
        this.mappingID = mappingID;
        this.button = mapping.get(mappingID);
        this.listener = e -> mappingClick();

        this.addActionListener(listener);

        if (hasIcon) {
            Icon icon = new ImageIcon(MappingButton.class.getClassLoader().getResource("icon/" + mappingID + ".png"));
            setIcon(icon);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }
        setMargin(new Insets(2, 2, 2, 2));
    }

    public void mappingClick() {
        button.doClick();
    }

}
