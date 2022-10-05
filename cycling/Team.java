package cycling;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * The Team class stores all the informaton related to a team,
 *  including which riders are a part of the team.
 */
public class Team implements Serializable{
    public static int teamCounter = 0;
    private int teamId;
    private String name;
    private String description;
    ArrayList<Rider> arrayListOfRiders = new ArrayList<>();

    /**
     * Constructor for the team class.
     * @param name The name of the team.
     * @param description A description of the team.
     */
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
        teamCounter += 1;
        teamId = teamCounter;
    }

    /**
     * Gets the ID for the team.
     * @return The team ID.
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Gets the name of the team.
     * @return The team name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the team.
     * @return The description of the team.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Creates a new rider and adds it to a list of riders that belong to the team.
     * @param teamId The ID of the team the rider belongs to.
     * @param name The name of the rider.
     * @param yearOfBirth The year of birth for the rider.
     * @return The ID created for the rider
     */
    public int createRider(int teamId, String name, int yearOfBirth) {
        Rider newRider = new Rider(teamId, name, yearOfBirth);
        arrayListOfRiders.add(newRider);
        return newRider.getRiderId();
    }

    /**
     * Removes a rider and removes it from the team.
     * @param riderId The ID of the rider
     */
    public void removeRider(int riderId) {
        for (Rider i:arrayListOfRiders) {
            if ((i.getRiderId()) == (riderId)) {
                arrayListOfRiders.remove(i);
            }
        }
    }

    /**
     * Gets all the riders that belong to the team.
     * @return A list of Riders that belong to the team.
     */
    public ArrayList<Integer> getTeamRiders() {
        ArrayList<Integer> arrayListOfTeamRiderIDs = new ArrayList<>();
        for (Rider rider:arrayListOfRiders) {
            arrayListOfTeamRiderIDs.add(rider.getRiderId());
        }
        return arrayListOfTeamRiderIDs;
    }
}