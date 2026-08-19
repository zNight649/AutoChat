package net.labymod.addons.autochat;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.ConfigName;
import net.labymod.api.configuration.settings.annotation.SliderSetting;
import net.labymod.api.configuration.settings.annotation.SwitchSetting;
import net.labymod.api.configuration.settings.annotation.TextFieldSetting;

/**
 * Die Einstellungen von AutoChat. Diese Klasse wird von LabyMod automatisch als JSON im
 * .minecraft/laby-addons-config/ Ordner gespeichert und beim Start wieder geladen - die
 * Einstellungen bleiben also nach einem Minecraft-Neustart erhalten, ohne dass wir selbst etwas
 * dafür programmieren müssen.
 *
 * WICHTIG: Die genauen Package-Pfade der Annotationen (SwitchSetting, TextFieldSetting,
 * SliderSetting, ConfigName, ConfigProperty, AddonConfig) können sich zwischen LabyMod-4-API-
 * Versionen leicht verschieben. Öffne dieses Projekt in IntelliJ (nachdem du die LabyMod-API als
 * Abhängigkeit über das offizielle Template eingebunden hast) und nutze bei rot markierten
 * Klassen "Alt+Enter" -> "Import Class", damit IntelliJ automatisch den bei dir aktuell
 * korrekten Import setzt, falls sich hier etwas geändert haben sollte.
 */
@ConfigName("settings")
public class AutoChatConfiguration extends AddonConfig {

  /**
   * 1) AutoChat aktiviert - Ein/Aus-Schalter, Standardmäßig: Aus
   */
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(false);

  /**
   * 2) Nachricht - freies Textfeld. maxLength großzügig gewählt, damit auch längere Texte
   * möglich sind (Minecraft-Chat-Limit liegt ohnehin bei 256 Zeichen).
   */
  @TextFieldSetting(maxLength = 256)
  private final ConfigProperty<String> message = new ConfigProperty<>("Hallo ich bin Peter");

  /**
   * 3) Intervall in Minuten. Per SliderSetting mit min = 1 ist es technisch unmöglich, 0 oder
   * einen negativen Wert einzustellen - das deckt die Anforderung "Intervall darf nicht 0 oder
   * negativ sein" bereits auf UI-Ebene ab. Zusätzlich wird im Code (AutoChatTickListener) noch
   * einmal defensiv geprüft.
   */
  @SliderSetting(min = 1, max = 180, steps = 1)
  private final ConfigProperty<Integer> intervalMinutes = new ConfigProperty<>(10);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<String> message() {
    return this.message;
  }

  public ConfigProperty<Integer> intervalMinutes() {
    return this.intervalMinutes;
  }
}
