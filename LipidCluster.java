import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class LipidCluster{
    private List<Lipid> lipidList;
    private Color color;

    public LipidCluster() {
        lipidList = new ArrayList<Lipid>();
    }
      
    public LipidCluster(List<Lipid> lipids) {
        lipidList = lipids;
    }

    public void add(Lipid l) {
        lipidList.add(l);
    }

    public List<Lipid> getLipidList() {    
        return lipidList;
    }

    public Color getColor(){
        return color;
    }
    
    public void setColor(Color c){
        this.color = c;
    }
    
}