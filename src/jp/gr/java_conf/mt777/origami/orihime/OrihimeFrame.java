package jp.gr.java_conf.mt777.origami.orihime;

import jp.gr.java_conf.mt777.kiroku.memo.Memo;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OrihimeFrame extends ap {
    public String completeFileName;
    public String fileName;
    public List<Observer> observers = new ArrayList<>();

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

        if( !file_ok){
            return memo_temp;
        }

        // Op button
        if (img_kaisetu_fname.equals("qqq/yomi_tuika.png")) {

        } else { // Normal Open button
            frame_title=frame_title_0+"        "+fd.getFile();
            setTitle(frame_title);es1.set_title(frame_title);
            fileName = fd.getFile();
            // Remove extension
            if (fileName.contains(".")) {
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
            }
        } catch(Exception e) {
            System.out.println(e);
            frame_title=frame_title_0+"        "+"X";
            setTitle(frame_title);es1.set_title(frame_title);


        }
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
}
