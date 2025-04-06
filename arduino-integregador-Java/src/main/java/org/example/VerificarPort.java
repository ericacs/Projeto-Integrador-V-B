package org.example;

import com.fazecast.jSerialComm.SerialPort;

public class VerificarPort {
    public static void main(String[] args) {
        SerialPort[] portas = SerialPort.getCommPorts();
        System.out.println("Portas disponíveis:");
        for (SerialPort porta : portas) {
            System.out.println(porta.getSystemPortName());
        }
    }

}
