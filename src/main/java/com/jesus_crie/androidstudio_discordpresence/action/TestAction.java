package com.jesus_crie.androidstudio_discordpresence.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.FakeVirtualFile;
import com.jesus_crie.androidstudio_discordpresence.Log;
import com.jesus_crie.androidstudio_discordpresence.service.DiscordService;
import com.jesus_crie.androidstudio_discordpresence.service.EditorListenerService;

public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        ApplicationInfo info = ApplicationInfo.getInstance();

        Log.info("Version Name: " + info.getVersionName());
        Log.info("Build version: " + info.getBuild().asString());
        Log.info("Composed Version: " + info.getFullVersion());
    }
}
