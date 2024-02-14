package com.tianscar.carbonizedpixeldungeon.utils;

import com.badlogic.gdx.utils.IntArray;

public class GrowableIntArray extends IntArray {

    @Override
    public synchronized void add(int value) {
        super.add(value);
    }

    @Override
    public synchronized int get(int index) {
        return super.get(index);
    }

    @Override
    public synchronized int removeIndex(int index) {
        return super.removeIndex(index);
    }

    public synchronized int[] elements() {
        return super.toArray();
    }

}
