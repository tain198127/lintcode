package com.danebrown.jvm.customlist;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * description
 * date 2022-04-24 11:04
 * 用于存储参数表缓存记录索引下标
 *
 * @author sunshan
 */
public class IndexList {
    //存储元素索引下标
    private int[] index;
    //下标个数
    private int size;
    //初始大小
    private int initSize = 10;
    //当前数组下标
    private int currentIndex = 0;
    //超过1千个元素，数组初始大小为50，否则为10
    private int initArrayBound = 1000;
    //转为bit存储标识
    private boolean bitSoreFlag = false;
    //当达到4*1024=4096个数时，即4*4096 =16KB时转成用BitSet
    private int bitTransferCnt = 4096;
    //bit位置集合
    private BitSet bitIndex;
    //记录总数
    private int records;

    //根据元素总记录数来分配索引数组大小
    public IndexList(int recordSize) {
        if (recordSize >= initArrayBound) {
            int len = ((recordSize + "").length() - 1) * initSize;
            index = new int[len];
            records = recordSize;
            size = len;
        } else {
            index = new int[initSize];
            size = initSize;
        }
    }


    /**
     * 当index数组达到20KB容量，转成用BitSet存储
     *
     * @return
     */
    public BitSet transferBitSet() {
        BitSet bitSet = new BitSet(records);
        for (int i = 0; i < currentIndex; i++) {
            bitSet.set(index[i], true);
        }
        return bitSet;
    }

    public void add(int pos) {
        if (currentIndex == bitTransferCnt) {
            bitSoreFlag = true;
            bitIndex = transferBitSet();
            //清理index数组
            index = null;
        }
        if (bitSoreFlag) {
            bitIndex.set(pos, true);
            currentIndex++;
        } else {
            if (currentIndex < size) {
                index[currentIndex++] = pos;
            } else {
                int length = size + size / 2;
                int[] newIndex = new int[length];
                System.arraycopy(index, 0, newIndex, 0, size);
                newIndex[currentIndex++] = pos;
                index = newIndex;
                size = length;
            }
        }
    }


    @Override
    public String toString() {
        int iMax = currentIndex - 1;
        if (iMax == -1) return "[]";
        if (bitSoreFlag) {
            ArrayList list = new ArrayList<>();
            int key = getIndexFromBitSet(0);
            while (key != -1) {
                list.add(key);
                key = getIndexFromBitSet(key + 1);
            }
            return list.toString();
        } else {
            if (index == null) return "null";
            StringBuilder b = new StringBuilder();
            b.append('[');
            for (int i = 0; ; i++) {
                b.append(index[i]);
                if (i == iMax) return b.append(']').toString();
                b.append(", ");
            }
        }
    }

    public int getEle(int pos) {
        return index[pos];
    }

    public int getSize() {
        return size;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isBitSoreFlag() {
        return bitSoreFlag;
    }

    /**
     * 从fromInde下标开始返回Bit值为1的Bit位置
     *
     * @param fromIndex
     * @return
     */
    public int getIndexFromBitSet(int fromIndex) {
        return bitIndex.nextSetBit(fromIndex);
    }
}
