package com.agoda.camelon.journaler;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.Properties;

public class KafkaJournaler implements CameleonJournaler {

    private final String bootstrapServers = "localhost:9092";
    private final String topic = "auto-audit-log";
    private final Producer<String, String> producer;

    public KafkaJournaler() {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(props);
    }

    @Override
    public void onBegin(String methodName, String sql, String[] parameters) {

    }

    @Override
    public void onSuccess(String methodName, int updatedRows, String sql, String[] parameters) {
        sendAuditLogMessage("Success");
    }

    @Override
    public void onFailure(String methodName, Exception ex, String sql, String[] parameters) {
        sendAuditLogMessage("Failure");
    }

    @Override
    public void onCommit(String sql) {
        sendAuditLogMessage("Commit");
    }

    @Override
    public void onRollback(String sql) {
        sendAuditLogMessage("Rollback");
    }

    public void sendAuditLogMessage(String status)
    {
        AutoAuditLogMessage autoAuditLogMessage = new AutoAuditLogMessage(
                "123",
                "456",
                "UpdateAvailablity",
                "availability",
                new ArrayList<String>(),
                status); //need to remove those hard coded
        try {
            String autoAuditLogMessageJson = autoAuditLogMessage.toJson();
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, autoAuditLogMessage.getCorrelationId(),
                    autoAuditLogMessageJson);
            System.out.println("Sending " + record.value());
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Sent order successfully to topic: " + metadata.topic() +
                            " | Partition: " + metadata.partition() + " | Offset: " + metadata.offset());
                } else {
                    exception.printStackTrace();
                }
            });
            System.out.println("Send is done " + record.value());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

