
package uet.oop.bomberman.entities.character.enemy.ai;

import static java.lang.Math.abs;
import java.util.Vector;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;

public class AIHigh extends AI{
	Bomber _bomber;
	Enemy _e;
	double _range;
    Vector<int[]> spreadList;
    
    protected Board _board;
    public static int w = 0;
    public static int h = 0;
    int[][] matrix;
    
    //update matrix
    public void updateMatrix(){
        if(w != _board.getWidth() || h!= _board.getHeight() || matrix == null){
            w = _board.getWidth();
            h = _board.getHeight();
            matrix = new int[h][w];
        }
        
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int mark = 0;
                Entity e = _board.getEntity(j, i, _e);//x,y
                
                if(e instanceof Wall || e instanceof Portal || e instanceof Bomb)
                    mark = -1;
                else if(e instanceof LayeredEntity){
                    e = ((LayeredEntity) e).getTopEntity();
                    if(e instanceof Brick)
                        mark = -1;
                }
                
                matrix[i][j] = mark;
            }
        }
    }
    
    //return {0,1,2,3, 1|2} direction in order for move and num of best move 1|2
    int[] direct(int x1, int y1, int x2, int y2){
        int a = -1,b = -1;
        int[] arr = new int[5];
        if(x2 - x1 > 0) a = 1;
        if(x2 - x1 < 0) a = 3;
        if(y2 - y1 > 0) b = 2;
        if(y2 - y1 < 0) b = 0;
        
        if(a == -1 || b == -1){
            arr[0] = a == -1 ? b : a;
            arr[1] = (1+arr[0]) % 4;
            arr[2] = (3+arr[0]) % 4;
            arr[3] = (2+arr[0]) % 4;
            arr[4] = 1;
            return arr;
        } else{
            if(abs(x2 - x1) >= abs(y2 - y1)){
                arr[0] = a;
                arr[1] = b;
            } else {
                arr[0] = b;
                arr[1] = a;
            }
            arr[2] = (2+arr[1]) % 4;
            arr[3] = (2+arr[0]) % 4;
            arr[4] = 2;
            return arr;
        }
    }
    
    //get postion from direction
    int[] nextMove(int x, int y, int dir){//convent dir into postion
        int[] xy = {x,y};
        if(dir == 0) xy[1]--;
        if(dir == 1) xy[0]++;
        if(dir == 2) xy[1]++;
        if(dir == 3) xy[0]--;
        return xy;
    }
    
    //{x,y,0} neu khong thay duong thang den, {x,y,1} neu gap 1 duong da di
    int[] move(int x1, int y1, int x2, int y2){
        //di tu 1 -> 2 tim duong tu 2 ve 1
        int[] dirList;
        int[] xy = {x1, y1};//{x,y,can't move 0 or have gone through 1}
        int type = 0;
        
        
        //////loop
        boolean canMove;
        do{
            if(matrix[xy[1]][xy[0]] == 0)//da di
                matrix[xy[1]][xy[0]] = 1;
            
            canMove = false;
            dirList = direct(xy[0], xy[1], x2, y2);
            int[] test;
            for(int i=0; i < dirList[4]; i++){
                test = nextMove(xy[0], xy[1], dirList[i]);//get pos from dir

                if(test[0]<0 || test[1]<0 || test[0]>=w ||test[1]>=h)//kt co ra ngoai khong
                    continue;

                if(matrix[test[1]][test[0]] != -1 && abs(test[0]-x2) + abs(test[1]-y2) <= 1){//ve dich
                    xy = test;
                    matrix[xy[1]][xy[0]] = 5;
                    break;
                }

                if(matrix[test[1]][test[0]] == 3) //da di di huong khac
                    continue;
                
                if(matrix[test[1]][test[0]] == 1){ //da di khong di nua
                    type = 1;
                    break;
                }

                if(matrix[test[1]][test[0]] == 0){ //chua di va cung khong vuong
                    xy = test;
                    canMove = true;
                    break;
                }
            }
        }while(canMove);
        //khong di dc
        int[] result = new int[3];
        result[0] = xy[0];
        result[1] = xy[1];
        result[2] = type;
        ////
        return result;
    }
        
    //return {-1} to out if spreadList empty
    int[] spread(int x1, int y1, int x2, int y2){
    //spread tu x, y -> dx, dy
        int[] xy = {x1, y1};
        
        spreadList = new Vector<int[]>();
        
        if(abs(xy[0]-x2)+abs(xy[1]-y2) > 1)
            spreadList.add(xy);
        
        while(abs(xy[0]-x2)+abs(xy[1]-y2) > 1 && spreadList.size() <= _range){//chua tim dc va size < 15
            if(spreadList.isEmpty()){//kt spredList
                int[] fail = {-1};
                return fail;//thoat va khong co loi di
            }

            xy = spreadList.firstElement();//lay dau list
            xy = move(xy[0], xy[1], x2, y2);
            
            if(xy[2] == 0){//bi can
                matrix[xy[1]][xy[0]] = 3;
                if(xy[0]>0 && (matrix[xy[1]][xy[0]-1] == 1 || matrix[xy[1]][xy[0]-1] == 0)){
                    int[] point = {xy[0]-1, xy[1]};
                    spreadList.add(point);
                }
                if(xy[0]<w-1 && (matrix[xy[1]][xy[0]+1] == 1 || matrix[xy[1]][xy[0]+1] == 0)){
                    int[] point = {xy[0]+1, xy[1]};
                    spreadList.add(point);
                }
                if(xy[1]>0 && (matrix[xy[1]-1][xy[0]] == 1 || matrix[xy[1]-1][xy[0]] == 0)){
                    int[] point = {xy[0], xy[1]-1};
                    spreadList.add(point);
                }
                if(xy[1]<h-1 && (matrix[xy[1]+1][xy[0]] == 1 || matrix[xy[1]+1][xy[0]] == 0)){
                    int[] point = {xy[0], xy[1]+1};
                    spreadList.add(point);
                }

            }//else{// == 1  //gap duong da di                    
            //}
            spreadList.remove(0);
        }
        
        
        if(abs(xy[0]-x2)+abs(xy[1]-y2) <= 1)
            return xy;
        else{
            int[] fail = {-1};
            return fail;//thoat va khong co loi di hoac qua xa
        }
    }
    
	public AIHigh(Bomber bomber, Enemy e, double range, Board board) {
		_bomber = bomber;
		_e = e;
        _range = range;//special it is the maximun of different way it have to try.
        _board = board;
	}

	@Override
    public int calculateDirection(){
        updateMatrix();
                
        int x1 = _e.getXTile();
        int y1 = _e.getYTile();
        int x2 = _bomber.getXTile();
        int y2 = _bomber.getYTile();

        {// neu chi co 1 huong
            int numOfDir = 0;
            int oneDir = 0;
            for(int i = 0; i < 4; i++){
                int[]pos = nextMove(x1, y1, i);
                if(matrix[pos[1]][pos[0]] == 0){
                    numOfDir++;
                    oneDir = i;
                }
                if(numOfDir >= 2)
                    break;
            }
            if(numOfDir == 1)
                return oneDir;
        }
        int[] pos = spread(x2, y2, x1, y1);//tìm t? bomber v? enemy
        if(pos[0] != -1){
            int[] dir = direct(x1, y1, pos[0], pos[1]);
            return  direct(x1, y1, pos[0], pos[1])[0];
        }else
            return random.nextInt(4);
    }
    
}