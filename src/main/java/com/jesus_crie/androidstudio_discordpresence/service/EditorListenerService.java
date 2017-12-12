package com.jesus_crie.androidstudio_discordpresence.service;

import com.intellij.openapi.application.ApplicationAdapter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import com.jesus_crie.androidstudio_discordpresence.Log;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

public class EditorListenerService extends ApplicationAdapter implements FileEditorManagerListener {

    public static EditorListenerService getInstance() {
        return ServiceManager.getService(EditorListenerService.class);
    }

    private boolean enabled = false;
    private MessageBusConnection connection;
    private final WeakReference<DiscordService> discordService = new WeakReference<>(DiscordService.getInstance());

    private WeakReference<Project> currentProject = new WeakReference<>(null);
    private WeakReference<VirtualFile> currentFile = new WeakReference<>(null);

    public void enable() {
        if (connection != null) return;
        connection = ApplicationManager.getApplication().getMessageBus().connect();
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);
        ApplicationManager.getApplication().addApplicationListener(this);

        Project[] pjs = ProjectManager.getInstance().getOpenProjects();
        if (pjs.length > 0) {
            Project p = pjs[0];
            currentProject = new WeakReference<>(p);
            VirtualFile[] fs = FileEditorManager.getInstance(p).getOpenFiles();
            if (fs.length > 0) currentFile = new WeakReference<>(fs[0]);
        }

        enabled = true;
    }

    public void disable() {
        if (connection != null) connection.disconnect();
        connection = null;
        ApplicationManager.getApplication().removeApplicationListener(this);

        enabled = false;
    }

    public boolean isEnable() {
        return enabled;
    }

    private void update() {
        DiscordService service = discordService.get();
        if (service != null) service.updatePresence(currentProject.get(), currentFile.get());
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        Log.info("Editor change");

        Project p = event.getManager().getProject();
        if (currentProject.get() != null && !currentProject.get().getLocationHash().equals(p.getLocationHash())) currentProject = new WeakReference<>(p);
        currentFile = new WeakReference<>(event.getNewFile());

        update();
    }

    @Override
    public void applicationExiting() {
        Log.info("Exiting application");
        DiscordService.getInstance().stop();
        disable();
    }
}
