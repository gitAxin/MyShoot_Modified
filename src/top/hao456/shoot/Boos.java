package top.hao456.shoot;

import java.util.Random;

/**
 * 主要实现内容
 * Boos机的血量
 * Boos机的出现时机
 * Boos机发射子弹
 * Boos机被子弹碰撞
 * @author 天大java
 *
 */
public class Boos extends FlyingObject implements Award,Enemy{
	private int doubleFire;
	private int life;
	private boolean print;
	private int xSpeed = 1;//x坐标移动速度
	private int ySpeed = 1;//y坐标移动速度
	private int bulletIndex;
	
	
	public Boos(){
		image  = ShootGame.bossimage;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-this.width);
		y = -this.height;
		doubleFire = 10;
		life = 200;
		print = false;
		
		
	}
	public boolean hit(Bullet other){
		
		return other.x>this.x && other.x<this.x+this.width && other.y < this.y+this.height && other.y>this.y;
		
	}
	@Override
	public int getScore() {
		return 500;
	}

	@Override
	public int getType() {
		return 8;
	}

	@Override
	public void step() {
		if(y<=0 && y>=-this.height){
			y+=ySpeed;
		}
		
		x+=xSpeed;
		if(x>=ShootGame.WIDTH-this.width){
			xSpeed=-1;
		}
		if(x<=0){
			xSpeed =1;
		}
	}
	public BossBullet[] bossshoot(){
		int xStep = this.width/11;
		int yStep = this.height-35;
		int[] random = new int[20];
		for(int i = 0; i < random.length;i++){
			random[i]=(int)(Math.random()*11);
		}
		BossBullet[]bs = new BossBullet[20];
		for(int i = 0 ; i < bs.length;i++){
			if(i>9){
				yStep=this.height-80;
			}
			bs[i]=new BossBullet(this.x+xStep*random[i],yStep);
		}
		return bs;
	}
	
	
	public void subtractLife(){
		life--;
	}
	public int getDoubleFire() {
		return doubleFire;
	}
	public void setDoubleFire(int doubleFire) {
		this.doubleFire = doubleFire;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getxSpeed() {
		return xSpeed;
	}
	public void setxSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	@Override
	public boolean outOfBounds() {
		return false;
	}
	@Override
	public void setCondition() {
		
	}public void setPrint(){
		this.print = true;
	}
	public boolean isPrint(){
		return this.print;
	}
	
	
	
}




