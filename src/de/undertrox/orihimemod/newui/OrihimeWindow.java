package de.undertrox.orihimemod.newui;

import de.undertrox.orihimemod.Config;
import de.undertrox.orihimemod.OrihimeMod;
import de.undertrox.orihimemod.OrihimeModWindow;
import de.undertrox.orihimemod.mapping.ButtonMapping;
import de.undertrox.orihimemod.newui.button.ButtonFactory;
import de.undertrox.orihimemod.newui.button.MenuItemFactory;
import jp.gr.java_conf.mt777.origami.orihime.OrihimeFrame;
import jp.gr.java_conf.mt777.origami.orihime.ap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class OrihimeWindow extends JFrame {
    private JPanel canvas;
    private JPanel topPanel;
    private JPanel toolPanel;
    private JMenuBar menuBar;
    private ButtonMapping mapping;
    private ButtonFactory btnFactory;
    private MenuItemFactory menuItemFactory;
    OrihimeFrame frame;

    public OrihimeWindow(OrihimeFrame parentFrame, ButtonMapping mapping, ResourceBundle toolTips, OrihimeModWindow window) {
        super();
        parentFrame.setVisible(false);
        frame = parentFrame;
        this.mapping = mapping;
        this.btnFactory = new ButtonFactory(mapping, toolTips, window);

        menuBar = new JMenuBar();
        menuItemFactory = new MenuItemFactory(mapping, window);
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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispatchEvent(e);
            }
        });
    }

    private void initToolPanel() {
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    private void initMenuBar() {
        JMenu file = new JMenu("File");
        file.add(menuItemFactory.createMenuItem("open", "Open"));
        file.add(menuItemFactory.createMenuItem("save", "Save"));
        JMenu export = new JMenu("Export");
        export.add(menuItemFactory.createMenuItem("save_as_cp", "CP"));
        export.add(menuItemFactory.createMenuItem("save_as_dxf", "DXF"));
        export.add(menuItemFactory.createMenuItem("save_as_svg", "SVG"));
        export.add(menuItemFactory.createMenuItem("export_image", "PNG"));
        if (Config.useNewSave()){
            file.add(menuItemFactory.createMenuItem("save_as", "Save As"));
        }

        file.add(export);
        file.add(menuItemFactory.createMenuItem("exit", "Exit", e ->
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))));
        menuBar.add(file);

        JMenu edit = new JMenu("Edit");
        JMenu lineType = new JMenu("Line Type");
        lineType.add(menuItemFactory.createMenuItem("mountain", "Mountain"));
        lineType.add(menuItemFactory.createMenuItem("valley", "Valley"));
        lineType.add(menuItemFactory.createMenuItem("edge", "Edge"));
        lineType.add(menuItemFactory.createMenuItem("auxiliary", "Auxiliary"));
        edit.add(lineType);
        edit.add(menuItemFactory.createMenuItem("draw_line", "Draw Line (Unrestricted)"));
        edit.add(menuItemFactory.createMenuItem("draw_line_restricted", "Draw Line (Restricted)"));
        edit.add(menuItemFactory.createMenuItem("flatfold_vertex", "Flatfold Vertex"));
        edit.add(menuItemFactory.createMenuItem("angle_bisector", "Angle Bisector"));
        edit.add(menuItemFactory.createMenuItem("rabbit_ear", "Rabbit Ear"));
        edit.add(menuItemFactory.createMenuItem("perpendicular", "Perpendicular Line"));

        menuBar.add(edit);
    }

    private void initTopPanel() {
        JButton save = btnFactory.createButton("save");
        JButton open = btnFactory.createButton("open");
        topPanel.add(save);
        topPanel.add(open);
    }

    private static class Canvas extends JPanel {
        ap frame;
        public Canvas(ap frame) {
            this.frame = frame;
            this.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    frame.dispatchEvent(e);
                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    frame.dispatchEvent(e);
                    repaint();
                }
            });
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    frame.dispatchEvent(e);
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    frame.dispatchEvent(e);
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    frame.dispatchEvent(e);
                    repaint();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    frame.dispatchEvent(e);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    frame.dispatchEvent(e);
                    repaint();
                }
            });
            this.addMouseWheelListener(e -> {
                frame.dispatchEvent(e);
                repaint();
            });
        }

        @Override
        public void paint(Graphics g) {
            frame.paint(g);
        }
    }
}
