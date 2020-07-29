package de.undertrox.orihimemod.config;

import de.undertrox.orihimemod.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParsedConfigFile {
    List<ParsedConfigLine> lines = new ArrayList<>();

    private ParsedConfigFile(String fileContent) {
        for (String line : fileContent.split("\n")) {
            lines.add(ParsedConfigLine.parse(line));
        }
    }

    public static ParsedConfigFile fromFile(String fileName) {
        try {
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            StringBuilder builder = new StringBuilder();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                builder.append(data);
            }
            myReader.close();
            return new ParsedConfigFile(builder.toString());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return new ParsedConfigFile("");
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ParsedConfigLine line : lines) {
            builder.append(line.toString());
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
            } else {
                System.out.println("Config File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(toString());
            myWriter.close();
            System.out.println("Successfully updated the config file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public boolean contains(String key) {
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
                pairs.add(new Pair(line.getKey(), line.getValue()));
            }
        }
        return pairs;
    }
}
