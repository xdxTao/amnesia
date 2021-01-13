package com.xdx.entitys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务模板
 *
 * @author 小道仙
 * @date 2020年12月26日
 */
@Data
@Accessors(chain = true)
public class SyTemplate implements Serializable {
    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 标签id
     */
    private Integer labelId;

    /**
     * 模板标题
     */
    private String tmpTitle;

    /**
     * 模板描述
     */
    private String tmpDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public String getTmpTitle() {
        return tmpTitle;
    }

    public void setTmpTitle(String tmpTitle) {
        this.tmpTitle = tmpTitle;
    }

    public String getTmpDesc() {
        return tmpDesc;
    }

    public void setTmpDesc(String tmpDesc) {
        this.tmpDesc = tmpDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SyTemplate other = (SyTemplate) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getLabelId() == null ? other.getLabelId() == null : this.getLabelId().equals(other.getLabelId()))
            && (this.getTmpTitle() == null ? other.getTmpTitle() == null : this.getTmpTitle().equals(other.getTmpTitle()))
            && (this.getTmpDesc() == null ? other.getTmpDesc() == null : this.getTmpDesc().equals(other.getTmpDesc()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getLabelId() == null) ? 0 : getLabelId().hashCode());
        result = prime * result + ((getTmpTitle() == null) ? 0 : getTmpTitle().hashCode());
        result = prime * result + ((getTmpDesc() == null) ? 0 : getTmpDesc().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", labelId=").append(labelId);
        sb.append(", tmpTitle=").append(tmpTitle);
        sb.append(", tmpDesc=").append(tmpDesc);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}