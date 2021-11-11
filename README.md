# GoodLoad

GoodLoad is a hackthon project of Rubik team
It demotrates the ideal how to load data to Bear workspace real time
 

## Run Kafka at local

```shell
wget https://dlcdn.apache.org/kafka/3.0.0/kafka_2.13-3.0.0.tgz
tar xvzf kafka_2.13-3.0.0.tgz
mv kafka_2.13-3.0.0 kafka
cd kafka

# Start the ZooKeeper service
# Note: Soon, ZooKeeper will no longer be required by Apache Kafka.
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start the Kafka broker service
bin/kafka-server-start.sh config/server.properties
```

## Create topic

```shell
bin/kafka-topics.sh --create --topic good-load --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

# verify your created topic
bin/kafka-topics.sh --describe --topic good-load --bootstrap-server localhost:9092
```

## Producer to read csv_file, parse to json_data and send to Kafka

```
# install python libs
pip install -r requirements.txt

python3 producer.py
```


Contributor:
* nghia.le
* tu.ngo
* nhi.nguyen


