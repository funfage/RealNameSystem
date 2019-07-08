package com.real.name.util;

import com.real.name.others.BaseTest;
import com.real.name.common.utils.ImageTool;
import org.junit.Assert;
import org.junit.Test;

public class ImageToolTest extends BaseTest {



    @Test
    public void deleteImageTest(){
        boolean b = ImageTool.deleteImage(72 + "");
        Assert.assertEquals(b, true);

    }

}
