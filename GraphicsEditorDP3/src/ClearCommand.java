
import java.util.ArrayList;

//The Command for resetting the Receiver to the default state, the Shape-arrays are stored in case when this Command needs to be undone
public class ClearCommand implements Command {
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<Shape> stagingShapes = new ArrayList<Shape>();
	private boolean expRectVisible;
	private Receiver r;
	
	public ClearCommand(Receiver r) {
		this.r = r;
		this.shapes.addAll(r.getShapes());
		this.stagingShapes.addAll(r.getStagingShapes());
		this.expRectVisible = r.getExpandRect().isVisible();
	}
	
	@Override
	public void execute() {
		r.getShapes().clear();
		r.getStagingShapes().clear();
		r.getExpandRect().setVisible(false);
	}
	
	@Override
	public void unexecute() {
		r.getShapes().addAll(shapes);
		r.getStagingShapes().addAll(stagingShapes);
		r.getExpandRect().setVisible(expRectVisible);
	}
}