package top.hao456.shoot;
//主窗口类

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random; //生成随机数的类
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO; //图片的输入输出
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;//相框
import javax.swing.JLabel;
import javax.swing.JPanel;//面板


public class ShootGame extends JPanel {

	public static final int WIDTH = 512;// 窗口宽度
	public static final int HEIGHT = 768;// 窗口高度
	public static BufferedImage background;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage bao01;
	public static BufferedImage bao02;
	public static BufferedImage bao03;
	public static BufferedImage bossimage;
	public static BufferedImage bossbullet;
	public static BufferedImage jinbi_1;
	public static BufferedImage jinbi_2;
	public static BufferedImage win;
	static {// 初始化静态图片
		try {
			background = ImageIO.read(ShootGame.class.getResource("img_2.jpg")); // 同包中读取图片的方式
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			bao01 = ImageIO.read(ShootGame.class.getResource("bao01.png"));
			bao02 = ImageIO.read(ShootGame.class.getResource("bao02.png"));
			bao03 = ImageIO.read(ShootGame.class.getResource("bao03.png"));
			bossimage = ImageIO.read(ShootGame.class.getResource("bossimage.png"));
			bossbullet = ImageIO.read(ShootGame.class.getResource("bossbullet.png"));
			jinbi_1 = ImageIO.read(ShootGame.class.getResource("jinbi_1.png"));
			jinbi_2 = ImageIO.read(ShootGame.class.getResource("jinbi_2.png"));
			win = ImageIO.read(ShootGame.class.getResource("win.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private Airplane[] airplanes = new Airplane[] {};// 爆炸的数组
	private Hero hero = new Hero(); // 一个英雄机
	private FlyingObject[] flyings = {}; // 一堆敌人
	private Bullet[] bullets = {};// 一堆子弹
	private Sky sky = new Sky();// 天空
	List<BufferedImage> booss = new ArrayList<BufferedImage>();
	private BossBullet[] boosBullets = {};// boos子弹集合
	BossBullet[] bossBulletLives = new BossBullet[boosBullets.length];
	private Boos boss = null;// boos机
	
	public static final int START = 0;   //启动状态
	public static final int RUNNING = 1;//运行状态
	public static final int PAUSE = 2; //暂停状态
	public static final int GAMEOVER = 3;//游戏结束状态
	public static final int WIN = 4;//游戏胜利并结束
	public int state = START;	//当前状态 (默认启动状态)
	
	/**背景音乐*/
	java.net.URL music = getClass().getResource("start.wav");
	AudioClip sound = java.applet.Applet.newAudioClip(music);
	
	/**游戏失败音乐*/
	java.net.URL music2 = getClass().getResource("gameover.wav");
	AudioClip sound2 = java.applet.Applet.newAudioClip(music2);
	
	/**英雄机发射声音*/
	java.net.URL music3 = getClass().getResource("bullet.wav");
	AudioClip sound3 = java.applet.Applet.newAudioClip(music3);
	
	/**BOSS机发射声音*/
	java.net.URL music4 = getClass().getResource("guang2.wav");
	AudioClip sound4 = java.applet.Applet.newAudioClip(music4);
	
	/**奖励声音*/
	java.net.URL music5 = getClass().getResource("jiangli.wav");
	AudioClip sound5 = java.applet.Applet.newAudioClip(music5);
		
	
	/**敌机爆炸声音*/
	java.net.URL music6 = getClass().getResource("bao.wav");
	AudioClip sound6 = java.applet.Applet.newAudioClip(music6);
	
	
	/**游戏胜利声音*/
	java.net.URL music7 = getClass().getResource("shengli.wav");
	AudioClip sound7 = java.applet.Applet.newAudioClip(music7);
	
	
	/**开始菜单音效*/
	public void playMusic(){
		java.net.URL music = getClass().getResource("gamescore.wav");
		AudioClip sound2 = java.applet.Applet.newAudioClip(music);
		sound2.play();
	}
		
	int score = 0; // 玩家的得分


	public FlyingObject nextOne() {
		Random rand = new Random();
		int type = rand.nextInt(20);// 生成0-19的随机数
		if (type < 5) { // 0-5时生成小蜜蜂对象
			return new Bee();
		} else { // 4到19时生成敌机对象
			return new Airplane();
		}
	}

	int flyIndex = 0;

	/** 敌人(敌机+小蜜蜂)入场 */
	boolean flag = true;

	public void enterAction() { // 10秒走一次

		flyIndex++;
		if (flyIndex % 40 == 0 && boss == null) {
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj; //
		}

		if (score > 500 && flag == true) {
			boss = new Boos();
			flag = false;
		}

	}

	int bossIndex = 0;

	/** 敌人走步 */
	public void stepAction() {// 每10毫秒走一次
		hero.step();
		for (int i = 0; i < flyings.length; i++) { // 遍历所有敌人
			FlyingObject one = flyings[i];
			if (one instanceof Airplane) {
				Airplane e = (Airplane) one;
				e.step(); // 敌人走一步
			}
			if (one instanceof Bee) {
				Bee b = (Bee) one;
				if (!b.getState()) {
					b.dropStep(hero);

				} else {
					b.step();
				}
			}

		}
		for (int i = 0; i < bullets.length; i++) { // 遍历所有子弹
			bullets[i].step(); // 子弹走一步
		}

		for (int i = 0; i < flys.length; i++) {
			flys[i].step();
		}

		for (int i = 0; i < bees.length; i++) {
			bees[i].dropStep(hero);
		}

		/** boss机移动 */
		if (boss != null) { // 如果boss机已画,则boss机移动一步
			bossIndex++;
			boss.step();
		}

		/** boss机子弹移动一步 */
		if (boss != null) { // 如果boss机子弹已画,则boss机子弹移动一步
			for (int i = 0; i < boosBullets.length; i++) {
				boosBullets[i].step(boss);
			}
		}
	}

	/** 背景滚动 */
	public void skyStep() {
		if(boss==null){
			sky.step();
		}
	}

	int shootIndex = 0;

	// 子弹发射
	public void shootAction() {
		shootIndex++;
		if (shootIndex % 20 == 0) {
			Bullet[] bs = hero.shoot();
			sound3.play();
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
		}
		// boos子弹发射
		if (bossIndex >= 200) {
			if (shootIndex % 300 == 0 && boss!=null) {
				BossBullet[] bs1 = boss.bossshoot();
				sound4.play();
				boosBullets = Arrays.copyOf(boosBullets, boosBullets.length + bs1.length);
				System.arraycopy(bs1, 0, boosBullets, boosBullets.length - bs1.length, bs1.length);
			}
		}
	}

	// 越界处理
	public void outOfBoundsAction() {
		int index = 0; // 1)不越界敌人数组下标 2)不越界敌人的计数器
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (!f.outOfBounds()) {
				flyingLives[index] = f; // 不越界的放到FlyingLives
				index++;
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);

		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length];
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (!b.outOfBounds()) {
				bulletLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);

		index = 0;
		
		// boos机子弹越界处理
		BossBullet[] bossBulletLives = new BossBullet[boosBullets.length];
		for (int i = 0; i < boosBullets.length; i++) {
			BossBullet b1 = boosBullets[i];
			if (!b1.outOfBounds()) {
				bossBulletLives[index] = b1;
				index++;
			}
		}
		boosBullets = Arrays.copyOf(bossBulletLives, index);
	}


	/** 所有的子弹与所有敌人(敌机+小蜜蜂)的碰撞 */
	Airplane[] flys = new Airplane[] {}; // 变成爆炸的敌人
	Bee[] bees = new Bee[] {}; // 变成金币的奖励

	public void bangAction() {
		int aliveindex = 0;
		int removeindex = -1;
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			if (bang(b)) {
				Bullet t = bullets[i];
				bullets[i] = bullets[bullets.length - 1];
				bullets[bullets.length - 1] = t;
				bullets = Arrays.copyOf(bullets, bullets.length - 1);
			}
			
			if(boss != null && boss.shootBy(b)){
				Bullet t = bullets[i];
				bullets[i] = bullets[bullets.length - 1];
				bullets[bullets.length - 1] = t;
				bullets = Arrays.copyOf(bullets, bullets.length - 1);
				
				
			}
			

			if (boss != null) {
				if (boss.hit(b)) {
					boss.subtractLife();
					score+=10;
					
				}


			}

		}
		
		
		for (int i = 0; i < flys.length; i++) {
			Airplane air = flys[i];
			air.dindexPlus();
			if (air.getDindex() < 50) {
				flys[aliveindex] = air;
				aliveindex++;
			}
		}
		flys = Arrays.copyOf(flys, aliveindex);

		// 判断boos机子弹是否与英雄机相撞
		for (int i = 0; i < boosBullets.length; i++) {
			BossBullet b1 = boosBullets[i];
			if(hero.shootBy(b1)){
				hero.setlife(1);
			}

		}
	}

	

	/** 一个子弹与所有敌人(敌机+小蜜蜂)的碰撞 */
	public boolean bang(Bullet b) {

		int index = -1; // 被撞敌人的下标
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (f instanceof Airplane) { // 判断敌人是不是飞机
				Airplane air = (Airplane) f;
				if (air.getCondition() == false) {
					continue;

				}
			}
			if (f.shootBy(b)) {
				index = i;
				break; // 其余敌人不再参与比较了
			}
		}

		if (index != -1) {
			FlyingObject one = flyings[index]; // 被撞的敌人
			if (one instanceof Enemy) { // 若是敌人 强转为敌人
				sound6.play();
				Enemy e = (Enemy) one; //
				score += e.getScore();
				e.setCondition();
				System.out.println("分数:" + score);
				Airplane plane = (Airplane) one;
				plane.setCondition();
				flys = Arrays.copyOf(flys, flys.length + 1);
				flys[flys.length - 1] = plane;

			}

			if (one instanceof Bee) {
				Bee bee = (Bee) one;
				bee.setState(false);
				sound5.play();
				bees = Arrays.copyOf(bees, bees.length + 1);
				bees[bees.length - 1] = bee;
			}

			if (one instanceof Award) {
				Award a = (Award) one;
				int type = a.getType();
				switch (type) {
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					System.out.println("命数:" + hero.getLife());
					break;
				}
			}
			// 交换被撞的敌人与数组中的最后一个元素
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = t;
			// 缩容 (去掉最后一个元素,即被撞的敌人对象)
			flyings = Arrays.copyOf(flyings, flyings.length - 1);

			return true;
		}
		return false;

	}

	/** 检查英雄机和敌人是否相撞 */
	public void hitAction() {
		for (int i = 0; i < flyings.length; i++) {
			FlyingObject f = flyings[i];
			if (hero.hit(f)) {
				hero.downLife();
				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}

		for (int i = 0; i < bees.length; i++) {
			Bee be = bees[i];
			if (hero.hit(be)) {
				Bee t = bees[i];
				bees[i] = bees[bees.length - 1];
				bees[bees.length - 1] = t;
				bees = Arrays.copyOf(bees, bees.length - 1);
			}
		}

	}
	
	
	
	public void checkGameOverAction(){
		if(hero.getLife()<=0 ){
			sound2.play();
			state=GAMEOVER;
		}
		if(boss!=null){
			if(boss.getLife()<=0){
				sound7.play();
				boss=null;
				state=WIN;
			}
		}
		
	}
	
	

	/** 启动程序的执行 */
	
	boolean isplay = false;
	public void action() {
		// 创建鼠标侦听器
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING){
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}
			
			
			public void mouseClicked(MouseEvent e){
				switch(state){
				case START:
					state = RUNNING;
					isplay=true;
					break;
				case GAMEOVER:
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					boosBullets = new BossBullet[0];
					boss= null;
					flag = true;
					isplay=true;
					state = START;
					break;
				case WIN:
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					boosBullets = new BossBullet[0];
					boss= null;
					flag = true;
					isplay=true;
					state = START;
					break;
				}
				
			}
			
			
			public void mouseExited(MouseEvent e){
				if(state == RUNNING){
					state = PAUSE;
					sound.stop();
				}
			}
			public void mouseEntered(MouseEvent e){
				if(state == PAUSE){
					state = RUNNING;
					sound.play();
				}
			}
			
		};
		this.addMouseListener(l); // 处理鼠标操作事件
		this.addMouseMotionListener(l); //

		Timer timer = new Timer();//
		int intervel = 10; // 时间间隔(以毫秒为单位)
		timer.schedule(new TimerTask() {
			public void run() {
				if(isplay){
					sound.loop();
					isplay=false;
				}
				if(state==GAMEOVER || state==WIN){
					sound.stop();
				}
				if(state==RUNNING){
					enterAction();// 敌人(敌机+小蜜蜂);
					stepAction(); // 敌人 移动
					shootAction(); // 子弹入场
					outOfBoundsAction(); // 删除越界的飞行物
					bangAction();// 子弹击中敌机
					hitAction();
					skyStep();// 背景移动
					checkGameOverAction();
				}
				repaint();// 重画 (调用paint()方法)

			}
		}, intervel, intervel); // 第一个10:程序启动到第一次触发的时间间隔
	}

	/*
	 * 在实现时,Timer类可以调度任务,TimerTask则是通过在run()方法里实现具体任务.Timer实例可以调度多任务 它是线程安全的.
	 * Timer 的构造器被调用时,它创建了一个线程,这个线程可以用来调度任务 .
	 * 
	 * public void action(){
	 *  TimerTask task = new TimerTask(){   //实现任务 public
	 * 			void run(){
	 *    }
	 *  }; 
	 * int interel = 10; 
	 * Timer timer = new Timer();
	 * timer.schedule(task, interel,interel);
	 *}
	 */

	/** 重写paint() */
	public void paint(Graphics g) {
		paintSky(g);
		paintHero(g);// 画英雄机对象
		paintFlyingObject(g);// 画敌人(敌机+小蜜蜂)
		paintBullets(g);// 画子弹对象
		paintScoreAndFire(g); //
		paintflys(g);
		paintbees(g);
		paintBossBullets(g);
		paintBoss(g);
		paintState(g);
	}

	/** 画背景 */
	public void paintSky(Graphics g) {
		g.drawImage(sky.image, sky.x, sky.y, null);
		g.drawImage(sky.image, sky.x, sky.y1, null);
	}

	// 画Boss机
	public void paintBoss(Graphics g) {
		if (boss != null) { 
			g.drawImage(boss.image, boss.x, boss.y, null);
		}
	}

	// boos机子弹
	public void paintBossBullets(Graphics g) {
		if (boss != null) { 
			for (int i = 0; i < boosBullets.length; i++) {
				FlyingObject d = boosBullets[i];
				g.drawImage(d.image, d.x, d.y, null);

			}
			flyings = new FlyingObject[0];
		}

	}

	/** 画爆炸的敌机 */
	public void paintflys(Graphics g) {
		for (int i = 0; i < flys.length; i++) {
			Airplane air = flys[i];
			g.drawImage(air.image, air.x, air.y, null);
		}
	}

	public void paintbees(Graphics g) {
		for (int i = 0; i < bees.length; i++) {
			Bee e = bees[i];
			g.drawImage(e.image, e.x, e.y, null);
		}
	}

	public void paintHero(Graphics g) { // 画英雄机

		g.drawImage(hero.image, hero.x, hero.y, null);

	}

	public void paintFlyingObject(Graphics g) { // 画敌人
		for (int i = 0; i < flyings.length; i++) {// 遍历所有敌人,
			FlyingObject f = flyings[i];
			g.drawImage(f.image, f.x, f.y, null);
		}

	}

	public void paintBullets(Graphics g) {// 画子弹对象
		for (int i = 0; i < bullets.length; i++) {
			Bullet b = bullets[i];
			g.drawImage(b.image, b.x, b.y, null);
		}

	}

	public void paintScoreAndFire(Graphics g) {
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
		g.setColor(new Color(255, 0, 0));
		g.drawString("SCORE:" + score, 10, 25);
		g.drawString("LIFE:" + hero.getLife(), 10, 75);
		g.drawString("FIRE:" + hero.getdoubleFire(), 10, 50);
		if(boss != null){
			g.drawString("BOSSLIFE:"+((boss.getLife()/200.00)*100)+"%", 300, 25);
		}
		
	}
	
	/**画状态*/
	public void paintState(Graphics g){
		switch(state){
		case PAUSE:
			g.drawImage(pause, 0,0,null);
			break;
		case GAMEOVER:
			g.drawImage(gameover, 0,0,null);
			break;
		case WIN:
			g.drawImage(win, 0,0,null);
			break;
		}
	}
	
	
	

	public static void main(String[] args) {
		//新建窗口，设置背景图
		JFrame frame1 = new JFrame("Fly");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon img=new ImageIcon("src/cn/tedu/shoot/backgroundfirst.png");
		JLabel imgLabel =new JLabel(img);
		frame1.getLayeredPane().add(imgLabel,new Integer(Integer.MIN_VALUE));
		imgLabel.setBounds(0,0,img.getIconWidth(),img.getIconHeight());
		Container cp=frame1.getContentPane();
		cp.setLayout(new BorderLayout());
		((JPanel)cp).setOpaque(false);
		frame1.setSize(WIDTH, HEIGHT);
		//插入按钮
		JButton jb=new JButton();
		ImageIcon icon=new ImageIcon("src/cn/tedu/shoot/go.png");
		jb.setIcon(icon);
		jb.setOpaque(true);
		//jb.setFont(new Font("华文彩云",Font.PLAIN,32));
		//jb.setForeground(Color.RED);
		//jb.setBackground(Color.pink);
		frame1.setLayout(null);
		jb.setBounds(180,450,150,50);
		frame1.add(jb);
		frame1.setAlwaysOnTop(true); //设置总是在最上面
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗口默认关闭操作(关闭窗口时退出程序)
		frame1.setLocationRelativeTo(null); //设置居中显示
		frame1.setVisible(true); //1)设置窗口
		//为按钮实现鼠标监听，点击时实现页面跳转
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				frame1.setVisible(false);
				JFrame frame = new JFrame("Fly"); //创建窗口对象
				ShootGame game = new ShootGame(); //创建面板对象
				frame.add(game);
				frame.setSize(WIDTH, HEIGHT); //设置窗口大小
				frame.setAlwaysOnTop(true); //设置总是在最上面
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗口默认关闭操作(关闭窗口时退出程序)
				frame.setLocationRelativeTo(null); //设置居中显示
				frame.setVisible(true); //1)设置窗口可见  2)尽快调用paint()方法
				game.action(); //启动程序的执行
				game.playMusic();
			}
		});
		
		
		
		
	}

}
