# jBPM Rewards applications
A client to handle a version of the `rewards process`(https://github.com/tkobayas/jbpm6example/tree/master/rewards-basic) remotely (with a few changes)

## What you will find here?

You will find the following meaningful files/directories on this project:

* `rewards-client`: A JavaFX client application that uses the [jBPM remote API] to interact with a business process;
* `rewards-project-1.0.jar`: The server side built artifact that contains the business process we are going to interact remotely;
* `rewards-project`: The jBPM/BPM Suite built project that can be edited using Business Central/jBPM Web authoring web tools.

## Installing the rewards example locally

Supposing you have BPM Suite or jBPM 6.x running locally in your machine, first you must install the artifact that contains the process in the maven repository uses by jBPM/BPM Suite 6.x. The artifact that contains the process is `rewards-project-1.0.jar` and can be found in the root directory of this project.

### Uploading the artifact


Simply Run the following maven command:

~~~
mvn install:install-file -Dfile=./rewards-project-1.0.jar -DgroupId=example \
    -DartifactId=rewards-project -Dversion=1.0 -Dpackaging=jar
~~~

It is also possible to upload the JAR to BPM Suite/jBPM 6 artifact repository. Just go to Artifacts Repository screen and upload the jar `rewards-project-1.0.jar`:

![Artifact Repository](/images/artifact-repository.png?raw=true)

### Deploying 

Now that the artifact is installed, you must deploy it to make the process available in the server process engine. You can deploy it using the REST API or use the web interface in the Deploy screen:

![Process Deployment](/images/process-deployments.png?raw=true)


## Run the client application 


### Creating users

After installing the rewards server side, you must create an user with at least the roles `admin, PM, HR` to have permissions to run the tasks remotely.

* In BPM Suite use the script `./bin/add-users.sh` to create an user on `ApplicationRealm` with the roles mentioned above;
* When using jBPM 6.3 use `./standalone/configuration/users.properties` to configure the user password and `./standalone/configuration/roles.properties` to give its roles.

### Configuring the application

Edit the constants in class `RewardsService` to match the installation you are using. Attention to the `SERVER_URL` constant that should be `http://localhost:8080/business-central` for BPM Suite and `http://localhost:8080/jbpm-console` for jBPM 6.3.

### Run the application 

Finally you should be able to run the JavaFX application. Use the following maven command to run it:

~~~
cd rewards-client
mvn clean package exec:java -Dexec.mainClass="org.jugvale.rewardclient.App"
~~~
