package edu.uniasselvi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import edu.uniasselvi.ServerLog;
import edu.uniasselvi.bean.Message;
import edu.uniasselvi.bean.User;

public class ChatRMIServer extends UnicastRemoteObject implements IChatRMIServer {

    private static final long serialVersionUID = 1L;
    private ArrayList<User> connectedUsers;
    private ArrayList<Message> messages;
    private ServerLog serverLog;

    public ChatRMIServer(ServerLog serverLog) throws RemoteException {
        super();

        // Cria a lista de Usuários Conectados
        connectedUsers = new ArrayList<User>();
        messages = new ArrayList<Message>();
        this.serverLog = serverLog;

    }

    // Adiciona um novo usuário e retorna o id gerado
    private int addUser(User user) {
        User u = user;
        u.setId(this.connectedUsers.size() + 1);
        this.connectedUsers.add(u);
        
        Message m = new Message();
        m.setMessage("\nUsuário " + u.getName() + " (" + u.getIp() + ") conectou. ID: " + u.getId() + "!");
        
        serverLog.appendLog(m.getMessage());

        try {
            m.setFrom(u);
            sendMessage(m);
        } catch (RemoteException ex) {
            serverLog.appendLog("\nErro ao notificar usuários da entrada de " + u.getName() + "! Erro: " + ex.getMessage());
        }

        return (u.getId());
    }

    @Override
    public int connect(String name, String ip) throws RemoteException {
        User user = new User(name, ip);
        return this.addUser(user);
    }

    @Override
    public ArrayList<User> getConnectedUsers() throws RemoteException {

    	ArrayList<User> users = new ArrayList<User>();

        // Percorre todos os usuários e retorna apenas os conectados
        for (int i = 0; i < this.connectedUsers.size(); i++) {
            if (this.connectedUsers.get(i).isConnected()) {
                users.add(this.connectedUsers.get(i));
            }
        }

        return users;
    }

    @Override
    public ArrayList<Message> getNewMessages(int idUser) throws RemoteException {

        ArrayList<Message> mess = new ArrayList<Message>();

        // Carrega as mensagens não lidas
        for (int i = 0; i < this.messages.size(); i++) {

            Message m = this.messages.get(i);

            if (!m.isReaded() && m.getTo().getId() == idUser) {
                mess.add(m);
                // Diz que a mensagem foi lida
                m.setReaded(true);
            }
        }

        return mess;
    }

    // Busca o usuário do ID
    private User getUserByID(int idUser) {

        for (int i = 0; i < this.connectedUsers.size(); i++) {
            User user = this.connectedUsers.get(i);
            if (user.getId() == idUser) {
                return user;
            }
        }
        return new User();
    }

    @Override
    public void sendMessage(Message message, int[] toIDs)
            throws RemoteException {

        // Mensagem enviada para várias pessoas
        for (int i = 0; i < toIDs.length; i++) {
            Message mess = message;
            mess.setTo(this.getUserByID(toIDs[i]));
            sendMessage(message);
        }
    }

    @Override
    public void sendMessage(Message message)
            throws RemoteException {
    	
        // Vamos mandar sempre para todos
        for (int i = 0; i < this.connectedUsers.size(); i++) {

            // Eu mesmo não recebo de volta
            if (message.getFrom().getId() != this.connectedUsers.get(i).getId()) {
                Message m = new Message();
                m.setMessage(message.getMessage());
                m.setFrom(message.getFrom());
                m.setTo(this.connectedUsers.get(i));

                // Adiciona a mensagem
                this.messages.add(m);
            }
        }

        serverLog.appendLog("\nUsuário " + message.getFrom().getName() + " envia mensagem: " + message.getMessage());

    }

    @Override
    public void disconnect(int idUser) throws RemoteException {

        User user = this.getUserByID(idUser);
        user.disconnect();

        Message m = new Message();
        m.setMessage("\nUsuário " + user.getId() + " - " + user.getName() + " (" + user.getIp() + ") desconectou!");

        serverLog.appendLog(m.getMessage());

        try {
            m.setFrom(user);
            sendMessage(m);
        } catch (RemoteException ex) {
            serverLog.appendLog("\nErro ao notificar usuários da saída de " + user.getName() + "! Erro: " + ex.getMessage());
        }
    }

	@Override
	public void sendMessage(Message message, User selectedUser) throws RemoteException {
		message.setTo(selectedUser);
		this.messages.add(message);
		serverLog.appendLog("\nUsuário " + message.getFrom().getName() + " envia mensagem: " + message.getMessage());
	}
}