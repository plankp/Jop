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

public class Zombie {

    private static final int SCREEN_EDGE_BUFFER = 20;

    public static final int RADIUS = 8;

    public final Vector2 transform;

    private final float speed;

    private final Vector2 pTrans;

    public Zombie(float x, float y, Vector2 playerTransform, float speed) {
        this.transform = new Vector2(x, y);
        this.pTrans = playerTransform;
        this.speed = speed;
    }

    public void update(float dt) {
        final Vector2 v = pTrans.sub(transform);
        v.normalize();
        v.mulAssign(speed * dt);

        transform.addAssign(v);
    }

    public boolean isOutOfScreen(final int w, final int h) {
        return transform.x + RADIUS > w + SCREEN_EDGE_BUFFER
            || transform.x - RADIUS < -SCREEN_EDGE_BUFFER
            || transform.y + RADIUS > h + SCREEN_EDGE_BUFFER
            || transform.y - RADIUS < -SCREEN_EDGE_BUFFER;
    }
}