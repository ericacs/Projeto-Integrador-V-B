package org.example;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.plaf.TableHeaderUI;
import java.util.Random;

public class Simulador {

    public void Start() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                // Definindo a porta serial simulada
                SerialPort portaSerial = SerialPort.getCommPort("COM6"); // Substitua pelo nome da porta
                portaSerial.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

                // Abrindo a porta
                if (portaSerial.openPort()) {
                    System.out.println("Simulador iniciado na porta " + portaSerial.getSystemPortName());

                    try {

                        Random random = new Random();
                        double minimo = 1.0;
                        for (int i = 0; i < 10000; i++) {
                            var temperatura = minimo + (45 - minimo) * random.nextDouble();
                            var umidade = minimo + ( 100 -  - minimo) * random.nextDouble();
                            var luminosidade = minimo + (100 - minimo) * random.nextDouble();

                            try {
                                String dadosParaEnviar = String.format(java.util.Locale.US, "%.2f|%.2f|%.2f\n", temperatura, umidade, luminosidade);
                                portaSerial.getOutputStream().write(dadosParaEnviar.getBytes());
                                portaSerial.getOutputStream().flush();
                                System.out.println("Simulador - Dados enviados: " + dadosParaEnviar);
                            } catch (Exception e) {
                                System.out.println("Simulador - Erro ao enviar dados: " + e.getMessage());
                            }

                            Thread.sleep(2000);
                        }
                    }catch (Exception e) {
                        System.out.println("Simulador - Erro ao enviar dados: " + e.getMessage());
                    } finally {
                        portaSerial.closePort();
                    }


                } else {
                    System.out.println("Simulador - Falha ao abrir a porta serial.");
                }
            }
        });

        thread.start();
    }


}
