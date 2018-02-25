package railwayPlanning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

class Surface extends JPanel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int trainX = 50, trainY = 300;
	int trackX = 50, trackY = 300;

	public TrackSection start;
	public Train train;
	Polygon p;
	public ArrayList<Integer> trackID;
	List<TrackSection> tracks;
	List<Signal> signals;
	SolutionManager solMgr;
	
	private final int trackLengthStraight = 200;

	private Color pcolour = Color.BLUE;
	private BasicStroke trackBrush = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);;
	private BasicStroke labelBrush = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);;
	// private final int track[] = { 1, 2, 1 };

	Surface(ArrayList<Integer> IDs, TrackSection startSection, Train train, List<TrackSection> tracks,List<Signal> signals) {
        setPreferredSize(new Dimension(1280, 600));

		addMouseListener(this);

		trackID = IDs;
		start = startSection;
		this.train = train;
		this.tracks = tracks;
		this.signals = signals;

		solMgr = new SolutionManager();
		placeTracks(start, trackX, trackY);
		placeSignals();
		trackID.clear();
		// drawTracks(start,,0,0);
		p = new Polygon(new int[] { 50, 100, 80 }, new int[] { 50, 50, 100 }, 3);
	}

	private void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setPaint(Color.blue);

		g2d.setStroke(trackBrush);
		g2d.setFont(new Font("Serif", Font.PLAIN, 18));
		AffineTransform oldTransform = g2d.getTransform();

		trackID.clear();
		g2d.setTransform(oldTransform);
		g2d.translate(0, 0);
		drawTracks(start, g2d);
		drawSignals(g2d);
		
		g2d.setTransform(oldTransform);
		g2d.translate(trainX, trainY);
		g2d.rotate(Math.toRadians(1));

		drawTrain(g2d);
		System.out.println(g2d.getTransform().getTranslateX());
		g2d.dispose();
	}

	 public Dimension getPreferredSize() {
	        return new Dimension(1280, 800);
    }
	
	private void drawTracks(TrackSection ts, Graphics2D g2d) {
		drawTrack(ts, g2d);
		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			if (!trackID.contains(s.getLeftTrack().getTsID())) {
				drawTracks(s.getLeftTrack(), g2d);
			}
			if (!trackID.contains(s.getRightTrack().getTsID())) {
				drawTracks(s.getRightTrack(), g2d);
			}
			if (!trackID.contains(s.getExtraTrack().getTsID()))
				drawTracks(s.getExtraTrack(), g2d);

		} else {
			if (ts.isEndTrack()) {
				if (ts.isRightEnding()) {
					if (!trackID.contains(ts.getLeftTrack().getTsID()))
						drawTracks(ts.getLeftTrack(), g2d);
				} else {
					if (!trackID.contains(ts.getRightTrack().getTsID()))
						drawTracks(ts.getRightTrack(), g2d);
				}
			} else {
				if (!trackID.contains(ts.getLeftTrack().getTsID()))
					drawTracks(ts.getLeftTrack(), g2d);
				if (!trackID.contains(ts.getRightTrack().getTsID()))
					drawTracks(ts.getRightTrack(), g2d);
			}
		}

	}

	private void drawTrack(TrackSection ts, Graphics2D g2d) {
		Boolean isThrown = false;
		Boolean isRightDirection = false;
		g2d.setPaint(ts.getTrackColour());
		g2d.setStroke(trackBrush);
		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			isThrown = s.isDiverging();
			isRightDirection =s.isRightDirection();

			AffineTransform oldTransform = g2d.getTransform();
			Polygon p = new Polygon(s.getExtraTrackGraphic().xpoints, s.getExtraTrackGraphic().ypoints, s.getExtraTrackGraphic().npoints);
			
			
			System.out.println(p.getBounds().getCenterX());
			System.out.println((p.xpoints[0]-p.getBounds().getCenterX())+" switch\n");
			g2d.translate(p.getBounds().getCenterX(), p.getBounds().getCenterY());
			for(int i = 0; i < p.npoints; i++) {
				System.out.println(p.xpoints[i] + ", " + p.ypoints[i] + " BEFORE switch \n");
				p.xpoints[i] -= p.getBounds().getCenterX();
				p.ypoints[i] -= p.getBounds().getCenterY();
				System.out.println(p.xpoints[i] + ", " + p.ypoints[i] + " switch \n");
			}
			
			if(s.isDiverging() && s.isTurnRight()) {
				p.ypoints[0] -= 10;
				p.ypoints[1] -= 10;
			}else{
				if(s.isDiverging()){
					p.ypoints[0] += 10;
					p.ypoints[1] += 10;
				}
			}
//			int xDir = 1;
//			int yDir = 1;
//			if(!isRightDirection) {
//				xDir = -1;
//				System.out.println("yo" + s.getTsID());
//			}
//			if(!s.isTurnRight()) {
//				if(!isRightDirection){
//					xDir = -1;
//					yDir = 1;
//				}
//				else{
//					yDir = -1;
//				}
//			}
//			g2d.scale(xDir, yDir);
			g2d.drawPolyline(p.xpoints,p.ypoints,p.npoints);
			g2d.setTransform(oldTransform);
		}
		if(isThrown) {
			Line2D.Double l = new Double(ts.getTrackGraphic().getP1(), ts.getTrackGraphic().getP2());
			if(isRightDirection)
				l.x1 += 40;
			else
				l.x2 -= 40;
			g2d.draw(l);
		}
		else
			g2d.draw(ts.getTrackGraphic());

		g2d.setPaint(Color.black);
		g2d.setStroke(labelBrush);

		g2d.draw(ts.getLabelBox());
		g2d.drawString("tc" + ts.getTsID(), ts.getTextPlace().x, ts.getTextPlace().y);

		System.out.println("drew " + ts.getTsID());
		trackID.add(ts.getTsID());
	}

	private void placeTracks(TrackSection ts, int x, int y) {
		placeTrack(ts, x, y);
		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			int orig_x = x;
			if (!trackID.contains(s.getLeftTrack().getTsID())) {
				x -= trackLengthStraight;
				placeTracks(s.getLeftTrack(), x, y);
			}
			if (!trackID.contains(s.getRightTrack().getTsID())) {
				x += trackLengthStraight;
				placeTracks(s.getRightTrack(), x, y);
			}
			x = orig_x;
			if (!trackID.contains(s.getExtraTrack().getTsID())) {
				if (s.isRightDirection())
					x += trackLengthStraight;
				else
					x -= trackLengthStraight;

				if (s.isTurnRight())
					y += 100;
				else
					y -= 100;

				placeTracks(s.getExtraTrack(), x, y);
			}
		} else {
			if (ts.isEndTrack()) {
				if (ts.isRightEnding()) {
					if (!trackID.contains(ts.getLeftTrack().getTsID())) {
						x -= trackLengthStraight;
						placeTracks(ts.getLeftTrack(), x, y);
					}
				} else {
					if (!trackID.contains(ts.getRightTrack().getTsID())) {
						x += trackLengthStraight;
						placeTracks(ts.getRightTrack(), x, y);
					}
				}
			} else {
				if (!trackID.contains(ts.getLeftTrack().getTsID())) {
					x -= trackLengthStraight;
					placeTracks(ts.getLeftTrack(), x, y);
				}

				if (!trackID.contains(ts.getRightTrack().getTsID())) {
					x += trackLengthStraight;
					placeTracks(ts.getRightTrack(), x, y);
				}
			}
		}

	}

	private void placeTrack(TrackSection ts, int x, int y) {
		ts.setTrackGraphicPoints(x, y, (int) (x + trackLengthStraight * 0.95), y);
		ts.setLabelBoxPoint(x + 85, y + 10, 40, 20);
		ts.setTextPlace(new Point(x + 90, y + 25));
		// g2d.drawLine(0, 0, trackLengthStraight, 0);

		if (ts.getClass() == Switch.class) {
			Switch s = (Switch) ts;
			s.setLabelBoxPoint(x + 85, y + 10, 40, 20);
			s.setTextPlace(new Point(x + 90, y + 25));
			Polygon eLine = s.getExtraTrackGraphic();
			int xs, ys, xe, ye, xm;
			if(!s.isRightDirection())
				xs = x + (int) (eLine.xpoints[0]*0.95);
			else
				xs = x + (int) eLine.xpoints[0];
			ys = y + (int) eLine.ypoints[0];
			xm = (int) (x + (int)eLine.xpoints[1]);//(int) (eLine.xpoints[2] - eLine.xpoints[0]) * 0.15);
			if(s.isRightDirection())
				xe = (int) (x + (int)(eLine.xpoints[2]*0.95)); //(int) (eLine.xpoints[2] - eLine.xpoints[0]) * 0.95);
			else
				xe = (int) (x + (int)eLine.xpoints[2]); //(int) (eLine.xpoints[2] - eLine.xpoints[0]) * 0.95);
//			ys = y
			ye = y + (int) (eLine.ypoints[2] - eLine.ypoints[0]);
			s.setExtraTrackGraphic(new Polygon(new int[] {xs,xm,xe},new int[] {ys,ys,ye},3));
			//new Point(xs, ys),new Point(xm, ye), new Point(xe, ye))

			// eLine.setLine(xs,ys,xe,ye);

		}
		// g2d.draw(ts.getTrackGraphic());
		//
		// g2d.setPaint(Color.black);
		// g2d.setStroke(labelBrush);
		//
		// g2d.draw(ts.getLabelBox());
		// g2d.drawString("tc" + ts.getTsID(), x + 90, y + 25);

		System.out.println("drew " + ts.getTsID());
		trackID.add(ts.getTsID());
	}
	private void drawSignals(Graphics2D g2d) {
		for (Signal signal : signals) {
			SignalGraphic sg = signal.getSignalGraphic();
			if(signal.isClear()) {
				g2d.setPaint(signal.getSignalGraphic().getColourClear());
				g2d.fill(sg.getCircleProceed());
				g2d.setPaint(Color.BLACK);
				g2d.draw(sg.getCircleDanger());
			}else {
				g2d.setPaint(Color.BLACK);
				g2d.draw(sg.getCircleProceed());
				g2d.setPaint(signal.getSignalGraphic().getColourDanger());
				g2d.fill(sg.getCircleDanger());
				g2d.setPaint(Color.BLACK);
			}
			g2d.draw(sg.getLabelBox());
			g2d.drawString((signal.isFacingLeft() ? "<sig":"sig>")  + signal.getId(), sg.getTextPlace().x, sg.getTextPlace().y);
			
		}
	}
	
	private void placeSignals() {
		for (Signal signal : signals) {
			TrackSection tc = signal.getSignalTC();
			SignalGraphic sg = signal.getSignalGraphic();
			if(signal.isFacingLeft())
				sg.setSignalPosition(new Point((int)tc.getTrackGraphic().getP2().getX()- 50,(int)tc.getTrackGraphic().getP2().getY() - 30));
			else
				sg.setSignalPosition(new Point((int)tc.getTrackGraphic().getP1().getX(),(int)tc.getTrackGraphic().getP1().getY() - 30));
			Point pos = sg.getSignalPosition();
			pos.y = pos.y + 40;
//					ts.setLabelBoxPoint(x + 85, y + 10, 30, 20);
//			ts.setTextPlace(new Point(x + 90, y + 25));
			sg.setLabelBoxPoint(pos);
			sg.setTextPlace(new Point(pos.x + 7,pos.y+15));
		}
	}
	
	private void drawTrain(Graphics2D g2d) {
		g2d.translate(trainX, trainY - 20);
		g2d.setPaint(new Color(150, 150, 150));
		g2d.fillRect(0, 0, 70, 40);
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		doDrawing(g);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (p.contains(e.getPoint())) {
			System.out.println("you clicked me" + e.getPoint());
			pcolour = new Color(255 - pcolour.getRed(), 255 - pcolour.getGreen(), 255 - pcolour.getBlue());
		}
		System.out.println("clicked " + e.getPoint());
		for(Signal sig : signals) {
			if(sig.getSignalGraphic().getLabelBox().contains(e.getPoint())) {
				SolutionCmd newCmd = new SolutionCmd( sig, !sig.isClear());
				solMgr.addStep(newCmd);
				sig.setClear(!sig.isClear());
				repaint();
				return;
			}
		}
		for (TrackSection ts : tracks) {
			if (ts.getLabelBox().contains(e.getPoint())) {
				if (e.isMetaDown()) {
					if (ts.getClass() == Switch.class) {
						Switch s = (Switch) ts;
						SolutionCmd newCmd = new SolutionCmd( s, !s.isDiverging());
						solMgr.addStep(newCmd);
						s.setDiverging(!s.isDiverging());
						System.out.println("changeed " + s.isDiverging());
						repaint();
						return;
					}
				} else {
					System.out.println("YOU CLICKED " + ts.getTsID());
					Color tmp = ts.getTrackColour();
					ts.setTrackColour(new Color(255 - tmp.getRed(), 255 - tmp.getGreen(), 255 - tmp.getBlue()));
					System.out.println(ts.getTrackColour());
					repaint();
					return;
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}

public class RailsDemo extends JFrame implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Surface s;
	JTable list;
	JButton delBtn;
	JScrollPane listScroller;
	DefaultTableModel vegName;
	JPanel panel,solPanel;
	
	public RailsDemo() {

//		createGUI	();
	}

	private Surface createScene() {

		ArrayList<Integer> trackID = new ArrayList<Integer>();
		TrackSection start, middle, upRightEnd, middle2, end,newEnd,newEnd2;
		Switch s1,s2;
		middle = new TrackSection(1);
		middle2 = new TrackSection(2);
		start = new TrackSection(0, "start", false, middle);
		end = new TrackSection(4, "end", true, middle2);
		upRightEnd = new TrackSection(5, "upEnd", false, middle2);
		newEnd = new TrackSection(7);//, "endLeft2")//, true, s2);
		newEnd2 = new TrackSection(8);//, "endLeft3")//, true, s2);
		//id, left, right, isRightDir, isRightTurn, extraTrack
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
		
		Surface surf = new Surface(trackID, start, train, tracks,signals);
		return surf;
	}

	private void initUI() {
		setLayout(new FlowLayout());
	  	s = createScene();
        panel = new JPanel();
        solPanel = new JPanel();
        panel.setLayout(new FlowLayout());
		this.addKeyListener(this);
		panel.addKeyListener(this);
		s.addKeyListener(this);
		setTitle("Translation");
		s.setSize(getPreferredSize());
		panel.setSize(1280, 600);
		setSize(1600, 800);
		panel.add(s);
		solPanel.setLayout(new GridLayout(2,1));
		String[] columns = {"Step","Operation"};
		vegName = new DefaultTableModel(null,columns);

		s.solMgr.listMod = vegName;
//		vegName.addElement("Lady Finger");
//		vegName.addElement("Onion");
//		vegName.addElement("Potato");
//		vegName.addElement("Tomato");
		list = new JTable(vegName); //data has type Object[] 
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		list.setDefaultRenderer(String.class, new SolutionCellRenderer());
		list.setBackground(null);
		list.setShowGrid(false);
		list.setEnabled(false);
		list.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		list.getColumnModel().getColumn(0).setMaxWidth(30);
		list.setBorder(BorderFactory.createEmptyBorder());

		listScroller = new JScrollPane(list);
		listScroller.setBackground(null);
		listScroller.setBorder(BorderFactory.createEmptyBorder());
		listScroller.setPreferredSize(new Dimension(200, 300));
		
		solPanel.add(listScroller);	
		delBtn = new JButton("Delete");
		solPanel.add(delBtn);
		panel.add(solPanel);
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("G"), 0);
		panel.getActionMap().put(0, new MoveAction() );
        add(panel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				RailsDemo ex = new RailsDemo();
				ex.setVisible(true);
				ex.initUI();
			}
		});
	}

	/** Handle the key-pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		System.out.println("key press");
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			s.trainX -= 5;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			s.trainY += 5;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			s.trainY -= 5;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			s.trainX += 5;
		if (e.getKeyCode() == KeyEvent.VK_G)
			System.out.println("gheter");
		repaint();
	}

	
	private class MoveAction extends AbstractAction {

        MoveAction() {
        	System.out.println("HELLO VICTOR");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//			System.out.println(s.solCmds);
        }
    }
	/** Handle the key-released event from the text field. */
	public void keyReleased(KeyEvent e) {
	}

	/** Handle the key typed event from the text field. */
	public void keyTyped(KeyEvent e) {

	}

	/** Handle the button click. */
	public void actionPerformed(ActionEvent e) {

	}

}