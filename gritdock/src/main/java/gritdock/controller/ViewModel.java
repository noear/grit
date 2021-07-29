package gritdock.controller;

import java.util.HashMap;

/**
 * Created by noear on 14-9-10.
 */
//用作视图返回的模型
public class ViewModel extends HashMap<String,Object>{

    //put 的另一版本；返回自己；方便快速操作；
    public ViewModel set(String name, Object val)
    {
        put(name,val);
        return this;
    }

    public void code(int code){
        put("code",code);
    }

    public void code(int code, String msg){
        put("code",code);
        put("msg",msg);
    }

    public void msg(String msg) {
        put("msg", msg);
    }
}
