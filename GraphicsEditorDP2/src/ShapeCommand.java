
//The Command for changing the selected ShapeType for drawing new Shapes
public class ShapeCommand implements Command {
	private Receiver r;
	private ShapeType type;
	private ShapeType oldType;
	
	public ShapeCommand(Receiver r, ShapeType type) {
		this.r = r;
		this.type = type;
		this.oldType = r.getShapeType();
	}
	
	@Override
	public void execute() {
		r.setShapeType(type);
	}
	
	@Override
	public void unexecute() {
		r.setShapeType(oldType);
	}

}
