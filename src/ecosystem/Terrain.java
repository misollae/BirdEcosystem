package ecosystem;

import java.util.ArrayList;
import java.util.List;

import CellularAutomata.MajorityCellularAutomata;
import ecosystem.WorldConstants.Season;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;

public class Terrain extends MajorityCellularAutomata {

	private PImage greenTree;
	private PImage redTree;
	private PImage yellowtree;
	private PImage springgrass;
	private PImage autumngrass;
	private PImage wintergrass;
	private PImage seeds;
	private BirdApp eco;
	private PImage greenTree2;
	private PImage summergrass;
	
	public Terrain(PApplet p, SubPlot plt, BirdApp eco) {
		super(p, plt, WorldConstants.NROWS, WorldConstants.NCOLS, WorldConstants.NSTATES, 1);
		this.eco = eco;
		this.greenTree = p.loadImage("../imagens/summertree.png");
		greenTree.resize((int) (greenTree.width/1.7f), 0);
		this.greenTree2 = p.loadImage("../imagens/springtree2.png");
		greenTree2.resize((int) (greenTree2.width/1.7f), 0);
		this.redTree = p.loadImage("../imagens/autumntree.png");
		redTree.resize((int) (redTree.width/1.7f), 0);
		this.yellowtree = p.loadImage("../imagens/wintertree3.png");
		
		this.springgrass = p.loadImage("../imagens/springgrass.png");
		this.summergrass = p.loadImage("../imagens/summergrass.png");
		this.autumngrass = p.loadImage("../imagens/autumngrass.png");
		this.wintergrass = p.loadImage("../imagens/wintergrass.png");
		
		this.seeds = p.loadImage("../imagens/seedstran.png");


	}
	
	public List<Body> getObstacles(){
		List<Body> bodies = new ArrayList<Body>();
		for (int i = 0 ; i < nrows ; i++) {
			for (int j = 0 ; j < ncols ; j++) {
				if (cells[i][j].getState() == WorldConstants.PatchType.OBSTACLE.ordinal()) {
					Body b = new Body(this.getCenterCell(i, j));
					bodies.add(b);
				}
			}
		}
		return bodies;
	}
	
	public Season getSeason() {
		return eco.getSeason();
	}
	
	public List<Body> getTreeLocations(){
		List<Body> bodies = new ArrayList<Body>();
		for (int i = 0 ; i < nrows ; i++) {
			for (int j = 0 ; j < ncols ; j++) {
				if (cells[i][j].getState() == WorldConstants.PatchType.OBSTACLE.ordinal()) {
					Body b = new Body(this.getTree(i, j));
					bodies.add(b);
				}
			}
		}
		return bodies;
	}
	
	private PVector getCenterCell(int row, int col) {
		float x = (col + 0.5f) * cellWidth;
		float y = (row + 0.5f) * cellHeight;
		double[] w = plt.getWorldCoord(x, y);
		return new PVector((float)w[0], (float)w[1]);
	}
	
	private PVector getTree(int row, int col) {
		float x = (col + 0.5f) * cellWidth;
		float y = (row + 0.5f) * cellHeight;
		double[] w = plt.getWorldCoord(x - (greenTree.width/2), y - (48/5 + cellHeight));
		return new PVector((float)w[0], (float)w[1]);
	}

	protected void createCells() {
		int minRT = (int) (WorldConstants.REFENERATION_TIME[0]*1000);
		int maxRT = (int) (WorldConstants.REFENERATION_TIME[1]*1000);
		for (int i = 0 ; i < nrows ; i++) {
			for (int j = 0 ; j < ncols ; j++) {
				int timeToGrow = (int) (minRT+(maxRT-minRT)*Math.random());
				cells[i][j] = new Patch(this, i, j, timeToGrow);
			}
		}
		setMooreNeighbors();
	}
	
	public void regenerate() {
		for (int i = 0 ; i < nrows ; i++) {
			for (int j = 0 ; j < ncols ; j++) {
				((Patch) cells[i][j]).regenerate();
			}
		}
	}
	
	@Override
	public void display(PApplet p) {
		Season currentSeason = eco.getSeason();
		p.pushStyle();
		for (int i=0 ; i < nrows ; i++) {
			for (int j=0 ; j < ncols ; j++) {
				if (cells[i][j].getState() != WorldConstants.PatchType.EMPTY.ordinal()) {
					if (currentSeason == WorldConstants.Season.SPRING) cells[i][j].display(p, springgrass);
					else if (currentSeason == WorldConstants.Season.SUMMER) cells[i][j].display(p, summergrass);
					else if (currentSeason == WorldConstants.Season.AUTUMN) cells[i][j].display(p, autumngrass);
					else if (currentSeason == WorldConstants.Season.WINTER) cells[i][j].display(p, wintergrass);
					else cells[i][j].display(p);
					
					if (cells[i][j].getState() == WorldConstants.PatchType.FOOD.ordinal()) {
						cells[i][j].display(p, seeds);
					}
				} else {
					if (currentSeason == WorldConstants.Season.AUTUMN) cells[i][j].display(p, p.color(251, 167, 11));
					else if  (currentSeason == WorldConstants.Season.WINTER) cells[i][j].display(p, p.color(222, 249, 255));
					else cells[i][j].display(p);
				}
				
			}
		}
		
		for (Body b : getTreeLocations()) {
			float[] pp = plt.getPixelCoord(b.getPos().x, b.getPos().y);
			
			if (currentSeason == WorldConstants.Season.SPRING)
				p.image(greenTree2, pp[0], pp[1]);
			else if (currentSeason == WorldConstants.Season.SUMMER) 
				p.image(greenTree, pp[0], pp[1]);
			else if (currentSeason == WorldConstants.Season.AUTUMN) 
				p.image(redTree, pp[0], pp[1]);
			else if (currentSeason == WorldConstants.Season.WINTER) 
				p.image(yellowtree, pp[0], pp[1]);
		}
		p.popStyle();		
	}

	public List<Body> getFreePos() {
		List<Body> bodies = new ArrayList<Body>();
		for (int i = 0 ; i < nrows ; i++) {
			for (int j = 0 ; j < ncols ; j++) {
				if (cells[i][j].getState() != WorldConstants.PatchType.OBSTACLE.ordinal()) {
					Body b = new Body(this.getCenterCell(i, j));
					bodies.add(b);
				}
			}
		}
		return bodies;
	}
}


