package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid3d.SpeedPoint;

/**
 * Created by wmatex on 4.4.15.
 */
public class Straight {
    private static final double EPSILON = 0.00001;

    private Point4d start;
    private Point4d end;

    public Straight(SpeedPoint start, double tStart, SpeedPoint end, double tEnd) {
        this(new Point4d(start, tStart), new Point4d(end, tEnd));
    }

    public Straight(Point4d start, Point4d end) {
        super();
        if (start.getTime() <= end.getTime()) {
            this.start = start;
            this.end = end;
        } else {
            this.start = end;
            this.end = start;
        }
    }

    public double duration() {
        return end.getTime() - start.getTime();
    }

    public Point4d interpolate(double time) {
        double tStart = start.getTime();
        double tEnd = end.getTime();

        if (time < tStart || tEnd < time)
            throw new RuntimeException("This straight is not defined in time " + time);

        if (Math.abs(time - tStart) < EPSILON)
            return start;

        if (Math.abs(time - tEnd) < EPSILON)
            return end;

        double scale = ((double) (time - tStart)) / (tEnd - tStart);

        double xStart = start.getX();
        double xEnd = end.getX();

        double yStart = start.getY();
        double yEnd = end.getY();

        double speedStart = start.getSpeed();
        double speedEnd = end.getSpeed();

        double x = xStart + (xEnd - xStart) * scale;
        double y = yStart + (yEnd - yStart) * scale;
        double speed = speedStart + (speedEnd - speedStart) * scale;

        return new Point4d(x, y, speed, time);
    }

    public Point4d getStart() {
        return start;
    }

    public Point4d getEnd() {
        return end;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Straight other = (Straight) obj;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return String.format("(%s : %s)", start, end);
    }
}
