package com.example.gmail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LoggerProxy implements Logger{
private App app=new App();

	@Override
	public App signIn(String username, String password) {
		// TODO Auto-generated method stub
		return (checkIfFound(username,password,true)) ?  
				app.signIn(username, password):null;
	}

	@Override
	public boolean signUp(String username, String password, String name) {
		// TODO Auto-generated method stub
		if(!checkIfFound(username,password,false)) {
			app.signUp(username, password, name);
			app=new App(); app.setName(name);
			app.setPassword(password);app.setUserName(username);
			return true;
		}
		app=new App();
		return false;
	}
	
    //state to identify if we are looking for existing name only for sing up (set state as false) or
	//existing name and password for sign in (state set as true)
    private boolean checkIfFound(String username,String password,boolean state) {
    	List<App> list=Folder.getAppList("C:\\eclipseWorkspace\\System\\users.json");
    	for(App st:list){
    		if((st.getUserName().trim()).compareToIgnoreCase(username)==0) {
				   return (state) ?
						   (st.getPassword().trim().compareToIgnoreCase(password)==0):true;
			  }
    	}
    	return false;
	}
    
    public App getApp() {
    	return this.app;
    }

}
