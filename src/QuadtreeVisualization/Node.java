package QuadtreeVisualization;

import java.awt.Color;

/**
 * @author Sapo 
 * 		   This class represents points. Every points have x and y
 *         coordinates, four childs(they are north-west, nort-east,south-west
 *         and south east), Color(default color is red), region, parent, quadrant
 *         and ID.
 */
class Node {
	private int x;
	private int y;
	private int id;

	Quadrant quadrant = new Quadrant();

	private Node NW;
	private Node NE;
	private Node SW;
	private Node SE;
	private Node parent;

	private regions region;

	Color color;

	public enum regions {
		NW, NE, SW, SE;
	}

	public Node() {

	}

	public Node(int x, int y, int id, Color color) {
		this.x = x;
		this.y = y;
		NW = null;
		NE = null;
		SW = null;
		SE = null;
		this.color = color;
		this.id = id;
	}

	public void clear() {
		NW = null;
		NE = null;
		SW = null;
		SE = null;
		parent = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Node getNW() {
		return NW;
	}

	public void setNW(Node nW) {
		NW = nW;
	}

	public Node getNE() {
		return NE;
	}

	public void setNE(Node nE) {
		NE = nE;
	}

	public Node getSW() {
		return SW;
	}

	public void setSW(Node sW) {
		SW = sW;
	}

	public Node getSE() {
		return SE;
	}

	public void setSE(Node sE) {
		SE = sE;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public regions getRegion() {
		return region;
	}

	public void setRegion(regions region) {
		this.region = region;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}