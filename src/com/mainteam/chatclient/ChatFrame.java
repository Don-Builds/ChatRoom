
package com.mainteam.chatclient;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import com.mainteam.chatroom.ServerFrame;
import com.mainteam.login.LoginFrame;
/**
 * ChatFrame: 聊天室客户端 聊天室客户端，与服务端连接，并发送消息
 * 
 * @author M.T
 *
 */
public class ChatFrame extends JFrame {
	static {
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
		}
	}

	private static final long serialVersionUID = 1L;
	private final static int WIDTH = 870;
	private final static int HEIGHT = 700;
	public static int PORT = 8088;
	public boolean flag = true;
	private List<String> list = new ArrayList<String>();
	ImageIcon images[] = new ImageIcon[7];
	String nickName;
	String getText;
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
	JLabel member = new JLabel();
	Color c = new Color(74,112,139);
	JTextPane taChatMessage = new JTextPane();
	Document docs = taChatMessage.getDocument();
	SimpleAttributeSet attrset = new SimpleAttributeSet();
	JScrollPane ChatMessage = new JScrollPane();
	JTextPane txtMsg = new JTextPane();
	JScrollPane txtMsgList = new JScrollPane();
	DefaultListModel<String> dlmUsers = new DefaultListModel<String>();
	JList<String> lstUsers = new JList<String>(dlmUsers);
	DefaultListModel<String> dlmChat = new DefaultListModel<String>();
	JList<String> lstChat = new JList<String>(dlmChat);
	JScrollPane srpList = new JScrollPane();
	JComboBox<String> cmbType = new JComboBox<String>();
	private final JLabel lblNewLabel = new JLabel("USER");

	/**
	 * 构造方法: 用来获取客户端连接时的Socket对象和用户名，同时调用Init方法
	 * 
	 * @param socket
	 * @param nickName
	 */
	public ChatFrame(Socket socket, String nickName) {
		this.nickName = nickName;
		this.socket = socket;
		Init();
		try {
			flag = true;
			lblState.setForeground(c);
			lblState.setFont(new Font("微软雅黑",Font.PLAIN, 12));
			lblState.setText("欢迎  "+nickName+"  来到M.T聊天室");
			lblNewLabel.setFont(new Font("Algerian", Font.PLAIN, 20));
			lblNewLabel.setIcon(new ImageIcon("img/user3.png"));
			lblNewLabel.setBounds(590, 330, 90, 35);
			
			getContentPane().add(lblNewLabel);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("新用户:" + nickName);
			dos.flush();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "连接聊天室失败！");
		}
		System.out.println("线程启动");
		new ServerHandler().start();
		System.out.println("线程启动完毕");

	}

	/**
	 * 初始化窗口组件
	 */
	public void Init() {
		setSize(WIDTH, HEIGHT);
		setTitle("M.T聊天室      -M.T编程组");
		setResizable(false);
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(null);
		Border border = BorderFactory.createEtchedBorder(Color.white, new Color(170, 170, 170));
		lblState.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		lblState.setBorder(new TitledBorder(border, "消息提醒", TitledBorder.ABOVE_TOP, TitledBorder.CENTER,
				new Font("Dialog", Font.PLAIN, 12)));
		lblState.setHorizontalAlignment(SwingConstants.CENTER);
		lblState.setText("未连接至聊天室");
		lblState.setBounds(new Rectangle(580, 7, 275, 46));
		contentPane.add(lblState);

		function.setBorder(new TitledBorder(border, "\n功能模块", TitledBorder.ABOVE_TOP, TitledBorder.CENTER,
				new Font("Dialog", Font.PLAIN, 20)));
		function.setHorizontalAlignment(SwingConstants.CENTER);
		function.setBounds(new Rectangle(580, 60, 275, 275));
		contentPane.add(function);

		srpList.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		lstUsers.setFont(new java.awt.Font("Dialog", Font.PLAIN,20));
		srpList.setBounds(580, 365, 275, 285);
		contentPane.add(srpList);
		srpList.setViewportView(lstUsers);

		ChatMessage.setBounds(15, 7, 550, 450);
		contentPane.add(ChatMessage);
		taChatMessage.setEditable(false);
		//taChatMessage.setLineWrap(true);
		taChatMessage.setFont(new java.awt.Font("Dialog", Font.PLAIN, 20));
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

		btnSend.setBounds(new Rectangle(482, 611, 80, 35));
		btnSend.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnSend.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.blue));
		btnSend.setText("<html><span color=white>发送</span><html>");
		btnSend.addActionListener(new ChatFrame_btnSend_actionAdapter(this));
		contentPane.add(btnSend);

		btnClose.setBounds(new Rectangle(380, 611, 80, 35));
		btnClose.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnClose.setText("<html><span color=white>关闭</span><html>");
		btnClose.addActionListener(new ChatFrame_btnClose_actionAdapter(this));
		btnClose.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		contentPane.add(btnClose);

		btnClear.setBounds(new Rectangle(750, 250, 80, 35));
		btnClear.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnClear.setText("清屏");
		btnClear.addActionListener(new ChatFrame_btnClear_actionAdapter(this));
		contentPane.add(btnClear);

		btnExport.setBounds(new Rectangle(600, 250, 80, 35));
		btnExport.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
		btnExport.setText("导出");
		btnExport.addActionListener(new ChatFrame_btnExport_actionAdapter(this));
		contentPane.add(btnExport);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	/**
	 * 清屏功能
	 * 
	 * @param e
	 */
	public void btnClear(ActionEvent e) {
		taChatMessage.setText("");
	}

	/**
	 * 监听"关闭"按钮的关闭功能，用来关闭客户端的Socket和窗口
	 * 
	 * @param e
	 */
	public void btnClose_actionPerformed(ActionEvent e) {
		try {
			System.exit(0);
			socket.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
	
	public void sendMesg(){
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			String time = sdf.format(date);
			String text = nickName + "\n" + time + "\n" + txtMsg.getText() + "\n";
			txtMsg.setText("");
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(text);
			dos.flush();
			System.out.println("写出完毕");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 监听"发送"按钮的发送功能
	 * 
	 * @param e
	 */
	public void btnSend_actionPerformed(ActionEvent e) {
		sendMesg();
	}

	/**
	 * ServerHandler:服务端处理类 用来并发处理多个客户端的消息
	 * 
	 * @author M.T
	 *
	 */
	class ServerHandler extends Thread {
		public void run() {
			try {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				String line = null;
				while ((line = dis.readUTF()) != null) {
					if (line.startsWith("UserNick:")) {
						String[] data = line.split(":");
						if (dlmUsers.contains(data[1]))
							continue;
						dlmUsers.addElement(data[1]);
					}else  if(line.startsWith("OutUser:")){
						String[] data = line.split(":");
						dlmUsers.removeElement(data[1]);
					} else if (line != "\n") {
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
					} else {
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
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "与服务器断开连接!");
				System.exit(0);
			}

		}

	}
	
	/**
	 * "导出"按钮的导出聊天记录的功能
	 * 
	 * @param e
	 */
	@SuppressWarnings("static-access")
	public void btnExport(ActionEvent e) {
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

/**
 * "关闭"按钮监听类
 * 
 * @author M.T
 *
 */
class ChatFrame_btnClose_actionAdapter implements ActionListener {
	private ChatFrame adaptee;

	ChatFrame_btnClose_actionAdapter(ChatFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnClose_actionPerformed(e);
	}
}

/**
 * "发送"按钮监听类
 * 
 * @author M.T
 *
 */
class ChatFrame_btnSend_actionAdapter implements ActionListener {
	private ChatFrame adaptee;

	ChatFrame_btnSend_actionAdapter(ChatFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnSend_actionPerformed(e);
	}
}
class ChatFrame_btnExport_actionAdapter implements ActionListener {
	private ChatFrame adaptee;

	ChatFrame_btnExport_actionAdapter(ChatFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnExport(e);
	}
}

class ChatFrame_btnClear_actionAdapter implements ActionListener {
	private ChatFrame adaptee;

	public ChatFrame_btnClear_actionAdapter(ChatFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.btnClear(e);
	}
}
class btnKey implements KeyListener{
	private ChatFrame adaptee;
	
	public btnKey(ChatFrame adaptee) {
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
