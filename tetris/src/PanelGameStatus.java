import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.Font;
import javax.swing.*;
/*
 * Created on 2004-11-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Andrzej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PanelGameStatus extends JPanel implements PropertyChangeListener {

	JLabel Score, Line, kl, koniec;
	LabelDraw klocek;
	JSlider suwak;
	Tetrion tetrion;
	/**
	 * 
	 */
	public PanelGameStatus(Tetrion t) {
		super();
		// TODO Auto-generated constructor stub
		tetrion = t;
		
		this.setLayout(null);
		
		add(Score = new JLabel(" punkty : "));
		Score.setSize(75,30);
		Score.setLocation(10,15);
		this.Score = new JLabel(" Iloœæ punktów ");
		add(Score);
		Font font = new Font("Serif",Font.BOLD|Font.PLAIN,22);
		Score.setFont(font);
		Score.setOpaque(true);
		Score.setBorder(BorderFactory.createLineBorder(Color.black));
		Score.setBackground(Color.blue);
		Score.setSize(120,30);
		Score.setLocation(70,15);
		
		
		add(Line = new JLabel(" linie : "));
		Line.setSize(75,30);
		Line.setLocation(10,70);
		this.Line = new JLabel(" Iloœæ linii ");
		add(Line);
		Line.setFont(font);
		Line.setOpaque(true);
		Line.setBorder(BorderFactory.createLineBorder(Color.black));
		Line.setBackground(Color.blue);
		Line.setSize(120,30);
		Line.setLocation(70,70);
		
		add(kl = new JLabel(" nastêpny klocek : "));
		kl.setSize(150,30);
		kl.setLocation(70,160);
		klocek = new LabelDraw();
		add(klocek);
		klocek.setSize(109,109);
		klocek.setLocation(70,190);
		
		add(kl = new JLabel(" Speed "));
		kl.setSize(75,440);
		kl.setLocation(90,160);
		suwak = new JSlider(50,1000,400);
		add(suwak);
		suwak.setBorder(BorderFactory.createLineBorder(Color.black));
		suwak.setSize(170,50);
		suwak.setLocation(35,340);
		
		this.setBorder(BorderFactory.createLineBorder(Color.black,3));
		this.setLocation(250,10);
		this.setSize(240,400);
		this.setBackground(Color.gray);
	}

	class LabelDraw extends JLabel{

		public LabelDraw() {
			super();
			// TODO Auto-generated constructor stub
			this.setOpaque(true);
			this.setBorder(BorderFactory.createLineBorder(Color.black,3));
			setBackground(Color.white);
		}
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			for(int i,j=0 ;j<5; j++){
				for(i=0; i<5; i++){
					if(j>0)
						g.setColor(TetrionView.Col[tetrion.getColFigur(j-1,i)]);
					else
						g.setColor(Color.lightGray);
					g.fillRect(i*20+5, j*20+5, 19, 19);
					if( j>0 && tetrion.getColFigur(j-1,i)!=7){
						g.setColor(Color.BLACK);
						g.drawLine(i*20+23,j*20+5,i*20+23,j*20+23);
						g.drawLine(i*20+5,j*20+23,i*20+23,j*20+23);
						g.drawLine(i*20+22,j*20+5,i*20+22,j*20+23);  // <---
						g.drawLine(i*20+5,j*20+22,i*20+23,j*20+22);  // <---
					}
				}
			}
			
		}
}
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		Line.setText(new Integer(tetrion.intLine).toString());
		Line.setVerticalTextPosition(SwingConstants.CENTER);
		Score.setText(new Integer(tetrion.Score).toString());
	//	if(arg0.getPropertyName().equals("End"))
	//		new Xml(tetrion.Score);

	}
}
