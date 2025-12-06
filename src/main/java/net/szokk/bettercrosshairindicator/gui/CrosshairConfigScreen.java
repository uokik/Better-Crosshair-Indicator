package net.szokk.bettercrosshairindicator.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.szokk.bettercrosshairindicator.config.CrosshairConfig;

public class CrosshairConfigScreen extends Screen {
    private final Screen parent;
    private ColorSlider redSlider;
    private ColorSlider greenSlider;
    private ColorSlider blueSlider;
    private ColorSlider alphaSlider;
    private SizeSlider sizeSlider;

    private ButtonWidget topLeftXLabel, topLeftYLabel;
    private ButtonWidget topRightXLabel, topRightYLabel;
    private ButtonWidget bottomLeftXLabel, bottomLeftYLabel;
    private ButtonWidget bottomRightXLabel, bottomRightYLabel;

    public CrosshairConfigScreen(Screen parent) {
        super(Text.literal("Crosshair Config"));
        this.parent = parent;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    protected void init() {
        CrosshairConfig config = CrosshairConfig.getInstance();

        int sliderWidth = 180;
        int leftMargin = 20;
        int startY = 40;


        this.redSlider = new ColorSlider(leftMargin, startY, sliderWidth, 18, Text.literal("Red: "), config.getRed() / 255.0, v -> config.setRed((int) (v * 255)));
        this.addDrawableChild(redSlider);

        this.greenSlider = new ColorSlider(leftMargin, startY + 22, sliderWidth, 18, Text.literal("Green: "), config.getGreen() / 255.0, v -> config.setGreen((int) (v * 255)));
        this.addDrawableChild(greenSlider);

        this.blueSlider = new ColorSlider(leftMargin, startY + 44, sliderWidth, 18, Text.literal("Blue: "), config.getBlue() / 255.0, v -> config.setBlue((int) (v * 255)));
        this.addDrawableChild(blueSlider);

        this.alphaSlider = new ColorSlider(leftMargin, startY + 66, sliderWidth, 18, Text.literal("Alpha: "), config.getAlpha() / 255.0, v -> config.setAlpha((int) (v * 255)));
        this.addDrawableChild(alphaSlider);

        this.sizeSlider = new SizeSlider(leftMargin, startY + 88, sliderWidth, 18, Text.literal("Size: "), 50, 1, (config.getSize() - 1) / 49.0, v -> config.setSize((int) (v * 49 + 1)));
        this.addDrawableChild(sizeSlider);

        int offsetStartY = startY + 120;
        int cornerWidth = 88;
        int cornerSpacing = 8;


        addCornerControls(leftMargin, offsetStartY, "Top Left",
                config.getTopLeftOffsetX(), config.getTopLeftOffsetY(),
                config::setTopLeftOffsetX, config::setTopLeftOffsetY,
                lbl -> this.topLeftXLabel = lbl, lbl -> this.topLeftYLabel = lbl);


        addCornerControls(leftMargin + cornerWidth + cornerSpacing, offsetStartY, "Top Right",
                config.getTopRightOffsetX(), config.getTopRightOffsetY(),
                config::setTopRightOffsetX, config::setTopRightOffsetY,
                lbl -> this.topRightXLabel = lbl, lbl -> this.topRightYLabel = lbl);

        addCornerControls(leftMargin, offsetStartY + 60, "Bottom Left",
                config.getBottomLeftOffsetX(), config.getBottomLeftOffsetY(),
                config::setBottomLeftOffsetX, config::setBottomLeftOffsetY,
                lbl -> this.bottomLeftXLabel = lbl, lbl -> this.bottomLeftYLabel = lbl);

        addCornerControls(leftMargin + cornerWidth + cornerSpacing, offsetStartY + 60, "Bottom Right",
                config.getBottomRightOffsetX(), config.getBottomRightOffsetY(),
                config::setBottomRightOffsetX, config::setBottomRightOffsetY,
                lbl -> this.bottomRightXLabel = lbl, lbl -> this.bottomRightYLabel = lbl);

        int buttonY = offsetStartY + 130;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Reset Offsets"), button -> {
            config.resetOffsets();
            updateAllOffsetLabels();
        }).dimensions(leftMargin, buttonY, sliderWidth, 18).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> this.close())
                .dimensions(leftMargin, buttonY + 22, sliderWidth, 18).build());
    }

    private void addCornerControls(int x, int y, String corner, int initX, int initY,
                                   java.util.function.Consumer<Integer> setX,
                                   java.util.function.Consumer<Integer> setY,
                                   java.util.function.Consumer<ButtonWidget> xLabelRef,
                                   java.util.function.Consumer<ButtonWidget> yLabelRef) {
        int labelW = 45;
        int btnW = 18;

        this.addDrawableChild(ButtonWidget.builder(Text.literal(corner), b -> {})
                .dimensions(x, y, 88, 13).build());


        ButtonWidget xLabel = ButtonWidget.builder(Text.literal("X: " + initX), b -> {})
                .dimensions(x, y + 15, labelW, 16).build();
        this.addDrawableChild(xLabel);
        xLabelRef.accept(xLabel);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("-"), button -> {
            int current = parseOffsetValue(xLabel.getMessage().getString());
            setX.accept(current - 1);
            xLabel.setMessage(Text.literal("X: " + (current - 1)));
        }).dimensions(x + labelW + 2, y + 15, btnW, 16).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("+"), button -> {
            int current = parseOffsetValue(xLabel.getMessage().getString());
            setX.accept(current + 1);
            xLabel.setMessage(Text.literal("X: " + (current + 1)));
        }).dimensions(x + labelW + btnW + 4, y + 15, btnW, 16).build());

        ButtonWidget yLabel = ButtonWidget.builder(Text.literal("Y: " + initY), b -> {})
                .dimensions(x, y + 33, labelW, 16).build();
        this.addDrawableChild(yLabel);
        yLabelRef.accept(yLabel);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("-"), button -> {
            int current = parseOffsetValue(yLabel.getMessage().getString());
            setY.accept(current - 1);
            yLabel.setMessage(Text.literal("Y: " + (current - 1)));
        }).dimensions(x + labelW + 2, y + 33, btnW, 16).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("+"), button -> {
            int current = parseOffsetValue(yLabel.getMessage().getString());
            setY.accept(current + 1);
            yLabel.setMessage(Text.literal("Y: " + (current + 1)));
        }).dimensions(x + labelW + btnW + 4, y + 33, btnW, 16).build());
    }

    private int parseOffsetValue(String text) {
        try {
            String[] parts = text.split(": ");
            if (parts.length == 2) {
                return Integer.parseInt(parts[1]);
            }
        } catch (Exception ignored) {}
        return 0;
    }

    private void updateAllOffsetLabels() {
        if (topLeftXLabel != null) topLeftXLabel.setMessage(Text.literal("X: 0"));
        if (topLeftYLabel != null) topLeftYLabel.setMessage(Text.literal("Y: 0"));
        if (topRightXLabel != null) topRightXLabel.setMessage(Text.literal("X: 0"));
        if (topRightYLabel != null) topRightYLabel.setMessage(Text.literal("Y: 0"));
        if (bottomLeftXLabel != null) bottomLeftXLabel.setMessage(Text.literal("X: 0"));
        if (bottomLeftYLabel != null) bottomLeftYLabel.setMessage(Text.literal("Y: 0"));
        if (bottomRightXLabel != null) bottomRightXLabel.setMessage(Text.literal("X: 0"));
        if (bottomRightYLabel != null) bottomRightYLabel.setMessage(Text.literal("Y: 0"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, this.title, 20, 20, 0xFFFFFF);

        String helpText = "BCI config";
        int helpX = this.width / 2 - this.textRenderer.getWidth(helpText) / 2;
        context.drawTextWithShadow(this.textRenderer, helpText, helpX, 10, 0xFFFFFF00);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    private static class ColorSlider extends SliderWidget {
        private final Text prefix;
        private final SliderCallback callback;

        public ColorSlider(int x, int y, int width, int height, Text prefix, double value, SliderCallback callback) {
            super(x, y, width, height, Text.empty(), value);
            this.prefix = prefix;
            this.callback = callback;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Text.literal(prefix.getString() + (int)(this.getCurrentValue() * 255)));
        }

        @Override
        protected void applyValue() {
            callback.onValueChanged(this.getCurrentValue());
        }

        public double getCurrentValue() {
            return this.value;
        }

        @FunctionalInterface
        interface SliderCallback {
            void onValueChanged(double value);
        }
    }

    private static class SizeSlider extends SliderWidget {
        private final Text prefix;
        private final int maxValue;
        private final int minValue;
        private final SizeCallback callback;

        public SizeSlider(int x, int y, int width, int height, Text prefix, int maxValue, int minValue, double value, SizeCallback callback) {
            super(x, y, width, height, Text.empty(), value);
            this.prefix = prefix;
            this.maxValue = maxValue;
            this.minValue = minValue;
            this.callback = callback;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int displayValue = (int)(this.getCurrentValue() * (maxValue - minValue) + minValue);
            this.setMessage(Text.literal(prefix.getString() + displayValue));
        }

        @Override
        protected void applyValue() {
            callback.onValueChanged(this.getCurrentValue());
        }

        public double getCurrentValue() {
            return this.value;
        }

        @FunctionalInterface
        interface SizeCallback {
            void onValueChanged(double value);
        }
    }
}
