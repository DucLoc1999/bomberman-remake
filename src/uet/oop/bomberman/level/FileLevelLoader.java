package uet.oop.bomberman.level;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloom;
import uet.oop.bomberman.entities.character.enemy.Doll;
import uet.oop.bomberman.entities.character.enemy.Kondoria;
import uet.oop.bomberman.entities.character.enemy.Minvo;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

public class FileLevelLoader extends LevelLoader {

	/**
	 * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đ�?c được
	 * từ ma trận bản đồ trong tệp cấu hình
	 */
	private static char[][] _map;
	
	public FileLevelLoader(Board board, int level) throws LoadLevelException {
		super(board, level);
	}
	
	@Override
	public void loadLevel(int level) {
        System.out.println("uet.oop.bomberman.level.FileLevelLoader");
        String path = "/levels/Level"+level+".txt";
        try{
            URL absPath = FileLevelLoader.class.getResource(path);
            InputStream is = absPath.openStream();
            //FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            
            String line = reader.readLine();
            String[] s = line.split(" ");
        System.out.println(line);
            
            _level = Integer.valueOf(s[0]);
            _height = Integer.valueOf(s[1]);
            _width = Integer.valueOf(s[2]);
            _map = new char[_height][_width];
            for (int i = 0; i < _height; i++) {
                line = reader.readLine();
                for (int j = 0; j < _width; j++){
                    _map[i][j] = line.charAt(j);
                    System.out.print(_map[i][j]);
                }
                System.out.println("");
            }
            reader.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
	}

	@Override
	public void createEntities() {
		for (int x = 0; x < _width; x++) {
			for (int y = 0; y < _height; y++) {
                switch (_map[y][x]){
                
          		// th�m Wall 
                case '#':
                    _board.addEntity(x + y * _width, new Wall(x, y, Sprite.wall));
                    break;
                // th�m Bomber
                case 'p':
                    int xBomber = 1, yBomber = 1;
                    _board.addCharacter( new Bomber(Coordinates.tileToPixel(xBomber), Coordinates.tileToPixel(yBomber) + Game.TILES_SIZE, _board) );
                    Screen.setOffset(0, 0);
                    _board.addEntity(xBomber + yBomber * _width, new Grass(xBomber, yBomber, Sprite.grass));
                    break;
                // th�m Enemy
                case '1'://Balloom
                    _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                    _board.addCharacter( new Balloom(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                    break;
                case '2'://Oneal
                    _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                    _board.addCharacter( new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                    break;
                case '3'://Doll
                    _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                    _board.addCharacter( new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                    break;
                case '4'://Minvo
                    _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                    _board.addCharacter( new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                    break;
                case '5'://Kondoria
                    _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                    _board.addCharacter( new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                    break;
           		// th�m Brick
                case '*':
                    _board.addEntity(x + y * _width,
                        new LayeredEntity(x, y,
                            new Grass(x, y, Sprite.grass),
                            new Brick(x, y, Sprite.brick)
                            )
                    );
                    break;
                // th�m portal
                case 'x':
                    _board.addEntity(x + y * _width,
                        new LayeredEntity(x, y,
                            new Grass(x ,y, Sprite.grass),
                            new Portal(x, y, Sprite.portal),
                            new Brick(x, y, Sprite.brick)
                        )
                    );
                    break;
                // th�m Item k�m Brick che ph? ? tr�n
                case 'f'://powerup_flames
                    _board.addEntity(x + y * _width,
                        new LayeredEntity(x, y,
                            new Grass(x ,y, Sprite.grass),
                            new FlameItem(x, y, Sprite.powerup_flames),
                            new Brick(x, y, Sprite.brick)
                        )
                    );
                    break;
               case 'b'://powerup_bombs
                    _board.addEntity(x + y * _width,
                        new LayeredEntity(x, y,
                            new Grass(x ,y, Sprite.grass),
                            new BombItem(x, y, Sprite.powerup_bombs),
                            new Brick(x, y, Sprite.brick)
                        )
                    );
                    break;
                case 's'://powerup_speed
                    _board.addEntity(x + y * _width,
                        new LayeredEntity(x, y,
                            new Grass(x ,y, Sprite.grass),
                            new SpeedItem(x, y, Sprite.powerup_speed),
                            new Brick(x, y, Sprite.brick)
                        )
                    );
                    break;
                //th�m grass
                default:
                    _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                    break;

                }
			}
		}
	}

}
