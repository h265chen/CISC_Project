package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import main.Utils;

public class MqttManager  {
	private static  MqttManager managerInstance;
	
	public static MqttManager getInstance(MqttCallback callback, IMqttActionListener actionListener) {
		synchronized(MqttManager.class) {
			if(managerInstance == null) {
				managerInstance = new MqttManager( callback, actionListener);
			}
		}
		return managerInstance;
	}
	
	private MqttAsyncClient mClient;
	private MqttConnectOptions 	conOpt;
	String tmpDir = System.getProperty("java.io.tmpdir");
	MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
	MqttCallback mCallback;
	IMqttActionListener mActionListener;


	public static final String PlayerConnected = "PlayerConnected";
	public static final String Player2MoveDone = "Player2MoveDone";
	public static final String Player1MoveDone = "Player1MoveDone";
	public static final String PlayerWins = "PlayerWins";
	
	
	boolean quietMode 	= false;
	String action 		= "publish";
	public  String[] mTopics  = new String[] {PlayerConnected,Player2MoveDone,Player1MoveDone,PlayerWins};
	String message 		= "Message from async callback Paho MQTTv3 Java client sample";
	int[] mQos 			=  new int[] {2,2,2,2};
	String broker 		= "m16.cloudmqtt.com";
	int port 			= 15723;
	String clientId 	= "";
	boolean cleanSession = true;			// non durable subscriptions
	boolean ssl = false;
	String password = "WxOenOKKAw39";
	String userName = "hmlanxtm";

	String protocol = "tcp://";

	public MqttManager(MqttCallback callback, IMqttActionListener actionListener) {
		this.mCallback  = callback;
		this.mActionListener = actionListener;
		
		String url = protocol + broker + ":" + port;
		Utils.Log("url="+url);
    	clientId = MqttAsyncClient.generateClientId();
		conOpt = new MqttConnectOptions();
    	conOpt.setCleanSession(cleanSession);
    	
    	if(this.password != null ) {
    		conOpt.setPassword(this.password.toCharArray());
    	}
    	if(userName != null) {
            conOpt.setUserName(this.userName);
        }
    	try {
			mClient = new MqttAsyncClient(url, clientId, dataStore);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.printStackTrace();
   			Utils.Log("Unable to set up client: "+e.toString());
   			System.exit(1);
		}

		// Set this wrapper as the callback handler
    	mClient.setCallback(callback);

   	} 
	
	public void initiate() {
		try {
			mClient.connect(conOpt, "Connect context", new IMqttActionListener() {

				@Override
				public void onSuccess(IMqttToken asyncActionToken) {
					// TODO Auto-generated method stub
					Utils.Log("connect to mqtt successfully");
					try {
						mClient.subscribe(mTopics, mQos,"Subscribe  context", mActionListener);
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mActionListener.onFailure(null, e);
					}
				}

				@Override
				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					// TODO Auto-generated method stub
					Utils.Log("fail to connect to mqtt");
					mActionListener.onFailure(null, exception);
				}
				
			});
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mActionListener.onFailure(null, e);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mActionListener.onFailure(null, e);
		}
	}
	
}
