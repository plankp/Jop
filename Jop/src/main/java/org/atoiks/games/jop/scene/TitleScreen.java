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

public final class TitleScreen extends GameScene {

    private final Rectangle optStartClassic = new Rectangle((WIDTH - 180) / 2, 262, (WIDTH + 180) / 2, 305);
    private final Rectangle optStartReverse = new Rectangle((WIDTH - 180) / 2, 305, (WIDTH + 180) / 2, 348);
    private final Rectangle optHelp = new Rectangle((WIDTH - 86) / 2, 349, (WIDTH + 86) / 2, 392);
    private final Rectangle optConfig = new Rectangle((WIDTH - 104) / 2, 393, (WIDTH + 104) / 2, 436);
    private final Rectangle optQuit = new Rectangle((WIDTH - 86) / 2, 456, (WIDTH + 86) / 2, 499);

    @Override
    public void resize(int w, int h) {
        //
    }

    @Override
    public void leave() {
        optStartClassic.visible = false;
        optStartReverse.visible = false;
        optHelp.visible = false;
        optConfig.visible = false;
        optQuit.visible = false;
    }

    @Override
    public boolean update(final float dt) {
        final float mouseX = Input.getLocalX();
        final float mouseY = Input.getLocalY();

        optStartClassic.visible = optStartClassic.containsPoint(mouseX, mouseY);
        optStartReverse.visible = optStartReverse.containsPoint(mouseX, mouseY);
        optHelp.visible = optHelp.containsPoint(mouseX, mouseY);
        optConfig.visible = optConfig.containsPoint(mouseX, mouseY);
        optQuit.visible = optQuit.containsPoint(mouseX, mouseY);

        if (Input.isMouseButtonClicked(1) || Input.isMouseButtonClicked(2) || Input.isMouseButtonClicked(3)) {
            if (optStartClassic.visible) {
                scene.switchToScene(1);
                return true;
            }
            if (optStartReverse.visible) {
                scene.switchToScene(2);
                return true;
            }
            if (optHelp.visible) {
                scene.switchToScene(4);
                return true;
            }
            if (optConfig.visible) {
                scene.switchToScene(5);
                return true;
            }
            if (optQuit.visible) {
                return false;
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
        g.drawString("Jop", 358, 132);

        g.setFont(OPTION_FONT);
        g.drawString("Classic Mode", 320, 295);
        g.drawString("Reverse Mode", 316, 338);
        g.drawString("Help", 376, 382);
        g.drawString("Settings", 351, 426);
        g.drawString("Quit", 377, 489);

        optStartClassic.drawOn(g);
        optStartReverse.drawOn(g);
        optHelp.drawOn(g);
        optConfig.drawOn(g);
        optQuit.drawOn(g);
    }
}