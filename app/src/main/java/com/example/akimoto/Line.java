package com.example.akimoto;

import android.graphics.Point;

import java.util.ArrayList;

public class Line {
    // 線の色
    private int color;
    // 1本の線
    private ArrayList<Point> points;

    // コンストラクタ
    public Line(int color) {
        points = new ArrayList<Point>();
        this.color = color;
    }

    // 色を取得
    public int getColor() {
        return color;
    }
    // 1本の線を構成する点のArrayListを戻す
    public ArrayList<Point> getPoints() {
        return points;
    }

    // 点を線に追加
    public void addPoint(Point p) {
        points.add(p);
    }
}
