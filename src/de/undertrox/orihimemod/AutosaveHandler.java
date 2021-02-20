package de.undertrox.orihimemod;

import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AutosaveHandler {
    private OrihimeFrame frame;
    private boolean enable;
    private int interval;
    private int maxAge;
    private Timer timer;
    private String baseFileName;

    public String getBaseFileName() {
        return baseFileName;
    }

    public void setBaseFileName(String baseFileName) {
        this.baseFileName = baseFileName;
    }

    public AutosaveHandler(OrihimeFrame frame, boolean enable, int interval, int maxAge, String baseFileName) {
        this.frame = frame;
        this.enable = enable;
        this.interval = interval;
        this.maxAge = maxAge;
        timer = new Timer(this.interval*1000, this::autosave);
        this.baseFileName = baseFileName;
    }

    public void start() {
        if (enable) {
            timer.start();
        }
    }

    public void stop() {
        timer.stop();
    }

    public void autosave(ActionEvent e) {
        autosave();
    }

    public void autosave() {
        File file = new File("orihimeMod-Autosave");
        file.mkdirs();
        deleteFilesOlderThan(file, maxAge * 1000);
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
        System.out.println("AutoSaving " + baseFileName);
        SaveHelper.saveTo(frame, "orihimeMod-Autosave/" + dateFormat.format(date) + baseFileName + ".orh");
        SaveHelper.saveTo(frame, "orihimeMod-Autosave/" + dateFormat.format(date) + baseFileName + ".cp");
        System.out.println("AutoSaving Done.");
    }

    private void deleteFilesOlderThan(File file, int maxDiff) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFilesOlderThan(f, maxDiff);
            }
        } else {
            long diff = new Date().getTime() - file.lastModified();

            if (diff > maxDiff) {
                file.delete();
            }
        }
    }
}
