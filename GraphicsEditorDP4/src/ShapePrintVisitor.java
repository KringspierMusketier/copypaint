import java.util.ArrayList;

//Contains logic for printing attribute data for both ShapeLeaf and ShapeGroup
public class ShapePrintVisitor implements OperationVisitor<ArrayList<String>> {

	//Prints all the attribute information of this ShapeLeaf into a one-item String list
	@Override
	public ArrayList<String> visit(ShapeLeaf leaf) {
		ArrayList<String> result = new ArrayList<String>();
		int[] coords = leaf.getCoords();
		result.add(leaf.getShapeType().name().toLowerCase() + " " + leaf.getShapeColor().getColorName() + " " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + "\n");
		return result;
		
	}

	@Override
	public ArrayList<String> visit(ShapeGroup group) {
		ArrayList<Shape> shapes = group.getShapes();
		ArrayList<String> result = new ArrayList<String>();
		//Each group should be prefaced in the save file with "group X\n"
		String intro = "group " + shapes.size() + "\n";
		result.add(intro);
		
		for (Shape shape : shapes) {
			//Call the print function for each child Shape, preface each resulting line with a \t,
			//and add this list to the parent result list
			ArrayList<String> receiver = shape.accept(new ShapePrintVisitor());
			for (int i = 0; i < receiver.size(); i++) {
				result.add("\t" + receiver.get(i));
			}
		}
		return result;
	}

}
