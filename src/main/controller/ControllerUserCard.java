package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import main.model.CartaFedelta;
import main.model.Utente;

public class ControllerUserCard extends Controller<SectionUser>
{
    @FXML private Pane gatePane;

    @FXML private Label nomeLabel;
    @FXML private Label cognomeLabel;
    @FXML private Label tesseraLabel;
    @FXML private Label dataEmissioneLabel;
    @FXML private Label puntiLabel;

    @Override
    public void onSwapPane(Object data)
    {
        Utente currentUser = (Utente) data;

        CartaFedelta cartaFedelta = currentUser.getCartaFedelta();
        if (cartaFedelta != null)
        {
            nomeLabel.setText(currentUser.getNome());
            cognomeLabel.setText(currentUser.getCognome());
            tesseraLabel.setText(cartaFedelta.ID);
            dataEmissioneLabel.setText((cartaFedelta.dataEmissione).toString());
            puntiLabel.setText(String.valueOf(cartaFedelta.punti));
        }
    }

    @Override
    public Pane getGatePane() {
        return null;
    }
}