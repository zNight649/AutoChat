package net.labymod.addons.autochat.api;

/**
 * Version-unabhängige Schnittstelle zum tatsächlichen Senden einer Chatnachricht an den
 * Minecraft-Server. Die konkrete Implementierung ist zwangsläufig versionsabhängig, da der
 * Netzwerk-Handler (ClientPacketListener / NetHandlerPlayClient) sich je nach Minecraft-Version
 * im Package/Namen leicht unterscheiden kann.
 *
 * Siehe: https://dev.labymod.net/pages/addon/features/version-dependent/
 * (Muster "ExampleChatExecutor" aus der offiziellen LabyMod-4-Doku, hier für das Senden statt
 * nur Anzeigen von Nachrichten adaptiert.)
 */
public interface AutoChatExecutor {

  /**
   * Sendet die übergebene Nachricht so an den Server, als hätte der Spieler sie selbst im Chat
   * eingegeben und mit Enter bestätigt.
   *
   * @param message die zu sendende Nachricht (darf nie leer/null sein - das wird vorher geprüft)
   */
  void sendChatMessage(String message);
}
