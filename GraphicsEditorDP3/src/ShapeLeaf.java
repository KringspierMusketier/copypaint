import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

//The extension class of Shape which contains the attributes necessary to draw the Shape on screen
public class ShapeLeaf extends Shape {
	
	private int[] coords = new int[4];
	private ShapeType shapeType;
	private NamedColor shapeColor;
	private Point oldXY;
	private Point oldWH;
	
	public ShapeLeaf(int x, int y, int w, int h, ShapeType shapeType, NamedColor shapeColor) {
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

	public ShapeType getShapeType() {
		return shapeType;
	}

	public void setShapeType(ShapeType shapeType) {
		this.shapeType = shapeType;
	}

	public NamedColor getShapeColor() {
		return shapeColor;
	}

	public void setShapeColor(NamedColor shapeColor) {
		this.shapeColor = shapeColor;
	}
	
	//Prints all the attribute information of this ShapeLeaf into a one-item String list
	@Override
	public ArrayList<String> print() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(shapeType.name().toLowerCase() + " " + shapeColor.getColorName() + " " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + "\n");
		return result;
	}
}