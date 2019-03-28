
//The Command for drawing new Shapes
public class DrawCommand implements Command {
	private Shape shape;
	private Receiver r;
	
	public DrawCommand(Shape shape, Receiver r) {
		this.shape = shape;
		this.r = r;
	}
	
	@Override
	public void execute() {
		r.getShapes().add(shape);
		r.getStagingShapes().clear();
	}
	
	@Override
	public void unexecute() {
		r.getShapes().remove(shape);
	}
}
