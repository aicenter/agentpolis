package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.viewer.historian.vis;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.time.ZonedDateTime;

import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItem;
import cz.cvut.fel.aic.agentpolis.simulator.visualization.visio.viewer.historian.event.ViewLogItemType;

/**
 * Copy/paste with some modification from AgentC
 * 
 * @author Zbynek Moler
 * 
 */
public class NewTimeLine extends JPanel implements MouseMotionListener, MouseListener,
        MouseWheelListener {

    private static final long serialVersionUID = -4706223402656273288L;

    private static final DateTimeFormatter time2String = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final ZonedDateTime ZERO = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault());

    private static final int LINE_OFFSET = 100;
    private static final int VERTICAL_OFFSET = 80;

    private static final int DEFAULT_TICKMARK = 5 * 60; // 60 second

    private int MAX_SIMULATION_DURATION;
    private int MAX_TICK_MARK_ID;

    private static final Stroke BOLD_STROKE = new BasicStroke(2f);
    private static final Stroke DEFAULT_STROKE = new BasicStroke(1f);

    private static final Color DEFAULT_EV_COLOR = new Color(0x777777);

    private static final int visEventHeight = 20;
    private static final int eventStep = 30;
    private final int history = eventStep;

    private JScrollBar scrollBar;
    private DrawableArea drawableArea;

    private int oldX;
    private volatile boolean cursorAutoMove = true;

    private List<VisLogItem> allVisEvents = new ArrayList<>();
    private List<VisLogItem> currentShowVisEvents = new ArrayList<>();

    private int currentTickValueInSecond;

    // private final List<ActionListener> listeners;

    public NewTimeLine(long simulationDuration) {
        super(new BorderLayout(0, 0));

        long maxSimulationDuration = simulationDuration / 1000; // To secund
        checkArgument(maxSimulationDuration <= Integer.MAX_VALUE - 2 * LINE_OFFSET,
                "NewTimeLine to be able to use then the simulation duration has to be less then"
                        + (Integer.MAX_VALUE - 2 * LINE_OFFSET));

        MAX_SIMULATION_DURATION = (int) maxSimulationDuration;
        MAX_TICK_MARK_ID = MAX_SIMULATION_DURATION / DEFAULT_TICKMARK;

        // this.listeners = listeners;

        this.setBorder(BorderFactory.createLineBorder(new Color(0xa0a0a0), 1));

        this.scrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 10, 0, MAX_SIMULATION_DURATION);
        this.scrollBar.setUnitIncrement(10);
        this.scrollBar.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                drawableArea.repaint();
            }
        });

        this.add(scrollBar, BorderLayout.PAGE_END);

        this.drawableArea = new DrawableArea();
        this.add(drawableArea, BorderLayout.CENTER);

        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
    }

    /**
     * IS called when new {@code SimpleEvent}s are available.
     * 
     */
    public synchronized void update(ViewLogItem e, Set<ViewLogItemType> checked) {
        int x = e.getStep() + LINE_OFFSET;
        int y = -VERTICAL_OFFSET - 25;
        boolean ok = false;
        while (!ok && allVisEvents.size() > 0) {
            ok = true;
            int eventSize = allVisEvents.size();
            int lower = Math.max(0, eventSize - history);
            List<VisLogItem> list = allVisEvents.subList(lower, eventSize);
            for (Rectangle r : list) {
                if (r.contains(x, y - 2)) {
                    ok = false;
                    y -= eventStep;
                }
            }
        }

        int eventWidth = 30 + e.getDescription().length() * 5;

        VisLogItem rectangle = new VisLogItem(x - 9, y - visEventHeight, eventWidth,
                visEventHeight, x - LINE_OFFSET, e);
        allVisEvents.add(rectangle);
        if (checked.contains(e.getType())) {
            currentShowVisEvents.add(rectangle);
            recomputeRectanglesPosition(rectangle);
        }
        this.repaint();
    }

    public synchronized void filterEvents(Set<ViewLogItemType> checked) {
        currentShowVisEvents.clear();
        for (VisLogItem r : allVisEvents) {
            if (checked.contains(r.getEvent().getType())) {
                currentShowVisEvents.add(r);
            }
        }
        recomputeRectanglesPosition();
    }

    private synchronized void recomputeRectanglesPosition(VisLogItem rect) {
        int x = rect.getEvent().getStep() + LINE_OFFSET;
        int y = -VERTICAL_OFFSET - 25;
        boolean ok = false;
        while (!ok && currentShowVisEvents.size() > 0) {
            ok = true;
            int eventSize = currentShowVisEvents.size();
            int lower = Math.max(0, eventSize - history);
            List<VisLogItem> list = currentShowVisEvents.subList(lower, eventSize);
            for (Rectangle r : list) {
                if (!rect.equals(r) && (r.contains(x - 9, y - 2) || r.contains(x, y - 2))) {
                    ok = false;
                    y -= eventStep;
                }
            }
        }
        rect.x = x - 9;
        rect.y = y - visEventHeight;

    }

    private synchronized void recomputeRectanglesPosition() {
        for (VisLogItem rect : currentShowVisEvents) {
            recomputeRectanglesPosition(rect);
        }
    }

    private class DrawableArea extends Component {

        private static final long serialVersionUID = 1L;

        public DrawableArea() {
        }

        @Override
        public void paint(Graphics g) {
            Dimension size = getSize();

            int pos = scrollBar.getValue();

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, size.width, size.height);

            g2d.setColor(new Color(0xbbbbbb));

            int dx = currentTickValueInSecond - pos;
            if (dx > -LINE_OFFSET && dx < size.width + LINE_OFFSET) {
                g2d.setStroke(BOLD_STROKE);
                g2d.drawLine(dx + LINE_OFFSET, 0, dx + LINE_OFFSET, size.height);

                g2d.setStroke(DEFAULT_STROKE);
            }

            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            int start = (int) Math.floor(pos / (double) DEFAULT_TICKMARK);
            int end = (int) Math.ceil((pos + size.width) / (double) DEFAULT_TICKMARK);
            if (end > MAX_TICK_MARK_ID) {
                end = MAX_TICK_MARK_ID;
            }

            int offset = pos - (start * DEFAULT_TICKMARK);
            offset = offset % DEFAULT_TICKMARK;
            offset -= LINE_OFFSET;

            int startSteps = start * DEFAULT_TICKMARK;
            int endstart = end - start;
            for (int i = Math.max(-start, -2); i <= endstart; i++) {
                int xs = i * DEFAULT_TICKMARK;
                g2d.setColor(Color.BLACK);
                g2d.drawLine(xs - offset, size.height - VERTICAL_OFFSET - 5, xs - offset,
                        size.height - VERTICAL_OFFSET + 5);
                AffineTransform orig = g2d.getTransform();
                g2d.rotate(Math.PI / 2, xs - offset - 10, size.height - VERTICAL_OFFSET + 20);
                g2d.setColor(Color.BLACK);
                g2d.drawString(step2TimeString(xs + startSteps), xs - offset - 10, size.height
                        - VERTICAL_OFFSET + 20);
                g2d.setTransform(orig);
            }

            g2d.drawLine(Math.max(LINE_OFFSET - pos, 0), size.height - VERTICAL_OFFSET,
                    Math.min(size.width, MAX_SIMULATION_DURATION - pos + LINE_OFFSET), size.height
                            - VERTICAL_OFFSET);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int i = currentShowVisEvents.size() - 1; i >= 0; i--) {
                VisLogItem er = currentShowVisEvents.get(i);
                ViewLogItem e = er.getEvent();

                Color eventColor = (e.getType().getColor());
                paintEvent(g2d, e.getStep(), e.getType().toString(), er, eventColor, pos);
            }

        }

        private String step2TimeString(int timeStep) {
            return time2String.format(ZERO.plus(timeStep * 1000, ChronoUnit.MILLIS));
        }

        private void paintEvent(Graphics2D g, int tickX, String msg, Rectangle rectangle,
                Color eventColor, int pos) {
            int dx = tickX - pos;

            int eventWidth = rectangle.width;

            if (dx > -(LINE_OFFSET + eventWidth)
                    && dx < (getSize().width + LINE_OFFSET + eventWidth)) {
                g.setColor(DEFAULT_EV_COLOR);
                int tick = dx;

                g.fillOval(tick - 2 + LINE_OFFSET, getSize().height - 2 - VERTICAL_OFFSET, 5, 5);
                g.drawLine(tick + LINE_OFFSET, getSize().height - VERTICAL_OFFSET, tick
                        + LINE_OFFSET,
                        (int) (getSize().height + rectangle.getY() + rectangle.getHeight()));
                g.setColor(eventColor);
                g.fillRoundRect(tick - 9 + LINE_OFFSET, getSize().height + (int) rectangle.getY(),
                        rectangle.width, rectangle.height, 8, 8);
                g.setColor(DEFAULT_EV_COLOR);
                g.drawRoundRect(tick - 9 + LINE_OFFSET, getSize().height + (int) rectangle.getY(),
                        rectangle.width, rectangle.height, 8, 8);
                g.setColor(getCompatibleColor(eventColor));
                g.setFont(g.getFont().deriveFont(10.0f));
                g.drawString(msg, tick + LINE_OFFSET, getSize().height + (int) rectangle.getY()
                        + 15);
            }
        }

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

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        cursorAutoMove = false;
    }

    public void mouseExited(MouseEvent e) {
        cursorAutoMove = true;
    }

    public void mousePressed(MouseEvent e) {
        oldX = e.getX();
    }

    public void mouseReleased(MouseEvent e) {
        // synchronized (this) {
        // for (VisEvent r : allVisEvents) {
        // if (r.contains(e.getPoint())) {
        // IHistoryEvent event = r.getEvent();
        // Assert.assertTrue(event!=null);
        // if(event!=null) {
        // ActionEvent ae = new ActionEvent(event, 0, "Event Clicked");
        // for(ActionListener listener: listeners) {
        // listener.actionPerformed(ae);
        // }
        // }
        //
        // }
        // }
        // }
    }

    public void mouseDragged(MouseEvent e) {
        int diff = oldX - e.getX();

        if (scrollBar.getValue() <= 0 && diff < 0) {
            diff = 0;
        }

        if (scrollBar.getValue() >= scrollBar.getMaximum() && diff > 0) {
            diff = 0;
        }

        oldX = e.getX();

        if (Math.abs(diff) > 40) {
            if (Math.abs(diff) > 60) {
                if (Math.abs(diff) > 80) {
                    diff *= 4;
                } else {
                    diff *= 2;
                }
            } else {
                diff *= 1.5;
            }
        }
        scrollBar.setValue(scrollBar.getValue() + diff);
    }

    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        Point recounted = new Point(scrollBar.getValue() + p.x,
                (int) (p.y - getSize().getHeight() + visEventHeight));
        synchronized (this) {
            for (VisLogItem r : allVisEvents) {
                if (r.contains(recounted)) {
                    String descT = r.getEvent().getHtmlDescription();
                    if (descT == null) {
                        descT = r.getEvent().getDescription();
                    }

                    StringBuilder desc = new StringBuilder();
                    desc.append("<html><body><div>");
                    desc.append(descT);
                    desc.append("<br />");
                    desc.append("Source: ");
                    desc.append(r.getEvent().getSource());
                    desc.append("<br />");
                    desc.append("Date: ");
                    desc.append(r.getEvent().getTime().toString());
                    desc.append("</div></body></html>");

                    this.setToolTipText(desc.toString());
                    return;
                }
            }
        }
        this.setToolTipText(null);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        scrollBar.setValue(scrollBar.getValue() + scrollBar.getBlockIncrement()
                * e.getWheelRotation());
    }

    public void tryToSetTickView(int currentTimeOfSimulationInSecond) {
        currentTickValueInSecond = currentTimeOfSimulationInSecond;
        if (cursorAutoMove) {
            scrollBar.setValue(currentTickValueInSecond - getSize().width + (2 * LINE_OFFSET) + 60);
        }
    }
}
