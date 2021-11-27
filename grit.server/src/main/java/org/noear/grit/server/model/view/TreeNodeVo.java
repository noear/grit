package org.noear.grit.server.model.view;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author noear
 * @since 1.0
 */
@Getter
@Setter
public class TreeNodeVo<T> implements Serializable {
    T data;
    int level;

    public TreeNodeVo(T data, int level){
        this.data = data;
        this.level = level;
    }
}
