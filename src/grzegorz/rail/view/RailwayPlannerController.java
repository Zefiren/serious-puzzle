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
import grzegorz.rail.model.Interactable;
import grzegorz.rail.model.Scenario;
import grzegorz.rail.model.Signal;
import grzegorz.rail.model.SolutionCmd;
import grzegorz.rail.model.SolutionManager;
import grzegorz.rail.model.Switch;
import grzegorz.rail.model.TrackSection;
import grzegorz.rail.model.Train;
import grzegorz.rail.model.SolutionCmd.CommandType;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class RailwayPlannerController {

	// solution
	@FXML
	private TableView<SolutionCmd> stepsTable;
	@FXML
	private TableColumn<SolutionCmd, String> stepNumColumn;
	@FXML
	private TableColumn<SolutionCmd, String> stepColumn;
	@FXML
	private Button stepDelButton;
	@FXML
	private Button solutionFinishButton;

	// notification
	@FXML
	private AnchorPane notifAnchor;
	@FXML
	private Label notifTitle;
	@FXML
	private Label notifMessage;
	@FXML
	private Button closeButton;

	@FXML
	private AnchorPane scenarioAnchor;

	@FXML
	private AnchorPane stepsAnchor;

	@FXML
	private SplitPane splitPane;

	public Canvas scenarioCanvas;

	private CanvasPane canvasPane;

	private AnchorPane overlay;
	private Pane notifierPane;
	// private GraphicsContext gc;
	List<SolutionCmd> selected = new ArrayList<SolutionCmd>();

	private Map<Train, StackPane> trainBox = new HashMap<Train, StackPane>();

	private List<Point> shapes = new ArrayList<Point>();
	private Map<Interactable<?>, ArrayList<Button>> scenarioBtns;
	// Reference to the main application.
	private MainApp mainApp;

	private int sizeCube = 10;
	private int trackLength = 200;
	private int trackVertGap = 100;

	private double trackLengthRatio = 0.9;

	private int hPadding = 200;
	private int vPadding = 100;

	// the scale for the size of signal graphical objects in relation to track
	// length
	// eg X = 4 -> size = trackLength / 4
	private int signalScaleFraction = 5;

	List<Integer> trackID = new ArrayList<Integer>();
	Scenario scenario;
	SolutionManager solMgr;

	protected boolean deletingFlag;
	private boolean initialFlag = true;
	private boolean animationFlag = false;

	private boolean midstepFlag = false;
	private TrackSection tempTarget;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public RailwayPlannerController() {
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
		stepsTable.setItems(solMgr.getSolution());
	}

	// while(it.hasNext())
	// {
	// ((SolutionCmd) it).undoStep();
	// System.out.println("REMOVING "+step.getStep().getValue());
	// });

	/**
	 * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		solMgr = new SolutionManager();
		// Initialize the person table with the two columns.
		stepNumColumn.setCellValueFactory(cellData -> cellData.getValue().getStepNumberString().concat( cellData.getValue().getStepType()));
		stepColumn.setCellValueFactory(cellData -> cellData.getValue().getStep());
		stepsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		stepsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		stepsTable.getColumns().forEach(col -> {
			col.setSortable(false);
		});
		stepNumColumn.setReorderable(false);
		stepColumn.setReorderable(false);

		stepsTable.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
			if (!deletingFlag) {
				selected.clear();
				selected.addAll((stepsTable.getSelectionModel().getSelectedItems()));

				System.out.println(selected.size() + " SELECTED");
			}
		});
		CanvasPane canvasPane = new CanvasPane(MainApp.MINIMUM_WINDOW_WIDTH, MainApp.MINIMUM_WINDOW_HEIGHT);
		scenarioAnchor.getChildren().add(canvasPane);
		canvasPane.setPadding(new Insets(0));
		AnchorPane.setTopAnchor(canvasPane, 0.0);
		AnchorPane.setBottomAnchor(canvasPane, 0.0);
		AnchorPane.setLeftAnchor(canvasPane, 0.0);
		AnchorPane.setRightAnchor(canvasPane, 0.0);
		scenarioCanvas = canvasPane.getCanvas();
		scenarioAnchor.setOnScroll((ScrollEvent event) -> {

			sizeCube += (event.getDeltaY() / (Math.abs(event.getDeltaY()))) * 2;

			System.out.println(sizeCube);
		});

		scenarioBtns = new HashMap<Interactable<?>, ArrayList<Button>>();
		// AnimationTimer loop = new AnimationTimer() {
		// @Override
		// public void handle(long now) {
		overlay = new AnchorPane();
		scenarioAnchor.getChildren().add(overlay);
		AnchorPane.setTopAnchor(overlay, 0.0);
		AnchorPane.setBottomAnchor(overlay, 0.0);
		AnchorPane.setLeftAnchor(overlay, 0.0);
		AnchorPane.setRightAnchor(overlay, 0.0);

		GraphicsContext g = scenarioCanvas.getGraphicsContext2D();
		if (scenario != null) drawScenario(g);

		overlay.getChildren().add(notifAnchor);
		notifAnchor.setVisible(false);

		AnchorPane.setTopAnchor(notifAnchor, 10.0);
		AnchorPane.setRightAnchor(notifAnchor, 10.0);

		stepsAnchor.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.29));
		stepsAnchor.minWidthProperty().bind(splitPane.widthProperty().multiply(0.29));
		stepNumColumn.minWidthProperty().bind(stepsTable.widthProperty().multiply(0.25));
		stepNumColumn.maxWidthProperty().bind(stepsTable.widthProperty().multiply(0.25));

		scenarioAnchor.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				trackLength = (int) (newSceneWidth.doubleValue() * 0.8) / scenario.getWidth();
				hPadding = (int) (newSceneWidth.doubleValue() * 0.1);
				if (scenario != null) drawScenario(g);
			}
		});
		scenarioAnchor.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				trackVertGap = (int) (newSceneHeight.doubleValue() * 0.8) / scenario.getHeight();
				vPadding = (int) (newSceneHeight.doubleValue() * 0.1);
				if (scenario != null) drawScenario(g);
			}
		});

		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				notifAnchor.setVisible(false);
			}
		});

		stepDelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (selected.size() > 0) {
					deletingFlag = true;
					stepsTable.getSelectionModel().clearSelection();

					if (selected.size() == 1) solMgr.removeStep(selected.get(0));
					else solMgr.removeSteps(selected);

					deletingFlag = false;
					if (scenario != null) drawScenario(g);
				}
			}
		});

		solutionFinishButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (int i = solMgr.getSolution().size() - 1; i >= 0; i--) {
					solMgr.getSolution().get(i).undoStep();
				}
				// solMgr.getSolution().forEach(step -> step.undoStep());
				mainApp.SwitchToAnimation(solMgr);
			}
		});

		/*scenarioAnchor.setOnMouseClicked(event -> {
			double x = event.getX(), y = event.getY();
			shapes.add(new Point((int) x, (int) y));
			System.out.println(scenario.getTrack(new Point(1, 1)).getClass());
			((Switch) scenario.getTrack(new Point(1, 1))).setDiverging(true);
			drawScenario(g);
		});*/

	}

	private void drawScenario(GraphicsContext gc) {

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, scenarioCanvas.getWidth(), scenarioCanvas.getHeight());
		gc.setFill(Color.BLUE);
		for (Point p : shapes) {
			gc.fillRect(p.x, p.y, sizeCube, sizeCube);
		}

		Set<Entry<Point, TrackSection>> tracks = scenario.getTrackSet();
		Iterator<Entry<Point, TrackSection>> iterator = tracks.iterator();
		while (iterator.hasNext()) {
			Entry<Point, TrackSection> mentry = iterator.next();
			drawTrack(gc, mentry.getValue());
		}

		Set<Entry<TrackSection, Signal>> signals = scenario.getSignalSet();
		Iterator<Entry<TrackSection, Signal>> iterator1 = signals.iterator();
		while (iterator1.hasNext()) {
			Entry<TrackSection, Signal> mentry = iterator1.next();
			drawSignal(gc, mentry.getValue());
		}
		if (initialFlag || animationFlag) {
			List<Train> trains = scenario.getTrains();
			Iterator<Train> iterator2 = trains.iterator();
			while (iterator2.hasNext()) {
				Train train = iterator2.next();
				if (animationFlag) updateTrain(train);
				else createTrain(train);
			}
			animationFlag = true;
			initialFlag = false;
		}
	}

	private void setNotification(String title, String message, boolean fading) {
		notifAnchor.setVisible(true);
		if (title != null) notifTitle.setText(title);
		if (message != null) notifMessage.setText(message);
		if (fading) {
			FadeTransition ft = new FadeTransition(Duration.millis(3000), notifAnchor);
			ft.setFromValue(1.0);
			ft.setToValue(0.0);
			ft.play();
			ft.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					notifAnchor.setOpacity(1);
					notifAnchor.setVisible(false);
				}
			});
		}
	}

	private void createOccupyStep(TrackSection ts, Train tr) {
		if (ts != null) {
			tempTarget = ts;
			midstepFlag = true;
			setNotification("Step : Occupies", "Selected TC" + ts.getTsID() + ".\nSelect a train to complete step.", false);
		}
		if (tr != null && midstepFlag) {
			midstepFlag = false;
			SolutionCmd newCmd = new SolutionCmd(tempTarget, tr, 0);
			solMgr.addStep(newCmd);
			setNotification("Step : Occupies", "Occupies step added to solution.", true);
			ts = null;
			tr = null;
			stepsTable.refresh();

			// notifAnchor.setVisible(false);
		}
	}

	private void createTrain(Train tr) {
		StackPane trainPane = new StackPane();
		double width = trackLength / 3;
		double height = trackVertGap / 6;
		Polygon newTrainPol = new Polygon(0.0, 0.0, 0.0, height, width, height, width + width / 3, height / 2, width, 0.0);

		// newTrainArrow.layoutXProperty().bind(newTrain.layoutXProperty().add( (tr.getHeadingDirection() == Direction.right) ? newTrain.widthProperty() : newTrain.widthProperty().multiply(0.0) ) );
		System.out.println("hello train at " + tr.getLocation().getLocation());
		overlay.getChildren().add(trainPane);
		trainPane.setTranslateX(tr.getLocation().getLocation().getX() * trackLength + hPadding + trackLength / 2);
		trainPane.setTranslateY(tr.getLocation().getLocation().getY() * trackVertGap + vPadding - height / 2);
		newTrainPol.setFill(Color.CYAN);
		newTrainPol.setOpacity(0.8);
		newTrainPol.getStyleClass().add("poly-train");

		Rotate rotate = new Rotate(0, (width * 1.5) / 2, height / 2);
		if(tr.getHeadingDirection()== Direction.left)
			rotate.setAngle(180);
		newTrainPol.getTransforms().add(rotate);


		Label trainInfo = new Label(tr.getDestination().getLabel());
		trainInfo.getStyleClass().add("label-train");
		int fontSize = (int) (height * 0.8);
		trainInfo.setStyle("-fx-font-size: " + fontSize + "px");

		trainPane.getChildren().addAll(newTrainPol, trainInfo);
		trainPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				createOccupyStep(null, tr);
			}
		});
		trainBox.put(tr, trainPane);
	}

	private void updateTrain(Train tr) {
		StackPane trainPane = trainBox.get(tr);
		Polygon trainPoly = (Polygon) trainPane.lookup(".poly-train");
		double width = trackLength / 3;
		double height = trackVertGap / 6;

		trainPoly.getPoints().setAll(0.0, 0.0, 0.0, height, width, height, width * 1.5, height / 2, width, 0.0);
		Rotate rotate = (Rotate) trainPoly.getTransforms().get(0);
		if (tr.getHeadingDirection() == Direction.left) {

			// Angle of rotation for the train
			rotate.setAngle( 180);

			// Pivot point of the train poly
			rotate.setPivotX((width * 1.5) / 2);
			rotate.setPivotY(height / 2);
		}

		trainPane.setTranslateX(tr.getLocation().getLocation().getX() * trackLength + hPadding + trackLength / 3);
		trainPane.setTranslateY(tr.getLocation().getLocation().getY() * trackVertGap + vPadding - height / 2);

		int fontSize = (int) (height *0.8);
		Node trainInfo = trainPane.lookup(".label-train");
		trainInfo.setStyle("-fx-font-size: " + fontSize + "px");
	}

	private void drawSignal(GraphicsContext gc, Signal sig) {
		// CREA.TE GRAPHICS using GC
		double diameter = (trackLength / signalScaleFraction);
		Point sigLoc = new Point(sig.getSignalTC().getLocation().x * trackLength + hPadding, (int) (sig.getSignalTC().getLocation().y * trackVertGap + vPadding - diameter * 1.5));
		Color sigColour;

		if (sig.isClear()) {
			sigColour = Color.GREEN;
		} else {
			sigColour = Color.RED;
		}

		double oldWidth = gc.getLineWidth();
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(2);
		gc.setFill(Color.WHITE);
		if (sig.getDirection() == Direction.left) {
			sigLoc.x += trackLength * trackLengthRatio - diameter;
			gc.strokeLine(sigLoc.x, sigLoc.y + diameter * 0.5, sigLoc.x - diameter * 0.3, sigLoc.y + diameter * 0.5);
			gc.strokeLine(sigLoc.x - diameter * 0.3, sigLoc.y + diameter * 0.5, sigLoc.x - diameter * 0.3, sigLoc.y + diameter);
			gc.fillPolygon(new double[] { sigLoc.x - diameter * 0.2, sigLoc.x - diameter * 0.4, sigLoc.x - diameter * 0.3 }, new double[] { sigLoc.y + diameter, sigLoc.y + diameter, sigLoc.y + diameter * 1.5 }, 3);
		} else {
			gc.strokeLine(sigLoc.x + diameter, sigLoc.y + diameter * 0.5, sigLoc.x + diameter * 1.3, sigLoc.y + diameter * 0.5);
			gc.strokeLine(sigLoc.x + diameter * 1.3, sigLoc.y + diameter * 0.5, sigLoc.x + diameter * 1.3, sigLoc.y + diameter);
			gc.fillPolygon(new double[] { sigLoc.x + diameter * 1.2, sigLoc.x + diameter * 1.4, sigLoc.x + diameter * 1.3 }, new double[] { sigLoc.y + diameter, sigLoc.y + diameter, sigLoc.y + diameter * 1.5 }, 3);
		}
		gc.setFill(sigColour);
		gc.strokeOval(sigLoc.x, sigLoc.y, diameter, diameter);
		gc.setStroke(sigColour);
		gc.strokeOval(sigLoc.x + diameter * 0.1, sigLoc.y + diameter * 0.1, diameter * 0.8, diameter * 0.8);
		if (sig.isClear()) {
			gc.fillRect(sigLoc.x + diameter * 0.2, sigLoc.y + diameter * 0.4, diameter * 0.6, diameter * 0.2);
		} else {
			gc.fillRect(sigLoc.x + diameter * 0.4, sigLoc.y + diameter * 0.2, diameter * 0.2, diameter * 0.6);
		}

		gc.setLineWidth(oldWidth);
		gc.setStroke(Color.WHITE);

		// CREATE BUTTON FOR SIGNAL using Node Button
		if (!scenarioBtns.containsKey(sig)) {
			Button sigLabel = new Button("SIG" + sig.getId());
			overlay.getChildren().add(sigLabel);
			sigLabel.layoutXProperty().set(sigLoc.x + trackLength * 0.3);
			sigLabel.layoutYProperty().set(sigLoc.y + trackVertGap * 0.05);
			sigLabel.getStyleClass().add("scen");
			sigLabel.setMaxSize(50, 30);

			scenarioBtns.put(sig, new ArrayList<Button>());
			scenarioBtns.get(sig).add(sigLabel);

		} else {
			Button sigLabel = scenarioBtns.get(sig).get(0);
			sigLabel.layoutXProperty().set(sigLoc.x);
			sigLabel.layoutYProperty().set(sigLoc.y - diameter * 1.5);

		}

		scenarioBtns.get(sig).get(0).setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				SolutionCmd newCmd = new SolutionCmd(sig, !sig.isClear());
				solMgr.addStep(newCmd);
				sig.setClear(!sig.isClear());
				GraphicsContext g = scenarioCanvas.getGraphicsContext2D();
				drawScenario(g);
				setNotification("Step : Signal", "Signal Set step added to solution.", true);
			}

		});
	}

	private void drawTrack(GraphicsContext gc, TrackSection ts) {
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(3);

		Point loc = ts.getLocation();
		// Create TS button
		if (!scenarioBtns.containsKey(ts)) {
			String tcString = "TC" + ts.getTsID();

			Button tsLabel = new Button(tcString);
			overlay.getChildren().add(tsLabel);
			tsLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength * 0.3);
			tsLabel.layoutYProperty().set(loc.y * trackVertGap + vPadding + trackVertGap * 0.05);
			tsLabel.getStyleClass().add("scen");
			tsLabel.setMaxSize(55, 30);

			scenarioBtns.put(ts, new ArrayList<Button>());
			scenarioBtns.get(ts).add(tsLabel);

		} else {
			Button tsLabel = scenarioBtns.get(ts).get(0);
			tsLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength * 0.3);
			tsLabel.layoutYProperty().set(loc.y * trackVertGap + vPadding + trackVertGap * 0.05);

		}
		scenarioBtns.get(ts).get(0).setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				createOccupyStep(ts, null);
				GraphicsContext g = scenarioCanvas.getGraphicsContext2D();
				drawScenario(g);
			}
		});

		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			double xPts[];
			double yPts[];
			int ystart = loc.y * trackVertGap + vPadding;
			int yLevel = 0;
			int xstart = loc.x * trackLength + hPadding;

			if (s.isDiverging()) {
				gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding, loc.x * trackLength + hPadding + (trackLength * trackLengthRatio * (2.0 / 3.0)), loc.y * trackVertGap + vPadding);
			} else {
				gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding, loc.x * trackLength + hPadding + (trackLength * trackLengthRatio), loc.y * trackVertGap + vPadding);
				yLevel = (int) (trackVertGap * 0.1);
			}

			if (s.getSwitchDirection() == Direction.right) {
				xPts = new double[] { xstart, (int) (trackLength * 0.2) + xstart, (int) (trackLength * trackLengthRatio) + xstart };
				if (s.getTurnDirection() == Direction.right) yPts = new double[] { yLevel + ystart, yLevel + ystart, trackVertGap + ystart };
				else yPts = new double[] { -yLevel + ystart, -yLevel + ystart, -trackVertGap + ystart };
			} else {
				xPts = new double[] { xstart, (int) (trackLength * 0.7) + xstart, (int) (trackLength * trackLengthRatio) + xstart };
				if (s.getTurnDirection() == Direction.left) yPts = new double[] { trackVertGap + ystart, ystart + yLevel, ystart + yLevel };
				else yPts = new double[] { -trackVertGap + ystart, ystart - yLevel, ystart - yLevel };
			}

			gc.strokePolyline(xPts, yPts, 3);
			double yBtn;
			if ((loc.y * trackVertGap + vPadding) > yPts[2]) yBtn = loc.y * trackVertGap + vPadding;
			else yBtn = yPts[2];

			Button tsLabel = scenarioBtns.get(ts).get(0);
			tsLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength * 0.3);
			tsLabel.layoutYProperty().set(yBtn + trackVertGap * 0.05);

			if (scenarioBtns.get(ts).size() < 2) {
				Button swLabel = new Button("SW" + s.getSwitchID());

				overlay.getChildren().add(swLabel);

				swLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength * 0.3);
				swLabel.layoutYProperty().set(yBtn + trackVertGap * 0.2);
				swLabel.getStyleClass().add("scen");
				swLabel.setMaxSize(50, 30);

				scenarioBtns.get(ts).add(swLabel);

			} else {

				Button swLabel = scenarioBtns.get(ts).get(1);
				swLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength * 0.3);
				swLabel.layoutYProperty().set(yBtn + trackVertGap * 0.2);
			}
			scenarioBtns.get(s).get(1).setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					SolutionCmd newCmd = new SolutionCmd(s, !s.isDiverging());
					solMgr.addStep(newCmd);
					s.setDiverging(!s.isDiverging());
					GraphicsContext g = scenarioCanvas.getGraphicsContext2D();
					drawScenario(g);
					setNotification("Step : Switch", "Switch Set step added to solution.", true);
				}
			});
			/*
			 * Button swLabel = scenarioBtns.get(ts).get(1);
			 * swLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength *
			 * 0.3);
			 * swLabel.layoutYProperty().set(tsLabel.getBoundsInParent().getMaxY()+20);
			 */
		} else {
			if (ts.getLabel() != null) {
				Point labelPos = new Point();
				if (ts.isRightEnding()) {
					labelPos.setLocation(loc.x * trackLength + hPadding + trackLength * 1.3, loc.y * trackVertGap + vPadding);
				} else {
					labelPos.setLocation(loc.x * trackLength + hPadding - trackLength * 0.3, loc.y * trackVertGap + vPadding);
				}
				System.out.println(labelPos + " is label position for " + ts.getLabel());
				gc.setFont(new Font(trackVertGap / 2));
				gc.fillText(ts.getLabel(), labelPos.getX(), labelPos.getY());
			}
			gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding, loc.x * trackLength + hPadding + (trackLength * trackLengthRatio), loc.y * trackVertGap + vPadding);
			gc.setTextBaseline(VPos.CENTER);
			gc.setTextAlign(TextAlignment.CENTER);
		}

	}

	private static class CanvasPane extends Pane {

		private final Canvas canvas;

		public CanvasPane(double width, double height) {
			canvas = new Canvas(width, height);
			getChildren().add(canvas);
		}

		public Canvas getCanvas() {
			return canvas;
		}

		@Override
		protected void layoutChildren() {
			super.layoutChildren();

			final double x = snappedLeftInset();
			final double y = snappedTopInset();
			final double w = snapSize(getWidth()) - x - snappedRightInset();
			final double h = snapSize(getHeight()) - y - snappedBottomInset();
			canvas.setLayoutX(x);
			canvas.setLayoutY(y);
			canvas.setWidth(w);
			canvas.setHeight(h);
		}
	}

}