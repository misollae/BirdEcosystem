package ecosystem;

import java.util.ArrayList;
import java.util.List;

import aa.Arrive;
import ecosystem.WorldConstants.BirdColor;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.CustomRandomGenerator;
import tools.SubPlot;

public class Bird extends Prey {

	private BirdColor birdColor;
	private PImage bird;
	private PImage birdI;
	private List<Bird> children = new ArrayList<Bird>();
	private Population population;
	private Bird mate;
	private PImage heart;

	public Bird(PVector pos, float mass, float radius, PApplet parent, SubPlot plt, BirdColor birdColor, Population population) {
		super(pos, mass, radius, parent.color(0), parent, plt);
		this.population = population;
		this.birdColor = birdColor;
		this.lifetime = parent.random(60, 80);
		this.heart = parent.loadImage("../imagens/heart.png");
		this.bird = parent.loadImage("../imagens/" + birdColor.toString().toLowerCase() + "bird.png");
		this.birdI = parent.loadImage("../imagens/" + birdColor.toString().toLowerCase() + "bird2.png");
		heart.resize(10, 0);
	}
	
	public Bird(Prey prey, boolean mutate, PApplet parent, SubPlot plt, BirdColor birdColor, Population population) {
		super(prey, mutate, parent, plt);
		this.population = population;
		this.birdColor = birdColor;
		this.lifetime = parent.random(60, 80);
		this.heart = parent.loadImage("../imagens/heart.png");
		this.bird = parent.loadImage("../imagens/" + birdColor.toString().toLowerCase() + "bird.png");
		this.birdI = parent.loadImage("../imagens/" + birdColor.toString().toLowerCase() + "bird2.png");
		heart.resize(10, 0);
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushMatrix();
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		float[] rr = plt.getVectorCoord(radius, radius);
		
		PImage birdC = bird.copy();
		PImage birdIC = birdI.copy();

		birdC.resize((int)(rr[0]), 0);
		birdIC.resize((int)(rr[0]), 0);

		if (mate != null && PVector.dist(pos, mate.pos) <= 2f) p.image(heart, pp[0] - 47/4, pp[1]  - 42/1.4f);
		
		if (Math.abs(vel.heading()) < 2) p.image(birdC, pp[0] - 47/2, pp[1]  - 42/2);
		else p.image(birdIC, pp[0] - 47/2, pp[1]  - 42/2);
		p.popMatrix();
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setMate(Bird bird) {
		this.mate = bird;
	}
	
	public Bird getMate() {
		return mate;
	}
	
	public void remMate() {
		if (getBehaviors().size() == 3) removeBehavior(getBehaviors().get(getBehaviors().size()-1));
		this.mate = null;
	}
	
	private BirdColor getChildColor() {
		BirdColor childColor = WorldConstants.BirdColor.RED;
		
		Bird bird1 = this;
		Bird bird2 = this.mate;
		
		if ((bird1.birdColor == WorldConstants.BirdColor.RED || bird1.birdColor == WorldConstants.BirdColor.YELLOW) && 
			(bird2.birdColor == WorldConstants.BirdColor.RED || bird2.birdColor == WorldConstants.BirdColor.YELLOW)) {
			CustomRandomGenerator crg = new CustomRandomGenerator(new double[]{.37f, .37f, .19f, .7f});
			childColor = WorldConstants.BirdColor.values()[crg.getRandomClass()];
			if(bird1.birdColor != bird2.birdColor) {
				if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(8);
				if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(9);
				if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(10);
				if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(11);
			} else if (bird1.birdColor == bird2.birdColor && bird1.birdColor.equals(WorldConstants.BirdColor.YELLOW)) {
				if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(0);
				if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(1);
				if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(2);
				if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(3);
			} else if (bird1.birdColor == bird2.birdColor && bird1.birdColor.equals(WorldConstants.BirdColor.RED)) {
				if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(4);
				if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(5);
				if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(6);
				if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(7);
			}
		}
		if (((bird1.birdColor == WorldConstants.BirdColor.RED || bird1.birdColor == WorldConstants.BirdColor.YELLOW) && (bird2.birdColor == WorldConstants.BirdColor.BLUE)) ||
			 (bird2.birdColor == WorldConstants.BirdColor.RED || bird2.birdColor == WorldConstants.BirdColor.YELLOW) && (bird1.birdColor == WorldConstants.BirdColor.BLUE)) {
			CustomRandomGenerator crg = new CustomRandomGenerator(new double[]{.25f, .25f, .0f, .50f});
			childColor = WorldConstants.BirdColor.values()[crg.getRandomClass()];
			if(bird1.birdColor.equals(WorldConstants.BirdColor.YELLOW) || bird2.birdColor.equals(WorldConstants.BirdColor.YELLOW)) {
				if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(16);
				if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(17);
				if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(18);
				if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(19);
			} else if (bird1.birdColor.equals(WorldConstants.BirdColor.RED) || bird2.birdColor.equals(WorldConstants.BirdColor.RED)) {
				if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(12);
				if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(13);
				if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(14);
				if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(15);
			}
		}
		if (((bird1.birdColor == WorldConstants.BirdColor.RED || bird1.birdColor == WorldConstants.BirdColor.YELLOW) && (bird2.birdColor == WorldConstants.BirdColor.GREEN)) ||
			 (bird2.birdColor == WorldConstants.BirdColor.RED || bird2.birdColor == WorldConstants.BirdColor.YELLOW) && (bird1.birdColor == WorldConstants.BirdColor.GREEN))	{
			CustomRandomGenerator crg = new CustomRandomGenerator(new double[]{.25, .25f, .12f, .38f});
			childColor = WorldConstants.BirdColor.values()[crg.getRandomClass()];
			if(bird1.birdColor.equals(WorldConstants.BirdColor.YELLOW) || bird2.birdColor.equals(WorldConstants.BirdColor.YELLOW)) {
				if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(32);
				if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(33);
				if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(34);
				if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(35);
			} else if (bird1.birdColor.equals(WorldConstants.BirdColor.RED) || bird2.birdColor.equals(WorldConstants.BirdColor.RED)) {
				if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(28);
				if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(29);
				if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(30);
				if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(31);
			}
		}
		if ((bird1.birdColor == WorldConstants.BirdColor.BLUE ) && (bird2.birdColor == WorldConstants.BirdColor.BLUE)) {
			CustomRandomGenerator crg = new CustomRandomGenerator(new double[]{.0f, .0f, .1f, .99f});
			childColor = WorldConstants.BirdColor.values()[crg.getRandomClass()];
			if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(24);
			if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(25);
			if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(26);
			if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(27);
		}
		if ((bird1.birdColor == WorldConstants.BirdColor.BLUE  && bird2.birdColor == WorldConstants.BirdColor.GREEN) ||
			(bird1.birdColor == WorldConstants.BirdColor.GREEN && bird2.birdColor == WorldConstants.BirdColor.BLUE)) {
			CustomRandomGenerator crg = new CustomRandomGenerator(new double[]{.0f, .0f, .50f, .50f});
			childColor = WorldConstants.BirdColor.values()[crg.getRandomClass()];
			if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(36);
			if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(37);
			if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(38);
			if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(39);
		}
		if ((bird1.birdColor == WorldConstants.BirdColor.GREEN ) && (bird2.birdColor == WorldConstants.BirdColor.GREEN)) {
			CustomRandomGenerator crg = new CustomRandomGenerator(new double[]{.0f, .0f, .25f, .75f});
			childColor = WorldConstants.BirdColor.values()[crg.getRandomClass()];
			if (childColor.equals(WorldConstants.BirdColor.YELLOW)) population.matchFound(20);
			if (childColor.equals(WorldConstants.BirdColor.RED)) population.matchFound(21);
			if (childColor.equals(WorldConstants.BirdColor.GREEN)) population.matchFound(22);
			if (childColor.equals(WorldConstants.BirdColor.BLUE)) population.matchFound(23);
		}
		
		return childColor;
	}
	
	@Override
	public Animal reproduce(boolean mutate) {
		Bird child = null;
		if (energy > WorldConstants.PREY_ENERGY_TO_REPRODUCE && radius == WorldConstants.PREY_SIZE && 
			this.children.size() <= WorldConstants.MAX_NUM_CHILDREN) {
			
			if (mate == null) {
				if (population.getSeason() != WorldConstants.Season.WINTER) {
					for (Animal b : population.getAnimals()) {
						if (!b.equals(this) && ((Bird) b).getMate() == null && b.getRadius() == WorldConstants.PREY_SIZE && !children.contains(b)) {
							this.mate = (Bird) b;
							((Bird) b).setMate(this);
							this.addBehaviour(new Arrive(4f, mate));
							b.addBehaviour(new Arrive(4f, this));
							break;
						}
					}
				}
			} else {
				if (PVector.dist(mate.getPos(), pos) < 1f){
					BirdColor childColor = getChildColor();
					energy = WorldConstants.INI_PREY_ENERGY;
					if (mate.getMate() != null) mate.remMate();
					if (mate != null ) this.remMate();
					child = new Bird(this, mutate, parent, plt, childColor, this.population);
					child.setRadius(WorldConstants.INI_PREY_SIZE);
					children.add(child);
				}
			}
		}
		return child;
	}
	
	public BirdColor getBirdColor(){
		return birdColor;
	}
}
