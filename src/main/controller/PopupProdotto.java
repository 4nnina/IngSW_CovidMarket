package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.model.Attributo;
import main.model.Prodotto;
import main.model.Reparto;
import main.model.Responsabile;

import java.net.URL;
import java.util.EnumSet;
import java.util.ResourceBundle;

public class PopupProdotto extends Popup<Prodotto> implements Initializable
{
    @FXML
    private TextField nomeTextField;

    @FXML private Spinner<Integer> prezzoSpinner;
    @FXML private Spinner<Integer> qtSpinner;
    @FXML private Spinner<Integer> disponibileSpinner;

    @FXML private VBox caratteristicheVBox;

    @FXML
    private ComboBox<Reparto> repartoComboBox;

    @FXML
    private TextField marcaTextField;

    @FXML
    private TextField immagineTextField;

    @FXML
    private Button button;


    private Responsabile currentResponsabile;
    private Prodotto currentProdotto;

    public PopupProdotto(Responsabile responsabile, Prodotto prodotto) {
        super("../resources/fxml/inserisciProdotto.fxml");
        this.currentResponsabile = responsabile;
        this.currentProdotto = prodotto;
    }

    @Override
    public Object self() {
        return this;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        qtSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));
        prezzoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000));
        disponibileSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000));

        // Popola caratteristiche possibili
        caratteristicheVBox.setPrefHeight(Attributo.values().length * 20);
        for(Attributo attrib : Attributo.values())
            caratteristicheVBox.getChildren().add(new CheckBox(attrib.toString()));

        // Setta campi pre esistenti
        if (currentProdotto != null)
        {
            button.setText("Modifica");
        }
        else
        {
            button.setText("Aggiungi");
        }
    }

    @Override
    protected void onShow()
    {
        // Carica tutti i reparti
        for (Reparto rep: Reparto.values())
            if (rep != Reparto.Tutto && currentResponsabile.getRepartiGestiti().contains(rep))
                repartoComboBox.getItems().add(rep);
        repartoComboBox.getSelectionModel().select(0);
    }

    @FXML
    void buttonHandler(ActionEvent event)
    {
        // Modifica
        if (currentProdotto != null)
        {

        }
        // Aggiungi
        else
        {
            // Accumula caratteristiche
            EnumSet<Attributo> attributi = EnumSet.noneOf(Attributo.class);
            for (int i = 0; i < Attributo.values().length; ++i)
            {
                CheckBox checkBox = (CheckBox)caratteristicheVBox.getChildren().get(i);
                if (checkBox.isSelected())
                    attributi.add(Attributo.values()[i]);
            }

            Prodotto prodotto = new Prodotto.Builder()
                    .setNome(nomeTextField.getText())
                    .setPrezzo(prezzoSpinner.getValue())
                    .setMarca(marcaTextField.getText())
                    .setImagePath(immagineTextField.toString())
                    .setQuantitaPerConfezione(qtSpinner.getValue())
                    .setReparto(repartoComboBox.getValue())
                    .setAttributi(attributi)
                    .setQuantitaDisponibile(disponibileSpinner.getValue())
                    .build();

            close(prodotto);
        }
    }
}