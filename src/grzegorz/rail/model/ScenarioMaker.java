package grzegorz.rail.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public final class ScenarioMaker {



	private static Scenario scenario;

	private static int layoutHorizontalSize;

	private static int layoutVerticalSize;
	private static int layoutHorizontalMin;
	private static int layoutHorizontalMax;
	private static int layoutVerticalMin;
	private static int layoutVerticalMax;

	private static List<Integer> trackID;



	private static TrackSection createScene() {

		TrackSection start, middle, upRightEnd, middle2, end, newEnd, newEnd2;
		Switch s1, s2;
		middle = new TrackSection(1);
		middle2 = new TrackSection(2);
		start = new TrackSection(0, "A", false, middle);
		end = new TrackSection(4, "B", true, middle2);
		upRightEnd = new TrackSection(5, "C", false, middle2);
		newEnd = new TrackSection(7);// , "endLeft2")//, true, s2);
		newEnd2 = new TrackSection(8);// , "endLeft3")//, true, s2);
		// id, left, right, isRightDir, isRightTurn, extraTrack
		s1 = new Switch(3, 0, middle2, end, Direction.left, Direction.right, upRightEnd);
		s2 = new Switch(6, 1, newEnd, upRightEnd, Direction.left, Direction.right, newEnd2);

		upRightEnd.setRightTrack(s1);
		upRightEnd.setLeftTrack(s2);
		upRightEnd.setEndTrack(false);
		upRightEnd.setLabel(null);

		end.setLeftTrack(s1);

		middle.setLeftTrack(start);
		middle.setRightTrack(middle2);

		middle2.setLeftTrack(middle);
		middle2.setRightTrack(s1);

		newEnd.setEndTrack(true);
		newEnd.setRightEnding(false);
		newEnd.setRightTrack(s2);
		newEnd.setLabel("D");

		newEnd2.setEndTrack(true);
		newEnd2.setRightEnding(false);
		newEnd2.setRightTrack(s2);
		newEnd2.setLabel("E");

		Train train = new Train(0, start, end, Direction.right);
		Train train2 = new Train(1, end, newEnd2, Direction.left);
		scenario.addTrain(train);
		scenario.addTrain(train2);
		Signal sig1 = new Signal(0, middle, middle2, Direction.left);
		Signal sig2 = new Signal(1, end, s1, Direction.right);

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

	public static Scenario createScenario() {
		scenario = new Scenario(layoutHorizontalSize, layoutVerticalSize);
		trackID = new ArrayList<Integer>();
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
		return scenario;
	}

	private static void relocateTracks(TrackSection ts, int horizontalOffset, int verticalOffset, Scenario scen) {
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
				if (s.getSwitchDirection() == Direction.right) {
					if (s.getTurnDirection() == Direction.right) {
						// RIGHT = RIGHT direction and RIGHT TURN = DOWN direction
						relocateTracks(s.getExtraTrack(), horizontalOffset, verticalOffset, scen);
					} else {
						// NOT RIGHT TURN = UP direction
						relocateTracks(s.getExtraTrack(), horizontalOffset, verticalOffset, scen);
					}
				} else {
					if (s.getTurnDirection() == Direction.right) {
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

	private static void updateScenarioSizes(TrackSection ts, int x, int y) {
		if (x > layoutHorizontalMax) layoutHorizontalMax = x;

		if (x < layoutHorizontalMin) layoutHorizontalMin = x;

		if (y > layoutVerticalMax) layoutVerticalMax = y;

		if (y < layoutVerticalMin) layoutVerticalMin = y;

		// System.out.println("next" + ts.getLocation());
		// System.out.println("current" + topLeft.getLocation());
		// if( y <= topLeft.getLocation().y) {
		// topLeft = ts;
		// if(x < topLeft.getLocation().x)
		// topLeft = ts;
		// }

	}

	private static void placeTracks(TrackSection ts, int x, int y) {
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
				if (s.getSwitchDirection() == Direction.right) {
					if (s.getTurnDirection() == Direction.right) {
						// RIGHT = RIGHT direction and RIGHT TURN = DOWN direction
						placeTracks(s.getExtraTrack(), x + 1, y + 1);
					} else {
						// NOT RIGHT TURN = UP direction
						placeTracks(s.getExtraTrack(), x + 1, y - 1);
					}
				} else {
					if (s.getTurnDirection() == Direction.right) {
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

}
