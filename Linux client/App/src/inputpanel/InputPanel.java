package inputpanel;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

import game.Game;

public class InputPanel extends JPanel{
	Game game;
		
	public InputPanel(Game game, int inputPanelWidth, int inputPanelHeight) {
		this.game = game;
		final JLabel IP = new JLabel("IP:");
		IP.setFont(new Font(IP.getFont().getName(),IP.getFont().getStyle(), 30));
		IP.setForeground(Color.YELLOW);
		
		final JLabel portNo = new JLabel("Port number:");
		portNo.setFont(new Font(portNo.getFont().getName(),portNo.getFont().getStyle(), 27));
		portNo.setForeground(Color.YELLOW);
		
		final JTextField IPInput = new JTextField("127.0.0.1", 15);
		IPInput.setFont(new Font(IPInput.getFont().getName(),Font.ITALIC, 20));
		IPInput.setHorizontalAlignment(JTextField.CENTER);
		IPInput.setForeground(Color.GREEN);
		IPInput.setBackground(Color.DARK_GRAY);
		IPInput.setBorder(javax.swing.BorderFactory.createEmptyBorder());
//		IPInput.setOpaque(false);
		((AbstractDocument)IPInput.getDocument()).setDocumentFilter(new IPInputFilter(15));
		
		final JTextField portNoInput = new JTextField("8000",10);
		portNoInput.setFont(new Font(portNoInput.getFont().getName(),Font.ITALIC, 30));
		portNoInput.setHorizontalAlignment(JTextField.CENTER);
		portNoInput.setForeground(Color.GREEN);
		portNoInput.setBackground(Color.DARK_GRAY);
		portNoInput.setBorder(BorderFactory.createEmptyBorder());
//		portNoInput.setOpaque(false);
		((AbstractDocument)portNoInput.getDocument()).setDocumentFilter(new PortNumberInputFilter(4));
		
		final JButton connect = new JButton("Connect");
		connect.setForeground(Color.YELLOW);
		connect.setBackground(new Color(102,51,0));
		connect.setFocusPainted(false);
		connect.setBorder(BorderFactory.createEmptyBorder());
//		connect.setOpaque(true);
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				game.IP = IPInput.getText();
				game.portNo = Integer.parseInt(portNoInput.getText());
				game.tryConnecting = true;
			}
			
		});
		
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        this.setPreferredSize(new Dimension(inputPanelWidth, inputPanelHeight));
        this.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(IP, gbc);
        
        gbc.gridx = 1;
        gbc.ipadx = 200;
        gbc.ipady = 60;
        this.add(IPInput,gbc);
        
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        this.add(portNo,gbc);
        
        gbc.gridx = 1;
        gbc.ipadx = 200;
        gbc.ipady = 60;
        this.add(portNoInput,gbc);
        
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.ipadx = 50;
        gbc.ipady = 20;
        this.add(connect,gbc);
        
//		this.setOpaque(false);
		this.setVisible(true);
		
	}
}
