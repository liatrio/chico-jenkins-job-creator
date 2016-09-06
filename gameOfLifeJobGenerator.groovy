import java.io.FileReader
import java.nio.file.*


/*
Plugins Required:

Job DSL
Groovy
Git plugin
Build Pipeline Plugin
Copyartifact

*/

def gitUserNames = ['stein321','liatrio','chrisjblackburn']

gitUserNames.each {
    def buildJobName = it + '-game-of-life-build'
    def newFileName = it + "-gameoflife.war"
    def gitRepo = 'https://github.com/' + it + '/game-of-life.git'
    def deployJobName = it + '-game-of-life-deploy'
    def clickAbleUrl = "http://tomcat.chico.liatr.io/" + newFileName.split("\\.")[0]
    def clickableUrlLink = "echo SUCCESS! Your app has been deploy to: " +  clickAbleUrl
    mavenJob(buildJobName) {
        scm {
            git(gitRepo, 'master') {
                createTag(false)
            }

        }
        triggers {
          scm('H/2 * * * *')
        }
        goals('clean install')
        publishers{
            buildPipelineTrigger(deployJobName)
        }
    }
    def renameFile = "mv gameoflife.war " + newFileName
    def scpCommand = "scp -i /var/lib/jenkins/.ssh/id_rsa -o StrictHostKeyChecking=no " + newFileName + " tomcat-deploy@ip-172-31-26-108.us-west-2.compute.internal:/var/lib/tomcat8/webapps/"
    job(deployJobName){
        //copy artifact
        steps {
            copyArtifacts(buildJobName) {
                buildSelector{
                    workspace()
                }
                includePatterns('gameoflife-web/target/*')
                flatten()
            }
            shell(renameFile + "\n" + scpCommand + "\n" +  clickableUrlLink)

        }
        //rename
        //scp
    }
    def title = it + "-game-of-life"
    buildPipelineView(it + "-game-of-life") {
        filterBuildQueue()
        filterExecutors()
        displayedBuilds(5)
        selectedJob(buildJobName)
        alwaysAllowManualTrigger()
        showPipelineParameters()
        refreshFrequency(60)
    }
}
listView('Build Jobs') {
    description('Build jobs')
    filterBuildQueue()
    filterExecutors()
    jobs {

        regex(/.+-game-of-life-build/)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
listView('Deploy Jobs') {
    description('Deploy jobs')
    filterBuildQueue()
    filterExecutors()
    jobs {

        regex(/.+-game-of-life-deploy/)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
