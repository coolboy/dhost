/**
 * 
 */
package dhost.ui;

import java.awt.Color;
import java.awt.Point;

/**
 * @author Cool
 *
 */
public class GraphicState {
	private Type type_ = Type.OVAL;//Graphic Type
	private Point size_;//Width and height
	private Point pos_;//Position
	private Color clr_ = Color.black;//Color
	
	public enum Type {
	    RECT, OVAL
	}
	
	public Type getType() {
		return type_;
	}


	public void setType(Type type) {
		type_ = type;
	}


	public Point getSize() {
		return size_;
	}


	public void setSize(Point size) {
		size_ = size;
	}


	public Color getColor() {
		return clr_;
	}


	public void setColor(Color clr) {
		clr_ = clr;
	}


	public Point getPos() {
		return pos_;
	}


	public void setPos(Point pos) {
		pos_ = pos;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
