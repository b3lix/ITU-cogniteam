package com.app.itu;

public class Singleton {
    private static Singleton singleton;
    public String var0;
    public String var1;
    public String var2;

    public String requestBody;
    public String operation;
    public String url;
    public String cookieHeader;
    public String jsonOut;
    public String username;

    private Singleton()
    {
        operation = "";
        requestBody = "{\n" +
            "  \"username\":\"admin\",\n" +
            "  \"password\":\"admin\",\n" +
            "  \"Content-Type\":\"application/json\",\n"+
            "  \"skipCrossSell\":true\n" +
            "}";
        url = "https://travelwise.online:8090";
        cookieHeader = "";
    }

    public void setUrlOperation(String operation)
    {
        this.operation = operation;
        if (url.length() != 30)
        {
            url = url.substring(0, 30);
        }
        url = url + operation;
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
