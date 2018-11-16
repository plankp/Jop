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

import org.atoiks.games.jop.Config;
import org.atoiks.games.jop.Rectangle;

import static org.atoiks.games.jop.App.WIDTH;
import static org.atoiks.games.jop.App.HEIGHT;
import static org.atoiks.games.jop.App.OPTION_FONT;

public final class HelpScreen extends GameScene {

    private Config conf;

    private final Rectangle optBack = new Rectangle((WIDTH - 86) / 2, 451, (WIDTH + 86) / 2, 494);

    @Override
    public void resize(int w, int h) {
        //
    }

    @Override
    public void init() {
        this.conf = (Config) scene.resources().get("game.cfg");
    }

    @Override
    public void leave() {
        optBack.visible = false;
    }

    @Override
    public boolean update(final float dt) {
        final float mouseX = Input.getLocalX();
        final float mouseY = Input.getLocalY();

        optBack.visible = optBack.containsPoint(mouseX, mouseY);

        if (Input.isMouseButtonClicked(1) || Input.isMouseButtonClicked(2) || Input.isMouseButtonClicked(3)) {
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

        final String movePlayerStr = makeMovePlayerString();

        g.setColor(Color.white);
        g.setFont(OPTION_FONT);
        g.drawString("Classic Mode", 40, 82);
        g.drawString(movePlayerStr, 88, 114);
        g.drawString("Left click to fire bullet", 88, 146);
        g.drawString("Right/Middle click to fire arrow", 88, 178);
        g.drawString("Shoot the red dots before they consume you", 88, 210);

        g.drawString("Reverse Mode", 40, 260);
        g.drawString(movePlayerStr, 88, 294);
        g.drawString("Hold " + conf.btnShiftName() + " for slower movement", 88, 326);
        g.drawString("Hold Left near red dot to disable them", 88, 358);
        g.drawString("Right/Middle click near red dot to scare them", 88, 390);
        g.drawString("Consume the red dots before they shoot you", 88, 422);

        g.drawString("Back", 372, 484);

        optBack.drawOn(g);
    }

    private String makeMovePlayerString() {
        return new StringBuilder()
                .append(conf.btnUpName()).append(' ')
                .append(conf.btnDownName()).append(' ')
                .append(conf.btnLeftName()).append(' ')
                .append(conf.btnRightName())
                .append(" to move player")
                .toString();
    }
}