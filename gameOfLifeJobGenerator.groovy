import java.io.FileReader
import java.nio.file.*


def gitUserNames = ['stein321','liatrio','chrisBlackburn']

gitUserNames.each {
    def buildJobName = it + '-game-of-life-build'
    def newFileName = it + "-gameoflife-web-1.0-SNAPSHOT.war"
    def gitRepo = 'https://github.com/' + it + '/game-of-life.git'
    def deployJobName = it + '-game-of-life-deploy'
    mavenJob(buildJobName) {
        scm {
            git(gitRepo, 'master')
        }
        triggers {
          scm('H/2 * * * *')
        }
        goals('clean install')
        publishers {
            buildPipelineTrigger(deployJobName)
        }
    }
    def shellCommand = "mv gameoflife.war " + newFileName
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
            shell(shellCommand)
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
