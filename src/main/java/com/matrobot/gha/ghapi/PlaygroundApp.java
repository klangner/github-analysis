package com.matrobot.gha.ghapi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;

public class PlaygroundApp {

	private Properties prop = new Properties();
	
	
	public PlaygroundApp() throws IOException{
		
		prop.load(new FileInputStream("config.properties"));
	}
	
	
	protected void test() throws IOException {

		RepositoryService service = new RepositoryService();
		for (Repository repo : service.getRepositories("klangner")){
			System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
		}
	}

	
	public static void main(String[] args) throws IOException {

		PlaygroundApp app = new PlaygroundApp();
		
		app.test();
	}

}
