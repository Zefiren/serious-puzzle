package grzegorz.rail;

import java.io.IOException;

import grzegorz.rail.model.Scenario;
import grzegorz.rail.model.ScenarioMaker;
import grzegorz.rail.model.SolutionManager;
import grzegorz.rail.view.EndScreenController;
import grzegorz.rail.view.MenuController;
import grzegorz.rail.view.RailwayAnimationController;
import grzegorz.rail.view.RailwayPlannerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Scenario scenario;
	private SolutionManager solution;
	private SolutionManager plannerSolution;
	private boolean userAnimation;

	public final static int MINIMUM_WINDOW_WIDTH = 800;
	public final static int MINIMUM_WINDOW_HEIGHT = 600;

	/**
	 * The data as an observable list of Persons.
	 */

	/**
	 * Constructor
	 */
	public MainApp() {
		scenario = ScenarioMaker.createScenario(1);
	}

	public void setScenarioData(Scenario scenarioChosen) {
		scenario = scenarioChosen;

	}

	public Scenario getScenarioData() {
		return scenario;

	}

	public SolutionManager getSolutionData(boolean userSolution) {
		if (userSolution) {
			return solution;
		} else {
			return plannerSolution;
		}
	}

	public void setSolutionData(boolean userSolution, SolutionManager solution) {
		if (userSolution) {
			this.solution = solution;
		} else {
			this.plannerSolution = solution;
		}
	}

	public boolean isUserAnimation() {
		return userAnimation;
	}

	public void setUserAnimation(boolean userAnimation) {
		this.userAnimation = userAnimation;
	}

	public void SwitchToAnimation(SolutionManager sol) {
		solution = sol;
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SolutionAnimator.fxml"));
			AnchorPane solutionPlanner = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(solutionPlanner);

			// Give the controller access to the main app.
			RailwayAnimationController controller = loader.getController();
			controller.setMainApp(this);

			Scene scene = rootLayout.getScene();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Solution Animation");
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SwitchToMenu() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Menu.fxml"));
			AnchorPane solutionAnimator = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(solutionAnimator);

			// Give the controller access to the main app.
			MenuController controller = loader.getController();

			FXMLLoader loader2 = new FXMLLoader();
			loader2.setLocation(MainApp.class.getResource("view/MenuPane.fxml"));
			controller.menuPane = (AnchorPane) loader2.load();
			loader2.setController(controller);

			controller.setMainApp(this);

			Scene scene = rootLayout.getScene();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Solution Planning");
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SwitchToPlanner() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SolutionPlanner.fxml"));
			AnchorPane solutionAnimator = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(solutionAnimator);

			// Give the controller access to the main app.
			RailwayPlannerController controller = loader.getController();
			controller.setMainApp(this);

			Scene scene = rootLayout.getScene();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Solution Planning");
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SwitchToEndScreen() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/EndScreen.fxml"));
			AnchorPane solutionAnimator = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(solutionAnimator);

			// Give the controller access to the main app.
			EndScreenController controller = loader.getController();
			controller.setMainApp(this);

			Scene scene = rootLayout.getScene();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Solution Planning");
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setMinWidth(MINIMUM_WINDOW_WIDTH);
		this.primaryStage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
		this.primaryStage.setWidth(MINIMUM_WINDOW_WIDTH);
		this.primaryStage.setHeight(MINIMUM_WINDOW_HEIGHT);
		System.out.println("INIT WIDTH = " + this.primaryStage.getWidth());
		// Set the application icon.
		this.primaryStage.getIcons().add(new Image("file:resources/images/if_address-book_299084.png"));

		initRootLayout();

		userAnimation = true;
		SwitchToMenu();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * Returns the main stage.
	 *
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

}