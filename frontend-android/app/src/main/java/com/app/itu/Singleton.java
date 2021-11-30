package com.app.itu;

public class Singleton {
    private static Singleton singleton;

    private Singleton()
    {
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
