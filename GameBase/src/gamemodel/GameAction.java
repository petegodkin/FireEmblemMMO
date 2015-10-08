package gamemodel;

public interface GameAction {
	/**
	 * Called before doing any action to make sure it's a valid move
	 */
	boolean isValid(Board b);
	/**
	 * Called to do the action... obviously
	 */
	void doAction(Board b); 
}
