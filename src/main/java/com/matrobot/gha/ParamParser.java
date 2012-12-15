package com.matrobot.gha;

import java.util.ArrayList;
import java.util.List;


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
				if(!dataPath.endsWith("/")){
					dataPath += '/';
				}
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
	 * @return Full path folders based on date range
	 */
	public List<String> getMonthFolders(){
		
		List<String> folders = new ArrayList<String>();
		
		String[] tokens;
		int month;
		int year;
		int endMonth;
		int endYear;
		
		tokens = startDate.split("-");
		if(tokens.length == 2){
		
			year = Integer.parseInt(tokens[0]);
			month = Integer.parseInt(tokens[1]);
			
			tokens = endDate.split("-");
			if(tokens.length == 2){
			
				endYear = Integer.parseInt(tokens[0]);
				endMonth = Integer.parseInt(tokens[1]);
		
				while(year < endYear || month <= endMonth){
					folders.add(dataPath + year + "-" + month);
					
					month ++;
					if(month > 12){
						year ++;
						month = 1;
					}
				}
			}
		}
		
		return folders;
	}

	
	/**
	 * @return -repo=
	 */
	public String getRepositoryName() {
		return repositoryName;
	}
	
	
	/**
	 * @return True if all required params were provided
	 */
	public boolean hasAllParams(){
		
		return (command != null && dataPath != null && repositoryName != null &&
				startDate != null && endDate != null);
	}
	
}
