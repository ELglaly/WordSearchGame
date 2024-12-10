package com.gradescope.wordsearch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;

public class GridView {

    private int rows;
    private int cols;
    private TextArea textArea;
    public GridView(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public void drawGrid() {
        Stage stage= new Stage();
        HBox hbox=new HBox();

        Scene scene =new Scene();

    }
}
