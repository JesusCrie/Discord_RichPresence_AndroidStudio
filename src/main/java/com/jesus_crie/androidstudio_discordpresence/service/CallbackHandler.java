package com.jesus_crie.androidstudio_discordpresence.service;

import club.minnced.discord.rpc.DiscordRPC;

import java.lang.ref.WeakReference;

public class CallbackHandler extends Thread {

    private final WeakReference<DiscordRPC> rpc;

    public CallbackHandler(DiscordRPC rpc) {
        this.rpc = new WeakReference<>(rpc);
        setDaemon(true);
        setName("Thread [Callback Handler]");
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            DiscordRPC rpc = this.rpc.get();
            if (rpc == null) interrupt();
            else rpc.Discord_RunCallbacks();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {}
        }
    }
}
