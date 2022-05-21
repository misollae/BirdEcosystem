package CellularAutomata;
import processing.core.PApplet;
import setup.IProcessingApp;
import tools.SubPlot;

public class CellularAutomataApp implements IProcessingApp{
	private double[] window  = {0, 10, 0, 10};
	private float[] viewport = {0.3f,0.3f, 0.5f, 0.6f};
	private SubPlot plt;
	private MajorityCellularAutomata ca;

	
	@Override
	public void setup(PApplet p) {
		plt = new SubPlot(window, viewport, p.width, p.height);
		ca = new MajorityCellularAutomata(p, plt, 30, 40, 4, 1);
		ca.initRandom();
	}

	@Override
	public void draw(PApplet p, float dt) {
		ca.display(p);
	}

	public void keyPressed(PApplet p) {
	}
	
	@Override
	public void mousePressed(PApplet p) {
		ca.majorityRule();
	}

	@Override
	public void mouseReleased(PApplet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(PApplet p) {
		// TODO Auto-generated method stub
		
	}
}
