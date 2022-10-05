package cycling;

import java.security.Identity;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Hashtable;
import java.lang.reflect.Array;
import java.io.Serializable;

/**
 * The Stage class stores all the information related to a stage, and stores arraylists of results and segments for the given stage.
 */
public class Stage implements Serializable{
    public static int stageCounter = 0;
    private int stageId;//unique stage identifier
    private int raceId;
    private String stageName;//name of stage
    private String description;//description of stage
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private String state = "Not waiting on results";
    ArrayList<Result> arrayListOfResults = new ArrayList<>();//arraylist of all rider objects for given race
    ArrayList<Segment> arrayListOfSegments = new ArrayList<>();

    
    /**
     * Constructor for the Stage class.
     * @param raceId The ID of the race this stage object belongs to.
     * @param description The description of the stage.
     * @param length The length of the stage in kilometres.
     * @param startTime The date and time in which the stage will be raced.
     * @param type The type of the stage. This is used to determine the amount of points given to the winner.
     */
    public Stage(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type) {
        this.raceId = raceId;
        this.stageName = stageName;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        stageCounter+=1;
        stageId = stageCounter;
    }

    /**
     * Creates and returns a Hashtable dictionary with keys representing each rider in the stage and 
     * their respective values representing their Sprinter's Classification points in the stage. 
     * Values for all keys are initialised as 0 here.
     * @return The default starter Hashtable of rider keys each with a value of 0.
     */
    public Hashtable<String, Integer> createRiderSprintersPointsDictionary(){
        Hashtable<String, Integer> riderSprintersPointsDictionary = new Hashtable<String, Integer>();
        for (Result result:arrayListOfResults){
            if (!riderSprintersPointsDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                riderSprintersPointsDictionary.put("Rider "+String.valueOf(result.getRiderId()),0);
            }
        }
        return riderSprintersPointsDictionary;
    }

    /**
     * Creates and returns a Hashtable dictionary with keys representing each rider in the stage and 
     * their respective values representing their KOM Classification points in the stage. 
     * Values for all keys are initialised as 0 here.
     * @return The default starter Hashtable of rider keys each with a value of 0.
     */
    public Hashtable<String, Integer> createRiderKOMPointsDictionary(){
        Hashtable<String, Integer> riderKOMPointsDictionary = new Hashtable<String, Integer>();
        for (Result result:arrayListOfResults){
            if (!riderKOMPointsDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                riderKOMPointsDictionary.put("Rider "+String.valueOf(result.getRiderId()),0);
            }
        }
        return riderKOMPointsDictionary;
    }
    
    /**
     * Gets the length for the stage.
     * @return The stage length in kilometres.
     */
    public double getStageLength() {
        return length;
    }

    /**
     * Gets the ID for the stage.
     * @return The stage ID.
     */
    public int getStageId(){
        return stageId;
    }

    /**
     * Gets the ID for the race the stage object belongs to.
     * @return The race ID the stage object belongs to.
     */
    public int getRaceId(){
        return raceId;
    }

    /**
     * Gets the stage name.
     * @return The stage name.
     */
    public String getStageName(){
        return stageName;
    }

    /**
     * Gets the start date and time in which the stage will be raced.
     * @return The start date and time in which the stage will be raced..
     */
    public LocalDateTime getStartTime(){
        return startTime;
    }

    /**
     * Gets the stage description.
     * @return The stage description.
     */
    public String getDescription(){
        return description;
    }

    /**
     * Gets the number of result objects (held in the arraylist of results) within the stage.
     * @return The number of result objects (held in the arraylist of results) within the stage.
     */
    public int getNumberOfResults(){
        return arrayListOfResults.size();
    }

    /**
     * Gets the number of Result objects (held in the arraylist of results) within the stage.
     * @return The number of result objects (held in the arraylist of results) within the stage.
     */
    public ArrayList<Result> getArrayListOfResults(){
        return arrayListOfResults;
    }

    /**
     * Retrieves the list of segment (mountains and sprints) IDs for the Segment objects of the stage
     * @return The list of segment IDs ordered (from first to last) by their location in the stage.
     */
    public ArrayList<Integer> getStageSegments(){
        ArrayList<Double> arrayListOfSegmentLocations = new ArrayList<>();
        for (Segment segment:arrayListOfSegments){
            arrayListOfSegmentLocations.add(segment.getLocation());
        }
        Collections.sort(arrayListOfSegmentLocations);
        ArrayList<Integer> arrayListOfSegmentIDsSortedByLocation = new ArrayList<>();
        ArrayList<Double> oldArrayListOfSegmentLocations = new ArrayList<>();
        ArrayList<Integer> tempArrayOfIndexes = new ArrayList<>();
        for (Segment segment:arrayListOfSegments){
            oldArrayListOfSegmentLocations.add(segment.getLocation());
        }
        for (Double segmentLocation:arrayListOfSegmentLocations){
            int locationIndex = oldArrayListOfSegmentLocations.indexOf(segmentLocation);
            tempArrayOfIndexes.add(locationIndex);
        }
        for (int index:tempArrayOfIndexes){
            Segment exampleSegment = arrayListOfSegments.get(index);
            int id = exampleSegment.getSegmentId();
            arrayListOfSegmentIDsSortedByLocation.add(id);
        }

        return arrayListOfSegmentIDsSortedByLocation;
    }

    /**
     * Concludes the preparation of a stage. After conclusion, the stage's state
	 * should be "waiting for results".
     */
    public void concludeStagePreparation(){
        state = "waiting for results";

    }

    /**
     * Gets the state of the stage.
     * @return The state of the stage.
     */
    public String getStageState(){
        return state;
    }

    /**
     * Gets the type of the stage.
     * @return The type of the stage.
     */
    public StageType getStageType(){
        return type;
    }

    /**
     * Adds a climb segment to a stage.
     * @param stageId The ID of the stage to which the climb segment is being added.
     * @param location The kilometre location where the climb finishes within the stage.
     * @param type The category of the climb - {@link SegmentType#C4}, {@link SegmentType#C3}, 
     *                                         {@link SegmentType#C2}, {@link SegmentType#C1},
     *                                       or {@link SegmentType#HC}
     * @param averageGradient The average gradient for the climb.
     * @param length The length of the climb in kilometres.
     * @return The ID of the Segment object created.
     * 
     */
    public int addCategorizedClimbToStage (int stageId, Double location, SegmentType type, Double averageGradient, Double length) {
        Double locationnew = location;
        SegmentType typenew = type;
        Double averageGradientnew = averageGradient;
        Double lengthnew = length;
        CategorizedClimb newClimb = new CategorizedClimb(stageId, locationnew, typenew, averageGradientnew, lengthnew);
        arrayListOfSegments.add(newClimb);
        return newClimb.getSegmentId();
    }

    /**
     * Adds an intermediate sprint to a stage.
     * @param stageId The ID of the stage to which the intermediate sprint segment is being added.
     * @param location The kilometre location where the intermediate sprint finishes within the stage.
     * @return The ID of the Segment object created.
     */
    public int addIntermediateSprintToStage (int stageId, double location) {
        double locationnew = location;
        IntermediateSprint newSprint = new IntermediateSprint(stageId, locationnew);
        arrayListOfSegments.add(newSprint);
        return newSprint.getSegmentId();
    }

    /**
     * Removes a segment from a stage.
     * @param segmentid The ID of the Segment object to be removed from the arraylist of segments within the stage.
     */
    public void removeSegment(int segmentid){
        for (Segment i: arrayListOfSegments){
            if (i.getSegmentId()==(segmentid)){
                arrayListOfSegments.remove(i);
                break;
            }
        }
    }

    /**
     * Record the times of a rider in a stage.
     * @param stageId The ID of the stage the result refers to.
     * @param riderId The ID of the rider the result refers to.
     * @param checkpoints An array of times at which the rider reached each of the
	 *                    segments of the stage, including the start time and the
	 *                    finish line.
     */
    public void registerRiderResultsInStage(int stageId, int riderId, LocalTime[] checkpoints) {
        int stageIdnew = stageId;
        int riderIdnew = riderId;
        LocalTime[] checkpointsnew = checkpoints;
        Result newResults = new Result(stageIdnew, riderIdnew, checkpointsnew);
        arrayListOfResults.add(newResults);
    }

    /**
     * Get the times of a rider in a stage.
     * @param riderId The ID of the rider a given result refers to.
     * @return The array of times at which the rider reached each of the segments of
	 *         the stage and the total elapsed time. The elapsed time is the
	 *         difference between the finish time and the start time.
     * 
     *         An empty array is returned if there are no results registered for the 
     *         given stage; i.e, the array list of result objects of the stage object
     *         is empty.
     */
    public ArrayList<LocalTime> getRiderResultsInStage(int riderId){
        for (Result i:arrayListOfResults){
            if ((i.getRiderId())==(riderId)){
                LocalTime[] checkPointsArray = i.getCheckpoints();
                ArrayList<LocalTime> checkpointsArrayList = new ArrayList<>();
                for (LocalTime checkpoint:checkPointsArray){
                    checkpointsArrayList.add(checkpoint);
                }
                checkpointsArrayList.add(i.getElapsedTime());
                return checkpointsArrayList;
            }
        }return new ArrayList<LocalTime>();
    }

    /**
     * Gets the adjusted elapsed time for a rider in the stage.
	 * @param riderId The ID of the rider a given result refers to.
	 * @return The adjusted elapsed time for the rider in the stage. 
     * 
     *         Null if there is no result registered for 
     *         the rider in the stage.
     */
    public LocalTime getRiderAdjustedElapsedTimeInStage(int riderId) {
        for (Result i:arrayListOfResults){
            if ((i.getRiderId())==(riderId)){
                return i.getAdjustedElapsedTime(arrayListOfResults);
            }
        }return null;
    }

    /**
     * Gets the elapsed time for a rider in the stage.
	 * @param riderId The ID of the rider a given result refers to.
	 * @return The elapsed time for the rider in the stage. 
     * 
     *         Null if there is no result registered for 
     *         the rider in the stage.
     */
    public LocalTime getRiderElapsedTimeInStage(int riderId){
        for (Result i:arrayListOfResults){
            if ((i.getRiderId())==(riderId)){
                return i.getElapsedTime();
            }
        }return null;
    }


    /**
     * Removes the stage results from the rider.
	 * @param riderId The ID of the rider a given result refers to.
     */
    public void deleteRiderResultsInStage(int riderId) {
        for (Result i:arrayListOfResults){
            if ((i.getRiderId())==(riderId)){
                arrayListOfResults.remove(i);
                break;
            }
        }
    }

    /**
     * Get the riders finished position in a a stage.
     * @param allRiders An arraylist of all riders created in the cycling portal for the current race/stage
	 * @return A list of riders' ID sorted by their elapsed time. 
     * 
     *         An empty list if there is no result for the stage.
     */
    public ArrayList<Integer> getRidersRankInStage (ArrayList<Rider> allRiders) {
        ArrayList<LocalTime> arrayListOfRiderElapsedTimes = new ArrayList<>();
        if (arrayListOfResults.isEmpty()){
            return (new ArrayList<Integer>());
        }
        for(Result j:arrayListOfResults){
            arrayListOfRiderElapsedTimes.add(j.getElapsedTime());
        }
        Collections.sort(arrayListOfRiderElapsedTimes);
        ArrayList<Integer> arrayListOfSortedRiderIDs = new ArrayList<>();
        for(LocalTime z:arrayListOfRiderElapsedTimes){
            for (Result k:arrayListOfResults){
                if (z.equals(k.getElapsedTime())){
                    arrayListOfSortedRiderIDs.add(k.getRiderId());
                }
            }
        }
        return arrayListOfSortedRiderIDs;
    }

    /**
     * Get the adjusted elapsed times of riders in a stage.
     * @param allRiders An arraylist of all riders created in the cycling portal for the current race/stage
	 * @return The ranked list of adjusted elapsed times sorted by their finish time. 
     * 
     *         An empty list if there is no result for the stage.
     */
    public ArrayList<LocalTime> getRankedAdjustedElapsedTimesInStage(ArrayList<Rider> allRiders) {
        if (arrayListOfResults.isEmpty()){
            return (new ArrayList<LocalTime>());
        }
        ArrayList<LocalTime> arrayListOfRiderFinishTimes = new ArrayList<>();
        
        for(Result j:arrayListOfResults){
            arrayListOfRiderFinishTimes.add(j.getFinishTime());
        }
        Collections.sort(arrayListOfRiderFinishTimes);
        ArrayList<LocalTime> arrayListOfSortedAdjustedElapsedTimes = new ArrayList<>();
        for(LocalTime z:arrayListOfRiderFinishTimes){
            for (Result k:arrayListOfResults){
                if (z.equals(k.getFinishTime())){
                    arrayListOfSortedAdjustedElapsedTimes.add(k.getAdjustedElapsedTime(arrayListOfResults));
                }
            }
        }return arrayListOfSortedAdjustedElapsedTimes;       
    }

    /**
     * Get the number of points obtained by each rider in a stage.
     * @param allRiders An arraylist of all riders created in the cycling portal for the current race/stage
	 * @return @return The ranked list of points each rider received in the stage, sorted
	 *         by their elapsed time.
     * 
     *         An empty list if there is no result for the stage.
     */
    public ArrayList<Integer> getRidersPointsInStage(ArrayList<Rider> allRiders) {
        if (arrayListOfResults.isEmpty()){
            return (new ArrayList<Integer>());
        }
        ArrayList<LocalTime> arrayListOfRiderElapsedTimes = new ArrayList<>();
        ArrayList<LocalTime> arrayListOfRiderFinishTimes = new ArrayList<>();

        for(Result j:arrayListOfResults){
            arrayListOfRiderElapsedTimes.add(j.getElapsedTime());
        }
        for(Result j:arrayListOfResults){
            arrayListOfRiderFinishTimes.add(j.getFinishTime());
        }

        Collections.sort(arrayListOfRiderElapsedTimes);
        //time trial handler
        if (this.type.equals(StageType.TT)){
            ArrayList<Integer> arrayListOfSortedRiderIDs = new ArrayList<>();
            for(LocalTime z:arrayListOfRiderElapsedTimes){
                for (Result k:arrayListOfResults){
                    if (z.equals(k.getElapsedTime())){
                        arrayListOfSortedRiderIDs.add(k.getRiderId());
                    }
                }
            }
            Hashtable <String, Integer> riderSprintersPointsDictionary = this.createRiderSprintersPointsDictionary();
            for (int id: arrayListOfSortedRiderIDs){
                for (Result result: arrayListOfResults){
                    if(result.getRiderId()==(id)){
                        int currentDictionaryValue = riderSprintersPointsDictionary.get("Rider "+String.valueOf(id));
                        if ((arrayListOfSortedRiderIDs.indexOf(id)+1)>15||(arrayListOfSortedRiderIDs.indexOf(id)+1)<1){
                            break;
                        }else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==1){
                            int updatedDictionaryValue = currentDictionaryValue+=20;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==2){
                            int updatedDictionaryValue = currentDictionaryValue+=17;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==3){
                            int updatedDictionaryValue = currentDictionaryValue+=15;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==4){
                            int updatedDictionaryValue = currentDictionaryValue+=13;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==5){
                            int updatedDictionaryValue = currentDictionaryValue+=11;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==6){
                            int updatedDictionaryValue = currentDictionaryValue+=10;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==7){
                            int updatedDictionaryValue = currentDictionaryValue+=9;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==8){
                            int updatedDictionaryValue = currentDictionaryValue+=8;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==9){
                            int updatedDictionaryValue = currentDictionaryValue+=7;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==10){
                            int updatedDictionaryValue = currentDictionaryValue+=6;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==11){
                            int updatedDictionaryValue = currentDictionaryValue+=5;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==12){
                            int updatedDictionaryValue = currentDictionaryValue+=4;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==13){
                            int updatedDictionaryValue = currentDictionaryValue+=3;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==14){
                            int updatedDictionaryValue = currentDictionaryValue+=2;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                        else if((arrayListOfSortedRiderIDs.indexOf(id)+1)==15){
                            int updatedDictionaryValue = currentDictionaryValue+=1;
                            riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                        }
                    }
                }
            }ArrayList<Integer> arrayListOfPointsCorrespondingToRiderIDsSortedByElapsedTime = new ArrayList<>();
            for (int id:arrayListOfSortedRiderIDs){
                arrayListOfPointsCorrespondingToRiderIDsSortedByElapsedTime.add(riderSprintersPointsDictionary.get("Rider "+String.valueOf(id)));
            }
            return arrayListOfPointsCorrespondingToRiderIDsSortedByElapsedTime; 
        }
        //finish position handler
        Collections.sort(arrayListOfRiderFinishTimes);
        
        Hashtable <String, Integer> riderSprintersPointsDictionary = this.createRiderSprintersPointsDictionary();
        for (Result result:arrayListOfResults){
            int givenRiderId = result.getRiderId();
            LocalTime givenFinishTime = result.getFinishTime();
            int positionOfGivenRiderInStage = arrayListOfRiderFinishTimes.indexOf(givenFinishTime);
            int currentDictionaryValue = riderSprintersPointsDictionary.get("Rider "+String.valueOf(givenRiderId));
            if (this.type.equals(StageType.FLAT)){
                if (positionOfGivenRiderInStage+1>15||positionOfGivenRiderInStage+1<1){
                    break;
                }else if(positionOfGivenRiderInStage+1==1){
                    int updatedDictionaryValue = currentDictionaryValue+=50;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==2){
                    int updatedDictionaryValue = currentDictionaryValue+=30;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==3){
                    int updatedDictionaryValue = currentDictionaryValue+=20;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==4){
                    int updatedDictionaryValue = currentDictionaryValue+=18;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==5){
                    int updatedDictionaryValue = currentDictionaryValue+=16;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==6){
                    int updatedDictionaryValue = currentDictionaryValue+=14;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==7){
                    int updatedDictionaryValue = currentDictionaryValue+=12;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==8){
                    int updatedDictionaryValue = currentDictionaryValue+=10;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==9){
                    int updatedDictionaryValue = currentDictionaryValue+=8;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==10){
                    int updatedDictionaryValue = currentDictionaryValue+=7;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==11){
                    int updatedDictionaryValue = currentDictionaryValue+=6;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==12){
                    int updatedDictionaryValue = currentDictionaryValue+=5;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==13){
                    int updatedDictionaryValue = currentDictionaryValue+=4;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==14){
                    int updatedDictionaryValue = currentDictionaryValue+=3;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==15){
                    int updatedDictionaryValue = currentDictionaryValue+=2;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
            }
            else if (this.type.equals(StageType.MEDIUM_MOUNTAIN)){
                if (positionOfGivenRiderInStage+1>15||positionOfGivenRiderInStage+1<1){
                    break;
                }else if(positionOfGivenRiderInStage+1==1){
                    int updatedDictionaryValue = currentDictionaryValue+=30;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==2){
                    int updatedDictionaryValue = currentDictionaryValue+=25;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==3){
                    int updatedDictionaryValue = currentDictionaryValue+=22;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==4){
                    int updatedDictionaryValue = currentDictionaryValue+=19;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==5){
                    int updatedDictionaryValue = currentDictionaryValue+=17;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==6){
                    int updatedDictionaryValue = currentDictionaryValue+=15;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==7){
                    int updatedDictionaryValue = currentDictionaryValue+=13;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==8){
                    int updatedDictionaryValue = currentDictionaryValue+=11;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==9){
                    int updatedDictionaryValue = currentDictionaryValue+=9;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==10){
                    int updatedDictionaryValue = currentDictionaryValue+=7;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==11){
                    int updatedDictionaryValue = currentDictionaryValue+=6;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==12){
                    int updatedDictionaryValue = currentDictionaryValue+=5;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==13){
                    int updatedDictionaryValue = currentDictionaryValue+=4;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==14){
                    int updatedDictionaryValue = currentDictionaryValue+=3;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==15){
                    int updatedDictionaryValue = currentDictionaryValue+=2;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
            }
            else if (this.type.equals(StageType.HIGH_MOUNTAIN)){
                if (positionOfGivenRiderInStage+1>15||positionOfGivenRiderInStage+1<1){
                    break;
                }else if(positionOfGivenRiderInStage+1==1){
                    int updatedDictionaryValue = currentDictionaryValue+=20;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==2){
                    int updatedDictionaryValue = currentDictionaryValue+=17;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==3){
                    int updatedDictionaryValue = currentDictionaryValue+=15;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==4){
                    int updatedDictionaryValue = currentDictionaryValue+=13;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==5){
                    int updatedDictionaryValue = currentDictionaryValue+=11;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==6){
                    int updatedDictionaryValue = currentDictionaryValue+=10;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==7){
                    int updatedDictionaryValue = currentDictionaryValue+=9;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==8){
                    int updatedDictionaryValue = currentDictionaryValue+=8;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==9){
                    int updatedDictionaryValue = currentDictionaryValue+=7;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==10){
                    int updatedDictionaryValue = currentDictionaryValue+=6;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==11){
                    int updatedDictionaryValue = currentDictionaryValue+=5;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==12){
                    int updatedDictionaryValue = currentDictionaryValue+=4;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==13){
                    int updatedDictionaryValue = currentDictionaryValue+=3;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==14){
                    int updatedDictionaryValue = currentDictionaryValue+=2;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
                else if(positionOfGivenRiderInStage+1==15){
                    int updatedDictionaryValue = currentDictionaryValue+=1;
                    riderSprintersPointsDictionary.put("Rider "+String.valueOf(givenRiderId), updatedDictionaryValue);
                }
            }
        }
        ArrayList<Integer> arrayListOfSortedRiderIDs = new ArrayList<>();
        for(LocalTime z:arrayListOfRiderElapsedTimes){
            for (Result k:arrayListOfResults){
                if (z.equals(k.getElapsedTime())){
                    arrayListOfSortedRiderIDs.add(k.getRiderId());
                }
            }
        }
        ArrayList<LocalTime[]> arrayListOfCheckpointALs = new ArrayList<>();
        for (int y:arrayListOfSortedRiderIDs){
            for (Result i:arrayListOfResults){
                if ((y)==(i.getRiderId())){
                    arrayListOfCheckpointALs.add(i.getCheckpointsWithoutStartAndEnd());
                }
            }
        }
        ArrayList<ArrayList<LocalTime>>arrayListOfSortedCheckpointsForEachRider = new ArrayList<>();
        for (int j=0; j<arrayListOfResults.size(); j++){
            ArrayList<LocalTime> arrayListOfGivenSubElements = new ArrayList<>();
            for (LocalTime[] i:arrayListOfCheckpointALs){
                if (j > i.length|| j==(i.length)){
                    break;
                }
                arrayListOfGivenSubElements.add(i[j]);
            }
            Collections.sort(arrayListOfGivenSubElements);
            arrayListOfSortedCheckpointsForEachRider.add(arrayListOfGivenSubElements);
        }
        for (ArrayList<LocalTime> k:arrayListOfSortedCheckpointsForEachRider){
            int indexofelement = arrayListOfSortedCheckpointsForEachRider.indexOf(k);//final
            ArrayList<LocalTime> arrayListOfGivenSubElements = new ArrayList<>();
            for (LocalTime[] i:arrayListOfCheckpointALs){
                arrayListOfGivenSubElements.add((LocalTime)Array.get(i, indexofelement));
            }
            ArrayList<Integer> arrayListOfIndexes = new ArrayList<>();
            for (LocalTime x:k){
                for (LocalTime z:arrayListOfGivenSubElements){
                    if (x.equals(z)){
                        int indexofelement2 = arrayListOfGivenSubElements.indexOf(z);//final
                        arrayListOfIndexes.add(indexofelement2);
                    }
                }    
            }
            outerloop:
            for (int item:arrayListOfIndexes){
                for (int id:arrayListOfSortedRiderIDs){
                    if (arrayListOfSortedRiderIDs.indexOf(id) == (item)){
                        int currentDictionaryValue = riderSprintersPointsDictionary.get("Rider "+String.valueOf(id));
                        if (indexofelement==arrayListOfSegments.size()||indexofelement>arrayListOfSegments.size()){
                            break outerloop;
                        }
                        if (arrayListOfSegments.get(indexofelement).getSegmentType().equals(SegmentType.SPRINT)){
                            if (arrayListOfIndexes.indexOf(item)+1>15||arrayListOfIndexes.indexOf(item)+1<1){
                                break;
                            }else if(arrayListOfIndexes.indexOf(item)+1==1){
                                int updatedDictionaryValue = currentDictionaryValue+=20;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==2){
                                int updatedDictionaryValue = currentDictionaryValue+=17;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==3){
                                int updatedDictionaryValue = currentDictionaryValue+=15;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==4){
                                int updatedDictionaryValue = currentDictionaryValue+=13;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==5){
                                int updatedDictionaryValue = currentDictionaryValue+=11;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==6){
                                int updatedDictionaryValue = currentDictionaryValue+=10;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==7){
                                int updatedDictionaryValue = currentDictionaryValue+=9;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==8){
                                int updatedDictionaryValue = currentDictionaryValue+=8;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==9){
                                int updatedDictionaryValue = currentDictionaryValue+=7;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==10){
                                int updatedDictionaryValue = currentDictionaryValue+=6;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==11){
                                int updatedDictionaryValue = currentDictionaryValue+=5;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==12){
                                int updatedDictionaryValue = currentDictionaryValue+=4;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==13){
                                int updatedDictionaryValue = currentDictionaryValue+=3;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==14){
                                int updatedDictionaryValue = currentDictionaryValue+=2;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                            else if(arrayListOfIndexes.indexOf(item)+1==15){
                                int updatedDictionaryValue = currentDictionaryValue+=1;
                                riderSprintersPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                        }
                    }
                }
            }
        }
        ArrayList<Integer> arrayListOfPointsCorrespondingToRiderIDsSortedByElapsedTime = new ArrayList<>();
        for (int id:arrayListOfSortedRiderIDs){
            arrayListOfPointsCorrespondingToRiderIDsSortedByElapsedTime.add(riderSprintersPointsDictionary.get("Rider "+String.valueOf(id)));
        }
        return arrayListOfPointsCorrespondingToRiderIDsSortedByElapsedTime;
    }

    /**
     * Get the number of points obtained by each rider in a stage.
     * @param allRiders An arraylist of all riders created in the cycling portal for the current race/stage
	 * @return @return The ranked list of mountain points each riders received in the stage,
	 *         sorted by their finish time.
     * 
     *         An empty list if there is no result for the stage.
     */
    public ArrayList<Integer> getRidersMountainPointsInStage(ArrayList<Rider> allRiders) {
        if (arrayListOfResults.isEmpty()){
            return (new ArrayList<Integer>());
        }
        ArrayList<LocalTime> arrayListOfRiderFinishTimes = new ArrayList<>();

        for(Result j:arrayListOfResults){
            arrayListOfRiderFinishTimes.add(j.getFinishTime());
        }

        Collections.sort(arrayListOfRiderFinishTimes);
        
        ArrayList<Integer> arrayListOfSortedRiderIDs = new ArrayList<>();
        for(LocalTime z:arrayListOfRiderFinishTimes){
            for (Result k:arrayListOfResults){
                if (z.equals(k.getFinishTime())){
                    arrayListOfSortedRiderIDs.add(k.getRiderId());
                }
            }
        }
        ArrayList<LocalTime[]> arrayListOfCheckpointALs = new ArrayList<>();
        for (int y:arrayListOfSortedRiderIDs){
            for (Result i:arrayListOfResults){
                if ((y)==(i.getRiderId())){
                    arrayListOfCheckpointALs.add(i.getCheckpointsWithoutStartAndEnd());
                }
            }
        }
        ArrayList<ArrayList<LocalTime>>arrayListOfSortedCheckpointsForEachRider = new ArrayList<>();
        for (int j=0; j<arrayListOfResults.size(); j++){
            ArrayList<LocalTime> arrayListOfGivenSubElements = new ArrayList<>();
            for (LocalTime[] i:arrayListOfCheckpointALs){
                if (j > i.length|| j==(i.length)){
                    break;
                }
                arrayListOfGivenSubElements.add(i[j]);
            }
            Collections.sort(arrayListOfGivenSubElements);
            arrayListOfSortedCheckpointsForEachRider.add(arrayListOfGivenSubElements);
        }
        Hashtable <String, Integer> riderKOMPointsDictionary = this.createRiderKOMPointsDictionary();
        for (ArrayList<LocalTime> k:arrayListOfSortedCheckpointsForEachRider){
            int indexofelement3 = arrayListOfSortedCheckpointsForEachRider.indexOf(k);
            ArrayList<LocalTime> arrayListOfGivenSubElements = new ArrayList<>();
            for (LocalTime[] i:arrayListOfCheckpointALs){
                arrayListOfGivenSubElements.add((LocalTime)Array.get(i, indexofelement3));
            }
            ArrayList<Integer> arrayListOfIndexes2 = new ArrayList<>();
            for (LocalTime x:k){
                for (LocalTime z:arrayListOfGivenSubElements){
                    if (x.equals(z)){
                        int indexofelement4 = arrayListOfGivenSubElements.indexOf(z);
                        arrayListOfIndexes2.add(indexofelement4);
                    }
                }    
            }
            outerloop:
            for (int indexitem:arrayListOfIndexes2){
                for (int id:arrayListOfSortedRiderIDs){
                    if (arrayListOfSortedRiderIDs.indexOf(id) == (indexitem)){
                        int currentDictionaryValue = riderKOMPointsDictionary.get("Rider "+String.valueOf(id));
                        if (indexofelement3==arrayListOfSegments.size()||indexofelement3>arrayListOfSegments.size()){
                            break outerloop;
                        }
                        if (arrayListOfSegments.get(indexofelement3).getSegmentType().equals(SegmentType.SPRINT)){
                            break outerloop;
                        }
                        if (arrayListOfSegments.get(indexofelement3).getSegmentType().equals(SegmentType.C4)){
                            if (arrayListOfIndexes2.indexOf(indexitem)+1==1){
                                int updatedDictionaryValue = currentDictionaryValue+=1;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1!=1){
                                int updatedDictionaryValue = currentDictionaryValue+=0;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                        }else if(arrayListOfSegments.get(indexofelement3).getSegmentType().equals(SegmentType.C3)){
                            if (arrayListOfIndexes2.indexOf(indexitem)+1==1){
                                int updatedDictionaryValue = currentDictionaryValue+=2;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==2){
                                int updatedDictionaryValue = currentDictionaryValue+=1;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1!=1){
                                int updatedDictionaryValue = currentDictionaryValue+=0;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                        }
                        else if(arrayListOfSegments.get(indexofelement3).getSegmentType().equals(SegmentType.C2)){
                            if (arrayListOfIndexes2.indexOf(indexitem)+1==1){
                                int updatedDictionaryValue = currentDictionaryValue+=5;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==2){
                                int updatedDictionaryValue = currentDictionaryValue+=3;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==3){
                                int updatedDictionaryValue = currentDictionaryValue+=2;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==4){
                                int updatedDictionaryValue = currentDictionaryValue+=1;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1!=1){
                                int updatedDictionaryValue = currentDictionaryValue+=0;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                        }else if(arrayListOfSegments.get(indexofelement3).getSegmentType().equals(SegmentType.C1)){
                            if (arrayListOfIndexes2.indexOf(indexitem)+1==1){
                                int updatedDictionaryValue = currentDictionaryValue+=10;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==2){
                                int updatedDictionaryValue = currentDictionaryValue+=8;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==3){
                                int updatedDictionaryValue = currentDictionaryValue+=6;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==4){
                                int updatedDictionaryValue = currentDictionaryValue+=4;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==5){
                                int updatedDictionaryValue = currentDictionaryValue+=2;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==6){
                                int updatedDictionaryValue = currentDictionaryValue+=1;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1!=1){
                                int updatedDictionaryValue = currentDictionaryValue+=0;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                        }
                        else if (arrayListOfSegments.get(indexofelement3).getSegmentType().equals(SegmentType.HC)){
                            if (arrayListOfIndexes2.indexOf(indexitem)+1==1){
                                int updatedDictionaryValue = currentDictionaryValue+=20;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==2){
                                int updatedDictionaryValue = currentDictionaryValue+=15;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==3){
                                int updatedDictionaryValue = currentDictionaryValue+=12;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==4){
                                int updatedDictionaryValue = currentDictionaryValue+=10;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==5){
                                int updatedDictionaryValue = currentDictionaryValue+=8;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==6){
                                int updatedDictionaryValue = currentDictionaryValue+=6;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==7){
                                int updatedDictionaryValue = currentDictionaryValue+=4;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1==8){
                                int updatedDictionaryValue = currentDictionaryValue+=2;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }else if(arrayListOfIndexes2.indexOf(indexitem)+1!=1){
                                int updatedDictionaryValue = currentDictionaryValue+=0;
                                riderKOMPointsDictionary.put("Rider "+String.valueOf(id), updatedDictionaryValue);
                            }
                        }
                    }
                }
            }
        }

        ArrayList<Integer> arrayListOfMountainPointsCorrespondingToRiderIDsSortedByFinishTime = new ArrayList<>();
        for (int id:arrayListOfSortedRiderIDs){
            arrayListOfMountainPointsCorrespondingToRiderIDsSortedByFinishTime.add(riderKOMPointsDictionary.get("Rider "+String.valueOf(id)));
        }
        return arrayListOfMountainPointsCorrespondingToRiderIDsSortedByFinishTime;
    }
}
