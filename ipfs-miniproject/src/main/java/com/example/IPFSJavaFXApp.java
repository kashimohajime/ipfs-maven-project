package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Classe principale JavaFX.
 * Elle fournit une interface graphique pour interagir avec IPFS.
 */
public class IPFSJavaFXApp extends Application {

    // Instance du client IPFS
    private final IPFSClient ipfs = new IPFSClient();

    @Override
    public void start(Stage stage) {

        // Titre de l'application
        Label title = new Label("IPFS JavaFX Application");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Champ pour afficher ou saisir le CID
        TextField cidField = new TextField();
        cidField.setPromptText("CID");

        // Zone d'affichage des messages
        TextArea output = new TextArea();
        output.setEditable(false);

        // Boutons d'action
        Button uploadBtn = new Button("Upload");
        Button downloadBtn = new Button("Download");
        Button pinBtn = new Button("Pin");
        Button unpinBtn = new Button("Unpin");

        // Gestion de l'upload
        uploadBtn.setOnAction(e -> {
            try {
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(stage);

                if (file == null) return;

                String cid = ipfs.uploadFile(file);
                cidField.setText(cid);
                output.setText("Upload réussi\nCID : " + cid);

            } catch (Exception ex) {
                output.setText("Erreur : " + ex.getMessage());
            }
        });

        // Gestion du téléchargement
        downloadBtn.setOnAction(e -> {
            try {
                FileChooser chooser = new FileChooser();
                File file = chooser.showSaveDialog(stage);

                if (file == null) return;

                ipfs.downloadFile(cidField.getText(), file);
                output.setText("Fichier téléchargé avec succès");

            } catch (Exception ex) {
                output.setText("Erreur : " + ex.getMessage());
            }
        });

        // Pin du fichier
        pinBtn.setOnAction(e -> {
            try {
                ipfs.pin(cidField.getText());
                output.setText("Fichier pinné avec succès");
            } catch (Exception ex) {
                output.setText("Erreur : " + ex.getMessage());
            }
        });

        // Unpin du fichier
        unpinBtn.setOnAction(e -> {
            try {
                ipfs.unpin(cidField.getText());
                output.setText("Fichier dépinné");
            } catch (Exception ex) {
                output.setText("Erreur : " + ex.getMessage());
            }
        });

        // Organisation de l'interface
        VBox layout = new VBox(10,
                title,
                uploadBtn,
                cidField,
                downloadBtn,
                pinBtn,
                unpinBtn,
                output
        );

        layout.setPadding(new Insets(15));

        stage.setTitle("IPFS JavaFX");
        stage.setScene(new Scene(layout, 450, 450));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
