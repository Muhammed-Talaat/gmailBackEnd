package com.example.gmail;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class sentConcrete extends MailBuilder{

	public sentConcrete() {this.setType();}
	
	@Override
	public void setType() {
		// TODO Auto-generated method stub
		super.mail=new Sent();
	}


}