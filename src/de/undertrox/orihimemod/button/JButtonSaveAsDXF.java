package de.undertrox.orihimemod.button;

import de.undertrox.orihimemod.ExportDXF;
import jp.gr.java_conf.mt777.origami.orihime.Expose;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;
import jp.gr.java_conf.mt777.kiroku.Text;
import jp.gr.java_conf.mt777.origami.orihime.ap;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JButtonSaveAsDXF extends JButton {
    OrihimeFrame frame;

    public JButtonSaveAsDXF(OrihimeFrame frame) {
        this.frame = frame;
    }

    public void saveAsDXF(ActionEvent e) {
        Expose expose = new Expose(frame);
        Egaki_Syokunin es1 = expose.getEs1();
        expose.setExplanationFileName("qqq/kaki.png");
        expose.readImageFromFile3();
        expose.Button_kyoutuu_sagyou();
        expose.setI_mouseDragged_yuukou(0);
        expose.setI_mouseReleased_yuukou(1);
        es1.kiroku();
        FileDialog fd = new FileDialog(frame);
        fd.setTitle("Save file as .dxf");
        fd.setMode(FileDialog.SAVE);
        fd.setVisible(true);
        String fname = fd.getDirectory() + fd.getFile();
        Text memo1;
        memo1 = es1.getMemo_for_kakidasi();
        if (!fname.endsWith(".dxf")) {
            fname = fname + ".dxf";
        }
        expose.memoAndName2File(ExportDXF.cpToDxf(expose.orihime2cp(memo1)), fname);
        Text m = new Text();
        m.addLine(frame.textRenderer.serialize());
        expose.memoAndName2File(m, fname+"text");
        if (fd.getFile()!= null) {
            expose.setFrameTitle(expose.getFrameTitle0() + "        " + fd.getFile());
            frame.setTitle(expose.getFrameTitle());
            es1.set_title(expose.getFrameTitle());
        }
    }
}
