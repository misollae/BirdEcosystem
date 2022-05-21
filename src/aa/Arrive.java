package aa;

import physics.Body;
import processing.core.PApplet;
import processing.core.PVector;

public class Arrive extends Behavior{

	private Body specifiedTarget;

	public Arrive(float weight) {
		super(weight);
	}
	
	public Arrive(float weight, Body specifiedTarget) {
		super(weight);
		this.specifiedTarget = specifiedTarget;
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		 float R = me.dna.radiusArrive;
		 PVector desired;
		 if (specifiedTarget != null) desired = PVector.sub(specifiedTarget.getPos(),me.getPos());
		 else desired = PVector.sub(me.eye.getTarget().getPos(),me.getPos());
	 	 float d = desired.mag();
	 	 desired.normalize();
	 	 if (d < R) {
	 		float m = PApplet.map(d,0,R,0,me.dna.maxSpeed);
	      	desired.mult(m);
	    } else {
	    	desired.mult(me.dna.maxSpeed);
	    }
	    PVector steer = PVector.sub(desired,me.getVel());
	    steer.limit(me.dna.maxForce);
	    return steer;
	}
}
