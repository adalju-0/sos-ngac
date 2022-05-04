# Arrowhead Framework System of Systems NGAC Project (Java Spring-Boot)

## Description
A System of Systems implementation of NGAC in the Eclipse Arrowhead Framework. In the demo, a consumer sends requests to access (read/write) a data object in a time series database, and is given permission only if the corresponding policy is set in the NGAC server.

![SoS NGAC UML Component Diagram](doc/SystemsHolistic.png?raw=true "SoS NGAC UML Component Diagram")

### Policy used in the demo

<img src="doc/Policy.png" width="930">


### Requirements

The project has the following dependencies:
* **JRE/JDK 11** [Download from here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
* **Maven 3.5+** [Download from here](http://maven.apache.org/download.cgi) | [Install guide](https://www.baeldung.com/install-maven-on-windows-linux-mac)
* **The Open Group's Policy Machine**  [Download from here](https://github.com/esen96/tog-ngac-crosscpp-LTU) | The policy server provider of this project interfaces with the policy machine to enforce access rights and utilizes a local instance of the ngac server. [Setup guide](#ngacserver)
* **InfluxDB** [Download from here](https://portal.influxdata.com/downloads/)  
* **Authorization settings for the demo systems in your local arrowhead database**

  ***By Arrowhead Management Tool***
  - coming soon
  
  ***By Swagger API documentation***
  
  - [Click here](#authorizationsettings) for a detailed description on Swagger API management for this project. 
  
  ***By MySQL queries***
  
  *Intra-Cloud:*
  - Insert a new entry with the consumer details into the `system_` table.
  - Insert a new entry with the IDs of consumer entry, provider entry and the service definition entry into the `authorization_intra_cloud` table.
  - Insert a new entry with the IDs of authorization intra cloud entry and service interface entry into the `authorization_intra_cloud_interface_connection` table.
  
  *Inter-Cloud:*
  - Insert a new entry with the cloud details into the `cloud` table. The `authentication_info` have to be filled out with the gatekeper's public key of the cloud.
  - Insert a new entry with the IDs of the cloud entry, provider entry and the service definition entry into the `authorization_inter_cloud` table.
  - Insert a new entry with the IDs of authorization inter cloud entry and service interface entry into the `authorization_inter_cloud_interface_connection` table.



# Setup guide

## Launching the Arrowhead Core Systems

For the main documentation, go to the Arrowhead [Core Java Spring](https://github.com/eclipse-arrowhead/core-java-spring) repository.

Here is also a video of the setup process on a Windows system: https://www.youtube.com/watch?v=52Up5iDJKx4&ab_channel=AITIAInternationalZrt.

### Potential install issues

I ran into certain issues when installing the core systems that I decided to list here in case it might be of use to anyone.

1) The Docker setup doesn't seem to work out of the box due to a docker image issue. Me and several others have encountered the same issue but it is yet to be fixed. See [this issue](https://github.com/eclipse-arrowhead/core-java-spring/issues/361)
2) I had to install MySQL 5.7+ instead of the newer MySQL 8.0+ versions when doing a native install since the SQL scripts in the core java spring repository utilizes deprecated commands
3) I had to change my MySQL config file to allow remote connections in order to successfully run the install scripts




<a name="ngacserver" />

## Setting up the NGAC server

Download [SWI-Prolog](https://www.swi-prolog.org/Download.html) and clone the [NGAC Policy Machine](https://github.com/esen96/tog-ngac-crosscpp-LTU).

Follow the setup instructions in chapter 9 of the pdf documentation. For this project, we run the server in JSON mode, this is done by navigating to the root folder of the NGAC repository and running the ``./ngac-server -j`` command after the systems have been installed. 

Note that there might be authorization issues related to security when running the server on a Windows system, which is why we have mostly been running the NGAC server on Linux systems.

To set the demo policy for this project and run a few tests, navigate to the ``sos-ngac-demo`` folder and execute the ``demo.sh`` script.

## InfluxDB configuration
Enable HTTP communication in your influxDB config file and bind the port to its original address, leave the standard time formatting as is.

The DB configuration can be deduced by looking at the [Resource Access Point Constants](https://github.com/esen96/sos-ngac/blob/master/sos-ngac/sos-ngac-resource-system/src/main/java/ai/aitia/sos_ngac/resource_system/rap/RAPConstants.java).

In essence: 
* Create a root user with full priliveges and the same configuration as listed in the RAPConstants file. Alternatively, you can create a superuser with your own information, in which case you'll need to change some of the constant values. 

* Create a database either with the same name as the one listed in the constants, or create one with another name and change the constant value.

* The measurement used in the demo will be automatically created and populated by running the sensor function in the Consumer system. 

## Running the project

1) Clone this repository
2) Start the NGAC server in JSON response mode
3) Load and set the correct policy file by navigating to the ``sos-ngac-demo`` folder and running the ``demo.sh`` file. Ensure that the test cases are giving the correct responses 
4) If you're using the application executables in the provided release, then the following step is not necessary. However, if you plan on running this project natively, then navigate to the root foolder of this reposity and run the command:

```
mvn install
```
A successful install will result in the following response:

![mvn install image](doc/mvninstall.png?raw=true "mvn install")

<a name="authorizationsettings" />

## Authorization settings

It might be helpful to look at [this video](https://www.youtube.com/watch?v=9BHemnv3mQA&ab_channel=AITIAInternationalZrt.) for a demonstration of how a sample system is run. The steps for this project will be very similar to the one in the video.

1) Make sure you have access to the Swagger API:s of the Service Registry at ``https://localhost:8443``, and the Authorization at ``https://localhost:8445``. See the video or [the documentation](https://github.com/eclipse-arrowhead/core-java-spring) for instructions.
2) Run the Policy Server- and the Resource System provider applications. These providers automatically register their services in the Service Registry core system. 
3) Go to the Swagger API of the Service Registry, open the ``Management`` tab and call ``GET serviceregistry/mgmt`` -> ``Try it out`` -> ``Execute``. 
4) Copy the entire JSON data body from the of the ``query-interface`` service by the ``policyserver`` provider and the ``request-resource`` service of the ``resourcesystem`` provider. Save this information somewhere like a temporary .txt file as you will need it for setting the authorization rules
5) Register the consumer by heading to the Swagger API of the Service Registry. Under the tab ``Management``, use ``POST serviceregistry/mgmt/systems`` -> ``Try it out`` and fill in the body of the consumer:

```
{
  "address": "localhost",
  "port": 8080,
  "systemName": "resourceconsumer"
}
```
Click ``Execute`` and you should get a JSON response with the full body of the consumer. Also save this data along with the data from earlier.

6) Go to the Authorization Swagger API at https://localhost:8445 and go to ``Management`` -> ``POST authorization/mgmt/intracloud`` and enter two sets of authorization rules.
 * Consumer -> Resource System: Fill the ``consumerID`` field with the ID from the resourceconsumer body created earlier. Enter the ``interfaceID``, ``providerID``, and ``serviceDefinitionID`` of the ``request-resource`` service system definition of the ``resourcesystem`` provider that we saved from earlier
 * Resource System -> Policy Server: Fill the ``consumerID`` field with the ID from the ``resourcesystem`` provider ID. Enter the ``interfaceID``, ``providerID``, and ``serviceDefinitionID`` of the ``query-interface`` service system definition of the ``policyserver`` provider that we saved from earlier
7) You should now be able to run the consumer. Start by running the sensor, then instantiate a new consumer and execute your queries!


