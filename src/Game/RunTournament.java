package Game;

public class RunTournament {
	
	public static void main(String[] args) {
		int startingArmies;
		int bonusArmies;
		int attackDice;
		int defendDice;
		int [] wins = new int[5];
		boolean hasWinner;
		Player defendingPlayer;
		String chosenCountry = new String();
		String attackCountry = new String();
		String defendCountry = new String();
		String fortifyCountry = new String();
		GameBoard board;
			
		for(int games = 1; games < 1001; games++){
			hasWinner = false;
			board = new GameBoard();
			board.loadCardDeck("riskcards.txt");
			board.loadMap("riskmap.txt");
			board.addPlayer(new ComputerPlayer("Comp 1", new BeginnerStrategy()));
			board.addPlayer(new ComputerPlayer("Comp 2", new IntermediateStrategy()));
			startingArmies = board.startingArmies();		
			while(board.isInitialized() == false){
				chosenCountry =((ComputerPlayer)board.currentPlayer()).chooseCountry(board.getMap());
				board.setCountryOwner(chosenCountry, board.currentPlayer());
				board.placeArmies(chosenCountry, 1);
				startingArmies --;
				board.nextPlayer();
			}
			while(startingArmies != 0){
				chosenCountry = ((ComputerPlayer)board.currentPlayer()).placeArmy(board.getMap());
				board.placeArmies(chosenCountry, 1);
				startingArmies--;
				board.nextPlayer();
			}
			while(hasWinner == false){
				bonusArmies = 0;
				bonusArmies = board.calculateArmies(board.currentPlayer());
				if(((ComputerPlayer)board.currentPlayer()).checkForSet() == true){
					board.turnInSet(((ComputerPlayer)board.currentPlayer()).turnInCards());
					bonusArmies += board.armiesForTurnIn();
				}
				
				while(bonusArmies != 0){
					chosenCountry = ((ComputerPlayer)board.currentPlayer()).placeArmy(board.getMap());
					board.placeArmies(chosenCountry, 1);
					bonusArmies--;
				}
				
				while(((ComputerPlayer) board.currentPlayer()).playAgain(board.currentPlayer(), board.getMap())){
					attackCountry = new String();
					defendCountry = new String();
					attackCountry = ((ComputerPlayer)board.currentPlayer()).selectOwnedCountry(board.getMap());
					if(attackCountry != null){
						defendCountry = ((ComputerPlayer)board.currentPlayer()).attack(attackCountry, board.getMap());
						if(defendCountry != null){
							attackDice = ((ComputerPlayer)board.currentPlayer()).chooseAttackDice(attackCountry, board.getMap());
							defendingPlayer = board.getMap().getCountryOwner(defendCountry);
							defendDice = ((ComputerPlayer)defendingPlayer).chooseDefendDice(defendCountry, board.getMap());
							board.rollDice(attackCountry, attackDice, defendCountry, defendDice);
						}
					}
				}
				chosenCountry = ((ComputerPlayer)board.currentPlayer()).selectOwnedCountry(board.getMap());
				if(chosenCountry != null){
					fortifyCountry = ((ComputerPlayer)board.currentPlayer()).fortify(chosenCountry, board.getMap());
					if(fortifyCountry != null){
						board.fortifyCountry(chosenCountry, fortifyCountry, ((ComputerPlayer)board.currentPlayer()).moveArmies(chosenCountry, board.getMap()));
					}
				}
	
				if(board.hasDefeatedCountry() == true){
					board.dealCard(board.currentPlayer());
				}
				
				if(board.isWinner(board.currentPlayer()) == false){
					board.nextPlayer();
				}else{
					hasWinner = true;
				}
			}
			if(board.currentPlayer().getName() == "Comp 1"){ 
			   	wins[0]++;
			} else {
				wins[1]++;
			}
			System.out.println("Game " + games + " finished");
		}
		System.out.println("Beginner Strategy won     : " + wins[0] + " times");
		System.out.println("Intermediate Strategy won : " + wins[1] + " times");
		System.out.println("Done!");
	}

}
