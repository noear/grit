package gritdock.dso;


import org.noear.grit.model.domain.Subject;
import org.noear.solon.extend.grit.SessionBase;

/**
 * 跨应用会话管理
 * */
public final class Session extends SessionBase {

    private static final Session _current = new Session();
    public static Session current(){
        return _current;
    }


    //////////////////////////////////
    //当前项目的扩展

    @Override
    public void loadSubject(Subject subject) throws Exception {
        setUserId(subject.subject_id);
        setLoginName(subject.login_name);
        setDisplayName(subject.display_name);
    }


    /**
     * 获取验证码
     * */
    public final String getValidation() {
        return localGet("Validation_String", null);
    }

    /**
     * 设置验证码
     * */
    public final void setValidation(String validation) {
        localSet("Validation_String", validation.toLowerCase());
    }
}
