package com.mainteam.chatclient;

import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class ClientStart {
	public ClientStart(){
		try {
        	BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
        	org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ChatFrame frame = new ChatFrame();
		//frame.validate();
	}
	public static void main(String[] args) {
		new ClientStart();
	}
}
