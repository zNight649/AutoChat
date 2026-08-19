package net.labymod.addons.autochat.listener;

import net.labymod.addons.autochat.AutoChatAddon;
import net.labymod.addons.autochat.AutoChatConfiguration;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.game.GameTickEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;

/**
 * Enthält die komplette AutoChat-Timer-Logik.
 *
 * Funktionsweise:
 * - GameTickEvent (PRE) feuert 20x pro Sekunde. Wir zählen diese Ticks in {@link #tickCounter}
 *   hoch und rechnen sie in Minuten um (20 Ticks/Sekunde * 60 Sekunden = 1200 Ticks/Minute).
 * - Erst wenn tickCounter die eingestellte Anzahl Ticks für das Intervall erreicht hat, wird
 *   EINMALIG eine Nachricht gesendet und der Zähler auf 0 zurückgesetzt - es wird also
 *   garantiert NICHT bei jedem Tick etwas gesendet.
 * - Beim Betreten eines Servers (ServerJoinEvent) wird der Zähler auf 0 gesetzt -> der Timer
 *   startet sauber von vorne.
 * - Beim Verlassen/Kicken (ServerDisconnectEvent) wird der Zähler ebenfalls auf 0 gesetzt, damit
 *   beim nächsten Betreten wieder bei 0 begonnen wird.
 */
public class AutoChatTickListener {

  private static final int TICKS_PER_SECOND = 20;
  private static final int SECONDS_PER_MINUTE = 60;
  private static final int TICKS_PER_MINUTE = TICKS_PER_SECOND * SECONDS_PER_MINUTE; // 1200

  private final AutoChatAddon addon;
  private int tickCounter = 0;

  public AutoChatTickListener(AutoChatAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onServerJoin(ServerJoinEvent event) {
    // Timer startet beim Betreten eines Servers sauber von vorne.
    this.tickCounter = 0;
  }

  @Subscribe
  public void onServerDisconnect(ServerDisconnectEvent event) {
    // Timer wird beim Verlassen zurückgesetzt, damit er beim nächsten Join wieder bei 0 beginnt.
    this.tickCounter = 0;
  }

  @Subscribe
  public void onTick(GameTickEvent event) {
    // Nur die PRE-Phase verwenden, damit die Logik nicht doppelt (PRE + POST) ausgeführt wird.
    if (event.phase() != GameTickEvent.Phase.PRE) {
      return;
    }

    AutoChatConfiguration config = this.addon.configuration();

    // Wenn AutoChat deaktiviert ist, darf keine Nachricht gesendet werden - und der Zähler soll
    // auch nicht weiterlaufen, damit nach dem erneuten Aktivieren nicht sofort gesendet wird.
    if (!config.enabled().get()) {
      this.tickCounter = 0;
      return;
    }

    int intervalMinutes = config.intervalMinutes().get();
    if (intervalMinutes <= 0) {
      // Zusätzliche defensive Absicherung, obwohl der SliderSetting-Wert bereits min = 1 hat.
      return;
    }

    this.tickCounter++;

    int intervalTicks = intervalMinutes * TICKS_PER_MINUTE;
    if (this.tickCounter < intervalTicks) {
      return;
    }

    // Intervall erreicht -> Zähler zurücksetzen und Nachricht senden.
    this.tickCounter = 0;
    this.sendConfiguredMessage(config);
  }

  private void sendConfiguredMessage(AutoChatConfiguration config) {
    String message = config.message().get();
    if (message == null || message.trim().isEmpty()) {
      // Wenn die Nachricht leer ist, darf nichts gesendet werden.
      return;
    }

    this.addon.chatExecutor().sendChatMessage(message);
  }
}
