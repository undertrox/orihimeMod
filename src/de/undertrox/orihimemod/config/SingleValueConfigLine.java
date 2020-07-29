package de.undertrox.orihimemod.config;

public class SingleValueConfigLine extends ParsedConfigLine {
    public SingleValueConfigLine(String line) {
        super(line);
        value = line;
    }

    @Override
    public String toString() {
        return value;
    }
}
