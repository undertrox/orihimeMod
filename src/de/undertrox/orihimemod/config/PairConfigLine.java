package de.undertrox.orihimemod.config;

public class PairConfigLine extends ParsedConfigLine {
    public PairConfigLine(String line) {
        super(line);
        int equalsIndex = line.indexOf("=");
        key = line.substring(0, equalsIndex);
        value = line.substring(equalsIndex + 1);
    }

    @Override
    public String toString() {
        return getKey() + "=" + getValue();
    }
}
