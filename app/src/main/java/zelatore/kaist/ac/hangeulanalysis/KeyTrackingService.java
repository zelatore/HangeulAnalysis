package zelatore.kaist.ac.hangeulanalysis;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashMap;
import java.util.Map;

public class KeyTrackingService extends AccessibilityService {
    private String totalStr="";
    private boolean isLocked = false;
    HashMap<Integer, KeyObject> inputKeyObj = new HashMap<Integer, KeyObject>();
    int keyIdx=0;
    String keyType="E";
    boolean isChunjin;

    @Override
    public void onInterrupt() {}

//    @Override
//    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
//        String eventTypeStr = AccessibilityEvent.eventTypeToString(accessibilityEvent.getEventType());
//        Log.i("AccessibilityService","-------------------------------");
//        Log.i("AccessibilityService",eventTypeStr+".........");
//        if(accessibilityEvent.getPackageName() != null) {
//            String packageName = accessibilityEvent.getPackageName().toString();
//            Log.i("AccessibilityService", "package name: "+ packageName);
//        }
//
////        final AccessibilityNodeInfo textNodeInfo = findTextViewNode(getRootInActiveWindow());
////        if (textNodeInfo == null) return;
////
////        Rect rect = new Rect();
////        textNodeInfo.getBoundsInScreen(rect);
////        Log.i("AA", "The TextView Node: " + rect.toString());
//
//
//        List<AccessibilityWindowInfo> windows = getWindows();
//        Log.i("AA", String.format("Windows (%d):", windows.size()));
//        for (AccessibilityWindowInfo window : windows) {
//            Log.i("AA", String.format("window: %s", window.toString()));
//        }
//
//        /* Dump the view hierarchy */
//        dumpNode(getRootInActiveWindow(), 0);
//
//
//        Log.i("AccessibilityService","-------------------------------");
//    }
//
//    private void dumpNode(AccessibilityNodeInfo node, int indent) {
//        if (node == null) {
//            Log.e("AA", "node is null (stopping iteration)");
//            return;
//        }
//
//        String indentStr = new String(new char[indent * 2]).replace('\0', ' ');
//        Log.w("AA", String.format("%s NODE: %s", indentStr, node.toString()));
//        for (int i = 0; i < node.getChildCount(); i++) {
//            dumpNode(node.getChild(i), indent + 1);
//        }
//        /* NOTE: Not sure if this is really required. Documentation is unclear. */
//        node.recycle();
//    }





    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String eventTypeStr = AccessibilityEvent.eventTypeToString(accessibilityEvent.getEventType());
        /** 화면뷰 리소스 구하기: 전화 앱 **/
        /*
        if(eventTypeStr.equals("TYPE_VIEW_CLICKED")) {
            Log.i("AccessibilityService","-------------------------------");
            Log.i("AccessibilityService",eventTypeStr+".........");
            Log.i("AccessibilityService",accessibilityEvent.getText()+".........");
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
            trackingViewResources1(accessibilityNodeInfo);
            Log.i("AccessibilityService","-------------------------------");
        }
        */

        /** 키 입력 분석 **/
        Log.i("AccessibilityService","-------------------------------");
        if(eventTypeStr.equals("TYPE_VIEW_TEXT_CHANGED"))    isLocked = true;
        if(eventTypeStr.equals("TYPE_VIEW_TEXT_SELECTION_CHANGED") && isLocked) {
            isLocked = false;
            return;
        }

        if(accessibilityEvent.getPackageName() != null) {
            String packageName = accessibilityEvent.getPackageName().toString();
            Log.w("AccessibilityService", "package name: "+ packageName);
//            if(packageName.contains("inputmethod")) {
//                Log.e("AA","---------------------------------------------------------------------------------------------------------");
//                inputKeyObj.clear();
//                inputKeyObj = new HashMap<>();
//                keyIdx=0;
//            }

            if(eventTypeStr.equals("TYPE_VIEW_TEXT_CHANGED") || eventTypeStr.equals("TYPE_VIEW_TEXT_SELECTION_CHANGED")  ) {

                Log.i("AccessibilityService", eventTypeStr + ".........");
                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
                trackingViewResources2(accessibilityNodeInfo);
            }

            if(eventTypeStr.equals("TYPE_VIEW_FOCUSED")) {
                //Log.i("AccessibilityService", eventTypeStr + ".........");
                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
                trackingViewResources3(accessibilityNodeInfo);
            }
        }
        Log.i("AccessibilityService","-------------------------------");

    }


    private AccessibilityNodeInfo  trackingViewResources1(AccessibilityNodeInfo parentView) {
        if(parentView == null)  return null;
        if(parentView.getViewIdResourceName() != null)
            Log.w("AccessibilityService", "className: "+parentView.getClassName()+", resourceName: "+parentView.getViewIdResourceName()+", text: "+parentView.getText());

        for (int i=0; i< parentView.getChildCount(); i++) {
            AccessibilityNodeInfo child = parentView.getChild(i);
            if(child != null)   trackingViewResources1(child);
            else                return null;
        }

        return null;
    }

    private AccessibilityNodeInfo  trackingViewResources2(AccessibilityNodeInfo parentView) {
        if(parentView == null)  return null;

        if(parentView.getText() != null && parentView.getText().length() >= 0 && (String.valueOf(parentView.getClassName()).contains("EditText"))) {
            //Log.e("AA", "text: "+parentView.getText() + parentView.getInputType());
            getCurrentInputChar(parentView.getText(), parentView.getPackageName());
        }

        for (int i=0; i< parentView.getChildCount(); i++) {
            AccessibilityNodeInfo child = parentView.getChild(i);
            if(child != null)   trackingViewResources2(child);
            else                return null;
        }

        return null;
    }


    private AccessibilityNodeInfo  trackingViewResources3(AccessibilityNodeInfo parentView) {
        if(parentView == null)  return null;

        if(parentView.getText() != null && parentView.getText().length() >= 0 && (String.valueOf(parentView.getClassName()).contains("EditText"))) {
            Log.e("AA", "focused text: "+parentView.getText());
            totalStr = parentView.getText().toString();
        }

        for (int i=0; i< parentView.getChildCount(); i++) {
            AccessibilityNodeInfo child = parentView.getChild(i);
            if(child != null)   trackingViewResources2(child);
            else                return null;
        }

        return null;
    }


    private void getCurrentInputChar(CharSequence str, CharSequence packageName) {

        if(str == null && totalStr.length() >0) {
            Log.w("AccessibilityService", "입력 문자: backspace, 문자 타입: 특수문자");
            inputKeyObj.put(keyIdx++, new KeyObject(getAppNameByPackageName(getApplicationContext(), packageName.toString()), "backspace", isChunjin));
            totalStr="";
            isLocked = false;
            return;
        }
        String currentStr = str.toString();

        if(!currentStr.contains(totalStr) && !totalStr.contains(currentStr))    totalStr ="";


        Log.w("AccessibilityService", "현재 문자열: "+currentStr);
        Log.w("AccessibilityService", "Total 문자열: "+totalStr);
        String decomposeCurrentStr = AnalyzeHangeul.hangulToJaso(currentStr);
        Log.w("AccessibilityService", "Current 문자 분해: "+decomposeCurrentStr);

        String decomposeTotalStr = AnalyzeHangeul.hangulToJaso(totalStr);
        Log.w("AccessibilityService", "Total 문자 분해: "+decomposeTotalStr);

        /* 사용자가 새로운 키를 입력한 경우 */
        if(decomposeCurrentStr.length() >= decomposeTotalStr.length() && decomposeCurrentStr.length()!=0) {

            String ch = decomposeCurrentStr.charAt(decomposeCurrentStr.length()-1)+"";
            Log.w("AccessibilityService", "입력 문자: "+ch+",  문자 타입: "+getInputCharType(ch));

            String currentKeyType = getInputCharType(ch);
            if(!currentKeyType.equals(keyType) && keyType!=null && !ch.equals("ㆍ")) {
                keyType = currentKeyType;
                if(isChunjin) {
                    /* 천지인 좌표로 변환 */
                    for (Map.Entry<Integer, KeyObject> entry : inputKeyObj.entrySet()) {
                        entry.getValue().setupChunjin(true);
                        Log.d("AA", "글자: "+entry.getValue().getInputChar()+ ", posX: "+entry.getValue().getPosX() + ", posY: "+  entry.getValue().getPosY() );
                    }
                }

                /* 터치 이동 거리 계산*/
                int idx=0;
                float prevPosX=0f, prevPosY=0f;
                for (Map.Entry<Integer, KeyObject> entry : inputKeyObj.entrySet()) {
                    idx = entry.getKey();

                    if(idx == 0)    entry.getValue().setKeyDistance("0");
                    else            entry.getValue().setKeyDistance(calculateDistance(entry.getValue().getPosX(), prevPosX, entry.getValue().getPosY(), prevPosY));

                    prevPosX = entry.getValue().getPosX();
                    prevPosY = entry.getValue().getPosY();
                    idx++;

                    Log.d("AA", "글자: "+entry.getValue().getInputChar()+ ", posX: "+entry.getValue().getPosX() + ", posY: "+  entry.getValue().getPosY() + ", distance: "+  entry.getValue().getKeyDistance() );
                }

                Log.e("AA", "서버에 Map 저장.............................................");
                /**서버에 저장**/


                /* Map 초기화 */
                inputKeyObj.clear();
                keyIdx=0;
            }

            if(currentKeyType.equals("E")) {
                isChunjin = false;
                inputKeyObj.put(keyIdx++, new KeyObject(getAppNameByPackageName(getApplicationContext(), packageName.toString()), ch, isChunjin));
            }
            else if(currentKeyType.equals("H")) {
                inputKeyObj.put(keyIdx++, new KeyObject(getAppNameByPackageName(getApplicationContext(), packageName.toString()), ch, isChunjin));
            }
            else if(currentKeyType.equals("S")) {
                if(ch.equals("ㆍ")) {
                    keyType = "H";
                    isChunjin = true;
                }
            }

        }
        /* 사용자가 글자를 지운 경우: backspace 입력 */
        else if((decomposeCurrentStr.length() < decomposeTotalStr.length()) || (currentStr.length() < totalStr.length())) {
            Log.w("AccessibilityService", "입력 문자: backspace, 문자 타입: 특수문자");
            inputKeyObj.put(keyIdx++, new KeyObject(getAppNameByPackageName(getApplicationContext(), packageName.toString()), "backspace", isChunjin));
        }

        printHashMap(inputKeyObj);

        totalStr = currentStr;
    }

    private String calculateDistance(float posX, float prevPosX, float posY, float prevPosY) {
        double ac = Math.abs(posY - prevPosY);
        double cb = Math.abs(posX - prevPosX);
        return String.format("%.3f", Math.hypot(ac, cb));
    }

    private String getInputCharType(String str) {
        if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))    return "H";
        else if(str.matches("^[a-zA-Z]*$"))         return "E";
        else if(str.matches("^[0-9]*$"))            return "N";
        else                                               return "S";
    }

    private void printHashMap(HashMap<Integer, KeyObject> map) {
        Log.e("AA", "map size: "+ map.size());
        for (Map.Entry<Integer, KeyObject> entry : map.entrySet()) {
            Log.e("AA",entry.getKey()+" : "+entry.getValue().getAppName()+", "+entry.getValue().getInputChar()+", "+
                    entry.getValue().getPosX()+",  "+ entry.getValue().getPosY() );
        }
    }


    public static String getAppNameByPackageName(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        try {
            String tmpName = String.valueOf(pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)));
            return tmpName;
        } catch (PackageManager.NameNotFoundException e) {return null;}
    }


    /***********************************************************************************************/


//    private void trackingDetailedEvent(AccessibilityNodeInfo info) {
//        if(info == null) return;
//
//        if(info.getText() != null && info.getText().length() > 0 && (String.valueOf(info.getClassName()).contains("EditText")))
//            Log.w("AccessibilityService", info.getText() + " class: "+info.getClassName());
//
//        for(int i=0;i<info.getChildCount();i++){
//            AccessibilityNodeInfo child = info.getChild(i);
//            List<AccessibilityNodeInfo.AccessibilityAction> accessibilityActions = child.getActionList();
//            if(accessibilityActions !=null) {
//                Log.e("AccessibilityService", "***********");
//                for(int j=0;j<accessibilityActions.size();j++) {
//                    Log.e("AccessibilityService",  getActionStr(accessibilityActions.get(j).getId())+"");
//                }
//                Log.e("AccessibilityService", "***********");
//            }
//            trackingDetailedEvent(child);
//            if(child != null)   child.recycle();
//        }
//
//    }

}
