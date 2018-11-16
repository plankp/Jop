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

import java.awt.Color;

import org.atoiks.games.framework2d.IGraphics;

import org.atoiks.games.jop.Utils;
import org.atoiks.games.jop.Vector2;

import static org.atoiks.games.jop.App.WIDTH;
import static org.atoiks.games.jop.App.HEIGHT;

public class Player {

    public static final int RADIUS = 10;

    public final Vector2 transform = new Vector2();

    public int hp;

    public float dx;
    public float dy;

    public void update(float dt) {
        transform.addAssign(new Vector2(dx * dt, dy * dt));
        transform.x = Utils.clampf(transform.x, 0, WIDTH);
        transform.y = Utils.clampf(transform.y, 0, HEIGHT);
    }

    public void render(final IGraphics g) {
        g.setColor(Color.gray);
        g.fillCircle((int) transform.x, (int) transform.y, RADIUS);
    }
}