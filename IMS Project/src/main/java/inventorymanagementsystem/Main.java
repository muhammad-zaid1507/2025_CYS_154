package inventorymanagementsystem;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applyDarkTheme();
            new LoginFrame();
        });
    }

    public static void applyDarkTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // ── Panels ───────────────────────────────────────────────────────────
        UIManager.put("Panel.background",              Theme.BG_PRIMARY);

        // ── Labels ───────────────────────────────────────────────────────────
        UIManager.put("Label.foreground",              Theme.TEXT_PRIMARY);

        // ── Text Fields ──────────────────────────────────────────────────────
        UIManager.put("TextField.background",          Theme.BG_TERTIARY);
        UIManager.put("TextField.foreground",          Theme.TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground",     Theme.TEXT_PRIMARY);
        UIManager.put("TextField.selectionBackground", Theme.ACCENT);
        UIManager.put("TextField.selectionForeground", Color.WHITE);
        UIManager.put("TextField.border",
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BORDER),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        UIManager.put("PasswordField.background",          Theme.BG_TERTIARY);
        UIManager.put("PasswordField.foreground",          Theme.TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground",     Theme.TEXT_PRIMARY);
        UIManager.put("PasswordField.selectionBackground", Theme.ACCENT);

        // ── Buttons ──────────────────────────────────────────────────────────
        UIManager.put("Button.background", Theme.BG_TERTIARY);
        UIManager.put("Button.foreground", Theme.TEXT_PRIMARY);
        UIManager.put("Button.select",     Theme.BG_SECONDARY);
        UIManager.put("Button.focus",      Theme.ACCENT);

        // ── Tables ───────────────────────────────────────────────────────────
        UIManager.put("Table.background",          Theme.BG_PRIMARY);
        UIManager.put("Table.foreground",          Theme.TEXT_PRIMARY);
        UIManager.put("Table.gridColor",           Theme.BORDER);
        UIManager.put("Table.selectionBackground", Theme.ROW_SELECTED);
        UIManager.put("Table.selectionForeground", Theme.TEXT_PRIMARY);
        UIManager.put("TableHeader.background",    Theme.BG_TERTIARY);
        UIManager.put("TableHeader.foreground",    Theme.TEXT_PRIMARY);
        UIManager.put("TableHeader.cellBorder",
                BorderFactory.createMatteBorder(0, 0, 1, 1, Theme.BORDER));

        // ── Scroll ───────────────────────────────────────────────────────────
        UIManager.put("ScrollPane.background",  Theme.BG_PRIMARY);
        UIManager.put("Viewport.background",    Theme.BG_PRIMARY);
        UIManager.put("ScrollBar.background",   Theme.BG_SECONDARY);
        UIManager.put("ScrollBar.thumb",        Theme.BG_TERTIARY);
        UIManager.put("ScrollBar.thumbHighlight", Theme.BORDER);

        // ── Tabs ─────────────────────────────────────────────────────────────
        UIManager.put("TabbedPane.background",           Theme.BG_SECONDARY);
        UIManager.put("TabbedPane.foreground",           Theme.TEXT_PRIMARY);
        UIManager.put("TabbedPane.selected",             Theme.BG_TERTIARY);
        UIManager.put("TabbedPane.tabAreaBackground",    Theme.BG_SECONDARY);
        UIManager.put("TabbedPane.contentAreaColor",     Theme.BG_PRIMARY);
        UIManager.put("TabbedPane.unselectedBackground", Theme.BG_SECONDARY);
        UIManager.put("TabbedPane.darkShadow",           Theme.BORDER);
        UIManager.put("TabbedPane.shadow",               Theme.BORDER);
        UIManager.put("TabbedPane.light",                Theme.BG_TERTIARY);
        UIManager.put("TabbedPane.highlight",            Theme.BG_TERTIARY);
        UIManager.put("TabbedPane.focus",                Theme.ACCENT);

        // ── ComboBox ─────────────────────────────────────────────────────────
        UIManager.put("ComboBox.background",          Theme.BG_TERTIARY);
        UIManager.put("ComboBox.foreground",          Theme.TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", Theme.ACCENT);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("List.background",              Theme.BG_TERTIARY);
        UIManager.put("List.foreground",              Theme.TEXT_PRIMARY);
        UIManager.put("List.selectionBackground",     Theme.ACCENT);
        UIManager.put("List.selectionForeground",     Color.WHITE);

        // ── Dialogs & Option Panes ────────────────────────────────────────────
        UIManager.put("OptionPane.background",        Theme.BG_SECONDARY);
        UIManager.put("OptionPane.messageForeground", Theme.TEXT_PRIMARY);
        UIManager.put("Dialog.background",            Theme.BG_SECONDARY);
    }
}