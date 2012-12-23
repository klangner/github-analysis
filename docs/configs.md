# Examle YAML configs

## Find events

Extract events from given by **repository** property.


```YAML
command: find_events
repository: rails/rails
actor: <user_login>
event_type: PushEvent
date:
  from: 2011-11
  to: 2012-1
  
datapath: /home/klangner/datasets/github
output: events.csv
```

### Parameters:
- **repository** Filter events to this repository only
- **actor** Filter events to this actor only
- **event_type** Show only this event type


## Get repository activity

Extract activity for all repositories in the given time range.
Repositories can be ordered by **order_by** property.


```YAML
command: repo_activity
repository: rails/rails
order_by: forks
date:
  from: 2011-11
  to: 2012-1
  
datapath: /home/klangner/datasets/github
output: repos.csv
```

### Parameters:

- **repository** is optional parameter. If provided it will create report only for given repository.
If not provided, then all repositories will be included.
- **order_by** Allows ordering repositories. Allowed values:
  - community_size - Order by number of all unique actors
  - events - Order by number of all events
  - forks - Order by number of forks
  - pushes - Order by number of push events


## Get repository timeline

Extract activity for all repositories month by month in the given time range.


```YAML
command: repo_timeline
repository: rails/rails
date:
  from: 2011-3
  to: 2012-10
  
datapath: /home/klangner/datasets/github
output: repos_timeline.csv
```

### Parameters:

- **repository** is optional parameter. If provided it will create report only for given repository.
If not provided, then all repositories will be included.



