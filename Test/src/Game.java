import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Vector;

public class Game {
	private static final int SENTSTRING = 0;
	private static final int IQUIT = 0;
	private static final int IWON = 0;
	private static final int ITIED = 0;
	private static final Connect4Player player1 = null;
	private static final Connect4Player player2 = null;
	private static final String p1Queue = null;
	private static final String p2Queue = null;
	private static final int ERROR = 0;

	public void playGame(Connect4Player me) {
		String instr;
		boolean playgame = true;
		boolean theirturn = false;
		try {
		if (me == player2) {
		theirturn = true;
		}
		else if (me != player1) {
		System.out.println("Illegal call to playGame!");
		return;
		}
		while (playgame) {
		String sentString = null;
		if (!theirturn) {
		me.send("YOURTURN");
		instr = me.receive();
		instr = instr.toUpperCase();
		instr = instr.trim();
		if (instr.startsWith("IQUIT")) {
		sendStatus(me, IQUIT);
		playgame = false;
		}
		else if (instr.startsWith("IWON")) {
		sentString = me.receive();
		sentString = sentString.toUpperCase();
		sentString = sentString.trim();
		sendStatus(me, IWON);
		sendStatus(me, SENTSTRING);
		playgame = false;
		}
		else if (instr.startsWith("ITIED")) {
		sentString = me.receive();
		sentString = sentString.toUpperCase();
		sentString = sentString.trim();
		sendStatus(me, ITIED);
		sendStatus(me, SENTSTRING);
		}
		else {
		sentString = instr;
		sendStatus(me, SENTSTRING);
		}
		}
		else {
		theirturn = false;
		}
		if (playgame) {
		me.send("THEIRTURN");
		int stat = getStatus(me);
		if (stat == IWON) {
		me.send("THEYWON");
		if (getStatus(me) != SENTSTRING) {
		System.out.println("Received Bad Status");
		me.closeConnections();
		}
		me.send(sentString);
		playgame = false;
		}
		else if (stat == ITIED) {
		me.send("THEYTIED");
		if (getStatus(me) != SENTSTRING) {
		System.out.println("Received Bad Status");
		me.closeConnections();
		}
		me.send(sentString);
		playgame = false;
		}
		else if (stat == IQUIT) {
		me.send("THEYQUIT");
		playgame = false;
		}
		else if (stat == SENTSTRING) {
		me.send(sentString);
		}
		else if (stat == ERROR) {
		me.send("ERROR");
		me.closeConnections();
		playgame = false;
		}
		else {
		System.out.println("Received Bad Status");
		sendStatus(me,ERROR);
		me.closeConnections();
		playgame = false;
		}
		}
		}
		me.closeConnections();
		return;
		}
		catch (IOException e) {
		System.out.println("I/O Error:" + e);
		System.exit(1);
		}
		}
	
	private synchronized int getStatus(Connect4Player me) {
		Vector ourVector = ((me == player1) ? p1Queue : p2Queue);
		while (ourVector.isEmpty()) {
		try {
		wait();
		}
		catch (InterruptedException e) {
		System.out.println("Error:" + e);
		}
		}
		try {
		Integer retval = (Integer)(ourVector.firstElement());
		try {
		ourVector.removeElementAt(0);
		}
		catch (ArrayIndexOutOfBoundsException e) {
		System.out.println("Array index out of bounds:" + e);
		System.exit(1);
		}
		return retval.intValue();
		}
		catch (NoSuchElementException e) {
		System.out.println("Couldn’t get first element:" + e);
		System.exit(1);
		return 0; // never reached, just there to appease compiler
		}
		}
	
	private synchronized void sendStatus(Connect4Player me, int message) {
		Vector theirVector = ((me == player1) ?	p2Queue : p1Queue);
		theirVector.addElement(new Integer(message));
		notify();
		}
}
