import java.util.ArrayList;

//Get all the ShapeLeafs from the selected ShapeGroups, remove the ShapeGroups from 
//the Shapes list, and add all the ShapeLeaves back into Shapes
public class UngroupCommand implements Command {
	private Receiver r;
	private ArrayList<ShapeGroup> groups = new ArrayList<ShapeGroup>();
	
	public UngroupCommand(Receiver r) {
		this.r = r;
		ArrayList<Shape> shapes = r.getShapes();
		for (Shape s : shapes) {
			if (s.isSelected() && s instanceof ShapeGroup) {
				groups.add((ShapeGroup)s);
			}
		}
	}
	
	@Override
	public void execute() {
		for (ShapeGroup s : groups) {
			r.getShapes().remove(s);
			r.getShapes().addAll(s.getFullBranch());
		}
		r.clearSelected();
	}
	
	@Override
	public void unexecute() {
		for (ShapeGroup s : groups) {
			r.getShapes().removeAll(s.getFullBranch());
			r.getShapes().add(s);
		}
	}
}
