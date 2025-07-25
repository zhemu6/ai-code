package com.lushihao.aicode.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求类
 * @author lushihao
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id 根据id删除
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
