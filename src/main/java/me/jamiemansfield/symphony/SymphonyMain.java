//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony;

import static java.util.Objects.requireNonNull;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main-Class behind Symphony.
 */
public final class SymphonyMain extends Application {

    private static SymphonyMain instance;

    /**
     * Gets the currently running instance of Symphony.
     *
     * @return The symphony instance
     */
    public static SymphonyMain getInstance() {
        requireNonNull(instance, "Symphony was already initialised");
        return instance;
    }

    private Stage primaryStage;
    private Scene scene;

    public SymphonyMain() {
        if (instance != null) throw new RuntimeException("Symphony was already initialised");
        instance = this;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public Scene getScene() {
        return this.scene;
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        // Set the primary stage
        this.primaryStage = primaryStage;

        // Load the FXML
        final FXMLLoader loader = new FXMLLoader(SymphonyMain.class.getClassLoader().getResource("fxml/Main.fxml"));
        final Parent root = loader.load();

        // "Set the scene"
        this.scene = new Scene(root);
        this.primaryStage.setTitle("Symphony v" + SharedConstants.VERSION);
        this.primaryStage.setScene(this.scene);
        this.primaryStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }

}
