import java.awt.Graphics2D;

public class DrawEllipse implements Strategy {

	private static DrawEllipse instance = new DrawEllipse();
	private static final String shapeName = "ellipse";
	
	private DrawEllipse() { }
	
	public static DrawEllipse getInstance() {
		return instance;
	}
	
	@Override
	public String toString() {
		return shapeName;
	}
	
	@Override
	public void draw(ShapeLeaf leaf, Graphics2D g2) {
		if (leaf.isVisible()) {
			int[] coords = leaf.getCoords();
			
			g2.setPaint(leaf.getShapeColor().getAwtColor());
			g2.drawOval(coords[0], coords[1], coords[2], coords[3]);
			if (leaf.isSelected()) {
				g2.setPaint(NamedColor.SELECT.getAwtColor());
				g2.fillOval(coords[0], coords[1], coords[2], coords[3]);
			}
		}
	}
	
}
