# Examle YAML scripts

## Find events

```YAML
# Extract events from repository rails/rails into events.csv where data in range from 2011-11 to 2012-1

command: find_events

repository: rails/rails

date:
  from: 2011-11
  to: 2012-1
  
datapath: /home/klangner/datasets/github

output: events.csv  
```

## Get repository activity

```YAML
# Extract events from repository rails/rails into events.csv where data in range from 2011-11 to 2012-1

command: repo_activity

date:
  from: 2011-11
  to: 2012-1
  
datapath: /home/klangner/datasets/github

output: repos.csv  
```


