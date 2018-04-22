package com.example.admin.keeper;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Translator {

    public static String translate(String transDirection, String input) throws IOException {
        String urlStr = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + "trnsl.1.1.20180422T103524Z.09db2bf02270f9df.59fc710d319efd44f8502eb22d8cdf8faf177687";

        URL urlObj = new URL(urlStr);
        HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes("text=" + URLEncoder.encode(input, "UTF-8") +
                "&lang=" + transDirection);

        InputStream response = connection.getInputStream();
        String json = new java.util.Scanner(response).nextLine();
        int start = json.indexOf("[");
        int end = json.indexOf("]");
        String translated = json.substring(start + 2, end - 1);

        return translated;
    }

}
