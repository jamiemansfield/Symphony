//******************************************************************************
// Copyright (c) Jamie Mansfield <https://jamiemansfield.me/>
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//******************************************************************************

package me.jamiemansfield.symphony.gui.concurrent;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.TaskProgressView;

/**
 * The task manager, used for creating tracked tasks and
 * displaying the tasks window.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public final class TaskManager {

    public static final TaskManager INSTANCE = new TaskManager();

    private final TaskProgressView<TrackedTask<?>> progressView = new TaskProgressView<>();
    private final Scene scene = new Scene(this.progressView);

    private TaskManager() {
    }

    /**
     * Displays the task window.
     */
    public void display() {
        final Stage window = new Stage();
        window.setTitle("Tasks");
        window.setScene(this.scene);
        window.show();
    }

    /**
     * An extension of {@link Task} that is tracked by the parent
     * task manager.
     *
     * @param <T> The result of the task
     */
    public abstract class TrackedTask<T> extends Task<T> {
        {
            TaskManager.this.progressView.getTasks().add(this);
        }
    }

}
