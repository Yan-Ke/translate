package com.sian.translate.utlis;


import com.sian.translate.VO.ResultVO;

/**
 * Created by 廖师兄
 * 2017-12-10 18:03
 */
public class ResultVOUtil {


    public static ResultVO success() {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success(Object object) {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVO error(String msg) {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(1);
        resultVO.setMsg(msg);
        return resultVO;

    }

    public static ResultVO success(String logmsg) {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setLogmsg(logmsg);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVO success(Object object,String logmsg) {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setLogmsg(logmsg);
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVO uploadSuccess(String imagePath) {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setData(imagePath);
        resultVO.setMsg("成功");
        return resultVO;
    }
}
