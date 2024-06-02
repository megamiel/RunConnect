package com.example.runconnect.library;

public class Pointer <E>{
    private E e;
    public Pointer(E e){
        set(e);
    }

    public void set(E e){
        this.e=e;
    }

    public E get(){
        return e;
    }
}
