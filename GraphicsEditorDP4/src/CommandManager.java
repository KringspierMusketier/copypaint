
import java.util.Stack;

//The handler class for Commands. All Commands are called through CommandManager.
//Executed commands are stored on the undo stack, when an undo is requested, unexecute() will be called from that Command
//and it is moved to the redo stack, where in case of a redo the execute() can be called again.
//If a new Command is added, then the redo stack is cleared to avoid redoing Commands from unfamiliar states
public class CommandManager {
	private Stack<Command> undo = new Stack<Command>();
	private Stack<Command> redo = new Stack<Command>();
	
	public void execute(Command cmd) {
		undo.push(cmd);
		redo.clear();
		cmd.execute();
	}
	
	public void undo() {
		if (undo.size() != 0) {
			Command cmd = undo.pop();
			redo.push(cmd);
			cmd.unexecute();
			
		}
	}
	
	public void redo() {
		if (redo.size() != 0) {
			Command cmd = redo.pop();
			undo.push(cmd);
			cmd.execute();
			
		}
	}
	
	public void reset() {
		undo.clear();
		redo.clear();
	}
}
