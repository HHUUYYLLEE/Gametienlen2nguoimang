package game;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public class MouseHandler implements MouseListener{
	private Game game;
	public int posClickedX = 0, posClickedY = 0, 
			   posPressedX = 0, posPressedY = 0;
	
	public boolean button1Pressed = false, button2Pressed = false, button3Pressed = false, button4Pressed = false;
	public boolean button1Active = false, button2Active = false, button3Active = false, button4Active = false;

	public MouseHandler(Game game) {
		this.game = game;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		posClickedX = e.getX();
		posClickedY = e.getY();
		if(game.loading == false) {
			
		if(posClickedX >= game.ui.button1X && posClickedX <= game.ui.button1X + game.ui.buttonWidth && posClickedY >= game.ui.button1Y && posClickedY <= game.ui.button1Y + game.ui.buttonHeight)
			button1Active = true;
		if(posClickedX >= game.ui.button2X && posClickedX <= game.ui.button2X + game.ui.buttonWidth && posClickedY >= game.ui.button2Y && posClickedY <= game.ui.button2Y + game.ui.buttonHeight)
			button2Active = true;
		if(posClickedX >= game.ui.button3X && posClickedX <= game.ui.button3X + game.ui.buttonWidth && posClickedY >= game.ui.button3Y && posClickedY <= game.ui.button3Y + game.ui.buttonHeight)
			button3Active = true;
		if(posClickedX >= game.ui.button4X && posClickedX <= game.ui.button4X + game.ui.buttonWidth && posClickedY >= game.ui.button4Y && posClickedY <= game.ui.button4Y + game.ui.buttonHeight)
			button4Active = true;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		posPressedX = e.getX();
		posPressedY = e.getY();
		if(game.loading == false) {
			
		if(posPressedX >= game.ui.button1X && posPressedX <= game.ui.button1X + game.ui.buttonWidth && posPressedY >= game.ui.button1Y && posPressedY <= game.ui.button1Y + game.ui.buttonHeight)
			button1Pressed = true;
		if(posPressedX >= game.ui.button2X && posPressedX <= game.ui.button2X + game.ui.buttonWidth && posPressedY >= game.ui.button2Y && posPressedY <= game.ui.button2Y + game.ui.buttonHeight)
			button2Pressed = true;
		if(posPressedX >= game.ui.button3X && posPressedX <= game.ui.button3X + game.ui.buttonWidth && posPressedY >= game.ui.button3Y && posPressedY <= game.ui.button3Y + game.ui.buttonHeight)
			button3Pressed = true;
		if(posPressedX >= game.ui.button4X && posPressedX <= game.ui.button4X + game.ui.buttonWidth && posPressedY >= game.ui.button4Y && posPressedY <= game.ui.button4Y + game.ui.buttonHeight)
			button4Pressed = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		posClickedX = 0;
		posClickedY = 0;
		posPressedX = 0;
		posPressedY = 0;
		button1Pressed = false;
		button2Pressed = false;
		button3Pressed = false;
		button4Pressed = false;
		button1Active = false;
		button2Active = false;
		button3Active = false;
		button4Active = false;
	}
	
	public int cardNumberClicked(int handNumber, int posClickedX, int posClickedY) {
		if(posClickedY >= game.ui.firstHandCardPosY && posClickedY <= game.ui.firstHandCardPosY + game.ui.cardScaleY) {
			if(posClickedX > (handNumber - 1) * game.ui.cardDist + game.ui.firstHandCardPosX && posClickedX <= handNumber * game.ui.cardDist + game.ui.firstHandCardPosX + game.ui.cardScaleX) 
				return handNumber;
			else return ((int)((posClickedX - game.ui.firstHandCardPosX) / game.ui.cardDist) + 1);
		}
		if(posClickedY >= game.ui.firstHandCardPosY - game.ui.selectedCardHigher && posClickedY < game.ui.firstHandCardPosY && posClickedX >= game.ui.firstHandCardPosX && posClickedX < game.ui.firstHandCardPosX + (handNumber - 1) * game.ui.cardDist + game.ui.cardScaleX) {
			int left = 0, right = handNumber - 1, i = 0;
			while(posClickedX > left * game.ui.cardDist + game.ui.firstHandCardPosX + game.ui.cardScaleX && left <= handNumber - 2) left++;
			while(posClickedX < right * game.ui.cardDist + game.ui.firstHandCardPosX) right--;
			left++;
			right++;
			for(i = right; i >= left; --i) if(game.player1.Hand.get(i - 1).selected == true) return i;
		}
		return 99;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
