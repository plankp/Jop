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

package org.atoiks.games.jop.scene;

import java.awt.Color;

import java.awt.event.KeyEvent;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.IGraphics;
import org.atoiks.games.framework2d.GameScene;

import org.atoiks.games.jop.Rectangle;

import org.atoiks.games.jop.entity.Player;

import static org.atoiks.games.jop.App.WIDTH;
import static org.atoiks.games.jop.App.HEIGHT;
import static org.atoiks.games.jop.App.TITLE_FONT;
import static org.atoiks.games.jop.App.OPTION_FONT;

public abstract class Level extends GameScene {

    public static final Color PAUSE_OVERLAY = new Color(192, 192, 192, 100);

    public final Player player = new Player();

    protected boolean paused;

    private final Rectangle optResume = new Rectangle((WIDTH - 116) / 2, 262, (WIDTH + 116) / 2, 305);
    private final Rectangle optQuit = new Rectangle((WIDTH - 86) / 2, 327, (WIDTH + 86) / 2, 370);

    @Override
    public void leave() {
        optResume.visible = optQuit.visible = false;
        paused = false;
    }

    @Override
    public void resize(int w, int h) {
        //
    }

    @Override
    public final boolean update(final float dt) {
        if (paused) {
            return updatePause(dt);
        }

        if (Input.isKeyDown(KeyEvent.VK_ESCAPE)) {
            paused = true;
            return true;
        }

        return updateGame(dt);
    }

    @Override
    public final void render(final IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();

        renderGame(g);

        if (paused) {
            // The pause screen is like an overlay, rendered
            // over the actual gameplay!
            g.setColor(PAUSE_OVERLAY);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.white);

            g.setFont(TITLE_FONT);
            g.drawString("PAUSED", 325, 132);

            g.setFont(OPTION_FONT);
            g.drawString("Resume", 353, 295);
            g.drawString("Quit", 377, 360);

            optResume.drawOn(g);
            optQuit.drawOn(g);
        }
    }

    private boolean updatePause(final float dt) {
        final float mouseX = Input.getLocalX();
        final float mouseY = Input.getLocalY();

        optResume.visible = optResume.containsPoint(mouseX, mouseY);
        optQuit.visible = optQuit.containsPoint(mouseX, mouseY);

        if (Input.isMouseButtonClicked(1) || Input.isMouseButtonClicked(2) || Input.isMouseButtonClicked(3)) {
            if (optResume.visible) {
                paused = false;
                return true;
            }
            if (optQuit.visible) {
                scene.switchToScene(0);
                return true;
            }
        }

        return true;
    }

    protected abstract boolean updateGame(final float dt);

    protected abstract void renderGame(final IGraphics g);
}
