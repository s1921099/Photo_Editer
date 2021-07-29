package com.example.akimoto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PaintCanvasView extends View {
    // すべての線の管理
    private ArrayList<Line> lines = new ArrayList<Line>();
    // 一本の線
    private Line aLine;
    // 描画色
    private int currentColor = Color.BLACK;
    // コンテキスト
    private Context context;
    // 線の太さの初期値
    public int lineWidth = 12;

    // コンストラクター
    public PaintCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PaintCanvasView(Context context) {
        super(context);
    }

    // 最後の線を消去する
    public void undo() {
        if (lines.size() > 0) {
            lines.remove(lines.size() - 1);
        }
        invalidate();
    }

    // すべての線を消去する
    public void clear() {
        lines.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAll(canvas);
    }

    // すべての線を描画する
    public void drawAll(Canvas canvas) {
        // 背景を白に
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        // アンチエイリアスを有効に
        paint.setAntiAlias(true);

        // すべての線を描画する
        for (Line line : lines) {
            // 色を設定
            paint.setColor(line.getColor());
            // 線幅を設定
            paint.setStrokeWidth(lineWidth);
            // ポイントをつなげて一本の線を描画する
            for (int i = 0; i < (line.getPoints().size() - 1); i++) {
                Point s = line.getPoints().get(i);
                Point e = line.getPoints().get(i + 1);
                // 2点間の線を引く
                canvas.drawLine(s.x, s.y, e.x, e.y, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 新しい線を生成
                aLine = new Line(currentColor);
                // linesに線を追加
                lines.add(aLine);
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                Point p = new Point(x, y);
                // 線にポイントを追加
                aLine.addPoint(p);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        // 画面を再描画
        invalidate();
        return true;
    }

    // 色を設定する
    public void setColor(int c) {
        currentColor = c;
    }
}
