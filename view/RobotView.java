
package view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import model.*;

/**
 * A hozz� tartoz� Robot kirajzol�s��rt felel�s oszt�ly
 *
 */

public class RobotView implements Drawable{
	
	private BufferedImage imgForVelVec;
	private Point size;
	private final static int priority = 3;
	private Robot robot;
	
	/**
	 * Konstruktor, be�ll�tja a robot �s size attrub�tumot a kapott param�terekre
	 * 
	 * @param r	Robot
	 * @param size	P�lya m�rete
	 */
	
	public RobotView(Robot r, Point size){
		robot=r;
		this.size=size;
	}
	
	/**
	 * Kirajzolja a hozz� tartoz� Robot objektumot
	 */
	
 	public void Draw(Graphics2D g2){
 		if(robot.isAlive()){
			g2.drawImage(View.imgOfRobot, robot.getPosition().x*25, robot.getPosition().y*25, 25, 25, null);		//robot rajzol�sa
			g2.setColor(Color.ORANGE);
			g2.setFont(new Font("TimesRoman", Font.PLAIN, 10));
			g2.drawString(String.valueOf(robot.getId()), robot.getPosition().x*25+10, robot.getPosition().y*25+17);	//robot sorssz�m�nak rajzol�sa a robotra
			if(robot.isCurrent()){																					//sebess�gvektor rajzol�sa, ha ez a robot a jelenlegi robot
				BasicStroke bs=(BasicStroke) g2.getStroke();
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.RED);
				g2.drawRect(robot.getPosition().x*25, robot.getPosition().y*25, 25, 25);							//piros n�gyzetet rajzol k�r�, ezzel jelezve, hogy ez a robot van kiv�lasztva, ezt ir�ny�thatjuk
				g2.setStroke(bs);
				
				//sebess�g rajzol�sa
				this.imgForVelVec=new BufferedImage(size.x*25, size.y*25, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g=imgForVelVec.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);				//nem lesz annyira rec�s a ny�l
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
				g.fillRect(0 , 0, imgForVelVec.getWidth(), imgForVelVec.getWidth());
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
				if(robot.getSpeed()!=0){																			//csak akkor rajzol ny�lat ha van egy�ltal�n sebess�ge
					g.setColor(Color.RED);
					g.setStroke(new BasicStroke(2));
					Point source = new Point(robot.getPosition().x*25+12, robot.getPosition().y*25+12);				//innen indul a ny�l
					Point destination = new Point(robot.getPosition().x*25+robot.getVelocity().x*25+12, robot.getPosition().y*25+robot.getVelocity().y*25+12);	//itt a ny�l v�ge
					g.drawLine(source.x, source.y, destination.x, destination.y);									//vonal megh�z�sa a kett� v�gpont k�z�tt
					double angle = Math.atan2(destination.y - source.y, destination.x - source.x);					//forgat�si sz�g kisz�m�t�sa a ny�lhegy forgat�s�hoz
					//ny�lhegy koordin�t�i
					Point p1,p2,p3;
					p1=new Point(-4, -4);
					p2=new Point(0, 0);
					p3=new Point(-4, 4);
					//forgat�s
					double[] pt1 = {p1.x, p1.y};
					AffineTransform.getRotateInstance(angle, 0, 0).transform(pt1, 0, pt1, 0, 1);
					p1.x = (int) pt1[0];
					p1.y = (int) pt1[1];
					double[] pt2 = {p2.x, p2.y};
					AffineTransform.getRotateInstance(angle, 0, 0).transform(pt2, 0, pt2, 0, 1);
					p2.x = (int) pt2[0];
					p2.y = (int) pt2[1];
					double[] pt3 = {p3.x, p3.y};
					AffineTransform.getRotateInstance(angle, 0, 0).transform(pt3, 0, pt3, 0, 1);
					p3.x = (int) pt3[0];
					p3.y = (int) pt3[1];
					//eltol�s a v�gponthoz
					p1.setLocation(p1.x+destination.x , p1.y+destination.y);
					p2.setLocation(p2.x+destination.x , p2.y+destination.y);
					p3.setLocation(p3.x+destination.x , p3.y+destination.y);
					g.drawLine(p1.x, p1.y, p2.x, p2.y);
					g.drawLine(p3.x, p3.y, p2.x, p2.y);		//ny�lhegy megrajzol�sa
				}
			}else{
				imgForVelVec=null;
			}
		}
		else{
			imgForVelVec=null;
		}
 	}
	
 	/**
 	 * Visszaadja a kirajzol�si priorit�st
 	 */
 	
	public int getPriority(){
		return priority;
	}
	
	/**
	 * Visszaadja az imgForVelVec attrib�tum�t
	 * @return
	 */
	
	public BufferedImage getImgForVelVec(){
		return imgForVelVec;
	}
	
	/**
	 * Igazzal t�r vissza ha a kapott referencia megegyezik a robot attrib�tummal
	 */
	
	public boolean equals(Object o){
		return robot==o;
	}
	
	/**
	 * �sszehasonl�tja a kapott Drawable priorit�s�t a saj�tj�val, majd visszaadja az eredm�nyt
	 */
	
	public int compareTo(Drawable d){
		return Integer.valueOf(RobotView.priority).compareTo(Integer.valueOf(d.getPriority()));
	}
	
}
