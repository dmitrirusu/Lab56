package com.example.user.lab56;

import android.os.Environment;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by dima on 10/8/16.
 */

public class MyXml {

    public static ArrayList<Event> getFromXml() throws IOException, SAXException, ParserConfigurationException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "lab2" + "/");
        file.mkdirs();
        File file1 = new File(file, "userData.xml");
        if (!file1.exists()) {
            file1.createNewFile();
            FileOutputStream fileos = new FileOutputStream(file1);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "eventData");

            xmlSerializer.endTag(null, "eventData");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        }


        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file1);
        Element root = document.getDocumentElement();

        ArrayList<Event> events = new ArrayList<>();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            String message = childNodes.item(i).getLastChild().getFirstChild().getNodeValue();
            long time = Long.parseLong(childNodes.item(i).getFirstChild().getFirstChild().getNodeValue());
            String id = childNodes.item(i).getNodeName();
            events.add(new Event(time, message, id));
        }

        return events;
    }

    static class Event {
        long time;
        String message;
        String id;

        Event(long time, String message, String id) {
            this.time = time;
            this.message = message;
            this.id = id;
        }
    }
}
