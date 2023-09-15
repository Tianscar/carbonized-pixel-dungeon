package com.tianscar.carbonizedpixeldungeon.desktop;

import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.tianscar.carbonizedpixeldungeon.PDSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalPopupMenuSeparatorUI;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DesktopCrashDialog {

    // FIXME HiDPI for this dialog not really supported yet
    static volatile float DENSITY = 1.f;

    private static InputStream getResourceAsStream(String name) throws IOException {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        if (stream == null) throw new IOException();
        else return stream;
    }

    private static void putToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    public static void show(String title, String dispMsg, String rawMsg) {

        System.setProperty("sun.java2d.uiScale", "1");
        System.setProperty("awt.useSystemAAFontSettings", "lcd");

        ImageIO.setUseCache(false);

        final Font customFont;
        Font font;
        try {
            String pixelFont;
            Locale defaultLocale = Locale.getDefault();
            if (defaultLocale.getLanguage().equals(Locale.JAPANESE.getLanguage())) pixelFont = "fusion_pixel_jp";
            else if (defaultLocale.getLanguage().equals(Locale.KOREA.getLanguage())) pixelFont = "fusion_pixel_kr";
            else if (defaultLocale.getLanguage().equals("zh")) {
                if (defaultLocale.getCountry().equals("HK") || defaultLocale.getCountry().equals("MO") || defaultLocale.getCountry().equals("TW")) {
                    pixelFont = "fusion_pixel_tc";
                }
                else pixelFont = "fusion_pixel_zh";
            }
            else pixelFont = "pixel_font_latin1";
            font = Font.createFont(Font.TRUETYPE_FONT,
                            getResourceAsStream("fonts/" + (PDSettings.systemFont() ? "droid_sans_fallback" : pixelFont) + ".ttf"))
                    .deriveFont(16.f * DENSITY);
        } catch (IOException | FontFormatException ignored) {
            font = null;
        }
        customFont = font;
        final BufferedImage crashImage;
        BufferedImage image;
        try {
            image = ImageIO.read(getResourceAsStream("icons/error_image.png"));
        } catch (IOException e) {
            image = null;
        }
        if (image == null) crashImage = null;
        else {
            crashImage = new BufferedImage(Math.round(image.getWidth() / 3.f * DENSITY), Math.round(image.getHeight() / 3.f * DENSITY),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = crashImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, AffineTransform.getScaleInstance(1 / 3.f * DENSITY, 1 / 3.f * DENSITY), null);
            g.dispose();
            image.flush();
            image = null;
        }
        List<Image> iconImages = new ArrayList<>();
        try {
            iconImages.add(ImageIO.read(getResourceAsStream("icons/icon_16.png")));
            iconImages.add(ImageIO.read(getResourceAsStream("icons/icon_32.png")));
            iconImages.add(ImageIO.read(getResourceAsStream("icons/icon_64.png")));
            iconImages.add(ImageIO.read(getResourceAsStream("icons/icon_128.png")));
            iconImages.add(ImageIO.read(getResourceAsStream("icons/icon_256.png")));
        }
        catch (IOException ignored) {
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme() {
                    private final ColorUIResource BLACK = new ColorUIResource(0x000000);
                    private final ColorUIResource WHITE = new ColorUIResource(0xFFFFFF);
                    @Override
                    protected ColorUIResource getPrimary1() { return BLACK; }
                    @Override
                    protected ColorUIResource getPrimary2() { return super.getPrimary3(); }
                    @Override
                    protected ColorUIResource getSecondary2() {
                        return super.getPrimary3();
                    }
                    @Override
                    protected ColorUIResource getSecondary3() { return WHITE; }
                });
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException ignored) {
                }
                if (customFont != null) {
                    UIManager.put("MenuItem.acceleratorFont", customFont);
                    UIManager.put("MenuItem.font", customFont);
                    UIManager.put("TextArea.font", customFont);
                    UIManager.put("Button.font", customFont);
                }
                UIManager.put("MenuItem.acceleratorDelimiter", "+");
                UIManager.put("ScrollBar.width", Math.round(UIManager.getInt("ScrollBar.width") * DENSITY));
                UIManager.put("ScrollBar.thumbShadow", new Color(0x00000000, true));
                UIManager.put("ScrollBar.thumbHighlight", new Color(0x00000000, true));
                JButton close = new JButton(DesktopMessages.get(DesktopCrashDialog.class, "close_app"));
                close.setBorder(BorderFactory.createEmptyBorder());
                close.setFocusPainted(false);
                JButton copyAll = new JButton(DesktopMessages.get(DesktopCrashDialog.class, "copy_all"));
                copyAll.setBorder(BorderFactory.createEmptyBorder());
                copyAll.setFocusPainted(false);
                JTextArea textArea = new JTextArea(dispMsg);
                textArea.setEditable(false);
                JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.setLightWeightPopupEnabled(false);
                popupMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK, Math.round(DENSITY)));
                JMenuItem copy = new JMenuItem(DesktopMessages.get(DesktopCrashDialog.class, "copy"));
                popupMenu.add(copy);
                JSeparator separator = new JPopupMenu.Separator();
                separator.setUI(new MetalPopupMenuSeparatorUI() {
                    @Override
                    public void paint(Graphics g, JComponent c) {
                        ((Graphics2D) g).scale(DENSITY, DENSITY);
                        super.paint(g, c);
                    }
                });
                popupMenu.add(separator);
                JMenuItem selectAll = new JMenuItem(DesktopMessages.get(DesktopCrashDialog.class, "select_all"));
                popupMenu.add(selectAll);
                textArea.setComponentPopupMenu(popupMenu);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.getHorizontalScrollBar().setBorder(BorderFactory.createLineBorder(Color.BLACK, Math.round(DENSITY)));
                scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createLineBorder(Color.BLACK, Math.round(DENSITY)));
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                JOptionPane pane = new JOptionPane(scrollPane, JOptionPane.ERROR_MESSAGE,
                        JOptionPane.DEFAULT_OPTION, crashImage == null ? null : new ImageIcon(crashImage), new Object[] { close, copyAll }, close);
                ActionListener actionListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if ("closeApp".equals(e.getActionCommand())) pane.setValue(JOptionPane.OK_OPTION);
                        else if ("copyAll".equals(e.getActionCommand())) {
                            putToClipboard(rawMsg);
                            copyAll.setText(DesktopMessages.get(DesktopCrashDialog.class, "copied"));
                        }
                        else if ("copy".equals(e.getActionCommand())) {
                            textArea.copy();
                        }
                        else if ("selectAll".equals(e.getActionCommand())) {
                            textArea.selectAll();
                        }
                    }
                };
                close.setMnemonic('O');
                close.setActionCommand("closeApp");
                close.addActionListener(actionListener);
                copyAll.setActionCommand("copyAll");
                copyAll.addActionListener(actionListener);
                copy.setMnemonic('C');
                copy.setAccelerator(KeyStroke.getKeyStroke('C', SharedLibraryLoader.isMac ? KeyEvent.META_DOWN_MASK : KeyEvent.CTRL_DOWN_MASK));
                copy.setActionCommand("copy");
                copy.addActionListener(actionListener);
                selectAll.setMnemonic('A');
                selectAll.setAccelerator(KeyStroke.getKeyStroke('A', SharedLibraryLoader.isMac ? KeyEvent.META_DOWN_MASK : KeyEvent.CTRL_DOWN_MASK));
                selectAll.setActionCommand("selectAll");
                selectAll.addActionListener(actionListener);
                JDialog dialog = pane.createDialog(title);
                dialog.setMinimumSize(new Dimension(Math.round(640 * DENSITY), Math.round(480 * DENSITY)));
                dialog.setPreferredSize(new Dimension(Math.round(800 * DENSITY), Math.round(600 * DENSITY)));
                dialog.pack();
                dialog.setResizable(true);
                dialog.setIconImages(iconImages);
                dialog.setLocationByPlatform(true);
                dialog.setVisible(true);
                dialog.dispose();
                System.exit(1);
            }
        });

    }

}
