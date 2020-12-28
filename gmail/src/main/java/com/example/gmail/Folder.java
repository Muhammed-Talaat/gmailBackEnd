package com.example.gmail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdk.internal.joptsimple.internal.Strings;

public class Folder {
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static boolean deleteDirectory(File dir) {
	    if (dir.isDirectory()) {
	       String[] children = dir.list();
	       for (int i = 0; i < children.length; i++) {
	          boolean success = deleteDirectory (new File(dir, children[i]));
	          if (!success) {
	             return false;
	          }
	       }
	    }
	    return dir.delete();}
	
	
	
	public static List<App> getAppList(String path){
		List<App> list = new ArrayList<App>();
		if(new File(path).length()>0) {
		try {
			list= objectMapper.readValue(new File(path), new TypeReference<List<App>>(){});
		} catch (IOException e1) {
			e1.printStackTrace();
		}}
		return list;
	}
	
	
	public static void writeList(List<?> list,String path) {
		try {
			objectMapper.writeValue(new File(path), list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<Contact> getContactList(String path){
		List<Contact> list = new ArrayList<Contact>();
		if(new File(path).length()>3) {
		try {
			list= objectMapper.readValue(new File(path), new TypeReference<List<Contact>>(){});
		} catch (IOException e1) {
			e1.printStackTrace();
		}}
		return list;
	}
	
	public static List<Mail> loadMailsFromFolder(String type,String path){
		   String[] children = (new File(path)).list();
	           	 List<Mail> ls = new ArrayList<Mail>();
		for(String st : children) {
			if(st.contains(".json")) {
			boolean flag=true;
			Mail temp = null;
			try {
				 if(type.compareToIgnoreCase("sent")==0) {temp = new Sent();}
				 if(type.compareToIgnoreCase("inbox")==0) {temp = new inbox();}
				 if(type.compareToIgnoreCase("trash")==0) {temp = new trash();}
				 if(type.compareToIgnoreCase("draft")==0) {temp = new draft();}
				 if(type.compareToIgnoreCase("user")==0) {temp = new User();}
				 temp=objectMapper.readValue(new File(path+"\\"+st), temp.getClass());
				 temp.setDirectory(path+"\\"+st);
				 if(type.compareToIgnoreCase("trash")==0) {
					flag=AppBuilderConcrete.checkDate30(temp.getDate());
					if(!flag) {
						(new File(temp.getDirectory())).delete();
						String aug[]=temp.getDirectory().split("\\\\");
						aug[aug.length-1]="attachment\\"+temp.getDate();
						Folder.deleteDirectory(new File(String.join("\\",aug)));
					}
				 }
			} catch (IOException e) {
				e.printStackTrace();flag=false;
			}
			if(flag)ls.add(temp);
			}
		}
		return ls;
	}
	
	public static boolean createDirectory(String path,String name) {
		return (new File(path+name)).mkdir();
	}
	
	public static void createMailFileDate(String path,Mail mail) {
		String _path=path+"\\"+mail.getDate()+".json";
		try {
			(new File(_path)).createNewFile();
			objectMapper.writeValue(new File(_path), mail);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean renameDirectory(String path,String Oldname,String newName) {
		return (new File(path+"\\"+Oldname)).renameTo(new File(path+"\\"+newName));
	}
	
	public static void moveFileMail(String oldPath,String newPath) {
		try {
			Files.copy((new File(oldPath)).toPath(), (new File(newPath)).toPath(), StandardCopyOption.REPLACE_EXISTING);
			(new File(oldPath)).delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void moveAttachment(InputStream in,Path path) {
		try {
			Files.copy(in,path,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
