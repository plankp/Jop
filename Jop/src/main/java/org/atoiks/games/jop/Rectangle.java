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

package org.atoiks.games.jop;

import java.io.Serializable;

import org.atoiks.games.framework2d.IGraphics;

public final class Rectangle implements Serializable {

    private static final long serialVersionUID = 298034235991L;

    public float x1, y1;
    public float x2, y2;

    public boolean visible;

    public Rectangle(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void drawOn(final IGraphics g) {
        if (visible) {
            g.drawRect(x1, y1, x2, y2);
        }
    }

    public boolean containsPoint(float x, float y) {
        return x > x1 && x < x2 && y > y1 && y < y2;
    }
}