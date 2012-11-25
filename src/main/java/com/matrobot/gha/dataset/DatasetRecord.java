package com.matrobot.gha.dataset;

public class DatasetRecord {

	public class Repo{
		public String id;
        public String url;
        public String name;
	}
	
	
	public class Payload{
		/** name if fork null if not */
		public String ref;
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
	
	
	public boolean isCreateRepository(){
		
		if(type.equals("CreateEvent") && payload.ref_type.equals("repository")){
			if(payload.ref != null){
				System.out.println(payload.ref);
			}
			return true;
		}
		
		return false;
	}
}
