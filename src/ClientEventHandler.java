import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

public class ClientEventHandler implements CMAppEventHandler {
	private CMClientStub m_clientStub;
	private Client m_client;
	
	public ClientEventHandler(CMClientStub clientStub, Client client) {
		// TODO Auto-generated constructor stub
		m_clientStub = clientStub;
		m_client = client;
	}
	
	@Override
	public void processEvent(CMEvent cme) {
		// TODO Auto-generated method stub
		
		
		
		switch(cme.getType()) {
		case CMInfo.CM_DUMMY_EVENT:
			try {
				processDummyEvent(cme);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case CMInfo.CM_SESSION_EVENT:
			try {
				
				processSessionEvent(cme);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		default:
			return;
		}
	}
	private void processSessionEvent(CMEvent cme) throws Exception{
		CMSessionEvent se = (CMSessionEvent)cme;
		
		switch(se.getID()) {
		case CMSessionEvent.LOGIN_ACK:
			if(se.isValidUser() != 1) {
				m_client.login(m_client);
			}
			else {
				m_client.setBounds(200, 200, 400, 300);
				m_client.setVisible(true);
				m_client.sendMsg("enter/" + m_client.getId());
			}
		break;
		}
	}
	
	private void processDummyEvent(CMEvent cme) throws Exception {
		CMDummyEvent due = (CMDummyEvent)cme;
		String line = due.getDummyInfo();
		
		String[] array = line.split("/");
		int len;
		switch (array[0]) {
		case "list":
			m_client.getUserList().removeAll();
			len = array.length;
			for (int i = 1; i < len; i++)
				m_client.getUserList().add(array[i]);
			break;

		case "roomlist":
			m_client.getRoomList().removeAll();
			m_client.getRoomNumList().removeAll();
			len = array.length;
			for (int i = 1; i < len; i++) {
					if(i % 2 == 1)
						m_client.getRoomList().add(array[i]);
					else
						m_client.getRoomNumList().add(array[i] + "/6");
			}
			break;

		case "teamlist":
			m_client.getTchat().list.removeAll();
			len = array.length;
			for (int i = 1; i < len; i++)
				m_client.getTchat().list.add(array[i]);
			break;

		case "roommember":
			m_client.getRoom().list.removeAll();
			len = array.length;
			//System.out.println(m_client.getId() + " size!!:" + len);
			for (int i = 1; i < len; i++) {
				//System.out.println(array[i]);
				m_client.getRoom().list.add(array[i]);
			}
			break;

		/*case "roomout":
			//m_client.getRoom().list.remove(array[1]);
			break;*/

		case "msg":
			m_client.getRoom().ta.append("[" + array[1] + "]" + array[2] + "\n");
			break;

		case "tmsg":
			m_client.getTchat().ta.append("[" + array[1] + "]" + array[2] + "\n");
			break;

		case "gamestartB":
			m_client.getRoom().setVisible(false);
			m_client.getRoom().dispose();
			m_client.setTchat(new TeamChat(m_client.getId(), m_client.getRoom().rName, "black", m_client, m_client.getRoom()));
			m_client.setGui(new GUI(m_client.getId(), m_client, m_client.getRoom().rName, "black", m_client.getTchat()));
			m_client.getTchat().setBounds(200, 200, 400, 300);
			m_client.getTchat().setVisible(true);
			break;

		case "gamestartW":
			m_client.getRoom().setVisible(false);
			m_client.getRoom().dispose();
			m_client.setTchat(new TeamChat(m_client.getId(), m_client.getRoom().rName, "white", m_client, m_client.getRoom()));
			m_client.setGui(new GUI(m_client.getId(), m_client, m_client.getRoom().rName, "white", m_client.getTchat()));
			m_client.getTchat().setBounds(200, 200, 400, 300);
			m_client.getTchat().setVisible(true);
			break;

		case "gamestartD":
			m_client.getRoom().setVisible(false);
			m_client.getRoom().dispose();
			m_client.setTchat(new TeamChat2(m_client.getId(), m_client.getRoom().rName, "watch", m_client, m_client.getRoom()));
			m_client.setGui(new GUI(m_client.getId(), m_client, m_client.getRoom().rName, "watch", m_client.getTchat()));
			m_client.getTchat().setBounds(200, 200, 400, 300);
			m_client.getTchat().setVisible(true);
			break;

		case "xy":
			if(array[3].equals("end")) {
				m_client.getGui().updateMap(Integer.parseInt(array[1]), Integer.parseInt(array[2]), true);	
			}
			else if(array[3].equals("continue")) {
				m_client.getGui().updateMap(Integer.parseInt(array[1]), Integer.parseInt(array[2]), false);	
			}
			break;

		case "lock":
			m_client.getGui().setTurn(Integer.parseInt(array[1]));
			break;

		case "updatestack":
				m_client.getGui2().updateStack(Integer.parseInt(array[1]), array[2]);
			break;

		case "stopgame":
			m_client.getGui().showPopUp(array[1]);
			break;

		case "mkroom":
			m_client.makeRoom(array[1]);
			break;

		case "timeflow":
			m_client.updateTime(array[1]);
			break;

		case "updateturn":
			m_client.updateTurn(array[1]);

		case "success":
			switch(array[1]) {
			case "enter":
				m_client.joinRoom(array[3]);
			}
			
		case "reject":
			switch(array[1]) {
			case "enter":
				
			}
		}
	}

}