package cn.atsoft.dasheng.core.lock;

/**
 * 分布式锁接口
 *
 * @author fengshuonan
 * @Date 2019-09-25 16:03
 */
public interface DistributedLock {

    /**
     * 获取锁
     */
    boolean acquire();

    /**
     * 释放锁
     */
    void release();

}