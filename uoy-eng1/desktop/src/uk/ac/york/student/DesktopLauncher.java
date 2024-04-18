package uk.ac.york.student;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.HdpiMode;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
/**
 * The {@link DesktopLauncher} class is the entry point for the application.
 * It contains the main method which sets up the application configuration and launches the game.
 */
public class DesktopLauncher {
	/**
	 * The main method is the entry point for the application.
	 * It creates an instance of {@link Lwjgl3ApplicationConfiguration} and sets various configuration options.
	 * These options include the foreground FPS, windowed mode dimensions, HDPI mode, title, idle FPS, resizability, and initial background color.
	 * After setting the configuration options, it creates a new instance of {@link Lwjgl3Application} with a new {@link GdxGame} and the configured options.
	 *
	 * @param arg The command-line arguments passed to the application. This parameter is not used in the method.
	 */
	public static void main(String[] arg) {
		final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60); // Vsynced to 60fps (no need to go above/below this)
		config.setWindowedMode(1920, 1080);
		config.setHdpiMode(HdpiMode.Logical); // Convert coordinates to be logical (scaled to 1920x1080)
		config.setTitle("ENG1");
		config.setIdleFPS(15); // Ensure game doesn't take up unnecessary resources when idle
		config.setResizable(true);
		config.setInitialBackgroundColor(Color.WHITE);
		new Lwjgl3Application(new GdxGame(), config);
	}
}
