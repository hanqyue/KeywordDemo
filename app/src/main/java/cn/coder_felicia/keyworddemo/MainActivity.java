package cn.coder_felicia.keyworddemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText text;
    private EditText keyword;
    private Button sure_button;
    private CustomTextView output_text;
    private List<String> highlighted = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){

        text = (EditText) findViewById(R.id.text);
        keyword = (EditText) findViewById(R.id.keyword);
        sure_button = (Button) findViewById(R.id.sure_button);
        output_text = (CustomTextView) findViewById(R.id.output_text);

        output_text.setDefaultColor(Color.BLACK);
        output_text.setHighlight(Color.RED);
        sure_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure_button:
                highlighted.clear();
                Collections.addAll(highlighted, keyword.getText().toString().split(","));
                Log.d("highlightList",highlighted.toString());
                output_text.setDisplayedText(text.getText().toString(),highlighted);
                break;
            default:
                break;
        }
    }

}
