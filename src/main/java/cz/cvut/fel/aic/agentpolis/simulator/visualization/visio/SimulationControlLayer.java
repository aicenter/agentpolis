/* 
 * Copyright (C) 2017 fido.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package cz.cvut.fel.aic.agentpolis.simulator.visualization.visio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.cvut.fel.aic.agentpolis.siminfrastructure.time.StandardTimeProvider;
import cz.cvut.fel.aic.alite.simulation.Simulation;
import cz.cvut.fel.aic.alite.vis.Vis;
import cz.cvut.fel.aic.alite.vis.layer.AbstractLayer;
import cz.cvut.fel.aic.alite.vis.layer.common.HelpLayer;
import cz.cvut.fel.aic.alite.vis.layer.toggle.KeyToggleLayer;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * The layer shows the status of the simulation and controls it through various key bindings.
 * <p>
 * The information shown, tells the user, the current simulation speed (ratio of the real time and simulation time) and
 * the state of the simulation.
 * <p>
 * The simulation speed ratio can be controlled by '+' and '-' keys. And additionally, Ctrl+'*' sets the fastest
 * possible speed (infinite ratio), and '*' pressed sets the ratio to its default value.
 * <p>
 * All the possible key strokes are described in the internal help showed by the {@link HelpLayer}.
 *
 * @author Antonin Komenda
 * @author Ondrej Milenovsky
 * @author Zbynek Moler
 */
@Singleton
public class SimulationControlLayer extends AbstractLayer {
    
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    
    private static final Color TEXT_COLOR = Color.BLUE;
    
    private static final Point STRING_POSITION = new Point(15, 20);
    
    private static final Point TIME_POSITION = new Point(180, 22);

	protected static Logger logger = Logger.getLogger(SimulationControlLayer.class);

    
    
    
    
	private final Simulation simulation;
    
    private final StandardTimeProvider timeProvider;

    
    

    @Inject
	private SimulationControlLayer(Simulation simulation, StandardTimeProvider timeProvider) {
		this.simulation = simulation;
        this.timeProvider = timeProvider;
        
        KeyToggleLayer toggle = KeyToggleLayer.create("s");
		toggle.addSubLayer(this);
		toggle.setHelpOverrideString(getLayerDescription() + "\n" +
									 "By pressing 's', the simulation info can be turned off and on.");
	}

    
    
    
	@Override
	public void init(Vis vis) {
		super.init(vis);

		vis.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '+') {
					simulation.setSimulationSpeed(simulation.getSimulationSpeed() * 0.9);
				} else if (e.getKeyChar() == '-') {
					simulation.setSimulationSpeed(simulation.getSimulationSpeed() * 1.1);
				} else if (e.getKeyChar() == '(') {
					simulation.turnOnEventStepSimulation();

				} else if (e.getKeyChar() == ')') {
					simulation.turnOffEventStepSimulation();

				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					simulation.turnOffWaitingOnNextEventToInterruption();

				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					simulation.turnOnWaitingOnNextEventToInterruption();

				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					simulation.interruptionWaitingOnNextEvent();

				} else if (e.getKeyChar() == ' ') {
					if (simulation.isRunning()) {
						simulation.setRunning(false);
					} else {
						simulation.setRunning(true);
					}
				} else if (e.getKeyChar() == '*') {
					if ((e.getModifiers() & (KeyEvent.CTRL_MASK | KeyEvent.CTRL_DOWN_MASK)) != 0) {
						simulation.setSimulationSpeed(0);
					} else {
						simulation.setSimulationSpeed(1);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					javax.swing.JOptionPane.showConfirmDialog(null, "Delete GE files on exit to generate new?\n" +
																	"      Yes - delete only static.kmz\n" +
																	"      No - delete all GE files\n" +
																	"      Cancel - delete nothing", "UrbanSim",
															  JOptionPane.YES_NO_CANCEL_OPTION);
					// if (selected == JOptionPane.YES_OPTION) {
					// File f = new File(KmlFileGE.TMP_PATH +
					// GECreateStatic.LINK);
					// logger.info("Deleting " + f);
					// f.deleteOnExit();
					// } else if (selected == JOptionPane.NO_OPTION) {
					// ClearCreator cc = new ClearCreator();
					// cc.init(new String[] { ClearCreator.DELETE_ON_EXIT });
					// cc.create();
					// }
				}
			}
		});
	}

	@Override
	public void paint(Graphics2D canvas) {
		StringBuilder label = new StringBuilder();
		label.append("TIME: ");
		label.append(simulation.getCurrentTime() / 1000.0);
		label.append(' ');
		Dimension dim = Vis.getDrawingDimension();
		if (simulation.isFinished()) {
			label.append("(FINISHED)");
		} else {
			if (simulation.getCurrentTime() == 0) {
				label.append("(INITIALIZING)");

				canvas.setColor(new Color(0, 0, 0, 200));
				canvas.fillRect(200, 400, dim.width - 400, dim.height - 800);

				Font oldFont = canvas.getFont();
				canvas.setFont(new Font("Arial", 0, 20));
				canvas.setColor(Color.WHITE);
				canvas.drawString("INITIALIZING...", dim.width / 2 - 60, dim.height / 2 + 7);

				canvas.setFont(oldFont);
			} else {
				if (simulation.isRunning()) {
					label.append('(');
					label.append(MessageFormat.format("{0,number,#.##}", 1 / simulation.getSimulationSpeed()));
					label.append("x)");
				} else {
					label.append("(PAUSED)");
				}
			}
		}
        
        VisioUtils.printTextWithBackgroud(canvas, label.toString(), STRING_POSITION, TEXT_COLOR, BACKGROUND_COLOR);

		Font font = canvas.getFont();
		canvas.setFont(new Font(font.getName(), Font.BOLD, 18));

        VisioUtils.printTextWithBackgroud(canvas, converSimTimeForVis(timeProvider), TIME_POSITION, TEXT_COLOR, 
                BACKGROUND_COLOR);

		canvas.setFont(new Font(font.getName(), 0, 12));
		// canvas.drawString(TimeStorage.printDate(environment.getTimeStorage().getActualTime()),
		// 257,
		// 22);
		canvas.setFont(font);
	}

	private static final long DAY_IN_MILLIS = Duration.ofDays(1).toMillis();
	private static final long HOUR_IN_MILLIS = Duration.ofHours(1).toMillis();
	private static final long MIN_IN_MILLIS = Duration.ofMinutes(1).toMillis();
    private static final long SECONDS_IN_MILLIS = Duration.ofSeconds(1).toMillis();
	private static final Format formatter = new SimpleDateFormat("HH:mm:ss");
	private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("YYYY-MM-dd");

	private String converSimTimeForVis(StandardTimeProvider timeProvider) {

		long timeFromSimulationStart = timeProvider.getCurrentSimTime();

		long timeInDayRange = timeFromSimulationStart % DAY_IN_MILLIS;
		long hours = timeInDayRange / HOUR_IN_MILLIS;
		long min = (timeInDayRange % HOUR_IN_MILLIS) / MIN_IN_MILLIS;
        long sec = (timeInDayRange % MIN_IN_MILLIS) / SECONDS_IN_MILLIS;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, (int) hours);
		calendar.set(Calendar.MINUTE, (int) min);
        calendar.set(Calendar.SECOND, (int) sec);

		return formatter.format(calendar.getTime()) + " " + timeProvider.getCurrentDayInWeek().toString() + " " +
			   fmt.format(timeProvider.getCurrentDate());
	}

	@Override
	public String getLayerDescription() {
		String description =
				"[Simulation Control] Layer controls the simulation and shows simulation time and speed,\n" +
				"by pressing '<space>', the simulation can be paused and unpaused,\n" +
				"by pressing '+'/'-', the simulation can be speed up and slow down,\n" +
				"by pressing '*', the speed of simulation is set to default value (1x),\n" +
				"by pressing Ctrl+'*', the speed of simulation is set to fastest possible speed (????)\n" +
				"by pressing '<delete>', delete GE static file on exit, so next run new file will be generated.\n";
		return buildLayersDescription(description);
	}

}
