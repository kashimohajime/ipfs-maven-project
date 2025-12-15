package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;


import java.io.File;

public class IPFSJavaFXApp extends Application {

    private final IPFSClient ipfs = new IPFSClient();

    @Override
    public void start(Stage stage) {

        Label title = new Label("IPFS JavaFX Application");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField fileField = new TextField();
        fileField.setPromptText("File name (ex: test.txt)");

        TextField cidField = new TextField();
        cidField.setPromptText("CID");

        TextArea output = new TextArea();
        output.setEditable(false);

        Button uploadBtn = new Button("Upload");
        Button downloadBtn = new Button("Download");
        Button pinBtn = new Button("Pin");
        Button unpinBtn = new Button("Unpin");

        uploadBtn.setOnAction(e -> {
            try {
                File file = new File(fileField.getText());
                if (!file.exists()) {
                    output.setText("❌ File not found");
                    return;
                }
                String cid = ipfs.uploadFile(file);
                output.setText("✅ Uploaded\nCID:\n" + cid);
                cidField.setText(cid);
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

      downloadBtn.setOnAction(e -> {
    try {
        String cid = cidField.getText();

        if (cid.isEmpty()) {
            output.setText("❌ CID is empty");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file from IPFS");
        fileChooser.setInitialFileName("downloaded_file");

        File destination = fileChooser.showSaveDialog(stage);

        if (destination != null) {
            ipfs.download(cid, destination);
            output.setText("✅ File downloaded to:\n" + destination.getAbsolutePath());
        }

    } catch (Exception ex) {
        output.setText("Error: " + ex.getMessage());
    }
});


        pinBtn.setOnAction(e -> {
            try {
                ipfs.pin(cidField.getText());
                output.setText("📌 File pinned");
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        unpinBtn.setOnAction(e -> {
            try {
                ipfs.unpin(cidField.getText());
                output.setText("❌ File unpinned");
            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        VBox layout = new VBox(10,
                title,
                fileField,
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

