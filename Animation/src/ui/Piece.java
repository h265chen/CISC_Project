package ui;

class Piece{
	int x = 0;
	int y = 0;
	String piece_type;
	
	public Piece(int x,int y, String piece_type) {
		this.x = x;
		this.y = y;
		this.piece_type = piece_type;
	}
	public void setNewCoords(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public String get_piece_type() {
		return this.piece_type;
	}
	public boolean is_occupied() {
		return (this.piece_type != "empty");
	}
}