# Kafka Setup

In development environments, you can use kafka with most OOB defaults.

## Installation (macOS - Big Sur)

``` 
# this will install java, zookeeper, and kafka
brew install kafka

# Starting ZK and Kafka as services

brew services start zookeeper
brew services start kafka

# Stopping

brew services stop kafka
brew services stop zookeeper

```

Or use the following to start Kafka in the foreground.

```
zookeeper-server-start -daemon /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties
```

After installing Kafka via Homebrew, following helpful info is output:

``` 
 ==> zookeeper
To have launchd start zookeeper now and restart at login:
  brew services start zookeeper
Or, if you don't want/need a background service you can just run:
  zkServer start
==> kafka
To have launchd start kafka now and restart at login:
  brew services start kafka
Or, if you don't want/need a background service you can just run:
  zookeeper-server-start -daemon /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties
```