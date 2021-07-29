package com.example.akimoto;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
//paint機能
public class PaintMainActivity extends AppCompatActivity {
    private PaintCanvasView paintcanvasView;

    // 「取消」ボタンボタンのイベントリスナー
    public class UndoBtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) { paintcanvasView.undo();
        }
    }

    // 「クリア」ボタンのイベントリスナー
    public class ClearBtnOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            paintcanvasView.clear();
        }
    }


    // 色の選択用ラジオボタンのイベントリスナー
    public class ColorBtnOnChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            String colorStr = radioButton.getText().toString();
            switch (colorStr) {
                case "黒":
                    paintcanvasView.setColor(Color.BLACK);
                    break;
                case "赤":
                    paintcanvasView.setColor(Color.RED);
                    break;
                case "青":
                    paintcanvasView.setColor(Color.BLUE);
                    break;
                case "緑":
                    paintcanvasView.setColor(Color.GREEN);
                    break;
                case "黄":
                    paintcanvasView.setColor(Color.YELLOW);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintcanvasView = (PaintCanvasView) findViewById(R.id.PaintView);

        // 色の選択のラジオボタン
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.colorRadio);
        radioGroup.setOnCheckedChangeListener(new ColorBtnOnChangeListener());


        // 「クリア」
        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new ClearBtnOnClickListener());

        // 「取消」ボ
        Button undoButton = (Button) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new UndoBtnOnClickListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.paint_action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

