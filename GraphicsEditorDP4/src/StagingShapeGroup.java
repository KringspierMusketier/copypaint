
//The Staging version of ShapeGroup
public class StagingShapeGroup extends ShapeGroup implements StagingShape {
	private int originalIndex;
	
	public StagingShapeGroup(ShapeGroup s, int originalIndex) {
		super.addGroup(s.getShapes());
		super.setSelected(s.isSelected());
		super.setVisible(s.isVisible());
		this.originalIndex = originalIndex;
	}

	@Override
	public int getOriginalIndex() {
		return originalIndex;
	}
}
