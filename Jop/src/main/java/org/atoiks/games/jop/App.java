/**
 *  Jop
 *  Copyright (C) 2017-2018  Atoiks-Games <atoiks-games@outlook.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.atoiks.games.jop;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.awt.Font;
import java.awt.Color;
import java.awt.FontFormatException;

import org.atoiks.games.framework2d.Scene;
import org.atoiks.games.framework2d.IGraphics;
import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.swing.Frame;

import org.atoiks.games.jop.scene.*;

public class App {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static final Font SANS_FONT;

    public static final Font TITLE_FONT;
    public static final Font OPTION_FONT;

    static {
        Font local = null;
        try {
            local = Font.createFont(Font.PLAIN, App.class.getResourceAsStream("/Logisoso.ttf"));
        } catch (IOException | FontFormatException ex) {
            // Fallback to using a generic SansSerif font
            local = new Font("SansSerif", Font.PLAIN, 16);
        } finally {
            // SANS_FONT is initialized as size 16 plain
            SANS_FONT = local.deriveFont(Font.PLAIN, 16f);
        }

        TITLE_FONT = SANS_FONT.deriveFont(64.0f);
        OPTION_FONT = SANS_FONT.deriveFont(32.0f);
    }

    public static void main(String[] args) {
        final FrameInfo info = new FrameInfo()
                .setTitle("Atoiks Games - Jop")
                .setSize(WIDTH, HEIGHT)
                .setResizable(false)
                .setFps(60.0f)
                .setLoader(new DummyLoader())
                .setGameScenes(new TitleScreen(), new ClassicMode(), new ReverseMode(), new GameOverScreen(), new HelpScreen(), new SettingsScreen());

        final Frame frame = new Frame(info);
        try {
            frame.init();
            frame.loop();
        } finally {
            final Config cfg = (Config) frame.getSceneManager().resources().get("game.cfg");
            frame.close();

            // Save config
            try (final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./game.cfg"))) {
                oos.writeObject(cfg);
            } catch (IOException ex) {
                // Next time, game will launch with default configurations
            }
        }
    }
}

final class DummyLoader extends Scene {

    private boolean firstRun = true;

    @Override
    public void render(IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();
    }

    @Override
    public boolean update(float dt) {
        if (firstRun) {
            firstRun = false;
        } else {
            // Load configuration file from "current" directory
            try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./game.cfg"))) {
                final Config cfg = (Config) ois.readObject();
                scene.resources().put("game.cfg", cfg == null ? new Config() : cfg);
            } catch (IOException | ClassNotFoundException ex) {
                // Supply default configuration
                scene.resources().put("game.cfg", new Config());
            }

            scene.gotoNextScene();
        }
        return true;
    }

    @Override
    public void resize(int w, int h) {
        //
    }
}