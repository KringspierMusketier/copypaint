
//The Command for selecting Shapes
public class SelectCommand implements Command {
	private Receiver r;
	private int[] selectBounds;
	
	public SelectCommand(Receiver r) {
		this.r = r;
		this.selectBounds = r.getSelectRect().getCoords();
	}
	
	@Override
	public void execute() {
		r.checkSelected(selectBounds);
	}
	
	@Override
	public void unexecute() {
		r.clearSelected();
	}
}
