import java.io.OutputStream;
import java.util.List;

public class OutputDevice {

    OutputStream outputStream;

    public OutputDevice(OutputStream os){
        this.outputStream = os;
    }
    public void writeToConsole(String message) {
        System.out.println(message);
    }

    public void writeToConsole(ContestantClimber climber) {
        System.out.println(climber);
    }
    public void writeToConsole(Route route) {
        System.out.println(route);
    }
    
    public void printMenu(List<ClimbingGym> gyms, int currentGym) {
        System.out.println("-------Menu-------");
        System.out.println("1) Add a gym");
        System.out.println("2) Select a gym");
        System.out.println("3) Add route(s) to current gym (one or more)");
        System.out.println("4) Remove route from current gym (requires routeID)");
        System.out.println("5) Add contestant(s) to current gym (one or more)");
        System.out.println("6) Remove contestant from current gym (requires contestantID)");
        System.out.println("7) Display climbers enrolled in competition held at current gym");
        System.out.println("8) Display routes present at current gym");
        System.out.println("9) Load hardcoded gym and participants");
        System.out.println("0) Exit");
        try {
            System.out.println("-------Current gym is " + gyms.get(currentGym) +"-------");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("-------No Gyms-------");
        }
    }
}
