package cycling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;


/**
 * CyclingPortal is a minimally compiling, but non-functioning implementor
 * of the CyclingPortalInterface interface.
 * 
 * @author Diogo Pacheco
 * @version 1.0
 *
 */
public class CyclingPortal implements CyclingPortalInterface {
	ArrayList<Race> arrayListOfRaces = new ArrayList<>();
	ArrayList<Team> arrayListOfTeams = new ArrayList<>();

	@Override
	public int[] getRaceIds() {
		if (arrayListOfRaces.size() == 0) {
			return new int[0];
		} else {
			ArrayList<Integer> arrayListOfRaceIDs = new ArrayList<>();
			for (Race race:arrayListOfRaces) {
				arrayListOfRaceIDs.add(race.getRaceId());
		}
		int[] arrayNew = new int[arrayListOfRaceIDs.size()];
		for (int i=0; i < arrayNew.length; i++) {
			arrayNew[i] = arrayListOfRaceIDs.get(i).intValue();
		}
		return arrayNew;
		}
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		Race newRace = new Race(name, description);
		for (Race race:arrayListOfRaces){
			if (race.getName().equals(name)){
				throw new IllegalNameException("The race name already exists in the platform");
			}
			if (name == null || name.isEmpty() || name.length() == 0 || name.length() > 30 || name.contains(" ")) {
				throw new InvalidNameException("Race name entered can't be empty, have more than 30 characters, or include spaces");
			}
		}
		arrayListOfRaces.add(newRace);
		return newRace.getRaceId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		for (Race race:arrayListOfRaces){
			if (race.getRaceId()==(raceId)){
				String concatenatedString = String.valueOf(race.getRaceId()) + " " + race.getName()
				+ " " + race.getDescription() +" "+ String.valueOf(race.getNumberOfStages())
				+ " "+ String.valueOf(race.getTotalLength());
				return concatenatedString;
			}
		}
		throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		int initialLength = arrayListOfRaces.size();
		for (Race race:arrayListOfRaces ){
			if (race.getRaceId() == (raceId)) {
				arrayListOfRaces.remove(race);
				break;
			}
		}
		if (arrayListOfRaces.size() == initialLength) {
			throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
		}
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		for (Race race:arrayListOfRaces){
			if (race.getRaceId()==(raceId)){
				int numberOfStages = race.getNumberOfStages();
				return numberOfStages;
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		for (Race race:arrayListOfRaces){
			if (race.getRaceId()==(raceId)){
				for (Stage s:race.arrayListOfStages){
					if (s.getStageName().equals(stageName)){
						throw new IllegalNameException("The stage name already exists in the platform");
					}
				}
				if (stageName==null || stageName.isEmpty()|| stageName.length()>30|| stageName.contains(" ")){
					throw new InvalidNameException("Name entered can't be empty, have more than 30 characters, or include spaces");
				}
				if (length<5){
					throw new InvalidLengthException("The length of the stage must be longer than 5km");
				}
				int stageId =race.createStage(raceId, stageName, description, length, startTime, type);
				return stageId;
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		for (Race race:arrayListOfRaces){
			if (race.getRaceId()==(raceId)){
				ArrayList<Integer> arrayListOfStageIDs = race.getRaceStages();
				//int[] array = arrayListOfStageIDs.toArray(new int[arrayListOfStageIDs.size()]);
				int[] arrayNew = new int[arrayListOfStageIDs.size()];
				for (int i=0; i < arrayNew.length; i++) {
					arrayNew[i] = arrayListOfStageIDs.get(i).intValue();
				}
				return arrayNew;
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
	}
	
	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					double stageLength = s.getStageLength();
					return stageLength;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		int initialLength=0;
		Race raceInQuestion = null;
		for (Race race: arrayListOfRaces){
			initialLength = race.arrayListOfStages.size();
			race.removeStage(stageId);
			raceInQuestion = race;
			break;
		}if (raceInQuestion.arrayListOfStages.size()==initialLength){
			throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
		}
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					if (location>s.getStageLength()){
						throw new InvalidLocationException("The entered finish location is not within the stage length");
					}
					if (s.getStageState().equals("waiting for results")){
						throw new InvalidStageStateException("Still waiting for results");
					}
					if (s.getStageType().equals(StageType.TT)){
						throw new InvalidStageTypeException("Time trial stages cannot contain any segments");
					}
					int climbId = s.addCategorizedClimbToStage(stageId, location, type, averageGradient, length);
					return climbId;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					if (location>s.getStageLength()){
						throw new InvalidLocationException("The entered finish location is not within the stage length");
					}
					if (s.getStageState().equals("waiting for results")){
						throw new InvalidStageStateException("Still waiting for results");
					}
					if (s.getStageType().equals(StageType.TT)){
						throw new InvalidStageTypeException("Time trial stages cannot contain any segments");
					}
					int sprintId = s.addIntermediateSprintToStage(stageId, location);
					return sprintId;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
		int initialLength =0;
		Stage stageInQuestion = null;
		outerloop:
		for (Race race: arrayListOfRaces){
			for (Stage i:race.arrayListOfStages){
				if (i.getStageState().equals("waiting for results")){
					throw new InvalidStageStateException("Still waiting for results");
				}
				initialLength= i.arrayListOfSegments.size();
				i.removeSegment(segmentId);
				stageInQuestion = i;
				break outerloop;
			}
		}if (stageInQuestion.arrayListOfSegments.size()==initialLength){
			throw new IDNotRecognisedException("The ID entered does not match to any segment in the system");
		}
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		int flag = 0;
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					if (s.getStageState().equals("waiting for results")){
						throw new InvalidStageStateException("Still waiting for results");
					}
					s.concludeStagePreparation();
					flag=1;
				}
			}
		}if (flag ==0 ){
			throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
		}
	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					ArrayList<Integer> arrayListOfSegmentIDs = s.getStageSegments();
					int[] arrayNew = new int[arrayListOfSegmentIDs.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = arrayListOfSegmentIDs.get(i).intValue();
					}
					return arrayNew;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		for (Team team:arrayListOfTeams) {
			if (team.getName().equals(name)) {
				throw new IllegalNameException("The team name already exists in the platform");
			}
			if (name == null || name.isEmpty() || name.length() == 0 || name.length() > 30 || name.contains(" ")) {
				throw new InvalidNameException("Team name entered can't be empty, have more than 30 characters, or include spaces");
			}
		}
		Team newTeam = new Team(name, description);
		arrayListOfTeams.add(newTeam);
		return newTeam.getTeamId();
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		int initialLength = arrayListOfTeams.size();
		for (Team team:arrayListOfTeams){
			if (team.getTeamId() == (teamId)){
				arrayListOfTeams.remove(team);
				break;
			}
		}
		if (arrayListOfTeams.size() == initialLength){
			throw new IDNotRecognisedException("The ID entered does not match to any team in the system");
		}
	}

	@Override
	public int[] getTeams() {
		if (arrayListOfTeams.size() == 0) {
			return new int[0];
		} else {
		ArrayList<Integer> arrayListOfTeamIDs = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			arrayListOfTeamIDs.add(team.getTeamId());
		}
		Integer[] array = arrayListOfTeamIDs.toArray(new Integer[arrayListOfTeamIDs.size()]);
		int[] arrayNew = new int[array.length];
		Arrays.setAll(arrayNew, i -> array[i]);
		return arrayNew;
		}
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		for (Team team:arrayListOfTeams){
			if (team.getTeamId() == (teamId)){
				ArrayList<Integer> arrayListOfRiderIDs = team.getTeamRiders();
				Integer[] array = arrayListOfRiderIDs.toArray(new Integer[arrayListOfRiderIDs.size()]);
				int[] arrayNew = new int[array.length];
				Arrays.setAll(arrayNew, i -> array[i]);
				return arrayNew;
			}
		}
		throw new IDNotRecognisedException("The ID entered does not match to any team in the system");
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		for (Team team:arrayListOfTeams){
			if (team.getTeamId() == (teamID)){
				if (name == null || yearOfBirth < 1900){
					throw new IllegalArgumentException("Name of rider cannot be empty and year of birth cannot be less than 1900");
				}
				int riderID = team.createRider(teamID, name, yearOfBirth);
				return riderID;
			}
		}
		throw new IDNotRecognisedException("The ID entered does not match to any team in the system");
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		int initialLength=0;
		Team teamInQuestion = null;
		outerloop:
		for (Team team: arrayListOfTeams){
			//initialLength = team.arrayListOfRiders.size();
			for (Rider rider: team.arrayListOfRiders){
				if (rider.getRiderId()==riderId){
					initialLength = team.arrayListOfRiders.size();
					team.removeRider(riderId);
					teamInQuestion = team;
					break outerloop;
				}
			}
		}for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				for (Result result: s.arrayListOfResults){
					if (result.getRiderId()==riderId){
						s.arrayListOfResults.remove(result);
					}
				}
			}
		}if (teamInQuestion.arrayListOfRiders.size() == initialLength){
			throw new IDNotRecognisedException("The ID entered does not match to any rider in the system");
		}
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {
		int initialLength=0;
		Stage stageInQuestion = null;
		outerloop:
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					initialLength = s.arrayListOfResults.size();
					for (Result result: s.arrayListOfResults){
						if (result.getRiderId()==riderId){
							throw new DuplicatedResultException("Rider already has a result for that stage");
						}
					}
					if (checkpoints.length != (s.arrayListOfSegments.size()+2)){
						throw new InvalidCheckpointsException("The number of checkpoints in the stage is invaliid");
					}
					if (!s.getStageState().equals("waiting for results")){
						throw new InvalidStageStateException("Results can only be added to a stage while it is waiting for results");
					}
					s.registerRiderResultsInStage(stageId, riderId, checkpoints);
					stageInQuestion = s;
					break outerloop;
				}
			}
		}if (stageInQuestion==null){
			throw new IDNotRecognisedException("The ID entered does not match to either any rider or stage in the system");
		}
		if (stageInQuestion.arrayListOfResults.size()==initialLength){
			throw new IDNotRecognisedException("The ID entered does not match to either any rider or stage in the system");
		}
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					ArrayList<LocalTime> arrayListOfRiderResults = s.getRiderResultsInStage(riderId);
					LocalTime[] array = arrayListOfRiderResults.toArray(new LocalTime[arrayListOfRiderResults.size()]);
					if (array.length ==0){
						throw new IDNotRecognisedException("The ID entered does not match to either any rider or stage in the system");
					}
					return array;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to either any rider or stage in the system");
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					LocalTime adjustedTime = s.getRiderAdjustedElapsedTimeInStage(riderId);
					if (adjustedTime == null){
						throw new IDNotRecognisedException("The ID entered does not match to either any rider or stage in the system");
					}
					return adjustedTime;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to either any rider or stage in the system");
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		int initialLength = 0;
		Stage stageInQuestion=null;
		outerloop:
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					initialLength = s.arrayListOfResults.size();
					s.deleteRiderResultsInStage(riderId);
					stageInQuestion = s;
					break outerloop;
				}
			}
		}if (stageInQuestion==null){
			throw new IDNotRecognisedException("The ID entered does not match to either any rider or stage in the system");
		}
		if (stageInQuestion.arrayListOfResults.size()==initialLength){
			throw new IDNotRecognisedException("The ID entered does not match to any rider or stage in the system");
		}
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		ArrayList<Rider> allRiders = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			for (Rider rider: team.arrayListOfRiders){
				allRiders.add(rider);
			}
		}
			
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					ArrayList<Integer> ridersRankInStage = s.getRidersRankInStage(allRiders);
					int[] arrayNew = new int[ridersRankInStage.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = ridersRankInStage.get(i).intValue();
					}
					return arrayNew;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
		
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		ArrayList<Rider> allRiders = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			for (Rider rider: team.arrayListOfRiders){
				allRiders.add(rider);
			}
		}
			
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					ArrayList<LocalTime> rankedAdjustedElapsedTimes = s.getRankedAdjustedElapsedTimesInStage(allRiders);
					LocalTime[] arrayNew = new LocalTime[rankedAdjustedElapsedTimes.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = rankedAdjustedElapsedTimes.get(i);
					}
					return arrayNew;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
	}
	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		ArrayList<Rider> allRiders = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			for (Rider rider: team.arrayListOfRiders){
				allRiders.add(rider);
			}
		}
			
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					ArrayList<Integer> ridersPointsInStage = s.getRidersPointsInStage(allRiders);
					int[] arrayNew = new int[ridersPointsInStage.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = ridersPointsInStage.get(i).intValue();
					}
					return arrayNew;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		ArrayList<Rider> allRiders = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			for (Rider rider: team.arrayListOfRiders){
				allRiders.add(rider);
			}
		}
			
		for (Race race: arrayListOfRaces){
			for (Stage s:race.arrayListOfStages){
				if (s.getStageId()==stageId){
					if (s.getStageType().equals(StageType.TT)){
						return new int[allRiders.size()];
					}
					ArrayList<Integer> ridersMountainPointsInStage = s.getRidersMountainPointsInStage(allRiders);
					int[] arrayNew = new int[ridersMountainPointsInStage.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = ridersMountainPointsInStage.get(i).intValue();
					}
					return arrayNew;
				}
			}
		}throw new IDNotRecognisedException("The ID entered does not match to any stage in the system");
	}

	@Override
	public void eraseCyclingPortal() {
		arrayListOfRaces.clear();
        arrayListOfTeams.clear();

        Team.teamCounter = 0;
		Race.raceCounter = 0;
		Rider.riderCounter = 0;
		Segment.segmentCounter = 0;
        //Stage.stageCounter = 0;
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename));
        outputStream.writeObject(this);
        outputStream.close();
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		eraseCyclingPortal();
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
		Object portalObject = inputStream.readObject();
		CyclingPortal newPortal = (CyclingPortal)portalObject;
		this.arrayListOfTeams = newPortal.arrayListOfTeams;
		this.arrayListOfRaces = newPortal.arrayListOfRaces;
		inputStream.close();
		
		//Race.raceCounter = arrayListOfRaces.get(arrayListOfRaces.size()-1).getRaceId();
		//Stage.stageCounter = Race.arrayListOfStages.get(Race.arrayListOfStages.size()-1).getStageId();
		//Segment.segmentCounter = Stage.arrayListOfSegments.get(Stage.arrayListOfSegments.size()-1).getSegmentId();
		//Team.teamCounter = arrayListOfTeams.get(arrayListOfTeams.size()-1).getTeamId();
		//Rider.riderCounter = Team.arrayListOfRiders.get(Team.arrayListOfRiders.size()-1).getRiderId();
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		int initialLength = arrayListOfRaces.size();
		for (Race race:arrayListOfRaces){
			if ((race.getName()).equals(name)){
				arrayListOfRaces.remove(race);
				break;
			}
		}
		if (arrayListOfRaces.size() == initialLength){
			throw new NameNotRecognisedException("The name entered does not match to any race in the system");
		}
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> arrayListOfRaceIDs = new ArrayList<>();
		for (Race race: arrayListOfRaces){
			arrayListOfRaceIDs.add(race.getRaceId());
		}
		if (!arrayListOfRaceIDs.contains(raceId)){
			throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
		}
		int numberOfRiders = 0;
        Hashtable<String, LocalTime> riderTotalAdjustedElapsedTimeDictionary = new Hashtable<String, LocalTime>();
		for (Race race: arrayListOfRaces){
            if (race.getRaceId()==(raceId)){
                if (race.getNumberOfStages()!=(0)){
                    numberOfRiders = (race.getStages().get(0)).getNumberOfResults();
                    for (Result result:((race.getStages().get(0)).getArrayListOfResults())){
                        if (!riderTotalAdjustedElapsedTimeDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                            riderTotalAdjustedElapsedTimeDictionary.put("Rider "+String.valueOf(result.getRiderId()),LocalTime.of(0,0,0,0));
                        }
                    }
                }
            }
        }
        if (numberOfRiders==0){
            return new LocalTime[0];
        }
        Set<String> setOfKeys = riderTotalAdjustedElapsedTimeDictionary.keySet();
        if (numberOfRiders==riderTotalAdjustedElapsedTimeDictionary.size()){
			for (Race race: arrayListOfRaces){
				if (race.getRaceId()==(raceId)){
					for (Stage stage: race.getStages()){
						for (String key : setOfKeys){
							LocalTime adjustedElapsedTime = stage.getRiderAdjustedElapsedTimeInStage(Integer.valueOf((key.split(" "))[1]));
							LocalTime currentDictionaryValue = riderTotalAdjustedElapsedTimeDictionary.get(key);
							Duration currentDictionaryValueDuration = Duration.ofNanos(currentDictionaryValue.toNanoOfDay());
							long currentDictionaryValueLong = currentDictionaryValueDuration.toNanos();
							LocalTime updatedDictionaryValue = adjustedElapsedTime.plusNanos(currentDictionaryValueLong);
							riderTotalAdjustedElapsedTimeDictionary.put(key, updatedDictionaryValue);
						}
					}ArrayList<LocalTime> arrayListOfTotalAdjustedElapsedTimesForEachRider = new ArrayList<>();
					for (String key : setOfKeys){
						arrayListOfTotalAdjustedElapsedTimesForEachRider.add(riderTotalAdjustedElapsedTimeDictionary.get(key));
					}Collections.sort(arrayListOfTotalAdjustedElapsedTimesForEachRider);
					//int[] array = arrayListOfTotalAdjustedElapsedTimesForEachRider.toArray(new int[arrayListOfTotalAdjustedElapsedTimesForEachRider.size()]);
					//return array;
					LocalTime[] arrayNew = new LocalTime[arrayListOfTotalAdjustedElapsedTimesForEachRider.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = arrayListOfTotalAdjustedElapsedTimesForEachRider.get(i);
					}
					return arrayNew;
				}
			}
        }return new LocalTime[0];
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> arrayListOfRaceIDs = new ArrayList<>();
		for (Race race: arrayListOfRaces){
			arrayListOfRaceIDs.add(race.getRaceId());
		}
		if (!arrayListOfRaceIDs.contains(raceId)){
			throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
		}
		///
		///
		///
		///
		///
		//STUFF TO GET ARRAY OF ORDERED TOTAL ELAPSED TIMES
		int numberOfRiders = 0;
        Hashtable<String, LocalTime> riderTotalElapsedTimeDictionary = new Hashtable<String, LocalTime>();
		for (Race race: arrayListOfRaces){
            if (race.getRaceId()==(raceId)){
                if (race.getNumberOfStages()!=(0)){
                    numberOfRiders = (race.getStages().get(0)).getNumberOfResults();
                    for (Result result:((race.getStages().get(0)).getArrayListOfResults())){
                        if (!riderTotalElapsedTimeDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                            riderTotalElapsedTimeDictionary.put("Rider "+String.valueOf(result.getRiderId()),LocalTime.of(0,0,0,0));
                        }
                    }
                }
            }
        }
        if (numberOfRiders==0){
            return new int[0];
        }
		ArrayList<LocalTime> arrayListOfTotalElapsedTimesForEachRider = new ArrayList<>();
        Set<String> setOfKeys = riderTotalElapsedTimeDictionary.keySet();
        if (numberOfRiders==riderTotalElapsedTimeDictionary.size()){
			for (Race race: arrayListOfRaces){
				if (race.getRaceId()==(raceId)){
					for (Stage stage: race.getStages()){
						for (String key : setOfKeys){
							LocalTime elapsedTime = stage.getRiderElapsedTimeInStage(Integer.valueOf((key.split(" "))[1]));
							LocalTime currentDictionaryValue = riderTotalElapsedTimeDictionary.get(key);
							Duration currentDictionaryValueDuration = Duration.ofNanos(currentDictionaryValue.toNanoOfDay());
							long currentDictionaryValueLong = currentDictionaryValueDuration.toNanos();
							LocalTime updatedDictionaryValue = elapsedTime.plusNanos(currentDictionaryValueLong);
							riderTotalElapsedTimeDictionary.put(key, updatedDictionaryValue);
						}
					}
					for (String key : setOfKeys){
						arrayListOfTotalElapsedTimesForEachRider.add(riderTotalElapsedTimeDictionary.get(key));
					}Collections.sort(arrayListOfTotalElapsedTimesForEachRider);
				}
			}
		}
		///
		//STUFF CONCERNING POINTS
		///
		///
		///
		///
		ArrayList<Rider> allRiders = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			for (Rider rider: team.arrayListOfRiders){
				allRiders.add(rider);
			}
		}
		int numberOfRiders2 = 0;
        Hashtable<String, Integer> riderTotalPointsDictionary = new Hashtable<String, Integer>();
		for (Race race: arrayListOfRaces){
            if (race.getRaceId()==(raceId)){
                if (race.getNumberOfStages()!=(0)){
                    numberOfRiders2 = (race.getStages().get(0)).getNumberOfResults();
                    for (Result result:((race.getStages().get(0)).getArrayListOfResults())){
                        if (!riderTotalPointsDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                            riderTotalPointsDictionary.put("Rider "+String.valueOf(result.getRiderId()),0);
                        }
                    }
                }
            }
        }
        if (numberOfRiders2==0){
            return new int[0];
        }
        Set<String> setOfKeys2 = riderTotalPointsDictionary.keySet();
        if (numberOfRiders2==riderTotalPointsDictionary.size()){
			for (Race race: arrayListOfRaces){
				if (race.getRaceId()==(raceId)){
					for (Stage stage: race.getStages()){
						for (String key : setOfKeys2){
							ArrayList<Integer> riderPointsInStage = stage.getRidersPointsInStage(allRiders);
							ArrayList<Integer> arrayListOfRiderIDsCorrespondingToRiderPointsInStageAL = stage.getRidersRankInStage(allRiders);
							int indexOfID = arrayListOfRiderIDsCorrespondingToRiderPointsInStageAL.indexOf(Integer.valueOf((key.split(" "))[1]));
							int pointsForGivenRiderInGivenStage = riderPointsInStage.get(indexOfID);
							int currentDictionaryValue = riderTotalPointsDictionary.get(key);
                    		int updatedDictionaryValue = currentDictionaryValue += pointsForGivenRiderInGivenStage;
							riderTotalPointsDictionary.put(key, updatedDictionaryValue);
						}
					}
				}
			}
		}ArrayList<Integer> riderTotalPointsSortedByTotalElapsedTime = new ArrayList<>();
		ArrayList<Integer> riderIDsCorrespondingToTotalElapsedTimes = new ArrayList<>();
		for (LocalTime elapsedTime: arrayListOfTotalElapsedTimesForEachRider){
			for (String key: setOfKeys){
				if (riderTotalElapsedTimeDictionary.get(key).equals(elapsedTime)){
					riderIDsCorrespondingToTotalElapsedTimes.add(Integer.valueOf((key.split(" "))[1]));
				}
			}for (Integer riderId: riderIDsCorrespondingToTotalElapsedTimes){
				riderTotalPointsSortedByTotalElapsedTime.add(riderTotalPointsDictionary.get("Rider "+ String.valueOf(riderId)));
			}
			int[] arrayNew = new int[riderTotalPointsSortedByTotalElapsedTime.size()];
			for (int i=0; i < arrayNew.length; i++) {
				arrayNew[i] = riderTotalPointsSortedByTotalElapsedTime.get(i);
			}
			return arrayNew;
		}return new int[0];
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> arrayListOfRaceIDs = new ArrayList<>();
		for (Race race: arrayListOfRaces){
			arrayListOfRaceIDs.add(race.getRaceId());
		}
		if (!arrayListOfRaceIDs.contains(raceId)){
			throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
		}
		///
		///
		///
		///
		///
		//STUFF TO GET ARRAY OF ORDERED TOTAL ELAPSED TIMES
		int numberOfRiders = 0;
        Hashtable<String, LocalTime> riderTotalElapsedTimeDictionary = new Hashtable<String, LocalTime>();
		for (Race race: arrayListOfRaces){
            if (race.getRaceId()==(raceId)){
                if (race.getNumberOfStages()!=(0)){
                    numberOfRiders = (race.getStages().get(0)).getNumberOfResults();
                    for (Result result:((race.getStages().get(0)).getArrayListOfResults())){
                        if (!riderTotalElapsedTimeDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                            riderTotalElapsedTimeDictionary.put("Rider "+String.valueOf(result.getRiderId()),LocalTime.of(0,0,0,0));
                        }
                    }
                }
            }
        }
        if (numberOfRiders==0){
            return new int[0];
        }
		ArrayList<LocalTime> arrayListOfTotalElapsedTimesForEachRider = new ArrayList<>();
        Set<String> setOfKeys = riderTotalElapsedTimeDictionary.keySet();
        if (numberOfRiders==riderTotalElapsedTimeDictionary.size()){
			for (Race race: arrayListOfRaces){
				if (race.getRaceId()==(raceId)){
					for (Stage stage: race.getStages()){
						for (String key : setOfKeys){
							LocalTime elapsedTime = stage.getRiderElapsedTimeInStage(Integer.valueOf((key.split(" "))[1]));
							LocalTime currentDictionaryValue = riderTotalElapsedTimeDictionary.get(key);
							Duration currentDictionaryValueDuration = Duration.ofNanos(currentDictionaryValue.toNanoOfDay());
							long currentDictionaryValueLong = currentDictionaryValueDuration.toNanos();
							LocalTime updatedDictionaryValue = elapsedTime.plusNanos(currentDictionaryValueLong);
							riderTotalElapsedTimeDictionary.put(key, updatedDictionaryValue);
						}
					}
					for (String key : setOfKeys){
						arrayListOfTotalElapsedTimesForEachRider.add(riderTotalElapsedTimeDictionary.get(key));
					}Collections.sort(arrayListOfTotalElapsedTimesForEachRider);
				}
			}
		}
		///
		//STUFF CONCERNING POINTS
		///
		///
		///
		///
		ArrayList<Rider> allRiders = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			for (Rider rider: team.arrayListOfRiders){
				allRiders.add(rider);
			}
		}
		int numberOfRiders2 = 0;
        Hashtable<String, Integer> riderTotalMountainPointsDictionary = new Hashtable<String, Integer>();
		for (Race race: arrayListOfRaces){
            if (race.getRaceId()==(raceId)){
                if (race.getNumberOfStages()!=(0)){
                    numberOfRiders2 = (race.getStages().get(0)).getNumberOfResults();
                    for (Result result:((race.getStages().get(0)).getArrayListOfResults())){
                        if (!riderTotalMountainPointsDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                            riderTotalMountainPointsDictionary.put("Rider "+String.valueOf(result.getRiderId()),0);
                        }
                    }
                }
            }
        }
        if (numberOfRiders2==0){
            return new int[0];
        }
        Set<String> setOfKeys2 = riderTotalMountainPointsDictionary.keySet();
        if (numberOfRiders2==riderTotalMountainPointsDictionary.size()){
			for (Race race: arrayListOfRaces){
				if (race.getRaceId()==(raceId)){
					for (Stage stage: race.getStages()){
						for (String key : setOfKeys2){
							ArrayList<Integer> riderMountainPointsInStage = stage.getRidersMountainPointsInStage(allRiders);
							ArrayList<Integer> arrayListOfRiderIDsCorrespondingToRiderMountainPointsInStageAL = stage.getRidersRankInStage(allRiders);
							int indexOfID = arrayListOfRiderIDsCorrespondingToRiderMountainPointsInStageAL.indexOf(Integer.valueOf((key.split(" "))[1]));
							int mountainPointsForGivenRiderInGivenStage = riderMountainPointsInStage.get(indexOfID);
							int currentDictionaryValue = riderTotalMountainPointsDictionary.get(key);
                    		int updatedDictionaryValue = currentDictionaryValue += mountainPointsForGivenRiderInGivenStage;
							riderTotalMountainPointsDictionary.put(key, updatedDictionaryValue);
						}
					}
				}
			}
		}ArrayList<Integer> riderTotalMountainPointsSortedByTotalElapsedTime = new ArrayList<>();
		ArrayList<Integer> riderIDsCorrespondingToTotalElapsedTimes = new ArrayList<>();
		for (LocalTime elapsedTime: arrayListOfTotalElapsedTimesForEachRider){
			for (String key: setOfKeys){
				if (riderTotalElapsedTimeDictionary.get(key).equals(elapsedTime)){
					riderIDsCorrespondingToTotalElapsedTimes.add(Integer.valueOf((key.split(" "))[1]));
				}
			}for (Integer riderId: riderIDsCorrespondingToTotalElapsedTimes){
				riderTotalMountainPointsSortedByTotalElapsedTime.add(riderTotalMountainPointsDictionary.get("Rider "+ String.valueOf(riderId)));
			}
			int[] arrayNew = new int[riderTotalMountainPointsSortedByTotalElapsedTime.size()];
			for (int i=0; i < arrayNew.length; i++) {
				arrayNew[i] = riderTotalMountainPointsSortedByTotalElapsedTime.get(i);
			}
			return arrayNew;
		}return new int[0];
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> arrayListOfRaceIDs = new ArrayList<>();
		for (Race race: arrayListOfRaces){
			arrayListOfRaceIDs.add(race.getRaceId());
		}
		if (!arrayListOfRaceIDs.contains(raceId)){
			throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
		}
        int numberOfRiders = 0;
        Hashtable<String, LocalTime> riderTotalAdjustedElapsedTimeDictionary = new Hashtable<String, LocalTime>();
		for (Race race: arrayListOfRaces){
            if (race.getRaceId()==(raceId)){
                if (race.getNumberOfStages()!=(0)){
                    numberOfRiders = (race.getStages().get(0)).getNumberOfResults();
                    for (Result result:((race.getStages().get(0)).getArrayListOfResults())){
                        if (!riderTotalAdjustedElapsedTimeDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                            riderTotalAdjustedElapsedTimeDictionary.put("Rider "+String.valueOf(result.getRiderId()),LocalTime.of(0,0,0,0));
                        }
                    }
                }
            }
        }
        if (numberOfRiders==0){
            return new int[0];
        }
        Set<String> setOfKeys = riderTotalAdjustedElapsedTimeDictionary.keySet();
        if (numberOfRiders==riderTotalAdjustedElapsedTimeDictionary.size()){
			for (Race race: arrayListOfRaces){
				if (race.getRaceId()==(raceId)){
					for (Stage stage: race.getStages()){
						for (String key : setOfKeys){
							LocalTime adjustedElapsedTime = stage.getRiderAdjustedElapsedTimeInStage(Integer.valueOf((key.split(" "))[1]));
							LocalTime currentDictionaryValue = riderTotalAdjustedElapsedTimeDictionary.get(key);
							//long currentDictionaryValueDuration = Duration.ofNanos(currentDictionaryValue.toNanoOfDay());
							Duration currentDictionaryValueDuration = Duration.ofNanos(currentDictionaryValue.toNanoOfDay());
							long currentDictionaryValueLong = currentDictionaryValueDuration.toNanos();
							LocalTime updatedDictionaryValue = adjustedElapsedTime.plusNanos(currentDictionaryValueLong);
							//LocalTime updatedDictionaryValue = adjustedElapsedTime.plusNanos(currentDictionaryValueDuration);
							riderTotalAdjustedElapsedTimeDictionary.put(key, updatedDictionaryValue);
						}
					}ArrayList<LocalTime> arrayListOfTotalAdjustedElapsedTimesForEachRider = new ArrayList<>();
					for (String key : setOfKeys){
						arrayListOfTotalAdjustedElapsedTimesForEachRider.add(riderTotalAdjustedElapsedTimeDictionary.get(key));
					}Collections.sort(arrayListOfTotalAdjustedElapsedTimesForEachRider);
					ArrayList<Integer> arrayListOfIDsSortedByTAE = new ArrayList<>();
					for (LocalTime j: arrayListOfTotalAdjustedElapsedTimesForEachRider){
						for (String key : setOfKeys){
							if (!arrayListOfIDsSortedByTAE.contains(Integer.valueOf((key.split(" "))[1]))){
								if (riderTotalAdjustedElapsedTimeDictionary.get(key).equals(j)){
									arrayListOfIDsSortedByTAE.add(Integer.valueOf((key.split(" "))[1]));
									break;
								} 
							}
						}
					}//int[] array = arrayListOfIDsSortedByTAE.toArray(new int[arrayListOfIDsSortedByTAE.size()]);
					//return array;
					int[] arrayNew = new int[arrayListOfIDsSortedByTAE.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = arrayListOfIDsSortedByTAE.get(i);
					}
					return arrayNew;
				}
			}
        }return new int[0];
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> arrayListOfRaceIDs = new ArrayList<>();
		for (Race race: arrayListOfRaces){
			arrayListOfRaceIDs.add(race.getRaceId());
		}
		if (!arrayListOfRaceIDs.contains(raceId)){
			throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
		}
		ArrayList<Rider> allRiders = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			for (Rider rider: team.arrayListOfRiders){
				allRiders.add(rider);
			}
		}
		int numberOfRiders = 0;
        Hashtable<String, Integer> riderTotalPointsDictionary = new Hashtable<String, Integer>();
		for (Race race: arrayListOfRaces){
            if (race.getRaceId()==(raceId)){
                if (race.getNumberOfStages()!=(0)){
                    numberOfRiders = (race.getStages().get(0)).getNumberOfResults();
                    for (Result result:((race.getStages().get(0)).getArrayListOfResults())){
                        if (!riderTotalPointsDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                            riderTotalPointsDictionary.put("Rider "+String.valueOf(result.getRiderId()),0);
                        }
                    }
                }
            }
        }
        if (numberOfRiders==0){
            return new int[0];
        }
        Set<String> setOfKeys = riderTotalPointsDictionary.keySet();
        if (numberOfRiders==riderTotalPointsDictionary.size()){
			for (Race race: arrayListOfRaces){
				if (race.getRaceId()==(raceId)){
					for (Stage stage: race.getStages()){
						for (String key : setOfKeys){
							ArrayList<Integer> riderPointsInStage = stage.getRidersPointsInStage(allRiders);
							ArrayList<Integer> arrayListOfRiderIDsCorrespondingToRiderPointsInStageAL = stage.getRidersRankInStage(allRiders);
							int indexOfID = arrayListOfRiderIDsCorrespondingToRiderPointsInStageAL.indexOf(Integer.valueOf((key.split(" "))[1]));
							int pointsForGivenRiderInGivenStage = riderPointsInStage.get(indexOfID);
							int currentDictionaryValue = riderTotalPointsDictionary.get(key);
                    		int updatedDictionaryValue = currentDictionaryValue += pointsForGivenRiderInGivenStage;
							riderTotalPointsDictionary.put(key, updatedDictionaryValue);
						}
					}ArrayList<Integer> arrayListOfTotalPointsForEachRider = new ArrayList<>();
					for (String key : setOfKeys){
						arrayListOfTotalPointsForEachRider.add(riderTotalPointsDictionary.get(key));
					}Collections.sort(arrayListOfTotalPointsForEachRider, Collections.reverseOrder());
					ArrayList<Integer> arrayListOfIDsSortedByPoints = new ArrayList<>();
					for (int j: arrayListOfTotalPointsForEachRider){
						for (String key : setOfKeys){
							if (!arrayListOfIDsSortedByPoints.contains(Integer.valueOf((key.split(" "))[1]))){
								if (riderTotalPointsDictionary.get(key)==(j)){
									arrayListOfIDsSortedByPoints.add(Integer.valueOf((key.split(" "))[1]));
									break;
								} 
							}
						}
            		}
					int[] arrayNew = new int[arrayListOfIDsSortedByPoints.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = arrayListOfIDsSortedByPoints.get(i);
					}
					return arrayNew;
				}
			}
		}return new int[0];
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> arrayListOfRaceIDs = new ArrayList<>();
		for (Race race: arrayListOfRaces){
			arrayListOfRaceIDs.add(race.getRaceId());
		}
		if (!arrayListOfRaceIDs.contains(raceId)){
			throw new IDNotRecognisedException("The ID entered does not match to any race in the system");
		}
		ArrayList<Rider> allRiders = new ArrayList<>();
		for (Team team:arrayListOfTeams){
			for (Rider rider: team.arrayListOfRiders){
				allRiders.add(rider);
			}
		}
		int numberOfRiders = 0;
        Hashtable<String, Integer> riderTotalMountainPointsDictionary = new Hashtable<String, Integer>();
		for (Race race: arrayListOfRaces){
            if (race.getRaceId()==(raceId)){
                if (race.getNumberOfStages()!=(0)){
                    numberOfRiders = (race.getStages().get(0)).getNumberOfResults();
                    for (Result result:((race.getStages().get(0)).getArrayListOfResults())){
                        if (!riderTotalMountainPointsDictionary.containsKey("Rider "+String.valueOf(result.getRiderId()))){
                            riderTotalMountainPointsDictionary.put("Rider "+String.valueOf(result.getRiderId()),0);
                        }
                    }
                }
            }
        }
        if (numberOfRiders==0){
            return new int[0];
        }
        Set<String> setOfKeys = riderTotalMountainPointsDictionary.keySet();
        if (numberOfRiders==riderTotalMountainPointsDictionary.size()){
			for (Race race: arrayListOfRaces){
				if (race.getRaceId()==(raceId)){
					for (Stage stage: race.getStages()){
						for (String key : setOfKeys){
							ArrayList<Integer> riderMountainPointsInStage = stage.getRidersMountainPointsInStage(allRiders);
							ArrayList<Integer> arrayListOfRiderIDsCorrespondingToRiderMountainPointsInStageAL = stage.getRidersRankInStage(allRiders);
							int indexOfID = arrayListOfRiderIDsCorrespondingToRiderMountainPointsInStageAL.indexOf(Integer.valueOf((key.split(" "))[1]));
							int mountainPointsForGivenRiderInGivenStage = riderMountainPointsInStage.get(indexOfID);
							int currentDictionaryValue = riderTotalMountainPointsDictionary.get(key);
                    		int updatedDictionaryValue = currentDictionaryValue += mountainPointsForGivenRiderInGivenStage;
							riderTotalMountainPointsDictionary.put(key, updatedDictionaryValue);
						}
					}ArrayList<Integer> arrayListOfTotalMountainPointsForEachRider = new ArrayList<>();
					for (String key : setOfKeys){
						arrayListOfTotalMountainPointsForEachRider.add(riderTotalMountainPointsDictionary.get(key));
					}Collections.sort(arrayListOfTotalMountainPointsForEachRider, Collections.reverseOrder());
					ArrayList<Integer> arrayListOfIDsSortedByMountainPoints = new ArrayList<>();
					for (int j: arrayListOfTotalMountainPointsForEachRider){
						for (String key : setOfKeys){
							if (!arrayListOfIDsSortedByMountainPoints.contains(Integer.valueOf((key.split(" "))[1]))){
								if (riderTotalMountainPointsDictionary.get(key)==(j)){
									arrayListOfIDsSortedByMountainPoints.add(Integer.valueOf((key.split(" "))[1]));
									break;
								} 
							}
						}
            		}
					int[] arrayNew = new int[arrayListOfIDsSortedByMountainPoints.size()];
					for (int i=0; i < arrayNew.length; i++) {
						arrayNew[i] = arrayListOfIDsSortedByMountainPoints.get(i);
					}
					return arrayNew;
				}
			}
		}return new int[0];
	}
}