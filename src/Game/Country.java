package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.awt.Color;

/**
 * This class stores the information of a country
 * in the game of Risk.
 * 
 * @author Shawn Francis and Josh Gordon
 *
 */
public class Country implements Serializable {
	
	private static final long serialVersionUID = 939216685142322516L;
	private String countryName;
	private Player currentOwner;
	private int currentArmy;
	private ArrayList<String> borderList;
	private Color color;
	private int centerX;
	private int centerY;
	
	/**
	 * This constructor creates a new country
	 * object setting the name and its borders
	 * and having no occupying armies and no
	 * player owner.
	 *
	 * @param name
	 * @param borders
	 */
	public Country(String name, ArrayList<String> borders){
		countryName = name;
		currentOwner = null;
		currentArmy = 0;
		borderList = new ArrayList<String>(borders);
	}
	
	/**
	 * This method returns the country's name
	 * @return
	 */
	public String getName(){
		return countryName;
	}
	
	/**
	 * This method returns the current
	 * player that occupies the country.
	 * @return
	 */
	public Player getOwner(){
		return currentOwner;
	}
	
	/**
	 * This method returns the color
	 * of the country on the GUI map.
	 * @return
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * This method returns the current
	 * army amount occupying the country.
	 * @return
	 */
	public int getArmy(){
		return currentArmy;
	}
	
	/**
	 * This method returns the country's 
	 * border list.
	 * @return
	 */
	public ArrayList<String> getBorders(){
		return borderList;
	}
	
	/**
	 * This method will set the current
	 * owner to the passed in player.
	 * @param player
	 */
	public void setOwner(Player player){
		currentOwner = player;
	}
	
	/**
	 * This method will set the color
	 * of the country on the GUI map.
	 * @param player
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setCenterX(int x) {
		this.centerX = x;
	}
	
	public int getCenterX() {
		return this.centerX;
	}
	
	public void setCenterY(int y) {
		this.centerY = y;
	}
	
	public int getCenterY() {
		return this.centerY;
	}
	
	/**
	 * This method will add the specified
	 * amount of armies to the current total
	 * occupying the country.
	 * @param armies
	 */
	public void addArmies(int armies){
		currentArmy += armies;
	}
	
	/**
	 * This method will remove the specified
	 * amount of armies from the current total
	 * occupying the country.  Granted the amount
	 * is less than the current amount.
	 * @param armies
	 */
	public void removeArmies(int armies){
		if(armies <= currentArmy){
			currentArmy -= armies;
		}
	}

	/**
	 * This method checks the list of borders
	 * for the country to see if it is in fact
	 * a border country.
	 * @param borderCountry
	 * @return
	 */
	public boolean isBorder(String borderCountry){
		for(int i = 0; i < borderList.size(); i++){
			if(borderList.get(i).equalsIgnoreCase(borderCountry)){
				return true;
			}
		}
		return false;
	}
}
