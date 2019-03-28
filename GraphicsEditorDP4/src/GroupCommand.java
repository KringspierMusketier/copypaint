import java.util.ArrayList;

//Pack all selected ShapeLeafs into a ShapeGroup, remove the ShapeLeafs from Shapes,
//and add the ShapeGroup in their stead
public class GroupCommand implements Command {
	
	private Receiver r;
	private ShapeGroup newGroup = new ShapeGroup();
	
	public GroupCommand(Receiver r) {
		this.r = r;
		ArrayList<Shape> shapes = r.getShapes();
		for (Shape s : shapes) {
			if (s.isSelected()) {
				newGroup.add(s);
			}
		}
	}
	
	@Override
	public void execute() {

		r.getShapes().removeAll(newGroup.getShapes());
		r.getShapes().add(newGroup);
		r.clearSelected();
		
	}
	
	@Override
	public void unexecute() {
		r.getShapes().remove(newGroup);
		r.getShapes().addAll(newGroup.getShapes());
	}
}
