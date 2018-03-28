package grzegorz.rail;

import java.io.IOException;

import grzegorz.rail.model.Scenario;
import grzegorz.rail.model.ScenarioMaker;
import grzegorz.rail.model.SolutionManager;
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

	public final static int MINIMUM_WINDOW_WIDTH = 800;
	public final static int MINIMUM_WINDOW_HEIGHT = 600;

	/**
	 * The data as an observable list of Persons.
	 */

	/**
	 * Constructor
	 */
	public MainApp() {
		scenario = ScenarioMaker.createScenario();
	}

	public Scenario getScenarioData() {
		return scenario;

	}

	public SolutionManager getSolutionData() {
		return solution;

	}

	public void SwitchToAnimation(SolutionManager sol) {
		solution = sol;
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SolutionAnimator.fxml"));
			AnchorPane solutionAnimator = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(solutionAnimator);

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

		showPersonOverview();
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
	 * Shows the person overview inside the root layout.
	 */
	public void showPersonOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			primaryStage.setTitle("Solution Planning");
			// Give the controller access to the main app.
			RailwayPlannerController controller = loader.getController();
			controller.setMainApp(this);

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