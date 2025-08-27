package com.danebrown.otherlib;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.BorderStyle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class PoiTLMain {
    public static void main(String[] args) throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("template.docx");
        XWPFTemplate template = XWPFTemplate.compile(input).render(
                new HashMap<String, Object>(){{
                    put("title", "Hi, poi-tl Word模板引擎");
                    //颜色
                    put("name", Texts.of("Sayi").color("000000").create());
                    //链接
                    put("link", Texts.of("website").link("http://deepoove.com").create());
                    //锚点
                    put("anchor", Texts.of("anchortxt").anchor("appendix1").create());
                    //表格
                    put("table0", Tables.of(new String[][] {
                            new String[] { "00", "01" },
                            new String[] { "10", "11" }
                    }).border(BorderStyle.DEFAULT).create());
                    //多行表格
                    RowRenderData row0 = Rows.of("姓名", "学历").textColor("FFFFFF")
                            .bgColor("4472C4").center().create();
                    RowRenderData row1 = Rows.create("李四", "博士");
                    put("table1", Tables.create(row0, row1));
                    //合并表格
                    RowRenderData row3 = Rows.of("列0", "列1", "列2").center().bgColor("4472C4").create();
                    RowRenderData row4 = Rows.create("没有数据", null, null);
                    MergeCellRule rule = MergeCellRule.builder().map(MergeCellRule.Grid.of(1, 0), MergeCellRule.Grid.of(1, 2)).build();
                    put("table3", Tables.of(row3, row4).mergeRule(rule).create());
                    //列表
                    put("list", Numberings.create("Plug-in grammar",
                            "Supports word text, pictures, table...",
                            "Not just templates"));
                    //ifelse
                    put("announce",true);
                }});
        template.writeAndClose(new FileOutputStream("output.docx"));
    }
}
