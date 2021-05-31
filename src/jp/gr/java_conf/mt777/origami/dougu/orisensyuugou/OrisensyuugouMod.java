package jp.gr.java_conf.mt777.origami.dougu.orisensyuugou;

import jp.gr.java_conf.mt777.kiroku.memo.Memo;
import jp.gr.java_conf.mt777.zukei2d.en.En;
import jp.gr.java_conf.mt777.zukei2d.senbun.Senbun;
import jp.gr.java_conf.mt777.zukei2d.takakukei.Takakukei;
import jp.gr.java_conf.mt777.zukei2d.ten.Ten;

public class OrisensyuugouMod extends Orisensyuugou{




    public int D_nisuru0(Ten p1, Ten p2, Ten p3, Ten p4){//�ܐ��̂ݍ폜
        Memo m = getMemo();
        for (int i = 0; i < m.getGyousuu(); i++) {
            System.out.println(m.getGyou(i+1));
        }
        int i_r=0;
        Takakukei sikaku= new Takakukei(4);
        sikaku.set(1, p1);
        sikaku.set(2, p2);
        sikaku.set(3, p3);
        sikaku.set(4, p4);

        String str="";//�����񏈗��p�̃N���X�̃C���X�^���X��
        Memo memo1 = new Memo();
        memo1.reset();
        memo1.addGyou("<線分集合>");
        int ibangou=0;

        for(int i=1;i<=sousuu;i++){
            Senbun s;s= sen(i);

            if(     (sikaku.totu_kyoukai_naibu(s)==1)&&(getcolor(i)<3)){i_r=1;}//���Ԑ���memo1�ɏ�����Ȃ��B�܂�폜�����B
            else if((sikaku.totu_kyoukai_naibu(s)!=1)||(getcolor(i)>=3)){
                ibangou=ibangou+1;
                memo1.addGyou("番号,"+str.valueOf(ibangou));
                memo1.addGyou( "色,"+str.valueOf(s.getcolor()));
                memo1.addGyou( "座標,"  +	str.valueOf(s.getax())+","+ str.valueOf(s.getay())+","+ str.valueOf(s.getbx())+","+ str.valueOf(s.getby()));
                memo1.addGyou("<tpp>"+ s.get_tpp() + "</tpp>");
                memo1.addGyou("<tpp_color_R>"+ s.get_tpp_color().getRed() + "</tpp_color_R>");
                memo1.addGyou("<tpp_color_G>"+ s.get_tpp_color().getGreen() + "</tpp_color_G>");
                memo1.addGyou("<tpp_color_B>"+ s.get_tpp_color().getBlue() + "</tpp_color_B>");
            }
        }

        memo1.addGyou("<円集合>");
        int ii=0;
        for(int i=1;i<=cir_size();i++ ){
            En e_temp= new En();  e_temp.set(cir_getEn(i));//ec.set(e_temp.get_tyuusin());er=e_temp.getr();

            ii=ii+1;
            memo1.addGyou("番号,"+str.valueOf(ii));
            memo1.addGyou( "中心と半径と色,"  +	str.valueOf(e_temp.getx())+","+ str.valueOf(e_temp.gety())+","+ str.valueOf(e_temp.getr())    +","+ str.valueOf(e_temp.getcolor())   );
            memo1.addGyou("<tpp>"+ e_temp.get_tpp() + "</tpp>");
            memo1.addGyou("<tpp_color_R>"+ e_temp.get_tpp_color().getRed() + "</tpp_color_R>");
            memo1.addGyou("<tpp_color_G>"+ e_temp.get_tpp_color().getGreen() + "</tpp_color_G>");
            memo1.addGyou("<tpp_color_B>"+ e_temp.get_tpp_color().getBlue() + "</tpp_color_B>");
            //}
        }

        reset();
        setMemo(memo1);
        return i_r;
    }






    //--------------------------------
//--------------------------------
    public int D_nisuru2(Ten p1, Ten p2, Ten p3, Ten p4){//�ܐ��̂ݍ폜

        int i_r=0;
        Takakukei sikaku= new Takakukei(4);
        sikaku.set(1, p1);
        sikaku.set(2, p2);
        sikaku.set(3, p3);
        sikaku.set(4, p4);

        String str=new String();//�����񏈗��p�̃N���X�̃C���X�^���X��
        Memo memo1 = new Memo();
        memo1.reset();
        memo1.addGyou("<線分集合>");
        int ibangou=0;

        for(int i=1;i<=sousuu;i++){
            Senbun s;s= sen(i);

            if(     (sikaku.totu_kyoukai_naibu(s)==1)&&(getcolor(i)==0)){i_r=1;}//������memo1�ɏ�����Ȃ��B�܂�폜�����B
            else if((sikaku.totu_kyoukai_naibu(s)!=1)||(getcolor(i)>=1)){
                ibangou=ibangou+1;
                memo1.addGyou("番号,"+str.valueOf(ibangou));
                memo1.addGyou( "色,"+str.valueOf(s.getcolor()));
                memo1.addGyou( "座標,"  +	str.valueOf(s.getax())+","+ str.valueOf(s.getay())+","+ str.valueOf(s.getbx())+","+ str.valueOf(s.getby()));
                memo1.addGyou("<tpp>"+ s.get_tpp() + "</tpp>");
                memo1.addGyou("<tpp_color_R>"+ s.get_tpp_color().getRed() + "</tpp_color_R>");
                memo1.addGyou("<tpp_color_G>"+ s.get_tpp_color().getGreen() + "</tpp_color_G>");
                memo1.addGyou("<tpp_color_B>"+ s.get_tpp_color().getBlue() + "</tpp_color_B>");
            }
        }

        memo1.addGyou("<円集合>");
        int ii=0;
        for(int i=1;i<=cir_size();i++ ){
            En e_temp= new En();  e_temp.set(cir_getEn(i));//ec.set(e_temp.get_tyuusin());er=e_temp.getr();
            ii=ii+1;
            memo1.addGyou("番号,"+str.valueOf(ii));
            memo1.addGyou( "中心と半径と色,"  +	str.valueOf(e_temp.getx())+","+ str.valueOf(e_temp.gety())+","+ str.valueOf(e_temp.getr())    +","+ str.valueOf(e_temp.getcolor())   );
            memo1.addGyou("<tpp>"+ e_temp.get_tpp() + "</tpp>");
            memo1.addGyou("<tpp_color_R>"+ e_temp.get_tpp_color().getRed() + "</tpp_color_R>");
            memo1.addGyou("<tpp_color_G>"+ e_temp.get_tpp_color().getGreen() + "</tpp_color_G>");
            memo1.addGyou("<tpp_color_B>"+ e_temp.get_tpp_color().getBlue() + "</tpp_color_B>");
        }

        reset();
        setMemo(memo1);
        return i_r;

    }

    // /


    //--------------------------------
    public int D_nisuru3(Ten p1, Ten p2, Ten p3, Ten p4){//�⏕�����̂ݍ폜
        int i_r=0;
        //Ten p1 = new Ten();   p1.set(si.geta());
        Takakukei sikaku= new Takakukei(4);
        sikaku.set(1, p1);
        sikaku.set(2, p2);
        sikaku.set(3, p3);
        sikaku.set(4, p4);

        String str=new String();//�����񏈗��p�̃N���X�̃C���X�^���X��
        Memo memo1 = new Memo();
        memo1.reset();
        memo1.addGyou("<線分集合>");
        int ibangou=0;

        for(int i=1;i<=sousuu;i++){
            Senbun s;s= sen(i);

            if(     (sikaku.totu_kyoukai_naibu(s)==1)&&(getcolor(i)==3)){i_r=1;}
            else if((sikaku.totu_kyoukai_naibu(s)!=1)||(getcolor(i)!=3)){
                ibangou=ibangou+1;
                memo1.addGyou("番号,"+str.valueOf(ibangou));
                //Senbun s;s= sen(i);
                memo1.addGyou( "色,"+str.valueOf(s.getcolor()));
                memo1.addGyou( "座標,"  +	str.valueOf(s.getax())+","+ str.valueOf(s.getay())+","+ str.valueOf(s.getbx())+","+ str.valueOf(s.getby()));
                memo1.addGyou("<tpp>"+ s.get_tpp() + "</tpp>");
                memo1.addGyou("<tpp_color_R>"+ s.get_tpp_color().getRed() + "</tpp_color_R>");
                memo1.addGyou("<tpp_color_G>"+ s.get_tpp_color().getGreen() + "</tpp_color_G>");
                memo1.addGyou("<tpp_color_B>"+ s.get_tpp_color().getBlue() + "</tpp_color_B>");
            }
        }



        Ten ec=new Ten();//�~�̒��S���W������ϐ�
        double er;//�~�̒��S���W������ϐ�


        Senbun s1=new Senbun(p1,p2);
        Senbun s2=new Senbun(p2,p3);
        Senbun s3=new Senbun(p3,p4);
        Senbun s4=new Senbun(p4,p1);


        memo1.addGyou("<円集合>");
        int ii=0;
        for(int i=1;i<=cir_size();i++ ){
            int idel=0;
            En e_temp= new En();  e_temp.set(cir_getEn(i));ec.set(e_temp.get_tyuusin());er=e_temp.getr();

            if(oc.kyori_senbun(ec,s1)<= er){ if((oc.kyori(s1.geta(),ec)>= er)||(oc.kyori(s1.geta(),ec)>= er))  {idel=1;}}
            if(oc.kyori_senbun(ec,s2)<= er){ if((oc.kyori(s2.geta(),ec)>= er)||(oc.kyori(s2.geta(),ec)>= er))  {idel=1;}}
            if(oc.kyori_senbun(ec,s3)<= er){ if((oc.kyori(s3.geta(),ec)>= er)||(oc.kyori(s3.geta(),ec)>= er))  {idel=1;}}
            if(oc.kyori_senbun(ec,s4)<= er){ if((oc.kyori(s4.geta(),ec)>= er)||(oc.kyori(s4.geta(),ec)>= er))  {idel=1;}}

            if(sikaku.totu_kyoukai_naibu(new Senbun( e_temp.get_tyuusin(), e_temp.get_tyuusin()))==1){idel=1;}

            if(idel==1){i_r=1;}
            if(idel==0){
                ii=ii+1;
                memo1.addGyou("番号,"+str.valueOf(ii));
                memo1.addGyou( "中心と半径と色,"  +	str.valueOf(e_temp.getx())+","+ str.valueOf(e_temp.gety())+","+ str.valueOf(e_temp.getr())    +","+ str.valueOf(e_temp.getcolor())   );
                memo1.addGyou("<tpp>"+ e_temp.get_tpp() + "</tpp>");
                memo1.addGyou("<tpp_color_R>"+ e_temp.get_tpp_color().getRed() + "</tpp_color_R>");
                memo1.addGyou("<tpp_color_G>"+ e_temp.get_tpp_color().getGreen() + "</tpp_color_G>");
                memo1.addGyou("<tpp_color_B>"+ e_temp.get_tpp_color().getBlue() + "</tpp_color_B>");
            }
        }

        reset();
        setMemo(memo1);


        return i_r;


    }


    private Senbun sen(int i){
        if(sousuu+1> Senb.size()){while(sousuu+1> Senb.size()){Senb.add(new Senbun());}}
        return (Senbun)Senb.get(i);
    }

    @Override
    public String setMemo(Memo memo1) {
        System.out.println("printing memo");
        for (int i = 0; i < memo1.getGyousuu(); i++) {
            System.out.println(memo1.getGyou(i+1));
        }
        return super.setMemo(memo1);
    }
}
