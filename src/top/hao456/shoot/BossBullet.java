package top.hao456.shoot;

public class BossBullet extends FlyingObject {

	private int xSpeed = 2;// 移动速度
	private int ySpeed = 5;
	private int cord;
	private double a; // 角度
	private double r;

	public BossBullet(int x, int y) {
		image = ShootGame.bossbullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
		this.a = 0.03;
		this.r = 200;

	}

	/** 子弹直线发射 */
	public void step() {
		y += ySpeed;

	}
	/**散弹发射*/
	public void step(Boos s){
			y+=ySpeed;
			int cord =s.width/3;
			if(x>s.x+cord*2){
				x+=xSpeed;
			}
			if(x<s.x+cord*1){
				x-=xSpeed;
			}
	}

	/** 子弹旋转运动 */
	public void revolveStep(Boos s) {
		a += a;
		x = (int) (s.x + s.width / 2 + this.r * (Math.sin(a)));
		y = (int) (s.y + s.height / 2 - this.r * (Math.cos(a)));
	}

	/** 子弹回弹运动 */
	public void reboundStep() {
		x -= xSpeed;
		y += ySpeed;
		if (x <= 0 || x >= ShootGame.WIDTH - this.width) {
			xSpeed = -xSpeed;// 到达左右边界时，x轴移动速度取反
		}
		if (y >= ShootGame.HEIGHT - this.height) {
			ySpeed = -ySpeed;// 到达下边界时，y轴移动速度取反
		}

	}

	public boolean outOfBounds() {
		return this.y >= ShootGame.HEIGHT;
	}

}
