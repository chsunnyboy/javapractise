package com.dtl645;

import java.text.MessageFormat;
import java.util.function.BiFunction;

public class Test2 {
    public static void main(String[] args) {
        /*Test2 t = new Test2();
        System.out.printf(t.insertDataRecord(t::formater));
        System.out.printf(t.insertDataRecord(MessageFormat::format));*/
        int times=0;
        while (times++ < 5) {
            System.out.println(times);
        }
    }
    public String insertDataRecord(BiFunction<String, Object[], String> formatter){
        String SQL_INSERT_RECORD = "INSERT INTO {0} ({1}) VALUES ({2});";
        return formatter.apply(SQL_INSERT_RECORD,new String[]{"temptable", "id", "1"});
    }
    public String formater(String sql, Object[] args){
        return MessageFormat.format(sql,args);
    }
}
