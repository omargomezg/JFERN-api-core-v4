package com.southpurity.apicore.controller;

public class View {
    public interface Anonymous {}
    public interface Customer extends Anonymous {}
    public interface Stocker extends Customer {}
    public interface Administrator extends Stocker {}
}
