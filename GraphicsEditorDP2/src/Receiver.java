import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;

//The model class which holds all data for DrawingArea
public class Receiver {
	//sets the size of the expansion rectangle
	private static final int EXPAND_RECT_SIZE = 10;
	//sets the color of the expansion rectangle
	private static final NamedColor EXPAND_RECT_COLOR = NamedColor.RED;
	//sets the fill color of the selection rectangle and selected shapes
	private static final NamedColor SELECT_COLOR = NamedColor.SELECT;
	
	//On startup, the rectangle shape, the drawing tool and the color black are selected by default
	private ShapeType selectedShape = ShapeType.RECTANGLE;
	private NamedColor selectedColor = NamedColor.BLACK;
	private Tool selectedTool = Tool.DRAW;
	
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<StagingShape> stagingShapes = new ArrayList<StagingShape>();
	private Shape selectRect = new Shape(-1, -1, 1, 1, ShapeType.SELECT_RECT, SELECT_COLOR);
	private Shape expandRect = new Shape(-1, -1, EXPAND_RECT_SIZE, EXPAND_RECT_SIZE, ShapeType.EXPAND_RECT, EXPAND_RECT_COLOR); 
	
	//SelectRect/ExpandRect are hidden on start-up
	public Receiver() {
		selectRect.setVisible(false);
		expandRect.setVisible(false);
	}
	
	public ArrayList<Shape> getShapes() {
		return shapes;
	}
	
	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
	}
	
	
	public ArrayList<StagingShape> getStagingShapes() {
		return stagingShapes;
	}
	
	public void setStagingShapes(ArrayList<Shape> shapes) {
		stagingShapes.clear();
		for (Shape s: shapes) {
			stagingShapes.add(new StagingShape(s, shapes.indexOf(s)));
		}
	}
	
	public Shape getExpandRect() {
		return expandRect;
	}
	
	public Shape getSelectRect() {
		return selectRect;
	}
	
	public void checkSelected(int[] selectBounds) {
		boolean one = false;

		//checks which shapes were inside the selection box, and selects them
		for (Shape s: shapes) {
			int[] coords = s.getCoords();
			if (AABBcheck(selectBounds, coords)) {
				s.setSelected(true);
				one = true;
			}
		}
		
		for (StagingShape s: stagingShapes) {
			int[] coords = s.getCoords();
			if (AABBcheck(selectBounds, coords)) {
				s.setSelected(true);
				one = true;
			}
		}
		
		//if at least one shape has been found, the expansion box will be displayed near the bottom right of the
		//selection box
		if (one) {
			int[] c = selectRect.getCoords();
			expandRect.setXY(new Point(c[0] + c[2], c[1] + c[2]));
			expandRect.setVisible(true);
		} else {
			clearSelected();
		}
		
		//reset the SelectRect
		selectRect.setVisible(false);
		selectRect.setXY(new Point(-1,-1));
		selectRect.setWH(new Point(1, 1));
	}
	
	public Tool getTool() {
		return selectedTool;
	}
	
	public ShapeType getShapeType() {
		return selectedShape;
	}
	
	public NamedColor getSelectedColor() {
		return selectedColor;
	}
	
	public void setTool(Tool tool) {
		this.selectedTool = tool;
	}
	
	public void setShapeType(ShapeType type) {
		this.selectedShape = type;
	}
	
	public void setNamedColor(NamedColor color) {
		this.selectedColor = color;
	}
	
	//unselects all shapes and hides the expansion box
	public void clearSelected() {
		for (Shape s : shapes) {
			s.setSelected(false);
			s.setVisible(true);
		}
		stagingShapes.clear();
		expandRect.setVisible(false);
		selectRect.setVisible(false);
		selectRect.setXY(new Point(-1,-1));
		selectRect.setWH(new Point(1,1));
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
	
}

