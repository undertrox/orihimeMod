package de.undertrox.orihimemod.newui.button;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeModWindow;
import de.undertrox.orihimemod.keybind.Keybind;
import de.undertrox.orihimemod.mapping.ButtonMapping;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

public class ButtonFactory {
    private ButtonMapping mapping;
    private ResourceBundle tooltips;
    private OrihimeModWindow window;

    public ButtonFactory(ButtonMapping mapping, ResourceBundle tooltips, OrihimeModWindow window) {
        this.mapping = mapping;
        this.tooltips = tooltips;
        this.window = window;
    }

    public JButton createButton(String mappingId, String text, boolean hasIcon) {
        MappingButton btn = new MappingButton(mappingId, mapping, hasIcon);
        btn.setText(text);
        //btn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).clear();
        String tooltip = Config.showNumberTooltips() ? "<br>keybind ID: " + mappingId: "";
        btn.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,false), "none");
        btn.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,false), "none");
        Runnable r = () -> {
            btn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).clear();
            for (Keybind keybind : Config.keybinds()) {
                if (keybind.getType() == Keybind.BUTTON && mapping.get(mappingId) == mapping.getButtons().get(keybind.getComponentID())) {
                    btn.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                            .put(KeyStroke.getKeyStroke(keybind.getKeyCode(),
                                    keybind.getModifiersInt()), "keybind");
                }
            }
        };
        r.run();
        Config.onReload(r);
        btn.getActionMap().put("keybind", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn.mappingClick();
            }
        });
        if (Config.showHelpTooltips()) {
            tooltip = tooltips.getString(mappingId) + tooltip;
        }
        btn.setToolTipText("<html>" + tooltip + "</html>");
        btn.addMouseListener(window.createRightClickListener(btn.getButton(), mapping));
        return btn;
    }

    public JButton createButton(String mappingId, boolean hasIcon) {
        return this.createButton(mappingId, "", hasIcon);
    }

    public JButton createButton(String mappingId) {
        return this.createButton(mappingId, true);
    }
}
