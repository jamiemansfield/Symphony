//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony;

import static com.google.common.io.Resources.getResource;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main-Class behind Symphony.
 */
public final class SymphonyMain extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getResource("fxml/Main.fxml"));
        final Parent root = loader.load();
        final Scene scene = new Scene(root);
        primaryStage.setTitle("Symphony v" + SharedConstants.VERSION);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
