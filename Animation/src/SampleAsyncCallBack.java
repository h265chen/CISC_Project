import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

 

public class SampleAsyncCallBack implements MqttCallback {
	int state = BEGIN;

	static final int BEGIN = 0;
	static final int CONNECTED = 1;
	static final int PUBLISHED = 2;
	static final int SUBSCRIBED = 3;
	static final int DISCONNECTED = 4;
	static final int FINISH = 5;
	static final int ERROR = 6;
	static final int DISCONNECT = 7;
	
	// Private instance variables
	MqttAsyncClient 	client;
	String 				brokerUrl;
	private boolean 			quietMode;
	private MqttConnectOptions 	conOpt;
	private boolean 			clean;
	Throwable 			ex = null;
	Object 				waiter = new Object();
	boolean 			donext = false;
	private String password;
	private String userName;

	public SampleAsyncCallBack(String brokerUrl, String clientId, boolean cleanSession,
    		boolean quietMode, String userName, String password) throws MqttException {
		this.brokerUrl = brokerUrl;
    	this.quietMode = quietMode;
    	this.clean = cleanSession;
    	this.password = password;
    	this.userName = userName;
    	String tmpDir = System.getProperty("java.io.tmpdir");
    	MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
    	try {
    		// Construct the object that contains connection parameters
    		// such as cleanSession and LWT
	    	conOpt = new MqttConnectOptions();
	    	conOpt.setCleanSession(clean);
	    	if(password != null ) {
	    		conOpt.setPassword(this.password.toCharArray());
	    	}
	    	if(userName != null) {
	            conOpt.setUserName(this.userName);
	        }
	    	clientId = MqttAsyncClient.generateClientId();
       		// Construct the MqttClient instance
   			client = new MqttAsyncClient(this.brokerUrl, clientId, dataStore);

   			// Set this wrapper as the callback handler
   	    	client.setCallback(this);

	   		} catch (MqttException e) {
	   			e.printStackTrace();
	   			log("Unable to set up client: "+e.toString());
	   			System.exit(1);
	   		}
	}
	/**
     * Subscribe to a topic on an MQTT server
     * Once subscribed this method waits for the messages to arrive from the server
     * that match the subscription. It continues listening for messages until the enter key is
     * pressed.
     * @param topicName to subscribe to (can be wild carded)
     * @param qos the maximum quality of service to receive messages at for this subscription
     * @throws MqttException
     */
    public void subscribe(String topicName, int qos) throws Throwable {
    	// Use a state machine to decide which step to do next. State change occurs
    	// when a notification is received that an MQTT action has completed
    	while (state != FINISH) {
    		switch (state) {
    			case BEGIN:
    				// Connect using a non-blocking connect
    		    	MqttConnector con = new MqttConnector();
    		    	con.doConnect();
    				break;
    			case CONNECTED:
    				// Subscribe using a non-blocking subscribe
    				Subscriber sub = new Subscriber();
    				sub.doSubscribe(topicName, qos);
    				break;
    			case SUBSCRIBED:
    		    	// Block until Enter is pressed allowing messages to arrive
    		    	log("Press <Enter> to exit");
    				try {
    					System.in.read();
    				} catch (IOException e) {
    					//If we can't read we'll just exit
    				}
    				state = DISCONNECT;
    				donext = true;
    				break;
    			case DISCONNECT:
    				Disconnector disc = new Disconnector();
    				disc.doDisconnect();
    				break;
    			case ERROR:
    				throw ex;
    			case DISCONNECTED:
    				state = FINISH;
    				donext = true;
    				break;
    		}

//    		if (state != FINISH && state != DISCONNECT) {
    			waitForStateChange(10000);
    		}
//    	}
    }
    
    /**
     * Wait for a maximum amount of time for a state change event to occur
     * @param maxTTW  maximum time to wait in milliseconds
     * @throws MqttException
     */
	private void waitForStateChange(int maxTTW ) throws MqttException {
		synchronized (waiter) {
    		if (!donext ) {
    			try {
					waiter.wait(maxTTW);
				} catch (InterruptedException e) {
					log("timed out");
					e.printStackTrace();
				}

				if (ex != null) {
					throw (MqttException)ex;
				}
    		}
    		donext = false;
    	}
	}
	
	
	
	void log(String message) {
    	if (!quietMode) {
    		System.out.println(message);
    	}
    }
	
	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		// Called when the connection to the server has been lost.
				// An application may choose to implement reconnection
				// logic at this point. This sample simply exits.
		log("Connection to " + brokerUrl + " lost!" + arg0);
		System.exit(1);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		log("Delivery complete callback: Publish Completed "+Arrays.toString(token.getTopics()));

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		String time = new Timestamp(System.currentTimeMillis()).toString();
		System.out.println("Time:\t" +time +
                           "  Topic:\t" + topic +
                           "  Message:\t" + new String(message.getPayload()) +
                           "  QoS:\t" + message.getQos());
	}
	
	/**
	 * Connect in a non-blocking way and then sit back and wait to be
	 * notified that the action has completed.
	 */
    public class MqttConnector {

		public MqttConnector() {
		}

		public void doConnect() {
	    	// Connect to the server
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the connect completes
	    	log("Connecting to "+brokerUrl + " with client ID "+client.getClientId());

	    	IMqttActionListener conListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
			    	log("Connected");
			    	state = CONNECTED;
			    	carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					ex = exception;
					state = ERROR;
					log ("connect failed" +exception);
					carryOn();
				}

				public void carryOn() {
			    	synchronized (waiter) {
			    		donext=true;
			    		waiter.notifyAll();
			    	}
				}
			};

	    	try {
	    		// Connect using a non-blocking connect
	    		client.connect(conOpt, "Connect sample context", conListener);
			} catch (MqttException e) {
				// If though it is a non-blocking connect an exception can be
				// thrown if validation of parms fails or other checks such
				// as already connected fail.
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
    }
		
	public class Subscriber {
		public void doSubscribe(String topicName, int qos) {
		 	// Make a subscription
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the subscription is in place.
	    	log("Subscribing to topic \""+topicName+"\" qos "+qos);

	    	IMqttActionListener subListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
			    	log("Subscribe Completed");
			    	state = SUBSCRIBED;
			    	carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					ex = exception;
					state = ERROR;
					log ("Subscribe failed" +exception);
					carryOn();
				}

				public void carryOn() {
			    	synchronized (waiter) {
			    		donext=true;
			    		waiter.notifyAll();
			    	}
				}
			};

	    	try {
	    		client.subscribe(topicName, qos, "Subscribe sample context", subListener);
	    	} catch (MqttException e) {
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}
		
	/**
	 * Disconnect in a non-blocking way and then sit back and wait to be
	 * notified that the action has completed.
	 */
	public class Disconnector {
		public void doDisconnect() {
	    	// Disconnect the client
	    	log("Disconnecting");

	    	IMqttActionListener discListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
			    	log("Disconnect Completed");
			    	state = DISCONNECTED;
			    	carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					ex = exception;
					state = ERROR;
					log ("Disconnect failed" +exception);
					carryOn();
				}
				public void carryOn() {
			    	synchronized (waiter) {
			    		donext=true;
			    		waiter.notifyAll();
			    	}
				}
			};

	    	try {
	    		client.disconnect("Disconnect sample context", discListener);
	    	} catch (MqttException e) {
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}

}
