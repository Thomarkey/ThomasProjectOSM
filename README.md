# README #

###Works with Java 1.11!

#Purpose of this project
The purpose of this project is to provide a base that every employee can use when starting a
new assignment. This will reduce initial setup time for that assignment. Through collaboration
of all colleagues this project will grow over time, setting up a standard for Refleqt to use
in all future assignments.

If you feel something can be improved, please do make a Pull Request!

At 05/07/2019 only selenium web-testing and API-Testing is supplied in the base.

#How to use api-testing

Refer to [Executors](README.md#executors) for explanation of the executors.

## api-support module
The purpose of this module is to generate the swagger-codegen DTO. It 
also provides logging to Rest-Assured and http calls done via the 
generated swagger-codegen DTO.

######To set-up the api-support module:
 * Open `api-support` module
 * Open `pom.xml`
 * Adjust the download and generate steps with your urls
    * When you have more then 1 swagger file
        * Create multiple download steps
        * Create multiple generate steps with the same package.
            * This is necessary for the logging to work.
            * If the Generating fails this means not all Models or Request
            are unique and this should be tackled by your dev team.
 * Select the value of `default-package` if you want to change this
    * `CMD + Shift + R` to replace all
    * Change the default value to the wished dto package name
        * This should change `Logger.aj`, `pom.xml`, `ApiCaller.class` & `ApiResponseOrException.class` 

## api-testing module
This api-testing uses the ApiCaller method. This method returns an
ApiResponseOrException object which contains both ApiResponse and 
ApiException. The latest ApiException is saved to the current state
so that a generic error handling step can be applied to it.

######CommonSteps
The system errors are mapped to the best practice of how to throw 
ApiException in a REST environment. If your dev team has implemented
other ways of handling errors you'll have to adjust these generic
functions.

You can find some examples of how to use @Transpose and World
in the CommonSteps. We expect you delete these functions after
understanding them.

Examples include:
 * POST-call with @Transpose for the body
 * Get-call with a stored ID
 * oAuth call via multi data part with rest-assured

#How to use web-testing

## Executors
####SingleExecutor
Can be used with all environments, no parallisation.

###### System property:
`-DcucumberTag=tag` optional, default value is **wip** (@wip -> work in progress)

####MultiExecutor
Can be used with all environments, with parallisation. 

###### System property:
`-DcucumberTag=tag` optional, default value is **wip** (@wip -> work in progress)

`-Dthreads=4` optional, default value is **4** 

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
    * Defaults to **blank version**
  * `-Dplatform= `
    * platform to use e.g. Windows/Mac
    * Default to "**ANY**"
  * `-Dresolution= `
    * resolution of the browser
    * Defaults to **1920*1080**
  
##Browsers
##### Property to set "browser".
e.g. `-Dbrowser=chrome` -> Default value is **chrome**

###### Options
* Chrome
* Firefox / ff
* IE / Internet Explorer
* MicrosoftEdge / Edge
* Safari

##Global Properties
##### Property to set "takeScreenshots".
e.g. `-DtakeScreenshots=true` 
   
-> Default value:
        
* False if `env` is set to browserstack/saucelabs
* True if `env` is set to local/docker

###### Options
* True
* False
* All other values are seens as False.

##### Property to set "decoratorTimeout".
e.g. `-DdecoratorTimeout=10` 
   
-> Default value: "1"

This property sets the minimum time the decorator will look for elements.
The value is done x6 for the maximum wait time.

###### Options
* Long values

#How to use app-testing

#### New device setup
* Right click on `Resource Bundle 'devices'`
* Select `new` --> `Add Property File to Resource Bundle`
* Press `+` in the popup that opened
* Enter the device you'd like to add
* Press OK
* Copy the content of a device from the same platform
* Change the values that need changing
* Congratulations you'd added a device!

#### TestExecutor
To execute a single device you can use the testExecutor.

For multiple devices you should use testSuite.xml

###### System property:
* `-DcucumberTag=tag` optional, default value is **wip** (@wip -> work in progress)
* `-Ddevice=EMULATOR_ANDROID` optional, default value is **EMULATOR_ANDROID**
* `-Dthreads=1` optional, default value is **1**
    * Recommended to keep this at 1
    * Can be set higher when using cloud provider
    * This will run multiple of your given device and run the scenarios parallel as well

#### Profile
See the profile defined in `pom.xml`

Run the profile by executing `mvn clean verify -PappTestSuite -Dprop...` in your terminal

###### System property:
* `-Ddevices=IPHONE_6,EMULATOR_ANDROID`
    * Give a list of comma separated devices.
    * The list of devices will be fed to `create_xml.py` in /scripts
    * This script will generate a new `testSuite.xml` with those devices
* Provide ENV specific properties here as well! (See next topics)

#### SauceLabs setup
You'll need a sauce labs account with at least one active appium project. Use these credentials.
###### System property:
* `-Dsaucelabs_username=user`
* `-Dsaucelabs_accessKey=key`

#### BrowserStack setup
You'll need a browser stack account!
###### System property:
* `-Dbrowserstack_username=user`
* `-Dbrowserstack_accessKey=key`