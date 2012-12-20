# Examle YAML configs

## Find events

Extract events from given by **repository** property.


```YAML
command: find_events
repository: rails/rails
date:
  from: 2011-11
  to: 2012-1
  
datapath: /home/klangner/datasets/github
output: events.csv
```

## Get repository activity

Extract activity for all repositories in the given time range.
Repositories can be ordered by **order_by** property.


```YAML
command: repo_activity
order_by: forks
date:
  from: 2011-11
  to: 2012-1
  
datapath: /home/klangner/datasets/github
output: repos.csv  
```

### Supported order_by fields:
- forks - Order by number of forks


