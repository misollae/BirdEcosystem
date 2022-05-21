package ecosystem;
import processing.core.*;
import setup.IProcessingApp;
import tools.SubPlot;


public class WorldConstants {
	// World
	public final static double[] WINDOW = {-10, 10, -10, 10};
		
	// Terrain
	public final static int NROWS = 45;
	public final static int NCOLS = 60;
//	public final static int NROWS = 35;
//	/public final static int NCOLS = 50;
	public static enum PatchType {
		EMPTY, OBSTACLE, FERTILE, FOOD;
	}
	public final static double[] PATCH_TYPE_PROB = {0.2f, 0.16f, 0.24f, .4f};
	public final static int NSTATES = PatchType.values().length;
	public static int[][] TERRAIN_COLORS = {
			{250, 200, 60}, {160, 30, 70}, {200, 200, 60}, {128, 181, 67}
	};
	
	public static enum BirdColor {
			YELLOW, RED, GREEN, BLUE;
	};
	
	public static enum Season {
		SPRING, SUMMER, AUTUMN, WINTER;
	};	

	public final static float[] REFENERATION_TIME = {10.f, 20.f}; // seconds

	// Prey Population
	public final static float INI_PREY_SIZE = .48f;
	public final static float PREY_SIZE = .78f;
	public final static float PREY_MASS = 1f;
	public final static int MAX_NUM_CHILDREN = 3;
	
	public final static float EAGLE_SIZE = 1.08f;
	public final static float EAGLE_MASS = 1f;

	public final static int INI_PREY_POPULATION = 4;
	public final static float INI_PREY_ENERGY = 10f;
	public final static float ENERGY_FROM_PLANT = 4f;
	public final static float PREY_ENERGY_TO_REPRODUCE = 70f;
	public final static int[] BASE_PREY_COLOR = {80, 100, 220};
}
