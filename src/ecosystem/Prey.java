package ecosystem;

import processing.core.PApplet;
import processing.core.PVector;
import tools.SubPlot;

public class Prey extends Animal {
	protected PApplet parent;
	protected SubPlot plt;

	public Prey(PVector pos, float mass, float radius, int color, PApplet parent, SubPlot plt) {
		super(pos, mass, radius, color, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = WorldConstants.INI_PREY_ENERGY;
	}
	
	public Prey(Prey prey, boolean mutate, PApplet parent, SubPlot plt) {
		super(prey, mutate, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = WorldConstants.INI_PREY_ENERGY;
	}

	@Override
	public Animal reproduce(boolean mutate) {
		Animal child = null;
		if (energy > WorldConstants.PREY_ENERGY_TO_REPRODUCE) {
			energy -= WorldConstants.PREY_ENERGY_TO_REPRODUCE;
			child = new Prey(this, mutate, parent, plt);
			if (mutate) child.mutateBehaviors();
		}
		return child;
	}

	@Override
	public void eat(Terrain terrain) {
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		if (patch.getState() == WorldConstants.PatchType.FOOD.ordinal()) {
			energy += WorldConstants.ENERGY_FROM_PLANT;
			patch.setFertile();
		}
	}

}
