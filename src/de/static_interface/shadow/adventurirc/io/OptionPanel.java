package de.static_interface.shadow.adventurirc.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import static de.static_interface.shadow.adventurirc.io.FileManager.CFG_CHAT_OUTPUT_FORMAT;
import static de.static_interface.shadow.adventurirc.io.FileManager.CFG_DOBEEP;
import static de.static_interface.shadow.adventurirc.io.FileManager.CFG_DOWNLOAD_UPDATES;
import static de.static_interface.shadow.adventurirc.io.FileManager.CFG_LOG_CHAT_OUTPUT;
import static de.static_interface.shadow.adventurirc.io.FileManager.CFG_NICKNAME;
import static de.static_interface.shadow.adventurirc.io.FileManager.CFG_TIME_FORMAT_CHAT;
import static de.static_interface.shadow.adventurirc.io.FileManager.CFG_TIME_FORMAT_LOG;

public class OptionPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private HashMap<String, String> settings = new HashMap<String, String>();

	public OptionPanel()
	{
		setLayout(null);
		addToSettings(CFG_NICKNAME);
		addToSettings(CFG_DOBEEP);
		addToSettings(CFG_TIME_FORMAT_LOG);
		addToSettings(CFG_TIME_FORMAT_CHAT);
		addToSettings(CFG_CHAT_OUTPUT_FORMAT);
		addToSettings(CFG_LOG_CHAT_OUTPUT);
		addToSettings(CFG_DOWNLOAD_UPDATES);

		TextFieldPanel nick = new TextFieldPanel(CFG_NICKNAME, settings.get(CFG_NICKNAME), "Dies ist dein Nickname.");
		CheckboxPanel dobeep = new CheckboxPanel(CFG_DOBEEP, settings.get(CFG_DOBEEP), "Soll ein Ton ertönen, wenn dein Nickname erwähnt wird ?");
		TextFieldPanel time_format_log = new TextFieldPanel(CFG_TIME_FORMAT_LOG, settings.get(CFG_TIME_FORMAT_LOG), "Wie soll die Zeitangabe in den Logs formattiert werden ?");
		TextFieldPanel time_format_chat = new TextFieldPanel(CFG_TIME_FORMAT_CHAT, settings.get(CFG_TIME_FORMAT_CHAT), "Wie soll die Zeitangabe in der Chatausgabe formattiert werden ?");
		TextFieldPanel chat_format = new TextFieldPanel(CFG_CHAT_OUTPUT_FORMAT, settings.get(CFG_CHAT_OUTPUT_FORMAT), "Wie soll die Chatausgabe formattiert werden ?");
		CheckboxPanel log_chat = new CheckboxPanel(CFG_LOG_CHAT_OUTPUT, settings.get(CFG_LOG_CHAT_OUTPUT), "Soll der Chat-Output geloggt werden ? (Fehlermeldungen werden immer geloggt)");
		DropdownPanel download = new DropdownPanel(CFG_DOWNLOAD_UPDATES, settings.get(CFG_DOWNLOAD_UPDATES), "Wie soll bei einem Update vorgegangen werden ? (YES bedeutet, dass es sofort geladen wird, NO bedeutet, dass Updates ignoriert werden, bei PROMPT wird eine Meldung angezeigt.", "YES", "NO", "PROMPT");

		time_format_chat.setBounds(5, 290, 640, 90);
		time_format_log.setBounds(5, 195, 640, 90);
		chat_format.setBounds(5, 385, 640, 90);
		log_chat.setBounds(5, 480, 640, 90);
		download.setBounds(5, 574, 640, 90);
		dobeep.setBounds(5, 100, 640, 90);
		nick.setBounds(5, 5, 640, 90);

		add(time_format_chat);
		add(time_format_log);
		add(chat_format);
		add(log_chat);
		add(download);
		add(dobeep);
		add(nick);

		setLayout(null);
	}

	private void addToSettings(String key)
	{
		settings.put(key, FileManager.getString(key));
	}
}
abstract class SettingContainer extends JPanel
{
	private static final long serialVersionUID = -2744408582668860189L;
	String key, value, comment;

	public SettingContainer(String key, String value, String comment)
	{
		this.key = key;
		this.value = value;
		this.comment = comment;
		setLayout(null);
	}

	void saveNewValue(String key, String value)
	{
		FileManager.setString(key, value);
	}
}
class DropdownPanel extends SettingContainer
{
	private static final long serialVersionUID = 1L;

	private JComboBox<String> options;
	private JScrollPane contentTopPane;
	private JPanel contentPane;
	private JLabel commentLabel;

	public DropdownPanel(String key_, String value_, String comment_, String... possibleChoices)
	{
		super(key_, value_, comment_);
		setLayout(null);

		options = new JComboBox<String>(possibleChoices);
		commentLabel = new JLabel("<html>"+comment);
		contentTopPane = new JScrollPane();
		contentPane = new JPanel();

		if ( !Arrays.asList(possibleChoices).contains(value) ) options.addItem(value);
		options.setSelectedItem(value);

		contentTopPane.setBounds(0, 0, 640, 70);
		commentLabel.setBounds(0, 0, 640, 40);
		contentPane.setBounds(0, 0, 640, 70);
		options.setBounds(0, 45, 640, 25);
		contentPane.setLayout(null);

		options.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveNewValue(key, String.valueOf(options.getSelectedItem()));
			}
		});

		contentPane.add(options);
		contentPane.add(commentLabel);
		contentTopPane.setViewportView(contentPane);
		add(contentTopPane);
	}
}
class TextFieldPanel extends SettingContainer
{
	private static final long serialVersionUID = 1L;

	private JScrollPane contentTopPane;
	private JTextField optionField;
	private JLabel commentLabel;
	private JPanel contentPane;

	public TextFieldPanel(String key_, String value_, String comment_)
	{
		super(key_, value_, comment_);

		commentLabel = new JLabel("<html>"+comment);
		contentTopPane = new JScrollPane();
		optionField = new JTextField(value_);
		contentPane = new JPanel();

		contentTopPane.setBounds(0, 0, 640, 70);
		commentLabel.setBounds(0, 0, 640, 40);
		optionField.setBounds(0, 45, 640, 25);
		contentPane.setBounds(0, 0, 640, 70);
		contentPane.setLayout(null);

		optionField.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				saveNewValue(key, optionField.getText().trim());
			}
		});

		contentPane.add(optionField);
		contentPane.add(commentLabel);
		contentTopPane.setViewportView(contentPane);
		add(contentTopPane);
	}
}
class CheckboxPanel extends SettingContainer
{
	private static final long serialVersionUID = 1L;

	private JScrollPane contentTopPane;
	private JCheckBox optionBox;
	private JLabel commentLabel;
	private JPanel contentPane;

	public CheckboxPanel(String key_, String value_, String comment_)
	{
		super(key_, value_, comment_);

		commentLabel = new JLabel("<html>"+comment);
		contentTopPane = new JScrollPane();
		optionBox = new JCheckBox();
		contentPane = new JPanel();

		contentTopPane.setBounds(0, 0, 640, 70);
		commentLabel.setBounds(0, 0, 640, 40);
		contentPane.setBounds(0, 0, 640, 70);
		optionBox.setBounds(0, 45, 640, 25);
		contentPane.setLayout(null);

		optionBox.setSelected(FileManager.getString(key_).equals(true) ? true : false);
		optionBox.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveNewValue(key, String.valueOf(optionBox.isSelected()));
			}
		});

		contentTopPane.setViewportView(contentPane);
		contentPane.add(commentLabel);
		contentPane.add(optionBox);
		add(contentTopPane);
	}
}