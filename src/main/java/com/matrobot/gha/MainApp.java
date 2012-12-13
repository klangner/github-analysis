package com.matrobot.gha;

public class MainApp {

	private ParamParser params;
	
	
	public MainApp(ParamParser params){
		this.params = params;
	}
	
	
	public void run() {
		System.out.println("Run command: " + params.getCommand());
	}


	public static void main(String[] args) {

		ParamParser params = new ParamParser(args);
		
		if(params.hasAllParams()){
			MainApp app = new MainApp(params);
			app.run();
		}
		else{
			showHelp();
		}
	}


	private static void showHelp() {

		System.out.println("Usage:");
		System.out.println("java -jar gha.jar <command> -data=<path> -from=<year-month> -to=<year-month> -repo=<user/repo>");
		System.out.println();

		System.out.println("Command:");
		System.out.println("* find_events: Find all events and save them to csv file");
		System.out.println();

		System.out.println("Example:");
		System.out.println("java -jar gha.jar find_events -data=dataset -from=2011-11 -to=2012-2 -repo=rails/rails");
		System.out.println();
	}
}
