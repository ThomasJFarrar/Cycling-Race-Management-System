package cycling;

/**
 * The CategorizedClimb class is a child class of Segment and stores all the data about a 
 *  categorized climb segment in a stage.
 */
public class CategorizedClimb extends Segment {
    private Double location;
    private SegmentType type;
    private Double averageGradient;
    private Double length;
    private int stageId;

    /**
     * Constructor for the CategorizedClimb class.
     * @param stageId The ID if the stage to which the climb segment is being added.
     * @param location The kilometre location where the climb finishes within the stage.
     * @param type The category of the climb - {@link SegmentType#C4},
     * {@link SegmentType#C3}, {@link SegmentType#C2},
     * {@link SegmentType#C1}, or {@link SegmentType#HC}.
     * @param averageGradient The average gradient for the climb.
     * @param length The length of the climb in kilometres.
     */
    public CategorizedClimb(int stageId, Double location, SegmentType type, 
                            Double averageGradient, Double length) {
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
        this.stageId = stageId;
    }

    /**
     * Gets the type of segment for the categorized climb.
     * @return The segment type for the categorized climb.
     */
    public SegmentType getSegmentType() {
        return type;
    }

    /**
     * Gets the kilometre location where the climb finishes within the stage.
     * @return The kilometre location where the climb finishes within the stage.
     */
    public Double getLocation() {
        return location;
    }
}