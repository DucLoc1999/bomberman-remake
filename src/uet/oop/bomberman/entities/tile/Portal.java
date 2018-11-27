package uet.oop.bomberman.entities.tile;

import java.util.logging.Level;
import java.util.logging.Logger;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.GameSound;


public class Portal extends Tile {
	public Portal(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}
	@Override
	public boolean collide(Entity e) {

		if(e instanceof Bomber ){
			if(Game.getBoard().detectNoEnemies()) {
                Board._sound.stop();
                Board._sound.getAudio(GameSound.WIN).play();
                try {
                    Thread.sleep(7500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                Game.getBoard().nextLevel();
			}
		}
		return true;
	}

}
