import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

//The extension class of Shape which contains the attributes necessary to draw the Shape on screen
public class ShapeLeaf extends Shape {
	
	private int[] coords = new int[4];
	private Strategy strategy;
	private NamedColor shapeColor;
	private Point oldXY;
	private Point oldWH;
	
	public ShapeLeaf(int x, int y, int w, int h, Strategy strategy, NamedColor shapeColor) {
		coords[0] = x;
		coords[1] = y;
		coords[2] = w;
		coords[3] = h;
		oldXY = new Point(x, y);
		oldWH = new Point(w, h);
		this.strategy = strategy;
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

	public void draw(Graphics2D drawable) {
		strategy.draw(this, drawable);
	}
	
	public Strategy getStrategy() {
		return strategy;
	}
	
	public String getShapeName() {
		return strategy.toString();
	}

	public NamedColor getShapeColor() {
		return shapeColor;
	}

	public void setShapeColor(NamedColor shapeColor) {
		this.shapeColor = shapeColor;
	}

	//The accept method for the Visitor
	@Override
	public <T> T accept(OperationVisitor<T> pov) {
		return pov.visit(this);
	}
}
