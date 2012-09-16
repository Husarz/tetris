import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.event.*;

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

/**
 *actionlistener - do obslugi menu
 *keylistener - klawa
 */
public class MainControl extends JFrame 
implements ActionListener, KeyListener {

	JMenuBar jMenuBar;
	JMenu jMenuGame;
	JMenuItem menuItem;
	JButton hsize, vsize;
	Timer timer; 
	boolean cancel, con = false, between=false; // sprawdza czy jest pause
	TetrionView tetrionview;
	Tetrion tetrion;
	PanelGameStatus panelgamestatus;
	String Player, port, host, conect;
	short which = 0;
	static ViewTetrion h;
	
	public MainControl()
	throws HeadlessException {
		super("AleJanek"); // new JFrame() konstruktor nadklasy
		
		jMenuBar = new JMenuBar();
		
		jMenuGame = new JMenu("Game");
	
		menuItem = new JMenuItem("Pause");
		menuItem.addActionListener(this);
		jMenuGame.add(menuItem);
		
		menuItem = new JMenuItem("New");
		menuItem.addActionListener(this);
		jMenuGame.add(menuItem);
		
		menuItem = new JMenuItem("End");
		menuItem.addActionListener(this);
		jMenuGame.add(menuItem);
		
		jMenuBar.add(jMenuGame);
//		+++++++++++++++++++++++++++++++
		jMenuGame = new JMenu("MultiPlayer");
		
		menuItem = new JMenuItem("Add player");
		menuItem.addActionListener(this);
		jMenuGame.add(menuItem);
		
		jMenuBar.add(jMenuGame);
//		+++++++++++++++++++++++++++++++
		jMenuGame = new JMenu("Scores");
		
		menuItem = new JMenuItem("Best Player");
		menuItem.addActionListener(this);
		jMenuGame.add(menuItem);
		
		jMenuBar.add(jMenuGame);
//		+++++++++++++++++++++++++++++++
		jMenuGame = new JMenu("Preferences");

		menuItem = new JMenuItem("LayOut");
		menuItem.addActionListener(this);
		jMenuGame.add(menuItem);
		
		jMenuBar.add(jMenuGame);
//		+++++++++++++++++++++++++++++++
		jMenuGame = new JMenu("Help");
		
		menuItem = new JMenuItem("Keys");
		menuItem.addActionListener(this);
		jMenuGame.add(menuItem);
		
		menuItem = new JMenuItem("About");
		menuItem.addActionListener(this);
		jMenuGame.add(menuItem);
		
		jMenuBar.add(jMenuGame);
//		+++++++++++++++++++++++++++++++	
		this.setJMenuBar(jMenuBar);
//		+++++++++++++++++++++++++++++++
		hsize = new JButton(new ICON());
		hsize.addActionListener(this);
		vsize = new JButton(new ICON());
		vsize.addActionListener(this);
		
		this.getContentPane().add(hsize);
		getContentPane().add(vsize);
		
		hsize.setVisible(true);
		hsize.setLocation(190,410);
		hsize.setSize(18,15);
		hsize.setFocusable(false);
		
		vsize.setVisible(true);
		vsize.setLocation(210,390);
		vsize.setSize(18,15);
		vsize.setFocusable(false);
		/*
		 * dodaje dan¹ klase do listy adresatów, którzy s¹ zwi¹zani
		 * z komunikacj¹ z klawiatury. 
		 */
		addKeyListener(this);  
	}
	
	
	public MainControl(TetrionView tv, Tetrion t, PanelGameStatus pgs)
	throws HeadlessException {
		this();  // konstruktor bezparametrowy  
		Dimension d;
		
		tetrionview = tv;
		tetrion = t;
		panelgamestatus = pgs;
	//	tetrion.fig = 0;
		
		panelgamestatus.suwak.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				Tetrion.Speed = panelgamestatus.suwak.getValue();
				panelgamestatus.suwak.setFocusable(false);
			}
		});
		
	//	this.setLayout(null);
		
		this.getContentPane().add(panelgamestatus);
		this.getContentPane().add(tetrionview); // do glownego kontenera
	//	d = new Dimension(tetrion.X*22, tetrion.Y*21);
	//	tetrionview.setSize(d);
		
		this.setFocusable(true);
		
		cancel = true;
	}
	
	
	/**
	 * @param arg0
	 */
	public MainControl(GraphicsConfiguration arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @throws java.awt.HeadlessException
	 */
	public MainControl(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MainControl(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
	}
	
	class ICON implements Icon{
		/* (non-Javadoc)
		 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
		 */
		public void paintIcon(Component arg0, Graphics g, int arg2, int arg3) {
			// TODO Auto-generated method stub
			g.setColor(Color.CYAN);
			g.fillRect(3,2,11,11);
		}
		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconHeight()
		 */
		public int getIconHeight() {
			// TODO Auto-generated method stub
			return 20;
		}

		public int getIconWidth() {
			// TODO Auto-generated method stub
			return 15;
		}
	}
	
	
	class TimeMachine extends TimerTask {
		public void run(){
			tetrion.Update("Down",which);
		}
	}

	public class Conection extends UnicastRemoteObject
	implements ViewTetrion{
		
		public Conection()throws RemoteException{
		}
		public void TakeFigure(short figura)throws RemoteException{
//			System.out.println(figura);
			tetrion.TakeFigure(figura);
			
		}
		public boolean CorectData(short whoFirst) throws RemoteException{
			tetrion.which = whoFirst;
			TakeFigure(tetrion.NextFigure[tetrion.yourfigur].c);
			return false;
		}
		public void between() throws RemoteException{
			between = true;
			System.out.println("nawiazano wzajemnie");
		}
		public void UpdateData(String move) throws RemoteException{
			between = false;
	//		System.out.println(move);
			Start();
		}
		
		public short getConection()throws RemoteException{
	//		System.out.println("Nawiaza³ polaczenie");
			tetrion.yourfigur = which;
			return which;
		}
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand() == "Pause"){
			if(tetrion.End) return;
			if(cancel){
				timer = new Timer();
				timer.schedule(new TimeMachine(),0,Tetrion.Speed);
				cancel = false;
			}
			else{
				timer.cancel();			
				cancel = true;
			}
			panelgamestatus.suwak.setEnabled(cancel);
			tetrionview.updateUI();
		}
		if (arg0.getActionCommand() == "New"){
			tetrion.NewGame();
		}
		if (arg0.getActionCommand() == "End"){
			if(!cancel)
				timer.cancel();
			cancel = true;
			tetrion.End = true;
			panelgamestatus.suwak.setEnabled(cancel);
		}
		if (arg0.getActionCommand() == "LayOut"){
			tetrionview.setSize(210,410);
		}
		if (arg0.getActionCommand() == "Add player"){

			new MultiPlayer(this);

		}
		if (arg0.getActionCommand() == "Best Player"){
		}
		if (arg0.getActionCommand() == "Keys"){
			
		}
		if (arg0.getActionCommand() == "About"){
			
		}
		if(arg0.getSource() == vsize){
			if(tetrion.setSizeMatrix(tetrion.X+1,tetrion.Y)){
				vsize.setLocation(vsize.getLocation().x+20,vsize.getLocation().y);
				hsize.setLocation(hsize.getLocation().x+20,hsize.getLocation().y);
				this.setSize(this.getSize().width+20, getSize().height);
				panelgamestatus.setLocation(panelgamestatus.getLocation().x+20,panelgamestatus.getLocation().y);
				this.validate();
				
			}
		}
		if(arg0.getSource() == hsize){
			if(tetrion.setSizeMatrix(tetrion.X,tetrion.Y+1)){
				vsize.setLocation(vsize.getLocation().x,vsize.getLocation().y+20);
				hsize.setLocation(hsize.getLocation().x,hsize.getLocation().y+20);
				this.setSize(this.getSize().width, getSize().height+20);
				this.validate();
			}
		}

	}
	public void keyPressed(KeyEvent arg0) {
		if(KeyEvent.VK_RIGHT == arg0.getKeyCode()){
			Start();
			tetrion.Update("Right",which);
		}
		if(KeyEvent.VK_LEFT == arg0.getKeyCode()){
			Start();
			tetrion.Update("Left",which);
		}
		if(KeyEvent.VK_DOWN == arg0.getKeyCode()){
			Start();
			tetrion.Update("Down",which);	
		}
		if(KeyEvent.VK_UP == arg0.getKeyCode()){
			Start();
			tetrion.Update("rotate",which);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void Registry(){
		try{
			this.con = false;
			LocateRegistry.createRegistry(Integer.parseInt(conect));	
		}catch(Exception e){
			System.err.println("Server LocateRegistry: " + e);
		}
		try{
			Conection c = new Conection();
			Naming.bind("//localhost:"+conect+"/UpdateData", c);
		}catch(Exception e){
			System.err.println("Server conection: " + e);
		}
		this.con = true;
	}
	public void getCon(){
		if(con){
			try {
				short i;
				h = (ViewTetrion)Naming.lookup(Player);
				i = h.getConection();
			//	System.out.println("->>>"+(i = h.getConection()));
				if(i==0){
					which = 1;
					tetrion.yourfigur = which;
					tetrion.NextFigure[tetrion.yourfigur] =
						tetrion.RandomFigure();
				}
				else{
					this.between=true;
					h.between();
			///		System.out.println("->>>");
				}
		    }
		    catch(Exception e){
		      System.err.println("client: " + e);
		    }
		}
	}
	
	public void keyTyped(KeyEvent arg0) {
	}
	
	public short take() {
		return tetrion.NextFigure[which].c;
	}
	public void Start(){
		if(cancel && !tetrion.End){
			if(between){
				try {
					tetrion.which = tetrion.yourfigur;

					h.CorectData(tetrion.yourfigur);

					h.TakeFigure(take());
		//			System.out.println("3");
					h.UpdateData("start");
		//			System.out.println("4");
				}
			    catch(Exception e){
			      System.err.println("nie tak mialo byc " + e.toString()
			      		+ "  " + e.getMessage());
			      return;
			    }
			}
			timer = new Timer();
			timer.schedule(new TimeMachine(),0,Tetrion.Speed);
			cancel = false;
			panelgamestatus.suwak.setEnabled(cancel);
		}
	}
}
