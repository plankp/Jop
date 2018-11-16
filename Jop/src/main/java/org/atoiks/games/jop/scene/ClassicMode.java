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

import org.atoiks.games.jop.entity.Player;
import org.atoiks.games.jop.entity.Zombie;

import org.atoiks.games.jop.entity.bullet.*;

import static org.atoiks.games.jop.App.WIDTH;
import static org.atoiks.games.jop.App.HEIGHT;
import static org.atoiks.games.jop.App.SANS_FONT;

public final class ClassicMode extends Level {

    private enum BulletType {
        POINT, ARROW;
    }

    public static final float TILT_ANGLE = (float) Math.PI / 7;

    public static final float[] ZOMBIE_XS = {
        -10, WIDTH / 2, WIDTH + 10, WIDTH + 10,
        WIDTH + 10, WIDTH / 2, -10, -10,
    };
    public static final float[] ZOMBIE_YS = {
        -10, -10, -10, HEIGHT / 2,
        HEIGHT + 10, HEIGHT + 10, HEIGHT + 10, HEIGHT / 2,
    };

    private Config conf;

    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private final ArrayList<Zombie> zombies = new ArrayList<>();

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
        zombies.clear();

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
        for (final Zombie z : zombies) {
            final Vector2 v = z.transform;
            g.drawCircle((int) v.x, (int) v.y, Zombie.RADIUS);
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
        if (Input.isKeyDown(conf.BTN_UP))       player.dy -= 50;
        if (Input.isKeyDown(conf.BTN_DOWN))     player.dy += 50;
        if (Input.isKeyDown(conf.BTN_LEFT))     player.dx -= 50;
        if (Input.isKeyDown(conf.BTN_RIGHT))    player.dx += 50;

        final float buf = calculateSpeedBuf();
        player.dy *= buf;
        player.dx *= buf;

        final float factor = calculateUpperBound(elapsedTime);
        while (time > factor) {
            time -= factor;

            spawnIndex %= 8;
            final float zx = ZOMBIE_XS[spawnIndex];
            final float zy = ZOMBIE_YS[spawnIndex];
            ++spawnIndex;

            zombies.add(new Zombie(zx, zy, player.transform, calculateEnemySpeed(elapsedTime)));
        }

        player.update(dt);

        final float px = player.transform.x;
        final float py = player.transform.y;

        if (Input.isMouseButtonClicked(1)) {
            configureBullet(BulletType.POINT);
        }

        if (Input.isMouseButtonClicked(2) || Input.isMouseButtonClicked(3)) {
            configureBullet(BulletType.ARROW);
        }

        for (int i = 0; i < bullets.size(); ++i) {
            final Bullet b = bullets.get(i);
            b.update(dt);
            if (b.isOutOfScreen(WIDTH, HEIGHT)) {
                bullets.remove(i);
                if (--i < -1) break;
            }
        }

        for (int i = 0; i < zombies.size(); ++i) {
            final Zombie z = zombies.get(i);
            z.update(dt);
            if (z.isOutOfScreen(WIDTH, HEIGHT)) {
                zombies.remove(i);
                if (--i < -1) break;
            }
        }

        outer:
        for (int i = 0; i < bullets.size(); ++i) {
            final Bullet b = bullets.get(i);
            for (int j = 0; j < zombies.size(); ++j) {
                final Vector2 z = zombies.get(j).transform;
                if (b.testCollision(z.x, z.y, Zombie.RADIUS)) {
                    // Assumes one shot one kill
                    ++kills;
                    if (kills % 15 == 0) {
                        // Every 15 kills you gain one hit point
                        ++player.hp;
                    }

                    zombies.remove(j);
                    if (b.destroyOnCollision()) {
                        bullets.remove(i);
                        if (--i < -1) break outer;
                        continue outer;
                    }

                    if (--j < -1) break;
                }
            }
        }

        for (int i = 0; i < zombies.size(); ++i) {
            final Vector2 u = zombies.get(i).transform;
            if (Utils.fastCircleCollision(u.x, u.y, Zombie.RADIUS, px, py, Player.RADIUS)) {
                // Welp, this is literally a one shot kill!
                if (--player.hp < 1) {
                    scene.switchToScene(3);
                    return true;
                }

                zombies.remove(i);
                if (--i < -1) break;
            }
        }

        return true;
    }

    public static float calculateUpperBound(final float x) {
        return (float) Math.sqrt(-Math.min(x, 52) + 225) - 13;
    }

    public static float calculateEnemySpeed(final float x) {
        return Math.min(500, (float) Math.exp(0.005 * x) + 74);
    }

    private float calculateSpeedBuf() {
        // Cap speed buf at max of 3
        if (kills > 76) {
            return 3;
        }
        return 1 + kills / 38.0f;
    }

    private void configureBullet(final BulletType type) {
        final int mouseX = Input.getLocalX();
        final int mouseY = Input.getLocalY();

        final Vector2 pTrans = player.transform;

        final Vector2 k = new Vector2(mouseX, mouseY);
        k.subAssign(pTrans);
        k.normalize();
        k.mulAssign(calculateSpeedBuf());

        switch (type) {
            case POINT: {
                final PointBullet bullet = new PointBullet(pTrans.x, pTrans.y);
                bullet.velocity.set(k.mul(bullet.speed()));
                bullets.add(bullet);
                break;
            }
            case ARROW: {
                final ArrowBullet bMid = new ArrowBullet(pTrans.x, pTrans.y);
                bMid.velocity.set(k.mul(bMid.speed()).rotate(0));
                bullets.add(bMid);

                final ArrowBullet bHigh = new ArrowBullet(pTrans.x, pTrans.y);
                bHigh.velocity.set(k.mul(bHigh.speed()).rotate(TILT_ANGLE));
                bullets.add(bHigh);

                final ArrowBullet bLow = new ArrowBullet(pTrans.x, pTrans.y);
                bLow.velocity.set(k.mul(bLow.speed()).rotate(-TILT_ANGLE));
                bullets.add(bLow);
                break;
            }
        }
    }
}
