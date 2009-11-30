package dhost.state;

public interface GameState
{
	public Cell getCell(int cellID);

	/**
	 * Process a CellChange request
	 */
	public void update(CellChange change);

}
