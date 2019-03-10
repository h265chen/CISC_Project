package ui;
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
		img_dir = dir + "/"+ piece_type + ".png";
    	ImageIcon piece = new ImageIcon(img_dir);
    	Image img = piece.getImage();
    	Image newimg2 = img.getScaledInstance(45,45,java.awt.Image.SCALE_SMOOTH);
    	piece = new ImageIcon(newimg2);
    	return returnLabel = new JLabel(piece);
	}
}