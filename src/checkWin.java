
public class checkWin {
	Map map;
	int y, x, tcolor;

	checkWin(Map map) {
		this.map = map;
	}

	public String checker(int y, int x, int tcolor) {
		this.y = y; this.x = x;
		this.tcolor = tcolor;
		
		if (checkRow(y, x) || checkCol(y, x) || checkUp(y, x) || checkDown(y, x))
			if (tcolor == 1)
			{	System.out.println("bwin");
				return "black";
			}
			else if (tcolor == -1)
			{
				System.out.println(("wwin"));
				return "white";
			}
		System.out.println("no");
		return "n";
	}

	private boolean checkRow(int y, int x) {
		int lidx = y - 1, ridx = y + 1;
		int snum = 1;
		boolean lflag = true, rflag = true;
		while (true) {
			if(lidx < 0) lflag = false;
			if(ridx > 19) rflag = false;
			if (!lflag && !rflag)
				break;
			if (lflag)
				if (map.getXY(lidx--, x) == tcolor)
					++snum;
				else
					lflag = false;
			if (rflag)
				if (map.getXY(ridx++, x) == tcolor)
					++snum;
				else
					rflag = false;
		}
		if (snum == 5)
			return true;
		else
			return false;
	}

	private boolean checkCol(int y, int x) {
		int didx = x - 1, uidx = x + 1;
		int snum = 1;
		boolean dflag = true, uflag = true;
		while (true) {
			if(didx < 0) dflag = false;
			if(uidx > 19) uflag = false;
			if (!dflag && !uflag)
				break;
			if (dflag)
				if (map.getXY(y, didx--) == tcolor)
					++snum;
				else
					dflag = false;
			if (uflag)
				if (map.getXY(y, uidx++) == tcolor)
					++snum;
				else
					uflag = false;
		}
		if (snum == 5)
			return true;
		else
			return false;
	}

	private boolean checkDown(int y, int x) {
		int lyidx = y - 1, lxidx = x - 1;
		int ryidx = y + 1, rxidx = x + 1;
		int snum = 1;
		boolean lflag = true, rflag = true;
		while (true) {
			if(lyidx < 0 || lxidx < 0)
				lflag = false;
			if(ryidx > 19 || rxidx > 19)
				rflag = false;
			if (!lflag && !rflag)
				break;
			if (lflag)
				if (map.getXY(lyidx--, lxidx--) == tcolor)
					snum++;
				else
					lflag = false;
			if (rflag)
				if (map.getXY(ryidx++, rxidx++) == tcolor)
					snum++;
				else
					rflag = false;
		}
		if (snum == 5)
			return true;
		else
			return false;
	}

	private boolean checkUp(int y, int x) {
		int lyidx = y - 1, lxidx = x + 1;
		int ryidx = y + 1, rxidx = x - 1;
		int snum = 1;
		boolean lflag = true, rflag = true;
		while (true) {
			if(lyidx < 0 || lxidx > 19) lflag = false;
			if(ryidx > 19 || rxidx < 0) rflag = false;
			if (!lflag && !rflag)
				break;
			if (lflag)
				if (map.getXY(lyidx--, lxidx++) == tcolor)
					snum++;
				else
					lflag = false;
			if (rflag)
				if (map.getXY(ryidx++, rxidx--) == tcolor)
					snum++;
				else
					rflag = false;
		}
		if (snum == 5)
			return true;
		else
			return false;
	}
}
