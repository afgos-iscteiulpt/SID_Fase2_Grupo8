package core;

import java.net.UnknownHostException;

import org.eclipse.paho.client.mqttv3.MqttException;

import sensor.*;

public class BrokerToMongo_Main {

	public static void main(String[] args) {
		try {
			MqttListener.main(null);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
