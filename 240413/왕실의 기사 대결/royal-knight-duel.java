import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static int N,soldierCount,turnCount,totalDamage;
    public static int[][] map;
    public static Soldier[] soldiers;
    public static int Blank =0;
    public static int Wall = 2;
    public static int Trap =1;
    public static int[][] soldierMap;
    public static boolean[][] checked;
    public static boolean[] visitedSolder;
    public static int firstMoveSoldier;
    public static int[] rArr = {-1,0,1,0};

    public static int[] cArr = {0,1,0,-1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        soldierCount = Integer.parseInt(st.nextToken());
        turnCount = Integer.parseInt(st.nextToken());
        map = new int[N+2][N+2];
        soldierMap = new int[N+1][N+1];
        visitedSolder = new boolean[N+1];
        soldiers = new Soldier[soldierCount+1];
        inputMap(br);
        inputSoldiers(br);
//        showMap();
        for(int i=0;i<turnCount;i++){
//            showSoldierMap();
            visitedSolder = new boolean[N+1];
            st = new StringTokenizer(br.readLine());
            firstMoveSoldier = Integer.parseInt(st.nextToken());
            if(soldiers[firstMoveSoldier].health<=0) continue; //이미 사라진 솔져라면
            int position = Integer.parseInt(st.nextToken());
            boolean canMove = checkSoldierCanMove(firstMoveSoldier,position);
            checked = new boolean[N+1][N+1];
            if(canMove) moveSoldier(firstMoveSoldier,position);
            visitedSolder = new boolean[N+1];
        }
//        showSoldierMap();
        for(int i=1;i<=soldierCount;i++){
            if(soldiers[i].health<=0) continue;
            totalDamage+=( soldiers[i].firstHealth-soldiers[i].health);
        }
        System.out.println(totalDamage);
    }
    public static void healthChange(int soldierNumber){
        if(soldierNumber==firstMoveSoldier) return;
        Soldier soldier = soldiers[soldierNumber];
        for(int i= soldier.r;i<soldier.r+soldier.h;i++){
            for(int j= soldier.c;j<soldier.c+soldier.w;j++){
                if(map[i][j]==Trap){
//                    System.out.println(i+":"+j+"damaged..who = "+soldierNumber);
                    soldier.health--;
                }
            }
        }
        if(soldier.health<=0){
            removeSoldier(soldier);
        }
    }
    public static void removeSoldier(Soldier soldier){
        for(int i= soldier.r;i<soldier.r+soldier.h;i++){
            for(int j= soldier.c;j<soldier.c+soldier.w;j++){
                soldierMap[i][j] =0;
            }
        }
    }

    private static void showSoldierMap() {
        for(int r=1;r<=N;r++){
            for(int j=1;j<=N;j++){
                System.out.print(soldierMap[r][j]+" ");
            }
            System.out.println();
        }
        System.out.println("------------");
    }

    public static void moveSoldier(int soldierNumber,int position){
        Soldier soldier = soldiers[soldierNumber];
        for(int i= soldier.r;i<soldier.r+soldier.h;i++){
            for(int j= soldier.c;j<soldier.c+soldier.w;j++){
                int soldierMapStatus = soldierMap[i+rArr[position]][j+cArr[position]];
                if(soldierMapStatus !=Blank && soldierMapStatus != soldierNumber){//빈칸도 아니고 본인 기사 넘버도 아니라면 (다른 기사라면)
                    moveSoldier(soldierMapStatus,position);
                }
                soldierMap[i+rArr[position]][j+cArr[position]] = soldierNumber;
                checked[i+rArr[position]][j+cArr[position]] = true;
                if(checked[i][j]) continue;
                soldierMap[i][j] = Blank;
            }
        }
        soldier.r+=rArr[position];
        soldier.c+=cArr[position];
        healthChange(soldierNumber);
        visitedSolder[soldierNumber] =true;
    }
    public static boolean checkSoldierCanMove(int soldierNumber, int position){
        Soldier soldier = soldiers[soldierNumber];
        for(int i= soldier.r;i<soldier.r+soldier.h;i++){
            for(int j= soldier.c;j<soldier.c+soldier.w;j++){
                int soldierMapStatus = soldierMap[i+rArr[position]][j+cArr[position]];
                if(map[i+rArr[position]][j+cArr[position]] == Wall){
//                    System.out.println((i+rArr[position])+":"+(j+cArr[position]));
//                    System.out.println(soldierNumber+":soldierNumber");
                    return false;
                }
                //만약 같은 솔져번호이거나 빈칸이라면
                if(soldierMapStatus== soldierNumber || soldierMapStatus == Blank) continue;
                //만약 이미 밀린 솔져라면
                if(visitedSolder[soldierMapStatus]) continue;
                boolean canMove = checkSoldierCanMove(soldierMapStatus,position);
                if(!canMove) return false;
            }
        }
        visitedSolder[soldierNumber] =true;
        return true;
    }

    private static void inputMap(BufferedReader br) throws IOException {
        StringTokenizer st;
        for(int i = 1; i<=N; i++){
            st = new StringTokenizer(br.readLine());
            for(int j=1;j<=N;j++){
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        for(int i=0;i<=N+1;i++){
            for(int j=0;j<=N+1;j++){
                if(i==0 ||j==0 ||i==N+1||j==N+1) map[i][j] =Wall;
            }
        }

    }

    private static void showMap() {
        for(int i=0;i<=N+1;i++){
            for(int j=0;j<=N+1;j++){
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }
    }

    private static void inputSoldiers(BufferedReader br) throws IOException {
        StringTokenizer st;
        for(int i = 1; i<=soldierCount; i++){
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int health =Integer.parseInt(st.nextToken());
            soldiers[i]=  new Soldier(r,c,h,w,health);
            for(int x = r;x<r+h;x++){
                for(int y =c;y<c+w;y++) soldierMap[x][y] = i;
            }
        }
    }

    public static class Soldier{
        int r,c,h,w;
        int health;
        int firstHealth;


        public Soldier(int r, int c, int h, int w, int firstHealth) {
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
            this.firstHealth = firstHealth;
            this.health= firstHealth;
        }
    }
}