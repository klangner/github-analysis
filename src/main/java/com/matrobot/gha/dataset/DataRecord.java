package com.matrobot.gha.dataset;

public class DataRecord {

	public class Repo{
		public String id;
        public String url;
        public String name;
	}
	
	
	public String created_at;
	public Repo repo;
	public Repo repository;
	public String type;
	
	
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
}
