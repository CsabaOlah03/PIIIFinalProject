import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class CompetitionManager {
    private InputDevice inputDevice;
    private OutputDevice outputDevice;

    public CompetitionManager(InputDevice inputDevice, OutputDevice outputDevice){
        this.inputDevice = inputDevice;
        this.outputDevice = outputDevice;
    }

    public ClimbingGym addGym(String name){
        ClimbingGym gym = new ClimbingGym(name);
        return gym;
    }
    public void displayGyms(LinkedList<ClimbingGym> gyms){
        outputDevice.writeToConsole("Write the name of the gym you want to select: ");

        // Sort by name, if same name, then by number of enrolled climbers
        Collections.sort(gyms);

        for(ClimbingGym gym: gyms){
            System.out.println(gym);
        }
    }

    public void registration(ClimbingGym gym, List<ContestantClimber> climbers){
        for(ContestantClimber contestantClimber: climbers) {
            gym.addContestantClimbers(contestantClimber); //Also displays details
        }
    }
    public void registration(ClimbingGym gym, ContestantClimber climber){
        gym.addContestantClimbers(climber); //Also displays details
    }
    public void addRoute(ClimbingGym gym, Route route){
        gym.addRoute(route);
    }

    public void addRoute(ClimbingGym gym, List<Route> routes){ // for adding routes from files
        for(Route route: routes){
            gym.addRoute(route);
        }
    }

    public void addContestant(ClimbingGym gym, ContestantClimber climber) {
        gym.addContestantClimbers(climber);
    }

    public void displayContestants(ClimbingGym gym) {
        Collections.sort(gym.getContestantClimbers());

        for(ContestantClimber climber: gym.getContestantClimbers()){
            outputDevice.writeToConsole(climber);
        }
    }

    public void displayRoutes(ClimbingGym gym) {
        Collections.sort(gym.getRoutes());

        outputDevice.writeToConsole("The routes present at " + gym.getName() + " are:");
        for(Route route: gym.getRoutes()) {
            outputDevice.writeToConsole(route);
        }
    }

    public void removeRoute(ClimbingGym gym, Integer routeID) throws RouteNotFoundException{
        gym.removeRoute(routeID);
        outputDevice.writeToConsole("Route with ID " + routeID + " has been removed from " + gym.getName());
    }

    public void removeContestant(ClimbingGym gym, Integer contestantID) throws ContestantNotFoundException {
        gym.removeContestant(contestantID);
        outputDevice.writeToConsole("Contestant with ID " + contestantID + " has been removed from " + gym.getName());
    }

    //public ClimbingGym getGym()
}
