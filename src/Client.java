
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class Client extends Frame implements ActionListener, MouseListener{
	private CMClientStub m_ClientStub;
	private ClientEventHandler m_CEventHandler;
	
	private List roomList, userList, roomNumList;
	private TextField tf;
	private Button createButton;
	private String id;
	private Room room;
	private TeamChat tchat;
	private GUI gui;
	private GUI2 gui2;
	
	public CMClientStub getM_ClientStub() {
		return m_ClientStub;
	}

	public void setM_ClientStub(CMClientStub m_ClientStub) {
		this.m_ClientStub = m_ClientStub;
	}

	public ClientEventHandler getM_CEventHandler() {
		return m_CEventHandler;
	}

	public void setM_CEventHandler(ClientEventHandler m_CEventHandler) {
		this.m_CEventHandler = m_CEventHandler;
	}

	public List getRoomList() {
		return roomList;
	}

	public void setRoomList(List roomList) {
		this.roomList = roomList;
	}

	public List getRoomNumList() {
		return roomNumList;
	}
	
	public List getUserList() {
		return userList;
	}

	public void setUserList(List userList) {
		this.userList = userList;
	}

	public TextField getTf() {
		return tf;
	}

	public void setTf(TextField tf) {
		this.tf = tf;
	}

	public Button getCreateButton() {
		return createButton;
	}

	public void setCreateButton(Button createButton) {
		this.createButton = createButton;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public TeamChat getTchat() {
		return tchat;
	}

	public void setTchat(TeamChat tchat) {
		this.tchat = tchat;
	}

	public GUI getGui() {
		return gui;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public GUI2 getGui2() {
		return gui2;
	}

	public void setGui2(GUI2 gui2) {
		this.gui2 = gui2;
	}
	
	public Client(String id)
	{
		super(id + "Welcome~~");
		this.id = id;
		
		m_ClientStub = new CMClientStub();
		m_CEventHandler = new ClientEventHandler(m_ClientStub, this);
				
		roomList = new List();
		userList = new List();
		roomNumList = new List();
		tf = new TextField();
		createButton = new Button("create");
		createButton.addActionListener(this);
		roomList.addMouseListener(this);
		Panel p1 = new Panel();
		p1.setLayout(new GridLayout(1, 3));
		Panel p2 = new Panel();
		p2.setLayout(new BorderLayout());

		p1.add(roomList);
		p1.add(roomNumList);
		p1.add(userList);

		p2.add(tf);
		p2.add(createButton, "East");
		add(p1);
		add(p2, "South");
		addWindowListener(new Exit());
	}
	
	public void actionPerformed(ActionEvent e) {

		String name = tf.getText();
		System.out.println("rn" + name);
		if (name.equals(""))
			return;

		try {
			sendMsg("checkroomname/" + id + "/" + name);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void makeRoom(String rName) throws Exception {
		setVisible(false);
		dispose();
		room = new Room(id, rName, this);
		room.setBounds(200, 200, 400, 300);
		room.setVisible(true);
		sendMsg("mkroom/" + id + "/" + rName);
	}
	
	public void setGUI2(GUI2 gui2) {
		this.gui2 = gui2;
	}
	
	public void sendMsg(String Msg) {
		CMDummyEvent due = new CMDummyEvent();
		due.setDummyInfo(Msg);
		m_ClientStub.send(due, "SERVER");
	}
	
	public void joinRoom(String rName) {
		setVisible(false);
		dispose();
		room = new Room(id, rName, this);
		room.setBounds(200, 200, 400, 300);
		room.setVisible(true);
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2)
			try {
				if (roomList.getItemCount() == 0)
					return;
				String roomname[] = roomList.getSelectedItem().split("/");
				System.out.println(roomname[0]);

				sendMsg("roomjoin/" + id + "/" + roomname[0]);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	}
	
	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
	
	class Exit extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
				sendMsg("logout/" + id);
		
				dispose();

				System.exit(0);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public CMClientStub getCStub() {
		return m_ClientStub;
	}
	
	public ClientEventHandler getClientEventHandler() {
		return m_CEventHandler;
	}
	
	public static void main(String[] args)
	{
		Random rc = new Random();
		
		Client client = new Client("user" + rc.nextInt(9999));
		client.getCStub().setAppEventHandler(client.getClientEventHandler());
		client.getCStub().startCM();
		client.getCStub().loginCM(client.id, "");
		client.setBounds(200, 200, 400, 300);
		client.setVisible(true);
		client.sendMsg("enter/" + client.id);
	}
}