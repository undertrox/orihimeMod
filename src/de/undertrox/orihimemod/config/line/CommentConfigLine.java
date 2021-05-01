package de.undertrox.orihimemod.config.line;

public class CommentConfigLine extends ParsedConfigLine {

    public CommentConfigLine(String line) {
        super(line);
        value = line.substring(1);
    }

    @Override
    public String getKey() {
        return "";
    }

    @Override
    public String toString() {
        return "#" + value;
    }
}
