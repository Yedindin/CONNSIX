import java.awt.Point;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class DummyAI {
	ConnectSix conSix;
	String c; // color
	Point[] endPoint = new Point[6];

	boolean test = true;
	boolean firstDraw = true;

	int detectLevel = 4;

	int black = 0;
	int white = 0;

	boolean state[][] = new boolean[19][19];
	int color[][] = new int[19][19];
	// 0 black
	// 1 white
	// 2 착수금지점

	boolean bState[][] = new boolean[19][19];
	boolean wState[][] = new boolean[19][19];

	int locationx[][] = new int[19][19];
	int locationy[][] = new int[19][19];

	int Wdetect[][] = new int[19][19];
	int Bdetect[][] = new int[19][19];

	Random rd = new Random();

	boolean start = false;

	int startcount = 0;

	Point loca;

	String mode = "";
	String win = "";

	boolean end = false;

	int x, y;
	int std = 0;
	int xsuby, ysubx = 0;

	int trigger = 0;

	boolean isBlackCom = true;

	Point toDraw;

	DummyAI(ConnectSix consix, String c) throws ConnSixException {
		this.conSix = consix;
		this.c = c;
		String alpha1 = "";
		String alpha2 = "";
		System.out.println("Red Stone positions are " + conSix.redStones);
		if (conSix.redStones != null)
			setRedStone(conSix.redStones);

		if (c.toLowerCase().compareTo("black") == 0) {
			setBlackState("K10");
			String first = conSix.drawAndRead("K10");
			String[] returnWhite = first.split(":");
			for (int i = 0; i < returnWhite.length; i++) {
				setWhiteState(returnWhite[i]);
			}
		} else if (c.toLowerCase().compareTo("white") == 0) {
			String first = conSix.drawAndRead("");
			setBlackState(first);
		}

		if (firstDraw) {
			if (c.toLowerCase().compareTo("black") == 0) {
				String bcoordinate = bfirstdraw();
				firstDraw = false;
				String read = conSix.drawAndRead(bcoordinate);
				String[] returnCoor = read.split(":");
				for (int i = 0; i < returnCoor.length; i++) {
					setWhiteState(returnCoor[i]);
				}
			} else if (c.toLowerCase().compareTo("white") == 0) {
				String wcoordinate = wfirstdraw();
				firstDraw = false;
				String read = conSix.drawAndRead(wcoordinate);
				String[] returnCoor = read.split(":");
				for (int i = 0; i < returnCoor.length; i++) {
					setBlackState(returnCoor[i]);
				}
			}
		}

		while (true) {

			if (c.toLowerCase().compareTo("black") == 0) {

				detectW4(4);
				detectB4(4);
				while (true) {
					alpha1 = drawByCom(true);
					if (alpha1 != null)
						break;
				}
				// drawByCom이 그려야 하는 위치를 String으로 return
				// 한번 놓고
				detectW4(4);
				detectB4(4);
				while (true) {
					alpha2 = drawByCom(true);
					if (alpha2 != null)
						break;
				}
				// drawByCom이 그려야 하는 위치를 String으로 return
				// String 2개를 concat해서 drawAndRead 부름
				// 한번 더 놓고
				// 끝
			} else if (c.toLowerCase().compareTo("white") == 0) {
				detectW4(4);
				detectB4(4);
				while (true) {
					alpha1 = drawByCom(false);
					if (alpha1 != null)
						break;
				}
				// drawByCom이 그려야 하는 위치를 String으로 return
				// 한번 놓고
				detectW4(4);
				detectB4(4);
				while (true) {
					alpha2 = drawByCom(false);
					if (alpha2 != null)
						break;
				}
				// drawByCom이 그려야 하는 위치를 String으로 return
				// String 2개를 concat해서 drawAndRead 부름
				// 한번 더 놓고
				// 끝
			}

			String draw = String.format("%s:%s", alpha1, alpha2);

			String read = conSix.drawAndRead(draw);
			

			if (read.compareTo("WIN") == 0 || read.compareTo("LOSE") == 0 || read.compareTo("EVEN") == 0) {
				break;
			}

			String[] returnCoor = read.split(":");
			if (c.toLowerCase().compareTo("black") == 0) {
				for (int i = 0; i < returnCoor.length; i++) {
					setWhiteState(returnCoor[i]);
				}
			} else if (c.toLowerCase().compareTo("white") == 0) {
				for (int i = 0; i < returnCoor.length; i++) {
					setBlackState(returnCoor[i]);
				}
			}

		}

	}

	public void setRedStone(String redStoneLocation) {
		String[] redStones = redStoneLocation.split(":");
		for (int i = 0; i < redStones.length; i++) {
			System.out.println(Integer.parseInt(redStones[i].substring(1)));
			System.out.println(redStones[i].charAt(0) - 65);
			int y = 19 - Integer.parseInt(redStones[i].substring(1));
			int x = redStones[i].charAt(0) - 65;
			if (x >= 9)
				x--;
			state[y][x] = true;
			color[y][x] = 2;
		}
	}

	public void setBlackState(String blackLocation) {
		int y = 19 - Integer.parseInt(blackLocation.substring(1));
		int x = blackLocation.charAt(0) - 65;
		if (x >= 9)
			x--;
		bState[y][x] = true;
		state[y][x] = true;
		color[y][x] = -1;
	}

	public void setWhiteState(String whiteLocation) {
		int y = 19 - Integer.parseInt(whiteLocation.substring(1));
		int x = whiteLocation.charAt(0) - 65;
		if (x >= 9)
			x--;
		wState[y][x] = true;
		state[y][x] = true;
		color[y][x] = 1;
	}

	public String index2coor(Point index) {
		int y = 19 - index.x;
		char x = (char) (index.y + 65);
		if (x >= 73)
			x++;

		return String.format("%c%02d", x, y);
	}

	public String bfirstdraw() {

		String coor = "";
		while (true) {

			int i1 = rd.nextInt(3) + 8;
			int j1 = rd.nextInt(3) + 8;
			int i2 = rd.nextInt(3) + 8;
			int j2 = rd.nextInt(3) + 8;

			double dist = Math.sqrt(Math.pow(Math.abs(i1 - i2), 2) + Math.pow(Math.abs(j1 - j2), 2));

			if (i1 == i2 && j2 == j1) {
				continue;
			}

			if (state[i1][j1] == false && state[i2][j2] == false) {
				if (Math.sqrt(2) >= dist) {
					color[i1][j1] = -1;
					bState[i1][j1] = true;
					state[i1][j1] = true;

					color[i2][j2] = -1;
					bState[i2][j2] = true;
					state[i2][j2] = true;

					coor = String.format("%s:%s", index2coor(new Point(i1, j1)), index2coor(new Point(i2, j2)));
					break;
				}

			}

		}
		return coor;

	}

	public String wfirstdraw() {

		String coor = "";

		while (true) {

			int i1 = rd.nextInt(3) + 8;
			int j1 = rd.nextInt(3) + 8;
			int i2 = rd.nextInt(3) + 8;
			int j2 = rd.nextInt(3) + 8;

			double dist = Math.sqrt(Math.pow(Math.abs(i1 - i2), 2) + Math.pow(Math.abs(j1 - j2), 2));

			if (i1 == i2 && j2 == j1) {
				continue;
			}

			if (state[i1][j1] == false && state[i2][j2] == false) {
				if (Math.sqrt(2) >= dist) {
					color[i1][j1] = 1;
					wState[i1][j1] = true;
					state[i1][j1] = true;

					color[i2][j2] = 1;
					wState[i2][j2] = true;
					state[i2][j2] = true;

					coor = String.format("%s:%s", index2coor(new Point(i1, j1)), index2coor(new Point(i2, j2)));

					break;
				}

			}

		}
		return coor;

	}

	public void detectV() {

		int wcount;
		int bcount;

		for (int i = 0; i < 19; i++) { // ㅡ자라인 new
			wcount = 0;
			bcount = 0;
			for (int j = 0; j < 19; j++) {
				if (wState[i][j] == true) {
					endPoint[wcount] = new Point(locationx[i][j], locationy[i][j]);
					wcount++;
					System.out.println("W okok - " + wcount);
					if (wcount >= 6) {
						win = "백돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				} else if (wState[i][j] == false) {
					wcount = 0;
				}

				if (bState[i][j] == true) {
					endPoint[bcount] = new Point(locationx[i][j], locationy[i][j]);
					bcount++;
					System.out.println("B okok - " + bcount);
					if (bcount >= 6) {
						win = "흑돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;

					}
				} else if (bState[i][j] == false) {
					bcount = 0;
				}
			}

		}
		for (int j = 0; j < 19; j++) { // |자라인
			wcount = 0;
			bcount = 0;
			for (int i = 0; i < 19; i++) {
				if (wState[i][j] == true) {
					endPoint[wcount] = new Point(locationx[i][j], locationy[i][j]);
					wcount++;
					System.out.println("W okok | " + wcount);
					if (wcount >= 6) {
						win = "백돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				} else if (wState[i][j] == false) {
					wcount = 0;
				}
				if (bState[i][j] == true) {
					endPoint[bcount] = new Point(locationx[i][j], locationy[i][j]);
					bcount++;
					System.out.println("B okok | " + bcount);
					if (bcount >= 6) {
						win = "흑돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;

					}
				} else if (bState[i][j] == false) {
					bcount = 0;
				}
			}
		}
		for (int s = 0; s < 14; s++) { // \가운데포함아래쪽
			wcount = 0;
			bcount = 0;
			for (int i = 0; i < 19; i++) {
				int j = s + i;
				if (j > 18)
					continue;
				if (j > 18 || wState[j][i] == false) {
					wcount = 0;
				} else if (wState[j][i] == true) {
					endPoint[wcount] = new Point(locationx[j][i], locationy[j][i]);
					wcount++;
					System.out.println("W okok \\ " + wcount);
					if (wcount >= 6) {
						win = "백돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
				if (j > 18 || bState[j][i] == false) {
					bcount = 0;
				} else if (bState[j][i] == true) {
					endPoint[bcount] = new Point(locationx[j][i], locationy[j][i]);
					bcount++;
					System.out.println("B okok \\ " + bcount);
					if (bcount >= 6) {
						win = "흑돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
			}
		}
		for (int s = 1; s < 14; s++) { // \위쪽
			wcount = 0;
			bcount = 0;
			for (int j = 0; j < 19; j++) {
				int i = s + j;
				if (i > 18)
					continue;
				if (i > 18 || wState[j][i] == false) {
					wcount = 0;
				} else if (wState[j][i] == true) {
					endPoint[wcount] = new Point(locationx[j][i], locationy[j][i]);
					wcount++;
					System.out.println("W okok \\ " + wcount);
					if (wcount >= 6) {
						win = "백돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
				if (i > 18 || bState[j][i] == false) {
					bcount = 0;
				} else if (bState[j][i] == true) {
					endPoint[bcount] = new Point(locationx[j][i], locationy[j][i]);
					bcount++;
					System.out.println("B okok \\ " + bcount);
					if (bcount >= 6) {
						win = "흑돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
			}
		}
		for (int s = 0; s < 27; s++) {
			wcount = 0;
			bcount = 0;

			for (int i = 0; i < 19; i++) {
				int j = s - i; // 열
				if (j < 0)
					continue;
				if (i > 18 || j < 0 || j > 18 || wState[i][j] == false) {
					wcount = 0;
				} else if (wState[i][j] == true) {
					endPoint[wcount] = new Point(locationx[i][j], locationy[i][j]);
					wcount++;
					System.out.println("W okok / " + wcount);
					if (wcount >= 6) {
						win = "백돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}
				if (i > 18 || j < 0 || j > 18 || bState[i][j] == false) {
					bcount = 0;
				} else if (bState[i][j] == true) {
					endPoint[bcount] = new Point(locationx[i][j], locationy[i][j]);
					bcount++;
					System.out.println("B okok / " + bcount);
					if (bcount >= 6) {
						win = "흑돌 ";
						end = true;
						JOptionPane.showMessageDialog(null, win + "이 승리했습니다!", "알림창", JOptionPane.PLAIN_MESSAGE);
						return;
					}
				}

			}
		}

	}

	public void detectW4(int level) {
		detectLevel = level;
//      System.out.println("detectW 4 run");

		Wdetect = new int[19][19];

		String wdetect = "";
		String bdetect = "";

		int wcount = 0;

		for (int i = 0; i < 19; i++) { // ㅡ자라인 new
			for (int j = 0; j < 14; j++) {
				wcount = 0;
				if (state[i][j] == true) {
					Wdetect[i][j] = -1;
				}
				for (int k = j; k < j + 6; k++) {
					if (wState[i][k] == true) {
						wcount++;
					} else if (bState[i][k] == true || color[i][k] == 2) {
						wcount = 0;
						break;
					}
				}
				if (wcount >= detectLevel) {
					// System.out.println("W okok - " + j);
					for (int k = j; k < j + 6; k++) {
						if (state[i][k] == false) {
							Wdetect[i][k] += 1;

						}
					}
				}

			}
		}

		for (int i = 0; i < 19; i++) { // |자라인
			for (int j = 0; j < 14; j++) {
				if (state[j][i] == true) {
					Wdetect[j][i] = -1;
				}
				for (int k = j; k < j + 6; k++) {
					if (color[k][i] == 1) {
						wcount++;
					} else if (color[k][i] == -1 || color[k][i] == 2) {
						wcount = 0;
						break;
					}

				}
				if (wcount >= detectLevel) {
					// System.out.println("W okok | " + j);

//               System.out.println("Starting point : " + j);
					for (int k = j; k < j + 6; k++) {
						if (color[k][i] == 0) {
							Wdetect[k][i] += 1;
						}
					}
				}
				wcount = 0;
			}
		}

		for (int s = 0; s < 14; s++) { // \가운데포함아래쪽
			for (int i = 0; i < 19; i++) {
				// System.out.println("okok들어");
				int j = s + i;
				if (j > 18)
					continue;
				if (state[j][i] == true) {
					Wdetect[j][i] = -1;
				}
				wcount = 0;
				for (int k = j, q = i; k < j + 6; k++, q++) {
					if (k > 18 || bState[k][q] == true || color[k][q] == 2) {
						wcount = 0;
						break;
					} else if (wState[k][q] == true) {
						wcount++;
//                  System.out.println("wcount " + wcount);
					}

				}
				if (wcount >= detectLevel) {
					// System.out.println("W okok \\ " + j);
					for (int k = j, q = i; k < j + 6; k++, q++) {
						if (state[k][q] == false) {
							Wdetect[k][q] += 1;
						}
					}

				}
			}

		}
		for (int s = 1; s < 14; s++) { // \위쪽
			for (int i = 0; i < 19; i++) {
				int j = s + i;
				if (j > 18)
					continue;
				wcount = 0;
				if (state[i][j] == true) {
					Wdetect[i][j] = -1;
				}
				for (int k = j, q = i; k < j + 6; k++, q++) {
					if (k > 18 || bState[q][k] == true || color[q][k] == 2) {
						wcount = 0;
						break;
					} else if (wState[q][k] == true) {
						wcount++;
//                  System.out.println("wcount " + wcount);
					}

				}
				if (wcount >= detectLevel) {
					// System.out.println("W okok \\ " + j);
					for (int k = j, q = i; k < j + 6; k++, q++) {
						if (state[q][k] == false) {
							Wdetect[q][k] += 1;
						}
					}

				}
			}

		}
		for (int SPIN = 0; SPIN < 27; SPIN++) {
			for (int i = 0; i < 19; i++) {
				int j = SPIN - i; // 열
				if (j < 0)
					continue;
				if (i < 18 && j > 0 && j < 18) {
					if (state[i][j] == true) {
						Wdetect[i][j] = -1;
					}
				}

				wcount = 0;
				for (int k = j, q = i; k > j - 6; k--, q++) {
					if (q > 18 || k < 0 || k > 18 || bState[q][k] == true || color[q][k] == 2) {
						wcount = 0;
						break;
					} else if (wState[q][k] == true) {
						wcount++;
					}

				}
				if (wcount >= detectLevel) {
					// System.out.println("W okok / " + j);
					for (int k = j, q = i; k > j - 6; k--, q++) {
						if (state[q][k] == false) {
							Wdetect[q][k] += 1;
						}
					}

				}
			}
		}

	}

	public void detectB4(int level) {
		detectLevel = level;
//      System.out.println("detectB 4 run");

		Bdetect = new int[19][19];

		String wdetect = "";
		String bdetect = "";

		int bcount = 0;

		for (int i = 0; i < 19; i++) { // ㅡ자라인 new
			for (int j = 0; j < 14; j++) {
				bcount = 0;
				if (state[i][j] == true) {
					Bdetect[i][j] = -1;
				}
				for (int k = j; k < j + 6; k++) {
					if (bState[i][k] == true) {
						// System.out.println("decect - "+i + " "+k);
						bcount++;
					} else if (wState[i][k] == true || color[i][k] == 2) {
						bcount = 0;
						break;
					}
				}
				if (bcount >= detectLevel) {
					// System.out.println("B okok - " + j);
					for (int k = j; k < j + 6; k++) {
						if (state[i][k] == false) {
							Bdetect[i][k] += 1;
						}
					}
				}

			}
		}

		for (int i = 0; i < 19; i++) { // |자라인
			for (int j = 0; j < 14; j++) {
				if (state[j][i] == true) {
					Bdetect[j][i] = -1;
				}
				for (int k = j; k < j + 6; k++) {
					if (color[k][i] == -1) {
						// System.out.println("decect | "+k + " "+i);
						bcount++;
					} else if (color[k][i] == 1 || color[k][i] == 2) {
						bcount = 0;
						break;
					}
				}
				if (bcount >= detectLevel) {
					// System.out.println("B okok | " + j);

//               System.out.println("Starting point : " + j);
					for (int k = j; k < j + 6; k++) {
						if (color[k][i] == 0) {
							Bdetect[k][i] += 1;
						}
					}
				}
				bcount = 0;
			}
		}

		for (int s = 0; s < 14; s++) { // \가운데포함아래쪽
			for (int i = 0; i < 19; i++) {
				// System.out.println("okok들어");
				int j = s + i;
				if (j > 18)
					continue;
				if (state[j][i] == true) {
					Bdetect[j][i] = -1;
				}
				bcount = 0;
				for (int k = j, q = i; k < j + 6; k++, q++) {
					if (k > 18 || wState[k][q] == true || color[k][q] == 2) {
						bcount = 0;
						break;
					} else if (bState[k][q] == true) {
						// System.out.println("decect \\ "+k + " "+q);

						bcount++;
//                  System.out.println("wcount " + wcount);
					}
				}
				if (bcount >= detectLevel) {
					// System.out.println("B okok \\ " + j);
					for (int k = j, q = i; k < j + 6; k++, q++) {
						if (state[k][q] == false) {
							Bdetect[k][q] += 1;
						}
					}

				}
			}

		}
		for (int s = 1; s < 14; s++) { // \위쪽
			for (int i = 0; i < 19; i++) {
				int j = s + i;
				if (j > 18)
					continue;
				if (state[i][j] == true) {
					Bdetect[i][j] = -1;
				}
				bcount = 0;
				for (int k = j, q = i; k < j + 6; k++, q++) {
					if (k > 18 || wState[q][k] == true || color[q][k] == 2) {
						bcount = 0;
						break;
					} else if (bState[q][k] == true) {
						// System.out.println("decect \\ "+q + " "+k);

						bcount++;
//                  System.out.println("wcount " + wcount);
					}
				}
				if (bcount >= detectLevel) {
					// System.out.println("B okok \\ " + j);
					for (int k = j, q = i; k < j + 6; k++, q++) {
						if (state[q][k] == false) {
							Bdetect[q][k] += 1;
						}
					}

				}
			}

		}
		for (int SPIN = 0; SPIN < 27; SPIN++) {
			for (int i = 0; i < 19; i++) {
				int j = SPIN - i; // 열
				if (j < 0)
					continue;
				if (i < 18 && j > 0 && j < 18) {
					if (state[i][j] == true) {
						Bdetect[i][j] = -1;
					}
				}
				bcount = 0;

				for (int k = j, q = i; k > j - 6; k--, q++) {
					if (q > 18 || k < 0 || k > 18 || wState[q][k] == true || color[q][k] == 2) {
						bcount = 0;
						break;
					} else if (bState[q][k] == true) {
						// System.out.println("decect / "+q + " "+k);
						bcount++;
					}
				}
				if (bcount >= detectLevel) {
					// System.out.println("B okok / " + j);
					for (int k = j, q = i; k > j - 6; k--, q++) {
						if (state[q][k] == false) {
							Bdetect[q][k] += 1;
						}
					}

				}
			}
		}

	}

	public Point findMaxB() {
		int x = 0;
		int y = 0;

		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (Bdetect[i][j] > Bdetect[x][y]) {
					x = i;
					y = j;
				}

			}
		}

		return new Point(x, y);

	}

	public Point findMaxW() {
		int x = 0;
		int y = 0;

		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (Wdetect[i][j] > Wdetect[x][y]) {
					x = i;
					y = j;
				}

			}
		}

		return new Point(x, y);

	}

	public String drawByCom(boolean black) {

		if (black) { // COM가 흑돌이면 WDETECT한 후 가중치 찾아서 그 위치에 DRAW

			if (searchBDetect() != 0) {
				toDraw = findMaxB();
			} else if (searchWDetect() == 0) {
				detectB4(2);
				if (searchBDetect() == 0)
					detectB4(1);
				toDraw = findMaxB();
				detectB4(4);
			} else {
				toDraw = findMaxW();
			}
			if (state[toDraw.x][toDraw.y] == true) {
				// 그자리에 이미 돌 있으면
				return null;
			}
			color[toDraw.x][toDraw.y] = -1;
			bState[toDraw.x][toDraw.y] = true;
		} else { // COM가 백돌이면 BDETECT한 후 가중치 찾아서 그 위치에 DRAW
			
			if (searchWDetect() != 0) {
				toDraw = findMaxW();
			} else if (searchBDetect() == 0) {
				detectW4(2);
				if (searchWDetect() == 0)
					detectW4(1);
				toDraw = findMaxW();
				detectW4(4);
			} else {
				toDraw = findMaxB();
			}
			if (state[toDraw.x][toDraw.y] == true) {
				// 그자리에 이미 돌 있으면
				return null;
			}
			color[toDraw.x][toDraw.y] = 1;
			wState[toDraw.x][toDraw.y] = true;
		}

		state[toDraw.x][toDraw.y] = true;
		System.out.println(toDraw.x + " , " + toDraw.y);
		String coor = index2coor(toDraw);

		return coor;
	}

	public int searchWDetect() {
		int count = 0;
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (Wdetect[i][j] != 0 && Wdetect[i][j] != -1)
					count++;
			}
		}
		return count;
	}

	public int searchBDetect() {
		int count = 0;
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (Bdetect[i][j] != 0 && Bdetect[i][j] != -1)
					count++;
			}
		}
		return count;
	}
}