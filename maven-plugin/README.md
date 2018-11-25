# Serverless Maven Plugin

A maven plugin for making it (a little) easier to use the [serverless](https://serverless.com) framework with java. 
The plugin currently:
* automatically generates the serverless.yml file based on introspection of project code
* provides wrapper mvn goals for serverless commands

Future functionality could be to 
* support more providers
* provide the possibility to verify deployed functions
* provide an abstraction layer that enables the exact same java code to work on all providers
* <whatever you come up with!>

## Installation

* Install the serverless framework as described at [Getting started with serverless](https://serverless.com/framework/docs/getting-started/)
* Add the plugin to your pom under the build/plugins section:
```
<plugin>
    <groupId>io.nanoservices</groupId>
    <artifactId>serverless-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <provider>aws</provider>
    </configuration>
    <executions>
        <execution>
            <phase>process-classes</phase>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
The plugin currently supports aws and openwhisk providers.

Until there is a non-SNAPSHOT version of the plugin you'll also need to add the sonatype snapshot repository
to your list of repositories:

```
<pluginRepositories>
    <pluginRepository>
        <id>sonatype-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </pluginRepository>
</pluginRepositories>
```

## Usage

Once configured as above you can create serverless functions/handlers in accordance with your target providers 
 platform and build your project; the plugin will generate a serverless.yml file for your selected provider into the 
 target/serverless folder during your build. Provided you've configured serverless for your provider you can use 
 
```
mvn serverless:deploy
```

to deploy the generated artifact to your target platform (this simply runs the corresponding serverless deploy 
for the generated serverless.yml).

If you want to invoke one of the deployed functions you can use

```
mvn serverless:invoke -Dfunction=<functionName>
```
 
Depending on which provider you've specified in the plugin configuration, handlers are added to serverless.yml 
as described below.

### AWS

When targeting AWS use the string "aws" for the provider; the plugin will look for 

* all classes implementing the AWS RequestHandler / RequestStreamHandler interfaces
* all methods annotated with the [Function](../annotations/src/main/java/io/nanoservices/serverless/annotations/Function.java) annotation

and add these to the generated serverless.yml file accordingly.

See the [AWS Sample](../maven-plugin-sample/src/main/java/io/nanoservices/samples/aws) package and containing 
[Plugin Sample project](../maven-plugin-sample/README.md) for a concrete example.

### OpenWhisk

When targeting OpenWhisk use the string "openwhisk" for the provider; the plugin will extract 

* all classes conforming to the [OpenWhisk requirement](https://console.bluemix.net/docs/openwhisk/openwhisk_actions.html#creating-java-actions)

and add them as handlers to the generated serverless.yml file. Use the 
[Function]((../annotations/src/main/java/io/nanoservices/serverless/annotations/Function.java)) annotation to name the handler 
(otherwise it will be named with the class name).   

See the [OpenWhisk Sample](../maven-plugin-sample/src/main/java/io/nanoservices/samples/openwhisk) pacakge and containing 
[Plugin Sample project](../maven-plugin-sample/README.md) for a concrete example.

If you want to specify the OW_APIHOST and OW_AUTH values directly in the pom you can do so using the providerParams
configuration element:

```
<plugin>
    <groupId>io.nanoservices</groupId>
    <artifactId>serverless-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <provider>openwhisk</provider>
        <providerConfig>
           <ApiHost>...</ApiHost>
           <Auth>...</Auth>
        </providerConfig>
    </configuration>
    <executions>
        <execution>
            <phase>process-classes</phase>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
 