
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MainStage extends Application {
  
  private Image icon_play, icon_pause, icon_skip, icon_speed;
  private int timelinespeed;
  
	@Override
	public void start(Stage stage) {
		initUI(stage);
	}
	
	private void initUI(Stage stage) {
	  GridPane root = new GridPane(); //DEF
	  SplitPane display = new SplitPane();
	  VBox explorer = new VBox();
	  SplitPane dividerPanel = new SplitPane();
    Canvas canvas = new Canvas(300, 300);
    Pane canvasPane = new Pane();

    loadImages();
    setTimeSpeed(1);
    
	  root.add(initTopMenu(stage), 0, 0, 3, 1);
    root.add(dividerPanel, 0, 1, 3, 1);
    dividerPanel.getItems().addAll(display,explorer);

    GraphicsContext gc = canvas.getGraphicsContext2D();
    drawLines(gc);
    canvasPane.getChildren().add(canvas);
    canvasPane.setPrefSize(600, 500);
    
    display.setPrefSize(600, 600);
    display.setOrientation(Orientation.VERTICAL);
    display.getItems().add(canvasPane);
    display.getItems().add(initTimeline(stage));
    display.getItems().add(initConsole(stage));
    
    display.setDividerPositions(0.7,0.1,0.2);
    dividerPanel.setDividerPositions(0.8);
    
	  root.setHgap(8);
	  root.setVgap(8);
	  root.setPadding(new Insets(0));
	  
    Scene scene = new Scene(root, 800, 600);
    stage.setScene(scene);
	  stage.show();
	}
	
	private HBox initTopMenu(Stage stage) {
	  HBox top_menu = new HBox();
    MenuBar top_mbar = new MenuBar();;
    MenuHandler handler = new MenuHandler();
    
    Menu top_fileMenu = new Menu("File");
    MenuItem topfile_newviz = new MenuItem("New Visualization");
    MenuItem topfile_newmol = new MenuItem("New Molecule");
    MenuItem topfile_saveviz = new MenuItem("Save Visualization State");
    MenuItem topfile_loadviz = new MenuItem("Load Visualization State");
    
    Menu top_optionsMenu = new Menu("Options");
    Menu top_helpMenu = new Menu("Help");
    
    top_mbar.prefWidthProperty().bind(stage.widthProperty());
    
    top_mbar.getMenus().add(top_fileMenu);
    top_mbar.getMenus().add(top_optionsMenu);
    top_mbar.getMenus().add(top_helpMenu);
    
    top_fileMenu.getItems().addAll(topfile_newviz,topfile_newmol,topfile_saveviz,topfile_loadviz);
    top_menu.getChildren().add(top_mbar);
    
    return top_menu;
	}
	
	private GridPane initTimeline(Stage stage) {
	   GridPane timeline = new GridPane();
     Slider slider = new Slider(0, 100, 0);
     Button b_play = new Button();
     Button b_pause = new Button();
     Button b_bskip = new Button();
     Button b_fskip = new Button();
     
     timeline.setVgap(5);
     
     GridPane speedButtonArea = new GridPane();
       Button b_ff = new Button();
       Button b_fb = new Button();
       VBox p = new VBox();
       
       b_fb.setGraphic(new ImageView(icon_speed));
       b_fb.setScaleY(0.8);
       b_ff.setGraphic(new ImageView(icon_speed));
       b_ff.getGraphic().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
       b_ff.setScaleY(0.8);
       
       Text textSpeed = new Text("Speed: " + getTimeSpeed() + "x");
       textSpeed.setFont(Font.font("Serif", FontWeight.SEMI_BOLD,10));
       
       p.getChildren().add(textSpeed);
       p.setAlignment(Pos.CENTER);
       p.prefHeight(b_play.heightProperty().get());
       
       speedButtonArea.add(p, 0, 0, 2, 1);
       speedButtonArea.add(b_fb, 0, 1, 1, 1);
       speedButtonArea.add(b_ff, 1, 1, 1, 1);
       speedButtonArea.maxHeightProperty().bind(slider.heightProperty());
     b_play.setGraphic(new ImageView(icon_play));
     b_pause.setGraphic(new ImageView(icon_pause));
     b_bskip.setGraphic(new ImageView(icon_skip));
     b_fskip.setGraphic(new ImageView(icon_skip));
     b_fskip.getGraphic().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

     
     List<Node> button_list = new ArrayList<Node>();
     button_list = Arrays.asList(b_play,b_pause,b_bskip,b_fskip,speedButtonArea);
     Iterator<Node> it = button_list.iterator();
     
     timeline.add(slider, 0, 0, 5, 1);    
     for (int i = 0; it.hasNext(); i++) {
       timeline.add(it.next(), i, 1, 1, 1);
     }
     
     timeline.setPadding(new Insets(5));
     
	   return timeline;
	}
	
	private TextArea initConsole (Stage stage) {
	  TextArea console = new TextArea();
	  console.setMinHeight(10);
	  console.setPadding(new Insets(5));
	  return console;
	}
	
  private void drawLines(GraphicsContext gc) {

    gc.beginPath();
    gc.moveTo(30.5, 30.5);
    gc.lineTo(150.5, 30.5);
    gc.lineTo(150.5, 150.5);
    gc.lineTo(30.5, 30.5);
    gc.stroke();
  }
  
  private void loadImages() {
    icon_play = new Image("file:icons//play.png");
    icon_pause = new Image("file:icons//pause.png");
    icon_skip = new Image("file:icons//skip.png");
    icon_speed = new Image("file:icons//fastf.png");
  }
  
  public int getTimeSpeed() {
    return timelinespeed;
  }
	
  public void setTimeSpeed(int t) {
    timelinespeed = t;
  }
	
	private class MenuHandler implements EventHandler<ActionEvent> {
	  
	  @Override
	  public void handle(ActionEvent event) {
	    doShowMessageDialog(event);
	  }
	  
	  private void doShowMessageDialog(ActionEvent event) {
	    
	  }
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
