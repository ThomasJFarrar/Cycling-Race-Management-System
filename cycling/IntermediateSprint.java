package cycling;

/**
 * The IntermediateSprint class is a child class of segment and stores all the data about an 
 * intermediate sprint segment in a stage.
 */
public class IntermediateSprint extends Segment {
    private double location;
    private SegmentType type = SegmentType.SPRINT;
    private int stageId;

    /**
     * Constructor for the IntermediateSprint class.
     * @param stageId The ID of the stage to which the intermediate sprint segment is being added.
     * @param location The kilometre location where the intermediate sprint finishes 
     *        within the stage.
     * @param type The type of the segment.
     */
    public IntermediateSprint(int stageId, double location) {
        this.location = location;
        this.stageId = stageId;
    }

    /**
     * Gets the type of the segment.
     * @return The segment type.
     */
    public SegmentType getSegmentType() {
        return type;
    }

    /**
     * Gets the kilometre location where the intermediate sprint finishes within the stage.
     * @return The kilometre location where the intermediate sprint finishes within the stage.
     */
    public Double getLocation() {
        return location;
    }
}