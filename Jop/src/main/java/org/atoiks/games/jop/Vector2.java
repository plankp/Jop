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

import java.util.Objects;

public final class Vector2 implements Serializable {

    private static final long serialVersionUID = 1298733181L;

    public float x;
    public float y;

    public Vector2() {
    }

    public Vector2(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(final Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public static Vector2 fromAToB(final float ax, final float ay, final float bx, final float by) {
        return new Vector2(bx - ax, by - ay);
    }

    public Vector2 rotate(final float rads) {
        final Vector2 v = new Vector2(this);
        v.rotateAssign(rads);
        return v;
    }

    public void rotateAssign(final float rads) {
        if (rads != 0) {
            final float magnitude = norm();
            final float angle = rads + (float) Math.atan2(y, x);

            this.x = magnitude * (float) Math.cos(angle);
            this.y = magnitude * (float) Math.sin(angle);
        }
    }

    public void set(final Vector2 v) {
        set(v.x, v.y);
    }

    public void set(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(final Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    public void addAssign(final Vector2 v) {
        this.x += v.x;
        this.y += v.y;
    }

    public Vector2 sub(final Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    public void subAssign(final Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    public Vector2 mul(final float k) {
        return new Vector2(x * k, y * k);
    }

    public void mulAssign(final float k) {
        this.x *= k;
        this.y *= k;
    }

    public float mul(final Vector2 v) {
        return x * v.x + y * v.y;
    }

    public Vector2 div(final float k) {
        return new Vector2(x / k, y / k);
    }

    public void divAssign(final float k) {
        this.x /= k;
        this.y /= k;
    }

    public float norm() {
        return (float) Math.hypot(x, y);
    }

    public float sqrNorm() {
        return x * x + y * y;
    }

    public void normalize() {
        divAssign(norm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object r) {
        if (r == this) return true;
        if (r instanceof Vector2) {
            final Vector2 v = (Vector2) r;
            return x == v.x && y == v.y;
        }
        return false;
    }

    @Override
    public String toString() {
        return "<" + x + "," + y + ">";
    }
}