# Installation

## Step 1. Download github archive data

Data can be downloaded from http://githubarchive.org
Put each month in separate folder with name year-month (e.g 2012-4)


## Step 2. Download application

You can download binary gha.jar file from:
https://docs.google.com/folder/d/0Byh8AijvGkRlRzBOeEp5TlpNVm8/edit


## Step 3. Run application

You can run application to extract events for particular repository with command:

    java -jar gha.jar find_events -data=<path_to_data> -from=2011-11 -to=2012-2 -repo=rails/rails

This command will create events.csv file.
