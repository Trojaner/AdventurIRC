package de.static_interface.shadow.adventurirc;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import de.static_interface.shadow.adventurirc.io.FileManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Bootstrap extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	
	public static void main(String[] args)
	{
		if ( FileManager.getString(FileManager.CFG_NICKNAME) != null )
		{
			new Thread(new AdventurIRC()).start();
			return;
		}
		else
		{
			new Bootstrap().setVisible(true);
			return;
		}
	}
	
	public Bootstrap()
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 302, 150);
		getContentPane().setLayout(null);
		
		JLabel lblTrageHierEinfach = new JLabel("Trage hier einfach deinen Nicknamen ein:");
		lblTrageHierEinfach.setBounds(10, 12, 281, 15);
		getContentPane().add(lblTrageHierEinfach);
		
		textField = new JTextField();
		textField.setBounds(10, 42, 280, 25);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnLosGehts = new JButton("und los geht's !");
		btnLosGehts.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ( textField.getText().trim().equals("") ) return;
				if ( Character.isDigit(textField.getText().charAt(0)) );
				FileManager.setString(FileManager.CFG_NICKNAME, textField.getText().trim());
				main(null);
				dispose();
				return;
			}
		});
		btnLosGehts.setBounds(10, 69, 280, 40);
		getContentPane().add(btnLosGehts);
	}
}
