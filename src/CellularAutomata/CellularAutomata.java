package CellularAutomata;

import processing.core.PApplet;
import tools.CustomRandomGenerator;
import tools.SubPlot;

public class CellularAutomata {
	protected int nrows;
	protected int ncols;
	protected int nStates;
	protected int radiusNeigh;
	protected Cell[][] cells;
	protected int[] colors;
	protected SubPlot plt;
	protected float cellWidth, cellHeight;
	protected float xmin, ymin;
	
	public CellularAutomata(PApplet p, SubPlot plt, int nrows, int ncols, int nStates, int radiusNeigh) {
		this.nrows       = nrows;
		this.ncols       = ncols;
		this.nStates     = nStates;
		this.radiusNeigh = radiusNeigh;
		this.plt = plt;
		float[] bb = plt.getBoundingBox();
		xmin = bb[0];
		ymin = bb[1];
		cells  = new Cell[nrows][ncols];
		colors = new int[nStates];
		cellWidth  = bb[2]  / ncols;
		cellHeight = bb[3] / nrows;
		createCells();
		setStateColors(p);
	}
	
	public void setStateColors(PApplet p) {
		for (int i = 0 ; i < nStates ; i++) {
			colors[i] = p.color(p.random(255), p.random(255), p.random(255));
		}
	}
	
	public void setStateColors(int[] colors) {
		this.colors = colors;
	}
	
	public int[] getStateColors() {
		return colors;
	}

	protected void createCells() {
		for (int i=0 ; i<nrows ; i++) {
			for (int j=0 ; j<ncols ; j++) {
				cells[i][j] = new Cell(this, i, j);
			}
		}
		setMooreNeighbors();
	}

	public void initRandom() {
		for (int i=0 ; i < nrows ; i++) {
			for (int j=0 ; j < ncols ; j++) {
				cells[i][j].setState((int)((nStates)*Math.random()));
			}
		}
	}
	
	public void initRandomCustom(double[] pmf) {
		CustomRandomGenerator crg = new CustomRandomGenerator(pmf);
		for (int i=0 ; i < nrows ; i++) {
			for (int j=0 ; j < ncols ; j++) {
				cells[i][j].setState(crg.getRandomClass());
			}
		}
	}
	
	public Cell world2Cell(double x, double y) {
		float[] xy = plt.getPixelCoord(x, y);
        return pixel2Cell(xy[0], xy[1]);
	}
	
	public Cell pixel2Cell(float x, float y) {
		int row = (int) ((y-ymin)/cellHeight);
		int col = (int) ((x-xmin)/cellWidth);
		if (row >= nrows) row = nrows-1;
		if (col >= ncols) col = ncols-1;
		if (row < 0) row = 0;
		if (col < 0) col = 0;
        return cells[row][col];
	}
	
	protected void setMooreNeighbors() {
		// Definir quantos vizinhos se tem (fórmula de Moore)
		int nNeighbors = (int)Math.pow(2*radiusNeigh + 1, 2);
		
		// Percorrer cada célula
		for (int i=0 ; i < nrows ; i++) {
			for (int j=0 ; j < ncols ; j++) {
				
				Cell[] neigh = new Cell[nNeighbors]; // Estando em cada célula cria-se o array da vizinhança
				int n = 0;	                         // E uma variável auxiliar
				
				// E percorre-se os vizinhos
				for (int ii=-radiusNeigh ; ii <= radiusNeigh ; ii++) {
					for (int jj=-radiusNeigh ; jj <= radiusNeigh ; jj++) {
						int row = (i + ii + nrows) % nrows; // Aritmética circular
						int col = (j + jj + ncols) % ncols; 
						neigh[n++] = cells[row][col]; // Colocando-os no array
					}
				}
				cells[i][j].setNeightbors(neigh);
			}
		}	
	}
	
	public void display(PApplet p) {
		p.pushStyle();
		for (int i=0 ; i < nrows ; i++) {
			for (int j=0 ; j < ncols ; j++) {
				cells[i][j].display(p);
			}
		}
		p.popStyle();
	}
	
}
