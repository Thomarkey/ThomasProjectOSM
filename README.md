# README #

###Only works with Java 1.8!

#How to use web-testing

## Executors
####Local
Can be used with all environments, no parallisation. Fixed @wip tag

####TestExecutor
Can be used with all environments, with parallisation. tag optional, default value = @wip

####JenkinsExecutor
Can be used with all environments, no parallisation. expects property cucumberTags to be set.
 

##Environment
####Property to set "env".
e.g. -Denv=local --> default value is local

####Options
 * local
 * docker / docker-grid / grid
 * browserstack
 * saucelabs
 
####With env = browserstrack/saucelabs expected parameters:
  * -Dusername=
  * -DaccessKey= 
####With env = browserstrack/saucelabs optional parameters:
  * -Dversion= (version of browser)
  * -Dplatform= (platform to use e.g. Windows/Mac)
  * -Dresolution=
  
##Browsers
####Property to set "browsers".
e.g. -Dbrowser=chrome -> Default value is chrome

####Options
* Chrome
* Firefox / ff
* IE / Internet Explorer
* MicrosoftEdge / Edge
* Safari

 
