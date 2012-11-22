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
	
	public String getRepositoryURL(){
		if(repo != null){
			return repo.url;
		}
		else if(repository != null){
			return repository.url;
		}
		return null;
	}
}
