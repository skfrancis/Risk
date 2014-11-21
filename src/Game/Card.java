package Game;

import java.io.Serializable;

/**
 * This Class stores a single card that is used within
 * the game of Risk.  It stores it's territory name and
 * it's troop type.
 *  
 * 0 - infantry
 * 1 - cavalry
 * 2 - artillery
 * 3 - wild card (all three types)
 * 
 * @author Shawn Francis and Josh Gordon
 *
 */
public class Card implements Serializable {

	private static final long serialVersionUID = 3511667480953989998L;
	private String territory;
	private int troopType;
	
	/**
	 * This constructor creates a card with its territory
	 * name and troop type.
	 * @param territory
	 * @param troop
	 */
	public Card (String territory, int troop) {
		this.territory = territory;
		this.troopType = troop;
	}

	/**
	 * This method returns the stored
	 * territory name. 
	 */
	public String getTerritory() {
		return territory;
	}
	
	/**
	 * This method returns the stored
	 * troop type.
	 */
	public int getTroopType() {
		return troopType;
	}
}
