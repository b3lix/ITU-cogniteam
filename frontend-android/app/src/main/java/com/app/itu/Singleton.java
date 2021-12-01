package com.app.itu;

import java.io.Console;

public class Singleton {
    private static Singleton singleton;
    public String var0;
    public String var1;
    public String var2;

    private Singleton()
    {
        String a = "dasdasads";
        //todo
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
