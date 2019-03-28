import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.*;

public class DrawingArea extends JComponent {
	
	private static final long serialVersionUID = 9044308264550141828L;

	public enum ShapeType {
		RECTANGLE, ELLIPSE, SELECT_RECT, EXPAND_RECT
	}
	
	public enum Tool {
		DRAW, SELECT
	}
	
	//sets the size of the expansion rectangle
	private static final int EXPAND_RECT_SIZE = 10;
	//sets the color of the expansion rectangle
	private static final Color EXPAND_RECT_COLOR = Color.red;
	//sets the fill color of the selection rectangle and selected shapes
	private static final Color SELECT_COLOR = new Color(63, 191, 191, 71);
	//defines the border stroke for selected shapes
	private static final Stroke DASHED_STROKE = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	
	private Image image;
	private Graphics2D g2;
	private int currentX, currentY, oldX, oldY, offsetX, offsetY;
	
	//The container for all drawn shapes on screen
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	//The shape that's displayed after selecting shapes to allow you to expand them
	private Shape expandRect = new Shape(-1, -1, EXPAND_RECT_SIZE, EXPAND_RECT_SIZE, ShapeType.EXPAND_RECT, EXPAND_RECT_COLOR); 
	
	//On startup, the rectangle shape and drawing tool are selected by default
	private ShapeType selectedShape = ShapeType.RECTANGLE;
	private Tool selectedTool = Tool.DRAW;
	
	//booleans for denoting whether the user is currently dragging or expanding objects
	private boolean dragging = false;
	private boolean expanding = false;
	
	public DrawingArea() {
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter( ) {
			
			//actions to take when the mouse button is pressed
			public void mousePressed(MouseEvent e) {
				if (selectedTool == Tool.DRAW) {
					//add a new shape to shapes based on the current mouse coordinates
					oldX = e.getX();
					oldY = e.getY();
					currentX = e.getX();
					currentY = e.getY();
					shapes.add(new Shape(oldX, oldY, (currentX - oldX), (currentY - oldY), selectedShape, g2.getColor()));
					repaint(); //function that needs to be called in order to refresh the drawing area
				} else if (selectedTool == Tool.SELECT) {
					oldX = e.getX();
					oldY = e.getY();
					currentX = e.getX();
					currentY = e.getY();
					dragging = false;
					expanding = false;
					
					int[] bounds = {oldX, oldY, 1, 1};
					
					/**
					 * Checks if the user has clicked on the expansion rectangle first. If not, it checks for whether the mouse click is
					 * inside any of the shapes that have been selected before. These checks happen separately because you cannot expand
					 * and drag a shape at the same time, and the check for whether the expansion box has been clicked takes priority.
					 */
					if (AABBcheck(bounds, expandRect.getCoords())) {
						expanding = true;
					} else {
						for (Shape s : shapes) {
							if (AABBcheck(bounds, s.getCoords()) && s.isSelected()) {
								dragging = true;
								break;
							}
						}
					}
					
					//If the user clicked outside all shapes, unselect all shapes and hide the expansion box.
					//Then, create a shape for the selection box
					if (!dragging && !expanding) {
						clearSelected();
						
						shapes.add(new Shape(oldX, oldY, (currentX - oldX), (currentY - oldY), ShapeType.SELECT_RECT, SELECT_COLOR));
					}
					repaint();
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if (selectedTool == Tool.SELECT) {
					if (!dragging && !expanding) {
						int[] selectBounds = shapes.get(shapes.size() - 1).getCoords();
						boolean one = false;
						
						//checks which shapes were inside the selection box, and selects them
						for (int i = 0; i < shapes.size() - 1; i++) {
							int[] coords = shapes.get(i).getCoords();
							if (AABBcheck(selectBounds, coords)) {
								shapes.get(i).setSelected(true);
								one = true;
							}
						}
						
						//if at least one shape has been found, the expansion box will be displayed near the bottom right of the
						//selection box
						if (one) {
								int[] c = shapes.get(shapes.size() - 1).getCoords();
								expandRect.setXY(new Point(c[0] + c[2], c[1] + c[2]));
								expandRect.setSelected(true);
						}
						shapes.remove(shapes.size() - 1);
					}
					
					//updates the old coordinates for all shapes
					for (Shape s: shapes) {
						s.setOldXY(s.getXY());
						s.setOldWH(s.getWH());
					}
					expandRect.setOldXY(expandRect.getXY());
					
					repaint();
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (selectedTool == Tool.DRAW) {
					currentX = e.getX();
					currentY = e.getY();
					
					//dynamically draws a shape on screen based on the mouse position
					//the values of the x/y and width/height are switched when the user is trying to move the mouse to the left of 
					//the original click position
					shapes.get(shapes.size() - 1).setX(Math.min(oldX, currentX));
					shapes.get(shapes.size() - 1).setW(Math.abs(currentX - oldX));
					shapes.get(shapes.size() - 1).setY(Math.min(oldY, currentY));
					shapes.get(shapes.size() - 1).setH(Math.abs(currentY - oldY));
					repaint();
				} else if (selectedTool == Tool.SELECT) {
					currentX = e.getX();
					currentY = e.getY();
					
					if (dragging) {
						offsetX = currentX - oldX;
						offsetY = currentY - oldY;
						
						//The selected dragged shapes have their position updated using the difference between the old click position
						//and your current mouse position as a measure. The original position of the shapes before the dragging started
						//is used as a reference instead of the newest position to prevent shapes from being dragged an exponentially
						//increasing distance
						for (Shape s : shapes) {
							if (s.isSelected()) {
								Point old = s.getOldXY();
								s.setX(old.x + offsetX);
								s.setY(old.y + offsetY);
							}
						}
						//the position of the expansion box is updated as well
						Point old = expandRect.getOldXY();
						expandRect.setX(old.x + offsetX);
						expandRect.setY(old.y + offsetY);
					} else if (expanding) {
						offsetX = currentX - oldX;
						offsetY = currentY - oldY;
						boolean x_limit = false;
						boolean y_limit = false;
						
						//updates the width/height of the selected shapes based on the dragged mouse distance
						//shapes have a minimal size of 2px and thus can't be shrunk beyond that
						for (Shape s : shapes) {
							if (s.isSelected()) {
								Point oldWH = s.getOldWH();
								Point newWH = new Point(oldWH.x + offsetX, oldWH.y + offsetY);
								if (newWH.x > 2) {
									s.setW(newWH.x);
								} else {
									x_limit = true;
								}
								
								if (newWH.y > 2) {
									s.setH(newWH.y);
								} else {
									y_limit = true;
								}
							}
						}
						Point old = expandRect.getOldXY();
						if (!x_limit) {
							expandRect.setX(old.x + offsetX);
						}
						if (!y_limit) {
							expandRect.setY(old.y + offsetY);
						}
						
					} else {
						shapes.get(shapes.size() - 1).setX(Math.min(oldX, currentX));
						shapes.get(shapes.size() - 1).setW(Math.abs(currentX - oldX));
						shapes.get(shapes.size() - 1).setY(Math.min(oldY, currentY));
						shapes.get(shapes.size() - 1).setH(Math.abs(currentY - oldY));
					}
					repaint();
				}
			}
			
		});
		
	}
		
	//this method is called each time repaint() is called
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			image = createImage(getSize().width, getSize().height);
			g2 = (Graphics2D) image.getGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		
		Color old = g2.getColor();
		Stroke oldStroke = g2.getStroke();
		
		g2.setPaint(Color.white);
		g2.fillRect(0,  0, getSize().width, getSize().height);
		
		//draws all shapes from shapes[] on screen based on the attributes per shape
		for (Shape s : shapes) {
			int[] coords = s.getCoords();
			ShapeType shape = s.getShapeType();
			Color color = s.getShapeColor();
			if (s.isSelected()) {
				//a dashed stroke is set for selected shapes
				g2.setStroke(DASHED_STROKE);
			}
			
			g2.setPaint(color);
			if (shape == ShapeType.RECTANGLE) {
				g2.drawRect(coords[0], coords[1], coords[2], coords[3]);
				if (s.isSelected()) {
					g2.setPaint(SELECT_COLOR);
					g2.fillRect(coords[0], coords[1], coords[2], coords[3]);
				}
			} else if (shape == ShapeType.ELLIPSE) {
				g2.drawOval(coords[0], coords[1], coords[2], coords[3]);
				if (s.isSelected()) {
					g2.setPaint(SELECT_COLOR);
					g2.fillOval(coords[0], coords[1], coords[2], coords[3]);
				}
			} else if (shape == ShapeType.SELECT_RECT || shape == ShapeType.EXPAND_RECT) {			
				g2.fillRect(coords[0], coords[1], coords[2], coords[3]);
			}
			
			g2.setStroke(oldStroke);
		}
		
		//if the expansion box is set to be visible, draw it too
		if (expandRect.isSelected()) {
			int[] coords = expandRect.getCoords();
			Color color = expandRect.getShapeColor();
			g2.setPaint(color);
			g2.fillRect(coords[0], coords[1], coords[2], coords[3]);
		}
		
		g2.setPaint(old);
		g.drawImage(image, 0, 0, null);
		
	}
	
	//removes all shapes and hides the expansion box
	public void clear() {
		shapes.clear();
		expandRect.setSelected(false);
		repaint();
	}
	
	//for drawing rectangles with the draw tool
	public void selectRectangle() {
		selectedShape = ShapeType.RECTANGLE;
	}
	
	//for drawing ellipses with the draw tool
	public void selectEllipse() {
		selectedShape = ShapeType.ELLIPSE;
	}
	
	//for selecting the draw tool, also unselects all shapes
	public void selectDraw() {
		selectedTool = Tool.DRAW;
		clearSelected();
		repaint();
	}
	
	//for selecting the select tool
	public void selectSelect() {
		selectedTool = Tool.SELECT;
		repaint();
	}
	
	//for giving shapes a red color
	public void selectRed() {
		g2.setPaint(Color.red);
	}
	
	//for giving shapes a black color
	public void selectBlack() {
		g2.setPaint(Color.black);
	}
	
	//checks whether two rectangles intersect
	private boolean AABBcheck(int[] a, int[] b) {
		if (a[0] < b[0] + b[2] &&
			a[0] + a[2] > b[0] &&
			a[1] < b[1] + b[3] &&
			a[1] + a[3] > b[1]) {
			return true;
		}
		return false;
	}
	
	//clears all shapes and hides the expansion box
	private void clearSelected() {
		for (Shape s : shapes) {
			s.setSelected(false);
		}
		expandRect.setSelected(false);
	}
}
