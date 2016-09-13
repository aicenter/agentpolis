package cz.agents.agentpolis.simulator.visualization.googleearth.utils;

import cz.agents.agentpolis.siminfrastructure.description.DescriptionImpl;

/**
 * 
 * The utilities transforms data from Java data structure into HTML
 * 
 * @author Zbynek Moler
 * 
 */
public class DescriptionGEUtil {

    private static final String NEW_HTML_LINE = "<BR>";

    private DescriptionGEUtil() {
    }

    public static String transformDescriptionToHTML(DescriptionImpl description) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String[] line : description.getDescriptions()) {
            for (String item : line) {
                stringBuffer.append(item);
                stringBuffer.append(' ');
            }
            stringBuffer.append(NEW_HTML_LINE);
        }
        return stringBuffer.toString();
    }

}
