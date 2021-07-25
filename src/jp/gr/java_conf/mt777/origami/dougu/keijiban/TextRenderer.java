package jp.gr.java_conf.mt777.origami.dougu.keijiban;

import jp.gr.java_conf.mt777.origami.dougu.camera.Camera;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;
import jp.gr.java_conf.mt777.origami.orihime.Text;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.ExposeES;
import jp.gr.java_conf.mt777.zukei2d.ten.Ten;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TextRenderer extends Keijiban {
    List<Text> textList;
    OrihimeFrame frame;
    Camera c;

    public TextRenderer(OrihimeFrame ap) {
        super(ap);
        frame = ap;
        textList = new ArrayList<>();
        c = ExposeES.getCamera(frame.getEs1());
    }

    public void addText(Text t) {
        t.setCamera(c);
        textList.add(t);
    }

    @Override
    public void keiji(Graphics graphics) {
        super.keiji(graphics);
        Font f = graphics.getFont();
        for (Text text : textList) {
            text.paint((Graphics2D) graphics);
        }
        graphics.setFont(f);
    }

    public void removeTextAt(Ten transform) {
        double minDist = Double.POSITIVE_INFINITY;
        Text nearest = null;
        for (Text text : textList) {
            // Distance
            double d = transform.kyori(text.getPos());
            if (d < minDist) {
                minDist = d;
                nearest = text;
            }
        }
        c = ExposeES.getCamera(frame.getEs1());
        if (nearest != null && minDist < 50/c.get_camera_bairitsu_x()) {
            textList.remove(nearest);
        }
    }

    public String serialize() {
        StringBuilder s = new StringBuilder();

        for (Text text : textList) {
            s.append(text.getPos().getx()).append(" ").append(text.getPos().gety()).append(" ").append(text.getSize())
             .append(" ").append(text.getText());
            s.append("\n");
        }

        return s.toString();
    }

    public static TextRenderer fromString(String s, OrihimeFrame ap) {
        String[] lines = s.split("\n");
        TextRenderer r = new TextRenderer(ap);
        for (String line : lines) {
            try {
                if (line.isEmpty()) continue;
                StringTokenizer t = new StringTokenizer(line, " ");
                double posX = Double.parseDouble(t.nextToken());
                double posY = Double.parseDouble(t.nextToken());
                int size = Integer.parseInt(t.nextToken());
                StringBuilder str = new StringBuilder();
                while (t.hasMoreTokens()) {
                    str.append(t.nextToken());
                }
                r.addText(new Text(new Ten(posX, posY), size, str.toString()));
            } catch (Exception e) {
                System.err.println("Syntax error in Text file");
                e.printStackTrace();
            }
        }
        return r;
    }

    public boolean empty() {
        return this.textList.size() == 0;
    }
}
