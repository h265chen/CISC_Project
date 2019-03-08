import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import javax.swing.border.LineBorder;
class ChessBoard extends JFrame{
    public final JPanel gui = new JPanel(new BorderLayout(3, 3));
    public JPanel squares [][];
    public JPanel chessBoard;
    private static final String COLS = "ABCDEFGH";
    final String dir = System.getProperty("user.dir");
    JToolBar tools = new JToolBar();
	public ChessBoard() {
		initializeGui();
	}
	public final void initializeGui() {
	    squares = new JPanel [8][8];
	    chessBoard = new JPanel(new GridLayout(0, 9));
	    chessBoard.setBorder(new LineBorder(Color.BLACK));
	    gui.add(tools, BorderLayout.PAGE_START);
	    gui.add(chessBoard);
	    for (int i = 0; i < squares.length; i++) {
	        for(int j = 0; j< squares[i].length; j++) {
	        	JPanel cell = new JPanel();
	        	if((i + j) % 2 == 0) {
	        		cell.setBackground(Color.white);
	        	}
	        	else {
	        		cell.setBackground(Color.GRAY);
	        	}
	        		squares[i][j] = cell;	
	        	}       	
	        }
	        //fill the chess board
	        chessBoard.add(new JLabel(""));
	        // fill the top row
	        for (int ii = 0; ii < 8; ii++) {
	            chessBoard.add(
	                    new JLabel(COLS.substring(ii, ii + 1),
	                    SwingConstants.CENTER));
	        }
	        for (int i= 0; i < 8; i++) {
	            for (int j = 0; j < 8; j++) {
	                switch (j) {
	                    case 0:
	                        chessBoard.add(new JLabel("" + (i + 1),
	                                SwingConstants.CENTER));
	                    default:
	                        chessBoard.add(squares[j][i]);
	                }
	            }
	        }
	       this.getContentPane().add(gui);
	    }
	
}