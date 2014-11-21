package Game;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 
 * @author Shawn Francis and Josh Gordon
 * 
 */
public class Map implements Serializable {

	private static final long serialVersionUID = -1808241378497805073L;
	private static final String baseDir = System.getProperty("user.dir")
			+ "/Maps/";
	private ArrayList<Continent> continentList;
	private ArrayList<Country> countryList;

	/**
	 * This constructor creates the map used in the Risk game. It loads in a map
	 * via the filename passed into the constructor.
	 * 
	 * @param fileName
	 */
	public Map(String fileName) {
		continentList = new ArrayList<Continent>();
		countryList = new ArrayList<Country>();
		loadMap(fileName);
	}

	public int getSize() {
		return continentList.size();
	}

	public Continent getContinent(int index) {
		return continentList.get(index);
	}

	/**
	 * This method checks each continent stored in the map to see if the passed
	 * in player owns all continents. i.e. checks if that player is the winner
	 * of the game.
	 * 
	 * @param player
	 * @return
	 */
	public boolean checkWinner(Player player) {
		for (int i = 0; i < continentList.size(); i++) {
			if (continentList.get(i).checkContinentOwned(player) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method checks to see if the map has been occupied by players
	 * completely or not.
	 * 
	 * @return
	 */
	public boolean checkInitialization() {
		for (int i = 0; i < continentList.size(); i++) {
			for (int j = 0; j < continentList.get(i).getSize(); j++) {
				if (continentList.get(i).getCountryOwner(j) == null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This method returns the total number of countries owned by the player
	 * passed into the method.
	 * 
	 * @param player
	 * @return
	 */
	public int getCountriesOwned(Player player) {
		int occupiedCountries = 0;
		for (int i = 0; i < continentList.size(); i++) {
			occupiedCountries += continentList.get(i).checkCountriesOwned(
					player);
		}
		return occupiedCountries;
	}

	/**
	 * This method returns the total troop bonus the player passed into the
	 * method gets for owning full continents.
	 * 
	 * @param player
	 * @return
	 */
	public int getContinentBonus(Player player) {
		int totalBonus = 0;
		for (int i = 0; i < continentList.size(); i++) {
			if (continentList.get(i).checkContinentOwned(player) == true) {
				totalBonus += continentList.get(i).getBonus();
			}
		}
		return totalBonus;
	}

	public void addArmies(String name, int armies) {
		String countryName = new String();
		for (int i = 0; i < continentList.size(); i++) {
			for (int j = 0; j < continentList.get(i).getSize(); j++) {
				countryName = continentList.get(i).getCountryName(j);
				if (countryName.equalsIgnoreCase(name) == true) {
					continentList.get(i).addArmies(j, armies);
					break;
				}
			}
		}
	}

	public void removeArmies(String name, int armies) {
		String countryName = new String();
		for (int i = 0; i < continentList.size(); i++) {
			for (int j = 0; j < continentList.get(i).getSize(); j++) {
				countryName = continentList.get(i).getCountryName(j);
				if (countryName.equalsIgnoreCase(name) == true) {
					continentList.get(i).removeArmies(j, armies);
				}
			}
		}
	}

	public int getCountryArmy(String name) {
		String countryName = new String();
		for (int i = 0; i < continentList.size(); i++) {
			for (int j = 0; j < continentList.get(i).getSize(); j++) {
				countryName = continentList.get(i).getCountryName(j);
				if (countryName.equalsIgnoreCase(name) == true) {
					return continentList.get(i).getCountryArmy(j);
				}
			}
		}
		return 0;
	}

	public ArrayList<String> getCountryBorders(String name) {
		String countryName = new String();
		for (int i = 0; i < continentList.size(); i++) {
			for (int j = 0; j < continentList.get(i).getSize(); j++) {
				countryName = continentList.get(i).getCountryName(j);
				if (countryName.equalsIgnoreCase(name) == true) {
					return continentList.get(i).getCountryBorders(j);
				}
			}
		}
		return null;
	}

	public Player getCountryOwner(String name) {
		String countryName = new String();
		for (int i = 0; i < continentList.size(); i++) {
			for (int j = 0; j < continentList.get(i).getSize(); j++) {
				countryName = continentList.get(i).getCountryName(j);
				if (countryName.equalsIgnoreCase(name) == true) {
					return continentList.get(i).getCountryOwner(j);
				}
			}
		}
		return null;
	}

	public void setCountryOwner(String name, Player player) {
		String countryName = new String();
		for (int i = 0; i < continentList.size(); i++) {
			for (int j = 0; j < continentList.get(i).getSize(); j++) {
				countryName = continentList.get(i).getCountry(j).getName();
				if (countryName.equalsIgnoreCase(name) == true) {
					continentList.get(i).setCountryOwner(j, player);
					return;
				}
			}
		}
	}

	/**
	 * This private method loads in a map and creates the "tree" of continents
	 * and countries based off the file loaded.
	 * 
	 * @param fileName
	 */
	private void loadMap(String fileName) {
		int armyBonus;
		Scanner fileScanner;
		String lineFromFile, continentName, countryName, bonus;
		Continent newContinent;
		Country newCountry;
		boolean continentDone = false;
		ArrayList<String> borderNames;
		StringTokenizer lineOfText;
		File cardFile = new File(baseDir + fileName);

		try {
			fileScanner = new Scanner(cardFile);
			while (fileScanner.hasNextLine() == true) {
				lineFromFile = fileScanner.nextLine();
				lineOfText = new StringTokenizer(lineFromFile, ",");
				continentName = lineOfText.nextToken();
				bonus = lineOfText.nextToken();
				armyBonus = Integer.parseInt(bonus);
				newContinent = new Continent(continentName, armyBonus);
				while (continentDone == false) {
					lineFromFile = fileScanner.nextLine();
					if (lineFromFile.equals("--End--")) {
						continentDone = true;
					} else {
						countryName = lineFromFile;
						lineFromFile = fileScanner.nextLine();
						lineOfText = new StringTokenizer(lineFromFile, ",");
						borderNames = new ArrayList<String>();
						while (lineOfText.hasMoreTokens()) {
							borderNames.add(lineOfText.nextToken());
						}
						lineFromFile = fileScanner.nextLine();
						lineOfText = new StringTokenizer(lineFromFile, ",");
						Color color = new Color(Integer.parseInt(lineOfText
								.nextToken()), Integer.parseInt(lineOfText
								.nextToken()), Integer.parseInt(lineOfText
								.nextToken()));
						lineFromFile = fileScanner.nextLine();
						lineOfText = new StringTokenizer(lineFromFile, ",");
						newCountry = new Country(countryName, borderNames);
						newCountry.setColor(color);
						newCountry.setCenterX(Integer.parseInt(lineOfText.nextToken()));
						newCountry.setCenterY(Integer.parseInt(lineOfText.nextToken()));
						countryList.add(newCountry);
						newContinent.addCountry(newCountry);
					}
				}
				continentList.add(newContinent);
				continentDone = false;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Country getCountry(int index) {
		return countryList.get(index);
	}
	
	public int numCountries() {
		return countryList.size();
	}

}
