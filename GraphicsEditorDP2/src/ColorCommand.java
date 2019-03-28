import java.awt.Color;
import java.awt.Graphics2D;

//The Command for setting the currently selected shape color for drawing
public class ColorCommand implements Command {
	private Receiver r;
	private NamedColor color;
	private NamedColor oldColor;
	
	public ColorCommand(Receiver r, NamedColor color) {
		this.r = r;
		this.color = color;
		this.oldColor = r.getSelectedColor();
	}
	
	@Override 
	public void execute() {
		r.setNamedColor(color);
	}
	
	@Override
	public void unexecute() {
		r.setNamedColor(oldColor);
	}
}
