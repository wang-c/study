package com.github.tjp.design.chain.arr;

/**
 * 组装订单处理链的对象工厂:
 * list组织模式的责任链OrderProcessorChain,每一调用它的doProcess时,都需要移动一个指针指向处理链的下一个,
 * 这种模式是线程不安全的,每次发起处理器链的调用时,都需要构建一个新的责任链,因此这里需要处理链构建工厂的接口抽象
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/9 上午12:29
 */
public interface OrderProcessorChainFactory {

    OrderProcessorChain bulidOrderProcessorChain();

}
