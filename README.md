# chico-jenkins-job-creator
Automation to create build and deployment jobs

A sample config should look like: 

```
{
 "components": [
   {
      "scmProject": "myusername/game-of-life",
      "productName": "game-of-life",
      "deploymentEnvironments": ["dev"],
      "ciEnvironments": ["dev"]
   },
 ]
}
```
