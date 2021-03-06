import java.awt.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

class Game {
	private JButton add = new JButton("Add");
	private JButton connectMqtt = new JButton("Connect to Server");
	private JButton printBoard = new JButton("Print Board");
    final String dir = System.getProperty("user.dir");
	private ChessBoard cb = new ChessBoard();
	private Piece p_array [][];
  	private JLabel piece_label;
	private MQTT_Interface mq_i;
	private Timer t;
	public Game() {
		pollingMessageQueue();
		connectToMqtt();
		initializeBoard();
		setButtons();
		setPieces();
		createSubscribeEventListener();
		createAddEventListener();
		createDeleteEventListener();
		createOnCloseEventListener();
		startGame();
	}
	private void connectToMqtt() {
  	  mq_i = new MQTT_Interface();
  	  t.start();
	}
	private void initializeBoard() {
		p_array = new Piece[8][8];
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++){
				Piece emptyPiece = new Piece(i,j,"empty");
				p_array[i][j] = emptyPiece;
			}
		}
	}
	private void setPieces() {
		JPanel board_square;
		//Add Player 2 pawns
		Piece_Image piece1 = new Piece_Image();
		for(int i = 0; i < 8; i ++) {
			JLabel piece_label = piece1.get_label("pawn_p2");
			Piece pawn = new Piece(i,1,"pawn_p2");
			p_array[i][1] = pawn;
			board_square = cb.squares[i][1];
			board_square.add(piece_label);
		}
		//Add player 1 pawns
		for(int i = 0; i < 8; i ++) {
			JLabel piece_label = piece1.get_label("pawn_p1");
			Piece pawn = new Piece(i,6,"pawn_p1");
			p_array[i][6] = pawn;
			board_square = cb.squares[i][6];
			board_square.add(piece_label);
		}
		
		p_array[0][7] = new Piece(0,7,"rook_p1");
		board_square = cb.squares[0][7];
		board_square.add(piece1.get_label("rook_p1"));
		
		p_array[7][7] = new Piece(7,7,"rook_p1");
		board_square = cb.squares[7][7];
		board_square.add(piece1.get_label("rook_p1"));
		
		p_array[1][7] = new Piece(1,7,"knight_p1");
		board_square = cb.squares[1][7];
		board_square.add(piece1.get_label("knight_p1"));
		
		p_array[6][7] = new Piece(6,7,"knight_p1");
		board_square = cb.squares[6][7];
		board_square.add(piece1.get_label("knight_p1"));
		
		p_array[2][7] = new Piece(2,7,"bishop_p1");
		board_square = cb.squares[2][7];
		board_square.add(piece1.get_label("bishop_p1"));
		
		p_array[5][7] = new Piece(5,7,"bishop_p1");
		board_square = cb.squares[5][7];
		board_square.add(piece1.get_label("bishop_p1"));

		p_array[3][7] = new Piece(3,7,"queen_p1");
		board_square = cb.squares[3][7];
		board_square.add(piece1.get_label("queen_p1"));
		
		p_array[4][7] = new Piece(4,7,"king_p1");
		board_square = cb.squares[4][7];
		board_square.add(piece1.get_label("king_p1"));
	
		p_array[0][0] = new Piece(0,0,"rook_p2");
		board_square = cb.squares[0][0];
		board_square.add(piece1.get_label("rook_p2"));
		
		p_array[7][0] = new Piece(7,0,"rook_p2");
		board_square = cb.squares[7][0];
		board_square.add(piece1.get_label("rook_p2"));
		
		p_array[1][0] = new Piece(1,0,"knight_p2");
		board_square = cb.squares[1][0];
		board_square.add(piece1.get_label("knight_p2"));
		
		p_array[6][0] = new Piece(6,0,"knight_p2");
		board_square = cb.squares[6][0];
		board_square.add(piece1.get_label("knight_p2"));
		
		p_array[2][0] = new Piece(2,0,"bishop_p2");
		board_square = cb.squares[2][0];
		board_square.add(piece1.get_label("bishop_p2"));
		
		p_array[5][0] = new Piece(5,0,"bishop_p2");
		board_square = cb.squares[5][0];
		board_square.add(piece1.get_label("bishop_p2"));
		
		p_array[3][0] = new Piece(3,0,"queen_p2");
		board_square = cb.squares[3][0];
		board_square.add(piece1.get_label("queen_p2"));
		
		p_array[4][0] = new Piece(4,0,"king_p2");
		board_square = cb.squares[4][0];
		board_square.add(piece1.get_label("king_p2"));
	}
	private void move_piece(int startCol,int startRow,int endCol, int endRow) {
		Piece start_p = p_array[startCol][startRow];
		Piece end_p = p_array[endCol][endRow];
		String piece_type = start_p.get_piece_type();
		Piece_Image pi_new= new Piece_Image();
		JLabel pi_newLabel = pi_new.get_label(piece_type);
		if(end_p.is_occupied()) {
			delete_piece(endRow,endCol);
		}
		p_array[startCol][startRow] = new Piece(startCol,startRow,"empty");
		p_array[endCol][endRow] = new Piece(endCol,endRow,piece_type);
		cb.squares[endCol][endRow].add(pi_newLabel);
		cb.squares[endCol][endRow].revalidate();
		delete_piece(startRow,startCol);
	}
	private void delete_piece(int row, int col){
  	  JPanel board_square = cb.squares[col][row];
  	  for (Component jc : board_square.getComponents()) {
  		    if ( jc instanceof JLabel ) {
  		        board_square.remove(jc);
  		        board_square.revalidate();
  		        board_square.repaint();
  		        break;
  		    }
  		}
	}
	private void createAddEventListener() {
	    add.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  move_piece(6,0,1,0);
	      }
	    });
	}
	private void createSubscribeEventListener() {
	    connectMqtt.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  //Create MQTT object and connect
	    	  mq_i.subscribeToTopic("Player1MoveDone");

	      }
	    });
		
	}
	private void createDeleteEventListener() {
	    printBoard.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent e)
	      {
	    	  for (int i = 0; i < 8; i++) {
	    		    for (int j = 0; j < 8; j++) {
	    		        System.out.print(p_array[j][i].piece_type + " ");
	    		    }
	    		    System.out.println();
	    		}
	      }
	    });
		
	}
	private void createOnCloseEventListener() {
		  cb.addWindowListener(new WindowAdapter()
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	            	//Disconnect from MQTT intance
	            	mq_i.disconnect();
	                System.out.println("Game closed");
	                e.getWindow().dispose();
	                t.stop();
	            }
	        });
		
	}
	private void pollingMessageQueue() {
		
		t = new Timer(1000, new ActionListener() {
	            public void actionPerformed(ActionEvent evt) {
	            	String messageBuffer = mq_i.getMessage();
	                if (messageBuffer != null) {
			            System.out.println(messageBuffer);
			            int ch1 = Character.getNumericValue(messageBuffer.charAt(0));
			            int ch2 = Character.getNumericValue(messageBuffer.charAt(1));
			            int ch3 = Character.getNumericValue(messageBuffer.charAt(2));
			            int ch4 = Character.getNumericValue(messageBuffer.charAt(3));
			            move_piece(ch1,ch2,ch3,ch4);
			            mq_i.resetMessage();
	                } 
	            }
	        });
	}
	private void setButtons() {
		cb.tools.add(add);
		cb.tools.add(printBoard);
		cb.tools.add(connectMqtt);
	}
	private void startGame() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
            	cb.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            	cb.setLocationByPlatform(true);
            	cb.pack();
            	cb.setSize(600,600);
            	cb.setResizable(false);
            	cb.setVisible(true);
            }  
        };
        SwingUtilities.invokeLater(r);
	}

}
