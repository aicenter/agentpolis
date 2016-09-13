/**
 * 
 */
package cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.vis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItemType;

/**
 * Copy/paste with some modification from AgentC
 * @author Zbynek Moler
 *
 */
/**
 * @author Ondrej Vanek
 * 
 */
public class CheckBoxJList extends JList<ViewLogItemType> implements ListSelectionListener,
        ChangeListener {

    private static final long serialVersionUID = 526555524959749092L;

    private List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private boolean black = true;
    private boolean colorIcon = false;

    private Color background;

    public CheckBoxJList(ListModel<ViewLogItemType> model, ChangeListener listener) {
        super();
        for (int i = 0; i < model.getSize(); i++) {
            String s = model.getElementAt(i).toString();
            JCheckBox checkBox = new JCheckBox(s);
            checkBox.addChangeListener(listener);

            checkBoxList.add(checkBox);

        }
        // this.addListSelectionListener(listener);
        super.setModel(model);
        setCellRenderer(new CheckBoxListCellRenderer());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                // System.out.println(e.getPoint());
                if (index != -1 /* && e.getPoint().x<17 */) {

                    // lastSelected = index;
                    JCheckBox checkBox = checkBoxList.get(index);
                    checkBox.setSelected(!checkBox.isSelected());
                    repaint();
                }
            }
        });

        setSelectionMode(0);
    }

    public static final Icon getUncheckedImage(Color color) {
        BufferedImage bi = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        g2d.setBackground(new Color(0, 0, 0, 255));
        g2d.setColor(color);
        g2d.fillRect(2, 2, 14, 14);
        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(new Color(0xeeeeee));
        g2d.drawLine(1, 1, 1, 15);
        g2d.drawLine(1, 1, 15, 1);
        g2d.setColor(new Color(0x888888));
        g2d.drawLine(16, 16, 2, 16);
        g2d.drawLine(16, 16, 16, 2);
        return new ImageIcon(bi);
    }

    public static final Icon getCheckedImage(Color color) {
        BufferedImage bi = new BufferedImage(18, 18, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        g2d.setBackground(new Color(0, 0, 0, 255));
        g2d.setColor(color);
        g2d.fillRect(2, 2, 14, 14);

        g2d.setStroke(new BasicStroke(1f));
        g2d.setColor(new Color(0xeeeeee));
        g2d.drawLine(1, 1, 1, 15);
        g2d.drawLine(1, 1, 15, 1);
        g2d.setColor(new Color(0x888888));
        g2d.drawLine(16, 16, 2, 16);
        g2d.drawLine(16, 16, 16, 2);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(getCompatibleColor(color));
        g2d.setStroke(new BasicStroke(1.4f));
        g2d.drawLine(4, 9, 7, 13);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(7, 13, 13, 3);
        return new ImageIcon(bi);
    }

    private static final Color getCompatibleColor(Color color) {
        int r = color.getRed();
        int b = color.getBlue();
        int g = color.getGreen();

        float lum = 0.3f * r + 0.59f * g + 0.11f * b;

        if (lum > 128) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    @Override
    public void setModel(ListModel<ViewLogItemType> model) {
        checkBoxList.clear();
        DefaultListModel<ViewLogItemType> m = new DefaultListModel<>();
        for (int i = 0; i < model.getSize(); i++) {
            ViewLogItemType s = model.getElementAt(i);
            JCheckBox checkBox = new JCheckBox(s.toString());
            checkBoxList.add(checkBox);
            m.addElement(s);
        }
        super.setModel(m);
    }

    public void setColorIcon(boolean colorIcon) {
        this.colorIcon = colorIcon;
    }

    private class CheckBoxListCellRenderer implements ListCellRenderer<ViewLogItemType> {

        public Component getListCellRendererComponent(JList<? extends ViewLogItemType> list,
                ViewLogItemType value, int index, boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = checkBoxList.get(index);

            if (value instanceof ViewLogItemType) {
                ViewLogItemType status = (ViewLogItemType) value;
                if (status != null) {
                    if (black) {
                        checkbox.setForeground(status.getColor());
                        checkbox.setBackground(Color.BLACK);
                    } else if (background != null) {
                        checkbox.setOpaque(true);
                        checkbox.setBackground(background);
                    }

                    if (colorIcon) {
                        checkbox.setIcon(getUncheckedImage(status.getColor()));
                        checkbox.setSelectedIcon(getCheckedImage(status.getColor()));
                    }
                }
            }

            return checkbox;

        }
    }

    /**
     * Gets the checked Extractors.
     * 
     * @return
     */
    public Set<Object> getChecked() {
        Set<Object> list = new HashSet<Object>();
        for (int i = 0; i < checkBoxList.size(); i++) {
            if (checkBoxList.get(i).isSelected()) {
                list.add(getModel().getElementAt(i));
            }
        }
        return list;
    }

    public void valueChanged(ListSelectionEvent e) {
        e.getSource();
    }

    public void stateChanged(ChangeEvent e) {
    }

    public void checkAll() {
        for (JCheckBox cb : checkBoxList) {
            cb.setSelected(true);
        }
    }

    public void setBlack(boolean b) {
        this.black = b;
    }

    /**
     * @param event
     */
    public void check(ViewLogItemType event) {
        this.setSelectedValue(event.toString(), false);
        int selectedIndex = this.getSelectedIndex();
        if (selectedIndex > -1) {
            checkBoxList.get(selectedIndex).setSelected(true);
        }
    }

    /**
     * @param background
     *            the background to set
     */
    public void setCheckboxBackround(Color background) {
        this.background = background;
        if (!black)
            this.setBackground(background);
        // this.repaint();
    }

}
