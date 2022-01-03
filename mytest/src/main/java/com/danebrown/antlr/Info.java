package com.danebrown.antlr;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.UnbufferedCharStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.io.StringBufferInputStream;

/**
 * Created by danebrown on 2021/5/13
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Slf4j
public class Info {
    public String parse(String sql) throws IOException {
//        StringBufferInputStream stringBufferInputStream =
//                new StringBufferInputStream(sql);
//        UnbufferedCharStream antlrInputStream =
//                new UnbufferedCharStream(stringBufferInputStream);
//
//        GrammerStatmentLexer lexer = new GrammerStatmentLexer(antlrInputStream);
//
//        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
//
//        log.info("tokens:{}",tokenStream.getTokens());
//
//        GrammerStatmentParser parser =
//                new GrammerStatmentParser(tokenStream);
//
//
//
//        GrammerStatmentParser.SqlContext sqlContext = parser.sql();
//        return sqlContext.getText();
        return "";

    }

    public static void main(String[] args) throws IOException {
        Info info = new Info();
        String parseString = info.parse("SELECT a FROM base WHERE");
        System.out.println(parseString);
    }
}
