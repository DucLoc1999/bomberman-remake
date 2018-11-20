package uet.oop.bomberman.entities.character.enemy.ai;

import static java.lang.Math.abs;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class AIMedium extends AI {
	Bomber _bomber;
	Enemy _e;
	double _range;
    
	public AIMedium(Bomber bomber, Enemy e, double range) {
		_bomber = bomber;
		_e = e;
        _range = range;
	}

	@Override
	public int calculateDirection() {
        double x = (_bomber.getX() - _e.getX())/Game.TILES_SIZE;
        double y = (_bomber.getY() - _e.getY())/Game.TILES_SIZE;
        
        if(abs(x) + abs(y) <= _range){
            int[] arr = new int[2];
            if(x > 0)
                arr[0] = 1;
            else if(x < 0)
                arr[0] = 3;
            else
                arr[0] = -1;//khong di theo huong nay
            
            if(y > 0)
                arr[1] = 2;
            else if(y < 0)
                arr[1] = 0;
            else
                return arr[0];//di theo huong cua arr[0]
            
            //di theo huong cua arr[1] neu arr[0] = -1, khong thi ngau nhien
            return arr[0] == -1 ? arr[1] : arr[random.nextInt(2)];
        } else
            return random.nextInt(4);
	}

}
