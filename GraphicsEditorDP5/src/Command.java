
//The interface for Commands, each Command can be executed to perform something and 'unexecuted' do undo aforementioned action
public interface Command {
	void execute();
	void unexecute();
}
