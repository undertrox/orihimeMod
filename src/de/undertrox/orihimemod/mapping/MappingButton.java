package de.undertrox.orihimemod.mapping;

import javax.swing.*;

public class MappingButton extends JButton {

    String mappingID;
    ButtonMapping mapping;
    AbstractButton button;

    public MappingButton(String mappingID, ButtonMapping mapping) {
        super("");
        this.mapping = mapping;
        this.mappingID = mappingID;
        this.button = mapping.get(mappingID);
        this.addActionListener(e -> mappingClick());
    }

    public void mappingClick() {
        button.doClick();
    }

}
