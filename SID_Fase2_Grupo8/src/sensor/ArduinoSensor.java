package sensor;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoSensor {
	
	private static MqttClient sampleClient;
	private static String topic = "SID2018-G8";
	private static int qos = 0;
	private static String broker = "tcp://iot.eclipse.org:1883";
	private static String clientId = "Sensor1";

	public static void main(String[] args) {


		MemoryPersistence persistence = new MemoryPersistence();

		try {
			sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}

		SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.openPort();
		try {
			while (true) {
				while (comPort.bytesAvailable() == 0)
					Thread.sleep(20);
				byte[] readBuffer = new byte[comPort.bytesAvailable()];
				comPort.readBytes(readBuffer, readBuffer.length);
				String str = new String(readBuffer, StandardCharsets.UTF_8);
				publish(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		comPort.closePort();
	}

	@SuppressWarnings("deprecation")
	private static void publish(String str) throws MqttPersistenceException, MqttException {
		String[] array = str.split(" "); // 0 - humidity , 1 - temperature
		Date date = new Date();
		String time = String.valueOf(date.getHours());
		time+= ":";
		time+= String.valueOf(date.getMinutes());
		time+=":";
		time+= String.valueOf(date.getSeconds());
		String k = array[1];
		String[] aa = k.split("\r");
		String jsonString = new JSONObject().put("temperature", aa[0]).put("humidity", array[0]).put("time", time).toString();
		MqttMessage message = new MqttMessage(jsonString.getBytes());
		message.setQos(qos);
		sampleClient.publish(topic, message);
	}
}
