package zelatore.kaist.ac.hangeulanalysis;

import android.content.pm.PackageManager;

class KeyObject {
    private final String[] c_key = {
            "l", "ㆍ", "ㅡ", "backspace",
            "ㄱ", "ㅋ", "ㄴ", "ㄹ", "ㄷ", "ㅌ",
            "ㅂ", "ㅍ", "ㅅ", "ㅎ","ㅈ", "ㅊ", ".", "?", "!",
            "ㅇ","ㅁ"," ", "@",
            "ㅏ","ㅑ","ㅓ","ㅕ","ㅗ","ㅛ","ㅜ","ㅠ","ㅡ","ㅣ",
            "ㅐ","ㅔ","ㅒ", "ㅖ", "ㅙ", "ㅞ", "ㅘ", "ㅝ", "ㅚ", "ㅟ", "ㅢ"
    };

    private final float[] c_coordinateX = {
            1,2,3,4,
            1,1,2,2,3,3,
            1,1,2,2,3,3,4,4,4,
            2,2,3,4,
            2,2,1,1,3,3,2,2,3,1,
            1,1,1,1,1,1,2,1,1,1,1
    };

    private final float[] c_coordinateY = {
            1,1,1,1,
            2,2,2,2,2,2,
            3,3,3,3,3,3,3,3,3,
            4,4,4,4,
            1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1
    };

    private final String[] q_kor_key = {
            "1", "2", "3", "4","5", "6", "7", "8","9","0",
            "ㅂ", "ㅈ", "ㄷ", "ㄱ", "ㅅ", "ㅛ", "ㅕ", "ㅑ", "ㅐ", "ㅔ",
            "ㅃ","ㅉ","ㄸ","ㄲ","ㅆ","ㅒ","ㅖ",
            "ㅁ", "ㄴ", "ㅇ", "ㄹ","ㅎ", "ㅗ", "ㅓ", "ㅏ", "ㅣ",
            "ㅋ","ㅌ","ㅊ", "ㅍ","ㅠ","ㅜ","ㅡ", "backspace",
            "@", " ", "."
    };

    private final String[] q_eng_key = {
            "1", "2", "3", "4","5", "6", "7", "8","9","0",
            "q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
            "a", "s", "d", "f","g", "h", "j", "k", "l",
            "z","x","c", "v","b","n","m", "backspace",
            "@", " ", "."
    };

    private final float[] q_kor_coordinateX = {
            1,2,3,4,5,6,7,8,9,10,
            1,2,3,4,5,6,7,8,9,10,
            1,2,3,4,5,8,9,
            1.5f,2.5f,3.5f,4.5f,5.5f,6.5f,7.5f,8.5f,9.5f,
            2.5f,3.5f,4.5f,5.5f,6.5f,7.5f,8.5f,9.5f,
            3,6,8.5f
    };

    private final float[] q_kor_coordinateY = {
            1,1,1,1,1,1,1,1,1,1,
            2,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,2,2,
            3,3,3,3,3,3,3,3,3,
            4,4,4,4,4,4,4,4,
            5,5,5
    };


    private final float[] q_eng_coordinateX = {
            1,2,3,4,5,6,7,8,9,10,
            1,2,3,4,5,6,7,8,9,10,
            1.5f,2.5f,3.5f,4.5f,5.5f,6.5f,7.5f,8.5f,9.5f,
            2.5f,3.5f,4.5f,5.5f,6.5f,7.5f,8.5f,9.5f,
            3,6,8.5f
    };

    private final float[] q_eng_coordinateY = {
            1,1,1,1,1,1,1,1,1,1,
            2,2,2,2,2,2,2,2,2,2,
            3,3,3,3,3,3,3,3,3,
            4,4,4,4,4,4,4,4,
            5,5,5
    };


    public String appName;
    public String inputChar;
    public String inputType;
    public String keyboardType;
    public float posX;
    public float posY;
    public String keyDistance;
    public long eventTime;

    public KeyObject(String appName, String inputChar, boolean isChunjin) {
        this.appName = appName;
        this.inputChar = inputChar;
        this.inputType = getInputCharType(inputChar);
        this.eventTime = System.currentTimeMillis();
        this.posX = findPositionX(inputChar, isChunjin);
        this.posY = findPositionY(inputChar, isChunjin);
    }

    public void setupChunjin(boolean isChunjin) {
        this.posX = findPositionX(this.inputChar, isChunjin);
        this.posY = findPositionY(this.inputChar, isChunjin);
    }


    private float findPositionX(String ch, boolean chunjinFlag) {
        if(this.inputType.equals("H")) {
            if(chunjinFlag) {
                for(int i=0;i<c_key.length;i++) {
                    if(c_key[i].equals(ch))
                        return c_coordinateX[i];
                }
            }
            else {
                for(int i=0;i<q_kor_key.length;i++) {
                    if(q_kor_key[i].equals(ch))
                        return q_kor_coordinateX[i];
                }
            }
        }
        else if(this.inputType.equals("E")) {
            for(int i=0;i<q_eng_key.length;i++) {
                if(q_eng_key[i].equals(ch))
                    return q_eng_coordinateX[i];
            }
        }
        return 0;
    }

    private float findPositionY(String ch, boolean chunjinFlag) {
        if(this.inputType.equals("H")) {
            if(chunjinFlag) {
                for(int i=0;i<c_key.length;i++) {
                    if(c_key[i].equals(ch))
                        return c_coordinateY[i];
                }
            }
            else {
                for(int i=0;i<q_kor_key.length;i++) {
                    if(q_kor_key[i].equals(ch))
                        return q_kor_coordinateY[i];
                }
            }
        }
        else if(this.inputType.equals("E")) {
            for(int i=0;i<q_eng_key.length;i++) {
                if(q_eng_key[i].equals(ch))
                    return q_eng_coordinateY[i];
            }
        }
        return 0;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getInputChar() {
        return inputChar;
    }

    public void setInputChar(String inputChar) {
        this.inputChar = inputChar;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getKeyboardType() {
        return keyboardType;
    }

    public void setKeyboardType(String keyboardType) {
        this.keyboardType = keyboardType;
    }

    public String getKeyDistance() {
        return keyDistance;
    }

    public void setKeyDistance(String keyDistance) {
        this.keyDistance = keyDistance;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    private String getInputCharType(String str) {
        if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))    return "H";
        else if(str.matches("^[a-zA-Z]*$"))         return "E";
        else if(str.matches("^[0-9]*$"))            return "N";
        else                                               return "S";
    }

}

