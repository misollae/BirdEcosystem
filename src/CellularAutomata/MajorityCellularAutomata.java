package CellularAutomata;

import processing.core.PApplet;
import tools.SubPlot;

public class MajorityCellularAutomata extends CellularAutomata {
	
	public MajorityCellularAutomata(PApplet p, SubPlot plt, int nrows, int ncols, int nStates, int radiusNeigh) {
		super(p, plt, nrows, ncols, nStates, radiusNeigh);
	}
	
	@Override
	protected void createCells() {
		for (int i=0 ; i<nrows ; i++) {
			for (int j=0 ; j<ncols ; j++) {
				cells[i][j] = new MajorityCell(this, i, j);
			}
		}
		setMooreNeighbors();
	}

	public boolean majorityRule() {
		for (int i=0 ; i<nrows ; i++) {
			for (int j=0 ; j<ncols ; j++) {
				((MajorityCell) cells[i][j]).computeHistogram();
			}
		}
		
		boolean anyChanged = false;
		for (int i=0 ; i<nrows ; i++) {
			for (int j=0 ; j<ncols ; j++) {
				boolean changed = ((MajorityCell) cells[i][j]).applyMajorityRule();
				if (changed) anyChanged = true;
			}
		}
		return anyChanged; 
	}
}
