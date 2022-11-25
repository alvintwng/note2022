Project: fxOracle

example of grid
``` java
        var grid08 = new Label("col 0, row 8, colSpan 8, rowSpan 1");
        grid.add(grid08, 0, 8, 8, 1);
        var grid82 = new Label("*82");
        grid.add(grid82, 8, 2);
```

### grid.add(grid, 0, 0, 0, 0);
```
javafx.scene.layout.GridPane

public void add(Node node,
                int i,
                int i1,
                int i2,
                int i3)

Adds a child to the gridpane at the specified [column, row] position and spans. This
convenience method will set the gridpane column, row, and span constraints on the child.

Parameters:
    child - the node being added to the gridpane
    columnIndex - the column index position for the child within the gridpane, counting from 0
    rowIndex - the row index position for the child within the gridpane, counting from 0
    colspan - the number of columns the child's layout area should span
    rowspan - the number of rows the child's layout area should span
```
### grid.add(grid, 0, 0, 0);
```
javafx.scene.layout.GridPane

public void add(Node node,
                int i,
                int i1)

Adds a child to the gridpane at the specified [column, row] position. This
convenience method will set the gridpane column and row constraints on the child.

Parameters:
    child - the node being added to the gridpane
    columnIndex - the column index position for the child within the gridpane, counting from 0
    rowIndex - the row index position for the child within the gridpane, counting from 0
```

### text.setText("")
```
javafx.scene.text.Text

public final void setText(String string)

Sets the value of the property text.
Property description:
    Defines text string that is to be displayed.
Default value:
    empty string
```

App.java
``` java
package com.zero1.fxoracle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * ref: https://docs.oracle.com/javafx/2/get_started/form.htm
 *
 * started from Netbean 17 > File > New Project > Java with Maven > Simple Java
 *
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        // GridPane with Gap and Padding Properties
        var grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // either .setPadding
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25)); // either .setAlignment

        // Add Text, Labels, and Text Fields. Controls
        /* The grid.add() method adds the scenetitle variable to the layout grid. 
        The numbering for columns and rows in the grid starts at zero, and scenetitle 
        is added in column 0, row 0. The last two arguments of the grid.add() method 
        set the column span to 2 and the row span to 1. */
        var scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        /* Label object with text User Name at column 0, row 1 and a Text Field object 
        that can be edited. */
        var userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        /*The text field is added to the grid pane at column 1, row 1 */
        var userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        var pw = new Label("Password:");
        grid.add(pw, 0, 2);

        var pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        // Add a Button and Text. Button
        var btn = new Button("Sign in");
        var hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        // Example 2-5 Text
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        // Add Code to Handle an Event.  Button Event
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Sign in button pressed");
            }
        });

        /* // lambda expression
        btn.setOnAction((ActionEvent e) -> {
            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText("Sign in button pressed");
        });
         */
        // This code sets the scene width and height to 300 by 275.
        var scene = new Scene(grid, 300, 275);

        primaryStage.setScene(scene);

        primaryStage.setTitle("JavaFX Welcome");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}

```
