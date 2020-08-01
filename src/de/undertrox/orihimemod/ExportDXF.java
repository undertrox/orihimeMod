package de.undertrox.orihimemod;

import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import java.util.StringTokenizer;

public class ExportDXF {
    public static Memo cpToDxf(Memo cp) {
        double scale = 3.0;
        double center = 4.0;
        double x1, y1, x2, y2;
        String str;
        int lineType;

        Memo dxf = new Memo();
        dxf.addGyou("  0");
        dxf.addGyou("SECTION");
        dxf.addGyou("  2");
        dxf.addGyou("HEADER");
        dxf.addGyou("  9");
        dxf.addGyou("$ACADVER");
        dxf.addGyou("  1");
        dxf.addGyou("AC1009");
        dxf.addGyou("  0");
        dxf.addGyou("ENDSEC");
        dxf.addGyou("  0");
        dxf.addGyou("SECTION");
        dxf.addGyou("  2");
        dxf.addGyou("ENTITIES");


        for (int lineNum = 1; lineNum <= cp.getGyousuu() ; lineNum++) {
            if (cp.getGyou(lineNum).length()>0) {
                StringTokenizer tk = new StringTokenizer(cp.getGyou(lineNum));
                str = tk.nextToken();
                try {
                    dxf.addGyou("  0");
                    dxf.addGyou("LINE");
                    dxf.addGyou("  8");
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

                    dxf.addGyou(layerName);
                    dxf.addGyou("  6");
                    dxf.addGyou("CONTINUOUS");
                    dxf.addGyou("  62");
                    dxf.addGyou("" + colorNumber);

                    dxf.addGyou("  10");
                    dxf.addGyou("" + scale(x1,scale,center));
                    dxf.addGyou("  20");
                    dxf.addGyou("" + scale(y1,-scale,center));

                    dxf.addGyou("  11");
                    dxf.addGyou("" + scale(x2,scale,center));
                    dxf.addGyou("  21");
                    dxf.addGyou("" + scale(y2,-scale,center));

                } catch (NumberFormatException e) {
                    System.err.println("Warning: CP to DXF conversion failed on line "+ lineNum + ". this shouldnt happen.");
                }
            }
        }

        dxf.addGyou("  0");
        dxf.addGyou("ENDSEC");
        dxf.addGyou("  0");
        dxf.addGyou("EOF");
        return dxf;
    }

    public static Memo cpToSvg(Memo cp) {
        Memo svg = new Memo();
        svg.addGyou("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"\n" +
                "\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
                " xmlns:xlink=\"http://www.w3.org/1999/xlink\" xml:space=\"preserve\"\n" +
                " width=\"1000px\" height=\"1000px\"\n" +
                " viewBox=\"0 0 1000 1000\" >");
        double scale = 1000/400.0;
        double center = 200*scale;
        for (int lineNum = 1; lineNum <= cp.getGyousuu() ; lineNum++) {
            if (cp.getGyou(lineNum).length()>0) {
                StringTokenizer tk = new StringTokenizer(cp.getGyou(lineNum));
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

                    svg.addGyou(String.format("<line style=\"%s\" x1=\"%s\" y1=\"%s\" x2=\"%s\" y2=\"%s\"/>", style,
                            scale(x1, scale, center), scale(y1, scale, center), scale(x2, scale, center), scale(y2, scale, center)));
                } catch (NumberFormatException e) {
                    System.err.println("Warning: CP to DXF conversion failed on line "+ lineNum + ". this shouldnt happen.");
                }
            }
        }
        svg.addGyou("</svg>");
        return svg;
    }

    private static double scale(double d, double scale, double center) {
        return d*scale+center;
    }
}
