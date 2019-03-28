import java.awt.Color;

//Enums for all paintable colors, each NamedColor holds a reference to an AWT-Color for passing the value to Graphics2D, and a string representation
//of the color name when printing shapes to a save file
public enum NamedColor {
	BLACK(Color.black, "black"),
	RED(Color.red, "red"),
	SELECT(new Color(63, 191, 191, 71), "select");
	
	private Color awtColor;
	private String name;
	
	private NamedColor(Color awtColor, String name) {
		this.awtColor = awtColor;
		this.name = name;
	}
	
	public Color getAwtColor() {
		return awtColor;
	}
	
	public String getColorName() {
		return name;
	}
}
