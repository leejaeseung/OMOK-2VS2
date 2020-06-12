
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Room extends Frame implements ActionListener {

	List list;
	TextField tf;
	TextArea ta;
	Button exitButton, blackButton, whiteButton, watchButton, readyButton;
	Client client;
	String rName;
	String id;
	// 0 d 1 b 2 w

	Room(String name, String rName, Client client) {
		super(name + "'s Room" + "  Room Name: " + rName);
		this.rName = rName;
		this.client = client;
		id = name;
		ta = new TextArea();
		//ta.enable(false);
		ta.setBackground(Color.WHITE);
		ta.setForeground(Color.black);
		list = new List();
		tf = new TextField();
		exitButton = new Button("Exit");
		blackButton = new Button("Black");
		whiteButton = new Button("White");
		watchButton = new Button("Watch");
		readyButton = new Button("Ready");

		Panel textPanel = new Panel();
		textPanel.setLayout(new GridLayout(1, 2));
		Panel BWDbuttons = new Panel();
		BWDbuttons.setLayout(new GridLayout(1, 3));
		Panel inputPanel = new Panel();
		inputPanel.setLayout(new GridLayout(4, 1));
		
		BWDbuttons.add(blackButton);
		BWDbuttons.add(watchButton);
		BWDbuttons.add(whiteButton);

		textPanel.add(ta);
		textPanel.add(list, "East");
		
		inputPanel.add(tf);
		inputPanel.add(readyButton);
		inputPanel.add(BWDbuttons);
		inputPanel.add(exitButton);

		add(textPanel);
		add(inputPanel, "South");
		tf.addActionListener(this);
		readyButton.addActionListener(this);
		exitButton.addActionListener(this);
		blackButton.addActionListener(this);
		whiteButton.addActionListener(this);
		watchButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitButton) {
			try {
				client.sendMsg("roomout/" + id + "/" + rName);
				client.dispose();
				dispose();
				setVisible(false);
				client.setVisible(true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == blackButton) {
			try {
				client.sendMsg("changeteam/" + id + "/" + rName + "//black/");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == whiteButton) {
			try {
				client.sendMsg("changeteam/" + id + "/" + rName + "//white/");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else if (e.getSource() == watchButton) {
			try {
				client.sendMsg("changeteam/" + id + "/" + rName + "//watch/");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == readyButton) {
			try {
				client.sendMsg("ready/" + id + "/" + rName);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getSource() == tf) {
			try {
				if (tf.getText().trim().length() > 0) {
					client.sendMsg("msg/" + id + "/" + rName + "/" + tf.getText());
					tf.setText("");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
