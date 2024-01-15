import java.util.*;

public class ClimbingGym implements Comparable<ClimbingGym> {
    private String name;
    private List<ContestantClimber> contestantClimbers;
    private List<Route> routes;

    public ClimbingGym(){

    }

    public ClimbingGym(String name){
        this.name = name;
        this.contestantClimbers = new ArrayList<>();
        this.routes = new ArrayList<>();
    }

    public ClimbingGym(String name, Route route){
        this.name = name;
        this.contestantClimbers = new ArrayList<>();
        this.routes.add(route);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContestantClimbers(List<ContestantClimber> contestantClimbers) {
        this.contestantClimbers = contestantClimbers;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public ClimbingGym(String name, ContestantClimber contestantClimber, List<Route> routes){
        this.name = name;
        this.contestantClimbers.add(contestantClimber);
        this.routes = routes;
    }

    public String getName(){
        return name;
    }

    public List<Route> getRoutes(){
        return routes;
    }

    public List<ContestantClimber> getContestantClimbers(){
        return contestantClimbers;
    }

    public void addContestantClimbers(ContestantClimber contestantClimber){
        this.contestantClimbers.add(contestantClimber);
        contestantClimber.setRoutes(this.routes);
        System.out.println("Climber " + contestantClimber + ", has enroled in the competition held at " + this.name);
    }

    public void addRoute(Route route){
        this.routes.add(route);
        System.out.println("Route " + route + ", has been added in the competition held at " + this.name);
    }

    public void removeRoute(Integer routeID) throws RouteNotFoundException{
        try{
            routes.remove(routes.get(--routeID));
        } catch (IndexOutOfBoundsException e) {
            throw new RouteNotFoundException("Route does not exist");
        }
    }
    public void removeContestant(Integer contestantID) throws ContestantNotFoundException{
            for (ContestantClimber climber : contestantClimbers) {
                if (climber.getContestantID() == contestantID) {
                    contestantClimbers.remove(climber);
                    return;
                }
            }
            throw new ContestantNotFoundException("Contestant not found!");
    }

    @Override
    public String toString() {
        return "Name: " + this.name + ", contestants:" + this.contestantClimbers.size() + ", routes: " + this.routes.size();
    }

    @Override
    public int compareTo(ClimbingGym other) {
        // Compare based on name
        int nameComparison = this.name.compareTo(other.name);

        if (nameComparison != 0) {
            // Names are different, return the result of name comparison
            return nameComparison;
        } else {
            // Names are equal, compare based on the number of climbers enrolled
            return Integer.compare(this.contestantClimbers.size(), other.contestantClimbers.size());
        }
    }


}
