package cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.vis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItemImpl;
import cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItemType;

/**
 * Copy/paste with some modification from AgentC
 * 
 * @author Zbynek Moler
 * 
 */
public class NewHistorianView extends JPanel implements ChangeListener, ActionListener {

    private static final long serialVersionUID = 8974397656215082486L;

    private NewTimeLine newTimeLine;
    private CheckBoxJList eventList = null;

    private final ViewLogItemType[] historyEventTypes;

    private Timer timer = new Timer(1000, this);

    public NewHistorianView(ViewLogItemType[] historyEventTypes, long simulationDuration) {
        super(new BorderLayout());

        this.historyEventTypes = historyEventTypes;

        this.newTimeLine = new NewTimeLine(simulationDuration);

        initComponents();

        this.addComponentListener(new ComponentListener() {

            public void componentShown(ComponentEvent e) {
                NewHistorianView.this.actionPerformed(null);
                timer.start();
            }

            public void componentResized(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentHidden(ComponentEvent e) {
                timer.stop();
            }
        });
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));

        JPanel leftTopPanel = new JPanel(new BorderLayout());
        leftTopPanel.add(getEventsCheckboxes(), BorderLayout.CENTER);
        leftTopPanel.add(Box.createRigidArea(new Dimension(10, 15)), BorderLayout.PAGE_END);
        leftPanel.add(leftTopPanel, BorderLayout.CENTER);

        // leftPanel.add(getVesselStatsPane(), BorderLayout.PAGE_END);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(getTimeLine(), BorderLayout.CENTER);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(leftPanel, BorderLayout.LINE_START);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        this.add(mainPanel, BorderLayout.CENTER);
    }

    private Component getTimeLine() {
        // return newTimeLine.getScrollPane();
        return newTimeLine;
    }

    private JPanel getEventsCheckboxes() {
        JPanel jp = new JPanel(new BorderLayout(10, 10));
        jp.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Event types"));

        DefaultListModel<ViewLogItemType> listModel = new DefaultListModel<>();
        for (ViewLogItemType status : historyEventTypes) {
            listModel.addElement(status);
        }

        eventList = new CheckBoxJList(listModel, this);
        eventList.setColorIcon(true);
        eventList.setBorder(BorderFactory.createEmptyBorder());
        eventList.setBackground((Color) UIManager.get("Panel.background"));
        eventList.setOpaque(false);
        eventList.setCheckboxBackround((Color) UIManager.get("Panel.background"));
        eventList.setBlack(false);
        eventList.checkAll();

        JScrollPane jsp = new JScrollPane(eventList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setBorder(BorderFactory.createEmptyBorder());
        jp.add(jsp, BorderLayout.CENTER);

        return jp;
    }

    public Set<ViewLogItemType> getChecked() {
        Set<ViewLogItemType> checked = new HashSet<ViewLogItemType>();
        Set<Object> chstr = eventList.getChecked();
        for (Object o : chstr) {
            checked.add((ViewLogItemType) o);
        }
        return checked;
    }

    public void stateChanged(ChangeEvent e) {
        newTimeLine.filterEvents(getChecked());
        this.repaint();
    }

    public void update(ViewLogItemImpl e) {
        if (e.getStep() > 1) {
            newTimeLine.update(e, getChecked());
        }
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void stepChanged(int currentTimeOfSimulationInSecond) {
        newTimeLine.tryToSetTickView(currentTimeOfSimulationInSecond);
        this.repaint();
    }

}
