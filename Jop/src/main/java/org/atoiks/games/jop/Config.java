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

import java.awt.event.KeyEvent;

public final class Config implements Serializable {

    private static final long serialVersionUID = -289448921971L;

    public int BTN_UP;
    public int BTN_DOWN;
    public int BTN_LEFT;
    public int BTN_RIGHT;
    public int BTN_SHIFT;

    public Config() {
        reset();
    }

    public void reset() {
        BTN_UP = KeyEvent.VK_W;
        BTN_DOWN = KeyEvent.VK_S;
        BTN_LEFT = KeyEvent.VK_A;
        BTN_RIGHT = KeyEvent.VK_D;
        BTN_SHIFT = KeyEvent.VK_SHIFT;
    }

    public String btnUpName() {
        return converter(BTN_UP);
    }

    public String btnDownName() {
        return converter(BTN_DOWN);
    }

    public String btnLeftName() {
        return converter(BTN_LEFT);
    }

    public String btnRightName() {
        return converter(BTN_RIGHT);
    }

    public String btnShiftName() {
        return converter(BTN_SHIFT);
    }

    private static String converter(final int vk) {
        switch (vk) {
            case KeyEvent.VK_SHIFT:
                return "Shift";
            case KeyEvent.VK_ENTER:
                return "Enter";
            case KeyEvent.VK_CONTROL:
                return "Ctrl";
            case KeyEvent.VK_ALT:
                return "Alt";
            case KeyEvent.VK_WINDOWS:
                return "Windows";
            case KeyEvent.VK_META:
                return "Meta";
            case KeyEvent.VK_BACK_SPACE:
                return "Backspace";
            case KeyEvent.VK_TAB:
                return "Tab";
            case KeyEvent.VK_DELETE:
                return "Del";
            default:
                return KeyEvent.getKeyText(vk);
        }
    }
}