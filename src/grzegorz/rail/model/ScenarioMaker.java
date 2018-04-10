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

	private static TrackSection createScene(int scenarioChosen) {
		switch (scenarioChosen) {
		case 0: {
			TrackSection A, tc1, tc2, B;
			tc1 = new TrackSection(1);
			tc2 = new TrackSection(2);
			A = new TrackSection(0, "A", false, tc1);
			B = new TrackSection(3, "B", true, tc2);

			tc1.setLeftTrack(A);
			tc1.setRightTrack(tc2);

			tc2.setLeftTrack(tc1);
			tc2.setRightTrack(B);

			Train train1 = new Train(1, A, B, Direction.right);

			scenario.addTrain(train1);

			Signal sig1 = new Signal(0, A, tc1, Direction.left);

			List<TrackSection> tracks = new ArrayList<TrackSection>();
			List<Signal> signals = new ArrayList<Signal>();
			tracks.add(A);
			tracks.add(tc1);
			tracks.add(tc2);
			tracks.add(B);

			signals.add(sig1);
			scenario.addSignal(sig1);

			return A;


		}
		case 1: {
			TrackSection tc1, tc2, tc6, tc3, tc5, tc8, tc9,tc11,tc12;
			Switch tc4s1, tc7s2,tc10s3;
			tc2 = new TrackSection(2);
			tc3 = new TrackSection(3);
			tc1 = new TrackSection(1, "A", false, tc2);
			tc5 = new TrackSection(5, "B", true, tc3);

			tc6 = new TrackSection(6, null, null);
			tc10s3 = new Switch(10,2, tc5, null, Direction.right, Direction.left, null);
			tc5.setRightTrack(tc10s3);
			tc5.setEndTrack(false);
			tc5.setLabel(null);

			tc11 = new TrackSection(11, "C", true, tc10s3);
			tc10s3.setRightTrack(tc11);

			tc12 = new TrackSection(12, "D", true, tc10s3);
			tc10s3.setExtraTrack(tc12);

			tc8 = new TrackSection(8);
			tc9 = new TrackSection(9);

			// id, left, right, isRightDir, isRightTurn, extraTrack
			tc4s1 = new Switch(4, 0, tc3, tc5, Direction.left, Direction.right, tc6);
			tc7s2 = new Switch(7, 1, tc8, tc6, Direction.left, Direction.right, tc9);

			tc6.setRightTrack(tc4s1);
			tc6.setLeftTrack(tc7s2);

			tc5.setLeftTrack(tc4s1);

			tc2.setLeftTrack(tc1);
			tc2.setRightTrack(tc3);

			tc3.setLeftTrack(tc2);
			tc3.setRightTrack(tc4s1);

			tc8.setEndTrack(true);
			tc8.setRightEnding(false);
			tc8.setRightTrack(tc7s2);
			tc8.setLabel("E");

			tc9.setEndTrack(true);
			tc9.setRightEnding(false);
			tc9.setRightTrack(tc7s2);
			tc9.setLabel("F");

			Train train = new Train(0, tc1, tc12, Direction.right);
			Train train2 = new Train(1, tc11, tc9, Direction.left);
			scenario.addTrain(train);
			scenario.addTrain(train2);

			Signal sig1 = new Signal(0, tc3, tc4s1, Direction.left);
//			Signal sig2 = new Signal(1, tc5, tc4s1, Direction.right);
			Signal sig3 = new Signal(2, tc11, tc10s3, Direction.right);
//			Signal sig4 = new Signal(3, tc12, tc10s3, Direction.right);

			List<TrackSection> tracks = new ArrayList<TrackSection>();
			List<Signal> signals = new ArrayList<Signal>();
			tracks.add(tc1);
			tracks.add(tc5);
			tracks.add(tc4s1);
			tracks.add(tc7s2);
			tracks.add(tc6);
			tracks.add(tc2);
			tracks.add(tc3);
			tracks.add(tc8);
			tracks.add(tc9);
			tracks.add(tc10s3);
			tracks.add(tc11);
			tracks.add(tc12);

			signals.add(sig1);
//			signals.add(sig2);
			signals.add(sig3);
			scenario.addSignal(sig1);
//			scenario.addSignal(sig2);
			scenario.addSignal(sig3);

			return tc1;
		}
		case 2: {
			TrackSection tc1, tc2, tc5, tc6, tc7, tc10,tc11, tc14, tc15, tc16;
			Switch tc3s1, tc4s2, tc8s3, tc9s4, tc12s5, tc13s6;

			tc2 = new TrackSection(2);
			tc1 = new TrackSection(1, "A", false, tc2);
			tc2.setLeftTrack(tc1);

			tc3s1 = new Switch(3, 1, tc2, null, Direction.left, Direction.right, null);
			tc2.setRightTrack(tc3s1);
			tc4s2 = new Switch(4, 2, tc3s1, null, Direction.right, Direction.left, null);
			tc4s2.setLeftTrack(tc3s1);
			tc3s1.setRightTrack(tc4s2);

			tc5 = new TrackSection(5);
			tc4s2.setRightTrack(tc5);
			tc6 = new TrackSection(6, "D", true, tc5);
			tc5.setRightTrack(tc6);
			tc5.setLeftTrack(tc4s2);

			tc7 = new TrackSection(7, "B", false, null);
			tc16 = new TrackSection(16, "C", false, null);
			tc8s3 = new Switch(8, 3, tc7, null, Direction.left, Direction.right, tc16);
			tc7.setRightTrack(tc8s3);
			tc16.setRightTrack(tc8s3);
			tc9s4 = new Switch(9, 4, tc8s3, null, Direction.right, Direction.right, tc3s1);
			tc3s1.setExtraTrack(tc9s4);
			tc8s3.setRightTrack(tc9s4);
			tc10 = new TrackSection(10, tc9s4, null);
			tc9s4.setRightTrack(tc10);

			tc12s5 = new Switch(12, 5, null, null, Direction.left, Direction.left, tc4s2);
			tc4s2.setExtraTrack(tc12s5);
			tc11 = new TrackSection(11, tc10, tc12s5);
			tc10.setRightTrack(tc11);
			tc12s5.setLeftTrack(tc11);


			tc15 = new TrackSection(15, "F", true, null);
			tc14 = new TrackSection(14, "E", true, null);
			tc13s6 = new Switch(13, 6, tc12s5, tc14, Direction.right, Direction.left, tc15);
			tc14.setLeftTrack(tc13s6);
			tc15.setLeftTrack(tc13s6);
			tc12s5.setRightTrack(tc13s6);


			Train train = new Train(1, tc1, tc6, Direction.right);
			scenario.addTrain(train);

			Signal sig1 = new Signal(0, tc2, tc3s1, Direction.left);
			Signal sig2 = new Signal(1, tc5, tc4s2, Direction.right);
			Signal sig3 = new Signal(2, tc7, tc8s3, Direction.left);
			Signal sig4 = new Signal(3, tc14, tc13s6, Direction.right);
			Signal sig5 = new Signal(4, tc10, tc8s3, Direction.right);
			Signal sig6 = new Signal(5, tc11, tc13s6, Direction.left);
			Signal sig7 = new Signal(6, tc16, tc8s3, Direction.left);
			Signal sig8 = new Signal(6, tc15, tc13s6, Direction.right);

			List<TrackSection> tracks = new ArrayList<TrackSection>();
			List<Signal> signals = new ArrayList<Signal>();
			tracks.add(tc1);
			tracks.add(tc2);
			tracks.add(tc3s1);
			tracks.add(tc4s2);
			tracks.add(tc5);
			tracks.add(tc6);
			tracks.add(tc7);
			tracks.add(tc8s3);
			tracks.add(tc9s4);
			tracks.add(tc10);
			tracks.add(tc11);
			tracks.add(tc12s5);
			tracks.add(tc13s6);
			tracks.add(tc14);
			tracks.add(tc15);
			tracks.add(tc16);

			signals.add(sig1);
			signals.add(sig2);
			signals.add(sig3);
			signals.add(sig4);
			signals.add(sig5);
			signals.add(sig6);
			signals.add(sig7);
			signals.add(sig8);
			scenario.addSignal(sig1);
			scenario.addSignal(sig2);
			scenario.addSignal(sig3);
			scenario.addSignal(sig4);
			scenario.addSignal(sig5);
			scenario.addSignal(sig6);
			scenario.addSignal(sig7);
			scenario.addSignal(sig8);

			return tc1;
		}
		default: {
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
		}
	}

	public static Scenario createScenario(int scenarioChosen) {
		scenario = new Scenario(layoutHorizontalSize, layoutVerticalSize);
		trackID = new ArrayList<Integer>();

		layoutHorizontalSize = 0;
		layoutVerticalSize = 0;

		layoutHorizontalMin = 0;
		layoutHorizontalMax = 0;

		layoutVerticalMin = 0;
		layoutVerticalMax = 0;

		TrackSection startTs = createScene(scenarioChosen);
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
						if (s.getExtraTrack().getClass() == Switch.class) placeTracks(s.getExtraTrack(), x + 1, y + 2);
						else placeTracks(s.getExtraTrack(), x + 1, y + 1);

					} else {
						// NOT RIGHT TURN = UP direction
						if (s.getExtraTrack().getClass() == Switch.class) placeTracks(s.getExtraTrack(), x + 1, y - 2);
						else placeTracks(s.getExtraTrack(), x + 1, y - 1);

					}
				} else {
					if (s.getTurnDirection() == Direction.right) {
						// LEFT = LEFT direction and RIGHT TURN = UP direction
						if (s.getExtraTrack().getClass() == Switch.class) placeTracks(s.getExtraTrack(), x - 1, y - 2);
						else placeTracks(s.getExtraTrack(), x - 1, y - 1);

					} else {
						// NOT RIGHT TURN = DOWN direction
						if (s.getExtraTrack().getClass() == Switch.class) placeTracks(s.getExtraTrack(), x - 1, y + 2);
						else placeTracks(s.getExtraTrack(), x - 1, y + 1);

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
