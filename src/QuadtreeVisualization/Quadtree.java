package QuadtreeVisualization;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import QuadtreeVisualization.Node.regions;

/**
 * @author Sapo
 *
 */
public class Quadtree implements MouseListener, ActionListener, ChangeListener {
	JSlider radius = new JSlider(1, 300, 1);

	public static String textLabelOutput = "";

	TextArea outputText = new TextArea(20, 20);

	public static Node root = null;
	public static int id = 0;
	public static boolean flag = false; // If you click screen to add point when
										// flag is true, then query circle will
										// appear. Select 'create query' in User
										// Interface to set it as true.
	public static boolean isAddElementSelected = true;
	public int circleXAxis = -50, circleYAxis = -50, circleRadius = 10;

	JPanel controlPanel = new JPanel();
	JFrame frame = new JFrame();
	JFrame controlFrame = new JFrame();

	public static void main(String[] args) {
		new Quadtree();
	}

	ArrayList<Node> nodeList = new ArrayList<Node>();

	JPanel thePanel = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (root != null)
				if (!isAddElementSelected) {// If 'Create Query' option was
											// selected.
					findSmallestRect(circleXAxis, circleYAxis, circleRadius, root);

					g.setColor(Color.BLACK);
					g.drawOval(circleXAxis - circleRadius, circleYAxis - circleRadius, circleRadius * 2,
							circleRadius * 2);
					isAddElementSelected = false;
				}
			for (Node e : nodeList) {// Draw whole tree
				g.setColor(e.getColor());
				g.fillOval(e.getX() - 5, e.getY() - 5, 10, 10);

				g.drawLine(e.getX(), e.quadrant.getLowQuadrantY(), e.getX(), e.quadrant.getHighQuadrantY());
				g.drawLine(e.quadrant.getLowQuadrantX(), e.getY(), e.quadrant.getHighQuadrantX(), e.getY());

			}

			editTextArea();

		}

	};

	/**
	 * Edits text area in User Interface, shows how many points in circle and
	 * their coordinates.
	 */
	public void editTextArea() {
		int sum = 0;
		String coords = "";
		int XYAxisArray[][] = new int[nodeList.size()][3];
		int i = 0;
		for (Node e : nodeList) {
			if (e.getColor() == Color.BLUE) {
				sum++;
				XYAxisArray[i][0] = e.getX();
				XYAxisArray[i][1] = e.getY();
				XYAxisArray[i][2] = e.getId();

			} else
				XYAxisArray[i][2] = -1;

			i++;
		}

		Arrays.sort(XYAxisArray, new Comparator<int[]>() {
			@Override
			public int compare(int[] s1, int[] s2) {
				Integer t1 = s1[0];
				Integer t2 = s2[0];
				Integer t3 = s1[1];
				Integer t4 = s2[1];
				if (t1 != t2)
					return t1.compareTo(t2);
				else
					return t3.compareTo(t4);
			}
		});
		for (i = 0; i < nodeList.size(); i++) {
			if (XYAxisArray[i][2] != -1) {
				coords = coords + "\n(" + XYAxisArray[i][0] + "," + XYAxisArray[i][1] + ")";
			}
		}

		String output = "" + sum + "" + coords;
		outputText.setText(output);
	}

	public Quadtree() {
		frame.setSize(512, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(thePanel);
		thePanel.addMouseListener(this);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setTitle("Quadtree");
		controlFrame.setSize(512, 512);
		controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlFrame.add(controlPanel);
		frame.setLocation(500, 500);
		controlFrame.setResizable(false);
		controlFrame.setTitle("Control Panel");
		/* Control frame components */

		ButtonGroup option = new ButtonGroup();

		JButton resetButton = new JButton("Reset");
		resetButton.setSize(200, 100);
		resetButton.addActionListener(this);
		resetButton.setName("resetB");
		resetButton.setActionCommand("reset");

		JButton randomButton = new JButton("Add Random Element");
		randomButton.addActionListener(this);
		randomButton.setName("addRandom");
		randomButton.setActionCommand("addRandom");

		JRadioButton addElement = new JRadioButton("Add Element");
		addElement.addActionListener(this);
		addElement.setName("addElement");
		addElement.setActionCommand("addElement");
		addElement.setSelected(true);

		JRadioButton createQuery = new JRadioButton("Create Query");
		createQuery.addActionListener(this);
		createQuery.setName("createQuery");
		createQuery.setActionCommand("createQuery");

		option.add(addElement);
		option.add(createQuery);

		JLabel textRadius = new JLabel("Radius: ");
		radius.addChangeListener(this);

		radius.setPaintTicks(true);
		radius.setPaintLabels(true);

		outputText.setEditable(false);

		controlPanel.add(resetButton, BorderLayout.BEFORE_LINE_BEGINS);
		controlPanel.add(randomButton, BorderLayout.AFTER_LINE_ENDS);
		controlPanel.add(addElement, BorderLayout.NORTH);
		controlPanel.add(createQuery, BorderLayout.NORTH);
		controlPanel.add(textRadius);
		controlPanel.add(radius);
		controlPanel.add(outputText);
		controlFrame.pack();
		outputText.requestFocus();
		controlFrame.setVisible(true);

	}

	/**
	 * Gets point as parameter, this method inserts element to quadtree
	 * 
	 * @param point
	 */
	public void insert(Point point) {
		if (point.getX() > 507.0 || point.getY() > 507.0 || point.getX() < 5.0 || point.getY() < 5.0) {
			return;

		}

		if (root == null) {
			Node newNode = new Node((int) (point.getX()), (int) (point.getY()), id++, Color.RED);
			root = newNode;
			root.quadrant.setHighQuadrantX(512);
			root.quadrant.setLowQuadrantX(0);
			root.quadrant.setHighQuadrantY(512);
			root.quadrant.setLowQuadrantY(0);
			addElement(newNode);
			return;
		}
		Node current = root;
		Node parent = null;
		Node newNode = new Node((int) (point.getX()), (int) point.getY(), id++, Color.RED);
		while (true) {
			parent = current;
			/* If this point already exists */
			if (point.getX() == current.getX() && point.getY() == current.getY() && current != null
					|| (point.getX() < current.quadrant.getHighQuadrantX()
							&& point.getX() > current.quadrant.getLowQuadrantX() && point.getY() == current.getY())
					|| (point.getY() < current.quadrant.getHighQuadrantY()
							&& point.getY() > current.quadrant.getLowQuadrantY() && point.getX() == current.getX())) {
				newNode = null;
				id--;
				return;
			}

			/* Find out new point's region */
			if (point.getX() < current.getX() && point.getY() < current.getY()) {
				current = current.getNW();
				if (current == null) {
					parent.setNW(newNode);
					newNode.quadrant.setHighQuadrantX(setHighQuadrantX((int) point.getX(), (int) point.getY()));
					newNode.quadrant.setHighQuadrantY(setHighQuadrantY((int) point.getY(), (int) point.getX()));
					newNode.quadrant.setLowQuadrantX(setLowQuadrantX((int) point.getX(), (int) point.getY()));
					newNode.quadrant.setLowQuadrantY(setLowQuadrantY((int) point.getY(), (int) point.getX()));
					newNode.setRegion(regions.NW);
					newNode.setParent(parent);
					addElement(newNode);
					return;
				}
			} else if (point.getX() > current.getX() && point.getY() < current.getY()) {
				current = current.getNE();
				if (current == null) {
					parent.setNE(newNode);
					newNode.quadrant.setHighQuadrantX(setHighQuadrantX((int) point.getX(), (int) point.getY()));
					newNode.quadrant.setHighQuadrantY(setHighQuadrantY((int) point.getY(), (int) point.getX()));
					newNode.quadrant.setLowQuadrantX(setLowQuadrantX((int) point.getX(), (int) point.getY()));
					newNode.quadrant.setLowQuadrantY(setLowQuadrantY((int) point.getY(), (int) point.getX()));
					newNode.setRegion(regions.NE);
					newNode.setParent(parent);
					addElement(newNode);
					return;
				}
			} else if (point.getX() < current.getX() && point.getY() > current.getY()) {
				current = current.getSW();
				if (current == null) {
					parent.setSW(newNode);
					newNode.quadrant.setHighQuadrantX(setHighQuadrantX((int) point.getX(), (int) point.getY()));
					newNode.quadrant.setHighQuadrantY(setHighQuadrantY((int) point.getY(), (int) point.getX()));
					newNode.quadrant.setLowQuadrantX(setLowQuadrantX((int) point.getX(), (int) point.getY()));
					newNode.quadrant.setLowQuadrantY(setLowQuadrantY((int) point.getY(), (int) point.getX()));
					newNode.setRegion(regions.SW);
					newNode.setParent(parent);
					addElement(newNode);
					return;
				}
			} else if (point.getX() > current.getX() && point.getY() > current.getY()) {
				current = current.getSE();
				if (current == null) {
					parent.setSE(newNode);
					newNode.quadrant.setHighQuadrantX(setHighQuadrantX((int) point.getX(), (int) point.getY()));
					newNode.quadrant.setHighQuadrantY(setHighQuadrantY((int) point.getY(), (int) point.getX()));
					newNode.quadrant.setLowQuadrantX(setLowQuadrantX((int) point.getX(), (int) point.getY()));
					newNode.quadrant.setLowQuadrantY(setLowQuadrantY((int) point.getY(), (int) point.getX()));
					newNode.setRegion(regions.SE);
					newNode.setParent(parent);
					addElement(newNode);
					return;
				}
			}

		}
	}

	/**
	 * This method finds out smallest rectangle that point in it.
	 * 
	 * @param x
	 *            X coordinate of query circle
	 * @param y
	 *            Y coordinate of query circle
	 * @param r
	 *            Radius of query circle
	 * @param root
	 *            root node
	 */
	private void findSmallestRect(int x, int y, int r, Node root) {
		if (root == null)
			return;// Exit if no node exists
		flag = true;

		if (x - r > root.quadrant.getLowQuadrantX() && x + r < root.getX() && y - r > root.quadrant.getLowQuadrantY()
				&& y + r < root.getY()) {
			if (root.getNW() != null) {
				root = root.getNW();
				findSmallestRect(x, y, r, root);
			}
			System.out.println("No point");
		} else if (x - r > root.getX() && x + r < root.quadrant.getHighQuadrantX()
				&& y - r > root.quadrant.getLowQuadrantY() && y + r < root.getY()) {
			if (root.getNE() != null) {
				root = root.getNE();
				findSmallestRect(x, y, r, root);
			}
			System.out.println("No point");
		} else if (x - r > root.quadrant.getLowQuadrantX() && x + r < root.getX() && y - r > root.getY()
				&& y + r < root.quadrant.getHighQuadrantY()) {
			if (root.getSW() != null) {
				root = root.getSW();
				findSmallestRect(x, y, r, root);
			}
			System.out.println("No point");
		} else if (x - r > root.getX() && x + r < root.quadrant.getHighQuadrantX() && y - r > root.getY()
				&& y + r < root.quadrant.getHighQuadrantY()) {
			if (root.getSE() != null) {
				root = root.getSE();
				findSmallestRect(x, y, r, root);
			}
			System.out.println("No point");
		}
		traverse(x, y, r, root);
		return;
	}

	public void traverse(int x, int y, int r, Node startNode) {
		if (isInCircle(x, y, r + 5, startNode)) {
			startNode.setColor(Color.BLUE);
		} else {
			startNode.setColor(Color.RED);
		}

		if (startNode.getNW() != null)
			traverse(x, y, r, startNode.getNW());
		if (startNode.getNE() != null)
			traverse(x, y, r, startNode.getNE());
		if (startNode.getSW() != null)
			traverse(x, y, r, startNode.getSW());
		if (startNode.getSE() != null)
			traverse(x, y, r, startNode.getSE());
	}

	/**
	 * This method checks that if point in circle.
	 * 
	 * @param x
	 *            x coordinate of circle
	 * @param y
	 *            y coordinate of circle
	 * @param r
	 *            radius
	 * @param node
	 *            point
	 * @return true if node in circle
	 */
	private boolean isInCircle(int x, int y, int r, Node node) {
		double dx = x - node.getX();
		double dy = y - node.getY();
		dx *= dx;
		dy *= dy;
		double distanceSquared = dx + dy;
		double radiusSquared = r * r;
		return distanceSquared <= radiusSquared;

	}

	/**
	 * clears quadtree
	 */
	private void resetQuadtree() {
		if (root != null) {
			root = null;
			nodeList.clear();
			thePanel.repaint();
			id = 0;
			System.gc();
		}
	}

	/**
	 * Inserts an element randomly
	 */
	private void insertRandomElement() {
		double x = Math.random() * 450.0;
		double y = Math.random() * 450.0;
		Point p = new Point((int) x, (int) y);
		insert(p);
		thePanel.repaint();
	}

	private int setLowQuadrantY(int i, int x) {
		int y = 0;
		for (Node e : nodeList) {
			if (i > e.getY() && e.getY() > y && e.quadrant.getLowQuadrantY() < i && e.quadrant.getHighQuadrantY() > i) {
				if (e.quadrant.getHighQuadrantX() > x && e.quadrant.getLowQuadrantX() < x)
					y = e.getY();
			}
		}
		return y;
	}

	/* Set smallest rectangle's corner coordinates */

	private int setLowQuadrantX(int i, int y) {
		int x = 0;
		for (Node e : nodeList) {
			if (i > e.getX() && e.getX() > x && e.quadrant.getLowQuadrantX() < i && e.quadrant.getHighQuadrantX() > i) {
				if (e.quadrant.getHighQuadrantY() > y && e.quadrant.getLowQuadrantY() < y)
					x = e.getX();
			}
		}
		return x;
	}

	private int setHighQuadrantY(int i, int x) {
		int y = 512;
		for (Node e : nodeList) {
			if (e.getY() > i && y > e.getY() && e.quadrant.getLowQuadrantY() < i && e.quadrant.getHighQuadrantY() > i) {
				if (e.quadrant.getHighQuadrantX() > x && e.quadrant.getLowQuadrantX() < x)
					y = e.getY();
			}
		}
		return y;
	}

	private int setHighQuadrantX(int i, int y) {
		int x = 512;
		for (Node e : nodeList) {
			if (e.getX() > i && x > e.getX() && e.quadrant.getLowQuadrantX() < i && e.quadrant.getHighQuadrantX() > i) {
				if (e.quadrant.getHighQuadrantY() > y && e.quadrant.getLowQuadrantY() < y)
					x = e.getX();
			}
		}
		return x;
	}

	/********************************************************************/

	/**
	 * 
	 * Adding elements to array list.
	 * 
	 * @param node
	 */
	public void addElement(Node node) {
		nodeList.add(node);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		for (Node n : nodeList)
			n.setColor(Color.RED);
		Point p = e.getPoint();
		if (isAddElementSelected) {
			insert(p);
		} else {
			circleXAxis = (int) p.getX();
			circleYAxis = (int) p.getY();
		}

		thePanel.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("reset") && root != null)
			resetQuadtree();

		if (e.getActionCommand().equals("addRandom"))
			insertRandomElement();

		if (e.getActionCommand().equals("addElement"))
			isAddElementSelected = true;

		if (e.getActionCommand().equals("createQuery"))
			isAddElementSelected = false;

		for (Node n : nodeList)
			n.setColor(Color.RED);

		thePanel.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == radius) {
			circleRadius = radius.getValue();
			textLabelOutput = "";
			thePanel.repaint();
		}

	}
}
