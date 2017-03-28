/*
 * ChatRMIView.java
 */
package edu.uniasselvi;

import edu.uniasselvi.bean.Message;
import edu.uniasselvi.bean.User;
import edu.uniasselvi.server.ChatRMIServer;
import java.net.UnknownHostException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import edu.uniasselvi.server.IChatRMIServer;
import edu.uniasselvi.util.Solicita;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import java.net.InetAddress;
import javax.swing.JOptionPane;

/**
 * The application's main frame.
 */
public class ChatRMIView extends FrameView {

    // Atributos internos para conexão
    private Registry registry;
    private IChatRMIServer stub;
    private String nome;
    private String ip = "127.0.0.1";
    private int codigo;
    private ArrayList<User> connectedUsersList;
    private java.util.Timer timerUsers;
    private java.util.Timer timerMessages;
    private String hostaddress;
    private Registry registryServer = null;
    private ServerLog serverLog;
    private User selectedUser;

    public ChatRMIView(SingleFrameApplication app) {
        super(app);

        initComponents();

        try {
            // Get hostaddress (ip) as String
            this.hostaddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            this.hostaddress = "IPDesconhecido";
        }

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ChatRMIApp.getApplication().getMainFrame();
            aboutBox = new ChatRMIAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ChatRMIApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtConversa = new javax.swing.JTextArea();
        txtMensagem = new javax.swing.JTextField();
        btSend = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        connectedUsers = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        menuStartServer = new javax.swing.JMenuItem();
        menuServerLog = new javax.swing.JMenuItem();
        menuStopServer = new javax.swing.JMenuItem();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        menuConnect = new javax.swing.JMenuItem();
        menuDisconnect = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(edu.uniasselvi.ChatRMIApp.class).getContext().getResourceMap(ChatRMIView.class);
        txtConversa.setBackground(resourceMap.getColor("txtConversa.background")); // NOI18N
        txtConversa.setColumns(20);
        txtConversa.setEditable(false);
        txtConversa.setRows(5);
        txtConversa.setText(resourceMap.getString("txtConversa.text")); // NOI18N
        txtConversa.setName("txtConversa"); // NOI18N
        jScrollPane1.setViewportView(txtConversa);

        txtMensagem.setText(resourceMap.getString("txtMensagem.text")); // NOI18N
        txtMensagem.setEnabled(false);
        txtMensagem.setName("txtMensagem"); // NOI18N
        txtMensagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMensagemKeyPressed(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(edu.uniasselvi.ChatRMIApp.class).getContext().getActionMap(ChatRMIView.class, this);
        btSend.setAction(actionMap.get("sendMessage")); // NOI18N
        btSend.setText(resourceMap.getString("btSend.text")); // NOI18N
        btSend.setName("btSend"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        connectedUsers.setBackground(resourceMap.getColor("connectedUsers.background")); // NOI18N
        connectedUsers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        connectedUsers.setEnabled(false);
        connectedUsers.setName("connectedUsers"); // NOI18N
        connectedUsers.addListSelectionListener(new  ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				setSelectedUser(connectedUsersList.get(e.getFirstIndex() -1));
			}
		});
        jScrollPane2.setViewportView(connectedUsers);

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        
        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        
        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                    	.addComponent(jLabel2)
                    	.addComponent(jLabel3)
                        .addComponent(txtMensagem, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(btSend)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                	.addComponent(jLabel2)
                	.addComponent(jLabel3)
                    .addComponent(btSend)
                    .addComponent(txtMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        jMenu2.add(exitMenuItem);

        menuBar.add(jMenu2);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        menuStartServer.setAction(actionMap.get("startServer")); // NOI18N
        menuStartServer.setText(resourceMap.getString("menuStartServer.text")); // NOI18N
        menuStartServer.setName("menuStartServer"); // NOI18N
        jMenu1.add(menuStartServer);

        menuServerLog.setAction(actionMap.get("openServerLog")); // NOI18N
        menuServerLog.setText(resourceMap.getString("menuServerLog.text")); // NOI18N
        menuServerLog.setEnabled(false);
        menuServerLog.setName("menuServerLog"); // NOI18N
        jMenu1.add(menuServerLog);

        menuStopServer.setAction(actionMap.get("stopServer")); // NOI18N
        menuStopServer.setText(resourceMap.getString("menuStopServer.text")); // NOI18N
        menuStopServer.setEnabled(false);
        menuStopServer.setName("menuStopServer"); // NOI18N
        jMenu1.add(menuStopServer);

        menuBar.add(jMenu1);

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        menuConnect.setAction(actionMap.get("connectServer")); // NOI18N
        menuConnect.setText(resourceMap.getString("menuConnect.text")); // NOI18N
        menuConnect.setName("menuConnect"); // NOI18N
        fileMenu.add(menuConnect);

        menuDisconnect.setAction(actionMap.get("disconnect")); // NOI18N
        menuDisconnect.setText(resourceMap.getString("menuDisconnect.text")); // NOI18N
        menuDisconnect.setEnabled(false);
        menuDisconnect.setName("menuDisconnect"); // NOI18N
        fileMenu.add(menuDisconnect);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 475, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    
    private void setSelectedUser(User user) {
    	this.selectedUser = user;
    }

    private void txtMensagemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMensagemKeyPressed
        if (evt.getKeyCode() == 10) {
            sendMessage();
        }
    }//GEN-LAST:event_txtMensagemKeyPressed

    // Habilita ou desabilita os campos da tela
    private void enableFilds(boolean enable) {
        this.btSend.setEnabled(enable);
        this.txtMensagem.setEnabled(enable);
        this.connectedUsers.setEnabled(enable);
    }

    // Utilizada para atualizar a lista de Mensagens
    class refreshMessagesTask extends java.util.TimerTask {

        public void run() {
            try {
                //Terminate the timer thread
                timerMessages.cancel();
            } catch (Exception e){
                appendMessage("Erro não fatal: " + e.getMessage());
            }finally{
                // Chama de novo
                refreshMessagesList();
            }
        }
    }

    // Atualiza a lista de Usuários ativos
    private void refreshMessagesList() {

        try {
            try {

                // Busca a lista de novas mensagens
                ArrayList<Message> messages = stub.getNewMessages(this.codigo);

                // Atualiza a lista na tela
                for (int i = 0; i < messages.size(); i++) {
                    Message m = messages.get(i);
                    appendMessage(m.getFrom().getName() + "(" + m.getFrom().getIp() + "): " + m.getMessage());
                }

            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }

            timerMessages = new java.util.Timer();
            timerMessages.schedule(new refreshMessagesTask(), 3 * 1000);

        } catch (Exception er){
            appendMessage("Erro ao buscar Mensagens: " + er.getMessage());
            disconnect();
        }
    }

    // Utilizada para atualizar a lista de usuários
    class refreshUsersTask extends java.util.TimerTask {

        public void run() {
            try {
                //Terminate the timer thread
                timerUsers.cancel();
            } catch (Exception e){
                appendMessage("Erro não fatal: " + e.getMessage());
            } finally {
                // Chama de novo
                refreshUserList();
            }
        }
    }

    // Atualiza a lista de Usuários ativos
    private void refreshUserList() {

        try {
            try {

                // Busca a lista de usuários conectados
                connectedUsersList = stub.getConnectedUsers();

                // Limpa a lista dos usuarios
                connectedUsers.removeAll();

                DefaultListModel model = new DefaultListModel();
                
                model.add(0, "Todos");

                // Atualiza a lista na tela
                for (int i = 0; i < connectedUsersList.size(); i++) {
                    User u = connectedUsersList.get(i);
                    model.add(i+1, u.getName());
                }
                connectedUsers.setModel(model);

            } catch (Exception ex) {
                throw new Exception(ex.getMessage());
            }

            timerUsers = new java.util.Timer();
            timerUsers.schedule(new refreshUsersTask(), 5 * 1000);

        } catch(Exception er){
            appendMessage("Erro ao buscar lista de usuários: " + er.getMessage());
            // Desconecta o cara
            disconnect();
        }
    }

    @Action
    public void connectServer() {
        try {

            // Pede o Ip do Servidor e nome do usuário
            this.ip = Solicita.getString("Informe o IP do Servidor", true, this.ip);
            this.nome = Solicita.getString("Informe seu Nome", true, this.nome);

            // Faz a conexão
            this.registry = LocateRegistry.getRegistry(ip, 1000);
            this.stub = (IChatRMIServer) registry.lookup("ServidorRMI");

            // Conecta ao servidor
            this.codigo = this.stub.connect(this.nome, this.hostaddress);

            // Habilita os campos
            this.enableFilds(true);

            // Limpa Caixa de Texto
            this.appendMessage("Bem vindo " + this.nome + ". Seu ID é: " + this.codigo);

            this.txtMensagem.requestFocus();

            // Ajusta menu
            this.menuConnect.setEnabled(false);
            this.menuDisconnect.setEnabled(true);

            // Atualiza a lista de usuarios ativos
            this.refreshUserList();
            // Atualiza as novas mensagens
            this.refreshMessagesList();

        } catch (AccessException e) {
            this.appendMessage("Erro no Acesso: " + e.getMessage());
            disconnect();
        } catch (RemoteException e) {
            this.appendMessage("Erro Remoto: " + e.getMessage());
            disconnect();
        } catch (NotBoundException e) {
            this.appendMessage("Servidor Não encontrado: " + e.getMessage());
            disconnect();
        } catch (Exception e) {
            if (!e.getMessage().equals("CANCEL")) {
                this.appendMessage(e.getMessage());
            }
        }
    }

    @Action
    public void sendMessage() {
        try {

            // Monta a mensagem
            Message m = new Message();

            User eu = new User();
            eu.setId(this.codigo);
            eu.setIp(this.hostaddress);
            eu.setName(this.nome);

            m.setFrom(eu);
            m.setMessage(this.txtMensagem.getText());

            // Envia a mensagem
            if(selectedUser == null){
            	stub.sendMessage(m);            	
            } else {
            	stub.sendMessage(m, selectedUser);
            }

            this.appendMessage(this.nome + " (" + this.hostaddress + "): " + m.getMessage());

            // Limpa a caixa de texto
            this.txtMensagem.setText("");

            // seta o foco
            this.txtMensagem.requestFocus();

        } catch (AccessException e) {
            this.appendMessage("Erro no acesso: " + e.getMessage());
            disconnect();
        } catch (RemoteException e) {
            this.appendMessage("Erro Remoto: " + e.getMessage());
            disconnect();
        }
    }

    // Adiciona uma mensagem na caixa de texto
    private void appendMessage(String text) {
        this.txtConversa.append("\n" + text);
        // Faz a navegação para o fim do texto
        //this.txtConversa.setCaretPosition( this.txtConversa.getCaretPosition() );
    }

    @Action
    public void startServer() {

        try {

            // Abre a janela de Log do Servidor
            if (serverLog == null) {
                JFrame mainFrame = ChatRMIApp.getApplication().getMainFrame();
                serverLog = new ServerLog();
                serverLog.setLocationRelativeTo(mainFrame);
            }

            ChatRMIServer chatServer = new ChatRMIServer(serverLog);

            //Liga o stub do objeto remoto no registro, e inicia o RMIRegistry na porta 1000
            if (registryServer == null) {
                registryServer = LocateRegistry.createRegistry(1000);
            }
            registryServer.bind("ServidorRMI", chatServer);

            // Adiciona um texto no Log
            serverLog.appendLog("Servidor está no ar... Seu IP é: " + this.hostaddress);

            // Abre a janela de logs do Servidor
            ChatRMIApp.getApplication().show(serverLog);

            // Ajusta menu
            this.menuStartServer.setEnabled(false);
            this.menuStopServer.setEnabled(true);
            this.menuServerLog.setEnabled(true);

        } catch (RemoteException e) {
            this.appendMessage("Erro ao instanciar Servidor. Erro:" + e.getMessage());
        } catch (AlreadyBoundException e) {
            this.appendMessage("Servidor já instanciado. Erro: " + e.getMessage());
        }

    }

    @Action
    public void disconnect() {

        try {
            try {
                // Disconecta o Cliente
                stub.disconnect(this.codigo);
            } catch (AccessException e) {
                this.appendMessage("Erro no acesso. Erro: " + e.getMessage());
            } catch (RemoteException e) {
                this.appendMessage("Erro remoto. Erro: " + e.getMessage());
            } catch (Exception e){
                
            } finally {

                // Desabilita os campos da tela
                this.enableFilds(false);

                try {
                    // Para de Atualizar os usuários
                    timerUsers.cancel();
                } catch (Exception e){

                }

                try {
                    timerMessages.cancel();
                } catch (Exception e){

                }

                // Limpa Caixa de Texto
                this.appendMessage(this.nome + ", você está fora.");

                // Ajusta menu
                this.menuConnect.setEnabled(true);
                this.menuDisconnect.setEnabled(false);
            }
        } catch (Exception e) {
            this.appendMessage("Erro: " + e.getMessage());
        }
    }

    @Action
    public void stopServer() {
        /*
        try {

        //Liga o stub do objeto remoto no registro, e inicia o RMIRegistry na porta 1000
        registryServer.unbind("ServidorRMI");
        this.appendMessage("Server on " + this.hostaddress +" was Stoped.");

        // Ajusta menu
        this.menuStartServer.setEnabled(true);
        this.menuStopServer.setEnabled(false);

        } catch (RemoteException e) {
        this.appendMessage("Instantiate Server Error: " + e.getMessage());
        } catch (NotBoundException ex) {
        Logger.getLogger(ChatRMIView.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        JOptionPane.showMessageDialog(menuConnect, "Para parar o servidor, feche o programa. Não tive tempo para fazer isso. ;)");
    }

    @Action
    public void openServerLog() {

        if (serverLog == null) {
            JFrame mainFrame = ChatRMIApp.getApplication().getMainFrame();
            serverLog = new ServerLog();
            serverLog.setLocationRelativeTo(mainFrame);
        }
        ChatRMIApp.getApplication().show(serverLog);

    }

	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSend;
    private javax.swing.JList connectedUsers;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuConnect;
    private javax.swing.JMenuItem menuDisconnect;
    private javax.swing.JMenuItem menuServerLog;
    private javax.swing.JMenuItem menuStartServer;
    private javax.swing.JMenuItem menuStopServer;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextArea txtConversa;
    private javax.swing.JTextField txtMensagem;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
