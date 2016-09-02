# chico-jenkins-job-creator
Automation to create build and deployment jobs

### [gameOfLifeJobGenerator.groovy](./gameOfLifeJobGenerator.groovy)

This jenkins DSL generates build, deploy jobs, and buildPipeline views for game of life builds. At the top is a list of git usernames. The _each_ loop will iterate through to create those jobs and views.

Any jenkins job that references this repo will be able to dynamically updates jobs within minutes of new commits. 
