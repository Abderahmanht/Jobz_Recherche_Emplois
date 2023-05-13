package com.example.pfe_jobz_recherche_emploi;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ___ConnectionClass {
    @SuppressLint("NewApi")
    public Connection SQLServerConnection() {
        Connection con = null;

        String ip = "192.168.129.47";
        int port = 1433;
        String username = "abderahman";
        String password = "admin";
        String databasename = "APM_Recherche_Emploi";

        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = String.format("jdbc:jtds:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s",ip,port, databasename, username, password);

            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return con;

    }
}
