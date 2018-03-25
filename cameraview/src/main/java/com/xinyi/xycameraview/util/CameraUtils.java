package com.xinyi.xycameraview.util;

import android.hardware.Camera.Size;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraUtils {
    private static final String tag = "DEBUG";
    private CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static CameraUtils myCamPara = null;
    private CameraUtils(){

    }
    public static CameraUtils getInstance(){
        if(myCamPara == null){
            myCamPara = new CameraUtils();
            return myCamPara;
        }
        else{
            return myCamPara;
        }
    }


    public  Size getPreviewSize(List<Size> list, int th){
        return getPreviewSize(list,th,0.75f);
    }

    /**
     * 挑选相等比例的
     * 挑选比例小于0.05的
     * 比例最接近的
     * 其他的
     * 策略就是先挑选相等比例的最大值，最大值的宽如果没有大于minWidth,则到小于0.05的分辨率当中去找，
     * 如果还是没有大于minWidth,那就直接区比例最接近的
     * @param list
     * @param minWidth 最小值宽度（为了保证画面清晰度）
     * @param rate 预览画面比例（为了保证画面不变形）
     * @return
     */
    public  Size getPreviewSize(List<Size> list, int minWidth,float rate){
        //升序
        Collections.sort(list, sizeComparator);
        List<Size> equalSizes = new ArrayList<>();
        List<Size> nearEqualSizes = new ArrayList<>();
        Size nearSize = null;

        float temp = Integer.MAX_VALUE;

        int i = 0;
        for(Size s:list){
            float sRate = s.height*1.0f/s.width;
            if(sRate == rate){
                equalSizes.add(s);
            }else if(Math.abs(sRate-rate) < 0.05 ){
                nearEqualSizes.add(s);
            }
            //挑选最接近的
            if(sRate <= temp){
                nearSize = s;
                temp = sRate;
            }
        }
        if(equalSizes.size() > 0){
            Size maxEqualSize = equalSizes.get(equalSizes.size() - 1);
            if(maxEqualSize.width >= minWidth){
                return maxEqualSize;
            }else{
                if(nearEqualSizes.size() > 0){
                    maxEqualSize = nearEqualSizes.get(nearEqualSizes.size() - 1);
                    if(maxEqualSize.width >= minWidth){
                        return maxEqualSize;
                    }
                }
            }
        }else if(nearEqualSizes.size() > 0 ){
            Size maxEqualSize = nearEqualSizes.get(nearEqualSizes.size() - 1);
            if(maxEqualSize.width >= minWidth){
                return maxEqualSize;
            }
        }
        return nearSize;
    }


    public Size getPictureSize(List<Size> list, int maxWidth,float rate){
        Collections.sort(list, sizeComparator);
        int i = 0;
        for(Size s:list){
            float sRate = s.height*1.0f/s.width;
            if((s.width >= maxWidth)  && Math.abs(sRate-rate) < 0.05){
                Log.i(tag, "最终设置图片尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }
        return list.get(i%list.size());
    }

    public  class CameraSizeComparator implements Comparator<Size>{
        //按升序排列
        public int compare(Size lhs, Size rhs) {
            // TODO Auto-generated method stub
            if(lhs.width == rhs.width){
                return 0;
            }
            else if(lhs.width > rhs.width){
                return 1;
            }
            else{
                return -1;
            }
        }

    }
}