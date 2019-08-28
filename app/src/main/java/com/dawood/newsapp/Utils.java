package com.dawood.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utils {
    public static String BaseUrl = BuildConfig.BASE_URL;
    public static String ApiKey = BuildConfig.API_KEY;

    public static void goToBrowser(String url, Context context) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
}
