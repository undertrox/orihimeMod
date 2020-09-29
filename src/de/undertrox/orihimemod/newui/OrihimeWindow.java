package de.undertrox.orihimemod.newui;

import de.undertrox.orihimemod.mapping.ButtonMapping;
import de.undertrox.orihimemod.mapping.MappingButton;
import jp.gr.java_conf.mt777.origami.orihime.ap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OrihimeWindow extends JFrame {
    private JPanel canvas;
    private JPanel topPanel;
    private JPanel toolPanel;
    private JMenuBar menuBar;
    private ButtonMapping mapping;
    ap frame;

    public OrihimeWindow(ap parentFrame, ButtonMapping mapping) {
        super();
        parentFrame.setVisible(false);
        frame = parentFrame;
        this.mapping = mapping;

        menuBar = new JMenuBar();
        initMenuBar();
        setJMenuBar(menuBar);

        setLayout(new BorderLayout());

        canvas = new Canvas(frame);
        canvas.setSize(frame.getSize());
        add(canvas, BorderLayout.CENTER);

        topPanel = new JPanel();
        initTopPanel();
        add(topPanel, BorderLayout.NORTH);

        toolPanel = new JPanel();
        initToolPanel();
        add(toolPanel, BorderLayout.WEST);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                frame.setSize(e.getComponent().getSize());
            }
        });
        addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initToolPanel() {
        toolPanel.setOpaque(true);
        toolPanel.setBackground(new Color(0,0,0,0));
        Icon icon = new ImageIcon("icon/Artboard 7.png");
        JButton test = new JButton(icon);
        toolPanel.add(test);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    private void initMenuBar() {
        JMenu m = new JMenu("File");
        m.add(new JMenuItem("Save"));
        m.add(new JMenuItem("Open"));
        m.add(new JMenuItem("Exit"));
        menuBar.add(m);
    }

    private void initTopPanel() {
        JButton test = new MappingButton("save", mapping);
        //test.setText("Save");
        test.setIcon(new ImageIcon("icon/Artboard 1.png"));
        test.setOpaque(false);
        test.setContentAreaFilled(false);
        test.setBorderPainted(false);
        test.setMargin(new Insets(2, 2, 2, 2));
        topPanel.add(test);
    }

    private static class Canvas extends JPanel {
        ap frame;
        public Canvas(ap frame) {
            this.frame = frame;
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    frame.mouseDragged(e);
                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    frame.mouseMoved(e);
                    repaint();
                }
            });
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.mouseClicked(e);
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    frame.mousePressed(e);
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    frame.mouseReleased(e);
                    repaint();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    frame.mouseEntered(e);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    frame.mouseExited(e);
                    repaint();
                }
            });
            this.addMouseWheelListener(e -> {
                frame.mouseWheelMoved(e);
                repaint();
            });
        }

        @Override
        public void paint(Graphics g) {
            frame.paint(g);
        }
    }
}
