package com.example.akimoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class AdjustmentMainActivity extends Activity {

    //HSV色空間 色相(Hue) 彩度(Saturation)明度(Value)
    Button btnLoadImage;
    TextView text1;
    ImageView imageResult;
    SeekBar hBar, sBar, vBar;
    TextView hueText, satText, valText;
    Button btnResetHSV;

    final int R_IMAGE = 1;

    Uri source;
    Bitmap bitmapMaster;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjustment_activity_main);
        //btnLoadImageはボタンを押してフォルダに干渉　Button　
        btnLoadImage = (Button) findViewById(R.id.loadingimage);
        text1 = (TextView) findViewById(R.id.Adtext);
        imageResult = (ImageView) findViewById(R.id.get);


        //画像の読み込み　　フォルダー リクエスト　
        //btnLoadImageはボタンを押してフォルダに干渉
        btnLoadImage.setOnClickListener(new View.OnClickListener () {
            @Override
            //クリック時にandroidフォルダーから画像を選択できる。
            public void onClick(View arg0) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //とった画像をR_IMAGEとする
                startActivityForResult(intent, R_IMAGE);
            }
        });

        hBar = (SeekBar) findViewById(R.id.hbar);
        sBar = (SeekBar) findViewById(R.id.sbar);
        vBar = (SeekBar) findViewById(R.id.vbar);
        hueText = (TextView) findViewById(R.id.texth);
        satText = (TextView) findViewById(R.id.texts);
        valText = (TextView) findViewById(R.id.textv);
        hBar.setOnSeekBarChangeListener(seekBarChangeListener);
        sBar.setOnSeekBarChangeListener(seekBarChangeListener);
        vBar.setOnSeekBarChangeListener(seekBarChangeListener);
        btnResetHSV = (Button)findViewById(R.id.resebthsv);
        btnResetHSV.setOnClickListener(arg0 -> {
            // シークバーリセット位置 真ん中
            hBar.setProgress(250);
            sBar.setProgress(250);
            vBar.setProgress(250);

            loadBitmapHSV();
        });
    }



//フォルダーからリクエストOKだったらloadBitmapHSVを作成そのまま下の編集に移動
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode , resultCode , data );
//リクエストOKだったそして、R_IMAGE1 を取れたら
        if ( resultCode == RESULT_OK ) {
            if ( requestCode == R_IMAGE ) {
                source = data.getData ( );
                //とったデータ
                try {
                    bitmapMaster = BitmapFactory
                            .decodeStream ( getContentResolver ( ).openInputStream (
                                    source ) );

                    // reset SeekBars
                    hBar.setProgress ( 250 );
                    sBar.setProgress ( 250 );
                    vBar.setProgress ( 250 );

                    loadBitmapHSV ( );

                } catch (FileNotFoundException e) {
                    e.printStackTrace ( );
                }
            }
        }
    }


//
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener () {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    };

    private void loadBitmapHSV() {
        if (bitmapMaster != null) {

            int progressHue = hBar.getProgress() - 250;
            int progressSat = sBar.getProgress() - 250;
            int progressVal = vBar.getProgress() - 250;


            float h = (float) progressHue * 365 / 250;
            float s = (float) progressSat / 250;
            float v = (float) progressVal / 250;

            hueText.setText("色彩: " + String.valueOf(h));
            satText.setText("彩度: " + String.valueOf(s));
            valText.setText("明るさ: " + String.valueOf(v));

            imageResult.setImageBitmap(updHSV(bitmapMaster, h, s, v));

        }
    }

    //Bitmapを利用して画像のSHVを変更する
    private Bitmap updHSV ( Bitmap src , float settingHue , float settingSat ,
                            float settingVal ) {

        int wid = src.getWidth ( );
        int hid = src.getHeight ( );
        int[] mapSrcColor = new int[ wid * hid ];
        int[] mapDestColor = new int[ wid * hid ];

        float[] plHSV = new float[ 3 ];
        src.getPixels ( mapSrcColor , 0 , wid , 0 , 0 , wid , hid );

        int index = 0;
        for ( int y = 0; y < hid; ++y ) {
            for ( int x = 0; x < wid; ++x ) {
                // カラー変換
                Color.colorToHSV ( mapSrcColor[ index ] , plHSV );
                // 調整
                plHSV[ 0 ] = plHSV[ 0 ] + settingHue;
                if ( plHSV[ 0 ] < 0.0f ) {
                    plHSV[ 0 ] = 0.0f;

                 } else if ( plHSV[ 0 ] > 365.0f ) {
                    plHSV[ 0 ] = 365.0f;
                }

                plHSV[ 2 ] = plHSV[ 2 ] + settingVal;
                if ( plHSV[ 2 ] < 0.0f ) {
                    plHSV[ 2 ] = 0.0f;

                 } else if ( plHSV[ 2 ] > 1.0f ) {
                    plHSV[ 2 ] = 1.0f;
                }

                plHSV[ 1 ] = plHSV[ 1 ] + settingSat;
                if ( plHSV[ 1 ] < 0.0f ) {
                    plHSV[ 1 ] = 0.0f;

                 } else if ( plHSV[ 1 ] > 1.0f ) plHSV[ 1 ] = 1.0f;


                // リセットする
                mapDestColor[index] = Color.HSVToColor(plHSV);

                index++;
            }
        }

        return Bitmap.createBitmap(mapDestColor, wid, hid, Bitmap.Config.ARGB_8888);

    }

}