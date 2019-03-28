import java.awt.Point;
import java.util.ArrayList;

public class ShapeGroup extends Shape {
	
	protected ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	public ArrayList<Shape> getShapes() {
		return shapes; 
	}
	
	//return all ShapeLeafs and return the contents of all ShapeGroups
	public ArrayList<ShapeLeaf> getFullBranch() {
		ArrayList<ShapeLeaf> result = new ArrayList<ShapeLeaf>();
		for (Shape s : shapes) {
			if (s instanceof ShapeLeaf) {
				result.add((ShapeLeaf)s);
			} else if (s instanceof ShapeGroup) {
				ArrayList<ShapeLeaf> answer = ((ShapeGroup) s).getFullBranch();
				result.addAll(answer);
			}
		}
		return result;
	}
	
	//Apply a position change to all child ShapeLeafs
	public void appendXY(Point xy) {
		ArrayList<ShapeLeaf> branches = getFullBranch();
		for (ShapeLeaf s : branches) {
			Point old = s.getOldXY();
			s.setX(old.x + xy.x);
			s.setY(old.y + xy.y);
		}
	}
	
	//Apply a size change to all child ShapeLeafs
	public void appendWH(Point wh) {
		ArrayList<ShapeLeaf> branches = getFullBranch();
		for (ShapeLeaf s : branches) {
			Point old = s.getOldWH();
			s.setW(old.x + wh.x);
			s.setH(old.y + wh.y);
		}
	}
	
	public void add(Shape s) {
		shapes.add(s);
	}
	
	public void addGroup(ArrayList<Shape> s) {
		shapes.addAll(s);
	}
	
	//Check all child Groups and Leafs as selected/unselected
	@Override
	public void setSelected(boolean b) {
		selected = b;
		for (Shape s : shapes) {
			s.setSelected(b);
		}
	}
	
	//Check all child Groups and Leafs as visible/invisible
	@Override
	public void setVisible(boolean b) {
		visible = b;
		for (Shape s : shapes) {
			s.setVisible(b);
		}
	}

	//The accept method for the visitor
	@Override
	public <T> T accept(OperationVisitor<T> pov) {
		return pov.visit(this);
	}
}
