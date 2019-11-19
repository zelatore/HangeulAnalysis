package zelatore.kaist.ac.hangeulanalysis;

class KeyObject {
    public String appName;
    public String inputChar;
    public String inputType;
    public String keyboardType;
    public double keyDistance;
    public long eventTime;

    public KeyObject(String appName, String inputChar, long eventTime) {
        this.appName = appName;
        this.inputChar = inputChar;
        this.inputType = getInputCharType(inputChar);
    }

    private String getInputCharType(String str) {
        if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))           return "한글";
        else if(str.matches("^[a-zA-Z]*$"))                 return "영문";
        else if(str.matches("^[0-9]*$"))                    return "숫자";
        else                                                        return "특수문자";
    }


}

