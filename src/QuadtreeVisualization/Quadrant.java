package QuadtreeVisualization;

/**
 * @author Sarper This class represents quadrants. There is four integers in
 *         quadrant class. They are quadrants' lower and higher X-Y Axis
 *         coordinates for each node.
 */
public class Quadrant {
	int highQuadrantX;
	int highQuadrantY;
	int lowQuadrantX;
	int lowQuadrantY;

	public int getHighQuadrantX() {
		return highQuadrantX;
	}

	public void setHighQuadrantX(int highQuadrantX) {
		this.highQuadrantX = highQuadrantX;
	}

	public int getHighQuadrantY() {
		return highQuadrantY;
	}

	public void setHighQuadrantY(int highQuadrantY) {
		this.highQuadrantY = highQuadrantY;
	}

	public int getLowQuadrantX() {
		return lowQuadrantX;
	}

	public void setLowQuadrantX(int lowQuadrantX) {
		this.lowQuadrantX = lowQuadrantX;
	}

	public int getLowQuadrantY() {
		return lowQuadrantY;
	}

	public void setLowQuadrantY(int lowQuadrantY) {
		this.lowQuadrantY = lowQuadrantY;
	}

	public Quadrant() {

	}
}
