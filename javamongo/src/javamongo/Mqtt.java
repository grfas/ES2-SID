package javamongo;

import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import mongo.MongoWrite;

public class Mqtt implements MqttCallback {

	MongoWrite wr = new MongoWrite();

	/** The broker url. */
	private static final String brokerUrl = "tcp://broker.mqtt-dashboard.com:1883";

	/** The client id. */
	private static final String clientId = "/sid_lab_2019_2";

	/** The topic. */
	private static final String topic = "/sid_lab_2019_2";

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		new Mqtt().subscribe(topic);
	}

	/**
	 * Subscribe.
	 *
	 * @param topic the topic
	 */
	public void subscribe(String topic) {

		MemoryPersistence persistence = new MemoryPersistence();

		try {

			MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			System.out.println("checking");

			System.out.println("Mqtt Connecting to broker: " + brokerUrl);
			sampleClient.connect(connOpts);
			System.out.println("Mqtt Connected");

			sampleClient.setCallback(this);
			sampleClient.subscribe(topic);

			System.out.println("Subscribed");
			System.out.println("Listening");

		} catch (MqttException me) {

			System.out.println("Mqtt reason " + me.getReasonCode());
			System.out.println("Mqtt msg " + me.getMessage());
			System.out.println("Mqtt loc " + me.getLocalizedMessage());
			System.out.println("Mqtt cause " + me.getCause());
			System.out.println("Mqtt excep " + me);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
	 * Throwable)
	 */
	public void connectionLost(Throwable arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.
	 * paho.client.mqttv3.IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
	 * String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {

		System.out.println("Mqtt topic : " + topic);
		System.out.println("Mqtt msg : " + message.toString());

		String mens = message.toString();
		if (!(mens.toString().contains("?"))) {

			String[] parts = mens.split(Pattern.quote(","));
			if (parts.length == 5) {
				System.out.println(parts.length);
				String a = parts[0];
				String b = parts[1];
				String c = parts[2];
				String d = parts[3];
				String e = parts[4];

				wr.insert(a, b, c, d, e);

			} else {
				
				String cell = " 'cell':0";
				String a = parts[0];
				String b = parts[1];
				String c = parts[2];
				String d = parts[3];
				String e = parts[4];
				
				String concat=cell.concat(e);
				System.out.println(concat);

				wr.insert(a, b, c, d, concat);

			}

		}

	}
}