import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.*; 


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextAlignment;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.SequentialTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;


/**
 * This program creates a main GUI for interactive data visualization
 *
 * Author: Cheng Qian
 * Last modified: September 2019
 */

public class Main extends Application {

    private final Desktop desktop = Desktop.getDesktop();
    private int timelinespeed;
    private int frameNumber = 0;;
    private Canvas canvas = new Canvas(600, 500);
    private Slider frameSlider = new Slider(1,51,1);
    private static AnimationTimer time;
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private Stage stage = new Stage();
    
    private File file;
    private FileChooser fileChooser = new FileChooser();

    private List<Float> lipid = new ArrayList<Float>();
    private List<Float> lipidX = new ArrayList<Float>();
    private List<Float> lipidY = new ArrayList<Float>();
    private List<Float> lipidZ = new ArrayList<Float>();

    private List<Float> prot = new ArrayList<Float>();
    private List<Float> protX = new ArrayList<Float>();
    private List<Float> protY = new ArrayList<Float>();
    private List<Float> protZ = new ArrayList<Float>();
    private List<Integer> type = new ArrayList<Integer>();

    private Plot2D plot = new Plot2D();
    private Particle p = new Particle();
    
    

    @Override
    public void start(Stage stage) {

        initUI(stage);
    }

    private void initUI(Stage stage) {
        HBox menu = new HBox(); 
        menu.getChildren().add(initMenuBar(stage));
        Scene scene = new Scene(menu, 300, 300);
        stage.setTitle("MenuBar");
        stage.setScene(scene);
        stage.show();
    }

    private HBox initMenuBar(Stage stage) {
        HBox topMenu = new HBox();
        MenuBar mbar = new MenuBar();
        mbar.prefWidthProperty().bind(stage.widthProperty());
        Menu fileMenu = new Menu("File");
        Image icon_folder = new Image("folder.png");
        fileMenu.setGraphic(new ImageView(icon_folder));
            MenuItem loadLipid = new MenuItem("Load Lipid");
                loadLipid.setOnAction ((ActionEvent event) -> {         
                    file= fileChooser.showOpenDialog(stage); 
                    ReadBin rb = new ReadBin();
                    lipid = rb.dataAsList(file);
                    lipidX = p.getLipidX(lipid); 
                    lipidY = p.getLipidY(lipid);
                    lipidZ = p.getLipidY(lipid);
                });  
            MenuItem loadProt = new MenuItem("Load Protein");
                loadProt.setOnAction ((ActionEvent event) -> {
                    file= fileChooser.showOpenDialog(stage); 
                    ReadBin rb = new ReadBin();
                    prot = rb.dataAsList(file);
                    protX = p.getProtX(prot);
                    protY = p.getProtY(prot);
                    protZ = p.getProtY(prot);
                }); 

            MenuItem saveMI = new MenuItem("Save");
                saveMI.setOnAction(new MyMenuHandler());
            MenuItem exitMI = new MenuItem("Exit");
                exitMI.setOnAction((ActionEvent event) -> {
                    Platform.exit();
                });

        Menu visMenu = new Menu("Visualization");
        Image icon_vis = new Image("vis.png");
        visMenu.setGraphic(new ImageView(icon_vis));
            MenuItem lipidMI = new MenuItem("Vis Lipid");
                lipidMI.setOnAction (new LipidMI());
            MenuItem protMI = new MenuItem("Vis Protein");
                protMI.setOnAction (new ProtMI());
        fileMenu.getItems().addAll(loadLipid,loadProt,saveMI,new SeparatorMenuItem(),exitMI);
        visMenu.getItems().addAll(lipidMI,protMI);
        mbar.getMenus().addAll(fileMenu,visMenu);      
        topMenu.getChildren().add(mbar);

        return topMenu;
    }

    private GridPane initTimeline(Stage stage) {

        GridPane timeline = new GridPane();            
            Button b_play = new Button();
            Button b_pause = new Button();
            Button b_bskip = new Button();
            Button b_fskip = new Button();
            timeline.setVgap(5);
            AnimationTimer timer = new MyTimer();
            
            Image icon_play = new Image("play.png");
            Image icon_pause = new Image("pause.png");
            Image icon_skip = new Image("skip.png");
            Image icon_speed = new Image("fastf.png");

            b_play.setGraphic(new ImageView(icon_play));
            b_play.setOnAction((ActionEvent playEvent) -> {
                if (frameNumber==51) {
                    frameSlider.setValue(1);
                } else {
                timer.start();
                }
            });
            
            b_pause.setGraphic(new ImageView(icon_pause));
            b_pause.setOnAction((ActionEvent pauseEvent) -> {
               timer.stop();
            });

            b_bskip.setGraphic(new ImageView(icon_skip));
            b_bskip.setOnAction((ActionEvent bskipEvent) -> {
                frameNumber-=1;
                frameSlider.setValue(frameNumber);
            });

            b_fskip.setGraphic(new ImageView(icon_skip));
            b_fskip.getGraphic().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            b_fskip.setOnAction((ActionEvent fskipEvent) -> {
                frameNumber+=1;
                frameSlider.setValue(frameNumber);
            });  

            Button homeBtn = new Button("Home");
            homeBtn.setOnAction((ActionEvent even) -> {
                stage.close();
                timer.stop();
                frameSlider.setValue(0);               
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); 
            });
 
        timeline.add(b_play, 0, 1, 1, 1);
        timeline.add(b_pause, 1, 1, 1, 1);
        timeline.add(b_bskip, 2, 1, 1, 1);
        timeline.add(b_fskip, 3, 1, 1, 1);
        timeline.add(homeBtn, 20, 0, 1, 1); 
        timeline.setPadding(new Insets(5));

        return timeline;
    }

    private SplitPane display() {
        SplitPane display = new SplitPane();
        Pane canvasPane = new Pane();
        canvasPane.getChildren().add(canvas);
        canvasPane.setPrefSize(600, 500);
        display.setPrefSize(600, 600);
        display.setOrientation(Orientation.VERTICAL);
        display.getItems().add(canvasPane);
        display.setDividerPositions(0.7,0.2,0.1);
        return display;
    }

    private class MyMenuHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            MenuItem mi = (MenuItem) event.getSource();
            String item = mi.getText();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText("Menu item selection information");
            alert.setContentText(item + "file loaded");
            alert.showAndWait();
        }
    }

    private class MyTimer extends AnimationTimer {

        @Override
        public void handle(long now) {
                frameNumber += 1;
            frameSlider.setValue(frameNumber);
            if (frameNumber >= 51) {
                stop();
                System.out.println("Animation stopped");
            }           
        }
    }

    private class LipidMI implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            GridPane root = new GridPane(); 
                SplitPane display = display();
        frameSlider.valueProperty().addListener(new LipidListener()); 
        GridPane timeline = initTimeline(stage);
        display.getItems().add(timeline); 
        frameSlider.valueProperty().addListener(new LipidListener());   
        CheckBox lColorBox = new CheckBox("Color");  
        lColorBox.setSelected(false);     
            lColorBox.setOnAction((ActionEvent even) -> {
                if (lColorBox.isSelected()) {
                    frameSlider.valueProperty().addListener(new LipidColorListener()); 
                } else {
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());                  
                    frameSlider.valueProperty().addListener(new LipidListener());    
                }
            });     
        timeline.add(frameSlider, 0, 0, 5, 1);
        timeline.add(lColorBox, 20, 1, 5, 1);   
        
        root.add(display, 0, 1, 3, 1);
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(0));

        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Lipid Vis");
        stage.setScene(scene);
        stage.show();
        }
    }

    private class LipidListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) {

            frameNumber = newValue.intValue(); 
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());    
            gc.setStroke(Color.BLACK);     
            try{
                plot.lipidAni(gc,lipidX,lipidY,frameNumber);
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private class LipidColorListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) {

            frameNumber = newValue.intValue(); 
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());     
            try{
                plot.lipidColorAni(gc,lipidX,lipidY,frameNumber);
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private class ProtMI implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            GridPane root = new GridPane(); 
                SplitPane display = display(); 
        frameSlider.valueProperty().addListener(new ProtListener());
        CheckBox pColorBox = new CheckBox("Color");  
        pColorBox.setSelected(false);     
            pColorBox.setOnAction((ActionEvent even) -> {
                if (pColorBox.isSelected()) {
                    frameSlider.valueProperty().addListener(new ProtColorListener()); 
                } else {
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());                  
                    frameSlider.valueProperty().addListener(new ProtListener());    
                }
            });        
        GridPane timeline = initTimeline(stage);
        timeline.add(frameSlider, 0, 0, 5, 1);
        timeline.add(pColorBox, 20, 1, 5, 1);   
        display.getItems().add(timeline);   
        
        root.add(display, 0, 1, 3, 1);
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(0));

        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Prot Vis");
        stage.setScene(scene);
        stage.show();
        }
    }

    private class ProtListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) {

            frameNumber = newValue.intValue(); 
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); 
            gc.setStroke(Color.BLACK); 
            try{
                plot.protAni(gc, protX, protY, frameNumber);
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private class ProtColorListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> observable,
                Number oldValue, Number newValue) {

            frameNumber = newValue.intValue(); 
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());          
            try{
                plot.protColorAni(gc, protX, protY, frameNumber);
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}