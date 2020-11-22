package ee.ut.math.tvt.salessystem.ui.controllers;//Autor: Robert Leht//autor: Robert Leht

import ee.ut.math.tvt.salessystem.ui.SalesSystemUI;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TeamController implements Initializable {

    private static final Logger log = LogManager.getLogger(TeamController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Team Controller is initialized");
    }
}