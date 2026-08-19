package net.labymod.addons.autochat;

import net.labymod.addons.autochat.api.AutoChatExecutor;
import net.labymod.addons.autochat.listener.AutoChatTickListener;
import net.labymod.api.addon.AddonMain;
import net.labymod.api.addon.LabyAddon;

/**
 * Hauptklasse des AutoChat-Addons.
 *
 * Die @AddonMain-Annotation sorgt dafür, dass LabyMod beim Bauen automatisch die addon.json
 * generiert - ohne sie würde LabyMod dieses Addon nicht erkennen.
 */
@AddonMain
public class AutoChatAddon extends LabyAddon<AutoChatConfiguration> {

  private AutoChatExecutor chatExecutor;
  private AutoChatTickListener tickListener;

  @Override
  protected void enable() {
    // Registriert die Einstellungsseite "AutoChat" in den normalen LabyMod-4-Einstellungen.
    this.registerSettingCategory();

    // Die versionsabhängige Implementierung von AutoChatExecutor wird von LabyMod anhand der
    // generierten ReferenceStorage-Klasse bereitgestellt (siehe core/README im Projekt).
    // WICHTIG: "ReferenceStorage" wird erst nach dem ERSTEN Gradle-Build automatisch generiert
    // (Package: net.labymod.addons.autochat.ReferenceStorage). Führe also zuerst einen Build
    // aus, füge danach den Import hinzu und baue erneut - siehe Anleitung am Ende der Antwort.
    this.chatExecutor =
        ((ReferenceStorage) this.referenceStorageAccessor()).autoChatExecutor();

    this.tickListener = new AutoChatTickListener(this);
    this.registerListener(this.tickListener);
  }

  @Override
  protected Class<? extends AutoChatConfiguration> configurationClass() {
    return AutoChatConfiguration.class;
  }

  public AutoChatExecutor chatExecutor() {
    return this.chatExecutor;
  }
}
