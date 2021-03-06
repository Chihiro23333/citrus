### Changes in Citrus 2.4?!

Citrus 2.4 comes with a set of new features especially regarding Apache Camel and Docker integrations. Bugfixes of course are also part of the package. See the following overview on what has changed.

### Docker support

Docker and Microservices are frequent topics in software development recently. We have added interaction with Docker in Citrus so the user can manage Docker containers within a test case. Citrus now provides special Docker test actions for building, starting, stopping and inspecting Docker images and containers in a test. See [docker](docker) for details.

### Http REST actions

We have significantly improved the Http REST support in Citrus. The focus is on simplifying the Http REST usage in Citrus test cases. With new Http specific test actions on client and server we can send and receive Http REST messages very easy. See [http](http) for details.

### Wait test action

With the new wait test action we can explicitly wait for some remote condition to become true inside of a test case. The conditions supported at the moment are Http url requests and file based conditions. A user can invoke a Http server url and wait for it to return a success **Http 200 OK** response. This is an awesome feature when waiting for a server to start up before the test continues. We can also think of waiting for a Docker container to start up before continuing. Or you can wait until a file is present on the local file system. See[actions-wait](actions-wait)for details.

### Camel actions

Citrus has already had support for Apache Camel routes and Camel context loading. Now with 2.4 version we have added some special Apache Camel test actions for interacting with a Camel context and its routes. This enables the tester to create and use a new Camel route on the fly inside a test case. Also Citrus is now able to interact with the Camel control bus accessing route statistics and status information. Also possible are start, stop, suspend, resume operations on a Camel route. See[camel-actions](camel-actions)and[camel-controlbus](camel-controlbus)for details.

### Purge endpoints action

Purging JMS queues and in memory channels at test runtime has become a widely used feature especially when aiming to make tests more stable in terms of independent tests. We have added a purge endpoint test action that works on any consumer endpoint. So you do not need to separate between endpoint implementations anymore and more important you can purge server in memory channel components very easy. See[actions-purge-endpoints](actions-purge-endpoints)for details.

### Release to Maven Central

This is not a new feature but also worth to tell here as it is a significant improvement on the whole framework project. We can now release the Citrus artifacts to Maven central repository. So you do not need the additional **labs.consol.de** repository in your Maven POM anymore. The **labs.consol.de** repository will continue to exist though as we will release SNAPSHOT versions of Citrus here in future.

