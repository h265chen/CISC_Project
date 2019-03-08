import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


class Piece_Image{	
    final String dir = System.getProperty("user.dir");
    ImageIcon piece;
	public Piece_Image() {

	}
	
	public JLabel get_label(String piece_type) {		
		String img_dir = dir;
		JLabel returnLabel;
		switch (piece_type) {
			case "pawn_p1" : img_dir = dir + "\\pawn_P1.png";
             	break;
			case "rook_p1": img_dir = dir + "\\rook_p1.png";
				break;
			case "knight_p1": img_dir = dir + "\\knight_p1.png";
				break;
			case "bishop_p1": img_dir = dir + "\\bishop_p1.png";
				break;
			case "king_p1": img_dir = dir + "\\king_p1.png";
				break;
			case "queen_p1": img_dir = dir + "\\queen_p1.png";
				break;
			case "pawn_p2":  img_dir = dir + "\\pawn_P2.png";
         		break;
			case "rook_p2": img_dir = dir + "\\rook_p2.png";
				break;
			case "knight_p2": img_dir = dir + "\\knight_p2.png";
				break;
			case "bishop_p2": img_dir = dir + "\\bishop_p2.png";
				break;
			case "king_p2": img_dir = dir + "\\king_p2.png";
				break;
			case "queen_p2": img_dir = dir + "\\queen_p2.png";
				break;
		}
    	ImageIcon piece = new ImageIcon(img_dir);
    	Image img = piece.getImage();
    	Image newimg2 = img.getScaledInstance(50,50,  java.awt.Image.SCALE_SMOOTH);
    	piece = new ImageIcon(newimg2);
    	return returnLabel = new JLabel(piece);
	}
}