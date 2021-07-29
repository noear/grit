package org.noear.grit.client;

import org.noear.grit.client.impl.EncryptUtils;
import org.noear.grit.client.impl.utils.TextUtils;
import org.noear.grit.client.model.Group;
import org.noear.grit.client.model.Resource;

/**
 * @author noear
 * @since 1.3
 */
public class GritUtil {
    /**
     * 构建密码
     */
    public static String buildPassword(String longName, String loginPassword) {
        return EncryptUtils.sha1(longName + "#" + loginPassword);
    }

    /**
     * 构建地址
     */
    public static String buildDockpath(Resource res) {
        if (res == null || TextUtils.isEmpty(res.link_uri)) {
            return "";
        } else {
            if (res.link_uri.indexOf("/$") > 0) {
                if (res.is_fullview) {
                    return res.link_uri + "?@=";
                } else {
                    return res.link_uri;
                }
            } else {
                if (res.is_fullview) {
                    return res.link_uri + "/@" + res.display_name + "?@=";
                } else {
                    return res.link_uri + "/@" + res.display_name;
                }
            }
        }
    }

    public static String buildDockurl(Group branched, Resource res) {


        if (res == null || TextUtils.isEmpty(res.link_uri)) {
            return "";
        } else {
            if (res.link_uri.indexOf("/$") > 0) {
                if (res.is_fullview) {
                    return "/." + branched.group_code + res.link_uri + "?@=";
                }else{
                    return "/." + branched.group_code + res.link_uri;
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("/.").append(branched.group_code).append(res.link_uri).append("/@").append(res.display_name);

                if (res.is_fullview) {
                    sb.append("?@=");
                }

                if (res.remark != null && res.remark.indexOf("://") > 0) {
                    if (sb.indexOf("?") > 0) {
                        sb.append("&__r=").append(res.resource_id);
                    } else {
                        sb.append("?__r=").append(res.resource_id);
                    }
                }

                return sb.toString();
            }
        }
    }

    public static String buildGroupCodeByPath(String path){
        int start_idx = path.indexOf(".");

        if(start_idx==1) {
            start_idx += 1;
            int end_idx = path.indexOf("/", start_idx);

            return path.substring(start_idx, end_idx);
        }else{
            return "";
        }
    }

    public static String cleanGroupCodeAtPath(String path){
        return path.replaceAll("/\\.[^/]*","");
    }

}
