
import java.awt.Point;
import java.util.ArrayList;

//The command for moving existing Shapes
//For each selected Shape a StagingShape is made which also holds an index for the Shape it's made from in the Shapes list
//Then the changes in position from the StagingShape after it is dragged are carried over to its source Shape
public class MoveCommand implements Command {
	private Receiver r;
	private ArrayList<StagingShape> changedShapes = new ArrayList<StagingShape>();
	private ArrayList<Point> changedValues = new ArrayList<Point>();
	
	public MoveCommand(Receiver r) {
		this.r = r;
		this.changedShapes.addAll(r.getStagingShapes());
		for (StagingShape s: changedShapes) {
			Shape original = r.getShapes().get(s.getOriginalIndex());
			Point old = original.getOldXY();
			
			changedValues.add(new Point(s.getXY().x - old.x, s.getXY().y - old.y));
		}
	}
	
	@Override
	public void execute() {
		for (int i = 0; i < changedShapes.size(); i++) {
			Shape original = r.getShapes().get(changedShapes.get(i).getOriginalIndex());
			Point old = original.getXY();
			
			original.setX(old.x + changedValues.get(i).x);
			original.setY(old.y + changedValues.get(i).y);
			original.setVisible(true);
		}
		r.getStagingShapes().clear();
	}
	
	@Override
	public void unexecute() {
		for (int i = 0; i < changedShapes.size(); i++) {
			Shape original = r.getShapes().get(changedShapes.get(i).getOriginalIndex());
			Point old = original.getXY();
			
			original.setX(old.x - changedValues.get(i).x);
			original.setY(old.y - changedValues.get(i).y);
			original.setVisible(false);
		}
		r.clearSelected();
	}
}
