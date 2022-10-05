package cycling;

import java.util.ArrayList;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.Duration;
import java.util.Hashtable;
import cycling.StageType;
import java.util.Collections;
import java.io.Serializable;

/**
 * The Result class holds all the information for results, points, and works out the elapsed time and adjusted elapsed times.
 */
public class Result implements Serializable{
    private int stageId;
    private int riderId;
    private LocalTime[] checkpoints;

    /**
     * Constructor for the result class.
     * @param stageId The ID of the stage the result refers to.
     * @param riderId The ID of the rider the result is for.
     * @param checkpoints An array of times at which the rider reached each of the segments of the stage, including the start time and the finish line.
     */
    public Result(int stageId, int riderId, LocalTime... checkpoints) {
        this.stageId = stageId;
        this.riderId = riderId;
        this.checkpoints = checkpoints;
    }

    /**
     * Gets the stage ID for the result.
     * @return The stage ID.
     */
    public int getStageId(){
        return stageId;
    }

    /**
     * Gets the rider ID for the result.
     * @return The rider ID.
     */
    public int getRiderId(){
        return riderId;
    }

    /**
     * Gets the array of times at which the rider reached each of the segments of the stage with the start time and the finish line, for the result.
     * @return An array of times.
     */
    public LocalTime[] getCheckpoints() {
        return checkpoints;
    }

    /**
     * Gets the finish time from the array of times the rider started, finished and reached the segments of the stage.
     * @return The finish time.
     */
    public LocalTime getFinishTime() {
        return checkpoints[checkpoints.length -1];
    }

    /**
     * Gets all the times at which the rider reached the segments of the stage, not including the start and finish time for a result.
     * @return An array of times which the rider reached the segments of the stage.
     */
    public LocalTime[] getCheckpointsWithoutStartAndEnd() {
        ArrayList<LocalTime> arrayListOfCheckpointsWithoutStartAndEnd = new ArrayList<LocalTime>();
        for (int i = 1; i < checkpoints.length; i++) {
            arrayListOfCheckpointsWithoutStartAndEnd.add(checkpoints[i]);
        }
        LocalTime[] checkpointsWithoutStartAndEnd = arrayListOfCheckpointsWithoutStartAndEnd.toArray(new LocalTime[arrayListOfCheckpointsWithoutStartAndEnd.size()]);
        return checkpointsWithoutStartAndEnd;
    }

    /**
     * Gets the amount of time between the start time and finish time of a result.
     * @return the elapsed time for a result
     */
    public LocalTime getElapsedTime(){
        Duration durationBetween = Duration.between(checkpoints[0], checkpoints[checkpoints.length-1]); 
        long elapsedTimeInNanoseconds = durationBetween.toNanos();
        LocalTime elapsedTime = LocalTime.ofNanoOfDay(elapsedTimeInNanoseconds);
        return elapsedTime;
    }

    /**
     * Gets the adjusted elapsed time for a rider in a stage, checking if another rider finished within 1 second of another and adjusting the time to be the smallest of both.
     * @param arrayListOfResults All rider results for a stage.
     * @return The adjusted elapsed time for the rider in a stage. Returns an empty array if there is no result for the rider in the stage.
     */
    public LocalTime getAdjustedElapsedTime(ArrayList<Result> arrayListOfResults){
        ArrayList<LocalTime> arrayListOfElapsedTimesForStage = new ArrayList<LocalTime>();
        ArrayList<Integer> arrayListOfRiderIds = new ArrayList<Integer>();
        for (int counter = 0; counter < arrayListOfResults.size(); counter++) {
            arrayListOfElapsedTimesForStage.add((arrayListOfResults.get(counter)).getElapsedTime());
            arrayListOfRiderIds.add((arrayListOfResults.get(counter)).getRiderId());
        }
        ArrayList<Integer> arrayListOfIndexes = new ArrayList<>();
        ArrayList<Integer> arrayListOfOrderedRiderIDs = new ArrayList<>();

        ArrayList<LocalTime> arrayListOfElapsedTimesForStageOld = new ArrayList<>(arrayListOfElapsedTimesForStage);
        Collections.sort(arrayListOfElapsedTimesForStage);
        for (LocalTime elapsedT: arrayListOfElapsedTimesForStage){
            arrayListOfIndexes.add(arrayListOfElapsedTimesForStageOld.indexOf(elapsedT));
        }for (int index:arrayListOfIndexes){
            arrayListOfOrderedRiderIDs.add(arrayListOfRiderIds.get(index));
        }

        for (int i = 0; i < (arrayListOfElapsedTimesForStage.size()-1); i++) {
            if (arrayListOfElapsedTimesForStage.get(i).until(arrayListOfElapsedTimesForStage.get(i+1), ChronoUnit.SECONDS)<60){
            //if ((arrayListOfElapsedTimesForStage.get(i).plusNanos(1000000000).compareTo(arrayListOfElapsedTimesForStage.get(i+1))>0)) { //If the time is less than 1000000000 nanoseconds behind the next time
                arrayListOfElapsedTimesForStage.set(i+1, arrayListOfElapsedTimesForStage.get(i)); //Adjust time
            }
        }
        

        for (int q = 0; q < arrayListOfElapsedTimesForStage.size(); q++) {
            //if ((arrayListOfRiderIds.get(q)).equals(this.riderId)) {
            if ((arrayListOfOrderedRiderIDs.get(q)).equals(this.riderId)) {
                //int idindex = arrayListOfOrderedRiderIDs.indexOf(q);
                LocalTime adjustedElapsedTimeForRider = arrayListOfElapsedTimesForStage.get(q);
                return adjustedElapsedTimeForRider;
            }
        }return LocalTime.of(0,0,0,0);
    }
}