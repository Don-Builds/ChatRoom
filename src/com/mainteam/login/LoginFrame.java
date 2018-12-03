package com.mainteam.login;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import com.mainteam.chatclient.ChatFrame;

/**
 * LonginFrame:
 * 登录窗口类
 * @author GraY
 *
 */
public class LoginFrame extends JFrame {
	static {
		try {
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
		}
	}
	private static final int LAYOUT_LEFT_TO_RIGHT = 0;
	private static final int WIDTH = 550;
	private static final int HEIGHT = 400;
	private ImageIcon image;
	JPanel contentPane;
	JLabel lblTitle = new JLabel();
	JLabel lblHost = new JLabel();
	JTextField txtName = new JTextField();
	JLabel lblNick = new JLabel();
	JLabel lblImage = new JLabel();
	JTextField txtNick = new JTextField();
	JButton btnLogin = new JButton();
	JButton btnClose = new JButton();
	private final JLabel lblNewLabel = new JLabel("");
	/**
	 * 构造方法：
	 * 初始化Init()
	 */
	public LoginFrame() {

		Init();
	}

	/**
	 * 初始化窗口组件
	 */
	public void Init() {
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(null);
		setResizable(false);
		setTitle("登录框    -M.T编程组");
		lblHost.setFont(new java.awt.Font("Dialog", LAYOUT_LEFT_TO_RIGHT, 15));
		lblHost.setText("服务器地址");
		lblHost.setBounds(new Rectangle(210, 120, 100, 30));
		txtName.setText("");
		txtName.setBounds(new Rectangle(210, 150, 120, 30));
		lblNick.setFont(new java.awt.Font("Dialog", LAYOUT_LEFT_TO_RIGHT, 15));
		lblNick.setText("用户昵称");
		lblNick.setBounds(new Rectangle(210, 180, 100, 30));
		txtNick.setText("");
		txtNick.setBounds(new Rectangle(210, 210, 120, 30));
		btnLogin.setBounds(new Rectangle(235, 250, 67, 30));
		btnLogin.setFont(new java.awt.Font("Dialog", LAYOUT_LEFT_TO_RIGHT, 15));
		btnLogin.setText("登录");
		btnLogin.addActionListener(new LoginJFrame_btnLogin_actionAdapter(this));
		btnClose.setBounds(new Rectangle(200, 200, 67, 30));
		btnClose.setFont(new java.awt.Font("Dialog", LAYOUT_LEFT_TO_RIGHT, 15));
		contentPane.add(lblImage);
		contentPane.add(lblTitle);
		contentPane.add(lblHost);
		contentPane.add(txtName);
		contentPane.add(lblNick);
		contentPane.add(txtNick);
		contentPane.add(btnLogin);
		lblNewLabel.setIcon(new ImageIcon("img/logo.png"));
		lblNewLabel.setBounds(301, 269, 273, 118);
		getContentPane().add(lblNewLabel);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * "登录"按钮的登录功能
	 * @param e
	 */
	public void btnLogin_actionPerformed(ActionEvent e) {
		String hostName = txtName.getText().trim();
		String nickName = txtNick.getText().trim();
		
		if (hostName.equals("") || nickName.equals("")) {
			JOptionPane.showMessageDialog(null, "服务器 地址和用户昵称必须输入!", "错误", JOptionPane.ERROR_MESSAGE);
		} else{
			try {
				InetAddress addr = InetAddress.getByName(hostName);
				Socket socket = new Socket(addr, 8088);
				ChatFrame cf = new ChatFrame(socket, nickName);
				cf.setLocationRelativeTo(cf);
				this.dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "服务器地址不存在或入口未开启!请检查您的输入!");
			}
		}
	}

	class LoginJFrame_btnLogin_actionAdapter implements ActionListener {
		private LoginFrame adaptee;

		LoginJFrame_btnLogin_actionAdapter(LoginFrame clientStart) {
			this.adaptee = clientStart;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.btnLogin_actionPerformed(e);
		}
	}

	public static void main(String[] args) {
		new LoginFrame();

	}
}
