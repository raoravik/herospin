/*
 * Copyright (c) 2016. Ravi Rao.
 *
 * This file is created as part of VISA POC and  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.visa.r4r.poc.herospin.customview;

/**
 * @author LiYanZhao
 * @date 16-3-19 上午10:51
 */
public class Point {

    private float x;
    private float y;
    private float originX;
    private float originY;
    private float diffX;
    private float diffY;
    private char[] label;

    public Point() {
        set(0, 0);
    }

    public Point(float x, float y) {
        set(x, y);
    }

    public Point(Point Point) {
        set(Point.x, Point.y);
        this.label = Point.label;
    }

    public void update(float scale) {
        x = originX + diffX * scale;
        y = originY + diffY * scale;
    }

    public void finish() {
        set(originX + diffX, originY + diffY);
    }

    public Point set(float x, float y) {
        this.x = x;
        this.y = y;
        this.originX = x;
        this.originY = y;
        this.diffX = 0;
        this.diffY = 0;
        return this;
    }

    public Point setTarget(float targetX, float targetY) {
        set(x, y);
        this.diffX = targetX - originX;
        this.diffY = targetY - originY;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

}
