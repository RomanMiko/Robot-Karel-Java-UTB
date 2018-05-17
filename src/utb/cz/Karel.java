package utb.cz;

import java.awt.*;
import javax.swing.*;
import java.util.*;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Karel {
	
	public enum Typ {
	    Karel, Dum, Zed, Znacka, Smaz, SmazVse 
	}
	public enum Smer {
	    Vprevo, Nahoru, Vlevo, Dolu 
	}
	
	
	Field[][] kTown;
	int spacex = 10;
	int spacey = 10;
	
	Typ kSelectedType = null;
	
	int karelx;
	int karely;
	Smer karelSmer;
	int dumx;
	int dumy;
	
	int[] karelPoint = new int[2];
	
	class Field {
		int count;
		boolean zed;
		
		public Field(Typ t) {
			switch (t) {
			case Zed:
				zed = true;
				this.count = 0;
				break;
			case Znacka:
				this.count = 1;
				break;

			default:
				break;
			}
		}
		
				
	}
	
	
	class Town extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			Color kBlack = new Color(0, 0, 0);
			Color kWhite = new Color(255, 255, 255);
			
			Image kDumImg = new ImageIcon(this.getClass().getResource("/Dum.png")).getImage();
			Image kDoluImg = new ImageIcon(this.getClass().getResource("/KarelDolu.png")).getImage();
			Image kNahoruImg = new ImageIcon(this.getClass().getResource("/KarelNahoru.png")).getImage();
			Image kVlevoImg = new ImageIcon(this.getClass().getResource("/KarelVlevo.png")).getImage();
			Image kVpravoImg = new ImageIcon(this.getClass().getResource("/KarelVpravo.png")).getImage();
			Image kZedImg = new ImageIcon(this.getClass().getResource("/Zed.png")).getImage();
			Image kZnacka1Img = new ImageIcon(this.getClass().getResource("/Znacka1.png")).getImage();
			Image kZnacka2Img = new ImageIcon(this.getClass().getResource("/Znacka2.png")).getImage();
			Image kZnacka3Img = new ImageIcon(this.getClass().getResource("/Znacka3.png")).getImage();
			Image kZnacka4Img = new ImageIcon(this.getClass().getResource("/Znacka4.png")).getImage();
			Image kZnacka5Img = new ImageIcon(this.getClass().getResource("/Znacka5.png")).getImage();
			Image kZnacka6Img = new ImageIcon(this.getClass().getResource("/Znacka6.png")).getImage();
			
			int width = kTown.length;
			int height = kTown[0].length;
			int fieldWidth = 40;
			int fieldHeight = 40;
			int townWidth = width*fieldWidth + 2*spacex + width;
			int townHeight = height*fieldHeight + 2*spacey + height;
			
			this.setSize(townWidth, townHeight);
			g.setColor(kWhite);
			g.fillRect(spacex + 1, spacey + 1, townWidth - 2 * spacex, townHeight - 2 * spacey);
			g.setColor(kBlack);
			g.drawRect(spacex + 1, spacey + 1, townWidth - 2 * spacex, townHeight - 2 * spacey);
			
			for (int i = 1; i < height; i++) {
				int startLineX = spacex + 1;
				int startLineY = spacey + 1 + fieldHeight*i + i;
				int finishLineX = townWidth - spacex;
				int finishLineY = startLineY;
				
				g.drawLine(startLineX, startLineY, finishLineX, finishLineY);
			}
			
			for (int i = 1; i < width; i++) {
				int startLineX = spacex + 1 + fieldWidth*i + i;
				int startLineY = spacey + 1;
				int finishLineX = startLineX;
				int finishLineY = townHeight - spacey;
				
				g.drawLine(startLineX, startLineY, finishLineX, finishLineY);	
			}
			
			for (int i = 0; i < kTown.length; i++) {
				for (int j = 0; j < kTown[i].length; j++) {
					int x1 = spacex + 2 + i*fieldWidth + i;
					int y1 = spacey + 2 + (kTown[i].length - j - 1)*fieldHeight + (kTown[i].length - j - 1);
					int x2 = spacex + 2 + i*fieldWidth + i + fieldWidth;
					int y2 = spacey + 2 + (kTown[i].length - j - 1)*fieldHeight + (kTown[i].length - j - 1) + fieldHeight;
					int widthImg = fieldWidth;
					int heigthImg = fieldHeight;
					Image img = null;
					if (i == dumx && j == dumy) {
						g.drawImage(kDumImg, x1, y1, x2, y2, 0, 0,widthImg,heigthImg, null);
					}
					if (kTown[i][j] != null && kTown[i][j].zed) {
						g.drawImage(kZedImg, x1, y1, x2, y2, 0, 0,widthImg,heigthImg, null);
					} else if (kTown[i][j] != null && kTown[i][j].count > 0) {
						switch (kTown[i][j].count) {
						case 1:
							img = kZnacka1Img;
							break;
						case 2:
							img = kZnacka2Img;
							break;
						case 3:
							img = kZnacka3Img;
							break;
						case 4:
							img = kZnacka4Img;
							break;
						case 5:
							img = kZnacka5Img;
							break;
						case 6:
							img = kZnacka6Img;
							break;

						default:
							break;
						}
						g.drawImage(img, x1, y1, x2, y2, 0, 0,widthImg,heigthImg, null);
					}
					if (i == karelx && j == karely) {
						x1+=4;
						y1+=4;
						x2-=4;
						y2-=4;
						widthImg-=8;
						heigthImg-=8;
						switch (karelSmer) {
						case Vprevo:
							img = kVpravoImg;
							break;
						case Nahoru:
							img = kNahoruImg;
							break;
						case Vlevo:
							img = kVlevoImg;
							break;
						case Dolu:
							img = kDoluImg;
							break;
						}
						g.drawImage(img, x1, y1, x2, y2, 0, 0,widthImg,heigthImg, null);
					}
				}
			}
		}
		
	}// end Town class
	
	
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Karel window = new Karel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Karel() {
		this.kTown = new Field[10][10];
		this.karelx = 0;
		this.karely = 0;
		this.karelSmer = Smer.Vprevo;
		this.dumx = 0;
		this.dumy = 0;
		
		initialize();
	}
	public Karel(int x, int y) {
		this.kTown = new Field[x][y];
		this.karelx = 0;
		this.karely = 0;
		this.karelSmer = Smer.Vprevo;
		this.dumx = 0;
		this.dumy = 0;
		
		initialize();
	}
	public Karel(int karelx, int karely, Smer karelSmer, int dumx, int dumy, Field[][] t) {
		this.kTown = t;
		this.karelx = karelx;
		this.karely = karely;
		this.karelSmer = karelSmer;
		this.dumx = dumx;
		this.dumy = dumy;
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		int frameWidth = 2*spacex+41*kTown.length+1;
		int frameHeight = 2*spacey+41*kTown[0].length+1+80;
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int frameX = dim.width/2-frameWidth/2;
		int frameY = dim.height/2-frameHeight/2;
		
		frame = new JFrame();
		frame.setBounds(frameX,frameY,frameWidth,frameHeight);
		//frame.setBounds(frameX,frameY,420,510); // for windowbuilder
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel_town = new Town();
		frame.getContentPane().add(panel_town, BorderLayout.CENTER);
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				System.out.println(e.getPoint());
				int startx = spacex;
				int starty = spacey;
				int endx = kTown.length*40 + spacex + kTown.length + 1 + 2;
				int endy = kTown[0].length*40 + spacey + kTown[0].length + 1 + 2 ;
				if (kSelectedType != null && x > startx && x < endx && y > starty && y < endy ) {
					int kx = (x-11)/40;
					int ky = kTown[0].length - (y-14)/40 - 1;
					System.out.println(kTown[kx][ky]);
					switch (kSelectedType) {
					case Karel:
						if (kTown[kx][ky] != null && kTown[kx][ky].zed) break;
						karelx = kx;
						karely = ky;
						karelSmer = Smer.Vprevo;
						break;
					case Dum:
						if (kTown[kx][ky] != null && kTown[kx][ky].zed) break;
						dumx = kx;
						dumy = ky;
						break;
					case Zed:
						if (kTown[kx][ky] != null && !kTown[kx][ky].zed) break;
						if (karelx == kx && karely == ky) break;
						if (dumx == kx && dumy == ky) break;
						kTown[kx][ky] = new Field(Typ.Zed);
						break;
					case Znacka:
						if (kTown[kx][ky] != null && kTown[kx][ky].zed) break;
						if (kTown[kx][ky] == null) {
							kTown[kx][ky] = new Field(Typ.Znacka);
						} else if (kTown[kx][ky].count < 6) {
							kTown[kx][ky].count++;
						}
						break;
					}
					
					panel_town.repaint();
					
					System.out.println("mouse clik in" + (x-11)/40 + ", " + (kTown[0].length - (y-14)/40 - 1));
					
					
					
				}
			}
		});
		
		JPanel panel_toolbar = new JPanel();
		frame.getContentPane().add(panel_toolbar, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("New label");
		panel_toolbar.add(lblNewLabel);
		ImageIcon kZedIcon = new ImageIcon(panel_toolbar.getClass().getResource("/Zed.png"));
		ImageIcon kDumIcon = new ImageIcon(panel_toolbar.getClass().getResource("/Dum.png"));
		ImageIcon kRobotIcon = new ImageIcon(panel_toolbar.getClass().getResource("/KarelVpravo.png"));
		ImageIcon kZnackaIcon = new ImageIcon(panel_toolbar.getClass().getResource("/Znacka1.png"));
			
		JButton kDumBtn = new JButton(kDumIcon);
		kDumBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedType = Typ.Dum;
			}
		});
		kDumBtn.setBackground(Color.WHITE);
		kDumBtn.setName("kDumBtn");
		panel_toolbar.add(kDumBtn);
		
		JButton kRobotBtn = new JButton(kRobotIcon);
		kRobotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedType = Typ.Karel;
			}
		});
		kRobotBtn.setBackground(Color.WHITE);
		kRobotBtn.setSize(40, 40);
		panel_toolbar.add(kRobotBtn);
		
		JButton kZnackaBtn = new JButton(kZnackaIcon);
		kZnackaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedType = Typ.Znacka;
			}
		});
		kZnackaBtn.setBackground(Color.WHITE);
		panel_toolbar.add(kZnackaBtn);
		
		JButton kZedBtn = new JButton(kZedIcon);
		kZedBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedType = Typ.Zed;
			}
		});
		kZedBtn.setBackground(Color.WHITE);
		kZedBtn.setSize(40, 40);
		panel_toolbar.add(kZedBtn);
		
		
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setSize(40, 40);
		panel_toolbar.add(btnNewButton);
	}

	public void krok() {
		int x = karelx;
		int y = karely;
		switch (karelSmer) {
		case Vprevo:
			x++;
			break;
		case Nahoru:
			y++;
			break;
		case Vlevo:
			x--;
			break;
		case Dolu:
			y--;
			break;
		}
		if (x > 0 && y > 0 && x < kTown.length && y < kTown[0].length) {
			
		}	
	}
	
	public void vleloVbok() {
		switch (karelSmer) {
		case Vprevo:
			karelSmer = Smer.Nahoru;
			break;
		case Nahoru:
			karelSmer = Smer.Vlevo;
			break;
		case Vlevo:
			karelSmer = Smer.Dolu;
			break;
		case Dolu:
			karelSmer = Smer.Vprevo;
			break;
		}
	}
	
	public void polozZnacku() {
		if (kTown[karelx][karely] != null && kTown[karelx][karely].zed) return;
		if (kTown[karelx][karely] == null) {
			kTown[karelx][karely] = new Field(Typ.Znacka);
		} else if(kTown[karelx][karely].count < 6) {
			kTown[karelx][karely].count++;
		}
	}
	
	public void zvedniZnacku() {
		if (kTown[karelx][karely] != null && kTown[karelx][karely].zed) return;
		if (kTown[karelx][karely] != null && kTown[karelx][karely].count == 1) {
			kTown[karelx][karely] = null;
		} else if(kTown[karelx][karely].count > 1) {
			kTown[karelx][karely].count--;
		}
	}
}// end Karel