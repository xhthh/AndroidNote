//package com.xht.androidnote.module.kotlin.screenshot;
//
//
//import com.aliyun.oss.common.utils.BinaryUtil;
//import com.aliyun.oss.internal.OSSUtils;
//import com.aliyun.oss.model.Callback;
//
//import org.apache.commons.codec.binary.Base64;
//
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import javax.activation.MimetypesFileTypeMap;
//
//public class PostObjectSample {
//    // 填写待上传的本地文件的完整路径。
//    private String localFilePath = "yourLocalFile";
//    // Endpoint以杭州为例，其它Region请按实际情况填写。
//    private String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
//    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//    private String accessKeyId = "yourAccessKeyId";
//    private String accessKeySecret = "yourAccessKeySecret";
//    // 填写Bucket名称。
//    private String bucketName = "yourBucketName";
//    // 填写不包含Bucket名称在内的Object的完整路径。
//    private String objectName = "yourObjectName";
//    // 设置回调服务器地址，例如http://oss-demo.oss-cn-hangzhou.aliyuncs.com:23450或http://127.0.0.1:9090。
//    private String callbackServerUrl = "yourCallbackServerUrl";
//    // 设置回调请求消息头中Host的值，例如oss-cn-hangzhou.aliyuncs.com。
//    private String callbackServerHost = "yourCallbackServerHost";
//    // 设置MD5值。MD5值由整个Body计算得出。
//    private static String contentMD5 = "yourContentMD5";
//
//    /**
//     * 表单上传。
//     *
//     * @throws Exception
//     */
//    private void PostObject() throws Exception {
//        // 在URL中添加Bucket名称，添加后URL格式为http://yourBucketName.oss-cn-hangzhou.aliyuncs.com。
//        String urlStr = endpoint.replace("http://", "http://" + bucketName + ".");
//        // 设置表单Map。
//        Map<String, String> formFields = new LinkedHashMap<String, String>();
//        // 设置文件名称。
//        formFields.put("key", this.objectName);
//        // 设置Content-Disposition。
//        formFields.put("Content-Disposition", "attachment;filename="
//                + localFilePath);
//        // 设置回调参数。关于回调的详细介绍，请参见Callback。
//        // Callback callback = new Callback();
//        // 设置回调服务器地址，例如http://oss-demo.oss-cn-hangzhou.aliyuncs.com:23450或http://127.0.0.1:9090。
//        // callback.setCallbackUrl(callbackServerUrl);
//        // 设置回调请求消息头中Host的值，如oss-cn-hangzhou.aliyuncs.com。
//        // callback.setCallbackHost(callbackServerHost);
//        // 设置发起回调时请求Body的值。
//        // callback.setCallbackBody("{\\\"mimeType\\\":${mimeType},\\\"size\\\":${size}}");
//        // 设置发起回调请求的Content-Type。
//        // callback.setCalbackBodyType(Callback.CalbackBodyType.JSON);
//        // 设置发起回调请求的自定义参数，由Key和Value组成，Key必须以x:开始，且必须小写。
//        // callback.addCallbackVar("x:var1", "value1");
//        // callback.addCallbackVar("x:var2", "value2");
//        // 在表单Map中设置回调参数。
//        // setCallBack(formFields, callback);
//        // 设置OSSAccessKeyId。
//        formFields.put("OSSAccessKeyId", accessKeyId);
//        String policy = "{\"expiration\": \"2120-01-01T12:00:00.000Z\",\"conditions\": [[\"content-length-range\", 0, 104857600]]}";
//        String encodePolicy = new String(Base64.encodeBase64(policy.getBytes()));
//        // 设置policy。
//        formFields.put("policy", encodePolicy);
//        // 生成签名。
//        String signaturecom = com.aliyun.oss.common.auth.ServiceSignature.create().computeSignature(accessKeySecret, encodePolicy);
//        // 设置签名。
//        formFields.put("Signature", signaturecom);
//        String ret = formUpload(urlStr, formFields, localFilePath);
//        System.out.println("Post Object [" + this.objectName + "] to bucket [" + bucketName + "]");
//        System.out.println("post reponse:" + ret);
//    }
//
//    private static String formUpload(String urlStr, Map<String, String> formFields, String localFile)
//            throws Exception {
//        String res = "";
//        HttpURLConnection conn = null;
//        String boundary = "9431149156168";
//        try {
//            URL url = new URL(urlStr);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(30000);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("User-Agent",
//                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
//            // 设置MD5值。MD5值由整个Body计算得出。如果希望开启MD5校验，可参考MD5加密设置。
//            // conn.setRequestProperty("Content-MD5", contentMD5);
//            conn.setRequestProperty("Content-Type",
//                    "multipart/form-data; boundary=" + boundary);
//            OutputStream out = new DataOutputStream(conn.getOutputStream());
//            // 遍历读取表单Map中的数据，将数据写入到输出流中。
//            if (formFields != null) {
//                StringBuffer strBuf = new StringBuffer();
//                Iterator<Map.Entry<String, String>> iter = formFields.entrySet().iterator();
//                int i = 0;
//                while (iter.hasNext()) {
//                    Map.Entry<String, String> entry = iter.next();
//                    String inputName = entry.getKey();
//                    String inputValue = entry.getValue();
//                    if (inputValue == null) {
//                        continue;
//                    }
//                    if (i == 0) {
//                        strBuf.append("--").append(boundary).append("\r\n");
//                        strBuf.append("Content-Disposition: form-data; name=\""
//                                + inputName + "\"\r\n\r\n");
//                        strBuf.append(inputValue);
//                    } else {
//                        strBuf.append("\r\n").append("--").append(boundary).append("\r\n");
//                        strBuf.append("Content-Disposition: form-data; name=\""
//                                + inputName + "\"\r\n\r\n");
//                        strBuf.append(inputValue);
//                    }
//                    i++;
//                }
//                out.write(strBuf.toString().getBytes());
//            }
//            // 读取文件信息，将要上传的文件写入到输出流中。
//            File file = new File(localFile);
//            String filename = file.getName();
//            String contentType = new MimetypesFileTypeMap().getContentType(file);
//            if (contentType == null || contentType.equals("")) {
//                contentType = "application/octet-stream";
//            }
//            StringBuffer strBuf = new StringBuffer();
//            strBuf.append("\r\n").append("--").append(boundary)
//                    .append("\r\n");
//            strBuf.append("Content-Disposition: form-data; name=\"file\"; "
//                    + "filename=\"" + filename + "\"\r\n");
//            strBuf.append("Content-Type: " + contentType + "\r\n\r\n");
//            out.write(strBuf.toString().getBytes());
//            DataInputStream in = new DataInputStream(new FileInputStream(file));
//            int bytes = 0;
//            byte[] bufferOut = new byte[1024];
//            while ((bytes = in.read(bufferOut)) != -1) {
//                out.write(bufferOut, 0, bytes);
//            }
//            in.close();
//            byte[] endData = ("\r\n--" + boundary + "--\r\n").getBytes();
//            out.write(endData);
//            out.flush();
//            out.close();
//            // 读取返回数据。
//            strBuf = new StringBuffer();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                strBuf.append(line).append("\n");
//            }
//            res = strBuf.toString();
//            reader.close();
//            reader = null;
//        } catch (ClientException e) {
////            System.err.println("Send post request exception: " + e);
//            System.err.println(e.getErrorCode() + " msg=" + e.getMessage());
//            throw e;
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//                conn = null;
//            }
//        }
//        return res;
//    }
//
//    private static void setCallBack(Map<String, String> formFields, Callback callback) {
//        if (callback != null) {
//            String jsonCb = OSSUtils.jsonizeCallback(callback);
//            String base64Cb = BinaryUtil.toBase64String(jsonCb.getBytes());
//            formFields.put("callback", base64Cb);
//            if (callback.hasCallbackVar()) {
//                Map<String, String> varMap = callback.getCallbackVar();
//                for (Map.Entry<String, String> entry : varMap.entrySet()) {
//                    formFields.put(entry.getKey(), entry.getValue());
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        PostObjectSample ossPostObject = new PostObjectSample();
//        ossPostObject.PostObject();
//    }
//}
