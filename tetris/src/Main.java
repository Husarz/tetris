
import javax.swing.*;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Main {

	public static void main(String[] args) {
		MainControl frame; // zmienna okna
		TetrionView tv;  // gui tetrionu
		Tetrion t; // engine
		PanelGameStatus pgs;
		
		t = new Tetrion();
		tv = new TetrionView(t);
		pgs = new PanelGameStatus(t);
		t.addPropertyChangeListener(tv); // zapisuje klase tv do listy klas ktore maja byc informowane o zmianach w engine
		t.addPropertyChangeListener(pgs);
		frame = new MainControl(tv,t,pgs);
		/*
	    try {
	         //UIManager.setLookAndFeel(
	            //UIManager.getCrossPlatformLookAndFeelClassName());
	         UIManager.setLookAndFeel(
	            "com.sun.javax.swing.plaf.metal");
	         SwingUtilities.updateComponentTreeUI( frame );
	      }
	      catch (Exception exc) {
	         System.out.println( exc.toString() );
	      }
		*/
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // parametry okna
		frame.setSize(510,480);
		frame.setVisible(true);
	}
}
