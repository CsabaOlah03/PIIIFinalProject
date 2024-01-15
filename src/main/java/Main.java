import java.io.*;
import java.sql.*;
import java.util.*;

import java.sql.Connection;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import org.w3c.dom.Text;

import static java.lang.System.exit;
public class Main extends Application{

/*  Ash nazg durbatulûk,
    ash nazg gimbatul,
    Ash nazg thrakatulûk
    agh burzum-ishi krimpatul.*/
    private static final Scanner theOneRing = new Scanner(System.in);
    private static LinkedList<ClimbingGym> gyms = new LinkedList<>();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/CompetitionManager";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";


    public static Type enumType(Integer number){
        switch (number){
            case 1: return Type.QUALIFICATIONS;
            case 2: return Type.FINALS;
        }
        return Type.QUALIFICATIONS;
    }

    public static Integer readNumber(OutputDevice outputDevice){
        Integer number;
        try {
            number = Integer.parseInt(theOneRing.nextLine());
        } catch (NoSuchElementException n) {
            n.printStackTrace();
            outputDevice.writeToConsole("No puedo senior");
            return -1;
        } catch (NumberFormatException e) {
            outputDevice.writeToConsole("INVALID INPUT: please use digits 0-9");
            return -1;
        }
        return number;
    }
    public static Integer removeRoute(ClimbingGym gym, OutputDevice outputDevice, CompetitionManager competitionManager, Integer numberOfRoutes) {
        outputDevice.writeToConsole("Enter ID of route you want to remove: ");

        Integer routeID = -1;
        while (routeID == -1) {
            routeID = readNumber(outputDevice);
        }

        try {
            competitionManager.removeRoute(gym, routeID);
        } catch (RouteNotFoundException e) {
            outputDevice.writeToConsole(e.getMessage());
        }

        return --numberOfRoutes;
    }

    private static Integer removeContestants(ClimbingGym gym, OutputDevice outputDevice, CompetitionManager competitionManager, int numberOfContestants) {
        outputDevice.writeToConsole("Enter ID of contestant you want to remove: ");

        Integer contestantID = -1;
        while (contestantID == -1) {
            contestantID = readNumber(outputDevice);
        }

        try{
            competitionManager.removeContestant(gym, contestantID);
        } catch (ContestantNotFoundException err) {
            outputDevice.writeToConsole(err.getMessage());
        }
        return --numberOfContestants;
    }

    public static Integer readContestants(ClimbingGym gym, Integer numberOfContestants, OutputDevice outputDevice, InputDevice inputDevice, CompetitionManager competitionManager){
        try {
            // Input data to create a new route
            outputDevice.writeToConsole(numberOfContestants + " contestants left to add: \n First name:");
            String firstName = inputDevice.readName();
            outputDevice.writeToConsole("Last name: ");
            String lastName = inputDevice.readName();
            outputDevice.writeToConsole("Age: ");
            Integer age = inputDevice.readAge();

            // Create new contestant climber
            ContestantClimber contestantClimber = new ContestantClimber(firstName,lastName,age,gym.getRoutes());

            // Add route to current gym
            competitionManager.addContestant(gym,contestantClimber);
            numberOfContestants--;
        } catch (NoSuchElementException n) {
            n.printStackTrace();
            outputDevice.writeToConsole("No puedo senior");
            return numberOfContestants;
        } catch (NumberFormatException e) {
            outputDevice.writeToConsole("INVALID INPUT: please use digits 0-9");
            return numberOfContestants;
        }
        return numberOfContestants;
    }

    public static Integer readRoute(ClimbingGym gym, Integer numberOfRoutes, OutputDevice outputDevice, InputDevice inputDevice, CompetitionManager competitionManager){
        try {
            // Input data to create a new route
            outputDevice.writeToConsole(numberOfRoutes + " routes left to add: \n Number of holds:");
            Integer numberOfHolds = inputDevice.readNumberOfHolds();
            outputDevice.writeToConsole("Difficulty (1-EASY 2-INTERMEDIATE 3-HARD 4-HARD_TO_DIFFICULT 5-DIFFICULT): ");
            Integer routeDifficulty = inputDevice.readDifficulty();
            outputDevice.writeToConsole("Type (1-QUALIFICATIONS 2-FINALS): ");
            Integer routeType = inputDevice.readType();

            // Transform Integers to enums
            Difficulty difficulty = enumDifficulty(routeDifficulty);
            Type type = enumType(routeType);

            // Create new route
            Route route = new Route(numberOfHolds, type, difficulty);

            // Add route to current gym
            competitionManager.addRoute(gym,route);
            numberOfRoutes--;
        } catch (NoSuchElementException n) {
            n.printStackTrace();
            outputDevice.writeToConsole("No puedo senior");
            return numberOfRoutes;
        } catch (NumberFormatException e) {
            outputDevice.writeToConsole("INVALID INPUT: please use digits 0-9");
            return numberOfRoutes;
        }
        return numberOfRoutes;
    }

    public static Difficulty enumDifficulty(Integer number){
        switch (number){
            case 1: return Difficulty.EASY;
            case 2: return Difficulty.INTERMEDIATE;
            case 3: return Difficulty.HARD;
            case 4: return Difficulty.HARD_TO_DIFFICULT;
            case 5: return Difficulty.VERY_DIFFICULT;
        }
        return Difficulty.EASY;
    }
    public static ClimbingGym addGym(OutputDevice outputDevice, CompetitionManager competitionManager){
        outputDevice.writeToConsole("Enter gym name:");
        String name = "";
        try {
            name = theOneRing.nextLine();
           // name = theOneRing.nextLine();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        ClimbingGym gym = competitionManager.addGym(name);
        return gym;
    }
    public static void menu(InputDevice inputDevice, OutputDevice outputDevice){
        int choice = -1;
        int currentGym = 0;
        CompetitionManager competitionManager = new CompetitionManager(inputDevice,outputDevice);
        while(choice != 0){

            outputDevice.printMenu(gyms,currentGym);

            try {
                choice = Integer.parseInt(theOneRing.nextLine());
                outputDevice.writeToConsole("You chose option " + choice);
            } catch (NoSuchElementException n) {
                n.printStackTrace();
                outputDevice.writeToConsole("No puedo senior");
                return;
            } catch (NumberFormatException e) {
                outputDevice.writeToConsole("INVALID INPUT: please use digits 0-9");
                continue;
            }

            switch (choice){
                case 1: //Add a gym
                    gyms.add(addGym(outputDevice,competitionManager));
                    break;
                case 2: //Select a gym

                    // Display the gyms
                    competitionManager.displayGyms(gyms);

                    // Get the name of the gym you want to select
                    String name = ""; /* Doesn't wait to read the input */
                    name = theOneRing.nextLine();

                    String text = name;

                    // Find the gym with that name
                    Optional<ClimbingGym> result = gyms.stream()
                            .filter(gym -> gym.getName().equals(text))
                            .findFirst();

                    if (result.isPresent()) {
                        ClimbingGym foundObject = result.get();
                        // Do something with the found object
                        System.out.println("Found object: " + foundObject);
                        currentGym = gyms.indexOf(foundObject);
                    } else {
                        System.out.println("Object with name " + name + " not found");
                    }

                    break;
                case 3: // Add route(s) to current gym (one or more)
                     {
                        outputDevice.writeToConsole("How many routes would you like to add?");
                        int numberOfRoutes = -1;
                        while (numberOfRoutes == -1)
                            numberOfRoutes = readNumber(outputDevice);

                        while (numberOfRoutes > 0) {
                            numberOfRoutes = readRoute(gyms.get(currentGym), numberOfRoutes, outputDevice, inputDevice, competitionManager);
                        }
                        break;
                    }
                case 4: // Remove route(s) from current gym (requires routeID)
                    outputDevice.writeToConsole("How many routes would you like to remove?");

                    // Read number of routes to remove from the current gym
                    int numberOfRoutes = -1;
                    while(numberOfRoutes == -1 || numberOfRoutes > gyms.get(currentGym).getRoutes().size()) {

                        // Check if the user input the correct values
                        try {
                            numberOfRoutes = readNumber(outputDevice);
                            if(numberOfRoutes > gyms.get(currentGym).getRoutes().size()) {
                                throw new IllegalBoundsException("Invalid input, there are only " + gyms.get(currentGym).getRoutes().size() + " routes");
                            }
                        } catch (IllegalBoundsException err){
                            numberOfRoutes = -1;
                            outputDevice.writeToConsole(err.getMessage());
                        } catch (NumberFormatException err) {
                            numberOfRoutes = -1;
                            err.printStackTrace();
                        }
                    }

                    // Remove routes
                    while(numberOfRoutes > 0) {

                        outputDevice.writeToConsole(numberOfRoutes + " routes left to remove:");
                        numberOfRoutes = removeRoute(gyms.get(currentGym),outputDevice,competitionManager,numberOfRoutes);
                        // Add update function for updating routes list for all contestant climbers
                    }

                    break;
                case 5: { // Add contestant(s) to current gym (one or more)

                    outputDevice.writeToConsole("How many contestants would you like to add?");

                    // Read number of contestatns to add to the current gym
                    int numberOfContestants = -1;
                    while (numberOfContestants == -1)
                        numberOfContestants = readNumber(outputDevice);

                    // Add request information for <numberOfContestants> contestants
                    while (numberOfContestants > 0) {
                        numberOfContestants = readContestants(gyms.get(currentGym), numberOfContestants, outputDevice, inputDevice, competitionManager);
                    }
                    break;
                }
                case 6: { // Remove contestant from current gym (requires contestantID)
                    outputDevice.writeToConsole("How many contestants would you like to remove?");

                    // Read number of contestant to remove from the current gym
                    int numberOfContestants = -1;
                    while (numberOfContestants == -1 || numberOfContestants > gyms.get(currentGym).getContestantClimbers().size()) {
                        // Check if the user input the correct values
                        try {
                            numberOfContestants = readNumber(outputDevice);
                            if (numberOfContestants > gyms.get(currentGym).getRoutes().size()) {
                                throw new IllegalBoundsException("Invalid input, there are only " + gyms.get(currentGym).getContestantClimbers().size() + " climbers");
                            }
                        } catch (IllegalBoundsException err){
                            numberOfRoutes = -1;
                            outputDevice.writeToConsole(err.getMessage());
                        } catch (NumberFormatException err) {
                            numberOfRoutes = -1;
                            outputDevice.writeToConsole(err.getMessage());
                        }
                    }

                    // Remove contestant
                    while (numberOfContestants > 0) {

                        outputDevice.writeToConsole(numberOfContestants + " contestants left to remove:");
                        numberOfContestants = removeContestants(gyms.get(currentGym), outputDevice, competitionManager, numberOfContestants);
                        // Add update function for updating routes list for all contestant climbers
                    }

                    break;
                }
                case 7: // Display climbers enrolled in competition held at current gym
                    competitionManager.displayContestants(gyms.get(currentGym));
                    break;
                case 8: // Display routes present at current gym
                    competitionManager.displayRoutes(gyms.get(currentGym));
                    break;
                case 9: // Load hardcoded info

                    gyms.add(inputDevice.getGym()[0]);
                    gyms.add(inputDevice.getGym()[1]);
                    //Add routes
                    competitionManager.addRoute(gyms.getLast(),inputDevice.getRoutes());
                    //Add climbers
                    competitionManager.registration(gyms.getLast(),inputDevice.getClimbers());
                    break;
                default:
            }
        }
        //return competitionManager;
    }

    private static void testStartNew() {
        System.out.println("Test Case 1: Start a new competition with fresh data");
        String[] args = {"startNew"};

        InputDevice inputDevice = new InputDevice(theOneRing);
        OutputDevice consoleOutput = new OutputDevice(System.out);
        CompetitionManager CM = new CompetitionManager(inputDevice, consoleOutput);
        CompetitionManager compManag = new CompetitionManager(inputDevice, consoleOutput);
        Tests tests = new Tests(compManag);

        gyms.clear();

        File file = null;

        try {
            file = new File("src\\test\\java\\test");
        } catch (NullPointerException e) {
            e.printStackTrace();
            consoleOutput.writeToConsole("Error: No file found");
            System.exit(1);
        }

        main(args);

        System.out.println("Test Case 1 completed.\n");
    }

    private static void testStartFromOld() {
        System.out.println("Test Case 2: Start a competition from old data");
        String[] args = {"startFromOld"};

        InputDevice inputDevice = new InputDevice(theOneRing);
        OutputDevice consoleOutput = new OutputDevice(System.out);
        CompetitionManager CM = new CompetitionManager(inputDevice, consoleOutput);
        CompetitionManager compManag = new CompetitionManager(inputDevice, consoleOutput);
        Tests tests = new Tests(compManag);

        gyms.clear();
        gyms.add(new ClimbingGym("Gym1"));
        gyms.add(new ClimbingGym("Gym2"));

        File file = null;

        try {
            file = new File("src\\test\\java\\test");
        } catch (NullPointerException e) {
            e.printStackTrace();
            consoleOutput.writeToConsole("Error: No file found");
            System.exit(1);
        }

        main(args);

        System.out.println("Test Case 2 completed.\n");
    }

    private static void testLaunchGUI(String[] args) {
        System.out.println("Test Case 3: Launch the GUI");
        String[] guiArgs = {"GUI"};

        InputDevice inputDevice = new InputDevice(theOneRing);
        OutputDevice consoleOutput = new OutputDevice(System.out);
        CompetitionManager CM = new CompetitionManager(inputDevice, consoleOutput);
        CompetitionManager compManag = new CompetitionManager(inputDevice, consoleOutput);
        Tests tests = new Tests(compManag);

        // Mock gyms with some data
        gyms.clear();
        gyms.add(new ClimbingGym("Gym1"));
        gyms.add(new ClimbingGym("Gym2"));

        // Call the main method
        main(guiArgs);

        System.out.println("Test Case 3 completed.\n");
    }
    public static void main(String[] args) {
        InputDevice inputDevice = new InputDevice(theOneRing);
        OutputDevice consoleOutput = new OutputDevice(System.out);
        CompetitionManager CM = new CompetitionManager(inputDevice,consoleOutput);
        CompetitionManager compManag = new CompetitionManager(inputDevice,consoleOutput);
        Tests tests = new Tests(compManag);

        File file = null;

        try {
            //file = new File("C:\\Users\\andio\\InteliJ Projects\\University\\Year II\\Programing III\\CompetitionClimbing\\src\\test\\java\\test");
            file = new File("src\\test\\java\\test");
        } catch (NullPointerException e) {
            e.printStackTrace();
            consoleOutput.writeToConsole("Error: No file found");
            exit(1);
        }

        switch (args[0]) {
            case "startNew":
                ObjectMapper objectMapperAlpha = new ObjectMapper();

                consoleOutput.writeToConsole("---==Fresh start==---");
                menu(inputDevice,consoleOutput);

                try{
                    objectMapperAlpha.writeValue(file,gyms);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

                break;

            case "startFromOld":
                ObjectMapper objectMapperBeta = new ObjectMapper();

                try{
                    gyms = objectMapperBeta.readValue(file, new TypeReference<LinkedList<ClimbingGym>>() {});
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

                consoleOutput.writeToConsole("---==Loaded data from files==---");
                menu(inputDevice,consoleOutput);

                try{
                    objectMapperBeta.writeValue(file,gyms);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }

                break;

            case "GUI":
                launch(args);
                break;

            case "tests":
                testStartNew();

                testStartFromOld();

                testLaunchGUI(args);
                break;
        }
    }

    public void logout(Stage stage){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you want to save before exiting?");

        if (alert.showAndWait().get() == ButtonType.OK){
            System.out.println("You successfully logged out");
            stage.close();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        final String[] user = {""};
        final String[] pass = {""};
        final String[] firstName = {""};
        final String[] lastName = {""};

        Group root = new Group();
        Scene scene = new Scene(root);

        TextField username = new TextField();
        TextField password = new TextField();
        username.setText("");
        password.setText("");

        Button signUp = new Button("Sign Up");
        Button signIn = new Button("Sign In");

        signIn.setOnAction(event -> {
            user[0] = username.getText();
            pass[0] = password.getText();

            if(user[0].equals("admin") && pass[0].equals("admin")){
                openAdminScene(primaryStage);
            } else {
                if (authenticateUser(user[0], pass[0])) {
                    openClimbingRoutesScene(primaryStage, user[0]);
                } else {
                    showAlert("User not found", "Please sign up to access climbing routes.");
                }
            }
        });

        signUp.setOnAction(event -> {
            createUserDetailsScene(primaryStage, user, pass, firstName, lastName);
        });


        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));

        grid.add(new Label("Username: "), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password: "), 0, 1);
        grid.add(password, 1, 1);

        grid.add(signUp, 0, 3);
        grid.add(signIn, 1, 3);

        root.getChildren().add(grid);

        primaryStage.setTitle("Competition Manager");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            logout(primaryStage);
        });
    }

    private void createUserDetailsScene(Stage primaryStage, String[] user, String[] pass, String[] firstName, String[] lastName) {
        GridPane userDetailsGrid = new GridPane();
        userDetailsGrid.setVgap(5);
        userDetailsGrid.setHgap(10);
        userDetailsGrid.setPadding(new Insets(5, 5, 5, 5));

        TextField username = new TextField();
        TextField password = new TextField();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField gymIDTF = new TextField();

        Button submitButton = new Button("Submit");

        submitButton.setOnAction(event -> {

            user[0] = username.getText();
            pass[0] = password.getText();
            firstName[0] = firstNameField.getText();
            lastName[0] = lastNameField.getText();
            String gymID = gymIDTF.getText();

            System.out.println("Username: " + user[0]);
            System.out.println("Password: " + pass[0]);
            System.out.println("First Name: " + firstName[0]);
            System.out.println("Last Name: " + lastName[0]);
            System.out.println("Gym ID: " + gymID);

            if (!signUpUser(user[0], pass[0], firstName[0], lastName[0], gymID)) {
                showAlert("SignUp error", "The user is already signed up");
            } else {
                updateContestantClimberTable(gymID, lastName[0]);
                openClimbingRoutesScene(primaryStage, user[0]);
            }
        });

        userDetailsGrid.add(new Label("Username: "), 0, 0);
        userDetailsGrid.add(username, 1, 0);
        userDetailsGrid.add(new Label("Password: "), 0, 1);
        userDetailsGrid.add(password, 1, 1);
        userDetailsGrid.add(new Label("First Name: "), 0, 2);
        userDetailsGrid.add(firstNameField, 1, 2);
        userDetailsGrid.add(new Label("Last Name: "), 0, 3);
        userDetailsGrid.add(lastNameField, 1, 3);
        userDetailsGrid.add(new Label("Gym ID: "), 0, 4);
        userDetailsGrid.add(gymIDTF, 1, 4);

        userDetailsGrid.add(submitButton, 1, 5);

        Scene userDetailsScene = new Scene(userDetailsGrid);
        primaryStage.setScene(userDetailsScene);
    }


    private boolean signUpUser(String username, String password, String firstName, String lastName, String gymID) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (isUsernameTaken(connection, username)) {
                showAlert("Username Taken", "This username is already taken. Please choose a different one.");
                return false;
            }

            String insert = "INSERT INTO user (username, password, firstName, lastName) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, firstName);
                preparedStatement.setString(4, lastName);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int userID = generatedKeys.getInt(1);
                            updateContestantClimberTable(gymID, userID, lastName);
                        }
                    }
                }
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    private void updateContestantClimberTable(String gymID, int userID, String lastName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String insert = "INSERT INTO contestantClimber (qualified, finalPoints, idgym, idcontestantClimber) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                preparedStatement.setBoolean(1, false);
                preparedStatement.setInt(2, 0);
                preparedStatement.setString(3, gymID);
                preparedStatement.setInt(4, userID);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateContestantClimberTable(String gymID, String lastName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String insert = "INSERT INTO contestantClimber (qualified, finalPoints, idgym, lastName) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                preparedStatement.setBoolean(1, false);
                preparedStatement.setInt(2, 0);
                preparedStatement.setString(3, gymID);
                preparedStatement.setString(4, lastName);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    private boolean isUsernameTaken(Connection connection, String username) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openClimbingRoutesScene(Stage primaryStage, String username) {
        System.out.println("Opening Climbing Routes Scene for user: " + username);

        Label gymNameLabel = new Label("Gym Name:");
        TextField gymNameTextField = new TextField();

        Label routeIDLabel = new Label("Route ID:");
        TextField routeIDTextField = new TextField();

        Label numberOfTriesLabel = new Label("Number of Tries:");
        TextField numberOfTriesTextField = new TextField();

        Button submitButton = new Button("Submit");

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Route Added");
        successAlert.setHeaderText(null);

        submitButton.setOnAction(event -> {

            String gymName = gymNameTextField.getText();
            String routeID = routeIDTextField.getText();
            String numberOfTries = numberOfTriesTextField.getText();


            if (!gymName.isEmpty() && !routeID.isEmpty() && !numberOfTries.isEmpty()) {
                System.out.println("Gym Name: " + gymName);
                System.out.println("Route ID: " + routeID);
                System.out.println("Number of Tries: " + numberOfTries);

                Thread databaseThread = new Thread(() -> {

                    boolean success = insertRouteIntoDatabase(gymName, routeID, numberOfTries);

                    Platform.runLater(() -> {
                        if (success) {

                            successAlert.setContentText("Route added successfully!");
                            successAlert.showAndWait();
                        } else {
                            showAlert("Error", "Failed to add route to the database.");
                        }
                    });
                });

                databaseThread.start();

            } else {
                showAlert("Error", "All fields must be filled.");
            }
        });

        VBox layout = new VBox(10,
                gymNameLabel,
                gymNameTextField,
                routeIDLabel,
                routeIDTextField,
                numberOfTriesLabel,
                numberOfTriesTextField,
                submitButton
        );

        Scene scene = new Scene(new Group(layout), 400, 300);
        primaryStage.setTitle("Climbing Routes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean insertRouteIntoDatabase(String gymName, String routeID, String numberOfTries) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openAdminScene(Stage primaryStage) {
        System.out.println("Opening Admin Scene");

        Button addGymButton = new Button("Add Gym");
        Button addRouteButton = new Button("Add Route");
        Button removeGymButton = new Button("Remove Gym");
        Button removeRouteButton = new Button("Remove Route");
        Button removeContestantClimberButton = new Button("Remove ContestantClimber");

        addGymButton.setOnAction(event -> openAddGymScene(primaryStage));
        addRouteButton.setOnAction(event -> openAddRouteScene(primaryStage));
        removeGymButton.setOnAction(event -> openRemoveGymScene(primaryStage));
        removeRouteButton.setOnAction(event -> openRemoveRouteScene(primaryStage));
        removeContestantClimberButton.setOnAction(event -> openRemoveContestantClimberScene(primaryStage));

        VBox adminLayout = new VBox(10,
                addGymButton,
                addRouteButton,
                removeGymButton,
                removeRouteButton,
                removeContestantClimberButton
        );

        Scene adminScene = new Scene(adminLayout, 400, 300);
        primaryStage.setScene(adminScene);
    }

    private boolean checkGym(String gymName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM gym WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, gymName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openAddGymScene(Stage primaryStage) {
        GridPane addGymGrid = new GridPane();
        addGymGrid.setVgap(5);
        addGymGrid.setHgap(10);
        addGymGrid.setPadding(new Insets(5, 5, 5, 5));

        TextField gymNameTF = new TextField();
        String gymName[] = {""};

        Button addGym = new Button("Add Gym");

        addGym.setOnAction(event -> {
            gymName[0] = gymNameTF.getText();
            
            System.out.println("gymName: " + gymName[0]);

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
                if (checkGym(gymName[0])) {
                    showAlert("Error", "The gym already exists in the database");
                } else {
                    String insert = "INSERT INTO gym (name) VALUES (?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                        preparedStatement.setString(1, gymName[0]);

                        preparedStatement.executeUpdate();
                        
                        openAdminScene(primaryStage);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        });

        addGymGrid.add(new Label("Add Gym: "), 0, 0);
        addGymGrid.add(gymNameTF, 0, 1);
        addGymGrid.add(addGym, 1, 1);

        Scene userDetailsScene = new Scene(addGymGrid);
        primaryStage.setScene(userDetailsScene);
    }

    private boolean checkRoute(String routeID) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM route WHERE idroute = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, routeID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openAddRouteScene(Stage primaryStage) {
        GridPane addRouteGrid = new GridPane();
        addRouteGrid.setVgap(5);
        addRouteGrid.setHgap(10);
        addRouteGrid.setPadding(new Insets(5, 5, 5, 5));

        TextField routeIDTF = new TextField();
        TextField routeTypeTF = new TextField();
        TextField routeDifficultyTF = new TextField();
        TextField routeNumOfHoldsTF = new TextField();
        TextField routeScoreTF = new TextField();
        TextField gymIDTF = new TextField();

        String routeID[] = {""};
        String routeType[] = {""};
        String routeDifficulty[] = {""};
        String routeNumOfHolds[] = {""};
        String routeScore[] = {""};
        String gymName[] = {""};
        String gymID[] = {""};

        Button addRoute = new Button("Add Route");

        addRoute.setOnAction(event -> {
            routeID[0] = routeIDTF.getText();
            routeType[0] = routeTypeTF.getText();
            routeDifficulty[0] = routeDifficultyTF.getText();
            routeNumOfHolds[0] = routeNumOfHoldsTF.getText();
            routeScore[0] = routeScoreTF.getText();
            gymID[0] = gymIDTF.getText();

            System.out.println("routeID: " + routeID[0]);
            System.out.println("routeType: " + routeType[0]);
            System.out.println("routeDifficulty: " + routeDifficulty[0]);
            System.out.println("routeNumOfHolds: " + routeNumOfHolds[0]);
            System.out.println("routeScore: " + routeScore[0]);
            System.out.println("gymName: " + gymName[0]);
            System.out.println("gymID: " + gymID[0]);

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
                if (checkRoute(routeID[0])) {
                    showAlert("Error", "The route already exists in the gym " + gymName );
                } else {
                    String insert = "INSERT INTO route (idroute, idgym, type, difficulty, numberOfHolds, score) VALUES (?, ?, ?, ?, ? ,?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insert)) {
                        preparedStatement.setString(1, routeID[0]);
                        preparedStatement.setString(2, gymID[0]);
                        preparedStatement.setString(3, routeType[0]);
                        preparedStatement.setString(4, routeDifficulty[0]);
                        preparedStatement.setString(5, routeNumOfHolds[0]);
                        preparedStatement.setString(6, routeScore[0]);

                        preparedStatement.executeUpdate();

                        openAdminScene(primaryStage);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        });

        addRouteGrid.add(new Label("Add RouteID: "), 0, 0);
        addRouteGrid.add(routeIDTF, 0, 1);
        addRouteGrid.add(new Label("Add RouteType: "), 0, 2);
        addRouteGrid.add(routeTypeTF, 0, 3);
        addRouteGrid.add(new Label("Add RouteDifficulty: "), 0, 4);
        addRouteGrid.add(routeDifficultyTF, 0, 5);
        addRouteGrid.add(new Label("Add RouteNumberOfHolds: "), 0, 6);
        addRouteGrid.add(routeNumOfHoldsTF, 0, 7);
        addRouteGrid.add(new Label("Add RouteScore: "), 0, 8);
        addRouteGrid.add(routeScoreTF, 0, 9);
        addRouteGrid.add(new Label("Add GymID: "), 1, 0);
        addRouteGrid.add(gymIDTF, 1, 1);

        addRouteGrid.add(addRoute, 1, 12);

        Scene userDetailsScene = new Scene(addRouteGrid);
        primaryStage.setScene(userDetailsScene);
    }

    private void openRemoveGymScene(Stage primaryStage) {
        GridPane removeGymGrid = new GridPane();
        removeGymGrid.setVgap(5);
        removeGymGrid.setHgap(10);
        removeGymGrid.setPadding(new Insets(5, 5, 5, 5));

        TextField gymNameTF = new TextField();
        String gymName[] = {""};

        Button removeGym = new Button("Remove Gym");

        removeGym.setOnAction(event -> {
            gymName[0] = gymNameTF.getText();

            System.out.println("gymName to remove: " + gymName[0]);

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
                if (!checkGym(gymName[0])) {
                    showAlert("Error", "The gym does not exist in the database");
                } else {
                    String delete = "DELETE FROM gym WHERE name = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
                        preparedStatement.setString(1, gymName[0]);

                        preparedStatement.executeUpdate();

                        openAdminScene(primaryStage);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        });

        removeGymGrid.add(new Label("Remove Gym: "), 0, 0);
        removeGymGrid.add(gymNameTF, 0, 1);
        removeGymGrid.add(removeGym, 1, 1);

        Scene removeGymScene = new Scene(removeGymGrid);
        primaryStage.setScene(removeGymScene);
    }

    private void openRemoveRouteScene(Stage primaryStage) {
        GridPane removeRouteGrid = new GridPane();
        removeRouteGrid.setVgap(5);
        removeRouteGrid.setHgap(10);
        removeRouteGrid.setPadding(new Insets(5, 5, 5, 5));

        TextField routeIDTF = new TextField();
        String routeID[] = {""};

        Button removeRoute = new Button("Remove Route");

        removeRoute.setOnAction(event -> {
            routeID[0] = routeIDTF.getText();

            System.out.println("routeID to remove: " + routeID[0]);

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
                if (!checkRoute(routeID[0])) {
                    showAlert("Error", "The route does not exist in the database");
                } else {
                    String delete = "DELETE FROM route WHERE idroute = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(delete)) {
                        preparedStatement.setString(1, routeID[0]);

                        preparedStatement.executeUpdate();

                        openAdminScene(primaryStage);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        });

        removeRouteGrid.add(new Label("Remove RouteID: "), 0, 0);
        removeRouteGrid.add(routeIDTF, 0, 1);
        removeRouteGrid.add(removeRoute, 1, 1);

        Scene removeRouteScene = new Scene(removeRouteGrid);
        primaryStage.setScene(removeRouteScene);
    }


    private void openRemoveContestantClimberScene(Stage primaryStage) {
        GridPane removeContestantClimberGrid = new GridPane();
        removeContestantClimberGrid.setVgap(5);
        removeContestantClimberGrid.setHgap(10);
        removeContestantClimberGrid.setPadding(new Insets(5, 5, 5, 5));

        TextField contestantClimberIDTF = new TextField();

        Button removeContestantClimber = new Button("Remove Contestant Climber");

        removeContestantClimber.setOnAction(event -> {
            String contestantClimberID = contestantClimberIDTF.getText();

            System.out.println("Contestant Climber ID: " + contestantClimberID);

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                if (checkContestantClimber(contestantClimberID)) {
                    removeContestantClimberFromDatabase(connection, contestantClimberID);
                    openAdminScene(primaryStage);
                } else {
                    showAlert("Error", "Contestant Climber not found in the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        removeContestantClimberGrid.add(new Label("Contestant Climber ID: "), 0, 0);
        removeContestantClimberGrid.add(contestantClimberIDTF, 1, 0);
        removeContestantClimberGrid.add(removeContestantClimber, 1, 1);

        Scene removeContestantClimberScene = new Scene(removeContestantClimberGrid);
        primaryStage.setScene(removeContestantClimberScene);
    }

    private boolean checkContestantClimber(String contestantClimberID) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM contestantClimber WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, contestantClimberID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void removeContestantClimberFromDatabase(Connection connection, String contestantClimberID) throws SQLException {
        String deleteQuery = "DELETE FROM contestantClimber WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, contestantClimberID);
            preparedStatement.executeUpdate();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}