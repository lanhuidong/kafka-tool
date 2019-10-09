package com.nexusy.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.TopicPartition;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author lanhuidong
 * @since 2019-10-08
 */
public class App {

    private static String bootstrapServer = "127.0.0.1:9092";

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--bootstrap-server=")) {
                bootstrapServer = arg.split("=")[1];
            }
        }
        listGroupIdsByTopics(args);
    }

    private static void listGroupIdsByTopics(String[] args) {
        List<String> topicList = new ArrayList<>();
        for (String arg : args) {
            if (arg.startsWith("--topics=")) {
                String topics = arg.split("=")[1];
                String[] xx = topics.split(",");
                topicList.addAll(Arrays.asList(xx));
            }
        }
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        Set<TopicAndGroupId> set = new HashSet<>();
        try (AdminClient client = AdminClient.create(props)) {
            List<String> groupIds = client.listConsumerGroups()
                .all()
                .get()
                .stream()
                .map(ConsumerGroupListing::groupId)
                .collect(Collectors.toList());
            Map<String, ConsumerGroupDescription> descMap = client.describeConsumerGroups(groupIds)
                .all()
                .get();
            for (ConsumerGroupDescription value : descMap.values()) {
                for (MemberDescription member : value.members()) {
                    for (TopicPartition topicPartition : member.assignment().topicPartitions()) {
                        if (topicList.contains(topicPartition.topic())) {
                            TopicAndGroupId topicAndGroupId = new TopicAndGroupId();
                            topicAndGroupId.setGroupId(value.groupId());
                            topicAndGroupId.setTopic(topicPartition.topic());
                            set.add(topicAndGroupId);
                        }
                    }
                }
            }

            List<TopicAndGroupId> list = new ArrayList<>(set);
            list.sort(Comparator.comparing(TopicAndGroupId::getTopic));
            for (TopicAndGroupId topicAndGroupId : list) {
                System.out.println("topic: " + topicAndGroupId.getTopic() + ", groupId: " + topicAndGroupId.getGroupId());
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

}
