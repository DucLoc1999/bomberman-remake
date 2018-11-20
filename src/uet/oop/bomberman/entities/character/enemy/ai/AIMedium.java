package uet.oop.bomberman.entities.character.enemy.ai;

import static java.lang.Math.abs;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class AIMedium extends AI {
	Bomber _bomber;
	Enemy _e;
	
	public AIMedium(Bomber bomber, Enemy e) {
		_bomber = bomber;
		_e = e;
	}

	@Override
	public int calculateDirection() {
        double x = (_bomber.getX() - _e.getX())/Game.TILES_SIZE;
        double y = (_bomber.getY() - _e.getY())/Game.TILES_SIZE;
        
        if(abs(x) + abs(y) <= 3){
            int[] arr = new int[2];
            if(x > 0)
                arr[0] = 1;
            else
                arr[0] = 3;
            
            if(y >= 0)
                arr[1] = 2;
            else
                arr[1] = 0;
            return arr[random.nextInt(2)];
        } else
            return random.nextInt(4);
	}

}
