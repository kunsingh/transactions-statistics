package com.n26.exercise.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SelfExpiringListTest {


    @Test
    public void test() throws Exception{
//        ExpiringList<Transaction> list = new SelfExpiringList<>();
//        for(int i=0;i<60;i++){
//            list.add(new Transaction(new BigDecimal(100 * (i+1)), new Date(System.currentTimeMillis())),System.currentTimeMillis());
//            Thread.sleep(1000);
//        }
//        Thread.sleep(1000);
//        for(Transaction t:list){
//            System.out.println(t.getAmount());
//        }
//        System.out.println("-------------");
//        Thread.sleep(1000);
//
//        list.stream().forEach(l->{
//            System.out.println(l.getAmount());
//        });


        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DDThh:mm:ss.sssZ");
        String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        final SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String date = sdf.format(new Date());
        System.out.println(date);
    }
}
