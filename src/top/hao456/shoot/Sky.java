package top.hao456.shoot;

public class Sky extends FlyingObject{
	protected int y1;
	private int speed;
	
	public Sky(){
		speed=1;
		image = ShootGame.background;
		width = image.getWidth();
		height = image.getHeight();
		x = 0;
		y = 0;
		y1 = -height;
	}

	public void step() {
		y+=speed;
		y1+=speed;;
		if(y>=height){
			y=-height;
		}
		if(y1>=height){
			y1=-height;
		}
		
	}

	public boolean outOfBounds() {
		return false;
	}
	
	

}
