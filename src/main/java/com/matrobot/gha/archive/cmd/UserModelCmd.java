package com.matrobot.gha.archive.cmd;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.matrobot.gha.Configuration;
import com.matrobot.gha.ICommand;
import com.matrobot.gha.archive.event.EventReader;
import com.matrobot.gha.archive.event.EventRecord;
import com.matrobot.gha.archive.event.IEventReader;

/**
 * Check user activity after time range
 * 
 * @author Krzysztof Langner
 */
public class UserModelCmd implements ICommand{

	private static final int TENURE = 10;
	class User{
		String login;
		Set<String> months = new HashSet<String>();
	}

	private Set<String> excludeUsers = new HashSet<String>();
	private HashMap<String, User> users = new HashMap<String, UserModelCmd.User>();
	
	
	@Override
	public void run(Configuration params) throws IOException {

		IEventReader reader;
System.out.println("init");
		List<String> months = params.getMonthFolders();
		reader = new EventReader(months.subList(0, 3));
		excludeAlreadyActivyUsers(reader);
System.out.println("parse and add");
		reader = new EventReader(months.subList(3, months.size()-TENURE));
		parseUserActivity(reader, true);
System.out.println("Parse last " + TENURE);
		reader = new EventReader(months.subList(months.size()-TENURE, months.size()));
		parseUserActivity(reader, false);
		
		createReport();
	}


	/**
	 * It is necessary to exclude users from few beginning months to find new users later.
	 * There is no event related to creating new user account  
	 */
	private void excludeAlreadyActivyUsers(IEventReader reader) {
		
		EventRecord	event;
		while((event = reader.next()) != null){
			String actor = event.getActorLogin();
			if(actor != null){
				excludeUsers.add(actor);
			}
		}
	}


	/**
	 * Parse user activity and add new user if flag set and user not already in excludeUsers
	 * 
	 * @param reader
	 * @param addUser - true to add user
	 */
	private void parseUserActivity(IEventReader reader, boolean addUser) {
		
		EventRecord	event;
		while((event = reader.next()) != null){
			String actor = event.getActorLogin();
			if(actor != null){
				User user = users.get(actor);
				if(user != null){
					user.months.add(findEventMonth(event));
				}
				else if(addUser && !excludeUsers.contains(actor)){
					user = new User();
					user.login = actor;
					user.months.add(findEventMonth(event));
					users.put(actor, user);
				}
			}
		}
	}


	private String findEventMonth(EventRecord event) {
		return event.getCreatedAt().substring(0, 7);
	}


	private void createReport() {

		int[] hist = new int[TENURE];
		for(int i = 0; i < hist.length; i++){
			hist[i] = 0;
		}
		
		for(User user : users.values()){
			int index = user.months.size()-1;
			if(index >= hist.length){
				index = hist.length-1;
			}
			for(int j = 0; j <= index; j++){
				hist[j] ++;
			}
		}

		for(int i = 0; i < hist.length; i++){
			System.out.println((i+1) + ": " + hist[i]);
		}
	}


	/**
	 * for local testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Configuration params = new Configuration("configs/users_model.yaml");
		
		UserModelCmd app = new UserModelCmd();
		app.run(params);
	}
	
}
