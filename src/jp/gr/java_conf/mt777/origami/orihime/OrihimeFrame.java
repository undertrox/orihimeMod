package jp.gr.java_conf.mt777.origami.orihime;

import de.undertrox.orihimemod.button.TextButton;
import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import jp.gr.java_conf.mt777.origami.dougu.keijiban.TextRenderer;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.ExposeES;
import jp.gr.java_conf.mt777.zukei2d.ten.Ten;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OrihimeFrame extends ap {
    public String completeFileName;
    public String fileName;
    public TextButton tb;
    public List<Observer> observers = new ArrayList<>();
    public TextRenderer textRenderer;
    public JTextField textField = new JTextField();

    public OrihimeFrame() {
        this.textRenderer = new TextRenderer(this);
        this.keijiban = textRenderer;
    }

    public void setShowHelp(boolean showHelp) {
        ikaisetuhyouji = showHelp? 1 : 0;
    }

    public void setHelpImage(String path) {
        this.img_kaisetu_fname = path;
        readImageFromFile3();
    }

    public boolean getShowHelp() {
        return ikaisetuhyouji >= 1;
    }

    public void readImageFromFile3() {
        super.readImageFromFile3();
    }

    public Egaki_Syokunin getEs1() {
        return this.es1;
    }

    public interface Observer {
        void update(String newCompleteFileName, String newFileName);
    }

    void notifyChange() {
        observers.forEach(o -> o.update(completeFileName, fileName));
    }

    @Override
    public Memo readFile2Memo() {
        String fname;
        Memo memo_temp = new Memo();

        boolean file_ok=false;

        FileDialog fd = new FileDialog(this,"Open File",FileDialog.LOAD);
        fd.setVisible(true);
        fname = fd.getDirectory() + fd.getFile();

        if(fname.endsWith("orh")){ file_ok=true;}
        if(fname.endsWith("obj")){ file_ok=true;}
        if(fname.endsWith("cp")){ file_ok=true;}

        textRenderer = new TextRenderer(this);
        if( !file_ok){
            return memo_temp;
        }

        // Op button
        if (!img_kaisetu_fname.equals("qqq/yomi_tuika.png")) { // Normal Open button
            frame_title=frame_title_0+"        "+fd.getFile();
            setTitle(frame_title);es1.set_title(frame_title);
            fileName = fd.getFile();
            // Remove extension
            if (fileName.lastIndexOf(".") > 0) {
                fileName = fileName.substring(0, fileName.lastIndexOf(".")-1);
            }
            completeFileName = fname;
            notifyChange();
        }


        try {
            if(fd.getFile()!=null) {  //�L�����Z���ł͂Ȃ��ꍇ�B
                BufferedReader br = new BufferedReader(new FileReader(fname));
                String rdata;
                memo1.reset();
                while((rdata = br.readLine()) != null) {
                    memo_temp.addGyou(rdata);
                }
                br.close();
                // Read text
                String textFile = fname + "text";
                String buffer;
                StringBuilder content = new StringBuilder();
                BufferedReader tbr = new BufferedReader(new InputStreamReader(
                        new FileInputStream(textFile), StandardCharsets.UTF_8));
                while ((buffer = tbr.readLine()) != null) {
                    content.append(buffer).append("\n");
                }
                try {
                    textRenderer = TextRenderer.fromString(content.toString(), this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            frame_title=frame_title_0+"        "+"X";
            setTitle(frame_title);es1.set_title(frame_title);


        }
        keijiban = textRenderer;
        if(fname.endsWith("obj")){
            System.out.println("obj�t�@�C���ǂ݂���" );
            return file_henkan.obj2orihime(memo_temp);
        }
        if(fname.endsWith("cp")){
            System.out.println("cp�t�@�C���ǂ݂���" );
            return file_henkan.cp2orihime(memo_temp);
        }
        return memo_temp;
    }

    private MouseEvent transform(MouseEvent e) {
        int btn = e.getButton();
        if (btn == MouseEvent.BUTTON1 && e.isMetaDown()) {
            btn = MouseEvent.BUTTON2;
        }
        return new MouseEvent(
                e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),
                e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), btn);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        e = transform(e);
        if (!(i_mouse_modeA == MOUSE_MODE_ADD_TEXT && e.getButton() == MouseEvent.BUTTON3))  {
            if (i_mouse_modeA == 19) {
                ExposeES.setSelectMode(getEs1(), 0);
                i_sel_mou_mode = 0;
            }
            super.mousePressed(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mouseEvent = transform(mouseEvent);
        if (!(i_mouse_modeA == MOUSE_MODE_ADD_TEXT && mouseEvent.getButton() == MouseEvent.BUTTON3))  {
            if (i_mouse_modeA == 19) {
                ExposeES.setSelectMode(getEs1(), 0);
                i_sel_mou_mode = 0;
            }
            super.mouseClicked(mouseEvent);
        }
    }

    public Ten transformPoint(Point p) {
        return ExposeES.getCamera(es1).TV2object(new Ten(p.getX(), p.getY()));
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        mouseEvent = transform(mouseEvent);
        if (!(i_mouse_modeA == MOUSE_MODE_ADD_TEXT && mouseEvent.getButton() == MouseEvent.BUTTON3))  {
            if (i_mouse_modeA == 19) {
                ExposeES.setSelectMode(getEs1(), 0);
                i_sel_mou_mode = 0;
            }
            super.mouseMoved(mouseEvent);
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseEvent = transform(mouseEvent);
        if (!(i_mouse_modeA == MOUSE_MODE_ADD_TEXT && mouseEvent.getButton() == MouseEvent.BUTTON3))  {
            if (i_mouse_modeA == 19) {
                ExposeES.setSelectMode(getEs1(), 0);
                i_sel_mou_mode = 0;
            }
            super.mouseDragged(mouseEvent);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mouseEvent = transform(mouseEvent);
        boolean hack = false;
        if (i_mouse_modeA == MOUSE_MODE_ADD_TEXT) {
            if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                if (!textField.getText().isEmpty()) {
                    textRenderer.addText(new Text(transformPoint(mouseEvent.getPoint()), 15, textField.getText()));
                }
            } else if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                hack = true;
                textRenderer.removeTextAt(transformPoint(mouseEvent.getPoint()));
            }
            repaint();
        }
        if (!hack) {
            if (i_mouse_modeA == 19) {
                ExposeES.setSelectMode(getEs1(), 0);
                i_sel_mou_mode = 0;
            }
            super.mouseReleased(mouseEvent);
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        super.mouseEntered(transform(mouseEvent));
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        super.mouseExited(transform(mouseEvent));
    }

    public static final int MOUSE_MODE_ADD_TEXT = 1234;

    public void textButtonClick(ActionEvent e) {
        i_mouse_modeA = MOUSE_MODE_ADD_TEXT;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        int amount = Math.abs(mouseWheelEvent.getWheelRotation());
        int wheelMoved = mouseWheelEvent.getWheelRotation() / amount;
        MouseWheelEvent newEvent = new MouseWheelEvent(mouseWheelEvent.getComponent(),
                                                       mouseWheelEvent.getID(),
                                                       mouseWheelEvent.getWhen(),
                                                       mouseWheelEvent.getModifiersEx(),
                                                       mouseWheelEvent.getX(), mouseWheelEvent.getY(),
                                                       mouseWheelEvent.getX(), mouseWheelEvent.getYOnScreen(),
                                                       mouseWheelEvent.getClickCount(),
                                                       mouseWheelEvent.isPopupTrigger(),
                                                       mouseWheelEvent.getScrollType(), mouseWheelEvent.getScrollAmount(), wheelMoved);
        for (int i = 0; i < amount; i++) {
            super.mouseWheelMoved(newEvent);
        }
    }

    @Override
    void memoAndName2File(Memo memo1,String fname) {
        try {
            BufferedWriter writer = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(fname), StandardCharsets.UTF_8));
            for (int i=1;i<=memo1.getGyousuu();i++){
                writer.write( memo1.getGyou(i) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
