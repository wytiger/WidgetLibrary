package com.wytiger.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.wytiger.banner.transformer.AccordionTransformer;
import com.wytiger.banner.transformer.BackgroundToForegroundTransformer;
import com.wytiger.banner.transformer.CubeInTransformer;
import com.wytiger.banner.transformer.CubeOutTransformer;
import com.wytiger.banner.transformer.DefaultTransformer;
import com.wytiger.banner.transformer.DepthPageTransformer;
import com.wytiger.banner.transformer.FlipHorizontalTransformer;
import com.wytiger.banner.transformer.FlipVerticalTransformer;
import com.wytiger.banner.transformer.ForegroundToBackgroundTransformer;
import com.wytiger.banner.transformer.RotateDownTransformer;
import com.wytiger.banner.transformer.RotateUpTransformer;
import com.wytiger.banner.transformer.ScaleInOutTransformer;
import com.wytiger.banner.transformer.StackTransformer;
import com.wytiger.banner.transformer.TabletTransformer;
import com.wytiger.banner.transformer.ZoomInTransformer;
import com.wytiger.banner.transformer.ZoomOutSlideTransformer;
import com.wytiger.banner.transformer.ZoomOutTranformer;

public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
