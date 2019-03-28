public interface OperationVisitor<T> {
	//interface for the Visitor object, tells us that in each Visitor that the operation logic
	//needs to be defined for both ShapeLeafs and ShapeGroups
	public T visit(ShapeLeaf leaf);
	public T visit(ShapeGroup group);
}
