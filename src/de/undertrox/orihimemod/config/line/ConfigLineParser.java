package de.undertrox.orihimemod.config.line;

public class ConfigLineParser {
    public static ParsedConfigLine parse(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            return new EmptyConfigLine();
        }
        if (line.startsWith("#")) {
            return new CommentConfigLine(line);
        }
        if (line.contains("=")) {
            return new PairConfigLine(line);
        }
        return new SingleValueConfigLine(line);

    }
}
