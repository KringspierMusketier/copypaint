import java.util.ArrayList;

//The parent-class for ShapeLeaf/ShapeGroup
//All Shape objects can either be selected or set (in)visible
public abstract class Shape {
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

	abstract ArrayList<String> print();
}
