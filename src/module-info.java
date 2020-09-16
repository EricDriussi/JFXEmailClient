module TestFx {
	
	requires javafx.fxml;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.web;
	
	opens start;
	opens start.view;
	opens start.controller;
	opens start.model;
	
}