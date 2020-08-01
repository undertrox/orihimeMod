package de.undertrox.orihimemod.keybind;

import de.undertrox.orihimemod.OrihimeMod;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class JInputKeybindDialog extends JDialog {
    public JButton btnOk;
    public JButton btnCancel;
    public JLabel keyText;

    public KeyEvent lastKeyEvent;

    public JInputKeybindDialog(Consumer<KeyEvent> onClose) {
        btnOk = new JButton("Save");
        btnCancel = new JButton("Cancel");
        keyText = new JLabel("<Press any Key>");
        keyText.setHorizontalAlignment(SwingConstants.CENTER);
        removeSpaceKeybind(btnCancel);
        removeSpaceKeybind(btnOk);
        this.setLayout(new GridLayout(3,1));
        this.add(keyText);
        this.add(btnOk);
        this.add(btnCancel);
        keyText.grabFocus();
        KeyListener l = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                String text = "";
                if (!e.isActionKey()) {
                    String mods = KeyEvent.getKeyModifiersText(e.getModifiers());
                    String key = KeyEvent.getKeyText(e.getExtendedKeyCode());
                    if (mods.length() > 0) {
                        mods += "+";
                    }
                    if (!mods.contains(key+"+")) {
                        text =  mods + KeyEvent.getKeyText(e.getExtendedKeyCode());
                    } else {
                        text = mods.substring(0,mods.length()-1);
                    }
                    keyText.setText(text);
                    lastKeyEvent = e;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        this.addKeyListener(l);
        keyText.addKeyListener(l);
        btnCancel.addKeyListener(l);
        btnOk.addKeyListener(l);

        btnCancel.addActionListener((e) -> {
            lastKeyEvent = null;
            onClose.accept(null);
            this.setVisible(false);

        });

        btnOk.addActionListener((e -> {
            onClose.accept(lastKeyEvent);
            this.setVisible(false);
        }));
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        keyText.grabFocus();
    }

    public void reset() {
        keyText.setText("<press any key>");
        lastKeyEvent = null;
    }

    private void removeSpaceKeybind(JButton c) {
        c.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,false), "none");
        c.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0,false), "none");
    }
}
