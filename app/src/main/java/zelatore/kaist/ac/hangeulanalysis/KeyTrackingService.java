package zelatore.kaist.ac.hangeulanalysis;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

public class KeyTrackingService extends AccessibilityService {
    @Override
    public void onInterrupt() {}




    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.i("AccessibilityService","-------------------------------");
        Log.i("AccessibilityService",AccessibilityEvent.eventTypeToString(accessibilityEvent.getEventType())+".........");

        if(accessibilityEvent.getPackageName() != null) {
            String packageName = accessibilityEvent.getPackageName().toString();
            Log.w("AA", "package name: "+ packageName);

            if(packageName != null) {
                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
                trackingDetailedEvent(accessibilityNodeInfo);
            }
        }
        Log.i("AccessibilityService","-------------------------------");
    }
    



    private void trackingDetailedEvent(AccessibilityNodeInfo info) {
        if(info == null)    return;
        String viewName = info.getViewIdResourceName();
        String className = info.getClassName().toString();
        if(info.getText() != null)
            Log.w("AccessibilityService", info.getText().toString());

        for(int i=0;i<info.getChildCount();i++){
            AccessibilityNodeInfo child = info.getChild(i);
            trackingDetailedEvent(child);
            if(child != null)   child.recycle();
        }

    }



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


    private String getInputCharType(String str) {
        if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))           return "한글";
        else if(str.matches("^[a-zA-Z]*$"))                 return "영문";
        else if(str.matches("^[0-9]*$"))                    return "숫자";
            //else if(str.matches("[ !@#$%^&*(),.?\":{}|<>]"))    return "특수문자";
        else                                                        return "특수문자";
    }

}
