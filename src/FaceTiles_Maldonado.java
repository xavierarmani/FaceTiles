/* Author: Xavier Maldonado
 * Instructor: Dr. Klump
 * Object-Oriented Programming
 * 14 March 2016 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.io.*;

/* The Face class, this stores each face’s row, column, mouth type
 * (sad, happy, or indifferent), and color. It also has corresponding
 * constructors, get and set functions, and a toString function that lists
 * the values of all the private data members of the class*/

class Face {
	private int row;
	private int col;
	private int type;
	private int r;
	private int b;
	private int g;
	private int strtAng;
	private int arcAng;
	private int smileHi;
	private int smileWidth;
	private int smileX;
	private int smileY;
	private String comment;
	private Color randColor;

	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
	public int getType() {
		return type;
	}
	public int getStartAngle(){
		return strtAng;
	}
	public int getArcAngle(){
		return arcAng;
	}
	public int getSmileHeight(){
		return smileHi;
	}
	public int getSmileWidth(){
		return smileWidth;
	}
	public int getSmileX(){
		return smileX;
	}
	public int getSmileY(){
		return smileY;
	}
	public void setType(int type){
		if (type == 0){
			strtAng = 0;
			arcAng = -180;
			smileHi = 25;
			smileWidth = 30;
			smileX = 5;
			smileY = 10;
			comment = "Happy      ";
		}
		if (type == 1){
			strtAng = 0;
			arcAng = 180;
			smileHi = 9;
			smileWidth = 25;
			smileX = (int)7.8;
			smileY = 25;
			comment = "Sad        ";
		}
		if (type == 2) {
			strtAng = 0;
			arcAng = 180;
			smileHi = 0;
			smileWidth = 30;
			smileX = 5;
			smileY = 25;
			comment = "Indifferent";
		}
	}
	public void setColor(int red, int green, int blue ){
		randColor = new Color(red,green,blue);
		r = red;
		b = blue;
		g = green;
	}
	public Color getColor() {
		return randColor;
	}
	public Face(int row, int col, int type, int red, int green, int blue){
		this.row = row;
		this.col = col;
		setType(type);
		setColor(red, green, blue);
	}
	public Face(int row, int col) {
		this(row,col,0,0,0,0);
	}
	public String toString(){
		return String.format("Face:%s  Row:%1d Column:%1d "
				+ "Red Value:%4d Blue Value:%4d "
				+ "Green Value:%4d", comment,row,col,r,b,g);
	}
}

/* The ButtonHandler class responds to a button click, then
 * randomizes the face type, col, rows, and colors then repaints.
 * Also prints new information about the repainted tiles using FaceIO*/
class ButtonHandler implements ActionListener {
	private FaceTileFrame tf;
	private ArrayList<Face> tiles;
	public ButtonHandler(){
		tf = null;
		tiles = null;
	}
	public ButtonHandler(FaceTileFrame tf, ArrayList<Face> tiles) {
		this.tf = tf;
		this.tiles = tiles;
	}
	public void actionPerformed(ActionEvent e){
		if(tf != null){
			FaceRandomizer tr = new FaceRandomizer();
			FaceIO tp = new FaceIO();
			tp.print(tiles);
			tr.changeTiles(tiles);
			tf.repaint();
		}
	}
}

/* The FaceTilePanel that occupies most of the FaceTileFrame and paints and displays the set of faces.
 * Its paintComponent function will be responsible for rendering each face in its proper place with
 * the proper color and attributes*/

class FaceTilePanel extends JPanel {
	private ArrayList<Face> tiles;
	private FaceTilePanel(){
		tiles = null;
	}
	public FaceTilePanel(ArrayList<Face> tiles) {
		this.tiles = tiles;
	}
	/* line 160 draws the face outline
	 * line 161 draws the left eye
	 * line 162 draws the right eye
	 * line 163 draws the smile status*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color col;
		for (Face f: tiles) {
			g.setColor(f.getColor());
			g.drawOval(f.getRow()*40, f.getCol()*40, 40, 40);
			g.drawOval(f.getRow()*40 + 8, f.getCol()*40+8, 8, 8);
			g.drawOval(f.getRow()*40 + 24, f.getCol()*40+8, 8, 8);
			g.drawArc(f.getRow()*40 + f.getSmileX(), f.getCol()*40 +
					f.getSmileY(),f.getSmileWidth(), f.getSmileHeight(), f.getStartAngle(), f.getArcAngle());
			col = f.getColor();

		}
	}
}

/* This FaceIO is able to write the list of Face objects to the console window as well as to a file*/
class FaceIO {
	public boolean writeTiles(ArrayList<Face> tiles, String fname){
		return writeTiles(tiles,new File(fname));
	}
	public boolean writeTiles(ArrayList<Face> tiles, File f) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			for (Face t : tiles) {
				bw.write(t.toString());
				bw.newLine();
			}
			bw.close();
			return true;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	public void print(ArrayList<Face> tiles){
		for (Face t: tiles) {
			System.out.println(t.toString());
		}
	}
}
/* The FaceTileFrame class holds two panels, a FaceTilePanel and another panel that
 * contains the Randomize button. Also has a BorderLayout to arrange them */

class FaceTileFrame extends JFrame{
	private FaceTileFrame tf;
	private ArrayList<Face> tiles;
	private FaceRandomizer tr;
	private FaceIO tp;
	private JPanel buttonPanel;
	public FaceTileFrame(){
		setupUI(null);
		tf = null;
		tiles = null;
	}
	public FaceTileFrame(ArrayList<Face> tiles, FaceTileFrame tf) {
		this.tiles = tiles;
		this.tf = tf;
		setupUI(tiles);
		tr = new FaceRandomizer();
		tp = new FaceIO();
	}
	public void setupMenu(){
		JMenuBar mbar = new JMenuBar();
		JMenu mnuFile = new JMenu("File");
		JMenuItem miSave = new JMenuItem("Save");
		miSave.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent e) {
				FaceIO fio = new FaceIO();
				if (fio.writeTiles(tiles, "faces.txt") == true){
					JOptionPane.showMessageDialog(null, "Success!");
				} else {
					JOptionPane.showMessageDialog(null, "Failure");
				}
			}
		});
		mnuFile.add(miSave);
		JMenuItem miExit = new JMenuItem("Exit");
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		mnuFile.add(miExit);
		mbar.add(mnuFile);
		JMenu mnuEdit = new JMenu("Edit");
		JMenuItem miRand = new JMenuItem("Randomize!");
		miRand.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e){
				tp.print(tiles);
				tr.changeTiles(tiles);
				repaint();
			}
		});
		mnuEdit.add(miRand);
		mbar.add(mnuEdit);
		setJMenuBar(mbar);
	}
	public void setupUI(ArrayList<Face> tiles) {
		setupMenu();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FaceTilePanel tp = new FaceTilePanel(tiles);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		buttonPanel = new JPanel();
		JButton btnPress = new JButton("Randomize!");
		btnPress.addActionListener(new ButtonHandler(this,tiles));
		buttonPanel.add(btnPress);

		c.add(tp, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.SOUTH);
	}
}
/* The main function creates an array of 100 tiles. It creates and
 * uses a FaceIO to print the tiles to System.out. It creates and
 * shows the FaceTileFrame to display the faces.*/
public class FaceTiles_Maldonado {
	public static void main(String [] args) {
		Face tile;
		ArrayList<Face> tiles = new ArrayList<Face>();
		FaceRandomizer tr = new FaceRandomizer();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				tile = tr.buildTile(row, col);
				tiles.add(tile);
			}
		}
		FaceTileFrame tf = new FaceTileFrame(tiles, null);
		FaceIO tp = new FaceIO();
		tp.print(tiles);
		tf.setBounds(10,10,420,500);
		tf.setVisible(true);
	}
}
/* FaceRandomizer class has functions for building a face with random parameters,
for changing an existing face attributes to random values, and for 
changing the attributes of an entire array of faces. */

class FaceRandomizer {
	private Random rnd;
	public FaceRandomizer() {
		rnd = new Random();
	}
	public Face buildTile(int row, int col) {
		//randomly generate red, green, blue, letter, type of face
		Face result = new Face(row,col);
		result = changeTile(result);
		return result;
	}
	public Face changeTile(Face tile) {
		Random r = new Random();
		int red = rnd.nextInt(256);
		int green = rnd.nextInt(256);
		int blue = rnd.nextInt(256);
		int type = rnd.nextInt(3);

		tile.setColor(red,green,blue);
		tile.setType(type);
		return tile;
	}
	public void changeTiles(ArrayList<Face> tiles) {
		for (Face f : tiles) {
			changeTile(f);
		}
	}
}

