package cycling;

import java.io.Serializable;

/**
 * The Rider class stores all the data related to the rider.
 */
public class Rider implements Serializable{
    public static int riderCounter = 0;
    private int riderId;
    private int teamId;
    private String name;
    private int yearOfBirth;

    /**
     * Constructor for the Rider class.
     * @param teamId The ID of the rider's team.
     * @param name The name of the rider.
     * @param yearOfBirth The year of birth of the rider.
     * @see cycling.team
     */
    public Rider(int teamId, String name, int yearOfBirth) {
        this.teamId = teamId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        riderCounter ++;
        riderId = riderCounter;
    }

    /**
     * Gets the team ID of the team the rider belongs to.
     * @return The team ID.
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Gets the ID for the rider.
     * @return The rider ID.
     */
    public int getRiderId() {
        return riderId;
    }

    /**
     * Gets the name of the rider.
     * @return The name of the rider.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the year of birth of the rider.
     * @return The year of birth of the rider.
     */
    public int getYearOfBirth() {
        return yearOfBirth;
    }
}