package grzegorz.rail.view;

import grzegorz.rail.MainApp;
import javafx.fxml.FXML;

public class RootController {

	// Reference to the main application.
	private MainApp mainApp;
	private boolean maximized = false;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public RootController() {
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table

	}

	/**
	 * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	@FXML
	private void returnToMainMenu() {
		mainApp.SwitchToMenu();
	}

	@FXML
	private void closeApplication() {
		System.exit(0);
	}



	@FXML
	private void aboutPopup() {
		mainApp.ShowAboutScreen();
	}

}