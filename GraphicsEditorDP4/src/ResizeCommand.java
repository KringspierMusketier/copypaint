
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
			Shape f = r.getShapes().get(s.getOriginalIndex());
			f.accept(new ShapeResizeVisitor(changedValues.get(i)));
		}
		r.getStagingShapes().clear();
	}
	
	@Override
	public void unexecute() {
		for (int i = 0; i < changedShapes.size(); i++) {
			StagingShape s = (StagingShape) changedShapes.get(i);
			Shape f = r.getShapes().get(s.getOriginalIndex());
			Point newSize = new Point(changedValues.get(i));
			newSize.x *= -1;
			newSize.y *= -1;
			f.accept(new ShapeResizeVisitor(newSize));
		}
		r.clearSelected();
	}
}
