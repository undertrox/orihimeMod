package de.undertrox.orihimemod.keybind;

import java.awt.event.KeyEvent;

public class Keybind {
    private int buttonNumber;
    private int keyCode;
    private boolean shift;
    private boolean ctrl;
    private boolean alt;

    public Keybind(int buttonNumber, int keyCode, boolean shift, boolean ctrl, boolean alt) {
        this.buttonNumber = buttonNumber;
        this.keyCode = keyCode;
        this.shift = shift;
        this.ctrl = ctrl;
        this.alt = alt;
    }

    public Keybind(int buttonNumber, int keyCode) {
        this(buttonNumber, keyCode, false, false, false);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Keybind(");
        if (ctrl) b.append("ctrl+");
        if(alt) b.append("alt+");
        if(shift) b.append("shift+");
        b.append(keyCode);
        b.append(")");
        return b.toString();
    }

    public Keybind(int buttonNumber, char key, boolean shift, boolean ctrl, boolean alt){
        this(buttonNumber, KeyEvent.getExtendedKeyCodeForChar(key), shift, ctrl, alt);
    }

    public Keybind(int buttonNumber, char key) {
        this(buttonNumber, key, false, false, false);
    }

    public int getButtonNumber() {
        return buttonNumber;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean hasShift() {
        return shift;
    }

    public boolean hasCtrl() {
        return ctrl;
    }

    public boolean hasAlt() {
        return alt;
    }

    /**
     *
     * @param event KeyEvent to test
     * @return true, if the event matches the parameters of the keybind
     */
    public boolean matches(KeyEvent event) {
        return this.getKeyCode() == event.getExtendedKeyCode() && modifiersMatch(event.getModifiersEx());
    }

    /**
     * returns true if the modifier mask in the argument matches the modifiers of this keybind
     */
    public boolean modifiersMatch(int modifiers) {
        int onmask = 0;
        int offmask = 0;
        if (hasAlt()) {
            onmask |= KeyEvent.ALT_DOWN_MASK;
        } else {
            offmask |= KeyEvent.ALT_DOWN_MASK;
        }
        if (hasShift()) {
            onmask |= KeyEvent.SHIFT_DOWN_MASK;
        } else {
            offmask |= KeyEvent.SHIFT_DOWN_MASK;
        }
        if (hasCtrl()) {
            onmask |= KeyEvent.CTRL_DOWN_MASK;
        } else {
            offmask |= KeyEvent.CTRL_DOWN_MASK;
        }
        return (modifiers & (onmask|offmask)) == onmask;
    }

}
