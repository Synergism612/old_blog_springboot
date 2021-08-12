package com.project.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Aboutme implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关于我的头像
     */
    private String icon;

    /**
     * 关于我的名字
     */
    private String name;

    /**
     * 关于我的留言
     */
    private String note;

    /**
     * 关于我的简介
     */
    private String intro;

    /**
     * 关于我的内容
     */
    private String content;


}
