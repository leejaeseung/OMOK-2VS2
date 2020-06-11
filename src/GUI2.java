
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class WindowHandler extends WindowAdapter {
	GUI2 gui2;

	WindowHandler(GUI2 gui2) {
		this.gui2 = gui2;
	}

	public void windowClosing(WindowEvent e) {
		System.out.println("gui2 wclose");
		gui2.changeFlag();
	}
}

public class GUI2 extends JFrame {
	private Container c;
	MapSize size = new MapSize();
	DrawBoard d;
	private Map map;
	String id;
	TeamChat2 tchat2;

	public GUI2(String id, TeamChat2 tchat2) {
		super(id + "'s turn");
		this.id = id;
		this.tchat2 = tchat2;
		c = getContentPane();
		setBounds(200, 20, 650, 700);
		c.setLayout(null);
		map = new Map(size, null, null, null);
		d = new DrawBoard(size, map);
		setContentPane(d);
		addMouseListener(new mouseEventHandler2(map, size, d, this));
		setVisible(true);
		this.addWindowListener(new WindowHandler(this));
	}

	public void updateMap(int y, int x) throws Exception {
		if (1 == map.setMap(y, x, true))
			setContentPane(d);
	}

	public void updateStack(int size, String line) {
		map.updateStack(size, line);
		setContentPane(d);
	}

	public void changeFlag() {
		tchat2.changeFlag();
	}
}

class mouseEventHandler2 extends MouseAdapter implements MouseMotionListener {
	Map map;
	MapSize size;
	DrawBoard d;
	GUI2 gui2;

	mouseEventHandler2(Map map, MapSize size, DrawBoard d, GUI2 g) {
		this.map = map;
		this.size = size;
		this.d = d;
		gui2 = g;
	}

	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			System.out.println("right 1");
			if (!map.SisEmpty()) {
				System.out.println("right 2");
				int y;
				int x;
				x = map.Spop();
				y = map.Spop();
				map.setNull(y, x);
				map.changeCheck();
				gui2.setContentPane(d);
			}
		} else if (e.getClickCount() == 1) {
			int x = (int) Math.round((double) ((e.getX() - 26) / 30));
			int y = (int) Math.round((double) ((e.getY() - 56) / 30));
			if (x >= 20 || y >= 20)
				return;
			if (0 == map.getMap(y, x))
				return;
			gui2.setContentPane(d);
			map.pushStack(y, x);
		}
	}
}