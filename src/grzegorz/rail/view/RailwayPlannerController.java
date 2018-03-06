package grzegorz.rail.view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import grzegorz.rail.MainApp;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import railwayPlanning.Interactable;
import railwayPlanning.Scenario;
import railwayPlanning.Signal;
import railwayPlanning.SolutionCmd;
import railwayPlanning.SolutionManager;
import railwayPlanning.Switch;
import railwayPlanning.TrackSection;
import railwayPlanning.Train;

public class RailwayPlannerController {

	@FXML
	private TableView<SolutionCmd> stepsTable;
	@FXML
	private TableColumn<SolutionCmd, String> stepNumColumn;
	@FXML
	private TableColumn<SolutionCmd, String> stepColumn;

	@FXML
	private Button stepDelButton;

	@FXML
	private AnchorPane scenarioAnchor;

	@FXML
	private AnchorPane stepsAnchor;

	@FXML
	private SplitPane splitPane;

	public Canvas scenarioCanvas;

	private CanvasPane canvasPane;
	private Pane overlay;
	// private GraphicsContext gc;
	List<SolutionCmd> selected = new ArrayList<SolutionCmd>();

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
	private int layoutVerticalMax = 0;
	private int layoutHorizontalMax = 0;

	private int layoutVerticalMin = 0;
	private int layoutHorizontalMin = 0;

	private int layoutVerticalSize = 0;
	private int layoutHorizontalSize = 0;
	protected boolean deletingFlag;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public RailwayPlannerController() {
	}

	// while(it.hasNext())
	// {
	// ((SolutionCmd) it).undoStep();
	// System.out.println("REMOVING "+step.getStep().getValue());
	// });

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		solMgr = new SolutionManager();
		// Initialize the person table with the two columns.
		stepNumColumn.setCellValueFactory(cellData -> cellData.getValue().getStepNumberString());
		stepColumn.setCellValueFactory(cellData -> cellData.getValue().getStep());
		stepsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		stepsTable.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
			if (!deletingFlag) {
				selected.clear();
				selected.addAll((stepsTable.getSelectionModel().getSelectedItems()));
//						.stream().map(s -> s.getStepNumber())
//						.collect(Collectors.toList())));
				// selected.forEach(step -> System.out.println("#"+step);
				// ));
				System.out.println(selected.size() + " SELECTED");
			}
		});

		stepDelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				deletingFlag = true;
				stepsTable.getSelectionModel().clearSelection();
				solMgr.removeSteps(selected);
				deletingFlag = false;
			}
		});
		System.out.println(scenarioAnchor.getHeight());
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

		createScenario();
		scenarioBtns = new HashMap<Interactable<?>, ArrayList<Button>>();
		// AnimationTimer loop = new AnimationTimer() {
		// @Override
		// public void handle(long now) {
		overlay = new Pane();
		scenarioAnchor.getChildren().add(overlay);
		GraphicsContext g = scenarioCanvas.getGraphicsContext2D();
		drawScenario(g);

		stepsAnchor.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.25));
		stepsAnchor.minWidthProperty().bind(splitPane.widthProperty().multiply(0.25));
		stepNumColumn.minWidthProperty().bind(stepsTable.widthProperty().multiply(0.25));
		stepNumColumn.maxWidthProperty().bind(stepsTable.widthProperty().multiply(0.25));

		scenarioAnchor.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				trackLength = (int) (newSceneWidth.doubleValue() * 0.8) / scenario.getWidth();
				hPadding = (int) (newSceneWidth.doubleValue() * 0.1);
				System.out.println("Width: " + newSceneWidth);
				Number n = (Number) scenarioAnchor.getWidth();
				System.out.println(
						"RATIO: " + newSceneWidth.doubleValue() / (newSceneWidth.doubleValue() + n.doubleValue()));
				// GraphicsContext g = scenarioCanvas.getGraphicsContext2D();
				drawScenario(g);
			}
		});
		scenarioAnchor.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
				System.out.println("scen height" + scenario.getHeight());
				System.out.println("scene size" + scenario.TrackCount());
				trackVertGap = (int) (newSceneHeight.doubleValue() * 0.8) / scenario.getHeight();
				vPadding = (int) (newSceneHeight.doubleValue() * 0.1);
				System.out.println("Height: " + newSceneHeight);
				// GraphicsContext g = scenarioCanvas.getGraphicsContext2D();
				drawScenario(g);
			}
		});

		stepsAnchor.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				System.out.println("Width: " + newSceneWidth);
				Number n = (Number) scenarioAnchor.getWidth();
				System.out.println(
						"RATIO: " + newSceneWidth.doubleValue() / (newSceneWidth.doubleValue() + n.doubleValue()));
			}
		});
		stepsAnchor.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
				System.out.println("Height: " + newSceneHeight);
			}
		});

		scenarioAnchor.setOnMouseClicked(event -> {
			double x = event.getX(), y = event.getY();
			shapes.add(new Point((int) x, (int) y));
			System.out.println(scenario.getTrack(new Point(1, 1)).getClass());
			((Switch) scenario.getTrack(new Point(1, 1))).setDiverging(true);
			drawScenario(g);
		});
		// // Listen for selection changes and show the person details when changed.
		// personTable.getSelectionModel().selectedItemProperty().addListener(
		// (observable, oldValue, newValue) -> showPersonDetails(newValue));

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
	}

	private void drawSignal(GraphicsContext gc, Signal sig) {
		// CREA.TE GRAPHICS using GC
		double diameter = (trackLength / signalScaleFraction);
		Point sigLoc = new Point(sig.getSignalTC().getLocation().x * trackLength + hPadding,
				(int) (sig.getSignalTC().getLocation().y * trackVertGap + vPadding - diameter * 1.5));
		Color left, right;
		if (sig.isFacingLeft()) {
			sigLoc.x += trackLength * trackLengthRatio - diameter * 2;
			if (sig.isClear()) {
				left = Color.GREEN;
				right = Color.BLACK;
			} else {
				left = Color.BLACK;
				right = Color.RED;
			}
		} else {
			if (sig.isClear()) {
				right = Color.GREEN;
				left = Color.BLACK;
			} else {
				right = Color.BLACK;
				left = Color.RED;
			}
		}

		// if (signal.isFacingLeft()) sg.setSignalPosition(new Point((int)
		// tc.getTrackGraphic().getP2().getX() - 50, (int)
		// tc.getTrackGraphic().getP2().getY() - 30));
		// else sg.setSignalPosition(new Point((int)
		// tc.getTrackGraphic().getP1().getX(), (int)
		// tc.getTrackGraphic().getP1().getY() - 30));
		double oldWidth = gc.getLineWidth();
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(2);

		gc.setFill(left);
		gc.fillOval(sigLoc.x, sigLoc.y, diameter, diameter);
		gc.strokeOval(sigLoc.x, sigLoc.y, diameter, diameter);

		gc.setFill(right);
		gc.fillOval(sigLoc.x + diameter, sigLoc.y, diameter, diameter);
		gc.strokeOval(sigLoc.x + diameter, sigLoc.y, diameter, diameter);

		gc.setLineWidth(oldWidth);

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
			Button tsLabel = new Button("TS" + ts.getTsID());
			overlay.getChildren().add(tsLabel);
			tsLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength * 0.3);
			tsLabel.layoutYProperty().set(loc.y * trackVertGap + vPadding + trackVertGap * 0.05);
			tsLabel.getStyleClass().add("scen");
			tsLabel.setMaxSize(50, 30);

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
				SolutionCmd newCmd = new SolutionCmd(ts, scenario.getTrain(0), 0);
				solMgr.addStep(newCmd);
				System.out.println("helloo?");
				// s.setDiverging(!s.isDiverging());
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
				gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding,
						loc.x * trackLength + hPadding + (trackLength * trackLengthRatio * (2.0 / 3.0)),
						loc.y * trackVertGap + vPadding);
			} else {
				gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding,
						loc.x * trackLength + hPadding + (trackLength * trackLengthRatio),
						loc.y * trackVertGap + vPadding);
				yLevel = (int) (trackVertGap * 0.1);
			}

			if (s.isRightDirection()) {
				xPts = new double[] { xstart, (int) (trackLength * 0.2) + xstart,
						(int) (trackLength * trackLengthRatio) + xstart };
				if (s.isTurnRight())
					yPts = new double[] { yLevel + ystart, yLevel + ystart, trackVertGap + ystart };
				else
					yPts = new double[] { -yLevel + ystart, -yLevel + ystart, -trackVertGap + ystart };
			} else {
				xPts = new double[] { xstart, (int) (trackLength * 0.7) + xstart,
						(int) (trackLength * trackLengthRatio) + xstart };
				if (!s.isTurnRight())
					yPts = new double[] { trackVertGap + ystart, ystart + yLevel, ystart + yLevel };
				else
					yPts = new double[] { -trackVertGap + ystart, ystart - yLevel, ystart - yLevel };
			}

			gc.strokePolyline(xPts, yPts, 3);
			double yBtn;
			if ((loc.y * trackVertGap + vPadding) > yPts[2])
				yBtn = loc.y * trackVertGap + vPadding;
			else
				yBtn = yPts[2];

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
				}
			});
			/*
			 * Button swLabel = scenarioBtns.get(ts).get(1);
			 * swLabel.layoutXProperty().set(loc.x * trackLength + hPadding + trackLength *
			 * 0.3);
			 * swLabel.layoutYProperty().set(tsLabel.getBoundsInParent().getMaxY()+20);
			 */
		} else {
			gc.strokeLine(loc.x * trackLength + hPadding, loc.y * trackVertGap + vPadding,
					loc.x * trackLength + hPadding + (trackLength * trackLengthRatio), loc.y * trackVertGap + vPadding);
			gc.setTextBaseline(VPos.CENTER);
			gc.setTextAlign(TextAlignment.CENTER);
		}

	}

	private void createScenario() {
		scenario = new Scenario(layoutHorizontalSize, layoutVerticalSize);

		TrackSection startTs = createScene();
		// need to create grid of track circuits
		// first calculate sizes
		placeTracks(startTs, 0, 0);

		trackID.clear();
		// remake grid locations
		relocateTracks(startTs, layoutHorizontalMin, layoutVerticalMin, scenario);

		layoutHorizontalSize = layoutHorizontalMax - layoutHorizontalMin + 1;
		layoutVerticalSize = layoutVerticalMax - layoutVerticalMin + 1;

		scenario.setWidth(layoutHorizontalSize);
		scenario.setHeight(layoutVerticalSize);

		System.out.println(layoutHorizontalSize + " by " + layoutVerticalSize);
		System.out.println("Number of tracks = " + scenario.TrackCount());
		System.out.println("tracks added:");
		Set<Entry<Point, TrackSection>> tracks = scenario.getTrackSet();
		Iterator<Entry<Point, TrackSection>> iterator = tracks.iterator();
		while (iterator.hasNext()) {
			Entry<Point, TrackSection> mentry = iterator.next();
			System.out.print("key is: " + mentry.getKey() + " & Value is: ");
			System.out.println(mentry.getValue().getTsID() + " & Type is: " + mentry.getValue().getClass());
		}
	}

	private void relocateTracks(TrackSection ts, int horizontalOffset, int verticalOffset, Scenario scen) {
		Point loc = ts.getLocation();
		loc.setLocation(loc.getX() + Math.abs(horizontalOffset), loc.getY() + Math.abs(verticalOffset));
		scen.addTrack(ts);
		trackID.add(ts.getTsID());

		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			// left track if it does not exist in list
			if (!trackID.contains(s.getLeftTrack().getTsID())) {
				relocateTracks(s.getLeftTrack(), horizontalOffset, verticalOffset, scen);
			}
			// right track if it does not exist in list
			if (!trackID.contains(s.getRightTrack().getTsID())) {
				relocateTracks(s.getRightTrack(), horizontalOffset, verticalOffset, scen);
			}
			// additional switch track if it does not exist in list
			if (!trackID.contains(s.getExtraTrack().getTsID())) {
				if (s.isRightDirection()) {
					if (s.isTurnRight()) {
						// RIGHT = RIGHT direction and RIGHT TURN = DOWN direction
						relocateTracks(s.getExtraTrack(), horizontalOffset, verticalOffset, scen);
					} else {
						// NOT RIGHT TURN = UP direction
						relocateTracks(s.getExtraTrack(), horizontalOffset, verticalOffset, scen);
					}
				} else {
					if (s.isTurnRight()) {
						// LEFT = LEFT direction and RIGHT TURN = UP direction
						relocateTracks(s.getExtraTrack(), horizontalOffset, verticalOffset, scen);
					} else {
						// NOT RIGHT TURN = DOWN direction
						relocateTracks(s.getExtraTrack(), horizontalOffset, verticalOffset, scen);
					}
				}
			}
		} else {
			if (ts.isEndTrack()) {
				if (ts.isRightEnding()) {
					if (!trackID.contains(ts.getLeftTrack().getTsID())) {
						relocateTracks(ts.getLeftTrack(), horizontalOffset, verticalOffset, scen);
					}
				} else {
					if (!trackID.contains(ts.getRightTrack().getTsID())) {
						relocateTracks(ts.getRightTrack(), horizontalOffset, verticalOffset, scen);
					}
				}
			} else {
				if (!trackID.contains(ts.getLeftTrack().getTsID())) {
					relocateTracks(ts.getLeftTrack(), horizontalOffset, verticalOffset, scen);
				}

				if (!trackID.contains(ts.getRightTrack().getTsID())) {
					relocateTracks(ts.getRightTrack(), horizontalOffset, verticalOffset, scen);
				}
			}
		}
	}

	private TrackSection createScene() {

		TrackSection start, middle, upRightEnd, middle2, end, newEnd, newEnd2;
		Switch s1, s2;
		middle = new TrackSection(1);
		middle2 = new TrackSection(2);
		start = new TrackSection(0, "start", false, middle);
		end = new TrackSection(4, "end", true, middle2);
		upRightEnd = new TrackSection(5, "upEnd", false, middle2);
		newEnd = new TrackSection(7);// , "endLeft2")//, true, s2);
		newEnd2 = new TrackSection(8);// , "endLeft3")//, true, s2);
		// id, left, right, isRightDir, isRightTurn, extraTrack
		s1 = new Switch(3, 0, middle2, end, false, true, upRightEnd);
		s2 = new Switch(6, 1, newEnd, upRightEnd, false, true, newEnd2);

		upRightEnd.setRightTrack(s1);
		upRightEnd.setLeftTrack(s2);
		upRightEnd.setEndTrack(false);

		end.setLeftTrack(s1);

		middle.setLeftTrack(start);
		middle.setRightTrack(middle2);

		middle2.setLeftTrack(middle);
		middle2.setRightTrack(s1);

		newEnd.setEndTrack(true);
		newEnd.setRightEnding(false);
		newEnd.setRightTrack(s2);

		newEnd2.setEndTrack(true);
		newEnd2.setRightEnding(false);
		newEnd2.setRightTrack(s2);

		Train train = new Train(0, start, end, start);
		scenario.addTrain(train);
		Signal sig1 = new Signal(0, middle, middle2, true);
		Signal sig2 = new Signal(1, end, s1, false);

		List<TrackSection> tracks = new ArrayList<TrackSection>();
		List<Signal> signals = new ArrayList<Signal>();
		tracks.add(start);
		tracks.add(end);
		tracks.add(s1);
		tracks.add(s2);
		tracks.add(upRightEnd);
		tracks.add(middle);
		tracks.add(middle2);
		tracks.add(newEnd);
		tracks.add(newEnd2);

		signals.add(sig1);
		signals.add(sig2);
		scenario.addSignal(sig1);
		scenario.addSignal(sig2);

		return start;
	}

	private void updateScenarioSizes(TrackSection ts, int x, int y) {
		if (x > layoutHorizontalMax)
			layoutHorizontalMax = x;

		if (x < layoutHorizontalMin)
			layoutHorizontalMin = x;

		if (y > layoutVerticalMax)
			layoutVerticalMax = y;

		if (y < layoutVerticalMin)
			layoutVerticalMin = y;

		// System.out.println("next" + ts.getLocation());
		// System.out.println("current" + topLeft.getLocation());
		// if( y <= topLeft.getLocation().y) {
		// topLeft = ts;
		// if(x < topLeft.getLocation().x)
		// topLeft = ts;
		// }

	}

	private void placeTracks(TrackSection ts, int x, int y) {
		ts.setLocation(new Point(x, y));
		updateScenarioSizes(ts, x, y);
		trackID.add(ts.getTsID());
		System.out.println("ID " + ts.getTsID());

		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			// left track if it does not exist in list
			if (!trackID.contains(s.getLeftTrack().getTsID())) {
				placeTracks(s.getLeftTrack(), x - 1, y);
			}
			// right track if it does not exist in list
			if (!trackID.contains(s.getRightTrack().getTsID())) {
				placeTracks(s.getRightTrack(), x + 1, y);
			}
			// additional switch track if it does not exist in list
			if (!trackID.contains(s.getExtraTrack().getTsID())) {
				if (s.isRightDirection()) {
					if (s.isTurnRight()) {
						// RIGHT = RIGHT direction and RIGHT TURN = DOWN direction
						placeTracks(s.getExtraTrack(), x + 1, y + 1);
					} else {
						// NOT RIGHT TURN = UP direction
						placeTracks(s.getExtraTrack(), x + 1, y - 1);
					}
				} else {
					if (s.isTurnRight()) {
						// LEFT = LEFT direction and RIGHT TURN = UP direction
						placeTracks(s.getExtraTrack(), x - 1, y - 1);
					} else {
						// NOT RIGHT TURN = DOWN direction
						placeTracks(s.getExtraTrack(), x - 1, y + 1);
					}
				}
			}
		} else {
			if (ts.isEndTrack()) {
				if (ts.isRightEnding()) {
					if (!trackID.contains(ts.getLeftTrack().getTsID())) {
						placeTracks(ts.getLeftTrack(), x - 1, y);
					}
				} else {
					if (!trackID.contains(ts.getRightTrack().getTsID())) {
						placeTracks(ts.getRightTrack(), x + 1, y);
					}
				}
			} else {
				if (!trackID.contains(ts.getLeftTrack().getTsID())) {
					placeTracks(ts.getLeftTrack(), x - 1, y);
				}

				if (!trackID.contains(ts.getRightTrack().getTsID())) {
					placeTracks(ts.getRightTrack(), x + 1, y);
				}
			}
		}

	}

	/*
	 * private void placeTrack(TrackSection ts, int x, int y) {
	 * ts.setTrackGraphicPoints(x, y, (int) (x + trackLengthStraight * 0.95), y);
	 * ts.setLabelBoxPoint(x + ts.getLabelBox().x, y + ts.getLabelBox().y,
	 * ts.getLabelBox().width, ts.getLabelBox().height); ts.setTextPlace(new Point(x
	 * + ts.getTextPlace().x, y + ts.getTextPlace().y)); // g2d.drawLine(0, 0,
	 * trackLengthStraight, 0);
	 * 
	 * if (ts.getClass() == Switch.class) { Switch s = (Switch) ts;
	 * s.setSwitchLabelBoxPoint(x + s.getSwitchLabelBox().x, y +
	 * s.getSwitchLabelBox().y, s.getSwitchLabelBox().width,
	 * s.getSwitchLabelBox().height); s.setSwitchTextPlace(new Point(x +
	 * s.getSwitchTextPlace().x, y + s.getSwitchTextPlace().y));
	 * 
	 * Polygon eLine = s.getExtraTrackGraphic(); int xs, ys, xe, ye, xm; if
	 * (!s.isRightDirection()) xs = x + (int) (eLine.xpoints[0] * 0.95); else xs = x
	 * + (int) eLine.xpoints[0]; ys = y + (int) eLine.ypoints[0]; xm = (int) (x +
	 * (int) eLine.xpoints[1]);// (int) (eLine.xpoints[2] - eLine.xpoints[0]) *
	 * 0.15); if (s.isRightDirection()) xe = (int) (x + (int) (eLine.xpoints[2] *
	 * 0.95)); // (int) (eLine.xpoints[2] - eLine.xpoints[0]) * 0.95); else xe =
	 * (int) (x + (int) eLine.xpoints[2]); // (int) (eLine.xpoints[2] -
	 * eLine.xpoints[0]) * 0.95); // ys = y ye = y + (int) (eLine.ypoints[2] -
	 * eLine.ypoints[0]); s.setExtraTrackGraphic(new Polygon(new int[] { xs, xm, xe
	 * }, new int[] { ys, ys, ye }, 3)); // new Point(xs, ys),new Point(xm, ye), new
	 * Point(xe, ye))
	 * 
	 * // eLine.setLine(xs,ys,xe,ye);
	 * 
	 * } // g2d.draw(ts.getTrackGraphic()); // // g2d.setPaint(Color.black); //
	 * g2d.setStroke(labelBrush); // // g2d.draw(ts.getLabelBox()); //
	 * g2d.drawString("tc" + ts.getTsID(), x + 90, y + 25);
	 * 
	 * System.out.println("drew " + ts.getTsID()); trackID.add(ts.getTsID()); }
	 */

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

	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		stepsTable.setItems(solMgr.getSolution());
	}

	/**
	 * Fills all text fields to show details about the person. If the specified
	 * person is null, all text fields are cleared.
	 *
	 * @param person
	 *            the person or null
	 */
	/*
	 * private void showPersonDetails(Person person) { if (person != null) { // Fill
	 * the labels with info from the person object.
	 * firstNameLabel.setText(person.getFirstName());
	 * lastNameLabel.setText(person.getLastName());
	 * streetLabel.setText(person.getStreet());
	 * postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
	 * cityLabel.setText(person.getCity());
	 * 
	 * // TODO: We need a way to convert the birthday into a String!
	 * birthdayLabel.setText(DateUtil.format(person.getBirthday())); } else { //
	 * Person is null, remove all the text. firstNameLabel.setText("");
	 * lastNameLabel.setText(""); streetLabel.setText("");
	 * postalCodeLabel.setText(""); cityLabel.setText("");
	 * birthdayLabel.setText(""); } }
	 */
}