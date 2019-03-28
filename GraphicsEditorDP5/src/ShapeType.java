
//The enum for all available ShapeTypes
public enum ShapeType {
	RECTANGLE(DrawRectangle.getInstance()), ELLIPSE(DrawEllipse.getInstance());
	
	private Strategy str;
	
	private ShapeType(Strategy str) {
		this.str = str;
	}
	
	public Strategy getStrategy() {
		return str;
	}
}
