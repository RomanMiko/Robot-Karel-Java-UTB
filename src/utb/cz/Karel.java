package utb.cz;

import java.awt.*;

import javax.security.auth.x500.X500Principal;
import javax.swing.*;
import java.util.*;
import java.awt.List;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.w3c.dom.css.ElementCSSInlineStyle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.ProtectionDomain;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;


public class Karel {
	
	public enum Typ {
	    Karel, Dum, Zed, Znacka 
	}
	public enum SelectedButten {
	    Karel, Dum, Zed, ZnackaPlus, ZnackaMinus, Smaz,  SmazVse 
	}
	public enum Smer {
	    Vprevo, Nahoru, Vlevo, Dolu 
	}
	
	private class KPoint {
	    public int x;
	    public int y;

	    public KPoint(int x, int y) {
	        this.x = x;
	        this.y = y;
	    }
	}
	
	Field[][] kTown;
	int spacex = 10;
	int spacey = 10;
	
	SelectedButten kSelectedBtn = null;
	
	int karelx;
	int karely;
	Smer karelSmer;
	int dumx;
	int dumy;
	
	public static final int KAREL_SPEED_MIN = 0;
	public static final int KAREL_SPEED_MAX = 2000;
	public static final int KAREL_SPEED_INIT = 2000;
	int karelSpeed;
	private JSlider kSpeedSlider;
	String karelText = "";
	private JTextArea textArea;
	
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
			Image kVpravoImg = new ImageIcon(this.getClass().getResource("/KarelVpravoTransparetní.png")).getImage();
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
	//public static void main(String[] args) {			//for windowbuilder
	public void zacni() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Karel window = new Karel();			//for windowbuilder
					//window.frame.setVisible(true);		//for windowbuilder
					frame.setVisible(true);
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
		loadWorld("karel.save");
		this.karelSmer = Smer.Vprevo;
		this.karelSpeed = 2000;
		/*this.kTown = new Field[10][10];
		this.karelx = 0;
		this.karely = 0;
		this.karelSmer = Smer.Vprevo;
		this.karelSpeed = 2000;
		this.dumx = 0;
		this.dumy = 0;*/
		
		initialize();
	}
	public Karel(int x, int y) {
		this.kTown = new Field[x][y];
		this.karelx = 0;
		this.karely = 0;
		this.karelSmer = Smer.Vprevo;
		this.karelSpeed = 0;
		this.dumx = 0;
		this.dumy = 0;
		
		initialize();
	}
	public Karel(String s) {
		loadWorld(s);
		this.karelSmer = Smer.Vprevo;
		this.karelSpeed = 2000;
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		final int KAREL_WIDTH_MIN = 363;
		int frameWidth = 2*spacex+41*kTown.length+1 < KAREL_WIDTH_MIN?KAREL_WIDTH_MIN:2*spacex+41*kTown.length+1;
		int frameHeight = 2*spacey+41*kTown[0].length+1+240;
		//System.out.println(frameWidth);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int frameX = dim.width/2-frameWidth/2;
		int frameY = dim.height/2-frameHeight/2;
		
		frame = new JFrame();
		frame.setBounds(frameX,frameY,frameWidth,frameHeight);
		//frame.setBounds(frameX,frameY,900,700); // for windowbuilder
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
				if (kSelectedBtn != null && x > startx && x < endx && y > starty && y < endy ) {
					int kx = (x-11)/40;
					int ky = kTown[0].length - (y-14)/40 - 1;
					System.out.println(kTown[kx][ky]);
					switch (kSelectedBtn) {
					case Smaz:
						kTown[kx][ky] = null;
						break;
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
					case ZnackaPlus:
						if (kTown[kx][ky] != null && kTown[kx][ky].zed) break;
						if (kTown[kx][ky] == null) {
							kTown[kx][ky] = new Field(Typ.Znacka);
						} else if (kTown[kx][ky].count < 6) {
							kTown[kx][ky].count++;
						}
						break;
					case ZnackaMinus:
						if (kTown[kx][ky] != null && !kTown[kx][ky].zed) {
							if (kTown[kx][ky].count > 1) {
								kTown[kx][ky].count--;
							} else kTown[kx][ky] = null;
						}
						break;
					}
					
					panel_town.repaint();
					
					System.out.println("mouse clik in" + (x-11)/40 + ", " + (kTown[0].length - (y-14)/40 - 1));
					
					
					
				}
			}
		});
		
		JPanel panel_toolbar = new JPanel();
		
		ImageIcon kZedIcon = new ImageIcon(panel_toolbar.getClass().getResource("/Zed.png"));
		ImageIcon kDumIcon = new ImageIcon(panel_toolbar.getClass().getResource("/Dum.png"));
		ImageIcon kRobotIcon = new ImageIcon(panel_toolbar.getClass().getResource("/KarelVpravo.png"));
		ImageIcon kZnackaPlusIcon = new ImageIcon(panel_toolbar.getClass().getResource("/ZnackaPlus.png"));
		ImageIcon kZnackaMinusIcon = new ImageIcon(panel_toolbar.getClass().getResource("/ZnackaMinus.png"));
		
		frame.getContentPane().add(panel_toolbar, BorderLayout.SOUTH);
		panel_toolbar.setLayout(new BoxLayout(panel_toolbar, BoxLayout.Y_AXIS));
		
		JPanel panel_toolbar_1 = new JPanel();
		panel_toolbar.add(panel_toolbar_1);
		
		JLabel kCoorLbl = new JLabel(". x .");
		panel_toolbar_1.add(kCoorLbl);
		
		/*panel_town.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p = getKarelCoordinates(e.getX(),e.getY());
				kCoorLbl.setText((int) p.getX() + " X " + (int) p.getY());
			}
		});*/
		
		JButton kDumBtn = new JButton(scaleIcon(kDumIcon));
		panel_toolbar_1.add(kDumBtn);
		kDumBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedBtn = SelectedButten.Dum;
			}
		});
		//kDumBtn.setBackground(Color.WHITE);
		//kDumBtn.setName("kDumBtn");
		
		JButton kRobotBtn = new JButton(scaleIcon(kRobotIcon));
		panel_toolbar_1.add(kRobotBtn);
		kRobotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedBtn = SelectedButten.Karel;
			}
		});
		//kRobotBtn.setBackground(Color.WHITE);
		//kRobotBtn.setSize(40, 40);
		
		JButton kZnackaPlusBtn = new JButton(scaleIcon(kZnackaPlusIcon));
		panel_toolbar_1.add(kZnackaPlusBtn);
		kZnackaPlusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedBtn = SelectedButten.ZnackaPlus;
			}
		});
		//kZnackaPlusBtn.setBackground(Color.WHITE);
		
		JButton kZnackaMinusBtn = new JButton(scaleIcon(kZnackaMinusIcon));
		panel_toolbar_1.add(kZnackaMinusBtn);
		kZnackaMinusBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedBtn = SelectedButten.ZnackaMinus;
			}
		});
		kZnackaMinusBtn.setBackground(Color.WHITE);
				
		JButton kZedBtn = new JButton(scaleIcon(kZedIcon));
		panel_toolbar_1.add(kZedBtn);
		kZedBtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
				kSelectedBtn = SelectedButten.Zed;
			}
		});
		kZedBtn.setBackground(Color.WHITE);
		
		JPanel panel_toolbar_2 = new JPanel();
		panel_toolbar.add(panel_toolbar_2);
		
		JButton kSmazBtn = new JButton("Smaž");
		panel_toolbar_2.add(kSmazBtn);
		kSmazBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedBtn = SelectedButten.Smaz;
			}
		});
		
		JButton kSmazVseBtn = new JButton("SmažVše");
		panel_toolbar_2.add(kSmazVseBtn);
		kSmazVseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kSelectedBtn = SelectedButten.SmazVse;
				int x = kTown.length;
				int y = kTown[0].length;
				kTown = new Field[x][y];
				karelx = 0;
				karely = 0;
				karelSmer = Smer.Vprevo;
				dumx = 0;
				dumy = 0;
				panel_town.repaint();
			}
		});
		
		JButton kNahrajBtn = new JButton("Nahrát");
		panel_toolbar_2.add(kNahrajBtn);
		kNahrajBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadWorld("karel.save");
			}
		});		
				
		JButton kUlozBtn = new JButton("Uložit");
		panel_toolbar_2.add(kUlozBtn);
		kUlozBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveWorld();
			}
		});
				
		JPanel panel_toolbar_3 = new JPanel();
		panel_toolbar.add(panel_toolbar_3);
		
		JLabel speedLbl = new JLabel("Rychlost");
		panel_toolbar_3.add(speedLbl);
		
		kSpeedSlider = new JSlider(JSlider.HORIZONTAL,
                							KAREL_SPEED_MIN,
                							KAREL_SPEED_MAX,
                							KAREL_SPEED_INIT);

		kSpeedSlider.setMajorTickSpacing(1000);
		kSpeedSlider.setMinorTickSpacing(100);
		kSpeedSlider.setPaintTicks(true);
		//kSpeedSlider.setPaintLabels(true);
		panel_toolbar_3.add(kSpeedSlider);
		
		JPanel panel_toolbar_4 = new JPanel();
		panel_toolbar_4.setSize(400, 200);
		panel_toolbar_4.setBackground(Color.white);
		panel_toolbar.add(panel_toolbar_4);
		
		int TAWidth = (int) Math.round(frameWidth/12.5);
		textArea = new JTextArea(5,TAWidth);
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel_toolbar_4.add(scrollPane);
		
	
		
	}

	// karlovy soukrome funkce
	private ImageIcon scaleIcon(ImageIcon i) {
		return new ImageIcon(i.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
	}
	
	private Point getKarelCoordinates(int x, int y) {
		return new Point(( x - spacex - 1 ) / 41 + 1,
						 kTown[0].length - ( y - spacey - 1 ) / 41
				   );
	}
	
	private void slowDownRepaint() {
		try {
			Thread.sleep(kSpeedSlider.getValue());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		frame.getComponent(0).repaint();
	}

	private void addKarelText(String s) {
		karelText += "\n" + s;
		textArea.setText(karelText);
	}
	
	private void saveWorld() {
		String save = "";
		for (int j = kTown.length; j >= -1; j--) {
			for (int i = -1; i <= kTown[0].length; i++) {
				if(i == -1 || j == -1 || i == kTown.length || j == kTown[0].length) {
					save += "X";
				} else if (i == karelx && j == karely) {
					save += "K";
				} else if (i == dumx && j == dumy) {
					save += "H";
				} else if (kTown[i][j] == null) {
					save += "_";
				} else if (kTown[i][j].zed) {
					save += "#";
				} else if (kTown[i][j].count > 0) {
					save += Integer.toString(kTown[i][j].count);
				}
			}
			if (j != -1) save += "\n";
		}
		//System.out.println(save);
		writeToFile(save);
	}
	
	private void loadWorld(String fileName) {
		String[] s = readFromFile(fileName);
		if(s == null) {
			kTown = new Field[10][10];
			karelx = 0;
			karely = 0;
			karelSmer = Smer.Vprevo;
			karelSpeed = 2000;
			dumx = 0;
			dumy = 0;
			return;
		}
		System.out.println(s);
		kTown = new Field[s.length-2][s[0].length()-2];
		for (int i = 1; i < (s.length - 1); i++) {
			char[] r = s[i].toCharArray();
			for (int j = 1; j < (s.length - 1); j++) {
				int y = s.length - i - 2;
				int x = j - 1;
				if (r[j] == 'K') {
					karelx = x;
					karely = y;
					karelSmer = Smer.Vprevo;
				} else if (r[j] == 'H') {
					dumx = x;
					dumy = y;
				} else if (r[j] == '#') {
					kTown[x][y] = new Field(Typ.Zed);
				} else if (r[j] == '1') {
					kTown[x][y] = new Field(Typ.Znacka);
				} else if (r[j] == '2') {
					kTown[x][y] = new Field(Typ.Znacka);
					kTown[x][y].count++;
				} else if (r[j] == '3') {
					kTown[x][y] = new Field(Typ.Znacka);
					kTown[x][y].count = 3;
				} else if (r[j] == '4') {
					kTown[x][y] = new Field(Typ.Znacka);
					kTown[x][y].count = 4;
				} else if (r[j] == '5') {
					kTown[x][y] = new Field(Typ.Znacka);
					kTown[x][y].count = 5;
				} else if (r[j] == '6') {
					kTown[x][y] = new Field(Typ.Znacka);
					kTown[x][y].count = 6;
				}
			}
		}
		if (frame != null) {
			frame.getComponent(0).repaint();
		}
	}
	
	private void writeToFile(String t) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("saves/karel.save")))
		{
		        bw.write(t);
		        bw.flush();
		}
		catch (Exception e)
		{
		        System.err.println("Do souboru se nepovedlo zapsat." + e);
		}
	}
	
	private String[] readFromFile(String fileName) {
		System.out.println("Vypisuji celý soubor:");
		try (BufferedReader br = new BufferedReader(new FileReader("saves/" + fileName)))
		{
		        String s;
		        ArrayList<String> sal = new ArrayList<>();
		        while ((s = br.readLine()) != null)
		        {
		                System.out.println(s);
		                sal.add(s);
		        }
		        return sal.toArray(new String[0]);
		}
		catch (Exception e)
		{
		        System.err.println("Chyba při četení ze souboru." + e);
		        
		}
		return null;
	}
	
	// Karlovy instrukce
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
		if (x >= 0 && y >= 0 && x < kTown.length && y < kTown[0].length) {
			if (kTown[x][y] != null && !kTown[x][y].zed) {
				karelx = x;
				karely = y;
			} else if (kTown[x][y] == null) {
				karelx = x;
				karely = y;
			} else addKarelText("Nemůžu učinit KROK, přede mnou je zeď.");
		} else addKarelText("Nemůžu učinit KROK, přede mnou je zeď.");
		slowDownRepaint();
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
		slowDownRepaint();
	}
	
	public void polozZnacku() {
		if (kTown[karelx][karely] != null && kTown[karelx][karely].zed) return;
		if (kTown[karelx][karely] == null) {
			kTown[karelx][karely] = new Field(Typ.Znacka);
		} else if(kTown[karelx][karely].count < 6) {
			kTown[karelx][karely].count++;
		} else addKarelText("Nemůžu položit značku, pole je plné.");
		slowDownRepaint();
	}
	
	public void zvedniZnacku() {
		if (kTown[karelx][karely] != null && kTown[karelx][karely].zed) return;
		if (kTown[karelx][karely] != null && kTown[karelx][karely].count == 1) {
			kTown[karelx][karely] = null;
		} else if(kTown[karelx][karely].count > 1) {
			kTown[karelx][karely].count--;
		} else addKarelText("Nemůžu zvednout značku, pole je prazdné.");
		slowDownRepaint();
		
	}
	
	private boolean jeZed(int x, int y) {
		System.out.println(kTown.length + ", " + kTown[0].length);
		System.out.println(x + ", " + y);
		if(x < 0 || y < 0 || x >= kTown.length || y >= kTown[0].length) return true;
		if(kTown[x][y] == null) return false;
		return  kTown[x][y].zed;
	}
	
	public boolean jePredemnouZed() {
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
		return jeZed(x, y);
	}

	public boolean jeVlevoZed() {
		int x = karelx;
		int y = karely;
		switch (karelSmer) {
		case Vprevo:
			y++;
			break;
		case Nahoru:
			x--;
			break;
		case Vlevo:
			y--;
			break;
		case Dolu:
			x++;
			break;
		}
		return jeZed(x, y);
	}

	public boolean jeVpravoZed() {
		int x = karelx;
		int y = karely;
		switch (karelSmer) {
		case Vprevo:
			y--;
			break;
		case Nahoru:
			x++;
			break;
		case Vlevo:
			y++;
			break;
		case Dolu:
			x--;
			break;
		}
		return jeZed(x, y);
	}

	public boolean jePredemnouVolno() {
		return !jePredemnouZed() ;
	}

	public boolean jeVlevoVolno() {
		return !jeVlevoZed();
	}

	public boolean jeVpravoVolno() {
		return !jeVpravoZed();
	}
	
	public boolean jeZdeZnacka() {
		if(kTown[karelx][karely] == null) return false;
		return !kTown[karelx][karely].zed;
	}

	public int pocetZnacek() {
		if(kTown[karelx][karely] == null || kTown[karelx][karely].zed) return 0;
		return kTown[karelx][karely].count;
	}

	public boolean jeZdeDomecek() {
		return karelx == dumx && karely == dumy;
	}
	
	public boolean jeOtocenyNaSever() {
		return karelSmer == Smer.Nahoru;
	}

	public boolean jeOtocenyNaJih() {
		return karelSmer == Smer.Dolu;
	}

	public boolean jeOtocenyNaZapad() {
		return karelSmer == Smer.Vlevo;
	}

	public boolean jeOtocenyNaVychod() {
		return karelSmer == Smer.Vprevo;
	}
	
	
}// end Karel
