import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

public class Server {
	
	private CMServerStub m_serverStub;
	private ServerEventHandler m_eventHandler;
	// 유지되어야 하는 방이름
	volatile ArrayList<String> roomNameList;
	// 대기방에 있는 사람명단
	volatile ArrayList<String> list;
	// 대기방에 있는 방 명단
	volatile HashMap<String, ArrayList<String>> map;	//all people names in each rooms
	
	volatile HashMap<String, ArrayList<String>> wMap;	//white stone people names in each rooms
	volatile HashMap<String, ArrayList<String>> bMap;	//black stone people names in each rooms
	volatile HashMap<String, ArrayList<String>> dMap;	//watch stone people names in each rooms
	
	volatile HashMap<String, Integer> wNum;			//white stone people count in each rooms
	volatile HashMap<String, Integer> bNum;			//black stone people count in each rooms
	volatile HashMap<String, Integer> dNum;			//watch stone people count in each rooms
	// <roomName, Integer>
	
	volatile HashMap<String, Integer> wReadyNum;	//white ready player count
	volatile HashMap<String, Integer> bReadyNum;	//black ready player count
	volatile HashMap<String, Boolean> wIsReady;
	volatile HashMap<String, Boolean> bIsReady;
	
	volatile HashMap<String, Integer> tNum;	
	// 오목판 두어진 돌에대한 정보 저장
	volatile HashMap<String, ArrayList<Integer>> mlist;

	Server() {
		roomNameList = new ArrayList<String>();
		map = new HashMap<String, ArrayList<String>>();
		wMap = new HashMap<String, ArrayList<String>>();
		bMap = new HashMap<String, ArrayList<String>>();
		dMap = new HashMap<String, ArrayList<String>>();
		
		wNum = new HashMap<String, Integer>();
		bNum = new HashMap<String, Integer>();
		dNum = new HashMap<String, Integer>();
		tNum = new HashMap<String, Integer>();
		
		wReadyNum = new HashMap<String, Integer>();
		bReadyNum = new HashMap<String, Integer>();
		wIsReady = new HashMap<String, Boolean>();
		bIsReady = new HashMap<String, Boolean>();
		
		list = new ArrayList<String>();
		mlist = new HashMap<String, ArrayList<Integer>>();
		
		m_serverStub = new CMServerStub();
		m_eventHandler = new ServerEventHandler(m_serverStub, this);
	}

	public CMServerStub getM_serverStub() {
		return m_serverStub;
	}



	public void setM_serverStub(CMServerStub m_serverStub) {
		this.m_serverStub = m_serverStub;
	}



	public ServerEventHandler getM_eventHandler() {
		return m_eventHandler;
	}



	public void setM_eventHandler(ServerEventHandler m_eventHandler) {
		this.m_eventHandler = m_eventHandler;
	}



	public ArrayList<String> getRoomNameList() {
		return roomNameList;
	}



	public void setRoomNameList(ArrayList<String> roomNameList) {
		this.roomNameList = roomNameList;
	}



	public ArrayList<String> getList() {
		return list;
	}



	public void setList(ArrayList<String> list) {
		this.list = list;
	}



	public HashMap<String, ArrayList<String>> getMap() {
		return map;
	}



	public void setMap(HashMap<String, ArrayList<String>> map) {
		this.map = map;
	}



	public HashMap<String, ArrayList<String>> getwMap() {
		return wMap;
	}



	public void setwMap(HashMap<String, ArrayList<String>> wMap) {
		this.wMap = wMap;
	}



	public HashMap<String, ArrayList<String>> getbMap() {
		return bMap;
	}



	public void setbMap(HashMap<String, ArrayList<String>> bMap) {
		this.bMap = bMap;
	}



	public HashMap<String, ArrayList<String>> getdMap() {
		return dMap;
	}



	public void setdMap(HashMap<String, ArrayList<String>> dMap) {
		this.dMap = dMap;
	}



	public HashMap<String, Integer> getwNum() {
		return wNum;
	}



	public void setwNum(HashMap<String, Integer> wNum) {
		this.wNum = wNum;
	}



	public HashMap<String, Integer> getbNum() {
		return bNum;
	}



	public void setbNum(HashMap<String, Integer> bNum) {
		this.bNum = bNum;
	}
	
	public HashMap<String, Integer> getdNum() {
		return dNum;
	}

	public void setdNum(HashMap<String, Integer> dNum) {
		this.dNum = dNum;
	}

	public HashMap<String, ArrayList<Integer>> getMlist() {
		return mlist;
	}

	public void setMlist(HashMap<String, ArrayList<Integer>> mlist) {
		this.mlist = mlist;
	}


	void addGuest(String g) throws Exception {
		list.add(g);
		broadcastList();
		getRoomlist(g);
	}

	void addGuest2(String g) throws Exception {
		list.add(g);
		broadcastList();
		broadcastRoomlist();
	}

	void removeGuest(String g) throws Exception {
		list.remove(g);
		broadcastList();
	}

	// 특정 guest에게 list뿌림
	void getList(String g) throws Exception {
		StringBuffer buffer = new StringBuffer("list/");
		for (String g2 : list)
			buffer.append(g2 + "/");
		
		sendMsg(buffer.toString(), g);
	}

	// 대기방에 있는guest들에게 새로운 list뿌림
	void broadcastList() throws Exception {
		StringBuffer buffer = new StringBuffer("list/");
		for (String g : list)
			buffer.append(g + "/");
		
		broadcast(buffer.toString());
	}

	// 특정 guest에게 roomlist 뿌림
	void getRoomlist(String g) throws Exception {
		//Set<String> roomlist = map.keySet();
		StringBuffer buffer = new StringBuffer("roomlist/");
		for (String room : roomNameList) {
			buffer.append(room + "/" + tNum.get(room) + "/");
		}
		
		sendMsg(buffer.toString(), g);
	}

	// 대기방에 있는 guest들에게 새로운 roomlist뿌림
	void broadcastRoomlist() throws Exception {
		//Set<String> roomlist = map.keySet();
		StringBuffer buffer = new StringBuffer("roomlist/");
		for (String room : roomNameList) {
			//int member = bNum.get(t) + wNum.get(t) + dNum.get(t);
			buffer.append(room + "/" + tNum.get(room) + "/");
		}
		broadcast(buffer.toString());
	}

	void addRoom(String rName, String g) throws Exception {
		System.out.println("addroom");
		ArrayList<String> arraylist = new ArrayList<String>();
		arraylist.add(g);
		map.put(rName, arraylist);
		wMap.put(rName, new ArrayList<String>());
		bMap.put(rName, new ArrayList<String>());
		dMap.put(rName, new ArrayList<String>());
		mlist.put(rName, new ArrayList<Integer>());
		wNum.put(rName, 0);
		bNum.put(rName, 0);
		dNum.put(rName, 1);
		tNum.put(rName, 1);
		dMap.get(rName).add(g);
		System.out.println("개설된방 :" + rName);
		broadcastRoomlist();
		updateRoomMember(rName);
	}

	void updateRoomMember(String rName) throws Exception {
		StringBuffer buffer = new StringBuffer("roommember/");
		ArrayList<String> temp = map.get(rName);
		System.out.println("size!!: " + temp.size());
		for (String g : temp) {
			buffer.append(g);
			if (wMap.get(rName).contains(g))
				buffer.append("(white)/");
			else if (bMap.get(rName).contains(g))
				buffer.append("(black)/");
			else
				buffer.append("(watch)/");
		}
		System.out.println(buffer.toString());
		for (String g : temp)
			sendMsg(buffer.toString(), g);
	}

	void checkRoomName(String rName, String g) throws Exception {
		synchronized (this) {
			if (roomNameList.contains(rName))
				return;
			roomNameList.add(rName);
			sendMsg("mkroom/" + rName, g);
		}
	}

	boolean checkWaitingRoomName(String rName) {
		if (map.containsKey(rName))
			return false;
		return true;
	}

	void enterRoom(String rName, String g) throws Exception {
		if(tNum.get(rName) < 7) {
			int otNum = tNum.get(rName);
			tNum.replace(rName, ++otNum);
			System.out.println("현재 방 인원수는 : " + tNum.get(rName));
			map.get(rName).add(g);
			dMap.get(rName).add(g);
			dNum.replace(rName, dNum.get(rName) + 1);
			updateRoomMember(rName);
			broadcastRoomlist();
		}
		else {
			//send reject enterRoom
			//
		}
	}
	
	void ready(String rName, String g) {
		
	}

	void removeRoomMember(String rName, String g) throws Exception {
		map.get(rName).remove(g);
		if (wMap.get(rName).contains(g)) {
			wMap.get(rName).remove(g);
			wNum.replace(rName, wNum.get(rName) - 1);
		} else if (bMap.get(rName).contains(g)) {
			bMap.get(rName).remove(g);
			bNum.replace(rName, bNum.get(rName) - 1);
		} else {
			dMap.get(rName).remove(g);
			dNum.replace(rName, dNum.get(rName) - 1);
		}
		tNum.replace(rName,  tNum.get(rName) - 1);
		updateRoomMember(rName);
	}

	void removeRoom(String rName) {
		if (map.get(rName).size() == 0) {
			map.remove(rName);
			wMap.remove(rName);
			wNum.remove(rName);
			bMap.remove(rName);
			bNum.remove(rName);
			dNum.remove(rName);
			tNum.remove(rName);
			dMap.remove(rName);
			mlist.remove(rName);
			roomNameList.remove(rName);
		}
	}

	void removeBWDRoom(String rName) {
		wMap.remove(rName);
		wNum.remove(rName);
		bMap.remove(rName);
		bNum.remove(rName);
		dNum.remove(rName);
		tNum.remove(rName);
		dMap.remove(rName);
		mlist.remove(rName);
	}

	synchronized void gameEnd(String rName, String g) throws Exception {
		if (checkWaitingRoomName(rName)) {
			removeBWDRoom(rName);
			addRoom(rName, g);
		} else {
			enterRoom(rName, g);
		}
	}

	synchronized void changeTeam(String rName, String team, String g) throws Exception {
		if (team.equals("white")) {
			if (!wMap.get(rName).contains(g)) {
				if (wNum.get(rName) < 2) {
					if (bMap.get(rName).contains(g)) {
						bMap.get(rName).remove(g);
						bNum.replace(rName, bNum.get(rName) - 1);
					} else {
						dMap.get(rName).remove(g);
						dNum.replace(rName, dNum.get(rName) - 1);
					}
					wMap.get(rName).add(g);
					wNum.replace(rName, wNum.get(rName) + 1);
				}
			}
		} else if(team.equals("black")){
			if (!bMap.get(rName).contains(g)) {
				if (bNum.get(rName) < 2) {
					if (wMap.get(rName).contains(g)) {
						wMap.get(rName).remove(g);
						wNum.replace(rName, wNum.get(rName) - 1);
					} else {
						dMap.get(rName).remove(g);
						dNum.replace(rName, dNum.get(rName) - 1);
					}
					bMap.get(rName).add(g);
					bNum.replace(rName, bNum.get(rName) + 1);
				}
			}
		}
		else {
			if (!dMap.get(rName).contains(g)) {
				if (dNum.get(rName) < 2) {
					if (bMap.get(rName).contains(g)) {
						bMap.get(rName).remove(g);
						bNum.replace(rName, bNum.get(rName) - 1);
					} else {
						wMap.get(rName).remove(g);
						wNum.replace(rName, wNum.get(rName) - 1);
					}
					dMap.get(rName).add(g);
					dNum.replace(rName, dNum.get(rName) + 1);
				}
			}
		}
		updateRoomMember(rName);
		gameStart(rName);
	}

	void BTeamOut(String rName, String g) {
		bMap.get(rName).remove(g);
	}

	void WTeamOut(String rName, String g) {
		wMap.get(rName).remove(g);
	}

	void DTeamOut(String rName, String g) throws Exception {
		dMap.get(rName).remove(g);
		sendTeamListD(rName);
		//System.out.println(dMap.get(rName).size());
	}

	void sendTeamListB(String rName) throws Exception {
		StringBuffer buffer = new StringBuffer("teamlist/");
		for (String g : bMap.get(rName))
			buffer.append(g + "/");
		for (String g : bMap.get(rName))
			sendMsg(buffer.toString(), g);
	}

	void sendTeamListW(String rName) throws Exception {
		StringBuffer buffer = new StringBuffer("teamlist/");
		for (String g : wMap.get(rName))
			buffer.append(g + "/");
		for (String g : wMap.get(rName))
			sendMsg(buffer.toString(), g);
	}

	void sendTeamListD(String rName) throws Exception {
		StringBuffer buffer = new StringBuffer("teamlist/");
		for (String g : dMap.get(rName))
			buffer.append(g + "/");
		for (String g : dMap.get(rName))
			sendMsg(buffer.toString(), g);
	}

	void gameStart(String rName) throws Exception {
		if (wMap.get(rName).size() == 2 && bMap.get(rName).size() == 2) {
			map.remove(rName);
			broadcastRoomlist();
			for (String g : bMap.get(rName))
				sendMsg("gamestartB", g);
			for (String g : wMap.get(rName))
				sendMsg("gamestartW", g);
			for (String g : dMap.get(rName))
				sendMsg("gamestartD", g);
			sendTeamListB(rName);
			sendTeamListW(rName);
			sendTeamListD(rName);
			broadcastLock(rName, "white");
			broadcastLock(rName, "black");
			broadcastLock(rName, "watch");
		}
	}

	void broadcastRoom(String rName, String msg) throws Exception {
		for (String g : map.get(rName))
			sendMsg(msg, g);
	}

	void broadcastTeam(String rName, String team, String msg) throws Exception {
		if (team.equals("white"))
			for (String g : wMap.get(rName))
				sendMsg(msg, g);
		else if (team.equals("black"))
			for (String g : bMap.get(rName))
				sendMsg(msg, g);
		else
			for (String g : dMap.get(rName))
				sendMsg(msg, g);
	}

	void broadcastTeamAnother(String rName, String id, String team, String msg) throws Exception {
		if (team.equals("white")) {
			for (String g : wMap.get(rName))
				if (g.equals(id) == false)
					sendMsg(msg, g);
		} else if (team.equals("black"))
			for (String g : bMap.get(rName))
				if (g.equals(id) == false)
					sendMsg(msg, g);
	}

	void broadcastLock(String rName, String team) throws Exception {
		int idx;
		if (team.equals("black")) {
			idx = 0;
			for (String g : bMap.get(rName)) {
				sendMsg("lock/" + Integer.toString(idx), g);
				idx += 2;
			}
		} else if (team.equals("white")) {
			idx = 1;
			for (String g : wMap.get(rName)) {
				sendMsg("lock/" + Integer.toString(idx), g);
				idx += 2;
			}
		} else
			for (String g : dMap.get(rName))
				sendMsg("lock/" + Integer.toString(-100), g);
	}

	void broadcast(String msg) throws Exception {
		for (String g : list)
			sendMsg(msg, g);
	}

	void push(String rName, String y, String x) {
		mlist.get(rName).add(Integer.parseInt(y));
		mlist.get(rName).add(Integer.parseInt(x));
	}

	void sendStack(String g, String rName) throws Exception {
		StringBuffer buffer = new StringBuffer("updatestack/");
		ArrayList<Integer> l = mlist.get(rName);
		int size = l.size();
		buffer.append(Integer.toString(size) + "/");
		System.out.println("size: " + size);
		for (int i = 0; i < size; i++)
			buffer.append(Integer.toString(l.get(i)) + ":");

		sendMsg(buffer.toString(), g);
	}
		
	// sendMsg
	void sendMsg(String msg, String id) {
		CMDummyEvent cmde = new CMDummyEvent();
		cmde.setDummyInfo(msg);
		m_serverStub.send(cmde, id);
	}

	public static void main(String args[]) throws Exception {
		Server server = new Server();
		server.m_serverStub.setAppEventHandler(server.m_eventHandler);
		server.m_serverStub.startCM();
	}
}
