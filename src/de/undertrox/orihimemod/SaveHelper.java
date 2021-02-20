package de.undertrox.orihimemod;

import jp.gr.java_conf.mt777.kiroku.Text;
import jp.gr.java_conf.mt777.origami.orihime.Expose;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;
import jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin.Egaki_Syokunin;

public class SaveHelper {
    public static boolean saveTo(OrihimeFrame frame, String filename) {
        Expose expose = new Expose(frame);
        Egaki_Syokunin es1 = expose.getEs1();
        es1.kiroku();
        Text memo1;
        memo1 = es1.getMemo_for_kakidasi();
        boolean success = false;

        if (filename.endsWith(".dxf")) {
            success = expose.memoAndName2File(ExportDXF.cpToDxf(expose.orihime2cp(memo1)), filename);
        } else if (filename.endsWith(".cp")) {
            success = expose.memoAndName2File(expose.orihime2cp(memo1), filename);
        } else if (filename.endsWith(".svg")) {
            success = expose.memoAndName2File(ExportDXF.cpToSvg(expose.orihime2cp(memo1)), filename);
        } else {
            if (!(filename.endsWith(".orh"))) {
                filename += ".orh";
            }
            success = expose.memoAndName2File(memo1, filename);
        }
        Text m = new Text();
        m.addLine(frame.textRenderer.serialize());
        expose.memoAndName2File(m, filename+"text");
        return success;
    }
}
