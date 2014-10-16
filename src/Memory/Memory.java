package Memory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import Memory.Kort.Status;

public class Memory implements ActionListener{
	//ActionEvent data
	private static Kort lastCard;
	private static int activeCards = 0;
	private Kort tempCard = null;
	private Timer sameCardTimer = new Timer(1500, this);
	private Timer wrongCardTimer = new Timer(1500, this);
	private int[] playerScore;
	private int playerTurn;
	
	//Game frame
	JFrame gameFrame = new JFrame("Memory");
	JPanel scorePanel = new JPanel();
	JPanel memoryPanel = new JPanel();
	JPanel menuPanel = new JPanel(new FlowLayout());
	
	Random random = new Random();
	
	//New game buttons and data
	JButton nyttSpel = new JButton("Nytt spel");
	JButton avsluta = new JButton("Avsluta");
	JTextField entry = new JTextField();
	JTextField noRowsField = new JTextField("5");
	JTextField noColField = new JTextField("5");
	JPanel rowAndCol = new JPanel();
	JFrame selectPlayersFrame = new JFrame("Please select the number of players");
	JLabel selectPlayersLabel = new JLabel("Enter number of players:");
	JLabel rowAndColText = new JLabel("Select # rows and columns: ");
	JPanel selectPlayersButtonPanel = new JPanel(new FlowLayout());
	JButton startGame = new JButton("Start Game!");
	
	JButton cancle = new JButton("Cancle");
	

	int noRows;
	int noCol;
	int noCards;
	int noPlayers;
	Kort[] deck = new Kort[(noCards/2)*2];
	
	//Victory frame
	JFrame victoryFrame = new JFrame("Victory!!!");
	JPanel victoryButtonPanel = new JPanel(new FlowLayout());
	int highestScore;
	
	
	//Resources
	File bildMapp = new File("bildmapp");
	File[] bilder = bildMapp.listFiles();
	Kort[] allCards = new Kort[bilder.length];
	
	Font playerFont = new Font("Comic Sans MS", 1, 20);
	
	public Memory(){
		for (int i = 0; i < allCards.length;i++){
			allCards[i] = new Kort(new ImageIcon(bilder[i].getPath()));
		}
		
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setLayout(new BorderLayout());
		gameFrame.setSize(1920,1080);
		
		nyttSpel.addActionListener(this);
		avsluta.addActionListener(this);
	
		menuPanel.add(nyttSpel);
		menuPanel.add(avsluta);

		gameFrame.add(menuPanel, BorderLayout.SOUTH);
		gameFrame.setVisible(true);
		
	}
	public void nyttSpel(){
		gameFrame.remove(memoryPanel);
		gameFrame.remove(scorePanel);
		gameFrame.remove(menuPanel);
		
		memoryPanel = new JPanel();
		scorePanel = new JPanel();
		
		//Bygger upp en Deck med kort, blandar och lägger ut dem på spelplanen.
		int tempRandom = 0;
		boolean duplicateCard = true;
		for (int i = 0; i < deck.length;i++){
			deck[i] = null;
		}
		for (int i = 0;i < deck.length/2;i++){
			duplicateCard = true;
			int noTries = 0;
			while (duplicateCard){
				tempRandom = random.nextInt(allCards.length-1);
				for (int j = 0; j < deck.length;j++){
					if (deck[j] == null){
						duplicateCard = false;
						j = deck.length;
					} else if (noTries == deck.length-1){
						throw new IndexOutOfBoundsException ("Please select a lower number of cards. Not enough unique cards in libary.");
					} else if (deck[j].sammaBild(allCards[tempRandom])){
						j = deck.length;
						noTries++;
}
				}
				
			}
			deck[i] = allCards[tempRandom].copy();
			deck[i+deck.length/2] = deck[i].copy();
			deck[i].addActionListener(this);
			deck[i+deck.length/2].addActionListener(this);
		}
		
		Verktyg.slumpOrdning(deck);

		for (int i = 0; i < deck.length; i++){
			memoryPanel.add(deck[i]);
		}
		//Bygger upp scorePanel
		scorePanel.setLayout(new GridLayout(noPlayers,1));
		playerScore = new int[noPlayers];
		for (int i = 0; i < noPlayers; i++ ){
			playerScore[i] = 0;
			JLabel player = new JLabel("Player " + (i+1) + "\nScore: " + playerScore[i]);
			player.setFont(playerFont);
			scorePanel.add(player);
			if (i == 0){
				player.setBackground(Color.yellow);
				player.setOpaque(true);
			}
		}
		
		//MenuPanel borde inte behöva uppdateras, men knapparna försvinner av någon anledning varvid de behöver skapas om vid nytt spel.
		menuPanel.add(nyttSpel);
		menuPanel.add(avsluta);
		
		gameFrame.add(scorePanel,BorderLayout.WEST);
		gameFrame.add(memoryPanel, BorderLayout.CENTER);
		gameFrame.add(menuPanel, BorderLayout.SOUTH); 
		
		memoryPanel.setLayout(new GridLayout(noCol,noRows));		
		gameFrame.setSize(1920,1079);
		gameFrame.setSize(1920,1080);
		gameFrame.repaint();
	}
	
	private void updateScore(int player){
		for (int i = 0; i < noPlayers; i++ ){
			scorePanel.remove(i);
			JLabel playerText = new JLabel("Player " + (i+1) + " Score: " + playerScore[i]);
			playerText.setFont(playerFont);
			scorePanel.add(playerText,i);
			if (i == player){
				playerText.setBackground(Color.YELLOW);
				playerText.setOpaque(true);
			}
		}
		gameFrame.repaint();
	}
	
	private void victory(){
		boolean gameOver = true;
		for (int i = 0; i < deck.length;i++){
			if (deck[i].getStatus() != Status.SAKNAS){
				gameOver = false;
			}
		}
		if (gameOver){
			highestScore = playerScore[0];
			String winnerText = "And the winner is: ";
			String winner = "Player 1";
			for (int i = 1; i < playerScore.length;i++){
				if (playerScore[i] > highestScore){
					highestScore = playerScore[i];
					winnerText = "And the winner is: ";
					winner = "Player " + (i+1);
				} else if (playerScore[i] == highestScore){
					winnerText = "There was a tie between: ";
					winner = winner + " and Player " +(i+1);
				}
			}
			victoryFrame.setLayout(new BorderLayout());
			victoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JLabel victoryText = new JLabel(winnerText+winner+" with " + highestScore +" points!");
			victoryText.setFont(playerFont);
			victoryButtonPanel.add(nyttSpel);
			victoryButtonPanel.add(avsluta);
			victoryFrame.add(victoryText,BorderLayout.CENTER);
			victoryFrame.add(victoryButtonPanel,BorderLayout.SOUTH);
			victoryFrame.pack();
			victoryFrame.setVisible(true);
		}
	}
	
	private void setNoPlayers(){
		selectPlayersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		selectPlayersFrame.setLayout(new BorderLayout());
		startGame.addActionListener(this);
		cancle.addActionListener(this);
		selectPlayersButtonPanel.add(startGame);
		selectPlayersButtonPanel.add(cancle);
		selectPlayersLabel.setFont(playerFont);
		selectPlayersFrame.add(selectPlayersLabel, BorderLayout.NORTH);
		selectPlayersFrame.add(entry, BorderLayout.CENTER);
		selectPlayersFrame.add(selectPlayersButtonPanel,BorderLayout.SOUTH);
		selectPlayersFrame.pack();
		selectPlayersFrame.setVisible(true);
		
	}
	
	private void setNoPlayers2(){
		selectPlayersFrame.remove(selectPlayersLabel);
		selectPlayersFrame.remove(entry);
		selectPlayersFrame.remove(rowAndCol);
		selectPlayersFrame.remove(selectPlayersButtonPanel);
		rowAndCol = new JPanel();
		selectPlayersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		selectPlayersFrame.setLayout(new GridLayout(3,1));
		rowAndCol.setLayout(new FlowLayout());
		rowAndColText.setFont(playerFont);
		rowAndCol.add(rowAndColText);
		rowAndCol.add(noRowsField);
		rowAndCol.add(noColField);
		startGame.addActionListener(this);
		cancle.addActionListener(this);
		selectPlayersButtonPanel.add(startGame);
		selectPlayersButtonPanel.add(cancle);
		selectPlayersLabel.setFont(playerFont);
		selectPlayersFrame.add(selectPlayersLabel);
		selectPlayersFrame.add(entry);
		selectPlayersFrame.add(rowAndCol);
		selectPlayersFrame.add(selectPlayersButtonPanel);
		selectPlayersFrame.pack();
		selectPlayersFrame.setVisible(true);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (avsluta.equals(arg0.getSource())){
			System.exit(1);
		} else if (nyttSpel.equals(arg0.getSource())){
			victoryFrame.setVisible(false);
			setNoPlayers2();
		} else if (startGame.equals(arg0.getSource())){
			try {
				noPlayers = Integer.parseInt(entry.getText());
				noRows = Integer.parseInt(noRowsField.getText());
				noCol = Integer.parseInt(noColField.getText());
				noCards = noRows*noCol;
				nyttSpel();
				selectPlayersFrame.setVisible(false);
			} catch (NullPointerException e) {
				setNoPlayers2();
			} catch (NumberFormatException e){
				setNoPlayers2();
			}
			
		} else if (cancle.equals(arg0.getSource())) {
			selectPlayersFrame.setVisible(false);
		} else if (sameCardTimer.equals(arg0.getSource())){
			sameCardTimer.stop();
			lastCard.setStatus(Status.SAKNAS);
			tempCard.setStatus(Status.SAKNAS);
			lastCard=null;
			activeCards = 0;
			playerScore[playerTurn]++;
			updateScore(playerTurn);
			victory();
		} else if (wrongCardTimer.equals(arg0.getSource())){
			wrongCardTimer.stop();
			lastCard.setStatus(Status.DOLT);
			tempCard.setStatus(Status.DOLT);
			lastCard=null;
			activeCards = 0;
			playerTurn = (playerTurn+1)%noPlayers;
			updateScore(playerTurn);
		} else if (activeCards <= 1 && arg0.getSource() instanceof Kort){
			tempCard = (Kort) arg0.getSource();
			if (tempCard.getStatus() == Status.DOLT){
				activeCards++;
				tempCard.setStatus(Status.SYNLIGT);
				if (lastCard == null){
					lastCard=tempCard;
				}else if (tempCard.sammaBild(lastCard)){
					sameCardTimer.start();
				} else {
					wrongCardTimer.start();
				}
			}
		}
	}
}
