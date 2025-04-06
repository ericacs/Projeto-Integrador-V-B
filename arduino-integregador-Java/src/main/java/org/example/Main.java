package org.example;

public class Main {
    public static void main(String[] args) {
        new Simulador().Start();
        try(var ar = new SerialArduino("COM7")){
            ar.processaDados();
        }
    }
}