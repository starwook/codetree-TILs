import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static int N,M,santaCount,rudolphPower,santaPower;
    public static Santa[] santas;
    public static int rudolphR,rudolphC;
    public static int RudolphNumber =-1;
    public static int nowTurn;
    public static int[] rArr = {-1,0,1,0,-1,-1,1,1};
    public static int[][] map;
    public static int[] cArr ={0,1,0,-1,1,-1,1,-1};


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        input(br, st);

        // 여기에 코드를 작성해주세요.
        for(int i=1;i<=M;i++){
            nowTurn =i;

//            System.out.println("turn:"+nowTurn);
            rudolphMove();
            santaMove();

            scoreOfForLivingSanta();

        }
        showSantaScore();
    }

    private static void showSantaScore() {
        for(int s =1;s<=santaCount;s++) System.out.print(santas[s].score+" ");
    }

    private static void showMapp() {
        for(int r=1;r<=N;r++){
            for(int c=1;c<=N;c++){
                System.out.print(map[r][c]+"(");
                if(map[r][c] ==-1 ||map[r][c]==0) System.out.print(-1);
                else System.out.print(santas[map[r][c]].faint);
                System.out.print(")"+" ");
            }
            System.out.println();
        }
        System.out.println("--------------");
    }

    private static void scoreOfForLivingSanta() {
        for(int santaNumber =1;santaNumber<=santaCount;santaNumber++){
            if(santas[santaNumber].out) continue;
            santas[santaNumber].score++;
        }
    }

    private static void santaMove() {
        for(int santaNumber =1;santaNumber<=santaCount;santaNumber++){
            Santa santa = santas[santaNumber];
//            System.out.println(santaNumber+"/"+santa.faint+"/"+nowTurn);
            if(santa.faint >= nowTurn) continue;
            santa.faint = 0;
            if(santa.out) {
                continue;
            }
            int firstDistance = getDistanceWithRudolph(santa.r,santa.c);
            int shortestDistance = firstDistance;
            int nextR =santa.r;
            int nextC =santa.c;
            int tmpPosition =0;
            for(int position=3;position>=0;position--){
                int tmpR = santa.r+rArr[position];
                int tmpC = santa.c+cArr[position];
                if(isOutOfMap(tmpR,tmpC)) continue;
                if(map[tmpR][tmpC] != 0 &&map[tmpR][tmpC] !=RudolphNumber) continue;
                int distanceWithRudolph = getDistanceWithRudolph(tmpR,tmpC);
                if(distanceWithRudolph >= firstDistance) continue;
                if(getDistanceWithRudolph(tmpR,tmpC)<=shortestDistance){
                    shortestDistance = getDistanceWithRudolph(tmpR,tmpC);
                    tmpPosition = getReversePosition(position);
                    nextR =tmpR;
                    nextC =tmpC;
                }
            }
//            System.out.println(santaNumber+":to:"+nextR+"/"+nextC);
            if(map[nextR][nextC] == RudolphNumber){
                santa.score += santaPower;
                santa.faint = nowTurn+1;
                santaGetBack(santa,tmpPosition,santaPower-1);
            }
            else if(map[nextR][nextC] ==0){
                map[santa.r][santa.c] =0;
                map[nextR][nextC] = santaNumber;
                santa.r = nextR;
                santa.c = nextC;
            }
        }
    }
    public static void santaGetBack(Santa santa, int position,int power){
        int nextR = santa.r +rArr[position]*power;
        int nextC = santa.c + cArr[position]*power;
        if(isOutOfMap(nextR,nextC)){
            santa.out =true;
            map[santa.r][santa.c] = 0;
            return;
        }
        if(map[nextR][nextC]!=0){
            int nextSantaNumber = map[nextR][nextC];
            santaGetBack(santas[nextSantaNumber],position,1);
            //그 자리에 산타가 있다면
        }

        map[nextR][nextC] = santa.number;
        map[santa.r][santa.c] = 0;
        santa.r = nextR;
        santa.c = nextC;
    }

    private static int getReversePosition(int tmpPosition) {
        if(tmpPosition ==0) return 2;
        if(tmpPosition ==2) return 0;
        if(tmpPosition ==1) return 3;
        else  return 1;

    }

    private static void rudolphMove() {
        int shortestDistance =100000;
        Santa selectedSanta= null;
        for(int s =1;s<=santaCount;s++){
            Santa santa = santas[s];
            if(santa.out) continue;
            int tmpDistance = getDistanceWithSanta(rudolphR,rudolphC,santa);
            if(tmpDistance <shortestDistance){
                shortestDistance = tmpDistance;
                selectedSanta = santa;
            }
            else if(tmpDistance==shortestDistance){
                if(santa.r>selectedSanta.r){
                    selectedSanta = santa;
                }
                else if(santa.r== selectedSanta.r){
                    if(santa.c> selectedSanta.c) selectedSanta = santa;
                }
            }
        }
        shortestDistance =100000;
        int nextR =0;
        int nextC =0;
        int selectedPosition =0;
        if(selectedSanta==null){
            return;
        }
        for(int position=0;position<8;position++){
            int tmpR = rudolphR+rArr[position];
            int tmpC = rudolphC+cArr[position];
            if(isOutOfMap(tmpR,tmpC)) {
                continue;
            }
            if(getDistanceWithSanta(tmpR,tmpC,selectedSanta)<shortestDistance){
                shortestDistance = getDistanceWithSanta(tmpR,tmpC,selectedSanta);
                nextR = tmpR;
                nextC = tmpC;
                selectedPosition = position;
            }
        }
//        System.out.println(selectedSanta.number+":selectedSantaNumber, selectedPostion:"+selectedPosition);
        if(map[nextR][nextC]==0){// 아무도 없다면
            map[nextR][nextC]= RudolphNumber;
            map[rudolphR][rudolphC] = 0;
            rudolphR = nextR;
            rudolphC = nextC;
        }
        else{
            int originalSanta = map[nextR][nextC];
            santas[originalSanta].faint = nowTurn+1;
            santas[originalSanta].score += rudolphPower;
            santaGetBack(santas[originalSanta], selectedPosition,rudolphPower);
            map[nextR][nextC]= RudolphNumber;
            map[rudolphR][rudolphC] = 0;
            rudolphR = nextR;
            rudolphC = nextC;
        }
    }
    public static boolean isOutOfMap(int r, int c){
        if(r<1||c<1||r>N||c>N) return true;
        return false;
    }


    public static int getDistanceWithSanta(int r, int c, Santa santa){
        return (int)(Math.pow(r-santa.r,2)+Math.pow(c-santa.c,2));
    }
    public static int getDistanceWithRudolph(int r,int c){
        return (int)(Math.pow(r-rudolphR,2)+Math.pow(c-rudolphC,2));
    }

    private static void input(BufferedReader br, StringTokenizer st) throws IOException {
        N = Integer.parseInt(st.nextToken());
        map = new int[N+1][N+1];
        M = Integer.parseInt(st.nextToken());
        santaCount = Integer.parseInt(st.nextToken());
        rudolphPower =Integer.parseInt(st.nextToken());
        santaPower = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        rudolphR = Integer.parseInt(st.nextToken());
        rudolphC = Integer.parseInt(st.nextToken());
        map[rudolphR][rudolphC] = RudolphNumber;
        santas = new Santa[santaCount+1];
        for(int i=1;i<=santaCount;i++){
            st = new StringTokenizer(br.readLine());
            int santaNumber = Integer.parseInt(st.nextToken());
            int santaR = Integer.parseInt(st.nextToken());
            int santaC = Integer.parseInt(st.nextToken());
            santas[santaNumber] =  new Santa(santaR,santaC,santaNumber,0);
            map[santaR][santaC] =santaNumber;
        }

    }

    public static class Santa{
        int r,c,number,score,faint;
        boolean out;

        public Santa(int r, int c, int number,int faintTurn) {
            this.r = r;
            this.c = c;
            this.number = number;
            this.faint = faintTurn;
        }
    }
}