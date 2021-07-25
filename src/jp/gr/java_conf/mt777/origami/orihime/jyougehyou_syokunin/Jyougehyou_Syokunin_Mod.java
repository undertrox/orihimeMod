package jp.gr.java_conf.mt777.origami.orihime.jyougehyou_syokunin;

import jp.gr.java_conf.mt777.origami.dougu.camera.Camera;
import jp.gr.java_conf.mt777.origami.dougu.tensyuugou.Tensyuugou;
import jp.gr.java_conf.mt777.origami.orihime.ap;
import jp.gr.java_conf.mt777.origami.orihime.jyougehyou_syokunin.jyougehyou.touka_jyouken.Touka_jyouken;
import jp.gr.java_conf.mt777.origami.orihime.tenkaizu_syokunin.Tenkaizu_Syokunin;
import jp.gr.java_conf.mt777.zukei2d.ten.Ten;

import java.awt.*;

public class Jyougehyou_Syokunin_Mod extends Jyougehyou_Syokunin {
    Touka_jyouken errorPos = null;
    public static boolean showIntersection = true;
    public Jyougehyou_Syokunin_Mod(ap ap0) {
        super(ap0);
    }


    @Override
    public int tuika_suitei() {
        //山?ﾜり谷?ﾜりの?﨣ｩら?Xに決定できる関係を?яｪしていく?B
        int Mid;//3面の比較で中間にくる面
        int flg_c = 1;
        System.out.println("追加??定開始---------------------????????????????????????????????????????????");
        while (flg_c >= 1) {
            flg_c = 0;
            System.out.println("追加??定------------------------");
            //System.out.println("山?ﾜり谷?ﾜりの?﨣ｩら追加??定   " );
            int flg_b = 1;
            while (flg_b >= 1) {
                flg_b = 0;
                for (int iS = 1; iS <= Smensuu; iS++) {
                    int flg_a = 1;
                    while (flg_a >= 1) {
                        flg_a = 0;
                        for (int iM = 1; iM <= s0[iS].getMenidsuu(); iM++) {//3面の比較で中間にくる面
                            int[] ueMenid = new int[s0[iS].getMenidsuu() + 1];//S面に含まれるあるMenの?繧ｪわにあるid番??を記録する?Bこれが20ということは?A
                            int[] sitaMenid = new int[s0[iS].getMenidsuu() + 1];//S面に含まれるあるMenの下がわにあるid番??を記録する?Bこれが20ということは?A
                            int ueMenid_max = 0;
                            int sitaMenid_max = 0;
                            Mid = s0[iS].getMenid(iM);
                            //?lえ方?FあるSmenのある面Midについて?lえる?B
                            //このSmen以外で面Aは面Midの?繿､?A面Bは面Midの下側と決まっていたとする?B
                            //一般に別?XのSmenで?A面Aは面Midの?繿､で?A面Bは面Midの下側だったからと言って?A面Aは面Bの?繿､とは決定できない?B
                            //しかし?Aここがポイントだが?A面A?A面Mid?A面Bがいっしょに含まれているSmenがあるなら?AそのSmenの?繪ｺ関係がわかっていなくても
                            //面Aは面Bの?繿､となる?Bだから?Aこの操??ではあるSmenから得る?﨣ﾍ３つの面がいっしょにあるかということである?B
                            //あるSmen内の?繪ｺ関係は必要ない?B
                            //
                            //ここの操??はあるSmenの?繪ｺ関係を?繪ｺ表から?ﾌ?Wしている?B
                            for (int i = 1; i <= s0[iS].getMenidsuu(); i++) {//Menid[iM]より?繧ﾉある面?B
                                if (iM != i) {
                                    if (jg.get(Mid, s0[iS].getMenid(i)) == 0) {
                                        ueMenid_max = ueMenid_max + 1;
                                        ueMenid[ueMenid_max] = s0[iS].getMenid(i);
                                    }
                                    if (jg.get(Mid, s0[iS].getMenid(i)) == 1) {
                                        sitaMenid_max = sitaMenid_max + 1;
                                        sitaMenid[sitaMenid_max] = s0[iS].getMenid(i);
                                    }
                                }
                            }
                            //  System.out.print("VVVVVVVVVVVueMenid_max:sitaMenid_max = ");System.out.print(ueMenid_max);
                            //  System.out.print(":");System.out.println(sitaMenid_max);
                            //
                            for (int iuM = 1; iuM <= ueMenid_max; iuM++) {//Menid[iM]より?繧ﾉある面?B
                                for (int isM = 1; isM <= sitaMenid_max; isM++) {//Menid[iM]より下にある面?B
                                    //  System.out.print(ueMenid[iuM]);System.out.print("<-??:下->");System.out.println(sitaMenid[isM]);


                                    if (jg.get(ueMenid[iuM], sitaMenid[isM]) == 0) {
                                        errorPos = new Touka_jyouken(sitaMenid[isM], ueMenid[iuM], sitaMenid[isM], ueMenid[iuM]);
                                        return 2;
                                    }//面の?繪ｺ関係の拡張で矛?ｔｭ?ｶ?B
                                    if (jg.get(sitaMenid[isM], ueMenid[iuM]) == 1) {
                                        errorPos = new Touka_jyouken(sitaMenid[isM], ueMenid[iuM], sitaMenid[isM], ueMenid[iuM]);
                                        return 2;
                                    }//面の?繪ｺ関係の拡張で矛?ｔｭ?ｶ?B

                                    if (jg.get(ueMenid[iuM], sitaMenid[isM]) < 0) {
                                        jg.set(ueMenid[iuM], sitaMenid[isM], 1);
                                        flg_a = flg_a + 1;
                                        flg_b = flg_b + 1;
                                        flg_c = flg_c + 1;
                                    }
                                    if (jg.get(sitaMenid[isM], ueMenid[iuM]) < 0) {
                                        jg.set(sitaMenid[isM], ueMenid[iuM], 0);
                                        flg_a = flg_a + 1;
                                        flg_b = flg_b + 1;
                                        flg_c = flg_c + 1;
                                    }
                                    //   System.out.print("AAAAAAAAAAAflg_a:");System.out.println(flg_a);
                                }
                            }
                        }
                    }
                }
                //System.out.print("?яｪされた関係の?? ?? ");System.out.println(flg_b);
            }
            //
            //jg.jg_hozon();//山?ﾜり谷?ﾜりの?﨣ｩら決定される?繪ｺ関係を保存しておく?B
            //jg のreset適?ﾘに?sわれているか確認のこと
            //System.out.println ("３面が関与する突き抜け??から追加??定   " );
            //jg.addTouka_jyouken(im,Mid_min,im,Mid_max)qqqqqqqqqqqq
            //(im,Mid_min,im,Mid_max);
            Touka_jyouken tg = new Touka_jyouken();
            int flg_a = 1;
            while (flg_a >= 1) {
                flg_a = 0;
                for (int i = 1; i <= jg.getTouka_jyoukensuu(); i++) {
                    tg = jg.getTouka_jyouken(i);
                    //if(onaji_Smen_ni_sonzai(tg.geta(),tg.getb(),tg.getd())==1) {
                    if (jg.get(tg.geta(), tg.getb()) == 1) {
                        if (jg.get(tg.geta(), tg.getd()) == 0) {
                            errorPos = tg;
                            return 3;
                        }
                        if (jg.get(tg.getd(), tg.geta()) == 1) {
                            errorPos = tg;
                            return 3;
                        }
                        if (jg.get(tg.geta(), tg.getd()) < 0) {
                            jg.set(tg.geta(), tg.getd(), 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(tg.getd(), tg.geta()) < 0) {
                            jg.set(tg.getd(), tg.geta(), 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    if (jg.get(tg.geta(), tg.getb()) == 0) {
                        if (jg.get(tg.geta(), tg.getd()) == 1) {
                            errorPos = tg;
                            return 3;
                        }
                        if (jg.get(tg.getd(), tg.geta()) == 0) {
                            errorPos = tg;
                            return 3;
                        }
                        if (jg.get(tg.geta(), tg.getd()) < 0) {
                            jg.set(tg.geta(), tg.getd(), 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(tg.getd(), tg.geta()) < 0) {
                            jg.set(tg.getd(), tg.geta(), 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //
                    if (jg.get(tg.geta(), tg.getd()) == 1) {
                        if (jg.get(tg.geta(), tg.getb()) == 0) {
                            errorPos = tg;
                            return 3;
                        }
                        if (jg.get(tg.getb(), tg.geta()) == 1) {
                            errorPos = tg;
                            return 3;
                        }
                        if (jg.get(tg.geta(), tg.getb()) < 0) {
                            jg.set(tg.geta(), tg.getb(), 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(tg.getb(), tg.geta()) < 0) {
                            jg.set(tg.getb(), tg.geta(), 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    if (jg.get(tg.geta(), tg.getd()) == 0) {
                        if (jg.get(tg.geta(), tg.getb()) == 1) {
                            errorPos = tg;
                            return 3;
                        }
                        if (jg.get(tg.getb(), tg.geta()) == 0) {
                            errorPos = tg;
                            return 3;
                        }
                        if (jg.get(tg.geta(), tg.getb()) < 0) {
                            jg.set(tg.geta(), tg.getb(), 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(tg.getb(), tg.geta()) < 0) {
                            jg.set(tg.getb(), tg.geta(), 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    // }
                }
                //System.out.print("?яｪされた関係の?狽ﾍ ?? ");System.out.println(flg_a);
            }
            //----------------
//	System.out.println ("４面が関与する突き抜け??から追加??定   " );
            //jg.addTouka_jyouken(im,Mid_min,im,Mid_max)qqqqqqqqqqqq
            //(im,Mid_min,im,Mid_max);
            //Touka_jyouken tg = new Touka_jyouken();
            flg_a = 1;
            while (flg_a >= 1) {
                flg_a = 0;
                for (int i = 1; i <= jg.get_uTouka_jyoukensuu(); i++) {
                    tg = jg.get_uTouka_jyouken(i);
                    int a, b, c, d;
                    a = tg.geta();
                    b = tg.getb();
                    c = tg.getc();
                    d = tg.getd();
                    //?@a>b>c?@だけならdの位置は決まらない
                    //?@a>c && b>d なら a>d && b>c
                    //  a>d && b>c なら a>c && b>d
                    //?@a<c && b<d なら a<d && b<c
                    //  a<d && b<c なら a<c && b<d
                    //?@a>c>b?@なら?@a>d>b
                    //a>c && b>d なら a>d && b>c
                    if ((jg.get(a, c) == 1) && (jg.get(b, d) == 1)) {
                        if (jg.get(a, d) == 0) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(b, c) == 0) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(a, d) < 0) {
                            jg.set(a, d, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, c) < 0) {
                            jg.set(b, c, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, a) < 0) {
                            jg.set(d, a, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, b) < 0) {
                            jg.set(c, b, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //a>d && b>c なら a>c && b>d
                    if ((jg.get(a, d) == 1) && (jg.get(b, c) == 1)) {
                        if (jg.get(a, c) == 0) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(b, d) == 0) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(a, c) < 0) {
                            jg.set(a, c, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, d) < 0) {
                            jg.set(b, d, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, a) < 0) {
                            jg.set(c, a, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, b) < 0) {
                            jg.set(d, b, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //a<c && b<d なら a<d && b<c
                    if ((jg.get(a, c) == 0) && (jg.get(b, d) == 0)) {
                        if (jg.get(a, d) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(b, c) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(a, d) < 0) {
                            jg.set(a, d, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, c) < 0) {
                            jg.set(b, c, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, a) < 0) {
                            jg.set(d, a, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, b) < 0) {
                            jg.set(c, b, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //a<d && b<c なら a<c && b<d
                    if ((jg.get(a, d) == 0) && (jg.get(b, c) == 0)) {
                        if (jg.get(a, c) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(b, d) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(a, c) < 0) {
                            jg.set(a, c, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, d) < 0) {
                            jg.set(b, d, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, a) < 0) {
                            jg.set(c, a, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, b) < 0) {
                            jg.set(d, b, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //?@a>c>b?@なら?@a>d>b
                    if ((jg.get(a, c) == 1) && (jg.get(c, b) == 1)) {
                        if (jg.get(d, a) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(b, d) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(a, d) < 0) {
                            jg.set(a, d, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, b) < 0) {
                            jg.set(d, b, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, a) < 0) {
                            jg.set(d, a, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, d) < 0) {
                            jg.set(b, d, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //?@a>d>b?@なら?@a>c>b
                    if ((jg.get(a, d) == 1) && (jg.get(d, b) == 1)) {
                        if (jg.get(c, a) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(b, c) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(a, c) < 0) {
                            jg.set(a, c, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, b) < 0) {
                            jg.set(c, b, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, a) < 0) {
                            jg.set(c, a, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, c) < 0) {
                            jg.set(b, c, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //?@b>c>a?@なら?@b>d>a
                    if ((jg.get(b, c) == 1) && (jg.get(c, a) == 1)) {
                        if (jg.get(d, b) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(a, d) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(b, d) < 0) {
                            jg.set(b, d, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, a) < 0) {
                            jg.set(d, a, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, b) < 0) {
                            jg.set(d, b, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(a, d) < 0) {
                            jg.set(a, d, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //?@b>d>a?@なら?@b>c>a
                    if ((jg.get(b, d) == 1) && (jg.get(d, a) == 1)) {
                        if (jg.get(c, b) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(a, c) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(b, c) < 0) {
                            jg.set(b, c, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, a) < 0) {
                            jg.set(c, a, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, b) < 0) {
                            jg.set(c, b, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(a, c) < 0) {
                            jg.set(a, c, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //?@c>a>d?@なら?@c>b>d
                    if ((jg.get(c, a) == 1) && (jg.get(a, d) == 1)) {
                        if (jg.get(b, c) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(d, b) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(c, b) < 0) {
                            jg.set(c, b, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, d) < 0) {
                            jg.set(b, d, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, c) < 0) {
                            jg.set(b, c, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, b) < 0) {
                            jg.set(d, b, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //?@c>b>d?@なら?@c>a>d
                    if ((jg.get(c, b) == 1) && (jg.get(b, d) == 1)) {
                        if (jg.get(a, c) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(d, a) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(c, a) < 0) {
                            jg.set(c, a, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(a, d) < 0) {
                            jg.set(a, d, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(a, c) < 0) {
                            jg.set(a, c, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(d, a) < 0) {
                            jg.set(d, a, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //?@d>a>c?@なら?@d>b>c
                    if ((jg.get(d, a) == 1) && (jg.get(a, c) == 1)) {
                        if (jg.get(b, d) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(c, b) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(d, b) < 0) {
                            jg.set(d, b, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, c) < 0) {
                            jg.set(b, c, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(b, d) < 0) {
                            jg.set(b, d, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, b) < 0) {
                            jg.set(c, b, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    //?@d>b>c?@なら?@d>a>c
                    if ((jg.get(d, b) == 1) && (jg.get(b, c) == 1)) {
                        if (jg.get(a, d) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(c, a) == 1) {
                            errorPos = tg;
                            return 4;
                        }
                        if (jg.get(d, a) < 0) {
                            jg.set(d, a, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(a, c) < 0) {
                            jg.set(a, c, 1);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(a, d) < 0) {
                            jg.set(a, d, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                        if (jg.get(c, a) < 0) {
                            jg.set(c, a, 0);
                            flg_a = flg_a + 1;
                            flg_c = flg_c + 1;
                        }
                    }
                    // }
                }
                //System.out.print("?яｪされた関係の?狽ﾍ ?? ");System.out.println(flg_a);
            }
            //----------------
            System.out.print("?яｪされた関係の?狽ﾌ?㈹v ?? ");
            System.out.println(flg_c);
        }
        System.out.println("追加??定 ?I了------------------------???????魔ｱこまで20150310??????????????????????");
        return 1000;
    }

    @Override
    public void oekaki_toukazu_with_camera(Graphics g, Tenkaizu_Syokunin orite, Tensyuugou otta_Men_zu, Tensyuugou Smen_zu, int i_toukazu_color, int toukazu_toukado) {
        super.oekaki_toukazu_with_camera(g, orite, otta_Men_zu, Smen_zu, i_toukazu_color, toukazu_toukado);
        Graphics2D g2 = (Graphics2D) g;
        if (errorPos != null) {
            if (showIntersection) {
                g2.setColor(new Color(255, 0, 0, 75));
                fillPolygon(g2, errorPos.geta(), otta_Men_zu, camera);
                fillPolygon(g2, errorPos.getb(), otta_Men_zu, camera);
                fillPolygon(g2, errorPos.getc(), otta_Men_zu, camera);
                fillPolygon(g2, errorPos.getd(), otta_Men_zu, camera);


                fillPolygon(g2, errorPos.geta(), orite.get(), orihime_ap.camera_of_orisen_nyuuryokuzu);
                fillPolygon(g2, errorPos.getb(), orite.get(), orihime_ap.camera_of_orisen_nyuuryokuzu);
                fillPolygon(g2, errorPos.getc(), orite.get(), orihime_ap.camera_of_orisen_nyuuryokuzu);
                fillPolygon(g2, errorPos.getd(), orite.get(), orihime_ap.camera_of_orisen_nyuuryokuzu);
            }
        }
    }

    private void fillPolygon(Graphics2D g, int id, Tensyuugou faces, Camera transform) {
        Ten t0 = new Ten();
        Ten t1 = new Ten();

        int[] x = new int[faces.getTenidsuu(id)+1];
        int[] y = new int[faces.getTenidsuu(id)+1];

        for (int i = 1; i <= faces.getTenidsuu(id) - 1; i++) {

            t0.setx(faces.getTenx(faces.getTenid(id, i)));
            t0.sety(faces.getTeny(faces.getTenid(id, i)));
            t1.set(transform.object2TV(t0));
            x[i] = (int)(t1.getx());
            y[i] = (int)(t1.gety());
            //x[i]=gx(Smen_zu.getTenx(Smen_zu.getTenid(im,i)));
            //y[i]=gy(Smen_zu.getTeny(Smen_zu.getTenid(im,i)));
        }

        t0.setx(faces.getTenx(faces.getTenid(id, faces.getTenidsuu(id))));
        t0.sety(faces.getTeny(faces.getTenid(id, faces.getTenidsuu(id))));
        t1.set(transform.object2TV(t0));
        x[0] = (int)(t1.getx());
        y[0] = (int)(t1.gety());
        //x[0]=gx(Smen_zu.getTenx(Smen_zu.getTenid(im,Smen_zu.getTenidsuu(im))));
        //y[0]=gy(Smen_zu.getTeny(Smen_zu.getTenid(im,Smen_zu.getTenidsuu(im))));

        //?ﾜり?繧ｪり?}を描くときのim番目のSmenの多角形の頂点の?ﾀ標?iPC表示???jを??めるのはここまで

        g.fill(new Polygon(x, y, faces.getTenidsuu(id)));
    }
}
