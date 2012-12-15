package com.matrobot.gha;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;


public class ParamParser {

	private String command;
	private String dataPath;
	private String repositoryName;
	private String startDate;
	private String endDate;
	private String outputFilename;
	private PrintStream outputStream;
	
	
	public ParamParser(String[] args){
		
		init(args);
	}
	
	private void init(String[] args) {
		
		for(int i=0; i < args.length; i++){
			String argument = args[i];
			if(argument.startsWith("-data=")){
				setDatapath(argument.substring(6));
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
			else if(argument.startsWith("-output=")){
				outputFilename = argument.substring(8);
			}
			else if(argument.endsWith(".yaml")){
				parserYaml(argument);
			}
			else if(command == null){
				command = argument;
			}
		}
	}

	private void setDatapath(String path) {
		
		dataPath = path;
		if(!dataPath.endsWith("/")){
			dataPath += '/';
		}
	}

	
	@SuppressWarnings("unchecked")
	private void parserYaml(String filename) {

		Yaml yaml = new Yaml();
		try {
			Map<String, Object> config = (Map<String, Object>) yaml.load(new FileInputStream(filename));
			command = config.get("command").toString();
			setDatapath(config.get("datapath").toString());
			repositoryName = config.get("repository").toString();
			outputFilename = config.get("output").toString();
			Map<String, String> dateRange = (Map<String, String>) config.get("date");
			startDate = dateRange.get("from");
			endDate = dateRange.get("to");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

	
	public PrintStream getOutputStream(){
		
		try {
			if(outputStream == null){
				if(outputFilename != null){
					FileOutputStream fos;
						fos = new FileOutputStream(outputFilename, false);
					outputStream = new PrintStream(fos);
				}
				else{
					outputStream = System.out;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			outputStream = System.out;
		}
		
		return outputStream;
	}
}
