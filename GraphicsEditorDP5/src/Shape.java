//The parent-class for ShapeLeaf/ShapeGroup
//All Shape objects can either be selected or set (in)visible

//Also implements the Operation interface, the required functions are defined in the classes
//inheriting Shape
public abstract class Shape implements Operation {
	protected boolean visible = true;
	protected boolean selected = false;
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
