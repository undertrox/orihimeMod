package jp.gr.java_conf.mt777.origami.orihime;

import de.undertrox.orihimemod.config.Config;
import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

// this class is used to expose Package-private Fields and Methods, mainly from the class ap
public class Expose {

    private ap frame;
    private Config config;

    public Expose(OrihimeFrame frame) {
        this.config = frame.config;
        this.frame = frame;
    }

    public void setFrame(ap frame) {
        this.frame = frame;
    }

    public String getExplanationFileName() {
        return frame.img_kaisetu_fname;
    }

    public void setExplanationFileName(String fileName) {
        frame.img_kaisetu_fname = fileName;
    }

    public void Button_kyoutuu_sagyou() {
        frame.Button_kyoutuu_sagyou();
    }

    public void setI_mouseDragged_yuukou(int i) {
        frame.i_mouseDragged_yuukou = i;
    }

    public void setI_mouseReleased_yuukou(int i) {
        frame.i_mouseReleased_yuukou = i;
    }

    public Egaki_Syokunin getEs1() {
        return frame.es1;
    }

    public void readImageFromFile3(){
        frame.readImageFromFile3();
    }

    public boolean memoAndName2File(Memo memo, String fname) {
        if (fname == null){ return true;}
        frame.memoAndName2File(memo, fname);
        Memo memo1 = new Memo();
        try {

                BufferedReader br = new BufferedReader(
                    config.useUtf8?
                        new InputStreamReader(new FileInputStream(fname), StandardCharsets.UTF_8)
                        : new FileReader(fname));

                String rdata;

                memo1.reset();
                while((rdata = br.readLine()) != null) {
                    memo1.addGyou(rdata);
                }
                br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        boolean eq = memo.getGyousuu()==memo1.getGyousuu();
        if (!eq) {
            System.out.println("Error while writing the file.");
            System.out.println(memo.getGyousuu());
            System.out.println(memo1.getGyousuu());
            return eq;
        }
        for (int i = 1; i < memo.getGyousuu(); i++) {
            if (!memo.getGyou(i).equals(memo1.getGyou(i))){
                eq = false;
                System.out.println(memo.getGyou(i));
                System.out.println(memo1.getGyou(i));
                System.out.println(i);
            }
        }
        System.out.println(eq);
        return eq;
    }

    public File_keisiki_henkan fileConversion() {
        return frame.file_henkan;
    }

    public String getFrameTitle0() {
        return frame.frame_title_0;
    }
    public void setFrameTitle0(String title) {
        frame.frame_title_0 = title;
        frame.frame_title = title;
    }

    public void setFrameTitle(String title) {
        frame.frame_title = title;
    }

    public String getFrameTitle() {
        return frame.frame_title;
    }

    public Memo orihime2cp(Memo memo) {
        return frame.file_henkan.orihime2cp(memo);
    }

    public Memo orihime2svg(Memo memo) {
        return frame.file_henkan.orihime2svg(memo);
    }

    public List<?> getOaz() {
        return frame.OAZ;
    }

    public void draw(Graphics g) {
        frame.es1.oekaki_with_camera(g, frame.i_bun_hyouji,frame.i_cp_hyouji,frame.i_a0_hyouji,frame.i_a1_hyouji,
                frame.fTenkaizuSenhaba,frame.i_orisen_hyougen,frame.f_h_TenkaizuSenhaba,frame.dim.width,frame.dim.height,frame.i_mejirusi_hyouji);
    }
}
