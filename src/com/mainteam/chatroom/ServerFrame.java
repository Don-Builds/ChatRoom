package com.mainteam.chatroom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

/**
 * ServerFrame: 聊天室服务端 用来与客户端创建连接，同时并发聊天内容至每一个客户端
 * 
 * @author GraY
 *
 */
public class ServerFrame extends JFrame {
	static {
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
		}
	}
	private static final long serialVersionUID = 1L;
	private static Map<Socket, String> map = new HashMap<Socket, String>();
	private Socket[] allOut = new Socket[0];
	private final static int WIDTH = 870;
	private final static int HEIGHT = 700;
	public static int PORT = 8088;
	private boolean flag = true;
	private String line = null;
	ServerSocket server;
	Socket socket;
	JPanel contentPane;
	JButton btnStart = new JButton();
	JButton btnSend = new JButton();
	JButton btnClose = new JButton();
	JButton btnKick = new JButton();
	JButton btnExport = new JButton();
	JButton btnClear = new JButton();
	JLabel function = new JLabel();
	JLabel lblState = new JLabel();
	JLabel lblUser = new JLabel();
	Color c = new Color(74,112,139);
	JFileChooser fc = new JFileChooser();
	JTextPane taChatMessage = new JTextPane();
	Document docs = taChatMessage.getDocument();
	SimpleAttributeSet attrset = new SimpleAttributeSet();
	JScrollPane ChatMessage = new JScrollPane();
	JTextPane txtMsg = new JTextPane();
	JScrollPane txtMsgList = new JScrollPane();
	ImageIcon red = new ImageIcon("img/red.gif");
	ImageIcon green = new ImageIcon("img/green.gif");
	DefaultListModel<String> dlmUsers = new DefaultListModel<String>();;
	JList<String> lstUsers = new JList<String>(dlmUsers);
	JScrollPane srpList = new JScrollPane(lstUsers);;

	/**
	 * 构造方法，执行调用Init()
	 */
	public ServerFrame() {
		Init();

	}

	/**
	 * 初始化组件
	 */
	public void Init() {
		setSize(WIDTH, HEIGHT);
		setTitle("聊天室      -M.T编程组");
		setResizable(false);
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(null);
		Border border = BorderFactory.createEtchedBorder(Color.white, new Color(170, 170, 170));
		lblState.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		lblState.setBorder(new TitledBorder(border, "聊天室状态", TitledBorder.ABOVE_TOP, TitledBorder.CENTER,
				new Font("Dialog", Font.PLAIN, 12)));
		lblState.setHorizontalAlignment(SwingConstants.CENTER);
		lblState.setForeground(c);
		lblState.setFont(new Font("微软雅黑",Font.PLAIN, 12));
		lblState.setText("服务器未开启");
		lblState.setBounds(new Rectangle(670, 7, 185, 46));
		contentPane.add(lblState);
		function.setBorder(new TitledBorder(border, "功能模块", TitledBorder.ABOVE_TOP, TitledBorder.CENTER,
				new Font("Dialog", Font.PLAIN, 20)));
		function.setHorizontalAlignment(SwingConstants.CENTER);
		function.setBounds(new Rectangle(580, 60, 275, 275));
		contentPane.add(function);

		srpList.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		lstUsers.setFont(new java.awt.Font("Dialog", Font.PLAIN,20));
		srpList.setBounds(580, 365, 275, 285);
		lstUsers.setVisibleRowCount(15);
		lstUsers.setSize(20,20);
		contentPane.add(srpList);
		srpList.setViewportView(lstUsers);

		ChatMessage.setBounds(15, 7, 550, 450);
		contentPane.add(ChatMessage);
		taChatMessage.setEditable(false);
		//taChatMessage.setLineWrap(true);
		//taChatMessage.setFont(new java.awt.Font("Dialog", Font.PLAIN, 20));
		taChatMessage.setBounds(15, 7, 550, 450);
		taChatMessage.setBorder(null);
		taChatMessage.setCaretPosition(taChatMessage.getText().length());
		ChatMessage.setViewportView(taChatMessage);

		txtMsgList.setBounds(17, 465, 546, 135);
		contentPane.add(txtMsgList);
		//txtMsg.setLineWrap(true);
		txtMsg.setFont(new java.awt.Font("Dialog", Font.PLAIN, 20));
		txtMsg.setBounds(17, 465, 546, 135);
		txtMsg.setBorder(null);
		txtMsg.addKeyListener(new btnKey(this));
		txtMsgList.setViewportView(txtMsg);

		btnStart.setBounds(new Rectangle(580, 15, 80, 35));
		btnStart.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnStart.setIcon(red);
		btnStart.setText("启动");
		btnStart.addActionListener(new ServerFrame_btnStart_actionAdapter(this));
		contentPane.add(btnStart);

		btnSend.setBounds(new Rectangle(482, 611, 80, 35));
		btnSend.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnSend.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
		btnSend.setText("<html><span color=white><strong><font face=\"微软雅黑\">发送</font></strong></span><html>");
		btnSend.addActionListener(new btnSend_actionAdapter(this));
		contentPane.add(btnSend);

		btnClose.setBounds(new Rectangle(380, 611, 80, 35));
		btnClose.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnClose.setText("<html><span color=white><strong><font face=\"微软雅黑\">关闭</font></strong></span><html>");
		btnClose.addActionListener(new ServerFrame_btnClose_actionAdapter(this));
		btnClose.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		contentPane.add(btnClose);

		btnKick.setBounds(new Rectangle(600, 200, 80, 35));
		btnKick.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnKick.setText("踢人");
		btnKick.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		btnKick.addActionListener(new btnKick_actionAdapter(this));
		contentPane.add(btnKick);

		btnClear.setBounds(new Rectangle(750, 250, 80, 35));
		btnClear.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnClear.setText("清屏");
		btnClear.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		btnClear.addActionListener(new ServerFrame_btnClear_actionAdapter(this));
		contentPane.add(btnClear);

		btnExport.setBounds(new Rectangle(600, 250, 80, 35));
		btnExport.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnExport.setText("导出");
		btnExport.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.normal));
		btnExport.addActionListener(new ServerFrame_btnExport_actionAdapter(this));
		contentPane.add(btnExport);
		lblUser.setText("USER");
		lblUser.setBackground(Color.GRAY);
		lblUser.setFont(new Font("Algerian", Font.PLAIN, 20));
		
		lblUser.setIcon(new ImageIcon("img/user3.png"));
		lblUser.setBounds(590, 330, 106, 35);
		setLocationRelativeTo(null);
		getContentPane().add(lblUser);

		setVisible(true);

	}

	/**
	 * 服务端"启动"按钮
	 * 
	 * @param e
	 */
	public void btnStart(ActionEvent e) {
		String text = btnStart.getText();
		if (text.equals("启动") || text.equals("开启")) {
			startServer();
		} else {
			stopServer();
		}
	}

	/**
	 * 服务端启动方法
	 */
	public void startServer() {
		try {
			flag = true;
			server = new ServerSocket(PORT);
			lblState.setForeground(c);
			lblState.setFont(new Font("微软雅黑",Font.PLAIN, 12));
			lblState.setText("聊天室已开启 " + "端口:" + PORT);
			btnStart.setText("关闭");
			btnStart.setIcon(green);
			new ListenerThread().start();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "服务器启动失败！");
		}
	}

	/**
	 * 客户端多线程监听类
	 * 
	 * @author GraY
	 *
	 */
	class ListenerThread extends Thread {
		public void run() {
			while (flag) {
				try {
					System.out.println("等待客户端连接");
					socket = server.accept();
					allOut = Arrays.copyOf(allOut, allOut.length + 1);
					allOut[allOut.length - 1] = socket;
					System.out.println("客户端已连接");
					new ClientHandler(socket).start();
				} catch (IOException ex) {
					ex.printStackTrace();
					flag = false;
				}
			}
		}
	}

	/**
	 * 服务端关闭方法
	 */
	public void stopServer() {
		try {
			flag = false;
			lblState.setForeground(c);
			lblState.setFont(new Font("微软雅黑",Font.PLAIN, 12));
			lblState.setText("服务器入口已关闭");
			btnStart.setText("开启");
			btnStart.setIcon(red);
			server.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "关闭服务器产生错误！");
		}
	}

	/**
	 * 客户端多线程处理类
	 * 
	 * @author GraY
	 *
	 */
	class ClientHandler extends Thread {
		private Socket socket;

		ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				while ((line = dis.readUTF()) != null) {
					if(line.startsWith("新用户")) {
						String[] data = line.split(":");
						if(map.containsValue("👤 "+data[1])) {
							data[1] = data[1]+" "+socket.getPort();
						}
						map.put(socket,"👤 "+data[1]);						
						dlmUsers.clear();
						Collection<String> values = map.values();
						for(String value : values) {
							dlmUsers.addElement(value);
							System.out.println(value);
							for (int i = 0; i < allOut.length; i++) {
								DataOutputStream dos = new DataOutputStream(allOut[i].getOutputStream());
								dos.writeUTF("UserNick:" + value);
								dos.flush();
							}
						}
					}else {
						//taChatMessage.append(line + "\n");
						String[] data = line.split("\\n");
						StyleConstants.setFontSize(attrset,30);
						StyleConstants.setFontFamily(attrset, "黑体");
						StyleConstants.setForeground(attrset, c);
						docs.insertString(docs.getLength(), data[0]+"\t\t", attrset);
						StyleConstants.setFontSize(attrset,15);
						StyleConstants.setForeground(attrset, Color.GRAY);
						docs.insertString(docs.getLength(), data[1]+"\n", attrset);
						StyleConstants.setFontSize(attrset,20);
						StyleConstants.setForeground(attrset, Color.BLACK);
						for(int i=2;i<data.length;i++) {
							docs.insertString(docs.getLength(), data[i]+"\n", attrset);
						}
						docs.insertString(docs.getLength(), "\n", attrset);
						taChatMessage.setCaretPosition(taChatMessage.getStyledDocument().getLength());
						System.out.println("line:" + line);
						for (int i = 0; i < allOut.length; i++) {
							DataOutputStream dos = new DataOutputStream(allOut[i].getOutputStream());
							dos.writeUTF(line);
							dos.flush();
					}
					
					}
				}
			} catch (Exception e) {//处理客户端下线异常
				try {
					System.out.println(socket.getPort()+"下线了");
					String nick = map.get(socket);
					System.out.println("nick"+nick);
					map.remove(socket);
					System.out.println(map);
					for(int i=0;i<allOut.length;i++) {
						if(allOut[i]==socket) {
							allOut[i] = allOut[allOut.length-1];
							allOut[allOut.length-1] = socket;
						}
					}
					allOut = Arrays.copyOf(allOut, allOut.length-1);
					dlmUsers.clear();
					Collection<String> values = map.values();
					for(String value : values) {
						dlmUsers.addElement(value);
						System.out.println(value);
						for (int i = 0; i < allOut.length; i++) {
							DataOutputStream dos = new DataOutputStream(allOut[i].getOutputStream());
							dos.writeUTF("OutUser:" + nick);
							dos.flush();
						}
					}
				}catch (Exception e1) {
					e1.printStackTrace();
				}

			}

		}
	}

	/**
	 * "清屏"按钮的清屏功能
	 * 
	 * @param e
	 */
	public void btnClear(ActionEvent e) {
		taChatMessage.setText("");
	}

	/**
	 * "关闭"按钮的关闭功能，释放Socket资源并关闭窗口
	 * 
	 * @param e
	 */
	public void btnClose(ActionEvent e) {
		try {
			System.exit(0);
			server.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * "发送"按钮的发送功能
	 * 
	 * @param e
	 */
	
	public void sendMesg() {
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			String time = sdf.format(date);
			String text = "管理员\n" +time+ "\n" + txtMsg.getText() + "\n";
			StyleConstants.setFontSize(attrset,30);
			StyleConstants.setFontFamily(attrset, "黑体");
			Color c = new Color(74,112,139);
			StyleConstants.setForeground(attrset, c);
			docs.insertString(docs.getLength(), "管理员\t\t", attrset);
			StyleConstants.setFontSize(attrset,15);
			StyleConstants.setForeground(attrset, Color.GRAY);
			docs.insertString(docs.getLength(), time+ "\n", attrset);
			StyleConstants.setFontSize(attrset,20);
			StyleConstants.setForeground(attrset, Color.BLACK);
			docs.insertString(docs.getLength(), txtMsg.getText() + "\n\n", attrset);
			txtMsg.setText("");
			taChatMessage.setCaretPosition(taChatMessage.getStyledDocument().getLength());
			for (int i = 0; i < allOut.length; i++) {
				DataOutputStream dos = new DataOutputStream(allOut[i].getOutputStream());
				dos.writeUTF(text);
				System.out.println("写出完毕");
				dos.flush();
			}
		} catch (Exception e1) {
			System.out.println("无客户端");
		}
	}
	
	
	public void btnSend(ActionEvent e) {
		sendMesg();
		

	}

	public void btnKick(ActionEvent e) {
		try {
			Set<Entry<Socket, String>> entrySet = map.entrySet();
			String nick = lstUsers.getSelectedValue();
			System.out.println(nick);
			for(Entry<Socket, String> entry : entrySet){
				if(entry.getValue().equals(nick)) {
					System.out.println(entry.getKey()+"关闭了");
					entry.getKey().close();
					break;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * "导出"按钮的导出聊天记录的功能
	 * 
	 * @param e
	 */
	@SuppressWarnings("static-access")
	void btnExport(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileFilter(new MyFileFilter());
		int choice = jfc.showSaveDialog(this);
		if (choice == jfc.APPROVE_OPTION) {
			File destnation = jfc.getSelectedFile();
			try {
				FileOutputStream fos = new FileOutputStream(destnation.getAbsolutePath() + ".log", true);
				fos.write(taChatMessage.getText().getBytes());
				fos.close();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "保存文件发生错误！");
			}
		}
	}

	// 文件过滤器
	class MyFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File f) {
			if (f.isDirectory() || f.getName().endsWith(".log")) {
				return true;
			} else {
				return false;
			}
		}

		public String getDescription() {
			return "日志文件(*.log)";
		}
	}
	
	public void keyPress(KeyEvent e) {
		if(e.isControlDown() && e.getKeyCode()==KeyEvent.VK_ENTER) {
			sendMesg();
			System.out.println(1111);
		}
	}
}

class ServerFrame_btnClear_actionAdapter implements ActionListener {
	private ServerFrame adaptee;

	ServerFrame_btnClear_actionAdapter(ServerFrame adaptee) {
		this.adaptee = adaptee;

	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnClear(e);
	}

}

class ServerFrame_btnClose_actionAdapter implements ActionListener {
	private ServerFrame adaptee;

	ServerFrame_btnClose_actionAdapter(ServerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnClose(e);
	}

}

class ServerFrame_btnExport_actionAdapter implements ActionListener {
	private ServerFrame adaptee;

	ServerFrame_btnExport_actionAdapter(ServerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnExport(e);
	}
}

class ServerFrame_btnStart_actionAdapter implements ActionListener {
	private ServerFrame adaptee;

	ServerFrame_btnStart_actionAdapter(ServerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnStart(e);
	}
}

class btnSend_actionAdapter implements ActionListener {
	private ServerFrame adaptee;

	public btnSend_actionAdapter(ServerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnSend(e);
	}
}

class btnKick_actionAdapter implements ActionListener {
	private ServerFrame adaptee;

	public btnKick_actionAdapter(ServerFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnKick(e);
	}
}

class btnKey implements KeyListener{
	private ServerFrame adaptee;
	
	public btnKey(ServerFrame adaptee) {
		this.adaptee = adaptee;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		adaptee.keyPress(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
}
