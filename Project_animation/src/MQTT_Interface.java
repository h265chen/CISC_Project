
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttReceivedMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
class MQTT_Interface implements MqttCallback{
	private String broker = "tcp://m16.cloudmqtt.com:15723";
	private String clientId = "Java_animation";
	private MemoryPersistence persistence;
	private MqttClient move_listener_client;
	private MqttConnectOptions connOpts;
	private String message = null;
	
	public MQTT_Interface() {
		establish_connection();
	}
	private void establish_connection() {
		persistence = new MemoryPersistence();
        try {
        	move_listener_client = new MqttClient(broker,clientId,persistence);
        	connOpts = new MqttConnectOptions();
        	connOpts.setCleanSession(true);
        	connOpts.setUserName("Hoyman");
        	connOpts.setPassword("queens".toCharArray());
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);            
            move_listener_client.connect(connOpts);
            move_listener_client.setCallback(this);
            System.out.println("Connected");

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
	}
	public void subscribeToTopic(String topic) {
        try {
			move_listener_client.subscribe(topic);
			System.out.println("Subscribed to topic: "+topic);
			
		} catch (MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
			me.printStackTrace();
		}

        
	}
	public void resetMessage() {
		this.message = null;
	}
	public String getMessage() {		
		return this.message;
	}
	@Override
	public void messageArrived(String topic, MqttMessage message)
	        throws Exception {
			this.message = message.toString();
	}
	@Override
	public void connectionLost(Throwable cause) {
	    // TODO Auto-generated method stub

	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub

	}
	public void disconnect() {
		try {
			move_listener_client.disconnect();
			System.out.println("Disconnected from MQTT Server");
		} catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
		}
	}
}