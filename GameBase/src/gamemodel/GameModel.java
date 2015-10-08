package gamemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import gameserver.Player;

public abstract class GameModel {
	protected Board board;
	protected List<Player> players;
	
	public GameModel(int x, int y) {
		board = new Board(x, y);
		players = new ArrayList<>();
	}
	
	public void addPlayers(Player...players) {
		this.players.addAll(Arrays.asList(players));
	}
	
	public abstract void start();
	
	protected List<Player> getWinners() {
		return players.stream().filter(p -> p.hasWon(this)).collect(Collectors.toList());
	}
	
	protected boolean isGameover() {
		return !getWinners().isEmpty();
	}
}
