module HotPizza {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	requires javafx.graphics;
	requires java.desktop;
  	
	opens all to javafx.graphics, javafx.fxml , javafx.base , javax.annotation;
	
 }
