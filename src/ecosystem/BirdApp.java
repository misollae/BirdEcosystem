package ecosystem;

import ecosystem.WorldConstants.Season;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GLabel;
import g4p_controls.GTextField;
import physics.ParticleSystem;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.SoundFile;
import setup.IProcessingApp;
import setup.PSControl;
import tools.CustomRandomGenerator;
import tools.SubPlot;
import tools.TimeGraph;

public class BirdApp implements IProcessingApp {
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;

	private Terrain terrain;
	private Population population;
	private Season season;
	private float timer;
	private SoundFile bg;
	private SoundFile wintersfx, autumnsfx;

	private float[] velParams = { PApplet.radians(225), PApplet.radians(120), 2f, 3f };
	private float[] lifetimeParams = { 16, 24 };
	private float[] radiusParams = { 0.05f, 0.08f };
	private float flow = 90;
	private ParticleSystem ps;
	private GButton updateBttn;
	private boolean chart;
	private GButton updateBttn2;
	private GButton closeBttn;
	private PImage birdB;
	private PImage birdR;
	private PImage birdY;
	private PImage birdG;
	private int[] colorsFill;
	private int[] colorsStroke;
	private GButton swapBttn;
	private boolean probability;
	private int[] probabilities;
	private GButton closeBttn2;
	private boolean stats;
	private int numYears;
	private GButton eagleButton;
	private SoundFile springsfx;
	private SoundFile summersfx;

	@Override
	public void setup(PApplet p) {
		this.numYears = 1;

		// Arrays para representação dos círculos no chart de cores
		this.colorsFill = new int[] { p.color(255, 202, 81), p.color(117, 12, 6), p.color(33, 167, 56),
				p.color(124, 129, 280) };
		this.colorsStroke = new int[] { p.color(250, 168, 61), p.color(102, 14, 8), p.color(27, 137, 46),
				p.color(107, 111, 157) };
		
		// Array com as probabilidades, por ordem, de cada mistura/resultado
		this.probabilities = new int[] { 37, 37, 19, 7, 37, 37, 19, 7, 37, 37, 19, 7, 25, 25, 0, 50, 25, 25, 0, 50, 0,
				0, 25, 75, 0, 0, 1, 99, 25, 25, 12, 38, 25, 25, 12, 38, 0, 0, 50, 50 };

		this.birdB = p.loadImage("../imagens/bluebird.png");
		this.birdB.resize(birdB.width / 15, 0);
		this.birdR = p.loadImage("../imagens/redbird.png");
		this.birdR.resize(birdR.width / 15, 0);
		this.birdY = p.loadImage("../imagens/yellowbird.png");
		this.birdY.resize(birdY.width / 15, 0);
		this.birdG = p.loadImage("../imagens/greenbird.png");
		this.birdG.resize(birdG.width / 15, 0);

		try {
			// inicialização do som
			bg = new SoundFile(p, "./sound/forestmusic.mp3");
			wintersfx = new SoundFile(p, "./sound/winter.mp3");
			autumnsfx = new SoundFile(p, "./sound/autumn2.mp3");
			springsfx = new SoundFile(p, "./sound/spring.mp3");
			summersfx = new SoundFile(p, "./sound/summer.mp3");

			bg.loop();
		} catch (Exception e) {
			System.out.println("Loading sound...");
		}

		PSControl psc = new PSControl(velParams, lifetimeParams, radiusParams, flow, p.color(166, 217, 255));
		ps = new ParticleSystem(new PVector(8, 14), new PVector(), .1f, .2f, psc);
		ps.stopAdding();

		this.timer = 0;
		this.season = WorldConstants.Season.SPRING;
		plt = new SubPlot(WorldConstants.WINDOW, viewport, p.width, p.height);
		terrain = new Terrain(p, plt, this);
		terrain.setStateColors(getColors(p));
		terrain.initRandomCustom(WorldConstants.PATCH_TYPE_PROB);
		for (int i = 0; i < 2; i++)
			terrain.majorityRule();
		population = new Population(p, plt, terrain);

		setUpG4P(p);

	}

	public void setUpG4P(PApplet p) {
		updateBttn = new GButton(p, 5, 5, 70, 20, "Bird Chart");
		updateBttn.setLocalColorScheme(180, true);
		updateBttn.addEventHandler(this, "handleUpdate");
		closeBttn = new GButton(p, 280, 8, 17, 17, "x");
		closeBttn.setLocalColorScheme(180, true);
		closeBttn.addEventHandler(this, "handleUpdate");
		closeBttn.setVisible(false);

		swapBttn = new GButton(p, 85, 8, 67, 17, "Probability");
		swapBttn.setLocalColorScheme(180, true);
		swapBttn.addEventHandler(this, "handleUpdate");
		swapBttn.setVisible(false);

		updateBttn2 = new GButton(p, 10, 27, 60, 20, "Stats");
		updateBttn2.setLocalColorScheme(180, true);
		updateBttn2.addEventHandler(this, "handleUpdate");

		eagleButton = new GButton(p, p.width - 105, 5, 100, 22, "Eagle's Eye: Off");
		eagleButton.setLocalColorScheme(180, true);
		eagleButton.addEventHandler(this, "handleUpdate");

		closeBttn2 = new GButton(p, 235, 30, 17, 17, "x");
		closeBttn2.setLocalColorScheme(180, true);
		closeBttn2.addEventHandler(this, "handleUpdate");
		closeBttn2.setVisible(false);
	}

	public void handleUpdate(GButton button, GEvent event) {
		if (button == updateBttn && event == GEvent.CLICKED) {
			closeBttn.setVisible(true);
			swapBttn.setVisible(true);
			this.chart = true;
		}
		if (button == updateBttn2 && event == GEvent.CLICKED) {
			closeBttn2.setVisible(true);
			this.stats = true;
		}
		if (button == eagleButton && event == GEvent.CLICKED) {
			population.setEagleView(!population.getEagleView());
			if (population.getEagleView())
				eagleButton.setText("Eagle's Eye: On");
			else
				eagleButton.setText("Eagle's Eye: Off");
		}
		if (button == swapBttn && event == GEvent.CLICKED) {
			if (!this.probability)
				swapBttn.setText("Quantity");
			else
				swapBttn.setText("Probability");
			this.probability = !this.probability;
		}
		if (button == closeBttn && event == GEvent.CLICKED) {
			closeBttn.setVisible(false);
			swapBttn.setVisible(false);
			this.chart = false;
		}
		if (button == closeBttn2 && event == GEvent.CLICKED) {
			closeBttn2.setVisible(false);
			this.stats = false;
		}
	}

	public Season getSeason() {
		return season;
	}

	private int[] getColors(PApplet p) {
		int[] colors = new int[WorldConstants.NSTATES];
		for (int i = 0; i < WorldConstants.NSTATES; i++) {
			colors[i] = p.color(WorldConstants.TERRAIN_COLORS[i][0], WorldConstants.TERRAIN_COLORS[i][1],
					WorldConstants.TERRAIN_COLORS[i][2]);

		}
		return colors;
	}

	@Override
	public void draw(PApplet p, float dt) {
		timer += dt;
		updateSeason();

		terrain.regenerate();
		population.update(dt, terrain);

		terrain.display(p);
		population.display(p, plt);

		ps.move(dt);
		ps.display(p, plt);

		if (chart) {
			p.stroke(158, 92, 20);
			p.fill(239, 173, 107);
			p.rect(80, 5, 220, 200);
			p.stroke(124, 71, 12);
			p.fill(158, 92, 20);
			p.rect(80, 5, 220, 20);
			p.image(birdY, 85, 30);
			p.image(birdY, 90, 37);
			p.image(birdR, 85, 60 + 3);
			p.image(birdR, 90, 67 + 3);
			p.image(birdR, 85, 90 + 6);
			p.image(birdY, 90, 97 + 6);
			p.image(birdR, 85, 120 + 9);
			p.image(birdB, 90, 127 + 9);
			p.image(birdY, 85, 150 + 12);
			p.image(birdB, 90, 157 + 12);
			p.image(birdG, 190, 30);
			p.image(birdG, 195, 37);
			p.image(birdB, 190, 60 + 3);
			p.image(birdB, 195, 67 + 3);
			p.image(birdR, 190, 90 + 6);
			p.image(birdG, 195, 97 + 6);
			p.image(birdY, 190, 120 + 9);
			p.image(birdG, 195, 127 + 9);
			p.image(birdB, 190, 150 + 12);
			p.image(birdG, 195, 157 + 12);
			for (int i = 0; i < population.getFoundMatches().size(); i++) {
				if (i < population.getFoundMatches().size() / 2) {
					if (population.getFoundMatches().get(i) == 0) {
						p.fill(158, 92, 20);
						p.textSize(12);
						p.text(probabilities[i] == 0 ? '-' : '?', 130 + 15 * (i % 4), 50 + 33 * (i / 4));
					} else {
						p.stroke(this.colorsStroke[i % 4]);
						p.fill(this.colorsFill[i % 4]);
						p.circle(135 + 14 * (i % 4), 45 + 33 * (i / 4), 7);
						p.fill(158, 92, 20);
						p.textSize(11);
						if (!probability)
							p.text(population.getFoundMatches().get(i),
									130 + 14 * (i % 4)
											+ 2 * (2 - population.getFoundMatches().get(i).toString().length()),
									60 + 33 * (i / 4));
						else
							p.text("." + probabilities[i],
									129 + 14 * (i % 4) + 2 * (2 - String.valueOf(probabilities[i]).length()),
									60 + 33 * (i / 4));
					}
				}

				if (i >= population.getFoundMatches().size() / 2) {
					if (population.getFoundMatches().get(i) == 0) {
						p.fill(158, 92, 20);
						p.text(probabilities[i] == 0 ? '-' : '?',
								235 + 15 * ((i - population.getFoundMatches().size() / 2) % 4),
								50 + 33 * ((i - population.getFoundMatches().size() / 2) / 4));
					} else {
						p.stroke(this.colorsStroke[i % 4]);
						p.fill(this.colorsFill[i % 4]);
						p.circle(240 + 14 * ((i - population.getFoundMatches().size() / 2) % 4),
								45 + 33 * ((i - population.getFoundMatches().size() / 2) / 4), 7);
						p.fill(158, 92, 20);
						if (!probability)
							p.text(population.getFoundMatches().get(i),
									235 + 14 * (i % 4)
											+ 2 * (2 - population.getFoundMatches().get(i).toString().length()),
									60 + 33 * ((i - population.getFoundMatches().size() / 2) / 4));
						else
							p.text("." + probabilities[i],
									234 + 14 * (i % 4) + 2 * (2 - String.valueOf(probabilities[i]).length()),
									60 + 33 * ((i - population.getFoundMatches().size() / 2) / 4));

					}
				}
			}
		}

		if (stats) {
			p.stroke(158, 92, 20);
			p.fill(239, 173, 107);
			p.rect(80, 27, 177, 190);
			p.stroke(124, 71, 12);
			p.fill(158, 92, 20);
			p.rect(80, 27, 177, 20);
			p.rect(88, 55, 160, 100);

			p.text("Colors", 152, 168);

			p.textSize(12);
			p.text("Year: " + numYears, 85, 185);
			p.text("Amount of birds: " + population.getNumAnimals(), 85, 197);
			p.text("Eagle attacks: " + population.getEagleAttacks(), 85, 209);

			for (int i = 0; i < colorsFill.length; i++) {
				float barSize = population.getAmountOfColor(WorldConstants.BirdColor.values()[i]) * 1.5f;

				if (i == 0)
					p.image(birdY, 95 + i * 35, 155 - barSize - birdY.height);
				if (i == 1)
					p.image(birdR, 95 + i * 35, 155 - barSize - birdY.height);
				if (i == 2)
					p.image(birdG, 95 + i * 35, 155 - barSize - birdY.height);
				if (i == 3)
					p.image(birdB, 95 + i * 35, 155 - barSize - birdY.height);

				p.fill(70, 37, 13);
				p.text(population.getAmountOfColor(WorldConstants.BirdColor.values()[i]), 110 + i * 35,
						155 - barSize - birdY.height - 3);
				p.stroke(this.colorsStroke[i % 4]);
				p.fill(this.colorsFill[i % 4]);
				p.rect(100 + i * 35, 155 - barSize, 25, barSize);
			}
		}
	}

	private void updateSeason() {
		if (this.season == WorldConstants.Season.WINTER && timer >= 15)
			ps.stopAdding();

		if (timer >= 20) {
			this.timer = 0;
			int currentSeason = this.season.ordinal();

			if (currentSeason == 3)
				this.season = WorldConstants.Season.values()[0];
			else
				this.season = WorldConstants.Season.values()[currentSeason + 1];

			if (this.season == WorldConstants.Season.SUMMER) {
				summersfx.play();
			}
			if (this.season == WorldConstants.Season.SPRING) {
				numYears++;
				springsfx.play();
			}
			if (this.season == WorldConstants.Season.AUTUMN)
				autumnsfx.play();
			if (this.season == WorldConstants.Season.WINTER) {
				for (Animal b : population.getAnimals()) {
					((Bird) b).remMate();
				}
				wintersfx.play();
				ps.startAdding();
			}
		}

	}

	@Override
	public void mousePressed(PApplet p) {
	}

	@Override
	public void mouseReleased(PApplet p) {
	}

	@Override
	public void keyPressed(PApplet p) {
	}

	@Override
	public void mouseDragged(PApplet p) {
	}

}
