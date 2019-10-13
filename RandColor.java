import java.util.*; 

public class RandColor {
    List<Double> rand = new ArrayList<Double>();
    int protNum = 257;
    int lipidNum = 19093;
    public List<Double> pColor (){
        for(int i=0; i<3*protNum; i++){
            rand.add(Math.random());
        }
        return rand;
    }

    public List<Double> lColor (){
        for(int i=0; i<3*lipidNum; i++){
            rand.add(Math.random());
        }
        return rand;
    }
}