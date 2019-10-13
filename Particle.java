import java.io.*;
import java.util.*; 

public class Particle {
    private List<Float> lipidX = new ArrayList<Float>();
    private List<Float> lipidY = new ArrayList<Float>();
    private List<Float> lipidZ = new ArrayList<Float>();
    private List<Float> protX = new ArrayList<Float>();
    private List<Float> protY = new ArrayList<Float>();
    private List<Float> protZ = new ArrayList<Float>();
    private double xPos, yPos;
    

    public List<Float> getLipidX(List<Float> list) {     
        for (int i = 0; i < list.size(); i+=3) {
            lipidX.add(list.get(i));
       }
       return lipidX;
    }

    public List<Float> getLipidY(List<Float> list) {
        for (int i = 1; i < list.size(); i+=3) {
            lipidY.add(list.get(i));
       }
       return lipidY;
    }

    public List<Float> getLipidZ(List<Float> list) {     
        for (int i = 2; i < list.size(); i+=3) {
            lipidX.add(list.get(i));
       }
       return lipidZ;
    }

    public List<Float> getProtX(List<Float> list) {     
        for (int i = 0; i < list.size(); i+=3) {
            protX.add(list.get(i));
       }
       return protX;
    }

    public List<Float> getProtY(List<Float> list) {     
        for (int i = 1; i < list.size(); i+=3) {
            protY.add(list.get(i));
       }
       return protY;
    }

    public List<Float> getProtZ(List<Float> list) {     
        for (int i = 2; i < list.size(); i+=3) {
            protY.add(list.get(i));
       }
       return protZ;
    }

    public double getXPos(){
        return xPos;
    }

    public void setXPos(double x){
        this.xPos = x;
    }

    public double getYPos(){
        return yPos;
    }

    public void setYPos(double y){
        this.yPos = y;
    }

}