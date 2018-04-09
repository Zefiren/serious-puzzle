package grzegorz.rail.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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

	private void readFile() {
//		final File f = new File(getClass().getProtectionDomain().getClassLoader().get.getPath());
        String s = null;

		System.out.println(getClass().getResource(".").getPath());
		File f = new File(getClass().getResource(".").getPath());
		try {
			File python = new File("src/grzegorz/rail/view/planner.py");
	        System.out.println(python.exists()); // true
	        System.out.println(python.getAbsolutePath());

//			Process p = Runtime.getRuntime().exec("py planner.py domain.txt problem.pddl plan.json",null,f);
			Process p = Runtime.getRuntime().exec("py src/grzegorz/rail/view/planner.py src/grzegorz/rail/view/domain.txt src/grzegorz/rail/view/problem1.pddl src/grzegorz/rail/view/plan.json");

			BufferedReader stdInput = new BufferedReader(new
	                 InputStreamReader(p.getInputStream()));

	            BufferedReader stdError = new BufferedReader(new
	                 InputStreamReader(p.getErrorStream()));

	            // read the output from the command
	            System.out.println("Here is the standard output of the command:\n");
	            while ((s = stdInput.readLine()) != null) {
	                System.out.println(s);
	            }

	            // read any errors from the attempted command
	            System.out.println("Here is the standard error of the command (if any):\n");
	            while ((s = stdError.readLine()) != null) {
	                System.out.println(s);
	            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	private void read() {
		readFile();
	}

	@FXML
	private void aboutPopup() {
		mainApp.ShowAboutScreen();
	}

}