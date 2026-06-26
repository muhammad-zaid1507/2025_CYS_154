package inventorymanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class SalesChartPanel extends JPanel {

    private List<String> labels = new ArrayList<>();
    private List<Double> values = new ArrayList<>();
    private final String chartTitle;

    private static final Color[] BAR_COLORS = {
            Theme.ACCENT,
            Theme.SUCCESS,
            Theme.INFO,
            Theme.WARNING,
            new Color(167, 139, 250),
            new Color(251, 113, 133),
            new Color(52,  211, 153)
    };

    public SalesChartPanel(String chartTitle) {
        this.chartTitle = chartTitle;
        setBackground(Theme.BG_SECONDARY);
    }

    public void setData(List<String> labels, List<Double> values) {
        this.labels = new ArrayList<>(labels);
        this.values = new ArrayList<>(values);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // ── Title ────────────────────────────────────────────────────────────
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Theme.TEXT_PRIMARY);
        FontMetrics titleFm = g2.getFontMetrics();
        g2.drawString(chartTitle, (w - titleFm.stringWidth(chartTitle)) / 2, 28);

        // ── No data ───────────────────────────────────────────────────────────
        if (labels == null || labels.isEmpty()) {
            g2.setFont(new Font("Arial", Font.PLAIN, 13));
            g2.setColor(Theme.TEXT_SECONDARY);
            String msg = "No sales data available yet.";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2);
            g2.dispose();
            return;
        }

        // ── Chart dimensions ──────────────────────────────────────────────────
        int leftPad = 72;
        int rightPad = 20;
        int topPad  = 50;
        int botPad  = 58;
        int chartW  = w - leftPad - rightPad;
        int chartH  = h - topPad - botPad;

        // ── Max value ─────────────────────────────────────────────────────────
        double maxVal = values.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        if (maxVal == 0) maxVal = 1;

        // ── Grid lines + Y labels ─────────────────────────────────────────────
        int gridLines = 5;
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= gridLines; i++) {
            int y = topPad + chartH - (i * chartH / gridLines);
            g2.setColor(Theme.BORDER);
            if (i > 0) g2.drawLine(leftPad, y, leftPad + chartW, y);
            g2.setColor(Theme.TEXT_SECONDARY);
            double val = maxVal * i / gridLines;
            String lbl = val >= 1000 ? String.format("%.0fK", val / 1000) : String.format("%.0f", val);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(lbl, leftPad - fm.stringWidth(lbl) - 6, y + 4);
        }

        // ── Axes ──────────────────────────────────────────────────────────────
        g2.setColor(new Color(80, 85, 120));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(leftPad, topPad, leftPad, topPad + chartH);
        g2.drawLine(leftPad, topPad + chartH, leftPad + chartW, topPad + chartH);
        g2.setStroke(new BasicStroke(1f));

        // ── Bars ──────────────────────────────────────────────────────────────
        int barCount = labels.size();
        int barWidth = Math.min(72, chartW / barCount - 18);
        int spacing  = (chartW - barCount * barWidth) / (barCount + 1);

        for (int i = 0; i < barCount; i++) {
            double val   = values.get(i);
            int    barH  = (int) (val / maxVal * chartH);
            int    x     = leftPad + spacing + i * (barWidth + spacing);
            int    y     = topPad + chartH - barH;
            Color  color = BAR_COLORS[i % BAR_COLORS.length];

            // Bar body
            g2.setColor(color);
            if (barH > 6) {
                g2.fillRect(x, y + 6, barWidth, barH - 6);
                g2.fill(new RoundRectangle2D.Float(x, y, barWidth, 12, 8, 8));
            } else if (barH > 0) {
                g2.fillRect(x, y, barWidth, barH);
            }

            // Top highlight
            g2.setColor(color.brighter());
            if (barH > 0) g2.fillRect(x, y, barWidth, Math.min(3, barH));

            // Value label above bar
            if (barH > 0) {
                g2.setFont(new Font("Arial", Font.BOLD, 10));
                g2.setColor(Theme.TEXT_PRIMARY);
                String valStr = val >= 1000
                        ? String.format("%.1fK", val / 1000)
                        : String.format("%.0f", val);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(valStr, x + (barWidth - fm.stringWidth(valStr)) / 2, y - 5);
            }

            // X label
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            g2.setColor(Theme.TEXT_SECONDARY);
            String label = labels.get(i);
            if (label.length() > 12) label = label.substring(0, 10) + "..";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, x + (barWidth - fm.stringWidth(label)) / 2, topPad + chartH + 16);

            // Dot under label
            g2.setColor(color);
            g2.fillOval(x + barWidth / 2 - 3, topPad + chartH + 22, 6, 6);
        }

        g2.dispose();
    }
}