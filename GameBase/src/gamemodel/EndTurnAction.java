package gamemodel;

public final class EndTurnAction implements GameAction {

	@Override
	public boolean isValid(Board b) {
		return true;
	}

	@Override
	public void doAction(Board b) {
	}
	
}
