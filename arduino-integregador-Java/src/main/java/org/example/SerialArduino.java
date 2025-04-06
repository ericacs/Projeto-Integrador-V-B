package org.example;

import com.fazecast.jSerialComm.SerialPort;

import java.io.Closeable;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SerialArduino implements Closeable {
    private final SerialPort comPort;

    public SerialArduino(String  port) {
        this.comPort = SerialPort.getCommPort(port);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

    }

    @Override
    public void close() {
        comPort.closePort();
    }

    public void processaDados(){
        System.out.println("Aplicação iniciado na porta " + comPort.getSystemPortName());
        InputStream in = comPort.getInputStream();
        try
        {
            StringBuilder linhaAtual = new StringBuilder();

            int dado;
            while ((dado = in.read()) != -1) {
                char caractere = (char) dado;

                // Verifica se atingiu o fim da linha
                if (caractere == '\n') {
                    String linhaCompleta = linhaAtual.toString().trim();
                    System.out.printf("Aplicação - Linha de dados recebidos: %s\n", linhaCompleta);

                    // Verifica se a linha possui exatamente 3 valores separados por "|"
                    String[] valores = linhaCompleta.split("\\|");
                    double temperatura = Double.parseDouble(valores[0]);
                    double umidade = Double.parseDouble(valores[1]);
                    double luminosidade = Double.parseDouble(valores[2]);

                    if(temperatura < 18 || temperatura > 27){
                        System.out.println("Aplicação salvando dados quando temperatura seja menor 18 e maior que 27 ");
                        salvarDados(temperatura, umidade, luminosidade);
                    }
                    linhaAtual.setLength(0);

                } else {
                    // Adiciona os caracteres à linha atual
                    linhaAtual.append(caractere);
                }
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void salvarDados(double temperatura, double umidade , double luminosidade){
        String url = "jdbc:sqlite:sensores.db"; // Banco SQLite local

        try (Connection conexao = DriverManager.getConnection(url)) {
            // Criar a tabela se não existir
            String criarTabela = "CREATE TABLE IF NOT EXISTS sensores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "temperatura REAL, " +
                    "umidade REAL, " +
                    "luminosidade REAL, " +
                    "data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            conexao.createStatement().execute(criarTabela);

            // Inserir os dados na tabela
            String sql = "INSERT INTO sensores (temperatura, umidade, luminosidade) VALUES (?, ?, ?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setDouble(1, temperatura);
            stmt.setDouble(2, umidade);
            stmt.setDouble(3, luminosidade);

            stmt.executeUpdate();
            System.out.println("Aplicação - Dados salvos no banco SQLite: " + temperatura + ", " + umidade + ", " + luminosidade);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

