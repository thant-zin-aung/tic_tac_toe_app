package game.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;

import java.util.List;

public class Player {
    private String name;
    private ImageView playerIcon;
    private ObservableList<ImageView> winStage = FXCollections.observableArrayList();

    public Player(String name, ImageView playerIcon) {
        this.name = name;
        this.playerIcon = playerIcon;
        winStage.addAll(new ImageView(),new ImageView(),new ImageView(),new ImageView(),new ImageView(),new ImageView(),new ImageView(),new ImageView(),new ImageView(),new ImageView());
    }

    public String getName() {
        return name;
    }

    public ImageView getPlayerIcon() {
        return playerIcon;
    }
    public void setWinStage(int index,ImageView icon) {
        this.winStage.set(index,icon);
    }
    public List<ImageView> getWinStage() {
        return winStage;
    }

    public boolean isWinner() {
        if ( checkWinningStage(1,2,3) ) return true;
        else if ( checkWinningStage(4,5,6)) return true;
        else if ( checkWinningStage(7,8,9)) return true;
        else if ( checkWinningStage(1,4,7)) return true;
        else if ( checkWinningStage(2,5,8)) return true;
        else if ( checkWinningStage(3,6,9)) return true;
        else if ( checkWinningStage(1,5,9)) return true;
        else if ( checkWinningStage(3,5,7)) return true;
        else return false;
    }
    private boolean checkWinningStage(int ... views) {
        boolean flag = true;
        for ( int item:views ) {
            if ( this.getWinStage().get(item).getImage()==null ) {
                flag=false;
                break;
            }
        }
        return flag;
    }
}
