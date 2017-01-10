package com.example.user.lab56;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static android.content.Context.ALARM_SERVICE;

class XmlHelper {

    private static File userData;

    @NonNull
    private static File getUserData() throws IOException {
        File folderPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + "lab2" + "/");
        boolean mkdirs = folderPath.mkdirs();
        userData = new File(folderPath, "userData.xml");

        if (!userData.exists()) {
            userData.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(userData);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "eventData");

            xmlSerializer.endTag(null, "eventData");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileOutputStream.write(dataWrite.getBytes());
            fileOutputStream.close();
        }
        return userData;
    }

    static void add(Context context, long time, String message) throws TransformerException, IOException, SAXException, ParserConfigurationException {
        if (userData == null) {
            userData = getUserData();
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(userData);
        Element root = document.getDocumentElement();

        int eventId = root.getChildNodes().getLength() + 1;
        Element event = document.createElement("id" + eventId);
        Element time1 = document.createElement("time");
        time1.appendChild(document.createTextNode(String.valueOf(time)));
        event.appendChild(time1);

        Element message1 = document.createElement("message");
        message1.appendChild(document.createTextNode(message));
        event.appendChild(message1);

        root.appendChild(event);

        saveUserData(userData, document);
        setAlarm(context, time, message, eventId);
    }

    static void update(String id, long time, String message) {
        try {
            if (userData == null) {
                userData = getUserData();
            }
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(userData);

            Node event = document.getElementsByTagName(id).item(0);
            Node nodeTime = event.getFirstChild().getFirstChild();
            nodeTime.setNodeValue(String.valueOf(time));

            Node nodeMessage = document.getElementsByTagName(id).item(0);
            Node firstChild1 = nodeMessage.getLastChild().getFirstChild();
            firstChild1.setNodeValue(message);

            saveUserData(userData, document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveUserData(File userData, Document document) throws TransformerException {
        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(userData);
        transformer.transform(source, result);
    }

    private static void setAlarm(Context context, long time, String message, int id) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("time", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
