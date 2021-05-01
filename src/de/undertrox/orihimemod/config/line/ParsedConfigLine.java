package de.undertrox.orihimemod.config.line;

public abstract class ParsedConfigLine {

    protected String value;
    protected String key;

    public ParsedConfigLine(String line) {
    }

    public String getValue() {
        return value.toLowerCase();
    }

    public String getKey() {
        return key.toLowerCase();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public abstract String toString();
}
