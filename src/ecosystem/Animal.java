package ecosystem;

import java.util.List;

import aa.Behavior;
import aa.Boid;
import aa.DNA;
import aa.Eye;
import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

public abstract class Animal extends Boid implements IAnimal {

	protected float energy;
	protected float lifetime;

	protected Animal(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(pos, mass, radius, color, p, plt);
		lifetime = 0;
	}
	
	protected Animal(Animal a, boolean mutate, PApplet p, SubPlot plt) {
		super(a.pos, a.mass, a.radius, a.color, p, plt);
		lifetime = 0;
		for(Behavior b : a.behaviors) {
			this.addBehaviour(b);
		}
		if (a.eye != null) {
			eye = new Eye(this, a.eye);
		}
		dna = new DNA(a.dna, mutate);
	}
	@Override
	public boolean die() {
		return (energy < 0) || lifetime < 0;
	}
	
	public void grow(float max) {
		this.radius = Math.min(max, this.radius + .002f);
	}

	@Override
	public void energy_consumption(float dt, Terrain terrain) {
		energy -= terrain.getSeason() == WorldConstants.Season.WINTER ? 1.2f*dt : dt; // metabolismo
		lifetime -= dt;
		energy -= mass*Math.pow(vel.mag(), 2) * dt;
	}

	public List<Behavior> getBehaviors() {
		return behaviors;
	}
}
