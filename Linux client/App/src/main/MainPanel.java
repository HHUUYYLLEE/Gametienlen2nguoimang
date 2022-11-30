package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MainPanel extends JPanel{
		private BufferedImage bg;
		UtilityTool uTool = new UtilityTool();
		public MainPanel() {
		
			try {
				bg = ImageIO.read(getClass().getResourceAsStream("/background/bg.png"));
				bg = uTool.scaleImage(bg, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		        g.drawImage(bg, 0, 0, null);
		}
	}

