import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {
    private CompetitionManager competitionManager;
    public Tests(CompetitionManager competitionManager){
        this.competitionManager = competitionManager;
    }

    @Test
    public void testClimberConstructor() {
        String firstName = "John";
        String lastName = "Doe";
        Integer age = 25;

        Climber climber = new Climber(firstName, lastName, age);

        assertEquals(firstName, climber.getFirstName());
        assertEquals(lastName, climber.getLastName());
        assertEquals(age, climber.getAge());
    }

    @Test
    public void testClimbingGymConstructorWithName() {
        ClimbingGym climbingGym = new ClimbingGym("Test Gym");

        assertEquals("Test Gym", climbingGym.getName());
        assertTrue(climbingGym.getContestantClimbers().isEmpty());
        assertTrue(climbingGym.getRoutes().isEmpty());
    }

    @Test
    public void testClimbingGymConstructorWithNameAndRoute() {
    Route route = new Route(10, Type.QUALIFICATIONS, Difficulty.INTERMEDIATE);
        ClimbingGym climbingGym = new ClimbingGym("Test Gym", route);

        assertEquals("Test Gym", climbingGym.getName());
        assertTrue(climbingGym.getContestantClimbers().isEmpty());
        assertFalse(climbingGym.getRoutes().isEmpty());
        assertEquals(route, climbingGym.getRoutes().get(0));
    }

    @Test
    public void testContestantClimberConstructorFromFileWithRoutes() {
        List<Route> routes = Arrays.asList(
                new Route(10, Type.FINALS, Difficulty.INTERMEDIATE),
                new Route(8, Type.QUALIFICATIONS, Difficulty.EASY)
        );
        ContestantClimber contestantClimber = new ContestantClimber("John", "Doe", 25, routes, 20, true);

        assertEquals("John", contestantClimber.getFirstName());
        assertEquals("Doe", contestantClimber.getLastName());
        assertEquals(25, contestantClimber.getAge());
        assertEquals(routes, contestantClimber.getRoutes());
        assertEquals(20, contestantClimber.getFinalPoints());
        assertTrue(contestantClimber.isQualifiedForFinals());
    }

    @Test
    public void testRouteConstructor() {
        Route route = new Route(12, Type.FINALS, Difficulty.HARD);

        assertEquals(12, route.getNumOfHolds());
        assertEquals(0, route.getNumOfTries());
        assertEquals(Type.FINALS, route.getType());
        assertEquals(Difficulty.HARD, route.getDifficulty());
        assertEquals(0, route.getScore());
    }

    @Test
    public void testRouteConstructorFromFile() {
        Route route = new Route(1, 12, 3, Type.QUALIFICATIONS, Difficulty.EASY, 25);

        assertEquals(12, route.getNumOfHolds());
        assertEquals(3, route.getNumOfTries());
        assertEquals(Type.QUALIFICATIONS, route.getType());
        assertEquals(Difficulty.EASY, route.getDifficulty());
        assertEquals(25, route.getScore());
    }
}