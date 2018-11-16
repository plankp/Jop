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

import java.util.ArrayList;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.IGraphics;

import org.atoiks.games.jop.Utils;
import org.atoiks.games.jop.Config;
import org.atoiks.games.jop.Vector2;

import org.atoiks.games.jop.entity.Human;
import org.atoiks.games.jop.entity.Player;

import org.atoiks.games.jop.entity.bullet.*;

import static org.atoiks.games.jop.App.WIDTH;
import static org.atoiks.games.jop.App.HEIGHT;
import static org.atoiks.games.jop.App.SANS_FONT;

import static org.atoiks.games.jop.scene.ClassicMode.ZOMBIE_XS;
import static org.atoiks.games.jop.scene.ClassicMode.ZOMBIE_YS;
import static org.atoiks.games.jop.scene.ClassicMode.calculateEnemySpeed;
import static org.atoiks.games.jop.scene.ClassicMode.calculateUpperBound;

public final class ReverseMode extends Level {

    // Disclaimer(?): wow... so turns out both game modes have
    //                a lot more in common then expected...

    public static final int PLAYER_FAST = 120;
    public static final int PLAYER_SLOW = 88;

    private Config conf;

    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private final ArrayList<Human> humans = new ArrayList<>();

    private float time;
    private float elapsedTime;

    private int spawnIndex;

    private int kills;

    @Override
    public void init() {
        this.conf = (Config) scene.resources().get("game.cfg");
    }

    @Override
    public void enter(int from) {
        player.transform.set(WIDTH / 2, HEIGHT / 2);
        player.hp = 1;

        bullets.clear();
        humans.clear();

        time = elapsedTime = 0;
        spawnIndex = 0;
        kills = 0;
    }

    @Override
    protected void renderGame(final IGraphics g) {
        player.render(g);

        g.setColor(Color.white);
        for (final Bullet b : bullets) {
            b.render(g);
        }

        g.setColor(Color.red);
        for (final Human z : humans) {
            final Vector2 v = z.transform;
            g.drawCircle((int) v.x, (int) v.y, Human.RADIUS);
        }

        g.setColor(Color.white);
        g.setFont(SANS_FONT);
        g.drawString("Kills:", 10, 20);
        g.drawString(Integer.toString(kills), 60, 20);
        g.drawString("Health:", 10, 38);
        g.drawString(Integer.toString(player.hp), 60, 38);
    }

    @Override
    protected boolean updateGame(final float dt) {
        time += dt;
        elapsedTime += dt;

        player.dx = player.dy = 0;
        final int actualSpeed = Input.isKeyDown(conf.BTN_SHIFT) ? PLAYER_SLOW : PLAYER_FAST;
        if (Input.isKeyDown(conf.BTN_UP))       player.dy -= actualSpeed;
        if (Input.isKeyDown(conf.BTN_DOWN))     player.dy += actualSpeed;
        if (Input.isKeyDown(conf.BTN_LEFT))     player.dx -= actualSpeed;
        if (Input.isKeyDown(conf.BTN_RIGHT))    player.dx += actualSpeed;

        final float factor = calculateUpperBound(elapsedTime);
        while (time > factor) {
            time -= factor;

            spawnIndex %= 8;
            final float zx = ZOMBIE_XS[spawnIndex];
            final float zy = ZOMBIE_YS[spawnIndex];
            ++spawnIndex;

            humans.add(new Human(zx, zy, player.transform, calculateEnemySpeed(elapsedTime)));
        }

        player.update(dt);

        final float px = player.transform.x;
        final float py = player.transform.y;

        for (int i = 0; i < bullets.size(); ++i) {
            final Bullet b = bullets.get(i);
            b.update(dt);
            if (b.isOutOfScreen(WIDTH, HEIGHT)) {
                bullets.remove(i);
                if (--i < -1) break;
            }
        }

        final int mouseX = Input.getLocalX();
        final int mouseY = Input.getLocalY();

        for (int i = 0; i < humans.size(); ++i) {
            final Human z = humans.get(i);
            final Vector2 ztrans = z.transform;

            if (Utils.fastCircleCollision(ztrans.x, ztrans.y, Human.RADIUS, mouseX, mouseY, 5)) {
                if (Input.isMouseButtonDown(1)) {
                    // Player is stunned if mouse is held near, it doesnt update
                    continue;
                }
                if (Input.isMouseButtonClicked(2) || Input.isMouseButtonClicked(3)) {
                    // Player is confused
                    z.confuse();
                }
            }

            z.update(dt);
            if (z.isOutOfScreen(WIDTH, HEIGHT)) {
                humans.remove(i);
                if (--i < -1) break;
            } else if (z.wantsToFireBullet()) {
                // Fire it towards the player!
                final Bullet bullet = new PointBullet(ztrans.x, ztrans.y);

                final Vector2 k = player.transform.sub(ztrans);
                k.normalize();
                k.mulAssign(bullet.speed());
                bullet.velocity.set(k);

                bullets.add(bullet);
            }
        }

        // bullets are trying to kill the player
        for (int i = 0; i < bullets.size(); ++i) {
            final Bullet b = bullets.get(i);
            if (b.testCollision(px, py, Player.RADIUS - 5)) {
                if (--player.hp < 1) {
                    scene.switchToScene(3);
                    return true;
                }

                if (b.destroyOnCollision()) {
                    bullets.remove(i);
                    if (--i < -1) break;
                }
            }
        }

        // We are trying to kill humans by running into them
        for (int i = 0; i < humans.size(); ++i) {
            final Vector2 u = humans.get(i).transform;
            if (Utils.fastCircleCollision(u.x, u.y, Human.RADIUS, px, py, Player.RADIUS)) {
                ++kills;
                if (kills % 10 == 0) {
                    // Every 10 kills you gain one hit point
                    ++player.hp;
                }

                humans.remove(i);
                if (--i < -1) break;
            }
        }

        return true;
    }
}