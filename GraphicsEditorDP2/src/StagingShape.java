import java.awt.Color;

//StagingShape is a child-class from Shape, with the only addition that it holds the index of its given source Shape in the Shapes list,
//for when the changes in StagingShape need to be passed back to the original Shape
public class StagingShape extends Shape {
	
	private int originalIndex;
	
	public StagingShape(Shape s, int originalIndex) {
		super(s.getOldXY().x, s.getOldXY().y, s.getOldWH().x, s.getOldWH().y, s.getShapeType(), s.getShapeColor());
		this.originalIndex = originalIndex;
	}
	
	public int getOriginalIndex() {
		return originalIndex;
	}
}
