import org.eclipse.paho.client.mqttv3.MqttException;

public class Animation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Default settings:
				boolean quietMode 	= false;
				String action 		= "publish";
				String topic1 		= "Player2Connected";
				String topic2 		= "Player2MoveDone";
				String message 		= "Message from async callback Paho MQTTv3 Java client sample";
				int qos 			= 2;
				String broker 		= "m16.cloudmqtt.com";
				int port 			= 15723;
				String clientId 	= "";
				String subTopic1		= "Player1MoveDone";
				String subTopic2		= "Player2MoveDone";
				String pubTopic 	= "Sample/Java/v3";
				boolean cleanSession = true;			// Non durable subscriptions
				boolean ssl = false;
				String password = "6GnU5tguyfIb";
				String userName = "hmlanxtm";
				//mqtt.connect("m16.cloudmqtt.com", 15723, "Ting",  "queens");

				String protocol = "tcp://";

//		    if (ssl) {
//		      protocol = "ssl://";
//		    }

				String url = protocol + broker + ":" + port;

//				if (clientId == null || clientId.equals("")) {
//					clientId = "SampleJavaV3_"+action;
//				}

				// With a valid set of arguments, the real work of
				// driving the client API can begin
				try {
					// Create an instance of the Sample client wrapper
					SampleAsyncCallBack sampleClient = new SampleAsyncCallBack(url,"",cleanSession, quietMode,userName,password);

					// Perform the specified action
//					if (action.equals("publish")) {
//						sampleClient.publish(topic,qos,message.getBytes());
//					} else if (action.equals("subscribe")) {
//						sampleClient.subscribe(topic,qos);
//					}
					sampleClient.subscribe(topic1,qos);
					
				} catch(MqttException me) {
					// Display full details of any exception that occurs
					System.out.println("reason "+me.getReasonCode());
					System.out.println("msg "+me.getMessage());
					System.out.println("loc "+me.getLocalizedMessage());
					System.out.println("cause "+me.getCause());
					System.out.println("excep "+me);
					me.printStackTrace();
				} catch (Throwable th) {
					System.out.println("Throwable caught "+th);
					th.printStackTrace();
				}
	}

}
