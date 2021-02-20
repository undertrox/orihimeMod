package de.undertrox.orihimemod;

import jp.gr.java_conf.mt777.kiroku.Text;
import java.util.StringTokenizer;

public class ExportDXF {
    public static Text cpToDxf(Text cp) {
        double scale = 3.0;
        double center = 4.0;
        double x1, y1, x2, y2;
        String str;
        int lineType;

        Text dxf = new Text();
        dxf.addLine("  0");
        dxf.addLine("SECTION");
        dxf.addLine("  2");
        dxf.addLine("HEADER");
        dxf.addLine("  9");
        dxf.addLine("$ACADVER");
        dxf.addLine("  1");
        dxf.addLine("AC1009");
        dxf.addLine("  0");
        dxf.addLine("ENDSEC");
        dxf.addLine("  0");
        dxf.addLine("SECTION");
        dxf.addLine("  2");
        dxf.addLine("ENTITIES");


        for (int lineNum = 1; lineNum <= cp.getLineNum() ; lineNum++) {
            if (cp.getLine(lineNum).length()>0) {
                StringTokenizer tk = new StringTokenizer(cp.getLine(lineNum));
                str = tk.nextToken();
                try {
                    dxf.addLine("  0");
                    dxf.addLine("LINE");
                    dxf.addLine("  8");
                    lineType = Integer.parseInt(str);
                    String layerName = "noname";
                    int colorNumber = 0;
                    switch(lineType) {
                        case 1:
                            layerName = "CutLine";
                            colorNumber = 250; // gray
                            break;
                        case 2:
                            layerName = "MountainLine";
                            colorNumber = 1; // red
                            break;
                        case 3:
                            layerName = "ValleyLine";
                            colorNumber = 5; // blue
                            break;
                    }
                    x1 = Double.parseDouble(tk.nextToken());
                    y1 = Double.parseDouble(tk.nextToken());
                    x2 = Double.parseDouble(tk.nextToken());
                    y2 = Double.parseDouble(tk.nextToken());

                    dxf.addLine(layerName);
                    dxf.addLine("  6");
                    dxf.addLine("CONTINUOUS");
                    dxf.addLine("  62");
                    dxf.addLine("" + colorNumber);

                    dxf.addLine("  10");
                    dxf.addLine("" + scale(x1,scale,center));
                    dxf.addLine("  20");
                    dxf.addLine("" + scale(y1,-scale,center));

                    dxf.addLine("  11");
                    dxf.addLine("" + scale(x2,scale,center));
                    dxf.addLine("  21");
                    dxf.addLine("" + scale(y2,-scale,center));

                } catch (NumberFormatException e) {
                    System.err.println("Warning: CP to DXF conversion failed on line "+ lineNum + ". this shouldnt happen.");
                }
            }
        }

        dxf.addLine("  0");
        dxf.addLine("ENDSEC");
        dxf.addLine("  0");
        dxf.addLine("EOF");
        return dxf;
    }

    public static Text cpToSvg(Text cp) {
        Text svg = new Text();
        svg.addLine("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"\n" +
                "\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
                " xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\"\n" +
                " width=\"1000px\" height=\"1000px\"\n" +
                " viewBox=\"0 0 1000 1000\" >");
        double scale = 1000/400.0;
        double center = 200*scale;
        for (int lineNum = 1; lineNum <= cp.getLineNum() ; lineNum++) {
            if (cp.getLine(lineNum).length()>0) {
                StringTokenizer tk = new StringTokenizer(cp.getLine(lineNum));
                String str = tk.nextToken();
                try {
                    int lineType = Integer.parseInt(str);
                    String style;
                    switch (lineType) {
                        case 1 :
                            style = "stroke:black;stroke-width:4;";
                            break;
                        case 2 :
                            style =  "stroke:red;stroke-width:2;";
                            break;
                        case 3 :
                            style =  "stroke:blue;stroke-width:2;";
                            break;
                        default :
                            style =  "stroke:gray;stroke-width:2;";
                            break;
                    }
                    double x1 = Double.parseDouble(tk.nextToken());
                    double y1 = Double.parseDouble(tk.nextToken());
                    double x2 = Double.parseDouble(tk.nextToken());
                    double y2 = Double.parseDouble(tk.nextToken());

                    svg.addLine(String.format("<line style=\"%s\" x1=\"%s\" y1=\"%s\" x2=\"%s\" y2=\"%s\"/>", style,
                            scale(x1, scale, center), scale(y1, scale, center), scale(x2, scale, center), scale(y2, scale, center)));
                } catch (NumberFormatException e) {
                    System.err.println("Warning: CP to DXF conversion failed on line "+ lineNum + ". this shouldnt happen.");
                }
            }
        }
        svg.addLine("</svg>");
        return svg;
    }

    private static double scale(double d, double scale, double center) {
        return d*scale+center;
    }
}
