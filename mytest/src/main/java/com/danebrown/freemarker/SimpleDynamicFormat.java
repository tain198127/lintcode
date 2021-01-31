package com.danebrown.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.threadly.util.StringBufferWriter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by danebrown on 2020/12/4
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
public class SimpleDynamicFormat {
    static Configuration cfg = new Configuration();

    static {
        cfg.setDefaultEncoding("UTF-8");
    }

    private static String freeMkrToString(String pattern, Map<String,Object> content) throws IOException, TemplateException {

        Template template = new Template("test", pattern, cfg);
        StringBuffer stringBuffer = new StringBuffer();
        StringBufferWriter stringBufferWriter = new StringBufferWriter(stringBuffer);
        template.process(content,stringBufferWriter);
        return stringBuffer.toString();
    }

    public static void main(String[] args) throws IOException, TemplateException {

        Template template = new Template("test", "${size?left_pad(8)}", cfg);
        StringBuffer stringBuffer = new StringBuffer();
        StringBufferWriter stringBufferWriter = new StringBufferWriter(stringBuffer);
        Map<String, String> dataModel = new HashMap<String, String>();
        dataModel.put("size", "16");
        template.process(dataModel, stringBufferWriter);
        System.out.println(stringBuffer.toString());


        String tpl = "${name?left_pad(8,\"0\")}";
        Map<String, Object> nameMod = new HashMap<>();
        nameMod.put("name", "16");
        String forString = freeMkrToString(tpl,nameMod);

        System.out.println(forString);


    }
}
