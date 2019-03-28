import java.awt.Color;

//StagingShapeLeaf is a child-class from ShapeLeaf, with the only addition that it holds the index of its given source ShapeLeaf in the Shapes list,
//for when the changes in StagingShapeLeaf need to be passed back to the original ShapeLeaf
public class StagingShapeLeaf extends ShapeLeaf implements StagingShape{
	
	private int originalIndex;
	
	public StagingShapeLeaf(ShapeLeaf s, int originalIndex) {
		super(s.getOldXY().x, s.getOldXY().y, s.getOldWH().x, s.getOldWH().y, s.getShapeType(), s.getShapeColor());
		this.originalIndex = originalIndex;
	}
	
	public int getOriginalIndex() {
		return originalIndex;
	}
}
