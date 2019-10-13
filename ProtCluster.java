import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class ProtCluster{
    private List<Prot> protList;
    private Color color;

    public ProtCluster() {
        protList = new ArrayList<Prot>();
    }
      
    public ProtCluster(List<Prot> prots) {
        protList = prots;
    }

    public void add(Prot p) {
        protList.add(p);
    }

    public List<Prot> getProtList() {    
        return protList;
    }

    public Color getColor(){
        return color;
    }
    
    public void setColor(Color c){
        this.color = c;
    }
    
}