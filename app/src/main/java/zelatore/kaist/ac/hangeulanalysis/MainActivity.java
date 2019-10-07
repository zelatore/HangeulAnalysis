package zelatore.kaist.ac.hangeulanalysis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    String totalStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.w("AA", "--------------------------------------------------");
                String currentStr = editText.getText().toString();
                //Log.w("AA", "현재 문자열: "+currentStr);
                String decomposeCurrentStr = Hangul.hangulToJaso(currentStr);
                Log.w("AA", "Current 문자 분해: "+decomposeCurrentStr);

                String decomposeTotalStr = Hangul.hangulToJaso(totalStr);
                Log.w("AA", "Total 문자 분해: "+decomposeTotalStr);

                /* 사용자가 새로운 키를 입력한 경우 */
//                if(decomposeCurrentStr.length() > decomposeTotalStr.length()) {
//                    String currentInputChar = decomposeCurrentStr.replace(decomposeTotalStr,"");
//                    Log.w("AA", "입력 문자: "+currentInputChar+",  문자 타입: "+getInputCharType(currentInputChar));
//                }
                if(decomposeCurrentStr.length() >= decomposeTotalStr.length()) {
                    String ch = decomposeCurrentStr.charAt(decomposeCurrentStr.length()-1)+"";
                    Log.w("AA", "입력 문자: "+ch+",  문자 타입: "+getInputCharType(ch));
                }
                /* 사용자가 글자를 지운 경우: backspace 입력 */
                else if(decomposeCurrentStr.length() < decomposeTotalStr.length()) {
                    Log.w("AA", "입력 문자: backspace, 문자 타입: 특수문자");
                }
//                else {
//                    String ch = decomposeCurrentStr.charAt(decomposeCurrentStr.length()-1)+"";
//                    Log.w("AA", "입력 문자: "+ch+",  문자 타입: "+getInputCharType(ch));
//                }
                Log.w("AA", "--------------------------------------------------");
                totalStr = currentStr;
            }
        });

    }

    private String getInputCharType(String str) {
        if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))           return "한글";
        else if(str.matches("^[a-zA-Z]*$"))                 return "영문";
        else if(str.matches("^[0-9]*$"))                    return "숫자";
        //else if(str.matches("[ !@#$%^&*(),.?\":{}|<>]"))    return "특수문자";
        else                                                        return "특수문자";
    }
}
