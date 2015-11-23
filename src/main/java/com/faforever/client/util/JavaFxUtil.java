package com.faforever.client.util;

import com.faforever.client.preferences.PreferencesService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

/**
 * Utility class to fix some annoying JavaFX shortcomings.
 */
public class JavaFxUtil {

  public static final StringConverter<Path> PATH_STRING_CONVERTER = new StringConverter<Path>() {
    @Override
    public String toString(Path object) {
      if (object == null) {
        return null;
      }
      return object.toAbsolutePath().toString();
    }

    @Override
    public Path fromString(String string) {
      if (string == null) {
        return null;
      }
      return Paths.get(string);
    }
  };
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final double ZOOM_STEP = 0.2d;

  private JavaFxUtil() {
    // Utility class
  }

  public static void makeNumericTextField(TextField textField) {
    makeNumericTextField(textField, -1);
  }

  public static void makeNumericTextField(TextField textField, int maxLength) {
    textField.textProperty().addListener((observable, oldValue, newValue) -> {
      String value = newValue;
      if (!value.matches("\\d*")) {
        value = newValue.replaceAll("[^\\d]", "");
      }

      if (maxLength > 0 && value.length() > maxLength) {
        value = value.substring(0, maxLength);
      }

      textField.setText(value);
      if (textField.getCaretPosition() > textField.getLength()) {
        textField.positionCaret(textField.getLength());
      }
    });
  }

  /**
   * Uses reflection to change to tooltip delay/duration to some sane values.
   * <p>
   * See <a href="https://javafx-jira.kenai.com/browse/RT-19538">https://javafx-jira.kenai.com/browse/RT-19538</a>
   */
  public static void fixTooltipDuration() {
    try {
      Field fieldBehavior = Tooltip.class.getDeclaredField("BEHAVIOR");
      fieldBehavior.setAccessible(true);
      Object objBehavior = fieldBehavior.get(null);

      Field activationTimerField = objBehavior.getClass().getDeclaredField("activationTimer");
      activationTimerField.setAccessible(true);
      Timeline objTimer = (Timeline) activationTimerField.get(objBehavior);

      objTimer.getKeyFrames().setAll(new KeyFrame(new Duration(500)));

      Field hideTimerField = objBehavior.getClass().getDeclaredField("hideTimer");
      hideTimerField.setAccessible(true);
      objTimer = (Timeline) hideTimerField.get(objBehavior);

      objTimer.getKeyFrames().setAll(new KeyFrame(new Duration(100000)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Centers a window FOR REAL. https://javafx-jira.kenai.com/browse/RT-40368
   */
  public static void centerOnScreen(Stage stage) {
    double width = stage.getWidth();
    double height = stage.getHeight();

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX((screenBounds.getMaxX() - screenBounds.getMinX() - width) / 2);
    stage.setY((screenBounds.getMaxY() - screenBounds.getMinY() - height) / 2);
  }

  public static void assertApplicationThread() {
    if (!Platform.isFxApplicationThread()) {
      throw new IllegalStateException("Must run in FX Application thread");
    }
  }

  public static void assertBackgroundThread() {
    if (Platform.isFxApplicationThread()) {
      throw new IllegalStateException("Must not run in FX Application thread");
    }
  }

  public static void configureWebView(WebView webView, PreferencesService preferencesService) {
    webView.setContextMenuEnabled(false);
    webView.setOnScroll(event -> {
      if (event.isControlDown()) {
        if (event.getDeltaY() > 0) {
          webView.setZoom(webView.getZoom() + ZOOM_STEP);
        } else {
          webView.setZoom(webView.getZoom() - ZOOM_STEP);
        }
      }
    });
    webView.setOnKeyPressed(event -> {
      if (event.isControlDown() && (event.getCode() == KeyCode.DIGIT0 || event.getCode() == KeyCode.NUMPAD0)) {
        webView.setZoom(1);
      }
    });

    String theme = preferencesService.getPreferences().getTheme();

    WebEngine engine = webView.getEngine();
    engine.setUserDataDirectory(preferencesService.getCacheDirectory().toFile());
    try {
      engine.setUserStyleSheetLocation(new ClassPathResource(ThemeUtil.themeFile(theme, "style-webview.css")).getURL().toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean isVisibleRecursively(Node node) {
    if (!node.isVisible()) {
      return false;
    }

    Parent parent = node.getParent();
    if (parent == null) {
      return node.getScene() != null;
    }
    return isVisibleRecursively(parent);
  }

  public static <T> void bindOnApplicationThread(Property<T> property, Callable<T> function, ObservableValue<?>... dependencies) {
    Runnable updateFunction = () -> Platform.runLater(() -> {
      try {
        property.setValue(function.call());
      } catch (Exception e) {
        logger.warn("Error while updating bound value", e);
      }
    });

    updateFunction.run();
    for (ObservableValue<?> dependency : dependencies) {
      dependency.addListener(observable -> updateFunction.run());
    }
  }
  public static String toRgbCode(Color color) {
    return String.format("#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }
}
