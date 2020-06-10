
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	// true °¡ black
	private boolean checkBNW = true;
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
			mStack.push(y);
			mStack.push(x);
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

	public short getXY(int y, int x) {
		return map[y][x];
	}

	public boolean getCheck() {
		return checkBNW;
	}

	public void changeCheck() {
		if (checkBNW)
			checkBNW = false;
		else
			checkBNW = true;
	}

	public void setNull(int y, int x) {
		map[y][x] = 0;
	}

	public boolean setMap(int y, int x) {
		if (map[y][x] != 0)
			return false;
		if (checkBNW)
			map[y][x] = BLACK;
		else
			map[y][x] = WHITE;
		changeCheck();
		return true;
	}

	public void sendXY(int y, int x) throws Exception {
		client.sendMsg("xy//" + rName + "/" + Integer.toString(y) + "/" + Integer.toString(x));
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
	}

	public void drawBlack(Graphics arg0, int x, int y) {
		arg0.setColor(Color.BLACK);
		arg0.fillOval((x + 1) * size.getCell() - 15, (y) * size.getCell() + 15, STONE_SIZE, STONE_SIZE);
	}

	public void drawWhite(Graphics arg0, int x, int y) {
		arg0.setColor(Color.WHITE);
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
	MapSize size = new MapSize();
	DrawBoard d;
	private Map map;
	TeamChat tchat;
	String rName;
	Client client;
	String id;

	public GUI(String id, String title, Client client, String rName, String team, TeamChat tchat) {
		super(id + "(" + team + ")");
		this.rName = rName;
		this.tchat = tchat;
		this.client = client;
		this.id = id;
		c = getContentPane();
		setBounds(200, 20, 650, 700);
		c.setLayout(null);
		map = new Map(size, client, rName, team);
		d = new DrawBoard(size, map);
		setContentPane(d);
		addMouseListener(new mouseEventHandler(map, size, d, this));
		setVisible(true);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowHandler2(this, tchat));
	}

	public void sendStopGame(String msg) throws Exception {
		client.sendMsg(msg + id + "/" + rName);
	}

	public void setTurn(int turn) {
		map.setTurn(turn);
	}

	public Client waitingroom() {
		return client;
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

	public void updateMap(int y, int x) throws Exception {
		if (true == map.setMap(y, x)) {
			setContentPane(d);
			String res = map.cWin.checker(y, x, map.getXY(y, x));
			System.out.println(map.team + " " + res);
			if (res != "n")
				if (map.team.equals("watch"))
					showPopUp("end");
				else {
					if (res == map.team)
						showPopUp("win");
					else
						showPopUp("lose");
				}
			map.turncount = (map.turncount + 1) % 4;
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
		if (map.turncount == map.turn)
			map.lock = false;
		while (!map.lock) {
			map.lock = true;
			int x = (int) Math.round((double) ((e.getX() - 26) / 30));
			int y = (int) Math.round((double) ((e.getY() - 56) / 30));
			if (x >= 20 || y >= 20)
				return;
			if (false == map.setMap(y, x))
				break;
			gui.setContentPane(d);
			try {
				map.sendXY(y, x);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String res = map.cWin.checker(y, x, map.getXY(y, x));
			System.out.println(map.team + " " + res);
			if (res != "n")
				if (res == map.team)
					try {
						gui.showPopUp("win");
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				else
					try {
						gui.showPopUp("lose");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

			map.turncount = (map.turncount + 1) % 4;
		}
	}
}