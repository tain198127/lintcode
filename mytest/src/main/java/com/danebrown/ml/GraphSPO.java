package com.danebrown.ml;

import com.google.common.collect.Sets;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.google.common.math.BigIntegerMath;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.MurmurHash2;
import org.apache.commons.codec.digest.MurmurHash3;
import org.apache.commons.math3.analysis.function.Abs;
import org.apache.logging.log4j.util.Strings;
import org.apache.lucene.util.LongBitSet;
import org.apache.lucene.util.MathUtil;
import org.apache.lucene.util.RamUsageEstimator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * Created by danebrown on 2020/3/24
 * mail: tain198127@163.com
 */
@Log4j2
@Data
public class GraphSPO {


    private List<MutableValueGraph<String, String>> subGraphs = new ArrayList<>();

    public MutableValueGraph<String, String> build(int times) {

        MutableValueGraph<String, String> mutableGraph = ValueGraphBuilder.undirected()
                .allowsSelfLoops(true)

                .build();
        for (int i = 0; i < times; i++) {
            int begin = ThreadLocalRandom.current().nextInt(i, times);
            int end = ThreadLocalRandom.current().nextInt(i, times);
            mutableGraph.putEdgeValue(String.valueOf(begin), String.valueOf(end), begin % 2 == 0 ? "ID" : "PHONE");
        }
//

        return mutableGraph;

    }

    public void buildSubGraph(MutableValueGraph<String, String> network) {
        if (network == null || network.nodes() == null || network.nodes().isEmpty())
            return;
        String firstNode = network.nodes().stream().findFirst().get();

        Set<String> nodes = Graphs.reachableNodes(network, firstNode);
        MutableValueGraph<String, String> innerMutableGraph = Graphs.inducedSubgraph(network, nodes);
        if (nodes == null || nodes.isEmpty())
            return;
        nodes.stream().forEach(n -> {
            network.removeNode(n);
        });
        this.subGraphs.add(innerMutableGraph);
    }

    public void buildAllSubNodes(MutableValueGraph<String, String> network) {
        while (network != null && network.nodes() != null && !network.nodes().isEmpty()) {
            buildSubGraph(network);
        }
    }


    private static void printObjSize(Object object){
        log.info("size is :{}",RamUsageEstimator.humanReadableUnits(RamUsageEstimator.sizeOfObject(object)));
    }

    public static void main(String[] args) {
        GraphSPO spo = new GraphSPO();
        MutableValueGraph<String, String> baseGraph = spo.build(50);
        Gson gson = new Gson();

        log.info("节点数[{}],边数[{}]，占用空间大小[{}]mb", baseGraph.nodes().size(), baseGraph.edges().size(), gson.toJson(baseGraph).getBytes().length / (1024 * 1024));
        spo.buildAllSubNodes(baseGraph);
//        log.info("getSubGraphs:{}", spo.getSubGraphs());
        log.info("oldGraph:{}", baseGraph);

        Set<String> firstSet = new HashSet<>();
        Set<String> secondSet = new HashSet<>();

        firstSet.addAll(Arrays.asList("1", "2", "3", "4"));
        secondSet.addAll(Arrays.asList("A", "B", "C", "4"));
        Set<String> cross = new HashSet<>();
        cross.addAll(firstSet);
        cross.retainAll(secondSet);
        log.info("{}", cross);

        int highDimArray = MurmurHash2.hash32("adfafasdfsfsdfsdfsdfsdfds");
        log.info("高维hash值是{}",highDimArray);
        BitSet ontBitSet = new BitSet();
        Set<String> needToHash = Sets.newHashSet("0a6a9ee3-9be5-4e4e-87d4-68836ab427d4",
                "13905221971",
                "15895253838",
                "110101198804129197",
                "110101198804124054",
                "15152047807",
                "110101198804129859",
                "110101198804124193",
                "110101198804120758",
                "15152048355",
                "110101198804120010",
                "110101198804127095",
                "15895250011",
                "110101198804126594",
                "15895252090",
                "13905229051",
                "13905224548");
        ontBitSet.set(highDimArray);

        needToHash.forEach(s -> ontBitSet.set(Math.abs(MurmurHash2.hash32(s))) );
        long startTime = System.nanoTime();
        printObjSize(ontBitSet);
        long endTime = System.nanoTime();
        log.info("使用MurmurHash2计算——>计算时间={}纳秒",endTime-startTime);
        Set<String> needToCheck = Sets.newHashSet("0a6a9ee3-9be5-4e4e-87d4-68836ab427d4",

                "13905224548");

        BitSet ontBitSet2 = new BitSet();
        needToCheck.forEach(s -> ontBitSet2.set(Math.abs(MurmurHash2.hash32(s))));
        printObjSize(ontBitSet2);
         startTime = System.nanoTime();
        boolean isCross = ontBitSet.intersects(ontBitSet2);
         endTime = System.nanoTime();
        log.info("使用MurmurHash2计算——>是否相交：{};计算时间={}纳秒",isCross,endTime-startTime);

        startTime = System.nanoTime();
        boolean isSetCross = needToHash.retainAll(needToCheck);
        endTime = System.nanoTime();
        log.info("使用set计算——>是否相交：{};计算时间={}纳秒,序列化大小{}",isSetCross,endTime-startTime,gson.toJson(needToHash).length());
//        String longNumber = Strings.EMPTY;
//        StringBuilder stringBuilder = new StringBuilder();
//
//        longNumber = stringBuilder.toString();
//        BigDecimal bigDecimal = new BigDecimal(longNumber);
//        printObjSize(bigDecimal);
//        log.info("bigDecimal:{}",bigDecimal);


    }
}
