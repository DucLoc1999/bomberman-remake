package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.GameSound;

public class BombItem extends Item {

	public BombItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý Bomber ăn Item
        if(e instanceof Bomber){
			Board._sound.getAudio(GameSound.ITEM).play();
			if(this.isRemoved())
                return false;
			remove();
			Game.addBombRate(1);
		}
        
        if(e instanceof Flame){
			if(this.isRemoved())
                return false;
			remove();
        }

        
		return false;
	}
	


}
