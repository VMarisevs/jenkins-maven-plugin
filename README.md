# Quick Overview
The Jenkins Maven Plugin allows to use the Jenkins CLI (command line interface) from within a Maven build. It allows the execution of any command supported by the CLI, see https://wiki.jenkins-ci.org/display/JENKINS/Jenkins+CLI for further information. Therefore, it downloads the current jenkins-cli.jar from the url specified and uses that artifact for interacting with the Jenkins server.

## What's new

### 1.2.0
- Added possibility to specify a stdin/stdout XSLT through its Maven coordinates (see "stdinXsltCoords" and "stdoutXsltCoords")
- Renamed configuration property name from "stdinXslt" to "stdinXsltFile"
- Renamed configuration property name from "stdoutXslt" to "stdoutXsltFile"

### 1.1.0
- Built-in XSLT transformation for stdin/stdout

### 1.0.2
- Changed configuration property name from "jenkins.baseURL" to "jenkins.baseUrl"
- Improved configuration validation
- Improved exception translations
- Improved integration tests

### 1.0.1
- Added missing information to pom.xml to pass OSSRH quality checks.

### 1.0.0 (not in Maven Central)
- First public release


## Installation
The plugin is available in [Maven Central Repository](http://search.maven.org/#artifactdetails|ch.sourcepond.maven.plugins|jenkins-maven-plugin|1.0.2|maven-plugin). To use it in your project, add following plugin definition to the build part of your pom.xml:
```
<plugin>
	<groupId>ch.sourcepond.maven.plugins</groupId>
	<artifactId>jenkins-maven-plugin</artifactId>
	<version>1.2.0</version>
</plugin>
```

## Configuration reference
The table below gives an overview about the parameters which can be specified. 

| Configuration element | Description |
| ---------------------- | ----------- |
| **jenkinscliDirectory** *(required)* | Specifies where the downloaded jenkins-cli.jar should be stored. Defaults to *${user.home}/.m2/jenkinscli* |
| **baseUrl** *(required)* | Specifies the URL where the Jenkins instance used by the plugin is available. Defaults to *${project.ciManagement.url}* |
| **cliJar** *(required)* | Specifies the relative path to baseUrl where the CLI-jar (necessary to run the plugin) can be downloaded. Defaults to *jnlpJars/jenkins-cli.jar* |
| **command** *(required)* | Specifies the Jenkins command including all its options and arguments to be executed through the CLI. |
| **customJenkinsCliJar** | Specifies a custom jenkins-cli.jar to be used by this plugin. If set, downloading jenkins-cli.jar from the Jenkins instance specified with *baseUrl* will completely be bypassed. |
| **stdin** | Specifies the file from where the standard input should read from. If set, the command receives the file data through stdin (for instance useful for "create job"). If not set, stdin does not provide any data. |
| **stdinXsltCoords** | Specifies the Maven coordinates of the XSTL file to be applied on the file specified by *stdin* before it's actually passed to the CLI command. The coordinate must have following form: *groupId:artifactId[:extension[:classifier]]:version* . This is useful for instance to transform a template job configuration into an actual one. The plugin will fail if the XSLT can not be resolved in any Maven repository. If *stdin* is not specified, this setting has no effect. Note: this setting conflicts with *stdinXsltFile*, so use only one of them. |
| **stdinXsltFile** | Specifies the XSTL file to be applied on the file specified by *stdin* before it's actually passed to the CLI command. This is useful for instance to transform a template job configuration into an actual one. If *stdin* is not specified, this setting has no effect. |
| **stdinXsltParams** | Specifies custom parameters which will be passed to the XSLT specified through *stdinXslt*. This configuration item is a map, see examples for how to use it. If *stdinXslt* is not specified, this settings has no effect. |
| **stdout** | Specifies the file where the standard output of the CLI should be written to. If set, the command sends the data received through stdout to the file specified (useful for example if the output of a command like "list-jobs" should be further processed). If not set, stdout is only written to the log. Note: if *append* is set to false (default) the target file will be replaced. |
| **stdoutXsltCoords** | Specifies the Maven coordinates of the XSTL file to be applied on the file specified by *stdout* before it's actually written. The coordinate must have following form: *groupId:artifactId[:extension[:classifier]]:version* . This is useful for instance to transform a template job configuration into an actual one. The plugin will fail if the XSLT can not be resolved in any Maven repository. If *stdout* is not specified, this setting has no effect. Note: this setting conflicts with *stdoutXsltFile*, so use only one of them. |
| **stdoutXsltFile** | Specifies the XSTL file to be applied on the file specified by *stdout* before it's actually written. This is useful for instance to transform a template job configuration into an actual one. If *stdout* is not specified, this setting has no effect. |
| **stdoutXsltParams** | Specifies custom parameters which will be passed to the XSLT specified through *stdoutXslt*. This configuration item is a map, see examples for how to use it. If *stdoutXslt* is not specified, this settings has no effect. |
| **append** | Specifies whether the target file defined by *stdout* should be replaced if existing. If set to true and the target file exists, all data will be appended to the existing file. If *stdout* is not set, this property has no effect. Defaults to *false* (overwrite file). |
| **proxyId** | Specifies the settings-id of the proxy-server which the CLI should use to connect to the Jenkins instance. This parameter will be passed as "-p" option to the CLI. If set, the plugin will search for the appropriate proxy-server in the Maven settings (usually ~/.m2/settings.xml, see https://maven.apache.org/guides/mini/guide-proxies.html) |
| **noKeyAuth** | Specifies, whether the CLI should skip loading the SSH authentication private key. This parameter will be passed as "-noKeyAuth" option to the CLI. Note: if set true, this setting conflicts with privateKey if privateKey is specified. Defaults to *false* |
| **privateKey** | Specifies the SSH authentication private key to be used when connecting to Jenkins. This parameter will be passed as "-i" option to the CLI. If not specified, the CLI will look for ~/.ssh/identity, ~/.ssh/id_dsa, ~/.ssh/id_rsa and those to authenticate itself against the server. Note: this setting conflicts with noKeyAuth if noKeyAuth is set true |
| **noCertificateCheck** | Specifies, whether certificate check should completely be disabled when the CLI connects to an SSL secured Jenkins instance. This parameter will be passed as "-noCertificateCheck" option to the CLI. This setting will bypass trustStore and trustStorePassword. Note: avoid enabling this switch because it's not secure (the CLI will trust everyone). Defaults to *false* |
| **trustStore** | Specifies the trust-store to be used by the CLI if it should connect to an SSL secured Jenkins instance. This parameter will be passed as "-Djavax.net.ssl.trustStore" option to the JVM which runs the CLI. If specified, a password must be set with configuration element *trustStorePassword*. |
| **trustStorePassword** | Specifies the password for the trust-store to be used by the CLI trustStore. This parameter will be passed as "-Djavax.net.ssl.trustStorePassword" option to the JVM which runs the CLI. According to keytool the password must be at least 6 characters. |

## Properties
The configuration described above can also be done through properties. Properties can be defined within the pom.xml, settings.xml, or, can be passed as command line arguments. Following table shows which configuration element corresponds to which property:

| Configuration element | Property |
| ---------------------- | ----------- |
| **jenkinscliDirectory** | jenkins.cliDirectory |
| **baseUrl** | jenkins.baseUrl |
| **cliJar** | jenkins.cliJar |
| **command** | jenkins.command |
| **customJenkinsCliJar** | jenkins.customCliJar |
| **stdin** | jenkins.stdin |
| **stdout** | jenkins.stdout |
| **append** | jenkins.append |
| **proxyId** | jenkins.proxyId |
| **noKeyAuth** | jenkins.noKeyAuth |
| **privateKey** | jenkins.privateKey |
| **noCertificateCheck** | jenkins.noCertificateCheck |
| **trustStore** | jenkins.trustStore |
| **trustStorePassword** | jenkins.trustStorePassword |

## Examples
You can find the full source code of the examples below in the *examples* directory.

### Show help for create-job (examples/execute-from-cmd)
To show the help message for Jenkins command "create-job" from the command line add following snipped to your pom.xml:
```
<plugin>
	<groupId>ch.sourcepond.maven.plugins</groupId>
	<artifactId>jenkins-maven-plugin</artifactId>
	<executions>
		<execution>
			<!-- Special Maven executionId which allows to run the plugin from the 
				command line without defining a lifecycle phase. -->
			<id>default-cli</id>
		</execution>
	</executions>
</plugin>
```
And execute following command in your project folder (where the pom.xml is located):
```
mvn jenkins:cli -Djenkins.command="help create-job"
```
You can also define your Jenkins baseURL like this:
```
mvn jenkins:cli -Djenkins.baseUrl=http://[HOST]:[PORT]/[JENKINS PATH] -Djenkins.command=[CMD]
```

### Create a new job on Jenkins (examples/create-job)
```
<plugin>
	<groupId>ch.sourcepond.maven.plugins</groupId>
	<artifactId>jenkins-maven-plugin</artifactId>
	<executions>
		<execution>
			<id>create-job</id>
			<goals>
				<goal>cli</goal>
			</goals>
			<configuration>
				<!-- Create a Jenkins job and specify the artifactId of this project 
					as name -->
				<command>create-job ${project.artifactId}</command>

				<!-- Let stdin read from the job configuration template file -->
				<stdin>${configTemplate}</stdin>

				<!-- Specify the XSLT file to be used to transform the data read from 
					stdin -->
				<stdinXsltFile>${xslt}</stdinXsltFile>

				<!-- If your XSLT is available from a Maven repository it's better to 
					use its coordinates to let the plugin download the XSLT file for you instead 
					of specifying a custom file with "stdinXsltFile". Note: you cannot specify 
					both ("stdinXsltFile" and "stdinXsltCoords") -->
				<!-- <stdinXsltCoords>xslt.groupid:xslt.artifactid:1.0.0</stdinXsltCoords> -->

				<!-- Specify the parameters which are used by the XSLT -->
				<stdinXsltParams>
					<description>${project.description}</description>
					<giturl>${project.scm.connection}</giturl>
					<credentialsId>${credentialsId}</credentialsId>
					<groupId>${project.groupId}</groupId>
					<artifactId>${project.artifactId}</artifactId>
				</stdinXsltParams>
			</configuration>
		</execution>
	</executions>
</plugin>
```