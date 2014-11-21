package Game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class stores continent information 
 * for the game of Risk.
 * 
 * @author Shawn Francis and Josh Gordon
 *
 */
public class Continent implements Serializable {
		
	private static final long serialVersionUID = 5703365860953114696L;
	private String continentName;
	private int troopBonus;
	private ArrayList<Country> countryList;
	
	/** 
	 * This constructor creates a new continent
	 * with its name, troop bonus and a list of
	 * countries contained within that continent.
	 * @param name
	 * @param bonus
	 */
	public Continent(String name, int bonus){
		countryList = new ArrayList<Country>();
		continentName = name;
		troopBonus = bonus;
	}
	
	/**
	 * This method gets the name of the continent.
	 * @return
	 */
	public String getName(){
		return continentName;
	}
	
	/**
	 * This method gets the troop bonus
	 * for the continent.
	 * @return
	 */
	public int getBonus(){
		return troopBonus;
	}
	
	/**
	 * This method adds a new country to the list
	 * of countries contained within the continent
	 * @param newCountry
	 */
	public void addCountry(Country newCountry){
		countryList.add(newCountry);
	}
	
	/**
	 * This method returns the size of the
	 * list of countries stored within the
	 * continent.
	 * @return
	 */
	public int getSize(){
		return countryList.size();
	}
	
	/**
	 * This method gets a country object from 
	 * the stored list of countries via the index
	 * passed into the method.
	 * @param index
	 * @return
	 */
	public Country getCountry(int index){
		return countryList.get(index);
	}
	
	/**
	 * This method returns the country's name
	 * based on the index passed in.
	 * @param index
	 * @return
	 */
	public String getCountryName(int index){
		return countryList.get(index).getName();
		
	}
	/**
	 * This method returns the current owner
	 * for the country at the specified index.
	 * @param index
	 * @return
	 */
	public Player getCountryOwner(int index){
		return countryList.get(index).getOwner();
	}
	
	/**
	 * This method returns the current army
	 * based on the index passed in.
	 * @param index
	 * @return
	 */
	public int getCountryArmy(int index){
		return countryList.get(index).getArmy();
	}
	
	/**
	 * This method returns the country's borders
	 * based on the index passed in.
	 * @param index
	 * @return
	 */
	public ArrayList<String> getCountryBorders(int index){
		return countryList.get(index).getBorders();
		
	}
	
	/**
	 * This method adds armies to the selected country
	 * at the passed in index.
	 * @param index
	 * @param armies
	 */
	public void addArmies(int index, int armies){
		countryList.get(index).addArmies(armies);
	}
	
	/**
	 * This method removes armies from the selected 
	 * country at the passed in index.
	 * @param index
	 * @param armies
	 */
	public void removeArmies(int index, int armies){
		countryList.get(index).removeArmies(armies);
	}
	
	/**
	 * This method sets the country's owner at the
	 * passed in index.
	 * @param index
	 * @param player
	 */
	public void setCountryOwner(int index, Player player){
		countryList.get(index).setOwner(player);
	}
	
	/**
	 * This method checks to see which
	 * countries are owned by the player
	 * passed into the method.
	 * @param player
	 * @return
	 */
	public int checkCountriesOwned(Player player){
		int countriesOwned = 0;
		for(int i = 0; i < countryList.size(); i++){
			if(countryList.get(i).getOwner() == player){
				countriesOwned++;
			}
		}
		return countriesOwned;
	}
	
	/**
	 * This method checks to see if the player
	 * passed into the method owns all the countries
	 * contained within the continent.
	 * @param player
	 * @return
	 */
	public boolean checkContinentOwned(Player player){
		boolean continentOwned = true;
		for(int i = 0; i < countryList.size(); i++){
			if(countryList.get(i).getOwner() != player)
				continentOwned = false;
		}
		return continentOwned;
	}
}
