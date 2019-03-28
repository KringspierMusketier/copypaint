
import java.awt.Point;
import java.util.ArrayList;

//The Command for resizing Shapes 
//Essentially the same as MoveCommmand but with the WH values instead of the XY values
public class ResizeCommand implements Command {
	private Receiver r;
	private ArrayList<StagingShape> changedShapes = new ArrayList<StagingShape>();
	private ArrayList<Point> changedValues = new ArrayList<Point>();
	
	public ResizeCommand(Receiver r) {
		this.r = r;
		this.changedShapes.addAll(r.getStagingShapes());
		for (StagingShape s: changedShapes) {
			Shape original = r.getShapes().get(s.getOriginalIndex());
			Point old = original.getOldWH();
			
			changedValues.add(new Point(s.getWH().x - old.x, s.getWH().y - old.y));
		}
	}
	
	@Override
	public void execute() {
		for (int i = 0; i < changedShapes.size(); i++) {
			Shape original = r.getShapes().get(changedShapes.get(i).getOriginalIndex());
			Point old = original.getWH();
			
			original.setW(old.x + changedValues.get(i).x);
			original.setH(old.y + changedValues.get(i).y);
			original.setVisible(true);
		}
		r.getStagingShapes().clear();
	}
	
	@Override
	public void unexecute() {
		for (int i = 0; i < changedShapes.size(); i++) {
			Shape original = r.getShapes().get(changedShapes.get(i).getOriginalIndex());
			Point old = original.getWH();
			
			original.setW(old.x - changedValues.get(i).x);
			original.setH(old.y - changedValues.get(i).y);
		}
		r.clearSelected();
	}
}
