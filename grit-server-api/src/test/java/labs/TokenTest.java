package labs;

import org.noear.grit.server.api.utils.TokenUtils;

/**
 * @author noear 2024/12/4 created
 */
public class TokenTest {
    public static void main(String[] args) throws Exception {
        System.out.println(TokenUtils.encode(12));
    }
}
