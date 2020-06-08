package de.undertrox.orihimemod.button;

import de.undertrox.orihimemod.ExportDXF;
import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import jp.gr.java_conf.mt777.origami.orihime.Expose;
import jp.gr.java_conf.mt777.origami.orihime.ap;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JButtonSaveAsDXF extends JButton {
    ap frame;

    public JButtonSaveAsDXF(ap frame) {
        this.frame = frame;
    }

    public void saveAsDXF(ActionEvent e) {
        Egaki_Syokunin es1 = Expose.getEs1();
        Expose.setExplanationFileName("qqq/kaki.png");
        Expose.readImageFromFile3();
        Expose.Button_kyoutuu_sagyou();
        Expose.setI_mouseDragged_yuukou(0);
        Expose.setI_mouseReleased_yuukou(1);
        es1.kiroku();
        FileDialog fd = new FileDialog(frame);
        fd.setTitle("Save file as .dxf");
        fd.setMode(FileDialog.SAVE);
        fd.setVisible(true);
        String fname = fd.getDirectory() + fd.getFile();
        Memo memo1;
        memo1 = es1.getMemo_for_kakidasi();
        if (!fname.endsWith(".dxf")) {
            fname = fname + ".dxf";
        }
        Expose.memoAndName2File(ExportDXF.cpToDxf(Expose.orihime2cp(memo1)), fname);
        if (fd.getFile()!= null) {
            Expose.setFrameTitle(Expose.getFrameTitle0() + "        " + fd.getFile());
            frame.setTitle(Expose.getFrameTitle());
            es1.set_title(Expose.getFrameTitle());
        }
    }
}
