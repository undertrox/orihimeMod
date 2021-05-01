package de.undertrox.orihimemod.config;

import de.undertrox.orihimemod.Pair;
import de.undertrox.orihimemod.config.line.ConfigLineParser;
import de.undertrox.orihimemod.config.line.PairConfigLine;
import de.undertrox.orihimemod.config.line.ParsedConfigLine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the config file in an easily accessible way while preserving comments
 * and whitespace, so the config file can be updated programmatically without losing those
 */
public class ParsedConfigFile {
    List<ParsedConfigLine> lines;

    public ParsedConfigFile() {
        this.lines = new ArrayList<>();
    }

    private ParsedConfigFile(List<ParsedConfigLine> lines) {
        this.lines = lines;
    }

    public static ParsedConfigFile fromStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<ParsedConfigLine> lines = new ArrayList<>();
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                lines.add(ConfigLineParser.parse(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ParsedConfigFile(lines);
    }

    public static ParsedConfigFile fromString(String fileContent) {
        List<ParsedConfigLine> lines = Arrays.stream(fileContent.split("\n"))
                .map(ConfigLineParser::parse)
                .collect(Collectors.toList());
        return new ParsedConfigFile(lines);
    }

    public static ParsedConfigFile fromFile(String fileName) {
        try {
            return fromStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ParsedConfigFile();
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ParsedConfigLine line : lines) {
            builder.append(line.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public void append(ParsedConfigFile file) {
        lines.addAll(file.lines);
    }

    public void saveTo(String fileName) {
        try {
            File saveFile = new File(fileName);
            if (saveFile.createNewFile()) {
                System.out.println("Config File created: " + saveFile.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(String key) {
        key = key.toLowerCase();
        for (ParsedConfigLine line : lines) {
            if (line.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public List<Pair<String, String>> getAllPairs() {
        List<Pair<String, String>> pairs = new ArrayList<>();
        for (ParsedConfigLine line : lines) {
            if (line instanceof PairConfigLine) {
                pairs.add(new Pair<>(line.getKey(), line.getValue()));
            }
        }
        return pairs;
    }

    public List<String> getValues(String key) {
        key = key.toLowerCase();
        List<String> result = new ArrayList<>();
        for (ParsedConfigLine line : lines) {
            if (line.getKey().equals(key)) {
                result.add(line.getValue());
            }
        }
        return result;
    }

    public void replaceKey(String oldKey, String newKey) {
        oldKey = oldKey.toLowerCase();
        for (ParsedConfigLine line : lines) {
            if (line.getKey().equals(oldKey)) {
                line.setKey(newKey);
            }
        }
    }

    public void setValue(String key, String value) {
        key = key.toLowerCase();
        boolean found = false;
        for (ParsedConfigLine line : lines) {
            if (line.getKey().equals(key)) {
                line.setValue(value);
                found = true;
            }
        }
        if (!found) {
            addPair(key, value);
        }
    }

    public void addPair(String key, String value) {
        lines.add(new PairConfigLine(key + "=" + value));
    }

    public void removePair(String key, String value) {
        for (Iterator<ParsedConfigLine> iterator = lines.iterator(); iterator.hasNext(); ) {
            ParsedConfigLine line = iterator.next();
            if (line instanceof PairConfigLine) {
                if (line.getKey().equals(key) && line.getValue().equals(value)) {
                    iterator.remove();
                }
            }
        }
    }
}
