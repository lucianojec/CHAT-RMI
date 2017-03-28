package edu.uniasselvi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import edu.uniasselvi.bean.Message;
import edu.uniasselvi.bean.User;

public interface IChatRMIServer extends Remote {
    public int connect(String name, String ipOrigem) throws RemoteException;
    public void disconnect(int idUser) throws RemoteException;
    public ArrayList<User> getConnectedUsers() throws RemoteException;
    public void sendMessage(Message message) throws RemoteException;
    public void sendMessage(Message message, int[] toIDs) throws RemoteException;
    public void sendMessage(Message message, User selectedUser) throws RemoteException;
    public ArrayList<Message> getNewMessages(int idUser) throws RemoteException;
}
