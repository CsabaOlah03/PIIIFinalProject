import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
public class InputDevice {

    private static Scanner scanner = null;

    public InputDevice(Scanner scanner){
        this.scanner = scanner;
    }
    public ClimbingGym[] getGym(){
        ClimbingGym[] climbingGyms = new ClimbingGym[2];
        climbingGyms[0] = new ClimbingGym("OneMove");
        climbingGyms[1] = new ClimbingGym("VerticalSpirit");
        return climbingGyms;
    }
    public List<ContestantClimber> getClimbers() {
        List<ContestantClimber> climbers = new ArrayList<>();
        climbers.add(new ContestantClimber("Adam","Ondra",25));
        climbers.add(new ContestantClimber("Ian","Young",30));
        climbers.add(new ContestantClimber("Lary","Smith",24));
        climbers.add(new ContestantClimber("Oksana","Astankova",25));
        return climbers;
    }
    public ContestantClimber getClimber() {
        ContestantClimber climber = new ContestantClimber("Steph","Care",26);
        return climber;
    }

    public List<Route> getRoutes(){
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(7,Type.QUALIFICATIONS,Difficulty.EASY));
        routes.add(new Route(8,Type.QUALIFICATIONS,Difficulty.EASY));
        routes.add(new Route(9,Type.QUALIFICATIONS,Difficulty.HARD));
        routes.add(new Route(10,Type.QUALIFICATIONS,Difficulty.INTERMEDIATE));
        routes.add(new Route(13,Type.QUALIFICATIONS,Difficulty.INTERMEDIATE));
        routes.add(new Route(12,Type.FINALS,Difficulty.HARD));
        routes.add(new Route(14,Type.FINALS,Difficulty.HARD_TO_DIFFICULT));
        return routes;
    }
    public Route getRoute() {
        Route route = new Route(7,Type.QUALIFICATIONS,Difficulty.EASY);
        return route;
    }

    public Integer readNumberOfHolds() throws NumberFormatException{
        Integer numberOfHolds = -1;
        do {
            numberOfHolds = Integer.parseInt(scanner.nextLine());
            if(numberOfHolds > 50){
                System.out.println("Please enter a number less than 50");
            }
        } while (numberOfHolds > 50 && numberOfHolds != -1);

        return numberOfHolds;
    }

    public Integer readDifficulty() throws NumberFormatException{
        Integer difficulty = -1;
        do {
            difficulty = Integer.parseInt(scanner.nextLine());
            if(difficulty > 5 || difficulty < 0){
                System.out.println("Please enter a number between 1 and 5");
            }
        } while (difficulty > 5 && difficulty != -1);

        return difficulty;
    }

    public Integer readType() throws NumberFormatException{
        Integer type = -1;
        do {
            type = Integer.parseInt(scanner.nextLine());
            if(type > 2 || type < 0){
                System.out.println("Please enter a number between 1 and 2");
            }
        } while (type > 2 && type != -1);

        return type;
    }

    public String readName() throws NoSuchElementException{
        String name = null;
        name = scanner.nextLine();
        return name;
    }

    public Integer readAge() throws NumberFormatException{
        Integer age = -1;
        age = Integer.parseInt(scanner.nextLine());
        return age;
    }
}
