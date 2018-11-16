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

package org.atoiks.games.jop.entity.bullet;

import org.atoiks.games.framework2d.IGraphics;

import org.atoiks.games.jop.Utils;

public final class PointBullet extends Bullet {

    public static final int RADIUS = 4;

    public PointBullet(float x, float y) {
        super(x, y);
    }

    @Override
    public void render(final IGraphics g) {
        g.drawCircle((int) transform.x, (int) transform.y, RADIUS);
    }

    @Override
    public float speed() {
        return 128;
    }

    @Override
    public boolean testCollision(final float x, final float y, final float r) {
        return Utils.fastCircleCollision(transform.x, transform.y, RADIUS, x, y, r);
    }

    @Override
    public boolean isOutOfScreen(final int w, final int h) {
        return transform.x + RADIUS > w
            || transform.x - RADIUS < 0
            || transform.y + RADIUS > h
            || transform.y - RADIUS < 0;
    }
}