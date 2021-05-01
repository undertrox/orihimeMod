package de.undertrox.orihimemod.config.line;


public class EmptyConfigLine extends ParsedConfigLine {

    public EmptyConfigLine() {
        this("");
    }

    private EmptyConfigLine(String line) {
        super(line);
    }

    @Override
    public String getValue() {
        return "";
    }

    @Override
    public String getKey() {
        return "";
    }

    @Override
    public String toString() {
        return "";
    }
}
