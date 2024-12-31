package org.example.user.utils;

public class myThreadLocal {
    public static final ThreadLocal<Integer> threadLocal=new ThreadLocal<>();

    public static void set(Integer id){
        threadLocal.set(id);
    }

    public static Integer get(){
        return threadLocal.get();
    }
}
