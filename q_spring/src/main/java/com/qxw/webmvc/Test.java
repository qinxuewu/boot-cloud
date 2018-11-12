package com.qxw.webmvc;

public class Test {


    public static void main(String[] args) {

        new Test().aa();
    }

    public void aa() {
        System.out.println("测试TimerTask========》");
        String s1 = this.getClass().getResource("").getPath();
        String s2 = this.getClass().getResource("/").getPath();
        String s3 = this.getClass().getResource("/com").getPath();
//	       String s4=this.getClass().getResource("/com/qxw").getPath();

        String s5 = this.getClass().getClassLoader().getResource("com/qxw").getPath();
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
//	       System.out.println(s4);
        System.out.println(s5);
    }
}
