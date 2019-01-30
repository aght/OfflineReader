package com.aght.offlinereader.downloader;

import android.util.Log;

import com.aght.offlinereader.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ContentTypeResolver {

    private static String DATA_FILE_NAME = "content_types.txt";
    private static String DATA_SEPARATOR = "~";
    private static ContentTypeResolver singleton;
    private static Map<String, String> contentTypeMap;

    private ContentTypeResolver() {
        contentTypeMap = new HashMap<>();
        getContentTypeData();
    }

    public static ContentTypeResolver getInstance() {
        if (singleton == null) {
            singleton = new ContentTypeResolver();
        }

        return singleton;
    }

    private static void getContentTypeData() {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(App
                            .getContext()
                            .getAssets()
                            .open(DATA_FILE_NAME)));

            String line;
            while ((line = br.readLine()) != null) {
                String tokens[] = line.split(DATA_SEPARATOR);
                contentTypeMap.put(tokens[0].toLowerCase(), tokens[1]);
            }

        } catch (IOException e) {

        }

    }

    public String getExtension(String contentType, String contentDisposition) {

        if (contentDisposition != null) {
            int index = contentDisposition.lastIndexOf(".");
            return sanitize(contentDisposition.substring(index, contentDisposition.length()));
        }

        if (contentType == null) {
            return ".txt";
        }

        int index = contentType.indexOf(";");

        if (index != -1) {
            contentType = contentType.substring(0, index);
        }

        String fileExtension = contentTypeMap.get(contentType.toLowerCase().trim());

        return fileExtension == null ? ".txt" : sanitize(fileExtension);
    }

    private String sanitize(String string) {
        return "." + string.replaceAll("[^a-zA-Z0-9]", "");
    }
}
