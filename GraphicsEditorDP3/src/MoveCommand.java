
import java.awt.Point;
import java.util.ArrayList;

//The command for moving existing Shapes
//For each selected Shape a StagingShape is made which also holds an index for the Shape it's made from in the Shapes list
//Then the changes in position from the StagingShape after it is dragged are carried over to its source Shape
public class MoveCommand implements Command {
	private Receiver r;
	private ArrayList<Shape> changedShapes = new ArrayList<Shape>();
	private ArrayList<Point> changedValues = new ArrayList<Point>();
	
	public MoveCommand(Receiver r) {
		this.r = r;
		this.changedShapes.addAll(r.getStagingShapes());
		for (Shape s: changedShapes) {
			Shape original = r.getShapes().get(((StagingShape)s).getOriginalIndex());
			if (original instanceof ShapeLeaf) {
				Point oldPos = ((ShapeLeaf)original).getOldXY();
				Point newPos = ((ShapeLeaf)s).getXY();
				changedValues.add(new Point(newPos.x - oldPos.x, newPos.y - oldPos.y));
			} else if (original instanceof ShapeGroup) {
				ShapeLeaf b = ((ShapeGroup)original).getFullBranch().get(0);
				ShapeLeaf c = ((ShapeGroup)s).getFullBranch().get(0);
				Point oldPos = b.getOldXY();
				Point newPos = c.getXY();
				changedValues.add(new Point(newPos.x - oldPos.x, newPos.y - oldPos.y));
			}
		}
	}
	
	@Override
	public void execute() {
		for (int i = 0; i < changedShapes.size(); i++) {
			StagingShape s = (StagingShape) changedShapes.get(i);
			if (s instanceof StagingShapeLeaf) {
				ShapeLeaf original = (ShapeLeaf) r.getShapes().get(s.getOriginalIndex());
				Point oldPos = original.getXY();
				Point newPos = changedValues.get(i);
				
				original.setX(oldPos.x + newPos.x);
				original.setY(oldPos.y + newPos.y);
				original.setVisible(true);
			} else if (s instanceof StagingShapeGroup) {
				ShapeGroup original = (ShapeGroup) r.getShapes().get(s.getOriginalIndex());
				original.appendXY(changedValues.get(i));
				original.setVisible(true);
			}
		}
		r.getStagingShapes().clear();
	}
	
	@Override
	public void unexecute() {
		for (int i = 0; i < changedShapes.size(); i++) {
			StagingShape s = (StagingShape) changedShapes.get(i);
			if (s instanceof StagingShapeLeaf) {
				ShapeLeaf original = (ShapeLeaf) r.getShapes().get(s.getOriginalIndex());
				Point oldPos = original.getXY();
				Point newPos = changedValues.get(i);
				
				original.setX(oldPos.x - newPos.x);
				original.setY(oldPos.y - newPos.y);
				original.setVisible(false);
			} else if (s instanceof StagingShapeGroup) {
				ShapeGroup original = (ShapeGroup) r.getShapes().get(s.getOriginalIndex());
				Point newPos = changedValues.get(i);
				original.appendXY(new Point(newPos.x * -1, newPos.y * -1));
			}
		}
		r.clearSelected();
	}
}
