package Client;

import java.awt.Color;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;


public class HTML {

    public static String bold(String str) {
        return "<strong>" + str + "</strong>";
    }

    public static String italic(String str) {
        return "<em>" + str + "</em>";
    }

    public static String red(String str) {
        return color(str, Color.RED);
    }

    public static String blue(String str) {
        return color(str, new Color(51, 102, 204) );
    }

    private static String color(String str, Color color) {
        return "<span style=\"color: rgb(" + color.getRed() + ","
                + color.getGreen() + "," + color.getBlue()+ ")\">" + str + "</span>";
    }

    public static String small(String type) {
        return "[<small>" + type.toUpperCase() + "</small>] ";
    }

    public static String addHyperlinks(String message) {
        return message.replaceAll("(ftp|https?)://[^\\s<]+", "<a href=\"$0\">$0</a>");
    }

    public static String escapeTags(String message) {
        message = message.replaceAll("<", "&lt;");
        return message.replaceAll(">", "&gt;");
    }

    public static String removeTags(String message) {
        return message.replaceAll("\\<.*?\\>", "");
    }

    // http://stackoverflow.com/a/1264912/557223
    public static String formatXML(String input) {
        try {
            Source xmlInput = new StreamSource( new StringReader(input) );
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 4);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            return null;
        }
    }

}
