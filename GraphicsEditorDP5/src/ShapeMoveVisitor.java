import java.awt.Point;

//Contains logic of Move operations for both ShapeLeaf and ShapeGroup
//requires the new position to move to as an input
public class ShapeMoveVisitor implements OperationVisitor<Void>{

	private Point newPos;
	
	public ShapeMoveVisitor(Point newPos) {
		this.newPos=newPos;
	}
	
	@Override
	public Void visit(ShapeLeaf leaf) {
		Point oldPos = leaf.getXY();
		
		leaf.setX(oldPos.x + newPos.x);
		leaf.setY(oldPos.y + newPos.y);
		leaf.setVisible(true);
		return null;
	}

	@Override
	public Void visit(ShapeGroup group) {
		group.appendXY(newPos);
		group.setVisible(true);
		return null;
	}

}
