package com.app.itu;

import java.io.Console;

public class Singleton {
    private static Singleton singleton;
    public String var0;
    public String var1;
    public String var2;
    public String requestBody;
    public String url;
    public String cookieHeader;

    private Singleton()
    {
        requestBody = "{\n" +
            "  \"username\":\"admin\",\n" +
            "  \"password\":\"admin\",\n" +
            "  \"Content-Type\":\"application/json\",\n"+
            "  \"skipCrossSell\":true\n" +
            "}";
        url = "https://travelwise.online:8090/auth/login";
    }

    public static Singleton getInstance()
    {
        if (singleton == null)
        {
            singleton = new Singleton();
        }
        return singleton;
    }
}
