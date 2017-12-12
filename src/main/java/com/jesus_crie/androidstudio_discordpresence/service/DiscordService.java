package com.jesus_crie.androidstudio_discordpresence.service;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.jesus_crie.androidstudio_discordpresence.Log;
import com.jesus_crie.androidstudio_discordpresence.Utils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DiscordService {
    public static DiscordService getInstance() {
        return ServiceManager.getService(DiscordService.class);
    }

    private static Map<String, String> ICONS = new HashMap<String, String>() {{
        put("java", "java");
        put("json", "json");
        put("manifest", "manifest");
        put("plain_text", "text");
        put("xml", "xml");
        put("groovy", "groovy");
    }};

    private static final String APP_ID = "386814617080299521";

    private long startTime;
    private CallbackHandler callbackHandler;
    private final DiscordRPC rpc = DiscordRPC.INSTANCE;
    private final DiscordEventHandlers handlers = new DiscordEventHandlers();
    private final DiscordRichPresence presence = new DiscordRichPresence();

    public DiscordService() {
        handlers.ready = this::onReady;
        handlers.disconnected = this::onDisconnect;
        handlers.errored = this::onError;
    }

    public void init() {
        startTime = System.currentTimeMillis() / 1000;
        callbackHandler = new CallbackHandler(rpc);
        callbackHandler.start();

        rpc.Discord_Initialize(APP_ID, handlers, true, null);

        ApplicationInfo info = ApplicationInfo.getInstance();
        presence.smallImageText = info.getVersionName() + " " + info.getFullVersion();
        presence.smallImageKey = "logo";
        presence.startTimestamp = startTime;
    }

    public void stop() {
        presence.largeImageKey = null;
        presence.largeImageText = null;
        presence.smallImageKey = null;
        presence.smallImageText = null;
        presence.state = null;
        presence.details = null;
        rpc.Discord_UpdatePresence(presence);

        rpc.Discord_Shutdown();
        callbackHandler.interrupt();
        Messages.showInfoMessage("Discord RPC has been successfully ended !", "Information");
    }

    public void updatePresence(@Nullable Project project, @Nullable VirtualFile file) {
        if (file != null) {
            String ext = file.getFileType().getName().toLowerCase();
            if (ext.isEmpty()) ext = "unknown";
            presence.largeImageKey = ICONS.getOrDefault(ext, "unknown");
            presence.largeImageText = "Editing " + Utils.capitalize(ext) + " file";
            presence.details = "Editing: " + file.getName();
        } else {
            Log.info("Update presence with no file");
            presence.largeImageText = null;
            presence.largeImageKey = null;
            presence.details = null;
        }

        if (project != null) presence.state = "Working on " + project.getName();
        else presence.state = "No project open";

        rpc.Discord_UpdatePresence(presence);
    }

    private void onReady() {
        Log.info("Discord ready !");
        ApplicationManager.getApplication().invokeLater(() -> Messages.showInfoMessage("Discord RPC ready !", "Information"));
    }

    private void onDisconnect(int errorCode, String message) {
        Log.info("Disconnected from Discord (" + errorCode + "): " + message);
    }

    private void onError(int errorCode, String message) {
        Log.info("Discord Error (" + errorCode + "): " + message);
    }
}
