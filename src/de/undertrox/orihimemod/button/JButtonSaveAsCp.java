package de.undertrox.orihimemod.button;

import de.undertrox.orihimemod.ExportDXF;
import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import jp.gr.java_conf.mt777.origami.orihime.ExposeClasses;
import jp.gr.java_conf.mt777.origami.orihime.ap;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JButtonSaveAsCp extends JButton {
    ap frame;

    public JButtonSaveAsCp(ap frame) {
        this.frame = frame;
    }

    public void saveAsCp(ActionEvent e) {
        Egaki_Syokunin es1 = ExposeClasses.getEs1();
        ExposeClasses.setExplanationFileName("qqq/kaki.png");
        ExposeClasses.readImageFromFile3();
        ExposeClasses.Button_kyoutuu_sagyou();
        ExposeClasses.setI_mouseDragged_yuukou(0);
        ExposeClasses.setI_mouseReleased_yuukou(1);
        es1.kiroku();
        FileDialog fd = new FileDialog(frame);
        fd.setTitle("Save file as .cp");
        fd.setVisible(true);
        String fname = fd.getDirectory() + fd.getFile();
        Memo memo1;
        memo1 = es1.getMemo_for_kakidasi();
        if (fname.endsWith(".dxf")) {
            ExposeClasses.memoAndName2File(ExportDXF.cpToDxf(ExposeClasses.orihime2cp(memo1)), fname);
        } else {
            if (!fname.endsWith(".cp")) {
                fname = fname + ".cp";
            }
            ExposeClasses.memoAndName2File(ExposeClasses.orihime2cp(memo1), fname);
        }
        if (fd.getFile()!= null) {
            ExposeClasses.setFrameTitle(ExposeClasses.getFrameTitle0() + "        " + fd.getFile());
            frame.setTitle(ExposeClasses.getFrameTitle());
            es1.set_title(ExposeClasses.getFrameTitle());
        }
    }
}
