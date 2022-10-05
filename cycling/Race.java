package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;

/**
 * The Race class stores all the information related to a race, and stores an arraylist of the stages for the race.
 */
public class Race implements Serializable {
    public static int raceCounter = 0;
    private int raceId;
    private String name;
    private String description;
    private int numberOfStages;
    private double totalLength;
    ArrayList<Stage> arrayListOfStages = new ArrayList<>();

    /**
     * Constructor for the Race class.
     * @param name The name of the race.
     * @param description The description of the race.
     */
    public Race(String name, String description) {
        this.name = name;
        this.description = description;
        raceCounter += 1;
        raceId = raceCounter;
    }

    /**
     * Gets the ID for the race.
     * @return The race ID.
     */
    public int getRaceId() {
        return raceId;
    }

    /**
     * Gets the name of the race.
     * @return The race name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the race.
     * @return The description of the race.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the number of stages that are in the race.
     * @return The number of stages in the race.
     */
    public int getNumberOfStages() {
        numberOfStages = arrayListOfStages.size();
        return numberOfStages;
    }

    /**
     * Gets the total length for a race in kilometres.
     * @return The total length of the race.
     */
    public double getTotalLength() {
        for (Stage stage:arrayListOfStages) {
            totalLength += stage.getStageLength();
        }return totalLength;
    }

    /**
     * Gets all the stages in the race.
     * @return A list of the stages that belong to the race.
     */
    public ArrayList<Stage> getStages() {
        return arrayListOfStages;
    }

    /**
     * Creates a new stage and adds it to the race.
     * @param raceId The ID of the race which the stage is added to.
     * @param stageName The name for the stage.
     * @param description A description for the stage.
     * @param length The length of the stage in kilometres.
     * @param startTime The date and time in which the stage will be raced.
     * @param type The type of the stage.
     */
    public int createStage(int raceId, String stageName, String description, double length,
                            LocalDateTime startTime, StageType type) {
        Stage newStage = new Stage(raceId, stageName, description, length, startTime, type);
        arrayListOfStages.add(newStage);
        return newStage.getStageId();
    }

    /**
     * Removes a stage from a race and all its related data.
     * @param stageId The ID of the stage being removed.
     */
    public void removeStage(int stageId) {
        for (Stage i:arrayListOfStages){
            if ((i.getStageId()) == stageId){
                arrayListOfStages.remove(i);
                break;
            }
        }
    }

    /**
     * Orders the stages based on which stage starts soonest to latest.
     * @return A list of stage ID's ordered by when they start.
     */
    public ArrayList<Integer> getRaceStages(){
        ArrayList<LocalDateTime> arrayListOfStageStartDateTimes = new ArrayList<>();
        for (Stage stage:arrayListOfStages) {
            arrayListOfStageStartDateTimes.add(stage.getStartTime());
        }
        Collections.sort(arrayListOfStageStartDateTimes);
        ArrayList<Integer> arrayListOfSortedStageIDs = new ArrayList<>();
        for (LocalDateTime i:arrayListOfStageStartDateTimes){
            for (Stage k:arrayListOfStages){
                if (i.equals(k.getStartTime())){
                    if (!arrayListOfSortedStageIDs.contains(k.getStageId())){
                        arrayListOfSortedStageIDs.add(k.getStageId());
                    }
                }
            }
        }
        return arrayListOfSortedStageIDs;
    }
}