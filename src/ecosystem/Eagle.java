package ecosystem;

import aa.Arrive;
import aa.Boid;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.SoundFile;
import tools.SubPlot;

public class Eagle extends Boid {

	boolean attacking;
	int attackStage = 0;
	private PImage eagleimg;
	private PImage eagleimgI;
	private SoundFile sound;

	public Eagle(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
	
		super(pos, mass, radius, color, p, plt);
		
		try {
			sound = new SoundFile(p, "./sound/hawk.mp3");
		} catch (Exception e) {
			System.out.println("Loading sound...");
		}
		
		this.eagleimg = p.loadImage("../imagens/eagle.png");
		this.eagleimgI = p.loadImage("../imagens/eagle2.png");

		eagleimg.resize(eagleimg.width/7, 0);
		eagleimgI.resize(eagleimgI.width/7, 0);

	}

	public boolean isAttacking() {
		return attacking;
	}
	
	public void stopAttacking() {
		behaviors.clear();
		attackStage = 0;
		attacking = false;
	}
	
	@Override 
	public void display(PApplet p, SubPlot plt) {
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		if (attacking) {
			
			if (Math.abs(vel.heading()) < 2) p.image(eagleimg, pp[0] - eagleimg.width/2, pp[1] - eagleimg.height/2);
			else p.image(eagleimgI, pp[0] - eagleimg.width/2, pp[1] - eagleimg.height/2);
		}
	}
	
	public void attack(Population population) {
		if ((attackStage == 1 && !population.getVisibleTargets().contains(eye.getTarget())) || (!isAttacking() && Math.random() * 800 < 5)) {
			behaviors.clear();
			if (population.getVisibleTargets().isEmpty()) return;
			if (!attacking) {
				sound.play();
				setPos(new PVector(-15, -15));
			}
			attacking = true;
			Body target = population.getVisibleTargets().get((int) Math.floor((Math.random() * (population.getVisibleTargets().size()))));
			eye.setTarget(target);
			addBehaviour(new Arrive(1f, target));
			attackStage = 1;
		} else if (attackStage == 1) {
			if (PVector.dist(pos, eye.getTarget().getPos()) < 1.5f) {
				population.eagleAttack(pos.copy());
				behaviors.clear();
				Body target = new Body(new PVector(-20, 20));
				eye.setTarget(target);
				addBehaviour(new Arrive(1f, target));
				attackStage = 2;
			}
		} else if (attackStage == 2) {
			if (pos.x < -20 || pos.y < -20 || pos.y > 20 || pos.x > 20) {
				attacking = false; 
				behaviors.clear();
				attackStage = 0;
			}
		}
	}
}
