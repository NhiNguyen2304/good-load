#!/usr/bin/env python3
# read csv_data from remote url
# convert csv_data to json_data
import pandas
from kafka import KafkaProducer


def run():
    csv_data = pandas.read_csv('https://raw.githubusercontent.com/tu-ngo/good-load/master/opportunity.csv',  # noqa
                            sep=",", header=0, index_col=False)

    dataFrame = pandas.DataFrame(csv_data)

    json_data = dataFrame.to_json(orient="records", date_format="epoch", double_precision=10,  # noqa
                                force_ascii=True, date_unit="ms", default_handler=None)  # noqa


    # send json_data to Kafka server
    producer = KafkaProducer(bootstrap_servers=['localhost:9092'])

    producer.send('good-load', str(json_data).encode('utf-8'))


if __name__ == '__main__':
    run()
