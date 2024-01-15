import java.util.*;

public class Route implements Scorable, Comparable<Route> {

    private static Integer routeIDCounter = 0;
    private Integer routeID;
    private Integer numOfHolds;
    private Integer numOfTries;
    private Type type;
    private Difficulty difficulty;
    private Integer score;

    public static Integer getRouteIDCounter() {
        return routeIDCounter;
    }

    public static void setRouteIDCounter(Integer routeIDCounter) {
        Route.routeIDCounter = routeIDCounter;
    }

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public Integer getNumOfHolds() {
        return numOfHolds;
    }

    public void setNumOfHolds(Integer numOfHolds) {
        this.numOfHolds = numOfHolds;
    }

    public Integer getNumOfTries() {
        return numOfTries;
    }

    public void setNumOfTries(Integer numOfTries) {
        this.numOfTries = numOfTries;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Route() {
    }

    public Route(Integer numOfHolds, Type type, Difficulty difficulty) {
        routeIDCounter++;
        this.routeID = routeIDCounter;
        this.numOfHolds = numOfHolds;
        this.numOfTries = 0;
        this.type = type;
        this.difficulty = difficulty;
        this.score = 0;
    }
    public Route(Integer routeID, Integer numOfHolds, Integer numOfTries, Type type, Difficulty difficulty, Integer score) {// constructor for reading from files
        this.routeID = routeID;
        this.numOfHolds = numOfHolds;
        this.numOfTries = numOfTries;
        this.type = type;
        this.difficulty = difficulty;
        this.score = score;
    }

    @Override
    public Integer computeScore() {
        //Compute the score based on type, difficulty and number of tries
        return null;
    }

    @Override
    public String toString() {
        return "ID: " + this.routeID + ", with " + this.numOfHolds + " holds, " + this.difficulty + ", " + this.type;
    }

    @Override
    public int compareTo(Route other) {
        return this.routeID.compareTo(other.routeID);
    }
}
