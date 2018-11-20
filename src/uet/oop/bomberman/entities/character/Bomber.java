package uet.oop.bomberman.entities.character;

import java.awt.Color;
import java.awt.event.KeyEvent;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;

import java.util.Iterator;
import java.util.List;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Wall;

public class Bomber extends Character {

    private List<Bomb> _bombs;
    protected Keyboard _input;
    private KeyEvent e ;

    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset v�? 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        _sprite = Sprite.player_right;
    }

    

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        // TODO: kiểm tra xem phím đi�?u khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có th�?a mãn hay không
        // TODO:  Game.getBombRate() sẽ trả v�? số lượng bom có thể đặt liên tiếp tại th�?i điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng th�?i gian quá ngắn
        // TODO: nếu 3 đi�?u kiện trên th�?a mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs v�? 0
    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
       if(!_alive) return;
	_alive = false;		
	Message msg = new Message("-1 LIVE", getXMessage(), getYMessage(), 2, Color.white, 14);
	_board.addMessage(msg);
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            _board.endGame();
        }
    }

    @Override
    protected void calculateMove() {
       
        int xa = 0, ya = 0;
		if(_input.up) ya--;
		if(_input.down) ya++;
		if(_input.left) xa--;
		if(_input.right) xa++;
		
		if(xa != 0 || ya != 0)  {
			move(xa * Game.getBomberSpeed(), ya * Game.getBomberSpeed());
			_moving = true;
		} else {
			_moving = false;
		}
    }

    @Override
    public boolean canMove(double x, double y) {
        //l?y size bomber l� x = 11 [0->10], y = 11 [5->15]
        double pos_x = _x + x;
        double pos_y = _y - Game.TILES_SIZE + y;
        
			Entity a = _board.getEntity((pos_x)/Game.TILES_SIZE, (pos_y+5)/Game.TILES_SIZE, this);//g�c tr�n tr�i
			if(a.collide(this))
				return false;
            
            a = _board.getEntity((pos_x+10)/Game.TILES_SIZE, (pos_y+5)/Game.TILES_SIZE, this);//g�c tr�n ph?i
			if(a.collide(this))
				return false;
            
            a = _board.getEntity((pos_x)/Game.TILES_SIZE, (pos_y+15)/Game.TILES_SIZE, this);//g�c d??i tr�i
			if(a.collide(this))
				return false;
            
            a = _board.getEntity((pos_x+10)/Game.TILES_SIZE, (pos_y+15)/Game.TILES_SIZE, this);//g�c d??i ph?i
			if(a.collide(this))
				return false;
            
		return true;
    }

    @Override
    public void move(double xa, double ya) {
        
        if(xa > 0)
            _direction = 1;
        else if(xa < 0)
            _direction = 3;
        
        if(ya > 0)
            _direction = 2;
        else if(ya > 0)
            _direction = 0;
        
        if(canMove(0, ya)) {
			_y += ya;
       		if(ya > 0) _direction = 2;
            if(ya < 0) _direction = 0;
		}
		
		if(canMove(xa, 0)) {
			_x += xa;
            if(xa > 0) _direction = 1;
            if(xa < 0) _direction = 3;
		}
    }

    @Override
    public boolean collide(Entity e) {
        if(e instanceof Flame) {
			this.kill();
			return false;
        }
        
		if(e instanceof Enemy) {
			this.kill();
			return false;
		}
    
        return false;
    }

    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }
}
