import java.awt.Graphics2D;

public class DrawRectangle implements Strategy {

	private static DrawRectangle instance = new DrawRectangle();
	private static final String shapeName = "rectangle";
	
	private DrawRectangle() { }
	
	public static DrawRectangle getInstance() {
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
			g2.drawRect(coords[0], coords[1], coords[2], coords[3]);
			if (leaf.isSelected()) {
				g2.setPaint(NamedColor.SELECT.getAwtColor());
				g2.fillRect(coords[0], coords[1], coords[2], coords[3]);
			}
		}
		
	}
}
