package Game;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

	private static final long serialVersionUID = 5113144972834813030L;
	protected String playerName;
	protected ArrayList<Card> myHand;
	private Color color;
	
	public Player(String name) {
		playerName = name;
		myHand = new ArrayList<Card>();
	}

	/**
	 * This method returns the stored name of the player.
	 * 
	 * @return
	 */
	public String getName() {
		return playerName;
	}

	/**
	 * This method adds a card to the player's hand of cards.
	 * 
	 * @param card
	 */
	public void addCard(Card card) {
		myHand.add(card);
	}

	/**
	 * This method removes a card from the player's hand of cards.
	 * 
	 * @param index
	 * @return
	 */
	public Card returnCard(int index) {
		return myHand.remove(index);
	}

	/**
	 * This method returns the current number of cards in the player's hand of
	 * cards.
	 * 
	 * @return
	 */
	public int getHandSize() {
		return myHand.size();
	}

	/**
	 * This method returns the player's hand of cards.
	 * 
	 * @return
	 */
	public ArrayList<Card> getHand() {
		return myHand;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
		
	/**
	 * This method checks the player's hand of cards to see if they have a set
	 * that can be turned in.
	 * 
	 * @return
	 */
	public boolean checkForSet() {
		boolean canTurnIn = false;
		int cannonCards = 0;
		int infantryCards = 0;
		int cavalryCards = 0;

		for (int i = 0; i < myHand.size(); ++i) {
			int troopType = myHand.get(i).getTroopType();
			if (troopType == 0)
				++infantryCards;
			if (troopType == 1)
				++cavalryCards;
			if (troopType == 2)
				++cannonCards;
			if (troopType == 3) {
				++infantryCards;
				++cavalryCards;
				++cannonCards;
			}
		}
		if ((cannonCards >= 3)
				|| (infantryCards >= 3)
				|| (cavalryCards >= 3)
				|| ((cannonCards >= 1) && (infantryCards >= 1) && (cavalryCards >= 1))
				&& (myHand.size() >= 3)) {
			canTurnIn = true;
		}
		return canTurnIn;
	}
}
