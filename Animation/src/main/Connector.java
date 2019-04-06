package main;
import java.sql.Timestamp;
import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import mqtt.MqttManager;
import ui.Game;

public class Connector {
	private static MqttManager mManager;
	private static MqttCallback mCallback;
	private static Game mGame;
	private static IMqttActionListener mListener;

	
	public Connector() {
		mGame =  new Game();
		initiateListener();
		initiateMCallback();
		mManager = MqttManager.getInstance(mCallback, mListener);
		mManager.initiate();
	}

	private void initiateMCallback() {
		mCallback = new MqttCallback() {

			@Override
			public void connectionLost(Throwable cause) {
				// TODO Auto-generated method stub
				Utils.Log("Connection to server lost ->" + cause);
				mGame.showHint("Lost connection to the server");
				System.exit(1);
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				// TODO Auto-generated method stub
				String time = new Timestamp(System.currentTimeMillis()).toString();
				System.out.println("Time:\t" +time +
		                           "  Topic:\t" + topic +
		                           "  Message:\t" + new String(message.getPayload()) +
		                           "  QoS:\t" + message.getQos());
				String coordinates = "";
				switch (topic){
					case MqttManager.PlayerConnected:
						//Player 1 Connected
						String msg = message.toString();
						System.out.println(message.toString());
						if(msg.equals("Player1Connected")) {
							mGame.setPlayerStatus(0,"Player 1 Connected");
						}else  {
							mGame.setPlayerStatus(1, "Player 2 Connected");
							mGame.setPlayerStatus(0, "Player 1 turn");
						}
						break;
					case MqttManager.Player1MoveDone:
						mGame.setPlayerStatus(1, "Player 2 turn");
						mGame.setPlayerStatus(0, "");
						coordinates  = message.toString();
						break;
					case MqttManager.Player2MoveDone:	
						mGame.setPlayerStatus(0, "Player 1 turn");
						mGame.setPlayerStatus(1, "");
						coordinates  = message.toString();
						break;
					case MqttManager.PlayerWins:	
						//0 means A wins
						// 1 means B wins
						if(message.toString() == "1") {
							mGame.showHint("Player 1 Wins the Game!");
							mGame.setPlayerStatus(0, "WIN");
							mGame.setPlayerStatus(1, "LOSE");
						}else {
							mGame.showHint("Player 2 Wins the Game!");
							mGame.setPlayerStatus(0, "LOSE");
							mGame.setPlayerStatus(1, "WIN");
						}
						break;
					default: 
							break;
				}
				
				if(coordinates == "") return;
				int startRow, startCol, endRow, endCol;
				startCol = Character.getNumericValue(coordinates.charAt(0));
	            startRow = Character.getNumericValue(coordinates.charAt(1));
	            endCol = Character.getNumericValue(coordinates.charAt(2));
	            endRow = Character.getNumericValue(coordinates.charAt(3));
				mGame.move_piece(startRow, startCol, endRow, endCol);
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				// TODO Auto-generated method stub
				Utils.Log("Delivery complete callback: Publish Completed "+Arrays.toString(token.getTopics()));

			}
			
		};
	}
	
	
	private void initiateListener() {
		mListener = new IMqttActionListener() {

			@Override
			public void onSuccess(IMqttToken asyncActionToken) {
				// TODO Auto-generated method stub
				Utils.Log("subscribe to mqtt successfully");
//				mGame.setHint("");
//				mGame.showHint("subscribe to mqtt successfully!!");
			}

			@Override
			public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
				// TODO Auto-generated method stub
				Utils.Log("fail to subscribe to mqtt fail");
				System.out.println(exception);
				mGame.showHint("Cannot connect to the Server!!");
			}
			
		};
	}
}
