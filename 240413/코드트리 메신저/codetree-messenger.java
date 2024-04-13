import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static int[] parent,indexTree;
    public static int[] authority;
    public static boolean[] alarm;
    public static int N,Q;
    public static int maxDepth =20;
    public static class Node{
        boolean alarmOff =false;
        int authority;
        Node leftNode;
        Node rightNode;
        int index;
        int parentNumber;
        int number;

        public Node(Node leftNode, Node rightNode) {
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }
    }
    public static Node[]  nodes;
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        nodes = new Node[N+1];
        for(int i=0;i<=N;i++){
            nodes[i] =  new Node(null,null);
        }


        int indexTreeCount =1;
        for(int i=1;i<=maxDepth;i++){
            indexTreeCount*=2;
        }
        indexTree = new int[indexTreeCount];
        for(int i=0;i<indexTreeCount;i++) indexTree[i] =-1;
        indexTree[1] =0;
        nodes[0].index = 1;

        for(int turn =0;turn<Q;turn++){
            st = new StringTokenizer(br.readLine());
            int operation = Integer.parseInt(st.nextToken());
            parent = new int[N+1];
            authority = new int[N+1];
            alarm = new boolean[N+1];
            if(operation==100){
                makeTree(st);
            }
            if(operation==200){
                int number = Integer.parseInt(st.nextToken());
                nodes[number].alarmOff = !nodes[number].alarmOff;
            }
            if(operation==300){
                int number = Integer.parseInt(st.nextToken());
                int power = Integer.parseInt(st.nextToken());
                nodes[number].authority = power;
            }
            if(operation==400){

                int leftNodeNumber = Integer.parseInt(st.nextToken());
                int rightNodeNumber = Integer.parseInt(st.nextToken());
                //부모 같다면 스킵
                if(nodes[leftNodeNumber].index/2 ==nodes[rightNodeNumber].index/2) continue;

                //부모 인덱스 구하기
                int leftNodeParentIndex = nodes[leftNodeNumber].index/2;
                int rightNodeParentIndex = nodes[rightNodeNumber].index/2;
                //부모 노드 구하기
                Node leftParent = nodes[indexTree[leftNodeParentIndex]];
                Node rightParent = nodes[indexTree[rightNodeParentIndex]];

                //인덱스에 따른 부모 번호 변경
                int tmpNodeNumber = indexTree[leftNodeParentIndex];
                indexTree[leftNodeParentIndex] = indexTree[rightNodeParentIndex];
                indexTree[rightNodeParentIndex] = tmpNodeNumber;

                //부모의 인덱스 변경
                leftParent.index = rightNodeParentIndex;
                rightParent.index = leftNodeParentIndex;

//                if(nodes[leftNodeNumber].parentNumber==nodes[rightNodeNumber].parentNumber) continue;
//
//                Node leftNode = nodes[leftNodeNumber];
//                Node rightNode =  nodes[rightNodeNumber];
//                Node leftNodeParent = nodes[leftNode.parentNumber];
//                Node rightNodeParent = nodes[rightNode.parentNumber];
//
//                //부모의 부모 바꾸기
//                int tmpGrandParent
//                //부모의 자식 바꾸기
//                Node tmpNode = leftNodeParent.leftNode;
//                leftNodeParent.leftNode = rightNodeParent.leftNode;
//                rightNodeParent.leftNode = tmpNode;
//
//                tmpNode = leftNodeParent.rightNode;
//                leftNodeParent.rightNode = rightNodeParent.rightNode;
//                rightNodeParent.rightNode = tmpNode;
//                //자식의 부모 바꾸기
//                changeChildsParent(leftNodeParent);
//                changeChildsParent(rightNodeParent);
            }
            if(operation==500){
//                showIndexTreeAndIndexOfNode();
                int number = Integer.parseInt(st.nextToken());
                System.out.println(getCount(number,0)-1);
            }
        }

    }
    public static int getCount(int number,int depth){
        Node node = nodes[number];
        int count = 0;
        int leftNumber = indexTree[node.index*2];
        int rightNumber = indexTree[node.index*2+1];
//        System.out.println(leftNumber+"/"+rightNumber);
        if(depth<=node.authority) count++;
        if(leftNumber !=-1){
            if(!nodes[leftNumber].alarmOff){
                count += getCount(indexTree[node.index*2],depth+1);
            }
        }
        if(rightNumber !=-1){
            if(!nodes[rightNumber].alarmOff){
                count += getCount(indexTree[node.index*2+1],depth+1);
            }
        }
        return count;

    }
    public static void changeChildsParent(Node node){
        if(node.leftNode!=null) node.leftNode.parentNumber = node.number;
        if(node.rightNode!=null) node.rightNode.parentNumber = node.number;
    }

    private static void makeTree(StringTokenizer st) {
        for(int i=1;i<=N;i++){
            int nowParent = Integer.parseInt(st.nextToken());
            parent[i] = nowParent;
            nodes[i].parentNumber = nowParent;
            nodes[i].number =i;
            if(nodes[nowParent].leftNode == null){
                nodes[nowParent].leftNode = nodes[i];
            }
            else{
                nodes[nowParent].rightNode = nodes[i];
            }
        }
        for(int i=1;i<=N;i++){
            int nowAuthority= Integer.parseInt(st.nextToken());
            authority[i] = nowAuthority;
            nodes[i].authority =nowAuthority;
        }
        makeIndex(1,0);

    }

    private static void showIndexTreeAndIndexOfNode() {
        for(int i=1;i<20;i++){
            System.out.print(indexTree[i]+" ");
        }
        System.out.println();
        System.out.println("--------------");
        for(int i=0;i<=N;i++){
            System.out.print(nodes[i].index+" ");
        }
        System.out.println();
        System.out.println("---------");
    }

    public static void makeIndex(int index,int number){
        Node node = nodes[number];
        if(node.leftNode!=null){
            indexTree[index*2] = node.leftNode.number;
            node.leftNode.index = index*2;
            makeIndex(index*2,node.leftNode.number);
        }
        if(node.rightNode!=null){
            indexTree[index*2+1] = node.rightNode.number;
            node.rightNode.index = index*2+1;
            makeIndex(index*2+1,node.rightNode.number);
        }
    }

}