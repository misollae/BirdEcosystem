package ecosystem;

import processing.core.PApplet;
import setup.IProcessingApp;
import tools.SubPlot;
import tools.TimeGraph;

public class TestEcosystemApp implements IProcessingApp {

	private float timeDuration = 60;
	private float refPopulation = 720;
	private float refMeanMaxSpeed = 1f;
	private float refStdMaxSpeed = 0.2f;
	
	private float[] viewport = {0f, 0f, .7f, 1f};
	
	private double[] winGraph1 = {0, timeDuration, 0, 2*refPopulation};
	private double[] winGraph2 = {0, timeDuration, 0, 2*refMeanMaxSpeed};
	private double[] winGraph3 = {0, timeDuration, 0, 2*refStdMaxSpeed};

	private float[] viewGraph1 = {.71f, .04f, .28f, .28f};
	private float[] viewGraph2 = {.71f, .37f, .28f, .28f};
	private float[] viewGraph3 = {.71f, .70f, .28f, .28f};
	
	private SubPlot plt, pltGraph1, pltGraph2, pltGraph3;
	private TimeGraph tg1, tg2, tg3;
	
	private Terrain terrain;
	private Population population;
	private float timer, updateGraphTime;
	private float intervalUpdate = 1;
	
	@Override
	public void setup(PApplet p) {
		plt = new SubPlot(WorldConstants.WINDOW, viewport, p.width, p.height);
		pltGraph1 = new SubPlot(winGraph1, viewGraph1, p.width, p.height);
		pltGraph2 = new SubPlot(winGraph2, viewGraph2, p.width, p.height);
		pltGraph3 = new SubPlot(winGraph3, viewGraph3, p.width, p.height);
		
		tg1 = new TimeGraph(p, pltGraph1, p.color(255, 0, 0), refPopulation);
		tg2 = new TimeGraph(p, pltGraph2, p.color(255, 0, 0), refMeanMaxSpeed);
		tg3 = new TimeGraph(p, pltGraph3, p.color(255, 0, 0), refStdMaxSpeed);
	
		//terrain = new Terrain(p, plt, this);
		terrain.setStateColors(getColors(p));
		terrain.initRandomCustom(WorldConstants.PATCH_TYPE_PROB);;
		for(int i=0; i<2 ; i++) terrain.majorityRule();
		population = new Population(p, plt, terrain);
		timer = 0;
		updateGraphTime = timer + intervalUpdate;
	}
	
	private int[] getColors(PApplet p) {
		int[] colors = new int[WorldConstants.NSTATES];
		for (int i = 0; i < WorldConstants.NSTATES ; i++) {
			colors[i] = p.color(WorldConstants.TERRAIN_COLORS[i][0], WorldConstants.TERRAIN_COLORS[i][1], WorldConstants.TERRAIN_COLORS[i][2]);
			
		}
		return colors;
	}

	@Override
	public void draw(PApplet p, float dt) {
		timer += dt;
		
		terrain.regenerate();
		population.update(dt, terrain);
		
		terrain.display(p);
		population.display(p, plt);
		
		if (timer > updateGraphTime) {
			System.out.println(String.format("Time = %ds", (int)timer));
			System.out.println("numANimals = " + population.getNumAnimals());
			System.out.println("MeanMaxSpeed = " + population.getMeanMaxSpeed());
			System.out.println("StdMaxSpeed = " + population.getStdMaxSpeed());
			tg1.plot(timer, population.getNumAnimals());
			tg2.plot(timer, population.getMeanMaxSpeed());
			//tg3.plot(timer, population.getStdMaxSpeed());
			updateGraphTime = timer + intervalUpdate;
		}
		
	}

	@Override
	public void mousePressed(PApplet p) {
		winGraph1[0] = timer;
		winGraph1[1] = timer + timeDuration;
		winGraph1[3] = 2*population.getNumAnimals();
		pltGraph1 = new SubPlot(winGraph1, viewGraph1, p.width, p.height);
		tg1 = new TimeGraph(p, pltGraph1, p.color(255, 0, 0), population.getNumAnimals());
		
		winGraph2[0] = timer;
		winGraph2[1] = timer + timeDuration;
		tg2 = new TimeGraph(p, pltGraph2, p.color(255, 0, 0), refMeanMaxSpeed);

		winGraph3[0] = timer;
		winGraph3[1] = timer + timeDuration;
		tg3 = new TimeGraph(p, pltGraph3, p.color(255, 0, 0), refStdMaxSpeed);
	}

	@Override
	public void mouseReleased(PApplet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(PApplet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(PApplet p) {
		// TODO Auto-generated method stub
		
	}

}
