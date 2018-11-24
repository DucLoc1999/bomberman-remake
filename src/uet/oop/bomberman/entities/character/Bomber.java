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
import uet.oop.bomberman.level.Coordinates;

public class Bomber extends Character {

    private List<Bomb> _bombs;
    protected Keyboard _input;
    private KeyEvent e ;

    /**
     * n·∫øu gi√° tr·ªã n√†y < 0 th√¨ cho ph√©p ƒë·∫∑t ƒë·ªëi t∆∞·ª£ng Bomb ti·∫øp theo,
     * c·ª© m·ªói l·∫ßn ƒë·∫∑t 1 Bomb m·ªõi, gi√° tr·ªã n√†y s·∫Ω ƒë∆∞·ª£c reset v·ª? 0 v√† gi·∫£m d·∫ßn trong m·ªói l·∫ßn update()
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
     * Ki·ªÉm tra xem c√≥ ƒë·∫∑t ƒë∆∞·ª£c bom hay kh√¥ng? n·∫øu c√≥ th√¨ ƒë·∫∑t bom t·∫°i v·ªã tr√≠ hi·ªán t·∫°i c·ªßa Bomber
     */
    private void detectPlaceBomb() {
        // TODO: ki?m tra xem phÌm ?i?u khi?n ??t bom cÛ ???c gı v‡ gi· tr? _timeBetweenPutBombs, Game.getBombRate() cÛ th?a m„n hay khÙng
        if(_input.space && Game.getBombRate()>0 && _timeBetweenPutBombs < 0){
            int x = Coordinates.pixelToTile(_x + _sprite.getSize()/2);
            int y = Coordinates.pixelToTile(_y - _sprite.getSize()/2 );

            placeBomb(x,y);
            Game.addBombRate(-1);
            _timeBetweenPutBombs = 30;
        }
        // TODO:  Game.getBombRate() s? tr? v? s? l??ng bom cÛ th? ??t liÍn ti?p t?i th?i ?i?m hi?n t?i
        // TODO: _timeBetweenPutBombs d˘ng ?? ng?n ch?n Bomber ??t 2 Bomb c˘ng t?i 1 v? trÌ trong 1 kho?ng th?i gian qu· ng?n
        // TODO: n?u 3 ?i?u ki?n trÍn th?a m„n thÏ th?c hi?n ??t bom b?ng placeBomb()
        // TODO: sau khi ??t, nh? gi?m s? l??ng Bomb Rate v‡ reset _timeBetweenPutBombs v? 0
    }

    protected void placeBomb(int x, int y) {
        // TODO: th?c hi?n t?o ??i t??ng bom, ??t v‡o v? trÌ (x, y)
        Bomb bomb = new Bomb(x,y,_board);
        _board.addBomb(bomb);

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
        //l?y size bomber l‡ x = 11 [0->10], y = 10 [5->15]
        double pos_x = _x + x;
        double pos_y = _y - Game.TILES_SIZE + y;
        
			Entity a = _board.getEntity((pos_x)/Game.TILES_SIZE, (pos_y+5)/Game.TILES_SIZE, this);//gÛc trÍn tr·i
			if(a.collide(this))
				return false;
            
            a = _board.getEntity((pos_x+10)/Game.TILES_SIZE, (pos_y+5)/Game.TILES_SIZE, this);//gÛc trÍn ph?i
			if(a.collide(this))
				return false;
            
            a = _board.getEntity((pos_x)/Game.TILES_SIZE, (pos_y+15)/Game.TILES_SIZE, this);//gÛc d??i tr·i
			if(a.collide(this))
				return false;
            
            a = _board.getEntity((pos_x+10)/Game.TILES_SIZE, (pos_y+15)/Game.TILES_SIZE, this);//gÛc d??i ph?i
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
