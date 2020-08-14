package com.danebrown.freemarker;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.Base64;
import java.util.List;

/**
 * Created by danebrown on 2020/8/5
 * mail: tain198127@163.com
 */
public class MacSign implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        StringBuffer stringBuffer = new StringBuffer();
        for (Object o: arguments
             ) {
            stringBuffer.append(o.toString());
        }

        return Base64.getEncoder().encodeToString(stringBuffer.toString().getBytes());
    }
}
