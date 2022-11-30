package game;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;

import javax.swing.JPanel;
import cards.*;



public class Game extends JPanel implements Runnable{
	
	
	private final int FPS = 60;
	public final int screenWidth, screenHeight;
	
	
	
	public boolean inGame = false;
	
	public boolean playerTurn = false, opponentPlayable = false, loading = false, inMatch = false;;
	public int opponentHandNumber;
	
	public String IP, infoSent = "", infoRecv = "";
	public int portNo;
	public boolean tryConnecting = false, failConnecting = true;
	private PrintWriter out;
	private BufferedReader in;
	
	private Thread gameThread;
	public Socket socket;
	public Player player1 = new Player();
	
//	public Stack<Card> Deck = new Stack<Card>();
	public MouseHandler mouseH = new MouseHandler(this);
	public CombinationChecker cmbCh = new CombinationChecker(this);
	public UI ui;
	
	public ArrayList<Card> cardSetSelected = new ArrayList<Card>(), opponentCardSetSelected = new ArrayList<Card>(), 
			cardSetDraw = new ArrayList<Card>(), opponentCardSetDraw = new ArrayList<Card>(), myHandCopy = new ArrayList<Card>();
	
	
	public Game(int gameWidth, int gameHeight, int inputPanelWidth) {
		this.setPreferredSize(new Dimension(gameWidth, gameHeight));
		screenWidth = gameWidth - inputPanelWidth;
		screenHeight = gameHeight;
		this.setDoubleBuffered(true);
		this.setFocusable(true);
		this.setOpaque(false);
		this.addMouseListener(mouseH);
		ui = new UI(this, inputPanelWidth);
	}
	
	public void setupGame() {
		playerTurn = true;
		player1.Hand.clear();
		myHandCopy.clear();
		cardSetSelected.clear();
		opponentCardSetSelected.clear();
		cardSetDraw.clear();
		opponentCardSetDraw.clear();
		inMatch = false;
		loading = false;
//		handNumber = 26;
//		if(!Deck.isEmpty()) Deck.clear();
//		if(!player1.Hand.isEmpty()) player1.Hand.clear();
//		if(!player2.Hand.isEmpty()) player2.Hand.clear();
		
//		shuffleAndDealCards();
		
		
		
		
	}
	
//	private void shuffleAndDealCards() {
//		int i;
//		for(i = 2; i <= 14; ++i) {
//			Deck.push(new Card(i, "Hearts", 4));
//			Deck.push(new Card(i, "Diamonds", 3));
//			Deck.push(new Card(i, "Clubs", 2));
//			Deck.push(new Card(i, "Spades", 1));
//			}
//		Collections.shuffle(Deck);
//		for(i = 0; i < handNumber; ++i) {
//			player1.Hand.add(Deck.pop());
//			player2.Hand.add(Deck.pop());
//		}
//		removeThree();
//	}

	public void run() {
		double drawInterval = 1000000000/FPS; // 0.0166 seconds
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime)/drawInterval;
			lastTime = currentTime;
			if(delta >= 1) {
			// 1. Update: Update information
			update();
			// 2. Draw: Draw the screen with the updated information
			repaint();
			delta--;
			}
		}
		
}
	
	
	
	public void update() {
		if(tryConnecting == true && isValidInet4Address(IP)) {
			try {
				socket = new Socket(IP, portNo);
				out = new PrintWriter(socket.getOutputStream(), true);
		        failConnecting = false;
		        tryConnecting = false;
			}catch(IOException e) {
				e.printStackTrace();
				failConnecting = true;
				tryConnecting = false;
				return;
			}
	        
		}
		
		if(failConnecting == false) {
			if(inMatch == false) {
			getCardsFromServer();
			decideTurn();
			myHandCopy = new ArrayList<Card>(player1.Hand);
			inMatch = true;
			return;
			}
			
			
		if(playerTurn == true) {
				
		selectCard();

		if(mouseH.button1Active == true && cmbCh.validToPlay(cardSetSelected, opponentCardSetSelected)) {
			playCards();
			return;
		}
		else mouseH.button1Active = false;
		
		if(mouseH.button2Active == true) clearSelection();
		
		if(mouseH.button3Active == true) swapping();
		
		if(mouseH.button4Active == true && !opponentCardSetSelected.isEmpty()) {
			passTurn();
			return;
		}
		else mouseH.button4Active = false;
	}
		
		if(playerTurn == false) OpponentPlay();
	}
		
}
	
	private void getCardsFromServer() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			infoRecv = in.readLine();
			System.out.println(infoRecv);
			String[] split = infoRecv.split("(?<=\\G.{" + 2 + "})");
			for(String temp : split) player1.Hand.add(new Card(Integer.parseInt(temp)));
			Collections.sort(player1.Hand, new Comparator<Card>() {
				
				@Override
				public int compare(Card c1, Card c2) {
					if(c1.getValue() == c2.getValue()) return c2.getSuitValue() - c1.getSuitValue();
					return c1.getValue() - c2.getValue();
				}
			});
			opponentHandNumber = player1.Hand.size();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void decideTurn() {
		for(Card card : player1.Hand) if(card.getValue() == 3 && card.getSuitValue() == 1) {
			playerTurn = true;
			cardSetDraw.add(card);
			removeThree();
			out.println("70");
			return;
		}
		playerTurn = false;
		removeThree();
		out.println("done");
	}

	public static boolean isValidInet4Address(String ip)
    {
		String IPV4_PATTERN =
	            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";

	    Pattern pattern = Pattern.compile(IPV4_PATTERN);

	    
	    Matcher matcher = pattern.matcher(ip);
	        return matcher.matches();
	    
    }
	
	private void passTurn() {
		out.println("91");
		cardSetSelected.clear();
		cardSetDraw.clear();
		for (Card card : player1.Hand) card.selected = false;
		opponentCardSetDraw.clear();
		opponentCardSetSelected.clear();
		playerTurn = false;
		mouseH.button4Active = false;
		loading = true;
		myHandCopy = new ArrayList<Card>(player1.Hand);
	}

	private void playCards() {
		infoSent = "";
		String cardIndexSent;
		for(Card card : cardSetSelected) {
			player1.Hand.removeIf(c -> (c.getValue() == card.getValue() && c.getSuitValue() == card.getSuitValue()));
			if(player1.Hand.isEmpty()) {
				out.println("90");
				setupGame();
				return;
			}
			cardIndexSent = Integer.toString(ui.getCardIndex(card));
			if(cardIndexSent.length() == 1) infoSent += "0" + cardIndexSent;
			else infoSent += cardIndexSent;
			System.out.println(infoSent);
		}
		cardSetDraw.clear();
		cardSetDraw = new ArrayList<Card>(cardSetSelected);
		cardSetSelected.clear();
		for (Card card : player1.Hand) card.selected = false;
		opponentCardSetDraw.clear();
		opponentCardSetSelected.clear();
		playerTurn = false;
		mouseH.button1Active = false;
		loading = true;
		out.println(infoSent);
		myHandCopy = new ArrayList<Card>(player1.Hand);
	}

	private void clearSelection() {
			for (Card card : player1.Hand) card.selected = false;
			cardSetSelected.clear();
			mouseH.button2Active = false;
	}

	private void selectCard() {
		int handIndex = mouseH.cardNumberClicked(player1.Hand.size(), mouseH.posClickedX, mouseH.posClickedY) - 1;
		if(handIndex >= 0 && handIndex < player1.Hand.size()) {
			if(player1.Hand.get(handIndex).selected == false) {
				player1.Hand.get(handIndex).selected = true;
				cardSetSelected.add(player1.Hand.get(handIndex));
			}
			else {
				player1.Hand.get(handIndex).selected = false;
				cardSetSelected.removeIf(c -> (c.getValue() == player1.Hand.get(handIndex).getValue() && c.getSuitValue() == player1.Hand.get(handIndex).getSuitValue()));
			}
			mouseH.posClickedX = 0;
			mouseH.posClickedY = 0;
		}
	}
	
	private void removeThree() {
		int count = 4;
		for(Card card : player1.Hand) if (card.getValue() == 3) count--;
		opponentHandNumber -= count;
		player1.Hand.removeIf(card -> (card.getValue() == 3));
	}
	
	private void swapping() {
		int indexSwap1 = 99, indexSwap2 = 99;
		if(cardSetSelected.size() == 2) {
			for (Card card : player1.Hand) {
			if(card.selected == true && indexSwap1 == 99) {
				indexSwap1 = player1.Hand.indexOf(card);
				continue;
			}
			if(card.selected == true) indexSwap2 = player1.Hand.indexOf(card);
		}
		Collections.swap(player1.Hand, indexSwap1, indexSwap2);
		myHandCopy = new ArrayList<Card>(player1.Hand);
		}
		mouseH.button3Active = false;
	}
	
	private void OpponentPlay() {
		opponentCardSetSelected.clear();
		opponentCardSetDraw.clear();
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			infoRecv = in.readLine();
			String[] split = infoRecv.split("(?<=\\G.{" + 2 + "})");
			switch(Integer.parseInt(split[0])) {
				case 90:
					setupGame();
					return;
				case 91:
					break;
				default:
					for(String temp : split) opponentCardSetSelected.add(new Card(Integer.parseInt(temp)));
					Collections.sort(opponentCardSetSelected, new Comparator<Card>() {
						@Override
						public int compare(Card c1, Card c2) {
							if(c1.getValue() == c2.getValue()) return c2.getSuitValue() - c1.getSuitValue();
							return c1.getValue() - c2.getValue();
						}
					});
					opponentHandNumber -= opponentCardSetSelected.size();
					opponentCardSetDraw = new ArrayList<Card>(opponentCardSetSelected);
					break;
			}
			
			playerTurn = true;
			cardSetSelected.clear();
			cardSetDraw.clear();
			loading = false;
			for (Card card : player1.Hand) card.selected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		ui.draw(g2);
		ui.drawMyHand(g2);
		ui.drawOpponentHand(g2, opponentHandNumber);
		ui.drawCardsOnField(g2, cardSetDraw, opponentCardSetDraw);
		g2.dispose();
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}


}
