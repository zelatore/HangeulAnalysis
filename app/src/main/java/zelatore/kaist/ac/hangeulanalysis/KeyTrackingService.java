package zelatore.kaist.ac.hangeulanalysis;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class KeyTrackingService extends AccessibilityService {
    private String totalStr="";
    private boolean isLocked = false;

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
        //Log.i("AccessibilityService","-------------------------------");
        //if(eventTypeStr.equals("TYPE_WINDOWS_CHANGED") || eventTypeStr.equals("TYPE_WINDOW_CONTENT_CHANGED")) {
        if(eventTypeStr.equals("TYPE_VIEW_CLICKED")) {
            Log.i("AccessibilityService","-------------------------------");
            Log.i("AccessibilityService",eventTypeStr+".........");
            Log.i("AccessibilityService",accessibilityEvent.getText()+".........");
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
            trackingViewResources(accessibilityNodeInfo);



            Log.i("AccessibilityService","-------------------------------");
        }


        /*
        if(eventTypeStr.equals("TYPE_VIEW_TEXT_CHANGED"))    isLocked = true;
        if(eventTypeStr.equals("TYPE_VIEW_TEXT_SELECTION_CHANGED") && isLocked) {
            isLocked = false;
            return;
        }
        */

//        if(accessibilityEvent.getPackageName() != null) {
//            String packageName = accessibilityEvent.getPackageName().toString();
//            Log.w("AccessibilityService", "package name: "+ packageName);
//
//            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
//            trackingViewResources(accessibilityNodeInfo);
//
//
////            if(eventTypeStr.equals("TYPE_VIEW_TEXT_CHANGED")) {
////                Log.i("AccessibilityService", eventTypeStr + ".........");
////                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityEvent.getSource();
////                accessibilityNodeInfo = trackingViewResources(accessibilityNodeInfo);
////                if(accessibilityNodeInfo == null) {
////                    Log.w("AccessibilityService", "트래킹 끝");
////                }
////            }
////            if(eventTypeStr.equals("TYPE_VIEW_TEXT_SELECTION_CHANGED"))
////                Log.i("AccessibilityService",eventTypeStr+".........");
//
//        }
        //Log.i("AccessibilityService","-------------------------------");
    }




    private AccessibilityNodeInfo  trackingViewResources(AccessibilityNodeInfo parentView) {
        if(parentView == null)  return null;
        if(parentView.getViewIdResourceName() != null)
            Log.w("AccessibilityService", "className: "+parentView.getClassName()+", resourceName: "+parentView.getViewIdResourceName()+", text: "+parentView.getText());

        //if(String.valueOf(parentView.getClassName()).contains("EditText"))
        //    Log.w("AccessibilityService", "className: "+parentView.getClassName()+",     input type: "+parentView.getInputType());

        //if(parentView.getText() != null && parentView.getText().length() > 0 && (String.valueOf(parentView.getClassName()).contains("EditText")))
        //if((String.valueOf(parentView.getClassName()).contains("EditText")))
        //    getCurrentInputChar(parentView.getText());

        for (int i=0; i< parentView.getChildCount(); i++) {
            AccessibilityNodeInfo child = parentView.getChild(i);
            if(child != null)   trackingViewResources(child);
            else                return null;
        }

        //Log.w("AccessibilityService", "함수 끝");
        return null;
    }

    private void getCurrentInputChar(CharSequence str) {
        if(str == null && totalStr.length() >0) {
            Log.w("AccessibilityService", "입력 문자: backspace, 문자 타입: 특수문자");
            totalStr="";
            isLocked = false;
            return;
        }
        String currentStr = str.toString();

        Log.w("AccessibilityService", "현재 문자열: "+currentStr);
        Log.w("AccessibilityService", "Total 문자열: "+totalStr);
        String decomposeCurrentStr = Hangul.hangulToJaso(currentStr);
        Log.w("AccessibilityService", "Current 문자 분해: "+decomposeCurrentStr);

        String decomposeTotalStr = Hangul.hangulToJaso(totalStr);
        Log.w("AccessibilityService", "Total 문자 분해: "+decomposeTotalStr);

        /* 사용자가 새로운 키를 입력한 경우 */
        if(decomposeCurrentStr.length() >= decomposeTotalStr.length()) {
            String ch = decomposeCurrentStr.charAt(decomposeCurrentStr.length()-1)+"";
            Log.w("AccessibilityService", "입력 문자: "+ch+",  문자 타입: "+getInputCharType(ch));
        }
        /* 사용자가 글자를 지운 경우: backspace 입력 */
        else if((decomposeCurrentStr.length() < decomposeTotalStr.length()) || (currentStr.length() < totalStr.length())) {
            Log.w("AccessibilityService", "입력 문자: backspace, 문자 타입: 특수문자");
        }

        totalStr = currentStr;
        isLocked = false;
    }

    private String getInputCharType(String str) {
        if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))           return "한글";
        else if(str.matches("^[a-zA-Z]*$"))                 return "영문";
        else if(str.matches("^[0-9]*$"))                    return "숫자";
        else                                                        return "특수문자";
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





    public AccessibilityNodeInfo findTextViewNode(AccessibilityNodeInfo nodeInfo) {

        //I highly recommend leaving this line in! You never know when the screen content will
        //invalidate a node you're about to work on, or when a parents child will suddenly be gone!
        //Not doing this safety check is very dangerous!
        if (nodeInfo == null) return null;

        Log.w("AA", nodeInfo.toString());

        //Notice that we're searching for the TextView's simple name!
        //This allows us to find AppCompat versions of TextView as well
        //as 3rd party devs well names subclasses... though with perhaps
        //a few poorly named unintended stragglers!
        if (nodeInfo.getClassName().toString().contains(EditText.class.getSimpleName())) {
            return nodeInfo;
        }

        //Do other work!

        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo result = findTextViewNode(nodeInfo.getChild(i));

            if (result != null) return result;
        }

        return null;
    }

}
