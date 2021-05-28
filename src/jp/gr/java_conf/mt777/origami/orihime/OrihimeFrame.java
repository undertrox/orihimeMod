package jp.gr.java_conf.mt777.origami.orihime;

import de.undertrox.orihimemod.button.TextButton;
import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import jp.gr.java_conf.mt777.origami.dougu.keijiban.TextRenderer;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.ExposeES;
import jp.gr.java_conf.mt777.zukei2d.ten.Ten;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    public int getI_OAZ() {
        return i_OAZ;
    }

    public void open(String path) {
        i_mouseDragged_yuukou=0; i_mouseReleased_yuukou=0;
        Memo memo_temp;

        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Could not find " + path);
            return;
        }

        memo_temp = getMemoFromFile(file.getParent(), file.getName());

        if(memo_temp.getGyousuu()>0){
            tenkaizu_syokika();
            es1.reset();
            es1.set_i_kitei_jyoutai(0);

            icol=1;	es1.setcolor(icol);
            ButtonCol_irokesi();ButtonCol_red.setForeground(Color.black);ButtonCol_red.setBackground(Color.red);
            OZ =temp_OZ;
            OAZ.clear();OAZ_add_new_Oriagari_Zu(); set_i_OAZ(0);
            settei_syokika_yosoku();

            Button_F_color.setBackground(OZ.oriagarizu_F_color);
            Button_B_color.setBackground(OZ.oriagarizu_B_color);
            Button_L_color.setBackground(OZ.oriagarizu_L_color);
            es1.setCamera(camera_of_orisen_nyuuryokuzu);
            es1.setMemo_for_yomikomi(memo_temp);es1.kiroku();

            d_syukusyaku_keisuu=camera_of_orisen_nyuuryokuzu.get_camera_bairitsu_x();
            text27.setText(String.valueOf(d_syukusyaku_keisuu));
            text27.setCaretPosition(0);

            d_kaiten_hosei=camera_of_orisen_nyuuryokuzu.get_camera_kakudo();
            text28.setText(String.valueOf(d_kaiten_hosei));
            text28.setCaretPosition(0);

        }
    }

    @Override
    public void Frame_tuika() {
        if (add_frame != null) {
            add_frame.setVisible(true);
        }
        if(i_add_frame==1){System.out.println("111 i_add_frame="+i_add_frame);
            //add_frame.dispose();
            //add_frame = new OpenFrame("add_frame",this);
        }

        if(i_add_frame==0){System.out.println("000 i_add_frame="+i_add_frame);
            add_frame = new OpenFrame("ad_fnc", this);
            //add_frame.setVisible(false);
            add_frame.removeWindowListener(add_frame.getWindowListeners()[0]);
            add_frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    e.getWindow().setVisible(false);
                }
            });
        }
        i_add_frame=1;
        add_frame.toFront();
    }

    public Frame adFncFrame() {
        return add_frame;
    }

    public void setI_OAZ(int i_oaz) {
        this.i_OAZ = i_oaz;
    }

    public void setI_OAZToMax() {
        this.i_OAZ = this.OAZ.size()-1;
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

        FileDialog fd = new FileDialog(this,"Open File",FileDialog.LOAD);
        fd.setVisible(true);

        return getMemoFromFile(fd.getDirectory(), fd.getFile());
    }

    private Memo getMemoFromFile(String directory, String file) {
        boolean file_ok = false;
        String fname = (directory == null? "" : directory + "/") + file;
        if(fname.endsWith("orh")){ file_ok=true;}
        if(fname.endsWith("obj")){ file_ok=true;}
        if(fname.endsWith("cp")){ file_ok=true;}
        Memo memo_temp = new Memo();
        if (!file_ok) return memo_temp;
        textRenderer = new TextRenderer(this);

        // Op button
        if (!img_kaisetu_fname.equals("qqq/yomi_tuika.png")) { // Normal Open button
            frame_title=frame_title_0+"        "+ file;
            setTitle(frame_title);es1.set_title(frame_title);
            fileName = file;
            // Remove extension
            if (fileName.lastIndexOf(".") > 0) {
                fileName = fileName.substring(0, fileName.lastIndexOf(".")-1);
            }
            completeFileName = fname;
            notifyChange();
        }


        try {
            if(file!=null) {  //�L�����Z���ł͂Ȃ��ꍇ�B

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(fname), StandardCharsets.UTF_8));
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
