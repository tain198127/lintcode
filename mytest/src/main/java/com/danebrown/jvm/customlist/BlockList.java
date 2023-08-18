package com.danebrown.jvm.customlist;


import java.util.ArrayList;
import java.util.List;

/**
 * description
 * date 2022-04-24 11:04
 *
 * @author sunshan
 */
public class BlockList<E> {


    //记录块大小，默认一个块100个元素
    private int blockSize = 100;
    //块总数
    private int blocks = 0;
    //当前数组下标
    private int pos = 0;
    //是否动态扩容
    private boolean isExpand = false;


    /**
     * 获取缓存表数据对象
     *
     * @return
     */
    public ArrayList<List<E>> getAllRecords() {
        return allRecords;
    }


    private ArrayList<List<E>> allRecords = new ArrayList<List<E>>();

    /**
     * 返回记录总数
     *
     * @return
     */
    public int size() {
        return pos;
    }


    public void clear() {
        for (int i = 0; i <= blocks; i++) {
            List<E> block = allRecords.get(i);
            block.clear();
        }
        pos = 0;
    }

    public boolean isEmpty() {
        return pos == 0 ? true : false;
    }

    /**
     * @param blockIndex 块下标
     * @param index      记录下标
     * @param ele        元素
     * @return 返回旧元素
     */
    public E set(int blockIndex, int index, E ele) {
        E oldEle = allRecords.get(blockIndex).get(index);
        allRecords.get(blockIndex).set(index, ele);
        return oldEle;
    }

    /**
     * @param index 元素下标
     * @param ele
     * @return
     */
    public E set(int index, E ele) {
        int blockIndex = index / blockSize;
        int ix = index % blockSize;
        E oldEle = allRecords.get(blockIndex).get(ix);
        allRecords.get(blockIndex).set(ix, ele);
        return oldEle;
    }



    /**
     * 根据下标删除元素
     *
     * @param index 元素下标
     * @return
     * @throws IllegalArgumentException
     */
    public E remove(int index) throws IllegalArgumentException {
        if (index < 0 || index >= pos) {
            throw new IllegalArgumentException("数组下标越界");
        }
        int blockIndex = index / blockSize;
        int ix = index % blockSize;
        E oldEle = allRecords.get(blockIndex).get(ix);
        if (blockIndex == blocks) {
            allRecords.get(blockIndex).remove(ix);
            if (allRecords.get(blockIndex).size() == 0 && blocks != 0) {
                blocks--;
                allRecords.remove(blockIndex);
            }
            pos--;
        } else {
            //block块数据移动
            // moveEle(blockIndex, ix);
            //拿最后一个元素去覆盖待删除元素
            fillEle(blockIndex, ix);
        }
        return oldEle;
    }

    /**
     * 用最后一个元素覆盖当前待删除元素
     *
     * @param currentBlockIndex
     * @param startIndex
     */
    private void fillEle(int currentBlockIndex, int startIndex) {
        int lastIndex = pos - 1;
        E lastEle = get(lastIndex);
        //覆盖即删除
        set(currentBlockIndex, startIndex, lastEle);
        //删除最后一个元素
        remove(lastIndex);
    }

    /**
     * 移动元素
     *
     * @param currentBlockIndex 当前块号
     * @param startIndex        块内索引
     */
    public void moveEle(int currentBlockIndex, int startIndex) {
        int s2;
        int s3 = allRecords.get(currentBlockIndex).size() - 1;
        for (s2 = startIndex; s2 < s3; s2++) {
            E nextEle = allRecords.get(currentBlockIndex).get(s2 + 1);
            set(currentBlockIndex, s2, nextEle);
        }
        if (currentBlockIndex == blocks) {
            allRecords.get(currentBlockIndex).remove(s2);
        }
        int nextBlock = currentBlockIndex + 1;
        if (nextBlock > blocks) {
            return;
        }
        E firstEle = allRecords.get(nextBlock).get(0);
        set(currentBlockIndex, s2, firstEle);
        if (blocks == nextBlock && allRecords.get(nextBlock).size() == 1) {
            blocks--;
            allRecords.get(nextBlock).clear();
            allRecords.remove(nextBlock);
            return;
        } else {
            moveEle(nextBlock, 0);
        }
    }

    //默认构造方法中设置初始大小，设置可动态扩容
    public BlockList() {
        ArrayList<E> firstBlock = new ArrayList<>(blockSize);
        allRecords.add(firstBlock);
        isExpand = true;
    }

    /**
     * @param count 数组大小
     */
    public BlockList(int count) {
        blocks = count / blockSize;
        for (int i = 0; i <= blocks; i++) {
            ArrayList<E> blockEnt = new ArrayList<>(blockSize);
            allRecords.add(blockEnt);
        }
    }

    /**
     * @param e 增加元素
     * @return
     */
    public boolean add(E e) {
        if (pos < blockSize) {
            allRecords.get(0).add(e);
        } else {
            int blockIndex = pos / blockSize;
            int index = pos % blockSize;
            if (index == 0 && isExpand) {
                blocks++;
                ArrayList<E> block = new ArrayList<>(blockSize);
                allRecords.add(block);
            }
            allRecords.get(blockIndex).add(e);
        }
        pos++;
        return true;
    }

    public void addAll(List<E> list) {
        for (E obj : list) {
            add(obj);
        }
    }

    /**
     * 根据下标获取元素
     *
     * @param index
     */
    public E get(int index) {
        if (index < 0 || index >= pos) {
            return null;
        }
        int blockIndex = index / blockSize;
        int inx = index % blockSize;
        return allRecords.get(blockIndex).get(inx);
    }

    /**
     * 获取全量数据
     *
     * @return
     */
    public List getCacheEle() {
        if (pos == 0) {
            return new ArrayList<>();
        }
        List records = new ArrayList(pos);
        E ele;
        for (int i = 0; i <= blocks; i++) {
            if (i == blocks) {
                int lastBlockSize = pos % blockSize;
                if (lastBlockSize == 0) lastBlockSize = blockSize;
                for (int k = 0; k < lastBlockSize; k++) {
                    ele = allRecords.get(i).get(k);
                    records.add(ele);
                }
            } else {
                for (int j = 0; j < blockSize; j++) {
                    ele = allRecords.get(i).get(j);
                    records.add(ele);
                }
            }
        }
        return records;
    }
}