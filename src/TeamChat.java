
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamChat extends Frame implements ActionListener {

	List list;
	TextField tf;
	TextArea ta;
	Client client;
	Room room;
	String id;
	String rName;
	String team;
	Panel p1, p2;

	TeamChat(String name, String rName, String team, Client client, Room room) {
		super(name + "'s Team Chat");
		this.client = client;
		this.room = room;
		this.rName = rName;
		this.team = team;
		id = name;
		ta = new TextArea();
		ta.setBackground(Color.WHITE);
		ta.setForeground(Color.black);
		list = new List();
		tf = new TextField();

		p1 = new Panel();
		p1.setLayout(new BorderLayout());
		p2 = new Panel();
		p2.setLayout(new BorderLayout());

		p1.add(ta);
		p1.add(list, "East");
		p2.add(tf);

		add(p1);
		add(p2, "South");
		tf.addActionListener(this);
	}

	public String team()
	{
		return team;
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tf) {
			try {
				if (tf.getText().trim().length() > 0) {
					String msg = tf.getText();
					if(msg.equals("/f")) {
						client.sendMsg("surrender/" + id + "/" + rName + "//" + team);
					}
					else {
						client.sendMsg("tmsg/" + id + "/" + rName + "/" + msg + "/" + team);
					}
					tf.setText("");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}