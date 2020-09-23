package jp.gr.java_conf.mt777.origami.orihime.egaki_syokunin;

import jp.gr.java_conf.mt777.origami.dougu.camera.Camera;

public class ExposeES {
    public static Camera getCamera(Egaki_Syokunin es) {
        return es.camera;
    }
    public static void setSelectMode(Egaki_Syokunin es, int mode) {
        es.i_select_mode = mode;
    }
}
