import java.util.*;
public class ContestantClimber extends Climber implements Qualifiable,Comparable<ContestantClimber>{
    private static Integer IDCounter = 0;
    private Integer contestantID;
    private List<Route> routes; //make map of this
    private Integer finalPoints;
    private boolean qualifiedForFinals;

    public static Integer getIDCounter() {
        return IDCounter;
    }

    public static void setIDCounter(Integer IDCounter) {
        ContestantClimber.IDCounter = IDCounter;
    }

    public void setContestantID(Integer contestantID) {
        this.contestantID = contestantID;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setFinalPoints(Integer finalPoints) {
        this.finalPoints = finalPoints;
    }

    public void setQualifiedForFinals(boolean qualifiedForFinals) {
        this.qualifiedForFinals = qualifiedForFinals;
    }

    public ContestantClimber() {
    }

    //for reading from files
    public ContestantClimber(String firstName, String lastName, Integer age, List<Route> routes, Integer finalPoints, boolean qualifiedForFinals) { //for reading from files
        super(firstName, lastName, age);
        IDCounter++;
        this.routes = routes;
        this.finalPoints = finalPoints;
        this.qualifiedForFinals = qualifiedForFinals;
        this.contestantID = IDCounter;
    }
    public ContestantClimber(String firstName, String lastName, Integer age, List<Route> routes) {
        super(firstName, lastName, age);
        IDCounter++;
        this.routes = routes;
        this.finalPoints = 0;
        this.qualifiedForFinals = false;
        this.contestantID = IDCounter;
    }
    public ContestantClimber(String firstName, String lastName, Integer age) {
        super(firstName, lastName, age);
        IDCounter++;
        this.routes = new ArrayList<>();
        this.finalPoints = 0;
        this.qualifiedForFinals = false;
        this.contestantID = IDCounter;
    }

    public Integer getFinalPoints(){
        return finalPoints;
    }

    public Integer getContestantID() {return contestantID;}

    public void setRoutes(List<Route> routes) {//set routes to
        this.routes = routes;
    }

    @Override
    public boolean isQualifiedForFinals() {
        //If the finalPoints are more than a value
        // computed by taking into account the total points of the routes list (say 80% of the total points)
        // then return true; else return false
        return false;
    }
    @Override
    public String toString() {
        return "ID " + this.contestantID + " " + this.firstName + " " + this.lastName + ", age " + this.age;
    }

    @Override
    public int compareTo(ContestantClimber other) {
        // Compare based on name
        return this.getLastName().compareTo(other.getLastName());
    }

    public static Comparator<ContestantClimber> compareByFinalPoints = Comparator.comparingInt(ContestantClimber::getFinalPoints);
}
