import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class ServerEventHandler implements CMAppEventHandler{
	private CMServerStub m_serverStub;
	private Server m_server;
	
	public ServerEventHandler(CMServerStub serverStub, Server server) {
		// TODO Auto-generated constructor stub
		m_serverStub = serverStub;
		m_server = server;
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
		default:
			return;
		}
	}
	private void processDummyEvent(CMEvent cme) throws Exception {
		CMDummyEvent due = (CMDummyEvent)cme;
		String line = due.getDummyInfo();
		
		System.out.println(line + "수신");
		String array[] = line.split("/");
		String id = array[1];
		//tag/id/roomname/msg/team
		switch (array[0]) {
		// 아예 처음에 입장
		case "enter":
			// 서버 list에 guest추가
			m_server.addGuest(id);
			break;

		case "bgamestop":
			m_server.BTeamOut(array[2], id);
			m_server.addGuest(id);
			m_server.broadcastTeam(array[2], "white", "stopgame/win");
			m_server.broadcastTeam(array[2], "watch", "stopgame/end");
			m_server.broadcastTeamAnother(array[2], id, "black", "stopgame/lose");
			break;

		case "dgamestop":
			m_server.BTeamOut(array[2], id);
			m_server.addGuest(id);
			m_server.broadcastTeam(array[2], "white", "stopgame/win");
			m_server.broadcastTeam(array[2], "watch", "stopgame/end");
			m_server.broadcastTeamAnother(array[2], id, "black", "stopgame/lose");
			break;

		case "checkroomname":
			m_server.checkRoomName(array[2], id);
			break;

		case "wgamestop":
			m_server.WTeamOut(array[2], id);
			m_server.addGuest(id);
			m_server.broadcastTeam(array[2], "black", "stopgame/win");
			m_server.broadcastTeam(array[2], "watch", "stopgame/end");
			m_server.broadcastTeamAnother(array[2], id, "white", "stopgame/lose");
			break;

		case "gameend":
			m_server.gameEnd(array[2], id);
			break;

		case "mkroom":
			m_server.removeGuest(id);
			m_server.addRoom(array[2], id);
			break;

		case "roomjoin":
			m_server.enterRoom(array[2], id);
			
			break;

		case "roomout":
			m_server.removeRoomMember(array[2], id);
			m_server.removeRoom(array[2]);
			m_server.addGuest2(id);
			break;

		case "msg":
			m_server.broadcastRoom(array[2], "msg/" + id + "/" + array[3]);
			m_server.addGuest(id);
			break;

		case "tmsg":
			m_server.broadcastTeam(array[2], array[4], "tmsg/" + id + "/" + array[3]);
			break;

		case "changeteam":
			m_server.changeTeam(array[2], array[4], id);
			break;

		case "xy":
			System.out.println(1 + ":" + array[3] + " " + array[4] + " " + array[2]);
			m_server.broadcastTeam(array[2], "black", "xy/" + array[3] + "/" + array[4]);
			m_server.broadcastTeam(array[2], "white", "xy/" + array[3] + "/" + array[4]);
			m_server.broadcastTeam(array[2], "watch", "xy/" + array[3] + "/" + array[4]);
			m_server.push(array[2], array[3], array[4]);
			break;

		case "stack":
			m_server.sendStack(id, array[2]);
			break;

		case "logout":
			m_server.removeGuest(id);
			m_server.broadcastTeam(array[2], array[4], "tmsg/" + id + "/" + array[3]);
			return;
			
		case "ready":
			m_server.ready(array[2], id);
			
			break;
		}
	}
}
