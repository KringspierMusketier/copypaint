
import java.awt.Point;
import java.util.ArrayList;

//The Command for resizing Shapes  
//Essentially the same as MoveCommmand but with the WH values instead of the XY values
public class ResizeCommand implements Command {
	private Receiver r;
	private ArrayList<Shape> changedShapes = new ArrayList<Shape>();
	private ArrayList<Point> changedValues = new ArrayList<Point>();
	
	public ResizeCommand(Receiver r) {
		this.r = r;
		this.changedShapes.addAll(r.getStagingShapes());
		for (Shape s: changedShapes) {
			Shape original = r.getShapes().get(((StagingShape)s).getOriginalIndex());
			if (original instanceof ShapeLeaf) {
				Point oldPos = ((ShapeLeaf)original).getOldWH();
				Point newPos = ((ShapeLeaf)s).getWH();
				changedValues.add(new Point(newPos.x - oldPos.x, newPos.y - oldPos.y));
			} else if (original instanceof ShapeGroup) {
				ShapeLeaf b = ((ShapeGroup)original).getFullBranch().get(0);
				ShapeLeaf c = ((ShapeGroup)s).getFullBranch().get(0);
				Point oldPos = b.getOldWH();
				Point newPos = c.getWH();
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
				Point oldPos = original.getWH();
				Point newPos = changedValues.get(i);
				
				original.setW(oldPos.x + newPos.x);
				original.setH(oldPos.y + newPos.y);
				original.setVisible(true);
			} else if (s instanceof StagingShapeGroup) {
				ShapeGroup original = (ShapeGroup) r.getShapes().get(s.getOriginalIndex());
				original.appendWH(changedValues.get(i));
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
				Point oldPos = original.getWH();
				Point newPos = changedValues.get(i);
				
				original.setW(oldPos.x - newPos.x);
				original.setH(oldPos.y - newPos.y);
				original.setVisible(false);
			} else if (s instanceof StagingShapeGroup) {
				ShapeGroup original = (ShapeGroup) r.getShapes().get(s.getOriginalIndex());
				Point newPos = changedValues.get(i);
				original.appendWH(new Point(newPos.x * -1, newPos.y * -1));
			}
		}
		r.clearSelected();
	}
}
