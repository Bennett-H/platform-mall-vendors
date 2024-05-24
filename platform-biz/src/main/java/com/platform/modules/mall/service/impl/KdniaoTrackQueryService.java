
package com.platform.modules.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;

import com.platform.common.utils.Constant;
import com.platform.modules.sys.service.SysConfigService;
import org.json.JSONArray;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import java.io.*;

import java.net.HttpURLConnection;

import java.net.URL;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.HashMap;

import java.util.Map;

/**
 * 快递鸟接口对接
 *
 * @author m
 */
@Component
public class KdniaoTrackQueryService {

    @Autowired
    private SysConfigService sysConfigService;

    private static final char[] base64EncodeChars = new char[]{

            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',

            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',

            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',

            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',

            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',

            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',

            'w', 'x', 'y', 'z', '0', '1', '2', '3',

            '4', '5', '6', '7', '8', '9', '+', '/'};
    /**
     * 电商ID
     */

    //private final String EBusinessID = "请到快递鸟官网申请http://www.kdniao.com/ServiceApply.aspx";
    /**
     * 电商加密私钥，快递鸟提供，注意保管，不要泄漏
     */

    //private final String AppKey = "请到快递鸟官网申请http://www.kdniao.com/ServiceApply.aspx";

    /**
     * 快递鸟订阅推送测试请求url

     */

    //private String dingyueURL = "http://testapi.kdniao.com:8081/api/dist";
    /**
     * 快递鸟物流轨迹即时查询接口请求url
     */

    private final String ReqURL = "http://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";

    //DEMO

//    public static void main(String[] args) {
//
//        KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();
//
//        try {
//
//            //即时查询
//
//            //String result = api.getOrderTracesByJson("YTO", "4236260290247");
//
//            //订阅
//
//            String result = api.orderTracesSubByJson("YTO", "4236260290247");
//
//            System.out.print(result);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//
//        }
//
//    }
    /**
     * 快递鸟订阅推送正式请求url
     *
     * @param args
     */

    private final String dingyueURL = "http://api.kdniao.com/api/dist";

    public static String base64Encode(byte[] data) {

        StringBuffer sb = new StringBuffer();

        int len = data.length;

        int i = 0;

        int b1, b2, b3;

        while (i < len) {

            b1 = data[i++] & 0xff;

            if (i == len) {

                sb.append(base64EncodeChars[b1 >>> 2]);

                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);

                sb.append("==");

                break;

            }

            b2 = data[i++] & 0xff;

            if (i == len) {

                sb.append(base64EncodeChars[b1 >>> 2]);

                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);

                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);

                sb.append("=");

                break;

            }

            b3 = data[i++] & 0xff;

            sb.append(base64EncodeChars[b1 >>> 2]);

            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);

            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);

            sb.append(base64EncodeChars[b3 & 0x3f]);

        }

        return sb.toString();

    }

    /**
     * Json方式  物流信息订阅
     *
     * @throws Exception
     */

    public String orderTracesSubByJson(String expCode, String expNo) throws Exception {

        String requestData = "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";

        Map<String, String> params = new HashMap<>(16);

        params.put("RequestData", urlEncoder(requestData, "UTF-8"));

        params.put("EBusinessID", sysConfigService.getValue(Constant.KDN_BUSINESS_ID));

        params.put("RequestType", "1008");

        String dataSign = encrypt(requestData, sysConfigService.getValue(Constant.KDN_APP_KEY), "UTF-8");

        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));

        params.put("DataType", "2");

        String result = sendPost(dingyueURL, params);

        Object parse = JSON.parse(result);

        //根据公司业务处理返回的信息......

        return result;

    }

    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */

    public JSONObject getOrderTracesByJson(String expCode, String expNo,String mobile){
        JSONObject requestJson = new JSONObject();
        requestJson.put("OrderCode","");
        requestJson.put("ShipperCode",expCode);
        requestJson.put("LogisticCode",expNo);
        if(StrUtil.isNotBlank(mobile))
            requestJson.put("CustomerName",mobile.substring(mobile.length()-4));

        String requestData = requestJson.toJSONString();

        Map<String, String> params = new HashMap<>(16);

        params.put("RequestData", urlEncoder(requestData, "UTF-8"));

        params.put("EBusinessID",  sysConfigService.getValue(Constant.KDN_BUSINESS_ID));

        params.put("RequestType", "8001");

        String dataSign = encrypt(requestData,  sysConfigService.getValue(Constant.KDN_APP_KEY), "UTF-8");

        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));

        params.put("DataType", "2");

        String result = sendPost(ReqURL, params);

        return JSONObject.parseObject(result);

    }

    /**
     * MD5加密
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws Exception
     */

    @SuppressWarnings("unused")

    private String MD5(String str, String charset) {
        MessageDigest md = null;
        StringBuffer sb = new StringBuffer(32);
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(charset));

            byte[] result = md.digest();
            for (int i = 0; i < result.length; i++) {

                int val = result[i] & 0xff;

                if (val <= 0xf) {

                    sb.append("0");

                }

                sb.append(Integer.toHexString(val));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        return sb.toString().toLowerCase();

    }

    /**
     * base64编码
     *
     * @param str     内容
     * @param charset 编码方式
     * @throws UnsupportedEncodingException
     */

    private String base64(String str, String charset) {
        String encoded = null;
        try {
            encoded = base64Encode(str.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return encoded;
    }

    @SuppressWarnings("unused")

    private String urlEncoder(String str, String charset) {

        String result = null;
        try {
            result = URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return result;

    }

    /**
     * 电商Sign签名生成
     *
     * @param content  内容
     * @param keyValue Appkey
     * @param charset  编码方式
     * @return DataSign签名
     * @throws UnsupportedEncodingException ,Exception
     */

    @SuppressWarnings("unused")
    private String encrypt(String content, String keyValue, String charset) {

        if (keyValue != null) {

            return base64(MD5(content + keyValue, charset), charset);

        }

        return base64(MD5(content, charset), charset);

    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url    发送请求的 URL
     * @param params 请求的参数集合
     * @return 远程资源的响应结果
     */

    @SuppressWarnings("unused")

    private String sendPost(String url, Map<String, String> params) {

        OutputStreamWriter out = null;

        BufferedReader in = null;

        StringBuilder result = new StringBuilder();

        try {

            URL realUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();

            // 发送POST请求必须设置如下两行

            conn.setDoOutput(true);

            conn.setDoInput(true);

            // POST方法

            conn.setRequestMethod("POST");

            // 设置通用的请求属性

            conn.setRequestProperty("accept", "*/*");

            conn.setRequestProperty("connection", "Keep-Alive");

            conn.setRequestProperty("user-agent",

                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();

            // 获取URLConnection对象对应的输出流

            out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);

            // 发送请求参数

            if (params != null) {

                StringBuilder param = new StringBuilder();

                for (Map.Entry<String, String> entry : params.entrySet()) {

                    if (param.length() > 0) {

                        param.append("&");

                    }

                    param.append(entry.getKey());

                    param.append("=");

                    param.append(entry.getValue());

                    //System.out.println(entry.getKey()+":"+entry.getValue());

                }

                //System.out.println("param:"+param.toString());

                out.write(param.toString());

            }

            // flush输出流的缓冲

            out.flush();

            // 定义BufferedReader输入流来读取URL的响应

            in = new BufferedReader(

                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            String line;

            while ((line = in.readLine()) != null) {

                result.append(line);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        //使用finally块来关闭输出流、输入流

        finally {

            try {

                if (out != null) {

                    out.close();

                }

                if (in != null) {

                    in.close();

                }

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        return result.toString();

    }

    public JSONObject getJSONParam(HttpServletRequest request) {

        JSONObject jsonParam = null;

        try {

            // 获取输入流

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));

            // 写入数据到StringBuilder

            StringBuilder sb = new StringBuilder();

            String line;

            while ((line = streamReader.readLine()) != null) {

                sb.append(line);

            }

            jsonParam = JSONObject.parseObject(sb.toString());

            // 直接将json信息打印出来

            if (null != jsonParam) {

                System.out.println(jsonParam.toJSONString());

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return jsonParam;

    }

    /**
     * @param returnJson 解析json数据
     * @throws JSONException
     */

    public void parseJson(String returnJson) throws JSONException {

        org.json.JSONObject obj = new org.json.JSONObject(returnJson);

        // 存放着订阅的多个订单信息

        JSONArray array = obj.getJSONArray("Data");

        for (int i = 0; i < array.length(); i++) {

            org.json.JSONObject json = array.getJSONObject(i);

            // 订单号

            String LogisticCode = json.getString("LogisticCode");

            // 快递公司编号

            String ShipperCode = json.getString("ShipperCode");

            //快递轨迹

            JSONArray traces = json.getJSONArray("Traces");

            //将接收到的信息按照公司的业务进行处理.....

            System.out.println("LogisticCode" + LogisticCode);

            System.out.println("ShipperCode" + ShipperCode);

            System.out.println("traces" + traces);

        }

    }

    /**
     * @param response 返回信息给快递鸟公司
     */

    public void returnMessageToKuaiDiNiao(HttpServletResponse response) {

        try {

            PrintWriter out = response.getWriter();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            org.json.JSONObject obj = new org.json.JSONObject();

            obj.put("EBusinessID",  sysConfigService.getValue(Constant.KDN_BUSINESS_ID));

            obj.put("UpdateTime", sdf.format(new Date()));

            obj.put("Success", true);

            obj.put("Reason", "");

            out.write(obj.toString());

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
