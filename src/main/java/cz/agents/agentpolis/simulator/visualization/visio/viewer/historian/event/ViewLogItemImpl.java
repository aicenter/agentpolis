package cz.agents.agentpolis.simulator.visualization.visio.viewer.historian.event;

import java.time.ZonedDateTime;

/**
 * Base on AgentC implementation history view.
 * 
 */
public class ViewLogItemImpl implements ViewLogItem {

    private final String source;
    private final String description;
    private final String htmlDescription;
    private final ViewLogItemType type;
    private final ZonedDateTime time;
    private final int currentTimeOfSimInSecond; // reasone for int is
                                                // NewTimeLine

    /**
     * 
     * @param type
     * @param source
     *            Agent name
     * @param description
     * @param htmlDescription
     *            Only tags between &lt;body&gt; &lt;/body&gt;
     * @param time
     */
    public ViewLogItemImpl(ViewLogItemType type, String source, String description,
            String htmlDescription, ZonedDateTime time, int currentTimeOfSimInSecond) {
        this.source = source;
        this.description = description;
        this.htmlDescription = htmlDescription;
        this.type = type;
        this.time = time;
        this.currentTimeOfSimInSecond = currentTimeOfSimInSecond;
    }

    /**
     * Get agent name
     * 
     * @return
     */
    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    public String getHtmlDescription() {
        return htmlDescription;
    }

    public ViewLogItemType getType() {
        return type;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public int getStep() {
        return currentTimeOfSimInSecond;
    }

    @Override
    public String toString() {
        return "HistoryEvent [source=" + source + ", description=" + description
                + ", htmlDescription=" + htmlDescription + ", type=" + type + ", time=" + time
                + ", step=" + currentTimeOfSimInSecond + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((htmlDescription == null) ? 0 : htmlDescription.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + currentTimeOfSimInSecond;
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ViewLogItemImpl other = (ViewLogItemImpl) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (htmlDescription == null) {
            if (other.htmlDescription != null)
                return false;
        } else if (!htmlDescription.equals(other.htmlDescription))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        } else if (!source.equals(other.source))
            return false;
        if (currentTimeOfSimInSecond != other.currentTimeOfSimInSecond)
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}
