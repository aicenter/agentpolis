//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.15 at 11:39:45 AM CEST 
//


package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.environment.roadnet.generated;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for nodeTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="nodeTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="traffic_light"/>
 *     &lt;enumeration value="right_before_left"/>
 *     &lt;enumeration value="priority"/>
 *     &lt;enumeration value="dead_end"/>
 *     &lt;enumeration value="unregulated"/>
 *     &lt;enumeration value="traffic_light_unregulated"/>
 *     &lt;enumeration value="rail_signal"/>
 *     &lt;enumeration value="allway_stop"/>
 *     &lt;enumeration value="priority_stop"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "nodeTypeType")
@XmlEnum
public enum NodeTypeType {

    @XmlEnumValue("traffic_light")
    TRAFFIC_LIGHT("traffic_light"),
    @XmlEnumValue("right_before_left")
    RIGHT_BEFORE_LEFT("right_before_left"),
    @XmlEnumValue("priority")
    PRIORITY("priority"),
    @XmlEnumValue("dead_end")
    DEAD_END("dead_end"),
    @XmlEnumValue("unregulated")
    UNREGULATED("unregulated"),
    @XmlEnumValue("traffic_light_unregulated")
    TRAFFIC_LIGHT_UNREGULATED("traffic_light_unregulated"),
    @XmlEnumValue("rail_signal")
    RAIL_SIGNAL("rail_signal"),
    @XmlEnumValue("allway_stop")
    ALLWAY_STOP("allway_stop"),
    @XmlEnumValue("priority_stop")
    PRIORITY_STOP("priority_stop");
    private final String value;

    NodeTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NodeTypeType fromValue(String v) {
        for (NodeTypeType c: NodeTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
