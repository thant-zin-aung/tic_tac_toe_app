package game.controller;

import game.model.Player;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.Random;


public class MainController {
    @FXML private AnchorPane anchorPane;
    @FXML private HBox fruitHBox;
    @FXML private ImageView view1;
    @FXML private ImageView view2;
    @FXML private ImageView view3;
    @FXML private ImageView view4;
    @FXML private ImageView view5;
    @FXML private ImageView view6;
    @FXML private ImageView view7;
    @FXML private ImageView view8;
    @FXML private ImageView view9;
    @FXML private Line line1,line2,line3,line4;
    @FXML private Button p1IconButton,p2IconButton,startGameButton;


    @FXML private TextField p1Name;
    @FXML private TextField p2Name;
    @FXML private ImageView p1Icon;
    @FXML private ImageView p2Icon;

    @FXML private Label playerTurnLabel;
    @FXML private ImageView playerTurnIcon;
    @FXML private Label winnerLabel;

    private Player p1;
    private Player p2;
    private Player randomClickPlayer;
    private EventHandler<MouseEvent> clickOnPlaygrooundFruits;

    private boolean clickFlag = false;

    private ObservableList<ImageView> fruitImageViewList = FXCollections.observableArrayList(
        createImageView("fruit1.png"), createImageView("fruit2.png"), createImageView("fruit3.png"),
            createImageView("fruit4.png"), createImageView("fruit5.png"), createImageView("fruit6.png"),
            createImageView("fruit7.png"), createImageView("fruit8.png"), createImageView("fruit9.png")
    );

    private ObservableList<ImageView> playgroundView = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        playgroundView.addAll(view1,view2,view3,view4,view5,view6,view7,view8,view9);
        setItemToDropShadowEffect(fruitImageViewList);
        // Hiding Playground
        hideItems(true,line1,line2,line3,line4,playerTurnLabel,playerTurnIcon);
        hideItems(true,playgroundView);
        // Hiding Playground

        fruitHBox.getChildren().addAll(fruitImageViewList);


    }

    @FXML
    private void pressSelectIcon (MouseEvent me) {
        setFlagToTrue(false);
        if (me.getSource()==p1IconButton) p1Icon.setImage(null);
        if (me.getSource()==p2IconButton) p2Icon.setImage(null);
        setHBoxColor("#FCFF00");
        EventHandler<MouseEvent> clickOnFruit = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playScaleEffect((ImageView)event.getSource(),0.08,1,0.8,true,2);
                if ( me.getSource() == p1IconButton ) {
                    ImageView tempIcon = (ImageView)event.getSource();
                    p1Icon.setImage(tempIcon.getImage());
                    playScaleEffect(p1Icon,0.5,0.1,1,false,1);
                    setItemToDropShadowEffect(p1Icon);
                    setFlagToTrue(true);
                } else if ( me.getSource() == p2IconButton ) {
                    ImageView tempIcon = (ImageView)event.getSource();
                    p2Icon.setImage(tempIcon.getImage());
                    playScaleEffect(p2Icon,0.5,0.1,1,false,1);
                    setItemToDropShadowEffect(p2Icon);
                    setFlagToTrue(true);
                }
            }
        };

        for ( ImageView view:fruitImageViewList ) {
            view.addEventHandler(MouseEvent.MOUSE_CLICKED,clickOnFruit);
        }

        new Thread( () -> {
            while ( !clickFlag ) {
                try {
                    Thread.sleep(200);
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
            for ( ImageView view:fruitImageViewList ) {
                view.removeEventHandler(MouseEvent.MOUSE_CLICKED,clickOnFruit);
            }
            setHBoxColor("white");
        }).start();
    }

    @FXML
    public void pressStartGameButton() {
        p1 = new Player(p1Name.getText(),p1Icon);
        p2 = new Player(p2Name.getText(),p2Icon);
        hideItems(true,p1Name,p2Name,p1Icon,p2Icon,p1IconButton,p2IconButton,startGameButton,fruitHBox);
        hideItems(false,line1,line2,line3,line4,playerTurnLabel,playerTurnIcon);
        hideItems(false,playgroundView);

        clickOnPlaygrooundFruits = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int clickedIndex=0;
                ImageView clickedView = (ImageView)event.getSource();
                for ( int id = 0 ; id < playgroundView.size() ; id++ ) {
                    if ( playgroundView.get(id) == clickedView ) {
                        playgroundView.get(id).setImage(randomClickPlayer.getPlayerIcon().getImage());
                        playScaleEffect(clickedView,0.5,0.1,1,false,1);
                        clickedIndex = id+1;
                        break;
                    }
                }
                System.out.println("index: "+clickedIndex);
                randomClickPlayer.setWinStage(clickedIndex,clickedView);
                if ( randomClickPlayer.isWinner() ) {
                    finalMessage(randomClickPlayer.getName());
                    return;
                }
                if ( randomClickPlayer == p1 ) randomClickPlayer = p2;
                else if ( randomClickPlayer == p2 ) randomClickPlayer = p1;
                playerTurnIcon.setImage(randomClickPlayer.getPlayerIcon().getImage());
                playerTurnLabel.setText("It's "+randomClickPlayer.getName()+" turn");
            }
        };

        new Thread( () -> {
            int randomPlayerIndex = Math.round(new Random().nextInt(2));
            if ( randomPlayerIndex <= 0 ) randomClickPlayer = p1;
            else randomClickPlayer = p2;
            createListener(clickOnPlaygrooundFruits,playgroundView);
            playerTurnIcon.setImage(randomClickPlayer.getPlayerIcon().getImage());
            Platform.runLater(() -> playerTurnLabel.setText("It's "+randomClickPlayer.getName()+" turn") );
        }).start();

        new Thread( () -> {
            boolean drawFlag = false;
            while ( !drawFlag ) {
                for ( ImageView view:playgroundView ) {
                    if ( view.getImage() == null ) {
                        drawFlag=false;
                        break;
                    } else {
                        drawFlag=true;
                    }
                }
                try {
                    Thread.sleep(200);
                } catch ( Exception e) {
                    e.printStackTrace();
                }
            }
            destoryListener(clickOnPlaygrooundFruits,playgroundView);
        }).start();
    }

    private void createListener(EventHandler<MouseEvent> event,Node ... items) {
        for ( Node item:items ) {
            item.addEventHandler(MouseEvent.MOUSE_CLICKED,event);
        }
    }

    private void createListener(EventHandler<MouseEvent> event, ObservableList<? extends Node> items) {
        for ( Node item:items ) {
            item.addEventHandler(MouseEvent.MOUSE_CLICKED,event);
        }
    }

    private void destoryListener(EventHandler<MouseEvent> event,Node ... items) {
        for ( Node item:items ) {
            item.removeEventHandler(MouseEvent.MOUSE_CLICKED,event);
        }
    }

    private void destoryListener(EventHandler<MouseEvent> event, ObservableList<? extends Node> items) {
        for ( Node item:items ) {
            item.removeEventHandler(MouseEvent.MOUSE_CLICKED,event);
        }
    }

    private void finalMessage(String winner) {
        System.out.println("Winner: "+winner);
        destoryListener(clickOnPlaygrooundFruits,playgroundView);
        winnerLabel.setText(winner + " is the winner");
    }


    private ImageView createImageView(String imagePath) {
        ImageView imageView = new ImageView(new Image(getClass().getResource("../media/"+imagePath).toString()));
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        return imageView;
    }

    private void hideItems(boolean isHide , Node ... items ) {
        for ( Node item:items ) {
            if ( isHide ) {
                item.setVisible(false);
            } else {
                item.setVisible(true);
            }
        }
    }
    private void hideItems(boolean isHide , ObservableList<ImageView> items ) {
        for ( Node item:items ) {
            if ( isHide ) {
                item.setVisible(false);
            } else {
                item.setVisible(true);
            }
        }
    }

    private void setHBoxColor(String color) {
        fruitHBox.setStyle("-fx-background-color: "+color);
    }

    private void setItemToDropShadowEffect(Node ... items) {
        DropShadow dropShadow = new DropShadow();
        for ( Node item:items ) {
            item.setEffect(dropShadow);
        }
    }
    private void setItemToDropShadowEffect(ObservableList<? extends Node> items) {
        DropShadow dropShadow = new DropShadow();
        for ( Node item:items ) {
            item.setEffect(dropShadow);
        }
    }

    private void setFlagToTrue(boolean isTrue) {
        clickFlag=isTrue;
    }
    private void playScaleEffect (Node playNode,double durationTime,double fromValue,double toValue,boolean setAutoReverse,int cycleCount) {
        durationTime*=1000;
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(durationTime),playNode);
        scaleTransition.setFromX(fromValue);
        scaleTransition.setFromY(fromValue);
        scaleTransition.setToX(toValue);
        scaleTransition.setToY(toValue);
        scaleTransition.setCycleCount(cycleCount);
        scaleTransition.setAutoReverse(setAutoReverse);
        scaleTransition.play();
    }
}
