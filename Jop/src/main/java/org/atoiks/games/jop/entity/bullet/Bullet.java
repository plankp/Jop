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

import org.atoiks.games.jop.Vector2;

public abstract class Bullet {

    public final Vector2 transform;
    public final Vector2 velocity;

    public Bullet(float x, float y) {
        this.transform = new Vector2(x, y);
        this.velocity = new Vector2();
    }

    public void update(final float dt) {
        transform.addAssign(velocity.mul(dt));
    }

    public boolean destroyOnCollision() {
        return true;
    }

    public abstract float speed();

    public abstract void render(IGraphics g);

    public abstract boolean testCollision(float x, float y, float r);

    public abstract boolean isOutOfScreen(int w, int h);
}