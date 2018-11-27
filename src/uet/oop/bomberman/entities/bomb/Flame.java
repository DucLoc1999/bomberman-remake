package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.sound.GameSound;

public class Flame extends Entity {

	protected Board _board;
	protected int _direction;
	private int _radius;
	protected int xOrigin, yOrigin;
	protected FlameSegment[] _flameSegments = new FlameSegment[0];

	/**
	 *
	 * @param x ho�nh ?? b?t ??u c?a Flame
	 * @param y tung ?? b?t ??u c?a Flame
	 * @param direction l� h??ng c?a Flame
	 * @param radius ?? d�i c?c ??i c?a Flame
	 */
	public Flame(int x, int y, int direction, int radius, Board board) {
		xOrigin = x;
		yOrigin = y;
		_x = x;
		_y = y;
		_direction = direction;
		_radius = radius;
		_board = board;
		createFlameSegments();
	}

	/**
	 * T?o c�c FlameSegment, m?i segment ?ng m?t ??n v? ?? d�i
	 */
	private void createFlameSegments() {
		/**
		 * t�nh to�n ?? d�i Flame, t??ng ?ng v?i s? l??ng segment
		 */
		_flameSegments = new FlameSegment[calculatePermitedDistance()];

		/**
		 * bi?n last d�ng ?? ?�nh d?u cho segment cu?i c�ng
		 */
		boolean last;

		// TODO: t?o c�c segment d??i ?�y
		for(int i = 0; i< _flameSegments.length; i++){
			switch (_direction) {
				case 0: yOrigin--; break;
				case 1: xOrigin++; break;
				case 2: yOrigin++; break;
				case 3: xOrigin--; break;
			}
			if(i ==_flameSegments.length - 1){
				_flameSegments[i] = new FlameSegment((int) xOrigin, (int) yOrigin, _direction,true);
			}
			else{
				_flameSegments[i] = new FlameSegment((int) xOrigin, (int) yOrigin, _direction,false);
			}

		}

	}

	/**
	 * T�nh to�n ?? d�i c?a Flame, n?u g?p v?t c?n l� Brick/Wall, ?? d�i s? b? c?t ng?n
	 * @return
	 */
	private int calculatePermitedDistance() {
		// TODO: th?c hi?n t�nh to�n ?? d�i c?a Flame
		int radius = 0;
		while (radius < _radius){
			if(_direction == 0) _y--;
			if(_direction == 1) _x++;
			if(_direction == 2) _y++;
			if(_direction == 3) _x--;

			Entity a = _board.getEntity((int) _x, (int) _y, null);
			if( a instanceof Character)
                ((Character) a).kill();
			if(a.collide(this))
                break;
			radius++;

		}
		return radius;
	}
	
	public FlameSegment flameSegmentAt(int x, int y) {
		for (int i = 0; i < _flameSegments.length; i++) {
			if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
				return _flameSegments[i];
		}
		return null;
	}

	@Override
	public void update() {}
	
	@Override
	public void render(Screen screen) {
		for (int i = 0; i < _flameSegments.length; i++) {
			_flameSegments[i].render(screen);
		}
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: x? l� va ch?m v?i Bomber, Enemy. Ch� � ??i t??ng n�y c� v? tr� ch�nh l� v? tr� c?a Bomb ?� n?
        if(e instanceof Character){
            return false;
        }
        
		return false;
	}
}
