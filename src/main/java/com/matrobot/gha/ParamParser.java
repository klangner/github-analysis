package com.matrobot.gha;


public class ParamParser {

	private String command;
	private String dataPath;
	private String repositoryName;
	private String startDate;
	private String endDate;
	
	
	public ParamParser(String[] args){
		
		init(args);
	}
	
	private void init(String[] args) {
		
		for(int i=0; i < args.length; i++){
			String argument = args[i];
			if(argument.startsWith("-data=")){
				dataPath = argument.substring(6);
			}
			else if(argument.startsWith("-from=")){
				startDate = argument.substring(6);
			}
			else if(argument.startsWith("-to=")){
				endDate = argument.substring(4);
			}
			else if(argument.startsWith("-repo=")){
				repositoryName = argument.substring(6);
			}
			else if(command == null){
				command = argument;
			}
		}
	}

	/**
	 * @return command
	 */
	public String getCommand(){
		return command;
	}
	
	/**
	 * @return -data= parameter
	 */
	public String getDataPath(){
		return dataPath;
	}
	
	
	/**
	 * @return -from=
	 */
	public String getStartDate(){
		return startDate;
	}
	
	
	/**
	 * @return -to=
	 */
	public String getEndDate(){
		return endDate;
	}

	
	/**
	 * @return -repo=
	 */
	public String getRepositoryName() {
		return repositoryName;
	}
	
	
	public boolean hasAllParams(){
		
		return (command != null && dataPath != null && repositoryName != null &&
				startDate != null && endDate != null);
	}
}
