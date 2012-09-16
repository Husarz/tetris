
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
 
public class TetrionView extends JComponent implements PropertyChangeListener {

	/* (non-Javadoc)
	 * Tablica colorów która charakteryzujê komórkê.
	 */
	public static Color [] Col = {Color.red,Color.green,Color.orange,
			Color.pink,Color.blue,Color.yellow,
			Color.cyan,Color.lightGray};
	/* (non-Javadoc)
	 * Obiekt na kórym rysowana bêdzie plansza.
	 */

	Tetrion tetrion;
	/**
	 * 
	 */

	public TetrionView(Tetrion t) {
		super();
		tetrion = t;
		setBorder(BorderFactory.createLineBorder(Color.black,3));
	}
	

	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void paintComponent(Graphics g) {
		for(int j,i=0;i<tetrion.Y;i++)
			for(j=0;j<tetrion.X;j++){
				DrawElement(g,j,i);
			}
	}
	protected void DrawElement(Graphics g, int x, int y){
		g.setColor(Col[tetrion.getNum(y,x)]);
		g.fillRect(x*20+10, y*20+10, 19, 19);
		if(tetrion.getNum(y,x)!=7){
			g.setColor(Color.BLACK);		
			g.drawLine(x*20+28,y*20+10,x*20+28,y*20+28);
			g.drawLine(x*20+10,y*20+28,x*20+28,y*20+28);
			g.drawLine(x*20+27,y*20+10,x*20+27,y*20+28);  // <---
			g.drawLine(x*20+10,y*20+27,x*20+28,y*20+27);  // <---
		}
	}
	public void propertyChange(PropertyChangeEvent e) {
		this.repaint();
	}

}
