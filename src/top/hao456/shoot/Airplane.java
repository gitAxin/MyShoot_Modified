package top.hao456.shoot;
import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
public class Airplane extends FlyingObject implements Enemy{
	private int dindex = 0;
	private int speed = 2; //移动的速度
	private boolean condition;  //敌机的状态
	private BufferedImage[] images; //爆炸图片
	private int index;//协助爆炸图片切换
	private boolean paint;
	private File bmusic;
	/**构造方法*/
	public Airplane(){
		paint=true;
		image = ShootGame.airplane;
		width = image.getWidth();   //获取图片的宽度
		height = image.getHeight(); //获取图片的高度
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);//窗口宽减敌机宽
		y = -this.height;  // 负的敌机的高
		images = new BufferedImage[]{ShootGame.bao01,ShootGame.bao02,ShootGame.bao03};
		index = 0;
		condition = true;
		bmusic = new File("bao.wav");
	}
	
	public boolean isPaint() {
		return paint;
	}


	public void setPaint(boolean paint) {
		this.paint = paint;
	}
	
	public int getDindex() {
		return dindex;
	}

	public void setDindex(int dindex) {
		this.dindex = dindex;
	}
	
	public void dindexPlus(){
		dindex++;
	}
	
	
	public int getScore(){  
		return 5;     //打掉一个敌机得5分
	}
	/** 重写step()方法*/
	public void step(){ 
		y+=speed;   //y+(向下)
		if(!condition){
			image = images[index++/10%images.length];
			speed=0;
		}
	}
	
	public boolean getCondition(){
		return condition;
	}
	
	public void setCondition(){
		this.condition=false;
	}
	
	
	
	
	/**重写outOfBounds检查是否载界*/
	public  boolean outOfBounds(){
		return this.y>=ShootGame.HEIGHT;   //敌机的越界处理
	}

}
