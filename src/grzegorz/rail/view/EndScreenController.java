package grzegorz.rail.view;

import grzegorz.rail.MainApp;
import grzegorz.rail.model.Scenario;
import grzegorz.rail.model.SolutionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EndScreenController {

	@FXML
	private Label winnerMessage;

	@FXML
	private Label trainCountLabel;

	@FXML
	private Label userStepsNumber;

	@FXML
	private Label plannerStepsNumber;

	@FXML
	private Label scoreLabel;

	@FXML
	private Button editSolutionButton;

	@FXML
	private Button viewPlannerSolutionButton;

	@FXML
	private Button mainMenuButton;

	// Reference to the main application.
	private MainApp mainApp;

	Scenario scenario;
	SolutionManager userSolution;
	SolutionManager plannerSolMgr;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public EndScreenController() {
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		scenario = mainApp.getScenarioData();
		userSolution = mainApp.getSolutionData(true);
		plannerSolMgr = mainApp.getSolutionData(false);
		setLabels();
	}

	/**
	 * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	private void setLabels() {
		if (plannerSolMgr.getLength() < userSolution.getLength()) {
			winnerMessage.setText("Winner: Planner Solution Shorter");
		} else if (plannerSolMgr.getLength() > userSolution.getLength()) {
			winnerMessage.setText("Winner: User Solution Shorter");
		} else {
			winnerMessage.setText("Draw: Equal Length Solutions");
		}

		trainCountLabel.setText( scenario.getTrains().size() + " Trains in Scenario");

		userStepsNumber.setText(userSolution.getLength()+"");
		plannerStepsNumber.setText(plannerSolMgr.getLength()+"");

		//if planner sol LONGER THAN user sol, add points
		//if planner sol SHORTER THAN user sol, subtract points
		scoreLabel.setText(500 * scenario.getTrains().size() +  100 * (plannerSolMgr.getLength() - userSolution.getLength()) +" pts");
//		scoreLabel.setText(500 * scenario.getTrains().size() +  100 * userSolution.getLength() +" pts");
	}

	@FXML
	private void returnToPlanning() {
		for (int i = 0; i < userSolution.getLength(); i++) {
			userSolution.getSolution().get(i).performStep();
		}
		mainApp.SwitchToPlanner();
	}

	@FXML
	private void showPlannerSolution() {
		mainApp.setUserAnimation(false);
		mainApp.SwitchToAnimation(userSolution);
	}

	@FXML
	private void returnToMainMenu() {
		mainApp.SwitchToMenu();
	}


}