package com.mdmd.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;
import static java.lang.System.out;

public class WeiXinMessageUtil {

    public static final String RESP_MESSAGE_TYPE_TEXT = "text";
    public static final String RESP_MESSAGE_TYPE_EVENT = "event";
        public static final String RESP_EVENT_SCAN = "scan";
        public static final String RESP_EVENT_SUBSCRIBE = "subscribe";


    public static Map<String,String> pareXml(HttpServletRequest request) throws Exception{
        //记录解析结果
        Map<String, String> reqMap = new HashMap<>();
        InputStream inputStream = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        for (Element e:elements) {
            reqMap.put(e.getName(),e.getText());
        }
        inputStream.close();
        inputStream = null;
        return reqMap;
    }

    /**
     * 拓展xstream 使得支持CDATA块
     * 支持<（书名）标签
     */
    public static XStream xmStream =new XStream(new XppDriver(){
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out){
                //对所有的xml节点的转换都增加CDATA标记
                boolean cdata = true;
                @SuppressWarnings("unchecked")
                public void startNode(String name,Class clazz){
                    super.startNode(name, clazz);
                }
                protected void writeText(QuickWriter writer, String text){
                    if(cdata){
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }else {
                        writer.write(text);
                    }
                }
            };
        }
    });
}