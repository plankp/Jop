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

import java.awt.Font;
import java.awt.Color;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.IGraphics;
import org.atoiks.games.framework2d.GameScene;

import org.atoiks.games.jop.Rectangle;

import static org.atoiks.games.jop.App.WIDTH;
import static org.atoiks.games.jop.App.HEIGHT;
import static org.atoiks.games.jop.App.TITLE_FONT;
import static org.atoiks.games.jop.App.OPTION_FONT;

public final class GameOverScreen extends GameScene {

    private final Rectangle optRestart = new Rectangle((WIDTH - 86) / 2, 262, (WIDTH + 86) / 2, 305);
    private final Rectangle optBack = new Rectangle((WIDTH - 86) / 2, 327, (WIDTH + 86) / 2, 370);

    private int from;

    @Override
    public void enter(int from) {
        this.from = from;
    }

    @Override
    public void leave() {
        optRestart.visible = optBack.visible = false;
    }

    @Override
    public void resize(int w, int h) {
        //
    }

    @Override
    public boolean update(final float dt) {
        final float mouseX = Input.getLocalX();
        final float mouseY = Input.getLocalY();

        optRestart.visible = optRestart.containsPoint(mouseX, mouseY);
        optBack.visible = optBack.containsPoint(mouseX, mouseY);

        if (Input.isMouseButtonClicked(1) || Input.isMouseButtonClicked(2) || Input.isMouseButtonClicked(3)) {
            if (optRestart.visible) {
                // Restart the previous game mode!
                scene.switchToScene(from);
                return true;
            }
            if (optBack.visible) {
                scene.switchToScene(0);
                return true;
            }
        }
        return true;
    }

    @Override
    public void render(final IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();

        g.setColor(Color.white);
        g.setFont(TITLE_FONT);
        g.drawString("GAME OVER!", 275, 132);

        g.setFont(OPTION_FONT);
        g.drawString("Again", 367, 295);
        g.drawString("Back", 372, 360);

        optRestart.drawOn(g);
        optBack.drawOn(g);
    }
}