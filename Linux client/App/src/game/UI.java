package game;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import cards.Card;
import main.UtilityTool;


public class UI {
	private Game game;
	private BufferedImage bg, 
	swap, swapNo, swapPressed, 
	play, playNo, playPressed, 
	pass, passNo, passPressed, 
	unselect, unselectNo, unselectPressed;
	
	
	
	public final int cardScaleX = 100, cardScaleY = 140;
	public final int cardDist = 35;
	public int firstHandCardPosX = 0;
	public final int firstHandCardPosY = 800;
	
	public int firstFieldCardPosX;
	public final int myFieldCardPosY = 500, opponentFieldCardPosY = 300;
	
	public int opponentFirstHandCardPosX = 0;
	public final int opponentFirstHandCardPosY = 20;
	
	public final int selectedCardHigher = 80;
	public final int buttonWidth = 210, buttonHeight = 98;
	public final int button1X, button1Y = 350, 
					 button2X, button2Y = 450, 
					 button3X, button3Y = 550, 
					 button4X, button4Y = 650;
	private Card[] cards = new Card[53];
	UtilityTool uTool = new UtilityTool();
	
	public UI(Game game, int inputPanelWidth) {
		this.game = game;
		button1X = 1500 - inputPanelWidth;
		button2X = 1500 - inputPanelWidth;
		button3X = 1500 - inputPanelWidth;
		button4X = 1500 - inputPanelWidth;
		setupCardImage();
		setupButtons();
	}
	public void setupCardImage() {
		setup(0, 2, "Hearts", 4, "2_of_hearts");
		setup(1, 3, "Hearts", 4, "3_of_hearts");
		setup(2, 4, "Hearts", 4, "4_of_hearts");
		setup(3, 5, "Hearts", 4, "5_of_hearts");
		setup(4, 6, "Hearts", 4, "6_of_hearts");
		setup(5, 7, "Hearts", 4, "7_of_hearts");
		setup(6, 8, "Hearts", 4, "8_of_hearts");
		setup(7, 9, "Hearts", 4, "9_of_hearts");
		setup(8, 10, "Hearts", 4, "10_of_hearts");
		setup(9, 11, "Hearts", 4, "jack_of_hearts");
		setup(10, 12, "Hearts", 4, "queen_of_hearts");
		setup(11, 13, "Hearts", 4, "king_of_hearts");
		setup(12, 14, "Hearts", 4, "ace_of_hearts");
		setup(13, 2, "Diamonds", 3, "2_of_diamonds");
		setup(14, 3, "Diamonds", 3, "3_of_diamonds");
		setup(15, 4, "Diamonds", 3, "4_of_diamonds");
		setup(16, 5, "Diamonds", 3, "5_of_diamonds");
		setup(17, 6, "Diamonds", 3, "6_of_diamonds");
		setup(18, 7, "Diamonds", 3, "7_of_diamonds");
		setup(19, 8, "Diamonds", 3, "8_of_diamonds");
		setup(20, 9, "Diamonds", 3, "9_of_diamonds");
		setup(21, 10, "Diamonds", 3, "10_of_diamonds");
		setup(22, 11, "Diamonds", 3, "jack_of_diamonds");
		setup(23, 12, "Diamonds", 3, "queen_of_diamonds");
		setup(24, 13, "Diamonds", 3, "king_of_diamonds");
		setup(25, 14, "Diamonds", 3, "ace_of_diamonds");
		setup(26, 2, "Clubs", 2, "2_of_clubs");
		setup(27, 3, "Clubs", 2, "3_of_clubs");
		setup(28, 4, "Clubs", 2, "4_of_clubs");
		setup(29, 5, "Clubs", 2, "5_of_clubs");
		setup(30, 6, "Clubs", 2, "6_of_clubs");
		setup(31, 7, "Clubs", 2, "7_of_clubs");
		setup(32, 8, "Clubs", 2, "8_of_clubs");
		setup(33, 9, "Clubs", 2, "9_of_clubs");
		setup(34, 10, "Clubs", 2, "10_of_clubs");
		setup(35, 11, "Clubs", 2, "jack_of_clubs");
		setup(36, 12, "Clubs", 2, "queen_of_clubs");
		setup(37, 13, "Clubs", 2, "king_of_clubs");
		setup(38, 14, "Clubs", 2, "ace_of_clubs");
		setup(39, 2, "Spades", 1, "2_of_spades");
		setup(40, 3, "Spades", 1, "3_of_spades");
		setup(41, 4, "Spades", 1, "4_of_spades");
		setup(42, 5, "Spades", 1, "5_of_spades");
		setup(43, 6, "Spades", 1, "6_of_spades");
		setup(44, 7, "Spades", 1, "7_of_spades");
		setup(45, 8, "Spades", 1, "8_of_spades");
		setup(46, 9, "Spades", 1, "9_of_spades");
		setup(47, 10, "Spades", 1, "10_of_spades");
		setup(48, 11, "Spades", 1, "jack_of_spades");
		setup(49, 12, "Spades", 1, "queen_of_spades");
		setup(50, 13, "Spades", 1, "king_of_spades");
		setup(51, 14, "Spades", 1, "ace_of_spades");
		setup(52, 0, "None", 0 , "cover");
	}
	

	
	public void setupButtons() {
		swap = buttons("Swap");
		swapNo = buttons("SwapNo");
		swapPressed = buttons("SwapPressed");
		pass = buttons("Pass");
		passNo = buttons("PassNo");
		passPressed = buttons("PassPressed");
		play = buttons("Play");
		playNo = buttons("PlayNo");
		playPressed = buttons("PlayPressed");
		unselect = buttons("Unselect");
		unselectNo = buttons("UnselectNo");
		unselectPressed = buttons("UnselectPressed");
	}
	public BufferedImage buttons(String fileName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/buttons/" + fileName + ".png"));
			image = uTool.scaleImage(image, buttonWidth, buttonHeight);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	public int getCardIndex(Card card) {
		switch(card.getSuit()) {
		case "Hearts":
			return card.getValue() - 2;
		case "Diamonds":
			return card.getValue() + 11;
		case "Clubs":
			return card.getValue() + 24;
		case "Spades":
			return card.getValue() + 37;
		default:
			return 99;
		}
		
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(bg, 0, 0, null);
		if(game.loading == false) {
		if(game.cardSetSelected.size() >= 1 && game.mouseH.button1Pressed == true && game.cmbCh.validToPlay(game.cardSetSelected, game.opponentCardSetSelected))
			g2.drawImage(playPressed, button1X, button1Y, null);
		else if(game.cardSetSelected.size() >= 1 && game.cmbCh.validToPlay(game.cardSetSelected, game.opponentCardSetSelected)) g2.drawImage(play, button1X, button1Y, null);
		else g2.drawImage(playNo, button1X, button1Y, null);
		}
		else g2.drawImage(playNo, button1X, button1Y, null);
		
		if(game.loading == false) {
		if(game.cardSetSelected.size() >= 1 && game.mouseH.button2Pressed == true)
			g2.drawImage(unselectPressed, button2X, button2Y, null);
		else if(game.cardSetSelected.size() >= 1) g2.drawImage(unselect, button2X, button2Y, null);
		else g2.drawImage(unselectNo, button2X, button2Y, null);
		}
		else g2.drawImage(unselectNo, button2X, button2Y, null);
		
		if(game.loading == false) {
		if(game.cardSetSelected.size() == 2 && game.mouseH.button3Pressed == true) 
			g2.drawImage(swapPressed, button3X, button3Y, null);
		else if(game.cardSetSelected.size() == 2) g2.drawImage(swap, button3X, button3Y, null);
		else g2.drawImage(swapNo, button3X, button3Y, null);
		}
		else g2.drawImage(swapNo, button3X, button3Y, null);
		
		if(game.loading == false) {
		if(game.mouseH.button4Pressed == true && game.playerTurn == true && !game.opponentCardSetSelected.isEmpty()) 
			g2.drawImage(passPressed, button4X, button4Y, null);
		else if(game.playerTurn == true && !game.opponentCardSetSelected.isEmpty()) g2.drawImage(pass, button4X, button4Y, null);
		else g2.drawImage(passNo, button4X, button4Y, null);
		}
		else g2.drawImage(passNo, button4X, button4Y, null);
	}
	
	public void drawMyHand(Graphics2D g2) {
		int i = 0;
		firstHandCardPosX = (game.screenWidth - cardDist * (game.myHandCopy.size() - 1) - cardScaleX) / 2;
		for(Card card : game.myHandCopy) {
			if(card.selected == false) g2.drawImage(cards[getCardIndex(card)].getImage(), firstHandCardPosX + i, firstHandCardPosY, null);
			else g2.drawImage(cards[getCardIndex(card)].getImage(), firstHandCardPosX + i, firstHandCardPosY - selectedCardHigher, null);
			i += cardDist;
		}
		
	}
	
	public void drawOpponentHand(Graphics2D g2, int handNumber) {
		int i;
		opponentFirstHandCardPosX = (game.screenWidth - cardDist * (handNumber - 1) - cardScaleX)/2;
		for (i = 0; i < handNumber; ++i) 
			g2.drawImage(cards[52].getImage(), opponentFirstHandCardPosX + i * cardDist, opponentFirstHandCardPosY, null);
	}
	
	public void drawCardsOnField(Graphics2D g2, ArrayList<Card> myFieldCards, ArrayList<Card> opponentFieldCards) {
		int i = 0;
		if(!myFieldCards.isEmpty()) {
		firstFieldCardPosX = (game.screenWidth - cardDist * (myFieldCards.size() - 1) - cardScaleX)/2;
		for(Card card : myFieldCards) {
			g2.drawImage(cards[getCardIndex(card)].getImage(), firstFieldCardPosX + i, myFieldCardPosY, null);
			i += cardDist;
		}
		}
		if(!opponentFieldCards.isEmpty()) {
		i = 0;
		firstFieldCardPosX = (game.screenWidth - cardDist * (opponentFieldCards.size() - 1) - cardScaleX)/2;
		for(Card card : opponentFieldCards) {
			g2.drawImage(cards[getCardIndex(card)].getImage(), firstFieldCardPosX + i, opponentFieldCardPosY, null);
			i += cardDist;
		}
		}
	}
	public void setup(int index, int value, String suit, int suitValue, String imageName) {
		try {
			cards[index] = new Card(value, suit, suitValue);
			cards[index].setImage(ImageIO.read(getClass().getResourceAsStream("/cards/" + imageName + ".png")));
			cards[index].setImage(uTool.scaleImage(cards[index].getImage(), cardScaleX , cardScaleY));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
