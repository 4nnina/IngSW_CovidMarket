package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.storage.Database;
import main.model.Responsabile;
import main.model.Utente;
import main.utils.StageManager;

import java.util.ArrayList;

public class ControllerLoginPage {

    @FXML
    private Button loginButton;

    @FXML
    private TextField nomeUtenteTextField;

    @FXML
    private PasswordField pswPasswordField;

    @FXML
    private Button registratiButton;

    @FXML
    private CheckBox personaleCheckBox;


    private ArrayList<Utente> utenti;
    private ArrayList<Responsabile> responsabili;

    @FXML
    private void initialize(){
        utenti = new ArrayList<>(Database.getInstance().getUtenti());
        responsabili = new ArrayList<>(Database.getInstance().getResponsabili());

        //handler
        loginButton.setOnAction(this::loginButtonHandler);
        registratiButton.setOnAction((this::registratiButtonHandler));

    }

    private void registratiButtonHandler(ActionEvent actionEvent) {
        System.out.println("registrati");

        StageManager registratiPage = new StageManager();
        registratiPage.setStageRegistrazione((Stage) registratiButton.getScene().getWindow());

        //TODO: implemetare registrati, cambio
    }

    private void loginButtonHandler(ActionEvent actionEvent) {
        System.out.println("login");


        if(personaleCheckBox.isSelected()){
            //TODO: implemetare login personale, cambio
            StageManager homePersonalePage = new StageManager();
            homePersonalePage.setStageHomePersonale((Stage) loginButton.getScene().getWindow());
        }
        else{
            //TODO: implemetare login utente, cambio
            StageManager homePage = new StageManager();
            homePage.setStageHomeUtenti((Stage) loginButton.getScene().getWindow());
        }
    }


}
