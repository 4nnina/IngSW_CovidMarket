package main.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import main.model.CartaFedelta;
import main.model.Citta;
import main.model.MetodoPagamento;
import main.model.Utente;
import main.storage.Database;
import main.utils.CityHelper;
import main.utils.StageManager;
import main.utils.Validator;
import main.view.AutoCompleteBox;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class ControllerRegistrazione extends Controller
{

    @FXML private TextField nomeTextField;
    @FXML private TextField cognomeTextField;
    @FXML private TextField indirizzoTextField;
    @FXML private TextField telefonoTextField;
    @FXML private TextField mailTextField;

    @FXML private PasswordField pswPasswordField;
    @FXML private PasswordField confermaPswPasswordField;

    @FXML private Label capLabel;

    @FXML private ImageView covidMarketImageView;
    @FXML private Button salvaButton;

    @FXML private ChoiceBox<MetodoPagamento> pagamentoChoiceBox;
    @FXML private CheckBox attivaCheckbox;

    @FXML private ComboBox<String> cittaComboBox;
    private AutoCompleteBox cittaAutoBox;

    @FXML
    private void initialize()
    {
        salvaButton.setOnAction(this::salvaButtonHandler);
        covidMarketImageView.setOnMouseClicked(this::loginHandler);

        pagamentoChoiceBox.getItems().addAll(MetodoPagamento.values());
        pagamentoChoiceBox.getSelectionModel().select(MetodoPagamento.Nessuno);

        // Carica citta dal file
        CityHelper cityHelper = CityHelper.getInstance();
        cittaComboBox.getItems().addAll(FXCollections.observableArrayList(cityHelper.getCities()));
        cittaAutoBox = new AutoCompleteBox(cittaComboBox);

        cittaComboBox.setOnAction(foo ->
        {
            String citta = cittaComboBox.getValue();
            capLabel.setText(cityHelper.getCap(citta));
        });
    }

    private void loginHandler(MouseEvent mouseEvent) {
        stageManager.swap(Stages.Login);
    }

    // Controlla se i parametri inseriti vanno bene
    private boolean validateUserData()
    {
        boolean result = true;

        if (!Validator.isAlphanumeric(nomeTextField.getText()))
        {
            nomeTextField.setStyle("-fx-control-inner-background:red");
            result = false;
        }
        else
            nomeTextField.setStyle("-fx-control-inner-background: ecfbfa");

        if (!Validator.isAlphanumeric(cognomeTextField.getText()))
        {
            cognomeTextField.setStyle("-fx-control-inner-background:red");
            result = false;
        }
        else
            cognomeTextField.setStyle("-fx-control-inner-background: ecfbfa");

        if (!Validator.isTelephoneNumber(telefonoTextField.getText()))
        {
            telefonoTextField.setStyle("-fx-control-inner-background:red");
            result = false;
        }
        else
            telefonoTextField.setStyle("-fx-control-inner-background: ecfbfa");

        if (!Validator.isEmail(mailTextField.getText()))
        {
            mailTextField.setStyle("-fx-control-inner-background:red");
            result = false;
        }else
            mailTextField.setStyle("-fx-control-inner-background: ecfbfa");

        if(pswPasswordField.getText().isEmpty())
        {
            pswPasswordField.setStyle("-fx-control-inner-background:red");
            result = false;
        }else
            pswPasswordField.setStyle("-fx-control-inner-background: ecfbfa");

        if(confermaPswPasswordField.getText().isEmpty() ||
                !confermaPswPasswordField.getText().equals(pswPasswordField.getText()))
        {
            confermaPswPasswordField.setStyle("-fx-control-inner-background:red");
            result = false;
        }else
            confermaPswPasswordField.setStyle("-fx-control-inner-background: ecfbfa");

        if (!Validator.isAddressFormat(indirizzoTextField.getText()))
        {
            indirizzoTextField.setStyle("-fx-control-inner-background:red");
            result = false;
        }else
            indirizzoTextField.setStyle("-fx-control-inner-background: ecfbfa");


        if(capLabel.getText().isEmpty() || capLabel.getText() == null){
            cittaComboBox.setStyle("-fx-control-inner-background:red");
            result = false;
        }else
            cittaComboBox.setStyle("-fx-control-inner-background: ecfbfa");
        /*
        int tmp = 0;
        try{tmp = Integer.valueOf(capLabel.getText()); } catch (NumberFormatException e)
        {
            capLabel.setStyle("-fx-control-inner-background:red");
            result = false;
        }
        */

        return result;
    }

    @FXML
    private void salvaButtonHandler(ActionEvent actionEvent)
    {
        //TODO
        System.out.println("premuto salva");

        if (validateUserData())
        {
            Utente.Builder builder = new Utente.Builder()
                    .setNominativo(nomeTextField.getText().toUpperCase(), cognomeTextField.getText().toUpperCase())
                    .setIndirizzo(indirizzoTextField.getText(), cittaComboBox.getValue(), capLabel.getText())
                    .setTelefono(telefonoTextField.getText())
                    .setEmail(mailTextField.getText())
                    .setPassword(pswPasswordField.getText())
                    .setCartaFedelta(null)
                    .setMetodoPagamento(pagamentoChoiceBox.getValue());

            if (attivaCheckbox.isSelected()) {
                builder.setCartaFedelta(new CartaFedelta(LocalDate.now()));
            }

            Utente user = builder.build();
            Database database = Database.getInstance();

            if(existingUser(mailTextField.getText()))
            {
                if(database.getUtenti().add(user)) {
                    // Ha inserito con successo
                    resetTextField();
                    stageManager.setTargetUser(user);
                    stageManager.swap(Stages.HomeUtente);
                }
                else
                    System.out.println("non inserito in database");

            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Utente già esistente");
                alert.showAndWait();
                mailTextField.clear();
                System.out.println("UTENTE ESISTE GIA");
            }
        }
    }

    private boolean existingUser(String mail) {

        Database database = Database.getInstance();
        for(Utente user: database.getUtenti()){
            if(user.getEmail().equals(mail))
                return false;
        }

        return true;
    }

    private void resetTextField(){
        nomeTextField.clear();
        cognomeTextField.clear();
        indirizzoTextField.clear();
        cittaComboBox.cancelEdit();
        capLabel.setText("");
        pswPasswordField.clear();
        confermaPswPasswordField.clear();
        mailTextField.clear();
        pagamentoChoiceBox.getSelectionModel().select(0);
        telefonoTextField.clear();
    }
}
