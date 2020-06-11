
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

import javax.swing.*;

class MapSize {
	private final int CELL = 30;
	private final int SIZE = 20;

	public int getCell() {
		return CELL;
	}
	public int getSize() {
		return SIZE;
	}
}

class Map {
	private short[][] map;
	private final short BLACK = 1;
	private final short WHITE = -1;
	private final short GRAY = 2;
	// true is black
	private boolean checkBNW = true;
	private int grayX = -1;
	private int grayY = -1;
	Client client;
	String rName;
	String team;
	int turn;
	int turncount;
	boolean lock;
	checkWin cWin;
	Stack<Integer> mStack;

	public Map(MapSize ms, Client client, String rName, String team) {
		map = new short[ms.getSize()][];
		for (int i = 0; i < map.length; i++)
			map[i] = new short[ms.getSize()];
		this.rName = rName;
		this.client = client;
		lock = true;
		turncount = 0;
		cWin = new checkWin(this);
		this.team = team;
		mStack = new Stack<Integer>();
	}

	public int Spop() {
		return mStack.pop();
	}

	public boolean SisEmpty() {
		return mStack.isEmpty();
	}

	public void updateStack(int size, String line) {
		mStack.clear();
		String[] array = line.split(":");
		short flag = 1;
		size = size / 2;
		if (size % 2 == 1)
			changeCheck();
		for (int i = 0; i < size; i++) {
			int y = Integer.parseInt(array[2 * i]);
			int x = Integer.parseInt(array[2 * i + 1]);
			pushStack(y, x);
			map[y][x] = flag;
			flag *= -1;
		}
	}

	public void pushStack(int y, int x) {
		mStack.push(y);
		mStack.push(x);
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public short getBlack() {
		return BLACK;
	}

	public short getWhite() {
		return WHITE;
	}
	
	public short getGray() {
		return GRAY;
	}

	public short getXY(int y, int x) {
		return map[y][x];
	}

	public boolean getCheck() {
		return checkBNW;
	}

	public void changeCheck() {
		checkBNW = !checkBNW;
	}

	public void setNull(int y, int x) {
		map[y][x] = 0;
	}

	public int getMap(int y, int x) {
		if(map[y][x] == 0) {
			return 2;
		}
		else if(map[y][x] == BLACK || map[y][x] == WHITE)
		return 0;
		else
			return 1;
	}
	
	public int setMap(int y, int x, boolean isEnd) {
		if(map[y][x] == 0 && !isEnd) {
			if(grayX != -1 && grayY != -1) {
				map[grayY][grayX] = 0;
			}
			grayX = x;
			grayY = y;
			map[y][x] = GRAY;
			return 2;
		}
		if (map[y][x] == BLACK || map[y][x] == WHITE)
			return 0;
		
		System.out.println("Map Color : " + map[y][x]);
		
		if(map[y][x] == GRAY || isEnd) {
		if (checkBNW)
			map[y][x] = BLACK;
		else
			map[y][x] = WHITE;
		grayX = -1;
		grayY = -1;
		changeCheck();
		}
		return 1;
	}

	public void sendXY(int y, int x, int color) throws Exception {
		if(color == 1) {
			client.sendMsg("xy/" + team + "/" + rName + "/" + Integer.toString(y) + "/" + Integer.toString(x) + "/double");
			client.sendUpdateTurnMsg();
		}
		else if(color == 2)
			client.sendMsg("xy/" + team + "/" + rName + "/" + Integer.toString(y) + "/" + Integer.toString(x) + "/single");
	}
}

class DrawBoard extends JPanel {
	private MapSize size;
	private Map map;
	private final int STONE_SIZE = 28;

	public DrawBoard(MapSize m, Map map) {
		setBackground(new Color(206, 167, 61));
		size = m;
		setLayout(null);
		this.map = map;
	}

	public void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
		arg0.setColor(Color.BLACK);
		board(arg0);
		drawStone(arg0);
	}

	public void board(Graphics arg0) {
		for (int i = 1; i <= size.getSize(); i++) {
			arg0.drawLine(size.getCell(), i * size.getCell(), size.getCell() * size.getSize(), i * size.getCell());
			arg0.drawLine(i * size.getCell(), size.getCell(), i * size.getCell(), size.getCell() * size.getSize());
		}
	}

	public void drawStone(Graphics arg0) {
		for (int y = 0; y < size.getSize(); y++)
			for (int x = 0; x < size.getSize(); x++)
				if (map.getXY(y, x) == map.getBlack())
					drawBlack(arg0, x, y);
				else if (map.getXY(y, x) == map.getWhite())
					drawWhite(arg0, x, y);
				else if(map.getXY(y, x) == map.getGray())
					drawGray(arg0, x, y);
	}

	public void drawBlack(Graphics arg0, int x, int y) {
		arg0.setColor(Color.BLACK);
		arg0.fillOval((x + 1) * size.getCell() - 15, (y) * size.getCell() + 15, STONE_SIZE, STONE_SIZE);
	}

	public void drawWhite(Graphics arg0, int x, int y) {
		arg0.setColor(Color.WHITE);
		arg0.fillOval(x * size.getCell() + 15, y * size.getCell() + 15, STONE_SIZE, STONE_SIZE);
	}
	public void drawGray(Graphics arg0, int x, int y) {
		arg0.setColor(Color.GRAY);
		arg0.fillOval(x * size.getCell() + 15, y * size.getCell() + 15, STONE_SIZE, STONE_SIZE);
	}
}

class WindowHandler2 extends WindowAdapter {
	GUI gui;
	TeamChat tchat;

	WindowHandler2(GUI gui, TeamChat tchat) {
		this.gui = gui;
		this.tchat = tchat;
	}

	public void windowClosing(WindowEvent e) {
		tchat.setVisible(false);
		tchat.dispose();
		gui.setVisible(false);
		gui.dispose();
		if (tchat.team() == "black") {
			try {
				gui.sendStopGame("bgamestop/");
				gui.waitingroom().setVisible(true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (tchat.team() == "white") {
			try {
				gui.sendStopGame("wgamestop/");
				gui.waitingroom().setVisible(true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			try {
				gui.sendStopGame("dgamestop/");
				gui.waitingroom().setVisible(true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}

public class GUI extends JFrame {
	private Container c;
	private Map map;
	private int time;
	private JLabel timeLabel;
	private JLabel whoseTurn;
	private JPanel upperPanel;
	TeamChat tchat;
	String rName;
	Client client;
	String id;
	MapSize size = new MapSize();
	DrawBoard d;


	public GUI(String id, Client client, String rName, String team, TeamChat tchat) {
		super(id + "(" + team + ")");
		this.rName = rName;
		this.tchat = tchat;
		this.client = client;
		this.id = id;
		c = getContentPane();
		setBounds(200, 20, 650, 700);

		upperPanel = new JPanel();
		upperPanel.setLayout(new BorderLayout());

		time = 0;
		timeLabel = new JLabel();
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		timeLabel.setText("00:" + String.format("%02d", time));

		whoseTurn = new JLabel();
		whoseTurn.setHorizontalAlignment(JLabel.CENTER);
		whoseTurn.setText("unknown");

		c.setLayout(new BorderLayout());
		c.add(BorderLayout.NORTH, upperPanel);
		upperPanel.add(BorderLayout.WEST, whoseTurn);
		upperPanel.add(BorderLayout.EAST, timeLabel);

		map = new Map(size, client, rName, team);
		d = new DrawBoard(size, map);

		c.add(BorderLayout.CENTER, d);
		addMouseListener(new mouseEventHandler(map, size, d, this));
		setVisible(true);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowHandler2(this, tchat));
	}

	public void setCurTurnUserName(String userName) {
		this.whoseTurn.setText("NOW turn -> " + userName);
	}

	public void sendStopGame(String msg) throws Exception {
		client.sendMsg(msg + id + "/" + rName);
	}

	public Container getContainer() {
		return c;
	}

	public void setTurn(int turn) {
		map.setTurn(turn);
	}

	public Client waitingroom() {
		return client;
	}

	public void updateTime(int curTime) {
		this.time = curTime;
		if(this.time == -1) {
			timeLabel.setText("TIME OUT!!");
			d.setEnabled(false);
			this.nextTurn();
			this.map.changeCheck();
		}
		else {
			if(!d.isEnabled()) d.setEnabled(true);
			timeLabel.setText("00:" + String.format("%02d", time));
		}
	}

	public void nextTurn() {
		map.turncount = (map.turncount + 1) % 4;

		if(map.turncount == map.turn)
			client.sendUpdateTurnMsg();
	}

	public void showPopUp(String message) throws Exception {
		JOptionPane.showMessageDialog(this, message, "", JOptionPane.INFORMATION_MESSAGE);
		tchat.setVisible(false);
		tchat.dispose();
		setVisible(false);
		dispose();
		client.sendMsg("gameend/" + id + "/" + rName + "/");
		/*client.setRoom(new Room(id, rName, client));
		client.getRoom().setBounds(200, 200, 400, 300);
		client.getRoom().setVisible(true);*/
	}

	public void updateMap(int y, int x, boolean isEnd) throws Exception {

		System.out.println("¿ÀÀ×?");
		int mapColor = map.setMap(y, x, isEnd);

		System.out.println("MapColor : " + mapColor);
		if (1 == mapColor) {
			setContentPane(d);
			c.revalidate();
			c.repaint();
			String res = map.cWin.checker(y, x, map.getXY(y, x));
			System.out.println(map.team + " " + res);
			if (res != "n") {
				if (map.team.equals("watch"))
					showPopUp("end");
				else {
					if (res == map.team)
						showPopUp("win");
					else
						showPopUp("lose");
				}
			}
			nextTurn();
		}
		else if(2 == mapColor) {
			setContentPane(d);
			c.revalidate();
			c.repaint();
		}
	}
}

class mouseEventHandler extends MouseAdapter implements MouseMotionListener {

	Map map;
	MapSize size;
	DrawBoard d;
	GUI gui;

	mouseEventHandler(Map map, MapSize size, DrawBoard d, GUI g) {
		this.map = map;
		this.size = size;
		this.d = d;
		gui = g;
	}

	public void mouseClicked(MouseEvent e) {
		if (map.turncount == map.turn) {
			/*map.lock = false;
		while (!map.lock) {*/
			//map.lock = true;
			int x = (int) Math.round((double) ((e.getX() - 26) / 30));
			int y = (int) Math.round((double) ((e.getY() - 56) / 30));
			if (x >= 20 || y >= 20)
				return;
			int mapColor = map.getMap(y, x);
			System.out.println("MapColor : " + mapColor);
			if (0 == mapColor)
				return;
			/*if(2 == mapColor)
				map.lock = false;*/
			gui.getContainer().revalidate();
			gui.getContainer().repaint();
			try {
				map.sendXY(y, x, mapColor);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(mapColor == 1) {
			String res = map.cWin.checker(y, x, map.getXY(y, x));
			System.out.println(map.team + " " + res);
			if (res != "n") {
				if (res == map.team) {
					try {
						gui.showPopUp("win");
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				} else {
					try {
						gui.showPopUp("lose");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			map.turncount = (map.turncount + 1) % 4;
			}
		}
	}
}