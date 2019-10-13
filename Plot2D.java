
import java.io.*;
import java.util.*; 
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
/**
 * This program is to plot lipids and proteins 
 *j
 * Author: Cheng Qian
 * Last modified: September 2019
 */

public class Plot2D {
    private float scale = 4;                // the scale for you to zoom in or out
    private int nextParti = 51;            // the gap between one particle and the next one 
    private int nextLipid = 13*51;
    private int nextProt = 344*51;
    private Color randColor;
    private List<Double> lRand = new ArrayList<Double>();
    private List<Double> pRand = new ArrayList<Double>();
    
    public void drawProts(GraphicsContext gc,List<Float> protX,
                            List<Float> protY,int frame) throws Exception{ 
        ProtFetcher pf = new ProtFetcher();
        List<ProtCluster> clus = pf.findProtClusters(frame, protX, protY);
        for(ProtCluster pc : clus) {        
            randColor = Color.color(Math.random(),Math.random(),Math.random());
            pc.setColor(randColor);
            List<Prot> proList= pc.getProtList();
            System.out.println(proList.size());  
            for(Prot prot : proList) {
                List<Particle> parList = prot.getAtoms();
                for(Particle par:parList){
                    double x = par.getXPos();
                    double y = par.getYPos();
                    gc.beginPath();
                    gc.moveTo(x,y); 
                    gc.lineTo(x,y);
                    gc.setStroke(pc.getColor());
                    gc.stroke();
                }
            }
        }      
    }

    public void drawLipids(GraphicsContext gc,List<Float> lipidX,
                            List<Float> lipidY,int frame) throws Exception{ 
        LipidFetcher lf = new LipidFetcher();
        List<LipidCluster> clus = lf.findLipidClusters(frame, lipidX, lipidY);
        for(LipidCluster lc : clus) {        
            randColor = Color.color(Math.random(),Math.random(),Math.random());
            lc.setColor(randColor);
            List<Lipid> lipList= lc.getLipidList();
            System.out.println(lipList.size());  
            for(Lipid lip : lipList) {
                List<Particle> parList = lip.getAtoms();
                for(Particle par:parList){
                double x = par.getXPos();
                double y = par.getYPos();
                gc.beginPath();
                gc.moveTo(x,y); 
                gc.lineTo(x,y);
                gc.setStroke(lc.getColor());
                gc.stroke();
                }
            }
        }      
    }


    public void protAni(GraphicsContext gc,List<Float> protX,List<Float> protY,int frame) throws Exception{ 
        for (int i = frame-1; i < protX.size(); i+=51) {  
            float x = scale * protX.get(i);
            float y = scale * protY.get(i);
            gc.beginPath();
            gc.moveTo(x,y);
            gc.lineTo(x,y);
            gc.stroke();          
        }
    }

    public void protColorAni(GraphicsContext gc,List<Float> protX,List<Float> protY,int frame) throws Exception{ 
        RandColor randP = new RandColor();
        pRand = randP.pColor();
        int j = 0;
        for (int i = frame-1; i < protX.size(); i+=51) {  
            float x = scale * protX.get(i);
            float y = scale * protY.get(i);
            gc.beginPath();
            gc.moveTo(x,y);
            gc.lineTo(x,y);
            if (i % nextProt == frame-1) {
                randColor = Color.color(pRand.get(j),pRand.get(j+1),pRand.get(j+2));
                j+=3;
            }
            gc.setStroke(randColor);
            gc.stroke();          
        }
    }

    public void lipidAni(GraphicsContext gc,List<Float> lipidX,List<Float> lipidY,int frame) throws Exception{ 
        for (int i = frame-1; i < lipidX.size(); i+=51) {  
            float x = scale * lipidX.get(i);
            float y = scale * lipidY.get(i);
            gc.beginPath();
            gc.moveTo(x,y);
            gc.lineTo(x,y);
            gc.stroke();          
        }
    }

    public void lipidColorAni(GraphicsContext gc,List<Float> lipidX,List<Float> lipidY,int frame) throws Exception{ 
        RandColor randL = new RandColor();
        lRand = randL.lColor();
        int j = 0;
        for (int i = frame-1; i < lipidX.size(); i+=51) {  
            float x = scale * lipidX.get(i);
            float y = scale * lipidY.get(i);
            gc.beginPath();
            gc.moveTo(x,y);
            gc.lineTo(x,y);
            if (i % nextLipid == frame-1) {  
                randColor = Color.color(lRand.get(j),lRand.get(j+1),lRand.get(j+2));
                j+=3;            
            }
            gc.setStroke(randColor);
            gc.stroke();          
        }
    }
}