package com.example.pfe_jobz_recherche_emploi;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ___ConnectionClass {

    @SuppressLint("NewApi")
    public Connection connectionClass() {
        Connection con = null;

        String ipv4Address = getIPv4Address();
        String ip = "192.168.110.47", port = "1433", username = "abderahman", password = "admin", databasename = "APM_Recherche_Emploi";

        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + databasename + ";User=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }

        return con;
    }

    private String getIPv4Address() {
        String ipConfigOutput = runIpConfigCommand();
        String ipv4Address = extractIPv4Address(ipConfigOutput);
        return ipv4Address;
    }

    private String runIpConfigCommand() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ipconfig");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            return output.toString();
        } catch (IOException exception) {
            Log.e("Error", exception.getMessage());
            return null;
        }
    }

    private String extractIPv4Address(String ipConfigOutput) {
        if (ipConfigOutput == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\bAdresse IPv4\\.\\s*:\\s*(\\d+\\.\\d+\\.\\d+\\.\\d+)\\b");

        Matcher matcher = pattern.matcher(ipConfigOutput);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

}
