
/**
 * @author Andrzej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.rmi.*;

public interface ViewTetrion extends Remote {
	
	void UpdateData(String move) throws RemoteException;
	public void between() throws RemoteException;
	public boolean CorectData(short whoFirst) throws RemoteException;
	public void TakeFigure(short figura)throws RemoteException;
	public short getConection()throws RemoteException;
}
