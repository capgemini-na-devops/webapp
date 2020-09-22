project(key: 'MYP32', name: 'DSLProject_webapp') {
    plan(key: 'PETS32', name: 'DSLPlans_webapp') {
        description ''
        enabled true
		
		triggers {
		remote() {
        description 'my remote trigger'
        repositories 'webapp'
        ipAddresses '0.0.0.0/0'
        }
	
		polling() {
        description 'mypollsched'
        repositories 'webapp'
        scheduled {
            cronExpression '* * * ? * *'
        }
    }
    }      
         stage(name: 'Development') {
            description ''
            manual false
			
			branches {
	 	 branch(name: 'master') {
          vcsBranchName 'master/webapp'
        }
	}
        
            job(key: 'JOB1', name: 'webapp_Job') {
                description ''
                enabled true
            
                tasks { 
				     checkout() {
						description 'checkout repo'
						forceCleanBuild true
                       		
      scm{
        linkedRepository("webapp")
      }				
						}
                    custom(pluginKey: 'com.atlassian.bamboo.plugins.vcs:task.vcs.checkout') {
                        description 'github'
                        enabled true
                        isFinal false
                        configure(
                             'cleanCheckout': '',
                             'selectedRepository_0': 'defaultRepository',
                             'checkoutDir_0': '',
                        )
                    }
					custom(pluginKey: 'com.atlassian.bamboo.plugins.bamboo-artifact-downloader-plugin:artifactdownloadertask') {
                        description ''
                        enabled false
                        isFinal false
                        configure(
                             'sourcePlanKey': 'MYP32-PETS32',
                             'localPath_1': '',
                             'artifactName_1': 'webapp',
							 'url':'https://github.com/capgemini-na-devops/webapp.git'
                        )
                    } 
                    
                    custom(pluginKey: 'com.atlassian.bamboo.plugins.maven:task.builder.mvn3') {
                        description 'Build'
                        enabled true
                        isFinal false
                        configure(
                             'projectFile': '',
                             'goal': 'clean package',
                             'testDirectoryOption': 'standardTestDirectory',
                             'environmentVariables': '',
                             'testResultsDirectory': '**/target/surefire-reports/*.xml',
                             'buildJdk': 'JDK 1.8',
                             'label': 'maven1',
                             'testChecked': '',
                             'workingSubDirectory': '',
                             'useMavenReturnCode': 'false',
                        )
                    } 
                    custom(pluginKey: 'ch.mibex.bamboo.sonar4bamboo:sonar4bamboo.maven3task') {
                        description 'sonarqube'
                        enabled false
                        isFinal false
                        configure(
                             'incrementalFileForInclusionList': '',
                             'chosenSonarConfigId': '1',
                             'useGradleWrapper': '',
                             'sonarMainBranch': 'master',
                             'useNewGradleSonarQubePlugin': '',
                             'sonarJavaSource': '',
                             'sonarProjectName': '',
                             'buildJdk': 'JDK 1.8',
                             'gradleWrapperLocation': '',
                             'sonarLanguage': '',
                             'sonarSources': '',
                             'useGlobalSonarServerConfig': 'true',
                             'incrementalMode': '',
                             'sonarPullRequestAnalysis': '',
                             'failBuildForBrokenQualityGates': '',
                             'msbuilddll': '',
                             'sonarTests': '',
                             'incrementalNoPullRequest': 'incrementalModeFailBuildField',
                             'failBuildForSonarErrors': '',
                             'sonarProjectVersion': '',
                             'sonarBranch': '',
                             'executable': 'Maven1',
                             'illegalBranchCharsReplacement': '_',
                             'failBuildForTaskErrors': 'true',
                             'incrementalModeNotPossible': 'incrementalModeRunFullAnalysis',
                             'sonarJavaTarget': '',
                             'environmentVariables': '',
                             'incrementalModeGitBranchPattern': '(.*/)?feature/.*',
                             'legacyBranching': '',
                             'replaceSpecialBranchChars': '',
                             'additionalProperties': '',
                             'autoBranch': '',
                             'sonarProjectKey': '',
                             'incrementalModeBambooUser': '',
                             'overrideSonarBuildConfig': '',
                             'workingSubDirectory': '',
                        )
                    } 
                    custom(pluginKey: 'com.atlassian.bamboo.plugins.scripttask:task.builder.script') {
                        description 'nexus'
                        enabled false
                        isFinal false
                        configure(
                             'argument': '',
                             'scriptLocation': 'INLINE',
                             'environmentVariables': '',
                             'scriptBody': '''curl -v -u admin:admin123 --upload-file ${bamboo.build.working.directory}/target/petclinic.war http://18.236.232.129:8081/nexus/content/repositories/petclinic_repo/releases/org/foo/${bamboo.buildNumber}/petclinic.war
                    
                    cp /usr/local/sonatype-work/nexus/storage/petclinic_repo/releases/org/foo/${bamboo.buildNumber}/petclinic.war ${bamboo.build.working.directory}/target''',
                             'interpreter': 'LEGACY_SH_BAT',
                             'script': '',
                             'workingSubDirectory': '',
                        )
                    } 
                    custom(pluginKey: 'com.atlassian.bamboo.plugins.tomcat.bamboo-tomcat-plugin:deployAppTask') {
                        description ''
                        enabled true
                        isFinal false
                        configure(
                             'appVersion': '',
                             'tomcatUrl': 'http://18.237.88.206:9000/manager',
                         'warFilePath': '/target/SampleWebApplication.war',
                             'tomcatUsername': 'admin',
                             'deploymentTag': '',
                             'encTomcatPassword': 'yd/bAsOfEO4uVZLjHOc3rA==',
                             'appContext': '/mypet',
                             'tomcat7': '',
                        )
                    } 
                }
            
                artifacts { 
                    definition(name: 'webapp', copyPattern: '**/*.war') {
                        location 'target'
                        shared true
                    } 
            
                }
            } 

        } 
		
		        deploymentProject(name: 'Deployment for DSLProject') {
            description ''
           
            environment(name: 'Development') {
                description ''
				triggers {
			afterSuccessfulStage() {
            planStageToTriggerThisDeployment 'Development'
            customPlanBranchName 'master'
        }
    }
                tasks { 
                  
                  custom(pluginKey: 'com.atlassian.bamboo.plugins.bamboo-artifact-downloader-plugin:cleanWorkingDirectoryTask') {
                        description ''
                        enabled true
                        isFinal false
                        configure(
                        )
                    } 
                    custom(pluginKey: 'com.atlassian.bamboo.plugins.bamboo-artifact-downloader-plugin:cleanWorkingDirectoryTask') {
                        description ''
                        enabled true
                        isFinal false
                        configure(
                        )
                    } 
          custom(pluginKey: 'com.atlassian.bamboo.plugins.bamboo-artifact-downloader-plugin:artifactdownloadertask') {
                        description ''
                        enabled true
                        isFinal false
                        configure(
                             'sourcePlanKey': 'MYP32-PETS32',
                             'artifactName_0': 'webapp',
                             //'artifactId_0': '1114146',
                             'localPath_0': '',
                        )
                    } 
                  
                    custom(pluginKey: 'com.atlassian.bamboo.plugins.tomcat.bamboo-tomcat-plugin:deployAppTask') {
                        description ''
                        enabled true
                        isFinal false
                        configure(
                             'appVersion': '',
                             'tomcatUrl': 'http://18.237.88.206:9000/manager',
                             'warFilePath':'/SampleWebApplication.war',
                             'tomcatUsername': 'admin',
                             'deploymentTag': '',
                             'encTomcatPassword': 'yd/bAsOfEO4uVZLjHOc3rA==',
                             'appContext': '/mypet',
                             'tomcat7': '',
                        )
                    }
                } 
            
            }					
        
        }
	}
}
