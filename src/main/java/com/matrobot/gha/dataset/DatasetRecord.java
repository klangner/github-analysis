package com.matrobot.gha.dataset;

public class DatasetRecord {

	public class Repo{
		public String id;
        public String url;
        public String name;
	}
	
	
	public class Payload{
		/** “repository”, “branch”, or “tag” */
        public String ref_type;
	}
	
	
	public String created_at;
	public Repo repo;
	public Repo repository;
	public String type;
	public Payload payload;
	
	
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
		
		int index = id.indexOf("/repos/");
		if(index > 0){
			id = id.substring(index+7);
		}
		return id;
	}
	
	
	/**
	 * Is this create new repository event?
	 */
	public boolean isCreateRepository(){
		
		return (type.equals("CreateEvent") && payload.ref_type.equals("repository"));
	}
}
