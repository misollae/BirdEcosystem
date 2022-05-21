package ecosystem;

import java.util.ArrayList;
import java.util.List;

import aa.AvoidObstacle;
import aa.Eye;
import aa.Seek;
import aa.Wander;
import ecosystem.WorldConstants.BirdColor;
import ecosystem.WorldConstants.Season;
import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

public class Population {
	private List<Animal> allAnimals;
	private double[] window;
	private boolean mutate = false;
	private Terrain terrain;
	private ArrayList<Integer> foundMatches;
	private Eagle eagle;
	private int eagleAttacks;
	private boolean eagleView;
	
	public Population(PApplet parent, SubPlot plt, Terrain terrain) {
		this.terrain = terrain;
		window = plt.getWindow();
		allAnimals = new ArrayList<Animal>();
		List<Body> obstacles = terrain.getObstacles();
		
		this.foundMatches = new ArrayList<Integer>(40);
		for (int i = 0 ; i < 40 ; i++) {
			foundMatches.add(0);
		}

		for (int i = 0; i < WorldConstants.INI_PREY_POPULATION; i++) {
			PVector pos = new PVector(parent.random((float)window[0], (float)window[1]), parent.random((float)window[2], (float)window[3]));
			Animal a = new Bird(pos, WorldConstants.PREY_MASS, WorldConstants.PREY_SIZE, parent, plt, WorldConstants.BirdColor.values()[(int) parent.random(0, 2)], this);
			a.addBehaviour(new Wander(1));
			if (!obstacles.isEmpty()) {
				a.addBehaviour(new AvoidObstacle(3));
				Eye eye = new Eye(a, obstacles);
				a.setEye(eye);
			}
			allAnimals.add(a);
		}
		
		PVector pose = new PVector(0,0);
		eagle = new Eagle(pose, WorldConstants.EAGLE_MASS, WorldConstants.EAGLE_SIZE, parent.color(0), parent, plt);
		Eye eye = new Eye(eagle, getVisibleTargets());
		eagle.getDNA().reRandomizeSpeed(3, 6);
		eagle.setEye(eye);


		
	}
	
	public ArrayList<Integer> getFoundMatches() {
		return foundMatches;
	}
	
	public List<Body> getVisibleTargets() {
		List<Body> targets = new ArrayList<Body>();
		for(Animal a : allAnimals) {
			if (terrain.getSeason() == WorldConstants.Season.AUTUMN && (((Bird)a).getBirdColor() == WorldConstants.BirdColor.YELLOW || ((Bird)a).getBirdColor() == WorldConstants.BirdColor.RED)) {
				continue;
			}
			if (terrain.getSeason() == WorldConstants.Season.WINTER && ((Bird)a).getBirdColor() == WorldConstants.BirdColor.BLUE) {
				continue;
			}
			if ((terrain.getSeason() == WorldConstants.Season.SUMMER || terrain.getSeason() == WorldConstants.Season.SPRING) && (((Bird)a).getBirdColor() == WorldConstants.BirdColor.GREEN)) {
				continue;
			}
			targets.add(a);
		}
		return targets;
	}
	
	public void update(float dt, Terrain terrain) {
		eagle.attack(this);
		move(terrain, dt);
		eat(terrain);
		energy_consumption(dt, terrain);
		reproduce();
		grow();
		die();
	}
	
	private void move(Terrain terrain, float dt) {
		for(Animal a : allAnimals)
			a.applyBehaviors(dt);
		eagle.applyBehaviors(dt);
	}
	
	private void grow() {
		for(Animal a : allAnimals)
			a.grow(WorldConstants.PREY_SIZE); 
	}
	
	private void eat(Terrain terrain) {
		for(Animal a : allAnimals)
			a.eat(terrain);
	}
	
	private void energy_consumption(float dt, Terrain terrain) {
		for(Animal a : allAnimals)
			a.energy_consumption(dt, terrain);
	}
	
	private void die() {
		for(int i = allAnimals.size()-1 ; i>=0 ; i--) {
			Animal a = allAnimals.get(i);
			if (a.die()) {
				if (eagle.getEye().getTarget().equals(a)) {
					eagle.stopAttacking();
				}
				if (((Bird) a).getMate() != null) {
					((Bird) a).getMate().removeBehavior(((Bird) a).getMate().getBehaviors().get(((Bird) a).getMate().getBehaviors().size()-1));
					((Bird) a).getMate().remMate();
				}
				allAnimals.remove(a);	
			}
		}
	}
	
	private void reproduce() {
		for(int i = allAnimals.size()-1 ; i>=0 ; i--) {
			Animal a = allAnimals.get(i);
			Animal child = a.reproduce(mutate);
			if (child != null) {
				allAnimals.add(child);	
			}
		}
	}
	
	public void display(PApplet p, SubPlot plt) {
		if (!eagleView) {
			for (Animal a : allAnimals)
				a.display(p, plt);
		} else {
			for (Body a : getVisibleTargets())
				a.display(p, plt);
		}
		eagle.display(p, plt);
	}
	
	public int getNumAnimals() {
		return allAnimals.size();
	}
	
	public List<Animal> getAnimals() {
		return allAnimals;
	}
	
	public float getMeanMaxSpeed() {
		float sum = 0;
		for(Animal a : allAnimals)
			sum += a.getDNA().maxSpeed;
		return sum/allAnimals.size();
	}
	
	public float[] getMeanWeights() {
		float[] sums = new float[2];
		for(Animal a : allAnimals) {
			sums[0] += a.getBehaviors().get(0).getWeight();
			sums[1] += a.getBehaviors().get(1).getWeight();
		}
		sums[0] /= allAnimals.size();
		sums[1] /= allAnimals.size();
		return sums;
	}
	
	public float getStdMaxSpeed() {
		float mean = getMeanMaxSpeed();
		float sum = 0;
		for(Animal a : allAnimals)
			sum += Math.pow(a.getDNA().maxSpeed - mean, 2);
		return (float)Math.sqrt(sum/allAnimals.size());
	}


	public Season getSeason() {
		return terrain.getSeason();
	}
	
	public int getAmountOfColor(BirdColor color) {
		int cont = 0;
		for(Animal a : allAnimals) {
			if (((Bird)a).getBirdColor() == color) cont++; 
		}
		return cont;
	}

	public void matchFound(int i) {
		foundMatches.set(i, foundMatches.get(i)+1);
	}

	public void eagleAttack(PVector attackPos) {
		eagleAttacks++;
		for(int i = allAnimals.size()-1 ; i>=0 ; i--) {
			Animal a = allAnimals.get(i);
			if (getVisibleTargets().contains(a) && PVector.dist(a.getPos(), attackPos) < 2) {
				if (((Bird) a).getMate() != null) {
					((Bird) a).getMate().removeBehavior(((Bird) a).getMate().getBehaviors().get(((Bird) a).getMate().getBehaviors().size()-1));
					((Bird) a).getMate().remMate();
				}
				allAnimals.remove(a);
			}
		}
		
	}

	public int getEagleAttacks() {
		return eagleAttacks;
	}

	public boolean getEagleView() {
		return eagleView;
	}

	public void setEagleView(boolean eagleView) {
		this.eagleView = eagleView;
	}
}
