package aa;

public class DNA {
	public float maxSpeed;
	public float maxForce;
	public float visionDistance;
	public float visionSafeDistance;
	public float visionAngle;
	public float deltaTPursuit;
	public float radiusArrive;
	public float deltaTWander;
	public float radiusWander;
	public float deltaPhiWander;
	
	public DNA() {
		//Physics
		maxSpeed           = random(1f, 1.5f);
		maxForce           = random(4f, 7f);
		//Vision
		visionDistance     = random(1.5f, 2.5f);
		visionSafeDistance = 0.25f * visionDistance;
		visionAngle    = (float)Math.PI * 0.3f;
		//Persuit
		deltaTPursuit  = random(0.5f, 1f);
		//Arrive
		radiusArrive   = random(2.5f, 4.5f);
		//Wander
		deltaTWander = random(.3f, .6f);
		radiusWander   = random(1,3);
		deltaPhiWander = (float)Math.PI/8;
	}
	
	public DNA(DNA dna, boolean mutate) {
		//Physics
		maxSpeed           = dna.maxSpeed;
		maxForce           = dna.maxForce;
		//Vision
		visionDistance     = dna.visionDistance;
		visionSafeDistance = dna.visionSafeDistance;
		visionAngle    = dna.visionAngle;
		//Persuit
		deltaTPursuit  = dna.deltaTPursuit;
		//Arrive
		radiusArrive   = dna.radiusArrive;
		//Wander
		deltaTWander   = dna.deltaTWander;
		radiusWander   = dna.radiusWander;
		deltaPhiWander = dna.deltaPhiWander;
		if (mutate) mutate();
	}
	
	private void mutate() {
		maxSpeed += random(-0.2f, 0.2f);
		maxSpeed = Math.max(0, maxSpeed);
	}
	
	public void reRandomizeSpeed(float min, float max) {
		maxSpeed = random(min, max);		
	}
	
	public void reRandomizeForce(float min, float max) {
		maxForce = random(min, max);	
	}
	
	public void reRandomizeRadiusArrive(float min, float max) {
		radiusArrive = random(min, max);	
	}
	
	public void reRandomizeDeltaTWander(float min, float max) {
		deltaTWander = random(min, max);	
	}
	
	public void reRandomizeRadiusWander(float min, float max) {
		radiusWander = random(min, max);	
	}
	
	public static float random(float min, float max) {
		return (float) (min + (max - min)*Math.random());
	}

}
