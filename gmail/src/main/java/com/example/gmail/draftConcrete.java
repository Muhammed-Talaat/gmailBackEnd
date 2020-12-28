package com.example.gmail;

public class draftConcrete extends MailBuilder{
	
	public draftConcrete() {
		this.setType();
	}

	@Override
	public void setType() {
		// TODO Auto-generated method stub
		super.mail=new draft();
	}
	
	
}
