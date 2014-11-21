package Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This class stores the deck of cards used for the
 * Risk game.  When it is initially created it loads
 * the cards from a text file that is passed into the
 * constructor.
 * 
 * @author Shawn Francis and Josh Gordon 
 *
 */
public class CardDeck implements Serializable {
	
	private static final long serialVersionUID = -5245037504219260733L;
	private static final String baseDir = System.getProperty("user.dir") + "/Cards/";
	
	private ArrayList<Card> deck;
	private static Random ourGenerator = new Random();
	
	/**
	 * This constructor creates the deck of cards
	 * from the passed filename and shuffles them.
	 * @param fileName
	 */
	public CardDeck(String fileName) {
		deck = new ArrayList<Card>();
		this.loadDeck(fileName);
		Collections.shuffle(deck, ourGenerator);
	}
	
	/**
	 * This method adds a card back to the deck.
	 * @param card
	 */
	public void add (Card card) {
		deck.add(card);
	}
	
	/**
	 * This method returns the number of cards
	 * currently in the deck.
	 * @return
	 */
	public int getDeckSize() {
		return deck.size();
	}
	
	/**
	 * This method deals a card from the deck
	 * and removes it from the deck.
	 * @return
	 */
	public Card deal() {
		int card = ourGenerator.nextInt(deck.size());
		return deck.remove(card);
	}
	
	/**
	 * This private method takes the passed in
	 * filename and creates the deck of cards from
	 * the text file.
	 * @param fileName
	 */
	private void loadDeck(String fileName){
		int troopType;
		String territory, troop;
		String lineFromFile;
		Scanner fileScanner;
		File cardFile = new File(baseDir + fileName);
		
		try {
			fileScanner = new Scanner(cardFile);
			while(fileScanner.hasNextLine() == true){
				lineFromFile = fileScanner.nextLine();
				StringTokenizer lineOfText = new StringTokenizer(lineFromFile, ",");
				territory = lineOfText.nextToken();
				troop = lineOfText.nextToken();
				troopType = Integer.parseInt(troop);
				deck.add(new Card(territory, troopType));
			}
			fileScanner.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

