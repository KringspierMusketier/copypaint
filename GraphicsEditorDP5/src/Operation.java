public interface Operation {
	//interface for acceptees, tells us to accept objects implementing the OperationVisitor interface
	//and that the return type can be anything
	public <T> T accept(OperationVisitor<T> pov);
}
