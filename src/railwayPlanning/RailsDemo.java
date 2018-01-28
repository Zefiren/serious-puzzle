package railwayPlanning;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Surface extends JPanel implements MouseListener{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int trainX = 50, trainY = 300;
	public TrackSection start;
	public Train train;
	Polygon p ;
    public ArrayList<Integer> trackID;
    List<TrackSection> tracks;
    
    private final int trackLengthStraight = 200;

	private Color pcolour = Color.BLUE;
//    private final int track[] = { 1, 2, 1 };

    Surface(
    		ArrayList<Integer> IDs, 
    		TrackSection startSection, 
    		Train train, 
    		List<TrackSection> tracks
    		)
    {
        trackID = IDs;
        start = startSection;
        this.train = train;
        this.tracks = tracks;
//        drawTracks(start,,0,0);
        p = new Polygon(new int[] {50,100,80}, new int[] {50,50,100}, 3);
        addMouseListener(this);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int trackX = 50, trackY = 300;
        g2d.setPaint(Color.blue);

        BasicStroke bs1 = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        g2d.setStroke(bs1);

        AffineTransform oldTransform = g2d.getTransform();
        
        trackID.clear();
        g2d.setTransform(oldTransform);
        g2d.translate(0,0);
        drawTracks(start, g2d, trackX, trackY);
//        for (int piece : track) {
//            switch (piece) {
//            case 1: {
//                drawStraight(g2d, trackX, trackY);
//                trackY = 0;
//                break;
//            }
//            case 2: {
//                drawLeft(g2d, trackX, trackY);
//                trackY = -50;
//                break;
//            }
//            case 3: {
//                drawRight(g2d, trackX, trackY);
//                trackY = 50;
//                break;
//            }
//            }
//            trackX = 100;
//        } 
        g2d.setTransform(oldTransform);
        
        drawTrain(g2d);
        System.out.println(g2d.getTransform().getTranslateX());
        g2d.dispose();
    }
    
    private void drawTracks(TrackSection ts, Graphics2D g2d, int x, int y ){
    	drawTrack(ts, g2d, x, y);
    	if(ts.isEndTrack()){
    		if(ts.isRightEnding()){
    	    	if(!trackID.contains(ts.getLeftTrack().getTsID())){
    	    		x-= trackLengthStraight;
    	    		drawTracks(ts.getLeftTrack(), g2d, x, y);
    	    	}
    		}else{
    			if(!trackID.contains(ts.getRightTrack().getTsID())){
    	    		x+= trackLengthStraight;
    	    		drawTracks(ts.getRightTrack(), g2d, x, y);
    			}
    		}
    	}else{
	    	if(!trackID.contains(ts.getLeftTrack().getTsID())){
	    		x-= trackLengthStraight;
	    		drawTracks(ts.getLeftTrack(), g2d, x, y);
	    	}
	    	
	    	if(!trackID.contains(ts.getRightTrack().getTsID())){
	    		x+= trackLengthStraight;
	    		drawTracks(ts.getRightTrack(), g2d, x, y);
	    	}
    	}
    }    

    private void drawTrack(TrackSection ts, Graphics2D g2d, int x, int y ){
    	ts.setTrackGraphicPoints(x, y, x + trackLengthStraight, y);
    	if(ts.getTsID()==0)  
        	g2d.setPaint(Color.green);
        else if(ts.getTsID()==1)  
        	g2d.setPaint(Color.red);
//        g2d.drawLine(0, 0, trackLengthStraight, 0);
        
        g2d.draw(ts.getTrackGraphic());
        
       
        System.out.println("drew "+ ts.getTsID());
    	trackID.add(ts.getTsID());
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
		if(p.contains(e.getPoint())) {
			System.out.println("you clicked me"+e.getPoint());
			pcolour = new Color(255-pcolour.getRed(), 255-pcolour.getGreen(), 255-pcolour .getBlue());
		}
		System.out.println("clicked "+e.getPoint());
		for (TrackSection ts : tracks) {
			System.out.println("track bounds" + ts.getTrackGraphic().getBounds());
			if(ts.getTrackGraphic().contains(e.getPoint())) {
				System.out.println("YOU CLICKED " + ts.getTsID());
				Color tmp = ts.getTrackColour();
				ts.setTrackColour(new Color(
						255-tmp.getRed(), 
						255-tmp.getGreen(), 
						255-tmp .getBlue()
						));
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}

public class RailsDemo extends JFrame implements KeyListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Surface s;

    public RailsDemo() {

        initUI();
    }

    private Surface createScene(){
    	
    	ArrayList<Integer> trackID = new ArrayList<Integer>();
    	TrackSection start = new TrackSection(0);
    	TrackSection middle = new TrackSection(1);
    	TrackSection end = new TrackSection(2,"end",true,middle);
    	start.setRightTrack(middle);
    	start.setRightEnding(false);
    	start.setEndTrack(true);
    	start.setLabel("start");
    	
    	middle.setLeftTrack(start);
    	middle.setRightTrack(end);
    	
    	Train train = new Train(0,start,end, start);
    	
    	List<TrackSection> tracks = new ArrayList<TrackSection>();
    	tracks.add(start);
    	tracks.add(end);
    	tracks.add(middle);
    	
        Surface surf = new Surface(trackID, start, train,tracks);
        return surf;
    }
    private void initUI() {
        s = createScene();
        add(s);
        addKeyListener(this);
        setTitle("Translation");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        createScene();
        
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                RailsDemo ex = new RailsDemo();
                ex.setVisible(true);
            }
        });
    }

    /** Handle the key-pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            s.trainX -= 5;
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            s.trainY += 5;
        if (e.getKeyCode() == KeyEvent.VK_UP)
            s.trainY -= 5;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            s.trainX += 5;
        repaint();
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