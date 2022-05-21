package aa;

import physics.Body;
import processing.core.PVector;

public class Seek extends Behavior {

	private Body specifiedTarget;

	public Seek(float weight) {
		super(weight);
	}
	
	public Seek(float weight, Body specifiedTarget) {
		super(weight);
		this.specifiedTarget = specifiedTarget;
	}
	
	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.eye.getTarget();
		if (specifiedTarget != null ) return PVector.sub(specifiedTarget.getPos(), me.getPos());
		return PVector.sub(bodyTarget.getPos(), me.getPos()); // velocidade desejada
	}
	
}
