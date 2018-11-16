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

package org.atoiks.games.jop.entity;

import org.atoiks.games.jop.Vector2;

public class Human {

    private static final int SCREEN_EDGE_BUFFER = 20;

    private static final int FIRE_SEQ_LIMIT = 5;
    private static final float FIRE_TIME_LIMIT = 0.8f;

    public static final int RADIUS = 8;

    public final Vector2 transform;

    private final float speed;

    private final Vector2 pTrans;

    private float fireTime;
    private int bullets;

    private float confuseTime;

    private boolean shouldFire;

    public Human(float x, float y, Vector2 playerTransform, float speed) {
        this.transform = new Vector2(x, y);
        this.pTrans = playerTransform;
        this.speed = speed;
    }

    public void update(float dt) {
        // Confusion causes the person to move
        // backwards and not fire any bullets

        fireTime += dt;

        final boolean isConfused = confuseTime > 0;

        final Vector2 v = pTrans.sub(transform);
        v.normalize();
        v.mulAssign(speed * dt);

        if (isConfused) {
            confuseTime -= dt;
            v.mulAssign(-1);
        }

        transform.addAssign(v);

        shouldFire = false;
        if (bullets > FIRE_SEQ_LIMIT) {
            if (fireTime > FIRE_TIME_LIMIT) bullets = 0;
        } else if (fireTime > FIRE_TIME_LIMIT / FIRE_SEQ_LIMIT) {
            if (!isConfused) {
                shouldFire = true;
                ++bullets;
            }
            fireTime = 0;
        }
    }

    public boolean wantsToFireBullet() {
        if (shouldFire) {
            // Just in case this method is called multiple
            // times during one update frame!!
            shouldFire = false;
            return true;
        }
        return false;
    }

    public void confuse() {
        confuseTime = 5f;
    }

    public boolean isOutOfScreen(final int w, final int h) {
        return transform.x + RADIUS > w + SCREEN_EDGE_BUFFER
            || transform.x - RADIUS < -SCREEN_EDGE_BUFFER
            || transform.y + RADIUS > h + SCREEN_EDGE_BUFFER
            || transform.y - RADIUS < -SCREEN_EDGE_BUFFER;
    }
}