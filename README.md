# README #

###Only works with Java 1.8!

#Purpose of this project
The purpose of this project is to provide a base that every emloyee can use when starting a
new assignment. This will reduce initial setup time for that assignment. Through collaboration
of all colleagues this project will grow over time setting up a standard for Refleqt to use
in all future assignments.

If you feel something can be improved, please do make a Pull Request!

At 24/06/2019 only selenium web-testing is supplied in the base.

#How to use web-testing

## Executors
####SingleExecutor
Can be used with all environments, no parallisation.

###### System property:
`-DcucumberTag=tag` optional, default value = @wip (work in progress)

####MultiExecutor
Can be used with all environments, with parallisation. 

###### System property:
`-DcucumberTag=tag` optional, default value = @wip (work in progress)

`-Dthreads=4` optional, default value = 4 

#### @BeforeSuite / @AfterSuite
In these TestNG Tags docker will be setup. The version used in docker is defined by 
the maintainer of the library. If you'd like to overwrite the used tag follow these steps.
It is not recommended to use "latest" since updates in this docker container could break
your test suite.

###### Changing the docker tag that is used:

 * go to `https://hub.docker.com/r/selenium/standalone-firefox/tags`
 * Select a tag (These are shared between all selenium official containers)
 * Fill the setupDocker with your selected tag
 * e.g. `DockerProvider.getInstance().setupStandAlone("latest");`


##Environment
##### Property to set "env".
e.g. `-Denv=local` --> default value is **local**

###### Options

 * local
    * Start a local webDriver (e.g. chromedriver) to run the tests on.
 * docker / docker-grid / grid
    * Starts a remote webDriver that connects to localhost (Where docker should be running)
 * browserstack
    * Start a remote webDriver that connects to Browserstack
 * saucelabs
    * Start a remote webDriver that connects to SauceLabs
 
###### With env = browserstrack/saucelabs required parameters:

  * `-Dusername= `
    * This username can be found on the website of the cloud provider
  * `-DaccessKey= ` 
      * This access key or API key can be found on the website of the cloud provider
      
###### With env = browserstrack/saucelabs optional parameters:

  * `-Durl= `
    * If you'd like to overwrite the remote webdriver url
  * `-Dversion= `
    * Version of browser that the test will be run on
    * Defaults to blank version
  * `-Dplatform= `
    * platform to use e.g. Windows/Mac
    * Default to "ANY"
  * `-Dresolution= `
    * resolution of the browser
    * Defaults to 1920*1080
  
##Browsers
##### Property to set "browser".
e.g. `-Dbrowser=chrome` -> Default value is **chrome**

###### Options
* Chrome
* Firefox / ff
* IE / Internet Explorer
* MicrosoftEdge / Edge
* Safari