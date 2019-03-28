import java.awt.Graphics2D;

public interface Strategy {
	void draw(ShapeLeaf leaf, Graphics2D g2);
	String toString();
}
