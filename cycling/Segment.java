package cycling;

import java.io.Serializable;

/**
 * The Segment class is a parent class for CategorizedClimb and IntermediateSprint.
 */
abstract class Segment implements Serializable{
    public static int segmentCounter = 0;
    public int segmentId;

    /** 
     * Constructor for the Segment class.
     */
    public Segment() {
        segmentCounter += 1;
        segmentId = segmentCounter;
    }

    /**
     * Gets the ID for the segment.
     * @return The segment ID.
     */
    public int getSegmentId() {
        return segmentId;
    }

    /**
     * Gets the type of the segment.
     * @return 
     */
    public SegmentType getSegmentType() {
        return null;
    }

    /**
     * Gets the kilometre location where the segment finishes within the stage.
     * @return
     */
    public Double getLocation() {
        return null;
    }
}