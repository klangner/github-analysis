# GitHub-Analysis

This project contains code for parsing and analysis data availabe from githubarchive.org


## Installation

- Download data from http://githubarchive.org. Put each month in separate folder with name year-month (e.g 2012-4)
- [Download gha.jar](https://docs.google.com/folder/d/0Byh8AijvGkRlRzBOeEp5TlpNVm8/edit).



## Run application

Run the application to extract events for particular repository with command:

    java -jar gha.jar find_events -data=<path_to_data> -from=2011-11 -to=2012-2 -repo=rails/rails

This command will create events.csv file.


## Packages

- **com.matrobot.gha.archive** - Contains code to parse githubarchive.org files and create 
intermediate csv and json files which can be processed by other packages or 
external programs like [weka](http://www.cs.waikato.ac.nz/~ml/weka/).
- **com.matrobot.gha.insights** - Contains code for analyzing data and find patterns in it.
- **com.matrobot.gha.ghapi** - Get data from GitHub API.


Important links
===============

- Visualization: http://matrobot.com
- Data: http://githubarchive.org
- [Google groups for githubarchive.org](https://groups.google.com/forum/?fromgroups=#!forum/github-archive)


Dependencies
============

This project uses Maven to resolve dependencies


