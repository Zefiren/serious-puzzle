package grzegorz.rail.view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import grzegorz.rail.MainApp;
import grzegorz.rail.model.Direction;
import grzegorz.rail.model.Scenario;
import grzegorz.rail.model.ScenarioMaker;
import grzegorz.rail.model.Signal;
import grzegorz.rail.model.SolutionCmd;
import grzegorz.rail.model.SolutionManager;
import grzegorz.rail.model.Switch;
import grzegorz.rail.model.TrackSection;
import grzegorz.rail.model.Train;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

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