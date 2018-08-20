package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid2d;

import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid3d.SpeedPoint;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d.Point4d;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.planning.euclid4d.Straight;

import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

public class BasicSegmentedTrajectory implements Trajectory {

    private double startTime;
    private double endTime;
    private double maxTime;
    private SpeedPoint endWayPoint;
    private List<Straight> segments;

    public BasicSegmentedTrajectory(List<Straight> segments, double duration) {
        checkNonEmpty(segments);
        this.segments = ensureRandomAccessList(segments);
        checkContinuity(this.segments);

        this.startTime = segments.get(0).getStart().getTime();
        this.maxTime = startTime + duration;
        this.segments = segments;

        Point4d endTimePoint = segments.get(segments.size()-1).getEnd();
        this.endTime = endTimePoint.getTime();
        this.endWayPoint = endTimePoint.getSpeedPoint();
    }

    protected static void checkContinuity(List<Straight> segments) {
        for (int i = 1; i < segments.size(); i++) {
            Straight a = segments.get(i - 1);
            Straight b = segments.get(i);
            if (!a.getEnd().equals(b.getStart()))
                throw new IllegalArgumentException(String.format("The trajectory is not continuous (%s -!-> %s)", a, b));
        }
    }

    private List<Straight> ensureRandomAccessList(List<Straight> segments) {
        if (segments instanceof RandomAccess)
            return segments;
        else
            return new ArrayList<Straight>(segments);
    }

    private void checkNonEmpty(List<Straight> segments) {
        if (segments.isEmpty())
            throw new RuntimeException("Trajectory can not be created from empty list");
    }

    @Override
    public SpeedPoint get(double t) {
        if (t < startTime || t > maxTime) {
            return null;
        } else if (t > endTime) {
            return endWayPoint;
        }

        Straight segment = findSegment(t);

        return segment.interpolate(t).getSpeedPoint();
    }

    //TODO: using interpolation would decrease complexity from O(log N) to O(log log N)
    public Straight findSegment(double t) {
        int iMin = 0;
        int iMax = segments.size() - 1;

        while (iMax >= iMin) {
            int iMid = (iMin + iMax) / 2;

            Straight segment = segments.get(iMid);
            double tStart = segment.getStart().getTime();
            double tEnd = segment.getEnd().getTime();

            if (t < tStart) {
                iMax = iMid - 1;
            } else if (tEnd < t) {
                iMin = iMid + 1;
            } else {
                return segment;
            }
        }

        throw new RuntimeException("Straight in time " + t + " not found");
    }

    public List<Straight> getSegments() { return segments; }

    @Override
    public double getMinTime() {
        return startTime;
    }

    @Override
    public double getMaxTime() {
        return maxTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((segments == null) ? 0 : segments.hashCode());
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
        BasicSegmentedTrajectory other = (BasicSegmentedTrajectory) obj;
        if (segments == null) {
            if (other.segments != null)
                return false;
        } else if (!segments.equals(other.segments))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");

        if (!segments.isEmpty()) {
            sb.append(" " + segments.get(0).getStart());
        }

        for (Straight maneuver : segments) {
            sb.append(" " + maneuver.getEnd());
        }
        sb.append("");
        return sb.toString();
    }


}
