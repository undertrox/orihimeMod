package de.undertrox.orihimemod.config;

import de.undertrox.orihimemod.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ParsedConfigFile {
    List<ParsedConfigLine> lines = new ArrayList<>();

    public ParsedConfigFile() {
    }

    private ParsedConfigFile(String fileContent) {
        for (String line : fileContent.split("\n")) {
            lines.add(ParsedConfigLine.parse(line));
        }
    }

    public static ParsedConfigFile fromStream(InputStream stream) {
        String newLine = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(flag ? newLine : "").append(line);
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ParsedConfigFile(result.toString());
    }

    public static ParsedConfigFile fromFile(String fileName) {
        try {
            return fromStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ParsedConfigFile("");
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
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("Config File created: " + myObj.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(toString());
            myWriter.close();
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
        for (ParsedConfigLine line : lines) {
            if (line.getKey().equals(key)) {
                line.setValue(value);
            }
        }
    }

    public void addPair(String key, String value) {
        lines.add(new PairConfigLine(key + "=" + value));
    }

    public void removePair(String key, String value) {
        for (Iterator<ParsedConfigLine> iterator = lines.iterator(); iterator.hasNext(); ) {
            ParsedConfigLine line = iterator.next();
            if (line instanceof PairConfigLine) {
                if (line.getKey().equalsIgnoreCase(key) && line.getValue().equalsIgnoreCase(value)) {
                    iterator.remove();
                }
            }
        }
    }
}
