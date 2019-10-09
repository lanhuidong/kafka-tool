package com.nexusy.kafka;

import java.util.Objects;

/**
 * @author lanhuidong
 * @since 2019-10-08
 */
public class TopicAndGroupId {

    private String topic;
    private String groupId;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicAndGroupId that = (TopicAndGroupId) o;
        return Objects.equals(topic, that.topic) &&
            Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, groupId);
    }
}
