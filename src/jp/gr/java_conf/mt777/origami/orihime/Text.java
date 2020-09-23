package jp.gr.java_conf.mt777.origami.orihime;

import jp.gr.java_conf.mt777.origami.dougu.camera.Camera;
import jp.gr.java_conf.mt777.zukei2d.ten.Ten;

import java.awt.*;
import java.awt.geom.Point2D;

public class Text {
    Ten pos;
    Camera c;
    int size;
    String text;

    public Text(Ten pos, int size, String text) {
        this.pos = pos;
        this.size = size;
        this.text = text;
    }

    public void setCamera(Camera c) {
        this.c = c;
    }

    public Ten getPos() {
        return pos;
    }

    public void setPos(Ten pos) {
        this.pos = pos;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void paint(Graphics2D g) {

        Ten t = new Ten(getPos().getx(), getPos().gety());
        Ten transformed = c.object2TV(t);
        g.setFont(new Font("sans-serif",Font.PLAIN, size));
        g.setColor(Color.BLACK);
        g.drawString(text, (int) transformed.getx(), (int) transformed.gety());
    }
}
