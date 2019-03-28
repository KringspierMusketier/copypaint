import java.awt.Color;
import java.awt.Point;

//The model class for each drawn shape
public class Shape {
	
	private int[] coords = new int[4];
	private DrawingArea.ShapeType shapeType;
	private Color shapeColor;
	private boolean selected = false;
	private Point oldXY;
	private Point oldWH;
	
	public Shape(int x, int y, int w, int h, DrawingArea.ShapeType shapeType, Color shapeColor) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = w;
		coords[3] = h;
		oldXY = new Point(x, y);
		oldWH = new Point(w, h);
		this.shapeType = shapeType;
		this.shapeColor = shapeColor;
	}
	
	public int[] getCoords() {
		return coords;
	}

	public void setX(int x) {
		coords[0] = x;
	}


	public void setW(int w) {
		coords[2] = w;
	}


	public void setY(int y) {
		coords[1] = y;
	}

	public void setH(int h) {
		coords[3] = h;
	}
	
	public Point getXY() {
		return new Point(coords[0], coords[1]);
	}
	
	public Point getWH() {
		return new Point(coords[2], coords[3]);
	}
	
	public void setXY(Point xy) {
		coords[0] = xy.x;
		coords[1] = xy.y;
	}
	
	public void setWH(Point wh) {
		coords[2] = wh.x;
		coords[3] = wh.y;
	}
	
	public Point getOldXY() {
		return oldXY;
	}
	
	public void setOldXY(Point xy) {
		oldXY = xy;
	}
	
	public Point getOldWH() {
		return oldWH;
	}
	
	public void setOldWH(Point wh) {
		oldWH = wh;
	}

	public DrawingArea.ShapeType getShapeType() {
		return shapeType;
	}

	public void setShapeType(DrawingArea.ShapeType shapeType) {
		this.shapeType = shapeType;
	}

	public Color getShapeColor() {
		return shapeColor;
	}

	public void setShapeColor(Color shapeColor) {
		this.shapeColor = shapeColor;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
