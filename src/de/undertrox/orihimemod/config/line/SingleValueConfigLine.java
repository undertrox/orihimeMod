package de.undertrox.orihimemod.config.line;

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
