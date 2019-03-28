import java.awt.Point;

//Contains logic of resize operations for both ShapeLeaf and ShapeGroup
//Requires a Point that contains the new width and height as input
public class ShapeResizeVisitor implements OperationVisitor<Void>{

	private Point newSize;
	
	public ShapeResizeVisitor(Point newSize) {
		this.newSize = newSize;
	}
	
	@Override
	public Void visit(ShapeLeaf leaf) {
		Point oldSize = leaf.getWH();
		
		leaf.setW(oldSize.x + newSize.x);
		leaf.setH(oldSize.y + newSize.y);
		leaf.setVisible(true);
		return null;
	}

	@Override
	public Void visit(ShapeGroup group) {
		group.appendWH(newSize);
		group.setVisible(true);
		return null;
	}

}
