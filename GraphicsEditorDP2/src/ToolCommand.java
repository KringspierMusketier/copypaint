
//The Command for selecting a new Tool to use
public class ToolCommand implements Command {
	private Receiver r;
	private Tool tool;
	private Tool oldTool;
	
	public ToolCommand(Receiver r, Tool tool) {
		this.r = r;
		this.tool = tool;
		this.oldTool = r.getTool();
	}
	
	@Override
	public void execute() {
		r.setTool(tool);
	}
	
	@Override
	public void unexecute() {
		r.setTool(oldTool);
	}
}
