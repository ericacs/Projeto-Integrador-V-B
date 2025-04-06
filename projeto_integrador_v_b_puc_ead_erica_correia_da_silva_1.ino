// Definindo os pinos
int sensorTempPin = A0;  // TMP36 Sensor (Temperatura)
int umidadePin = A1;     // Potenciometro (Umidade)
int luminosidadePin = A2; // Potenciometro (Luminosidade)

int ventiladorPin = 9;   // Pino de controle do ventilador
int lampadaPin = 10;     // Pino de controle da lampada
int ledUmidadePin = 11;  // Pino de controle do LED de umidade

// Variaveis para armazenar os valores lidos dos sensores
int sensorTempValue = 0;
int umidadeValue = 0;
int luminosidadeValue = 0;

// Variáveis para armazenar os valores em percentual
float umidadePercentual = 0;
float luminosidadePercentual = 0;

void setup() {
  // Inicializando os pinos de controle como saida
  pinMode(ventiladorPin, OUTPUT);
  pinMode(lampadaPin, OUTPUT);
  pinMode(ledUmidadePin, OUTPUT);

  // Inicializando o monitor serial
  Serial.begin(9600);
}

void loop() {
  // Lendo os valores dos sensores
  sensorTempValue = analogRead(sensorTempPin);  // Leitura do TMP36 (Temperatura)

  // Convertendo a leitura analogica para tensao (em volts)
  float voltage = sensorTempValue * (5.0 / 1023.0);
  
  // A formula para converter para temperatura (em Celsius) pode ser:
  float temperatureC = (voltage - 0.5) * 100;  // Converte para temperatura em Celsius
  
  umidadeValue = analogRead(umidadePin);        // Leitura do Potenciometro (Umidade)
  luminosidadeValue = analogRead(luminosidadePin); // Leitura do Potenciometro (Luminosidade)

  // Convertendo os valores lidos para percentual (0 a 100%)
  umidadePercentual = (umidadeValue / 1023.0) * 100; // Conversão para percentual
  luminosidadePercentual = (luminosidadeValue / 1023.0) * 100; // Conversão para percentual

  // Exibindo os valores no monitor serial
  /*
  Serial.print("Temperatura (TMP36): ");
  Serial.println("Temperatura:"+temperatureC);   // Leitura do TMP36
  Serial.print("\tUmidade (Potenciometro): ");
  Serial.println("Umidade"+umidadePercentual);      // Leitura do Potenciometro (Umidade) em percentual
  Serial.print("\tLuminosidade (Potenciometro): ");
  Serial.println("Luminosidade"+luminosidadePercentual); // Leitura do Potenciometro (Luminosidade) em percentual
  */
  
  //Enviando para porta serial em formato tabular ex: temp|umid|lumn 
  Serial.print(temperatureC);  
  Serial.print("|");
  Serial.print(+umidadePercentual);
  Serial.print("|");
  Serial.println(luminosidadePercentual);
  
  // Imprimir o valor de luminosidade para depuracao
  //Serial.print("Valor de luminosidade (bruto): ");
  //Serial.println(luminosidadePercentual);

  // Controle do ventilador (Motor CC) baseado na temperatura
  if (temperatureC >= 23) {
    digitalWrite(ventiladorPin, HIGH);  // Liga o ventilador (motor gira)
    //Serial.println("Ventilador ligado"); // Exibe no monitor serial
  } else {
    digitalWrite(ventiladorPin, LOW);   // Desliga o ventilador (motor para)
    //Serial.println("Ventilador desligado"); // Exibe no monitor serial
  }

  // Controle da lampada (Luminosidade)
  // A lampada deve acender quando a luminosidade for baixa (<50%)
  if (luminosidadePercentual < 50) {  // Ajuste este valor conforme necessário
    digitalWrite(lampadaPin, HIGH);  // Liga a lampada
    //Serial.println("Lampada ligada"); // Exibe no monitor serial
  } else {
    digitalWrite(lampadaPin, LOW);   // Desliga a lampada
    //Serial.println("Lampada desligada"); // Exibe no monitor serial
  }

  // Controle de umidade (LED de umidade)
  if (umidadePercentual >= 50) {  // Ajuste o limite de umidade conforme necessário
    digitalWrite(ledUmidadePin, HIGH);  // Acende o LED para indicar alta umidade
    //Serial.println("Led ligado"); // Exibe no monitor serial
  } else {
    digitalWrite(ledUmidadePin, LOW);   // Desliga o LED para indicar baixa umidade
    //Serial.println("Led desligado"); // Exibe no monitor serial
  }
  // Atraso de 3 segundo antes da próxima leitura
  delay(3000);
}
