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
import org.atoiks.games.jop.Vector2;

public final class ArrowBullet extends Bullet {

    public static final float LENGTH = 24;

    private final Vector2 end = new Vector2();

    public ArrowBullet(float x, float y) {
        super(x, y);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        end.set(velocity);
        end.normalize();
        end.mulAssign(LENGTH);
        end.addAssign(transform);
    }

    @Override
    public void render(final IGraphics g) {
        g.drawLine(transform.x, transform.y, end.x, end.y);
    }

    @Override
    public boolean destroyOnCollision() {
        // This is pretty much the only advantage of arrow bullets
        // you can shoot through enemies
        return false;
    }

    @Override
    public float speed() {
        return 160;
    }

    @Override
    public boolean testCollision(final float x, final float y, final float r) {
        return Utils.intersectSegmentCircle(transform.x, transform.y, end.x, end.y, x, y, r);
    }

    @Override
    public boolean isOutOfScreen(final int w, final int h) {
        return isPtOutOfScreen(transform.x, transform.y, w, h)
            && isPtOutOfScreen(end.x, end.y, w, h);
    }

    private static boolean isPtOutOfScreen(final float x, float y, final int w, final int h) {
        return !(x > 0 && y > 0 && x < w && y < h);
    }
}