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
import grzegorz.rail.model.PlannerSolutions;
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
import javafx.scene.effect.GaussianBlur;
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

public class MenuController {

	@FXML
	private AnchorPane scenarioAnchor;


	public Canvas scenarioCanvas;

	private CanvasPane canvasPane;

	private AnchorPane overlay;
	public AnchorPane menuPane;
	// private GraphicsContext gc;

	private Map<Train, StackPane> trainBox = new HashMap<Train, StackPane>();

	// Reference to the main application.
	private MainApp mainApp;

	private int trackLength = 200;
	private int trackVertGap = 100;

	private double trackLengthRatio = 0.9;

	private int hPadding = 200;
	private int vPadding = 100;
	private int vStartPos = 0;

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
	public MenuController() {
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

		scenarioAnchor.getChildren().add(menuPane);
		AnchorPane.setTopAnchor(menuPane, 0.0);
		AnchorPane.setBottomAnchor(menuPane, 0.0);
		AnchorPane.setLeftAnchor(menuPane, 0.0);
		AnchorPane.setRightAnchor(menuPane, 0.0);
		VBox vb = (VBox) menuPane.getChildren().get(0);
		for (int i = 0; i< vb.getChildren().size(); i++) {
			Button btn = (Button) vb.getChildren().get(i);
			int x = i;
			btn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					mainApp.setScenarioData(ScenarioMaker.createScenario(x));
					PlannerSolutions.setScenario(mainApp.getScenarioData());
					mainApp.setSolutionData(false,PlannerSolutions.createSolution(x));
					mainApp.setSolutionData(true,new SolutionManager());
					mainApp.setAttemptNumber(1);
					mainApp.setShowingHint(false);
					mainApp.SwitchToPlanner();
				}
			});


		}
	}

	/**
	 * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		CanvasPane canvasPane = new CanvasPane(MainApp.MINIMUM_WINDOW_WIDTH, MainApp.MINIMUM_WINDOW_HEIGHT);
		scenarioAnchor.getChildren().add(canvasPane);
		canvasPane.setPadding(new Insets(0));
		AnchorPane.setTopAnchor(canvasPane, 0.0);
		AnchorPane.setBottomAnchor(canvasPane, 0.0);
		AnchorPane.setLeftAnchor(canvasPane, 0.0);
		AnchorPane.setRightAnchor(canvasPane, 0.0);
		scenarioCanvas = canvasPane.getCanvas();


		overlay = new AnchorPane();
		scenarioAnchor.getChildren().add(overlay);
		AnchorPane.setTopAnchor(overlay, 0.0);
		AnchorPane.setBottomAnchor(overlay, 0.0);
		AnchorPane.setLeftAnchor(overlay, 0.0);
		AnchorPane.setRightAnchor(overlay, 0.0);



		GraphicsContext g = scenarioCanvas.getGraphicsContext2D();
		if (scenario != null) drawScenario(g);

		GaussianBlur gb = new GaussianBlur();
//		mb.setRadius(15.0f);
//		mb.setAngle(45.0f);

		overlay.setEffect(gb);
		scenarioCanvas.setEffect(gb);

		scenarioAnchor.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				trackLength = (int) (newSceneWidth.doubleValue() * 0.8) / scenario.getWidth();
				hPadding = (int) (newSceneWidth.doubleValue() * 0.1);
				scenarioCanvas.setWidth(newSceneWidth.doubleValue());
				if (scenario != null) drawScenario(g);
			}
		});
		scenarioAnchor.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				System.out.println(scenario.getHeight() + " is height");
				scenarioCanvas.setHeight(newSceneHeight.doubleValue());
				if(scenario.getHeight()>1) {
					trackVertGap = (int) (newSceneHeight.doubleValue() * 0.8) / scenario.getHeight();
					vPadding = (int) (newSceneHeight.doubleValue() * 0.1);
					vStartPos = trackVertGap / 4;
				}else {
					trackVertGap = (int) (newSceneHeight.doubleValue() * 0.5) / scenario.getHeight();
					vPadding = (int) (newSceneHeight.doubleValue() * 0.25);
					vStartPos = trackVertGap/2;
					System.out.println("half size" + trackVertGap +"/"+ newSceneHeight);
				}
				if (scenario != null) drawScenario(g);
			}
		});

	}

	private void drawScenario(GraphicsContext gc) {

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, scenarioCanvas.getWidth(), scenarioCanvas.getHeight());
		gc.setFill(Color.BLUE);

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




	private void createTrain(Train tr) {
		StackPane trainPane = new StackPane();
		double width = trackLength / 3;
		double height = trackVertGap / 6;
		Polygon newTrainPol = new Polygon(0.0, 0.0, 0.0, height, width, height, width + width / 3, height / 2, width, 0.0);

		// newTrainArrow.layoutXProperty().bind(newTrain.layoutXProperty().add( (tr.getHeadingDirection() == Direction.right) ? newTrain.widthProperty() : newTrain.widthProperty().multiply(0.0) ) );
		System.out.println("hello train at " + tr.getLocation().getLocation());
		overlay.getChildren().add(trainPane);
		trainPane.setTranslateX(tr.getLocation().getLocation().getX() * trackLength + hPadding + trackLength / 2);
		trainPane.setTranslateY(tr.getLocation().getLocation().getY() * trackVertGap + vPadding - height / 2  + vStartPos);
		newTrainPol.setFill(Color.CYAN);
		newTrainPol.setOpacity(0.8);
		newTrainPol.getStyleClass().add("poly-train");

		Rotate rotate = new Rotate(0, (width * 1.5) / 2, height / 2);
		if (tr.getHeadingDirection() == Direction.left) rotate.setAngle(180);
		newTrainPol.getTransforms().add(rotate);

		Label trainInfo = new Label(tr.getDestination().getLabel());
		trainInfo.getStyleClass().add("label-train");
		int fontSize = (int) (height * 0.8);
		trainInfo.setStyle("-fx-font-size: " + fontSize + "px");

		trainPane.getChildren().addAll(newTrainPol, trainInfo);
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
			rotate.setAngle(180);

			// Pivot point of the train poly
			rotate.setPivotX((width * 1.5) / 2);
			rotate.setPivotY(height / 2);
		}

		trainPane.setTranslateX(tr.getLocation().getLocation().getX() * trackLength + hPadding + trackLength / 3);
		trainPane.setTranslateY(tr.getLocation().getLocation().getY() * trackVertGap + vPadding - height / 2  + vStartPos);

		int fontSize = (int) (height * 0.8);
		Node trainInfo = trainPane.lookup(".label-train");
		trainInfo.setStyle("-fx-font-size: " + fontSize + "px");
	}

	private void drawSignal(GraphicsContext gc, Signal sig) {
		// CREA.TE GRAPHICS using GC
		double diameter = (trackLength / signalScaleFraction);
		Point sigLoc = new Point(sig.getSignalTC().getLocation().x * trackLength + hPadding, (int) (sig.getSignalTC().getLocation().y * trackVertGap + vPadding - diameter * 1.5 + vStartPos));
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

	}

	private void drawTrack(GraphicsContext gc, TrackSection ts) {
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(3);

		Point loc = ts.getLocation();

		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			double xPts[];
			double yPts[];
			int ystart = loc.y * trackVertGap + vPadding  + vStartPos;
			int yLevel = 0;
			int xstart = loc.x * trackLength + hPadding;

			if (s.isDiverging()) {
				if (s.getSwitchDirection() == Direction.left)
					gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding + vStartPos, loc.x * trackLength + hPadding + (trackLength * trackLengthRatio * (2.0 / 3.0)), loc.y * trackVertGap + vPadding + vStartPos);
				else
					gc.strokeLine(loc.x * trackLength + hPadding + (trackLength * trackLengthRatio * (1.0 / 3.0)), loc.y * trackVertGap + vPadding + vStartPos, loc.x * trackLength + hPadding + (trackLength * trackLengthRatio) , loc.y * trackVertGap + vPadding + vStartPos);

			} else {
				gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding + vStartPos, loc.x * trackLength + hPadding + (trackLength * trackLengthRatio), loc.y * trackVertGap + vPadding + vStartPos);
				yLevel = (int) (trackVertGap * 0.1);
			}

			if (s.getSwitchDirection() == Direction.right) {
				xPts = new double[] { xstart, (int) (trackLength * 0.2) + xstart, (int) (trackLength * trackLengthRatio) + xstart };
				if (s.getTurnDirection() == Direction.right) yPts = new double[] { yLevel + ystart, yLevel + ystart, trackVertGap*0.95 + ystart };
				else yPts = new double[] { -yLevel + ystart, -yLevel + ystart, -trackVertGap*0.95 + ystart };
			} else {
				xPts = new double[] { xstart, (int) (trackLength * 0.7) + xstart, (int) (trackLength * trackLengthRatio) + xstart };
				if (s.getTurnDirection() == Direction.left) yPts = new double[] { trackVertGap*0.95 + ystart, ystart + yLevel, ystart + yLevel };
				else yPts = new double[] { -trackVertGap*0.95 + ystart, ystart - yLevel, ystart - yLevel };
			}

			gc.strokePolyline(xPts, yPts, 3);


		} else {

			/*
			 * Button swLabel = scenarioBtns.get(ts).get(1);
			 * swLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength *
			 * 0.3);
			 * swLabel.layoutYProperty().set(tsLabel.getBoundsInParent().getMaxY()+20);
			 */
			if (ts.getLabel() != null) {
				Point labelPos = new Point();
				if (ts.isRightEnding()) {
					labelPos.setLocation(loc.x * trackLength + hPadding + trackLength * 1.3, loc.y * trackVertGap + vPadding  + vStartPos);
				} else {
					labelPos.setLocation(loc.x * trackLength + hPadding - trackLength * 0.3, loc.y * trackVertGap + vPadding  + vStartPos);
				}
				System.out.println(labelPos + " is label position for " + ts.getLabel());
				gc.setFont(new Font(trackVertGap / 4));
				gc.fillText(ts.getLabel(), labelPos.getX(), labelPos.getY());
			}
			gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding  + vStartPos, loc.x * trackLength + hPadding + (trackLength * trackLengthRatio), loc.y * trackVertGap + vPadding + vStartPos);
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