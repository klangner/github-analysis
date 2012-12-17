package com.matrobot.gha.archive;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Read event from archive 
 */
public class EventRecord {

	public class Repo{
		public String id;
        public String url;
        public String name;
	}

	public class Actor{
		String login;
	}
	
	public class Author{
		String name;
		String email;
	}
	
	public class Commit{
		public Author author;
	}
	
	public class Payload{
		/** “repository”, “branch”, or “tag” */
        public String ref_type;
        /** 03-05 2011: “repository”, “branch”, or “tag” */
		public String object;
		/** Number of commits */
		public int size;
		/** Commits ver. 1*/
		public List<List<String>> shas;
		/** Commits ver. 2*/
		public List<Commit> commits;
	}
	
	public class ActorAttributes{
		String email;
        String login;
    }
	
	public String created_at;
	public Repo repo;
	public Repo repository;
	public String type;
	public Payload payload;
	public Actor actor;
	public ActorAttributes actor_attributes;
	
	
	/**
	 * Get repository id as: "username/repository_name"
	 */
	public String getRepositoryId(){
		
		String id;
		if(repo != null){
			id = repo.url;
		}
		else if(repository != null){
			id = repository.url;
		}
		else{
			return null;
		}
		
		String[] tokens = id.split("\\/");
		int count = tokens.length;
		if(count > 2){
			id = tokens[count-2] + "/" + tokens[count-1];
		}
		
		return id;
	}
	
	
	/**
	 * Get committers set
	 */
	public Set<String> getCommitters(){
		
		Set<String> committers = new HashSet<String>();
		
		if(payload.shas != null){
			for(List<String> commit : payload.shas){
				if(commit.size() > 3){
					committers.add(commit.get(3));
				}
			}
		}
		else if(payload.commits != null){
			for(Commit commit : payload.commits){
				committers.add(commit.author.name);
			}
		}
			
		return committers;
	}
	
	
	/**
	 * Is this create new repository event?
	 */
	public boolean isCreateRepository(){
		
		if(type.equals("CreateEvent")){
			if(payload.ref_type != null){
				return (payload.ref_type.equals("repository"));
			}
			else if(payload.object != null){
				return (payload.object.equals("repository"));
			}
		}
		
		return false;
	}
	
	
	/**
	 * Get actor email or epty string if not available
	 */
	public String getActorLogin(){
		
		String login = "";
		if(actor_attributes != null && actor_attributes.login != null){
			login = actor_attributes.login;
		}
		else if(actor != null){
			login = actor.login;
		}
		
		return login;
	}

	
	/**
	 * @return Header for CSV file
	 */
	public static String getCSVHeaders(){
		return "created_at, repository, type\n";
	}
	
	
	public String toCSV(){
		return created_at + "," + getRepositoryId() + "," + type + "\n";
	}
}
