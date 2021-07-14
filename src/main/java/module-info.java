module ProjectDB {

  requires java.sql;

  requires javafx.base;
  requires javafx.controls;
  requires javafx.graphics;
  requires javafx.fxml;

  requires com.jfoenix;

  opens app to javafx.fxml;
  exports app;
}
