package com.matrobot.gha.dataset;

/**
Sample data:
    {
    "created_at": "2012-04-01T00:00:00Z",
    "payload": {
        "ref_type": "repository",
        "ref": null,
        "description": "a project with ROR",
        "master_branch": "master"
    },
    "repo": {
        "id": 3889255,
        "url": "https://api.github.dev/repos/azonwan/rable",
        "name": "azonwan/rable"
    },
    "type": "CreateEvent",
    "public": true,
    "actor": {
        "avatar_url": "https://secure.gravatar.com/avatar/ca087d20dcf8cb24426c3e816adcdda7?d=http://github.dev%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
        "gravatar_id": "ca087d20dcf8cb24426c3e816adcdda7",
        "id": 830226,
        "url": "https://api.github.dev/users/azonwan",
        "login": "azonwan"
    },
    "id": "1536460828"
}
*/
public class EventRecord {

	public class Repo{
		public String id;
        public String url;
        public String name;
	}
	
	public class Payload{
		/** “repository”, “branch”, or “tag” */
        public String ref_type;
	}
	
	public class Actor{
        public String login;
	}
	
	
	public String created_at;
	public Repo repo;
	public Repo repository;
	public String type;
	public Payload payload;
//	public Actor actor;
	
	
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
