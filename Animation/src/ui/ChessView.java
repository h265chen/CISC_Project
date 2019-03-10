package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


class ChessView extends JFrame{
//    public final JPanel gui = new JPanel(new BorderLayout(3, 10));
    private JPanel playersView = new JPanel();
    public JPanel squares [][];
    public JPanel chessBoard;
    private static final String COLS = "ABCDEFGH";
    final String dir = System.getProperty("user.dir");
    JToolBar tools = new JToolBar();
    
    JLabel playerA = new JLabel("PlayerA");
    JLabel playerB = new JLabel("PlayerB");
    
    public JLabel playerARst = new JLabel("Wait to check inssssssssss");
    public JLabel playerBRst = new JLabel("Wait to check in");
    
    JLabel hint = new JLabel("");
    
	public ChessView() {
		initializeGui();
		
	}
	public final void initializeGui() {
//		Container pane = this.getContentPane();
//		pane.setLayout(new BorderLayout());
		setSize(new Dimension(700,500));
		this.setLayout(new BorderLayout());
		
		chessBoard = new JPanel(new GridLayout(0, 9));
		chessBoard.setBackground(Color.YELLOW);
		chessBoard.setPreferredSize(new Dimension(500, 500));
		
		playersView.setBackground(Color.lightGray);
	    playersView.setPreferredSize(new Dimension(200, 500));
	    Border border = playersView.getBorder();
	    Border margin = new EmptyBorder(50,30,50,50);
	    playersView.setBorder(new CompoundBorder(border, margin));
	   
	    
	    SpringLayout layout = new SpringLayout();
	    
	    playersView.setLayout(layout);
	    playersView.add(playerA);
	    playersView.add(playerARst);
	    playersView.add(playerBRst);
	    playersView.add(playerB);
	    playersView.add(hint);
	    
	    layout.putConstraint(SpringLayout.NORTH, playerARst, 20,
                SpringLayout.NORTH,playerA );
	    
	    layout.putConstraint(SpringLayout.SOUTH, playerBRst, 0,
                SpringLayout.SOUTH,playersView );
	    
	    layout.putConstraint(SpringLayout.SOUTH, playerB, -20,
                SpringLayout.NORTH,playerBRst );
	    layout.putConstraint(SpringLayout.NORTH, hint, 230,
                SpringLayout.NORTH,playersView );
	    
	    
	    Font playerFont = new Font("Courier", Font.BOLD,20);
	    Font statusFont = new Font("Courier", Font.PLAIN,15);
	    playerA.setFont(playerFont);
	    playerB.setFont(playerFont);
	    hint.setFont(statusFont);
	    hint.setForeground(Color.RED);
	    
	    playerARst.setFont(statusFont);
	    playerARst.setForeground(Color.GRAY);
	    playerARst.setPreferredSize(new Dimension(150, 100));
	    playerBRst.setPreferredSize(new Dimension(150, 100));

	    playerBRst.setFont(statusFont);
	    playerBRst.setForeground(Color.GRAY);
	    
		
	    add(tools, BorderLayout.PAGE_START);
	    add(chessBoard, BorderLayout.CENTER);
	    add(playersView, BorderLayout.LINE_START);
	    
	
	    squares = new JPanel [8][8];
	    
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
	    }
	
}