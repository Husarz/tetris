import java.awt.*;
import java.lang.String;
import javax.swing.*;
import java.awt.event.*;

/**
 * @author Andrzej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MultiPlayer extends JFrame {
	
	MainControl MC;
	JLabel jl;
	/**
	 * @throws java.awt.HeadlessException
	 */
	public MultiPlayer() throws HeadlessException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public MultiPlayer(MainControl arg0) {
		super();
		MC = arg0;
		final JTextField host, port, yourPort;
		final JButton conect, go;
		MC.Player = new String();
		MC.conect = new String();
		JPanel panelcenter = new JPanel();
		JPanel panelnorth = new JPanel();
		
		panelnorth.add(new JLabel("your: "));
		panelnorth.add(yourPort = new JTextField(5));
		panelnorth.add(go = new JButton("Go"));
		
		panelcenter.add(new JLabel(" host : "));
		panelcenter.add(host = new JTextField(10));
		
		panelcenter.add(new JLabel(" port : "));
		panelcenter.add(port = new JTextField(5));
		
		this.getContentPane().add(panelnorth,BorderLayout.NORTH);
		this.getContentPane().add(panelcenter,BorderLayout.CENTER);
		this.getContentPane().add(
				conect = new JButton("Connect"),BorderLayout.SOUTH);
		conect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				MC.Player = 
					"//" + host.getText() + ":" + port.getText()
					+ "/UpdateData";
				MC.port = port.getText();
				MC.host = host.getText();
				MC.getCon();
		//		System.out.println(MC.Player);
			}
		});
		go.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				MC.conect = yourPort.getText();
				MC.Registry();
	//			System.out.println(MC.conect);
			}
		});
		
		setSize(200,200);
		setVisible(true);
	}

	/**
	 * @param arg0
	 * @throws java.awt.HeadlessException
	 */
	public MultiPlayer(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MultiPlayer(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
}
