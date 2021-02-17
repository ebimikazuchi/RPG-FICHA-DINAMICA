package com.doglab.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.doglab.main.Game;

public class Label extends Entity{

	protected ArrayList<Entity> labels; 
	private BufferedImage lightImage;
	protected int size = 0;
	private boolean light = false;
	protected boolean current = false;
	public boolean tick = true;
	public int inLocal;
	
	protected static Comparator<Entity> labelSorter = new Comparator<Entity>() {

		@Override
		public int compare(Entity o1, Entity o2) {
			
			if(o1.order < o2.order) {
				return -1;
			}
			if(o1.order > o2.order) {
				return +1;
			}
	
			return 0;
		}
		
	};
	
	public Label(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		labels = new ArrayList<Entity>();
		lightImage = Game.spr_entities.getSprite(101, 156, 35, 36);
	}
	
	public void tick() {
		if(tick) {
			if((Game.mouseController.currentX > this.getX()+size && Game.mouseController.currentY > this.getY()+size-Game.roller.getY()*Game.roller.step) &&
					(Game.mouseController.currentX < this.getX()-size+this.getWidth() && 
							Game.mouseController.currentY < this.getY()-size-Game.roller.getY()*Game.roller.step+this.getHeight())) {
				light = true;
				if(!current) {
					size = 30;
					this.x-=size;
					this.y-=size;
					this.width+=size*2;
					this.height+=size*2;
					current = true;
					this.order = 1;
					Collections.sort(Game.entities, labelSorter);
				}
			}else {
				light = false;
				if(current) {
					this.order = 0;
					Collections.sort(Game.entities, labelSorter);
					this.x+=size;
					this.y+=size;
					this.width-=size*2;
					this.height-=size*2;
					current = false;
					size = 0;
				}
			}
			for(int i = 0; i < labels.size(); i++) {
				Entity e = labels.get(i);
				e.tick();
			}
		}
	}
	
	public void changeTickers() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Label) {
				((Label) e).tick = !((Label) e).tick;
			}
		}
		this.tick = true;
	}
	
	public void render(Graphics g) {
		if(light) {
			g.drawImage(lightImage, getX()-5, getY()-5-Game.roller.getY()*Game.roller.step, getWidth()+10, getHeight()+10, null);
		}
		g.setColor(new Color(0xFF000000));
		g.fillRect(getX(), getY()-Game.roller.getY()*Game.roller.step, getWidth(), getHeight());
		g.setColor(new Color(0xFF424242));
		g.drawRect(getX(), getY()-Game.roller.getY()*Game.roller.step, getWidth(), getHeight());
		for(int i = 0; i < labels.size(); i++) {
			Entity e = labels.get(i);
			e.render(g);
		}
	}

}
