package top.hao456.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

/**小蜜蜂 既是飞行物*/

public class Bee extends FlyingObject implements Award {
	private int xSpeed = 1;  //x坐标的移动速度
	private int ySpeed = 2;	//y坐标的移动速度
	private int awardType;  // //奖励的类型 (0或1);
	private boolean state;
	private int xpd=5;
	private int ypd=7;
	private BufferedImage[] images;
	private int index;
	private int beeIndex;
	
	/**构造方法*/
	public Bee(){
		image = ShootGame.bee;
		width = image.getWidth();   //获取图片的宽度
		height = image.getHeight(); //获取图片的高度
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);//窗口宽减敌机宽
		y = -this.height;  // 负的敌机的高
		awardType = rand.nextInt(2);
		state = true;
		images = new BufferedImage[]{ShootGame.jinbi_1,ShootGame.jinbi_2};
		index = 0;
		beeIndex=0;
		
		
	}
	
	
	public void setState(boolean type) {
		this.state = type;
	}
	
	public boolean getState(){
		return state;
	}
	
	public int getType(){
		return awardType; 
	}
	
	/** 重写step()方法*/
	public void step(){  //每10秒运行一次

		x+=xSpeed;
		y+=ySpeed;	//y+向下
		if(x>=ShootGame.WIDTH-this.width){
			xSpeed=-1;
		}
		if(x<=0){
			xSpeed=1;
		}
		
		}
	/**变金币下落*/
	public void dropStep(Hero hero){
		index++;
		image = images[index++/30%images.length];
		
		if(y<hero.y){
			y+=ypd;
		}
		
		if(y>hero.y){
			y-=ypd;
		}
		if(x>hero.x+this.width/2){
			x-=xpd;
		}
		if(x<hero.x+this.width/2){
			x+=xpd;
		}
			
	}
	
	
	
	
	/**重写outOfBounds检查是否越界*/
	public  boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT;   //蜜蜂的越界处理
		
	}
	
	
	

}
