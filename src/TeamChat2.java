
import java.awt.Button;
import java.awt.event.ActionEvent;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;

public class TeamChat2 extends TeamChat {

	Button b;
	boolean flag;

	TeamChat2(String name, String rName, String team, Client client, Room room) {
		super(name, rName, team, client, room);

		b = new Button("Watch");
		p2.add(b, "East");
		b.addActionListener(this);
		flag = false;
	}

	// 오목판 가져오기 관전하는사람들이
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b) {
			System.out.println("asd1");
			if (flag == false) {
				flag = true;
				System.out.println("asd2");
				GUI2 gui2 = new GUI2(id, this);
				client.setGUI2(gui2);
				try {
					client.sendMsg("stack/" + id + "/" + rName);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if (e.getSource() == tf) {
			try {
				if (tf.getText().trim().length() > 0) {
					client.sendMsg("tmsg/" + id + "/" + rName + "/" + tf.getText() + "/" + team);
					tf.setText("");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean flag() {
		return flag;
	}

	public void changeFlag() {
		flag = !flag;
		System.out.println(flag);
	}
}