package de.static_interface.shadow.adventurirc.gui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

import de.static_interface.shadow.adventurirc.io.FileManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SetupFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	
	public SetupFrame()
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 184);
		getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(115, 116, 211, 27);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnVerbinden = new JButton("Verbinden");
		btnVerbinden.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				FileManager.setString(FileManager.CFG_NICKNAME, getCleanString(textField.getText()));
				dispose();
			}
		});
		btnVerbinden.setBounds(342, 116, 98, 27);
		getContentPane().add(btnVerbinden);
		
		JButton btnBeenden = new JButton("Beenden");
		btnBeenden.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		btnBeenden.setBounds(13, 116, 90, 27);
		getContentPane().add(btnBeenden);
		
		JLabel lblWillkommen = new JLabel("<html><b>Willkommen bei AdventurIRC !");
		lblWillkommen.setBounds(13, 6, 187, 15);
		getContentPane().add(lblWillkommen);
		
		JLabel lblChat = new JLabel("<html>Um mit dem Chatten zu beginnen, trage einfach deinen Nicknamen hier ein ! (Möglich sind alle Zeichen von A-Z, 1-9 sowie der Unterstrich _)");
		lblChat.setBounds(13, 33, 427, 71);
		getContentPane().add(lblChat);
	}
	
	private static String getCleanString(String toClean)
	{
		String cleanString = "";
		for ( char c : toClean.toCharArray() )
		{
			if ( "!\"§$%&/()=?`´ß}][{¬½¼³²¹^¬⅛£¤\\⅜⅝⅞™±°¿¸¯`^+#ä~`^*Ä'˝¨ÖÜ÷×ºM;:-.,µ€@ΩŁ€®Ŧ¥↑ıØÞ¨ÆẞÐªŊĦ&Ł›‹©‚‘’º¦@ł€¶ŧ←↓→øþłĸŋđðſæ»«¢„“”µ·…–".contains(""+c) ) continue;
			else cleanString = cleanString+c;	
		}
		return cleanString;
	}
}
