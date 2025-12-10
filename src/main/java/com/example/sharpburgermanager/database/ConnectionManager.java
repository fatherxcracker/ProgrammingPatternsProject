package com.example.sharpburgermanager.database;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            // Load XML from resources
            InputStream is = ConnectionManager.class.getClassLoader()
                    .getResourceAsStream("dbconfig.xml");
            if (is == null) throw new RuntimeException("dbconfig.xml not found");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();

            URL = doc.getElementsByTagName("url").item(0).getTextContent();
            USER = doc.getElementsByTagName("user").item(0).getTextContent();
            PASSWORD = doc.getElementsByTagName("password").item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}