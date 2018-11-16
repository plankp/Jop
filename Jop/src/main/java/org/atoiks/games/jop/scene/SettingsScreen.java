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

import java.awt.event.KeyEvent;

import java.util.HashSet;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.IGraphics;
import org.atoiks.games.framework2d.GameScene;

import org.atoiks.games.jop.Config;
import org.atoiks.games.jop.Rectangle;

import static org.atoiks.games.jop.App.WIDTH;
import static org.atoiks.games.jop.App.HEIGHT;
import static org.atoiks.games.jop.App.TITLE_FONT;
import static org.atoiks.games.jop.App.OPTION_FONT;

import static org.atoiks.games.jop.scene.Level.PAUSE_OVERLAY;

public final class SettingsScreen extends GameScene {

    private enum ConfigType {
        NONE, BTN_UP, BTN_DOWN, BTN_LEFT, BTN_RIGHT, BTN_SHIFT;
    }

    private Config conf;

    private final Rectangle optBack = new Rectangle((WIDTH - 86) / 2, 491, (WIDTH + 86) / 2, 534);

    private final Rectangle optBtnUp = new Rectangle(320, 70, 400, 110);
    private final Rectangle optBtnDown = new Rectangle(320, 110, 400, 150);
    private final Rectangle optBtnLeft = new Rectangle(320, 150, 400, 190);
    private final Rectangle optBtnRight = new Rectangle(320, 190, 400, 230);
    private final Rectangle optBtnShift = new Rectangle(320, 230, 400, 270);

    private ConfigType config;
    private int lastDownKey = KeyEvent.VK_UNDEFINED;

    @Override
    public void resize(int w, int h) {
        //
    }

    @Override
    public void init() {
        this.conf = (Config) scene.resources().get("game.cfg");
    }

    @Override
    public void enter(int from) {
        setConfig(ConfigType.NONE);
    }

    private void setConfig(ConfigType newCfg) {
        config = newCfg;
        lastDownKey = Input.getLastDownKey();
    }

    @Override
    public void leave() {
        optBack.visible = false;
        optBtnUp.visible = false;
        optBtnDown.visible = false;
        optBtnLeft.visible = false;
        optBtnRight.visible = false;
        optBtnShift.visible = false;
    }

    @Override
    public boolean update(final float dt) {
        if (config == ConfigType.NONE) {
            updateMenu(dt);
        } else {
            updateConfiguration();
        }
        return true;
    }

    private void updateConfiguration() {
        int k = Input.getLastDownKey();
        if (k != lastDownKey) {
            switch (config) {
                case BTN_UP:    conf.BTN_UP = k; break;
                case BTN_DOWN:  conf.BTN_DOWN = k; break;
                case BTN_LEFT:  conf.BTN_LEFT = k; break;
                case BTN_RIGHT: conf.BTN_RIGHT = k; break;
                case BTN_SHIFT: conf.BTN_SHIFT = k; break;
            }
            setConfig(ConfigType.NONE);
        }
    }

    private void updateMenu(float dt) {
        final float mouseX = Input.getLocalX();
        final float mouseY = Input.getLocalY();

        optBack.visible = optBack.containsPoint(mouseX, mouseY);
        optBtnUp.visible = optBtnUp.containsPoint(mouseX, mouseY);
        optBtnDown.visible = optBtnDown.containsPoint(mouseX, mouseY);
        optBtnLeft.visible = optBtnLeft.containsPoint(mouseX, mouseY);
        optBtnRight.visible = optBtnRight.containsPoint(mouseX, mouseY);
        optBtnShift.visible = optBtnShift.containsPoint(mouseX, mouseY);

        if (Input.isMouseButtonClicked(1) || Input.isMouseButtonClicked(2) || Input.isMouseButtonClicked(3)) {
            if (optBack.visible) {
                if (hasNoControlCollisions()) {
                    scene.switchToScene(0);
                }
                return;
            }

            if (optBtnUp.visible) {
                setConfig(ConfigType.BTN_UP);
            } else if (optBtnDown.visible) {
                setConfig(ConfigType.BTN_DOWN);
            } else if (optBtnLeft.visible) {
                setConfig(ConfigType.BTN_LEFT);
            } else if (optBtnRight.visible) {
                setConfig(ConfigType.BTN_RIGHT);
            } else if (optBtnShift.visible) {
                setConfig(ConfigType.BTN_SHIFT);
            }
        }
    }

    @Override
    public void render(final IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();

        g.setColor(Color.white);
        g.setFont(OPTION_FONT);

        g.drawString("Up", 320, 100);
        g.drawString(conf.btnUpName(), 430, 100);
        g.drawString("Down", 320, 140);
        g.drawString(conf.btnDownName(), 430, 140);
        g.drawString("Left", 320, 180);
        g.drawString(conf.btnLeftName(), 430, 180);
        g.drawString("Right", 320, 220);
        g.drawString(conf.btnRightName(), 430, 220);
        g.drawString("Shift", 320, 260);
        g.drawString(conf.btnShiftName(), 430, 260);

        g.drawString("Back", 372, 524);

        optBack.drawOn(g);
        optBtnUp.drawOn(g);
        optBtnDown.drawOn(g);
        optBtnLeft.drawOn(g);
        optBtnRight.drawOn(g);
        optBtnShift.drawOn(g);

        if (config != ConfigType.NONE) {
            g.setColor(PAUSE_OVERLAY);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.white);
            g.drawString("PRESS NEW KEY", 310, 275);
        }
    }

    private boolean hasNoControlCollisions() {
        final HashSet<Integer> set = new HashSet<>();

        set.add(conf.BTN_UP);
        set.add(conf.BTN_DOWN);
        set.add(conf.BTN_LEFT);
        set.add(conf.BTN_RIGHT);
        set.add(conf.BTN_SHIFT);

        return set.size() == 5;
    }
}