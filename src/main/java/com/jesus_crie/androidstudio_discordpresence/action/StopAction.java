package com.jesus_crie.androidstudio_discordpresence.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jesus_crie.androidstudio_discordpresence.Log;
import com.jesus_crie.androidstudio_discordpresence.service.DiscordService;
import com.jesus_crie.androidstudio_discordpresence.service.EditorListenerService;

public class StopAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        EditorListenerService.getInstance().disable();
        DiscordService.getInstance().stop();

        Log.info("Service stopped !");
    }
}
