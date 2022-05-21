package CellularAutomata;
import processing.core.PApplet;
import processing.core.PImage;

public class Cell {
	protected final int row, col;
	protected int state;
	private Cell[] neighbors;
	protected CellularAutomata ca;
	
	public Cell(CellularAutomata ca, int row, int col) {
		this.ca        = ca;
		this.row       = row;
		this.col       = col;
		this.state     = 0;
		this.neighbors = null;
	}
	
	public void setNeightbors(Cell[] neigh) {
		this.neighbors = neigh;
	}
	
	public Cell[] getNeighbors() {
		return neighbors;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	public void display(PApplet p) {
		p.noStroke();
		p.fill(ca.getStateColors()[state]);
		p.rect(ca.xmin+col*ca.cellWidth, ca.ymin+row*ca.cellHeight, ca.cellWidth, ca.cellHeight);
	}
	
	public void display(PApplet p, PImage image) {
		p.noStroke();
		p.image(image, ca.xmin+col*ca.cellWidth, ca.ymin+row*ca.cellHeight);
	}

	public void display(PApplet p, int color) {
		p.noStroke();
		p.fill(color);
		p.rect(ca.xmin+col*ca.cellWidth, ca.ymin+row*ca.cellHeight, ca.cellWidth, ca.cellHeight);		
	}
}