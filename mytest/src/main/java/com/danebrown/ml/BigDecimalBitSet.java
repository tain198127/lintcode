//package com.danebrown.ml;
//
//import com.alibaba.fastjson.annotation.JSONField;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.google.gson.annotations.Expose;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.nio.ByteBuffer;
//import java.util.*;
//import java.util.concurrent.ConcurrentLinkedDeque;
//import java.util.stream.IntStream;
//import java.util.stream.StreamSupport;
//
///**
// * Created by danebrown on 2020/4/2
// * mail: tain198127@163.com
// */
//public class BigDecimalBitSet implements Serializable {
//    //每个BitSet长度为1000000000
//    private Deque<BitSet> segment = new ConcurrentLinkedDeque();
//    //用不同的端来表示一个长数字
//    @JsonIgnore
//    @Expose(serialize = false,deserialize=false)
//    @JSONField()
//    private static int LENGTH_OF_PER_SEGMENT = 1000000000;
//    //用于保存长数字
//
//    private String innerStringFormNum = StringUtils.EMPTY;
//    private BigDecimal innerBigDecimal;
//
//    /**
//     * Creates a new bit set. All bits are initially {@code false}.
//     */
//    public BigDecimalBitSet() {
//        segment.add(new BitSet());
//    }
//
//
//
//    /**
//     * Returns a new long array containing all the bits in this bit set.
//     *
//     * <p>More precisely, if
//     * <br>{@code long[] longs = s.toLongArray();}
//     * <br>then {@code longs.length == (s.length()+63)/64} and
//     * <br>{@code s.get(n) == ((longs[n/64] & (1L<<(n%64))) != 0)}
//     * <br>for all {@code n < 64 * longs.length}.
//     *
//     * @return a long array containing a little-endian representation
//     * of all the bits in this bit set
//     * @since 1.7
//     */
//    public BigDecimal toBigDecimal() {
//
//    }
//
//
//    /**
//     * Sets the bit at the specified index to the complement of its
//     * current value.
//     *
//     * @param bitIndex the index of the bit to flip
//     * @throws IndexOutOfBoundsException if the specified index is negative
//     * @since 1.4
//     */
//    public void flip(BigDecimal bitIndex) {
//        if (bitIndex < 0)
//            throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
//
//        int wordIndex = wordIndex(bitIndex);
//        expandTo(wordIndex);
//
//        words[wordIndex] ^= (1L << bitIndex);
//
//        recalculateWordsInUse();
//        checkInvariants();
//    }
//
//    /**
//     * Sets each bit from the specified {@code fromIndex} (inclusive) to the
//     * specified {@code toIndex} (exclusive) to the complement of its current
//     * value.
//     *
//     * @param fromIndex index of the first bit to flip
//     * @param toIndex   index after the last bit to flip
//     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative,
//     *                                   or {@code toIndex} is negative, or {@code fromIndex} is
//     *                                   larger than {@code toIndex}
//     * @since 1.4
//     */
//    public void flip(BigDecimal fromIndex, BigDecimal toIndex) {
//        checkRange(fromIndex, toIndex);
//
//        if (fromIndex == toIndex)
//            return;
//
//        int startWordIndex = wordIndex(fromIndex);
//        int endWordIndex = wordIndex(toIndex - 1);
//        expandTo(endWordIndex);
//
//        long firstWordMask = WORD_MASK << fromIndex;
//        long lastWordMask = WORD_MASK >>> -toIndex;
//        if (startWordIndex == endWordIndex) {
//            // Case 1: One word
//            words[startWordIndex] ^= (firstWordMask & lastWordMask);
//        } else {
//            // Case 2: Multiple words
//            // Handle first word
//            words[startWordIndex] ^= firstWordMask;
//
//            // Handle intermediate words, if any
//            for (int i = startWordIndex + 1; i < endWordIndex; i++)
//                words[i] ^= WORD_MASK;
//
//            // Handle last word
//            words[endWordIndex] ^= lastWordMask;
//        }
//
//        recalculateWordsInUse();
//        checkInvariants();
//    }
//
//    /**
//     * Sets the bit at the specified index to {@code true}.
//     *
//     * @param bitIndex a bit index
//     * @throws IndexOutOfBoundsException if the specified index is negative
//     * @since JDK1.0
//     */
//    public void set(BigDecimal bitIndex) {
//        String number = bitIndex.toString();
//
//        if (bitIndex < 0)
//            throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
//
//        int wordIndex = wordIndex(bitIndex);
//        expandTo(wordIndex);
//
//        words[wordIndex] |= (1L << bitIndex); // Restores invariants
//
//        checkInvariants();
//    }
//
//    /**
//     * Sets the bit at the specified index to the specified value.
//     *
//     * @param bitIndex a bit index
//     * @param value    a boolean value to set
//     * @throws IndexOutOfBoundsException if the specified index is negative
//     * @since 1.4
//     */
//    public void set(BigDecimal bitIndex, boolean value) {
//        if (value)
//            set(bitIndex);
//        else
//            clear(bitIndex);
//    }
//
//    /**
//     * Sets the bits from the specified {@code fromIndex} (inclusive) to the
//     * specified {@code toIndex} (exclusive) to {@code true}.
//     *
//     * @param fromIndex index of the first bit to be set
//     * @param toIndex   index after the last bit to be set
//     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative,
//     *                                   or {@code toIndex} is negative, or {@code fromIndex} is
//     *                                   larger than {@code toIndex}
//     * @since 1.4
//     */
//    public void set(BigDecimal fromIndex, BigDecimal toIndex) {
//        checkRange(fromIndex, toIndex);
//
//        if (fromIndex == toIndex)
//            return;
//
//        // Increase capacity if necessary
//        int startWordIndex = wordIndex(fromIndex);
//        int endWordIndex = wordIndex(toIndex - 1);
//        expandTo(endWordIndex);
//
//        long firstWordMask = WORD_MASK << fromIndex;
//        long lastWordMask = WORD_MASK >>> -toIndex;
//        if (startWordIndex == endWordIndex) {
//            // Case 1: One word
//            words[startWordIndex] |= (firstWordMask & lastWordMask);
//        } else {
//            // Case 2: Multiple words
//            // Handle first word
//            words[startWordIndex] |= firstWordMask;
//
//            // Handle intermediate words, if any
//            for (int i = startWordIndex + 1; i < endWordIndex; i++)
//                words[i] = WORD_MASK;
//
//            // Handle last word (restores invariants)
//            words[endWordIndex] |= lastWordMask;
//        }
//
//        checkInvariants();
//    }
//
//    /**
//     * Sets the bits from the specified {@code fromIndex} (inclusive) to the
//     * specified {@code toIndex} (exclusive) to the specified value.
//     *
//     * @param fromIndex index of the first bit to be set
//     * @param toIndex   index after the last bit to be set
//     * @param value     value to set the selected bits to
//     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative,
//     *                                   or {@code toIndex} is negative, or {@code fromIndex} is
//     *                                   larger than {@code toIndex}
//     * @since 1.4
//     */
//    public void set(BigDecimal fromIndex, BigDecimal toIndex, boolean value) {
//        if (value)
//            set(fromIndex, toIndex);
//        else
//            clear(fromIndex, toIndex);
//    }
//
//    /**
//     * Sets the bit specified by the index to {@code false}.
//     *
//     * @param bitIndex the index of the bit to be cleared
//     * @throws IndexOutOfBoundsException if the specified index is negative
//     * @since JDK1.0
//     */
//    public void clear(BigDecimal bitIndex) {
//        if (bitIndex < 0)
//            throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
//
//        int wordIndex = wordIndex(bitIndex);
//        if (wordIndex >= wordsInUse)
//            return;
//
//        words[wordIndex] &= ~(1L << bitIndex);
//
//        recalculateWordsInUse();
//        checkInvariants();
//    }
//
//    /**
//     * Sets the bits from the specified {@code fromIndex} (inclusive) to the
//     * specified {@code toIndex} (exclusive) to {@code false}.
//     *
//     * @param fromIndex index of the first bit to be cleared
//     * @param toIndex   index after the last bit to be cleared
//     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative,
//     *                                   or {@code toIndex} is negative, or {@code fromIndex} is
//     *                                   larger than {@code toIndex}
//     * @since 1.4
//     */
//    public void clear(BigDecimal fromIndex, int toIndex) {
//        checkRange(fromIndex, toIndex);
//
//        if (fromIndex == toIndex)
//            return;
//
//        int startWordIndex = wordIndex(fromIndex);
//        if (startWordIndex >= wordsInUse)
//            return;
//
//        int endWordIndex = wordIndex(toIndex - 1);
//        if (endWordIndex >= wordsInUse) {
//            toIndex = length();
//            endWordIndex = wordsInUse - 1;
//        }
//
//        long firstWordMask = WORD_MASK << fromIndex;
//        long lastWordMask = WORD_MASK >>> -toIndex;
//        if (startWordIndex == endWordIndex) {
//            // Case 1: One word
//            words[startWordIndex] &= ~(firstWordMask & lastWordMask);
//        } else {
//            // Case 2: Multiple words
//            // Handle first word
//            words[startWordIndex] &= ~firstWordMask;
//
//            // Handle intermediate words, if any
//            for (int i = startWordIndex + 1; i < endWordIndex; i++)
//                words[i] = 0;
//
//            // Handle last word
//            words[endWordIndex] &= ~lastWordMask;
//        }
//
//        recalculateWordsInUse();
//        checkInvariants();
//    }
//
//    /**
//     * Sets all of the bits in this BitSet to {@code false}.
//     *
//     * @since 1.4
//     */
//    public void clear() {
//        while (wordsInUse > 0)
//            words[--wordsInUse] = 0;
//    }
//
//    /**
//     * Returns the value of the bit with the specified index. The value
//     * is {@code true} if the bit with the index {@code bitIndex}
//     * is currently set in this {@code BitSet}; otherwise, the result
//     * is {@code false}.
//     *
//     * @param bitIndex the bit index
//     * @return the value of the bit with the specified index
//     * @throws IndexOutOfBoundsException if the specified index is negative
//     */
//    public boolean get(BigDecimal bitIndex) {
//        if (bitIndex < 0)
//            throw new IndexOutOfBoundsException("bitIndex < 0: " + bitIndex);
//
//        checkInvariants();
//
//        int wordIndex = wordIndex(bitIndex);
//        return (wordIndex < wordsInUse)
//                && ((words[wordIndex] & (1L << bitIndex)) != 0);
//    }
//
//    /**
//     * Returns a new {@code BitSet} composed of bits from this {@code BitSet}
//     * from {@code fromIndex} (inclusive) to {@code toIndex} (exclusive).
//     *
//     * @param fromIndex index of the first bit to include
//     * @param toIndex   index after the last bit to include
//     * @return a new {@code BitSet} from a range of this {@code BitSet}
//     * @throws IndexOutOfBoundsException if {@code fromIndex} is negative,
//     *                                   or {@code toIndex} is negative, or {@code fromIndex} is
//     *                                   larger than {@code toIndex}
//     * @since 1.4
//     */
//    public BitSet get(BigDecimal fromIndex, BigDecimal toIndex) {
//        checkRange(fromIndex, toIndex);
//
//        checkInvariants();
//
//        int len = length();
//
//        // If no set bits in range return empty bitset
//        if (len <= fromIndex || fromIndex == toIndex)
//            return new BitSet(0);
//
//        // An optimization
//        if (toIndex > len)
//            toIndex = len;
//
//        BitSet result = new BitSet(toIndex - fromIndex);
//        int targetWords = wordIndex(toIndex - fromIndex - 1) + 1;
//        int sourceIndex = wordIndex(fromIndex);
//        boolean wordAligned = ((fromIndex & BIT_INDEX_MASK) == 0);
//
//        // Process all words but the last word
//        for (int i = 0; i < targetWords - 1; i++, sourceIndex++)
//            result.words[i] = wordAligned ? words[sourceIndex] :
//                    (words[sourceIndex] >>> fromIndex) |
//                            (words[sourceIndex + 1] << -fromIndex);
//
//        // Process the last word
//        long lastWordMask = WORD_MASK >>> -toIndex;
//        result.words[targetWords - 1] =
//                ((toIndex - 1) & BIT_INDEX_MASK) < (fromIndex & BIT_INDEX_MASK)
//                        ? /* straddles source words */
//                        ((words[sourceIndex] >>> fromIndex) |
//                                (words[sourceIndex + 1] & lastWordMask) << -fromIndex)
//                        :
//                        ((words[sourceIndex] & lastWordMask) >>> fromIndex);
//
//        // Set wordsInUse correctly
//        result.wordsInUse = targetWords;
//        result.recalculateWordsInUse();
//        result.checkInvariants();
//
//        return result;
//    }
//
//    /**
//     * Returns the index of the first bit that is set to {@code true}
//     * that occurs on or after the specified starting index. If no such
//     * bit exists then {@code -1} is returned.
//     *
//     * <p>To iterate over the {@code true} bits in a {@code BitSet},
//     * use the following loop:
//     *
//     * <pre> {@code
//     * for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
//     *     // operate on index i here
//     *     if (i == Integer.MAX_VALUE) {
//     *         break; // or (i+1) would overflow
//     *     }
//     * }}</pre>
//     *
//     * @param fromIndex the index to start checking from (inclusive)
//     * @return the index of the next set bit, or {@code -1} if there
//     * is no such bit
//     * @throws IndexOutOfBoundsException if the specified index is negative
//     * @since 1.4
//     */
//    public BigDecimal nextSetBit(BigDecimal fromIndex) {
//        if (fromIndex < 0)
//            throw new IndexOutOfBoundsException("fromIndex < 0: " + fromIndex);
//
//        checkInvariants();
//
//        int u = wordIndex(fromIndex);
//        if (u >= wordsInUse)
//            return -1;
//
//        long word = words[u] & (WORD_MASK << fromIndex);
//
//        while (true) {
//            if (word != 0)
//                return (u * BITS_PER_WORD) + Long.numberOfTrailingZeros(word);
//            if (++u == wordsInUse)
//                return -1;
//            word = words[u];
//        }
//    }
//
//    /**
//     * Returns the index of the first bit that is set to {@code false}
//     * that occurs on or after the specified starting index.
//     *
//     * @param fromIndex the index to start checking from (inclusive)
//     * @return the index of the next clear bit
//     * @throws IndexOutOfBoundsException if the specified index is negative
//     * @since 1.4
//     */
//    public BigDecimal nextClearBit(BigDecimal fromIndex) {
//        // Neither spec nor implementation handle bitsets of maximal length.
//        // See 4816253.
//        if (fromIndex < 0)
//            throw new IndexOutOfBoundsException("fromIndex < 0: " + fromIndex);
//
//        checkInvariants();
//
//        int u = wordIndex(fromIndex);
//        if (u >= wordsInUse)
//            return fromIndex;
//
//        long word = ~words[u] & (WORD_MASK << fromIndex);
//
//        while (true) {
//            if (word != 0)
//                return (u * BITS_PER_WORD) + Long.numberOfTrailingZeros(word);
//            if (++u == wordsInUse)
//                return wordsInUse * BITS_PER_WORD;
//            word = ~words[u];
//        }
//    }
//
//    /**
//     * Returns the index of the nearest bit that is set to {@code true}
//     * that occurs on or before the specified starting index.
//     * If no such bit exists, or if {@code -1} is given as the
//     * starting index, then {@code -1} is returned.
//     *
//     * <p>To iterate over the {@code true} bits in a {@code BitSet},
//     * use the following loop:
//     *
//     * <pre> {@code
//     * for (int i = bs.length(); (i = bs.previousSetBit(i-1)) >= 0; ) {
//     *     // operate on index i here
//     * }}</pre>
//     *
//     * @param fromIndex the index to start checking from (inclusive)
//     * @return the index of the previous set bit, or {@code -1} if there
//     * is no such bit
//     * @throws IndexOutOfBoundsException if the specified index is less
//     *                                   than {@code -1}
//     * @since 1.7
//     */
//    public BigDecimal previousSetBit(BigDecimal fromIndex) {
//        if (fromIndex < 0) {
//            if (fromIndex == -1)
//                return -1;
//            throw new IndexOutOfBoundsException(
//                    "fromIndex < -1: " + fromIndex);
//        }
//
//        checkInvariants();
//
//        int u = wordIndex(fromIndex);
//        if (u >= wordsInUse)
//            return length() - 1;
//
//        long word = words[u] & (WORD_MASK >>> -(fromIndex + 1));
//
//        while (true) {
//            if (word != 0)
//                return (u + 1) * BITS_PER_WORD - 1 - Long.numberOfLeadingZeros(word);
//            if (u-- == 0)
//                return -1;
//            word = words[u];
//        }
//    }
//
//    /**
//     * Returns the index of the nearest bit that is set to {@code false}
//     * that occurs on or before the specified starting index.
//     * If no such bit exists, or if {@code -1} is given as the
//     * starting index, then {@code -1} is returned.
//     *
//     * @param fromIndex the index to start checking from (inclusive)
//     * @return the index of the previous clear bit, or {@code -1} if there
//     * is no such bit
//     * @throws IndexOutOfBoundsException if the specified index is less
//     *                                   than {@code -1}
//     * @since 1.7
//     */
//    public BigDecimal previousClearBit(BigDecimal fromIndex) {
//        if (fromIndex < 0) {
//            if (fromIndex == -1)
//                return -1;
//            throw new IndexOutOfBoundsException(
//                    "fromIndex < -1: " + fromIndex);
//        }
//
//        checkInvariants();
//
//        int u = wordIndex(fromIndex);
//        if (u >= wordsInUse)
//            return fromIndex;
//
//        long word = ~words[u] & (WORD_MASK >>> -(fromIndex + 1));
//
//        while (true) {
//            if (word != 0)
//                return (u + 1) * BITS_PER_WORD - 1 - Long.numberOfLeadingZeros(word);
//            if (u-- == 0)
//                return -1;
//            word = ~words[u];
//        }
//    }
//
//    /**
//     * Returns the "logical size" of this {@code BitSet}: the index of
//     * the highest set bit in the {@code BitSet} plus one. Returns zero
//     * if the {@code BitSet} contains no set bits.
//     *
//     * @return the logical size of this {@code BitSet}
//     * @since 1.2
//     */
//    public BigDecimal length() {
//        if (wordsInUse == 0)
//            return 0;
//
//        return BITS_PER_WORD * (wordsInUse - 1) +
//                (BITS_PER_WORD - Long.numberOfLeadingZeros(words[wordsInUse - 1]));
//    }
//
//    /**
//     * Returns true if this {@code BitSet} contains no bits that are set
//     * to {@code true}.
//     *
//     * @return boolean indicating whether this {@code BitSet} is empty
//     * @since 1.4
//     */
//    public boolean isEmpty() {
//        return wordsInUse == 0;
//    }
//
//    /**
//     * Returns true if the specified {@code BitSet} has any bits set to
//     * {@code true} that are also set to {@code true} in this {@code BitSet}.
//     *
//     * @param set {@code BitSet} to intersect with
//     * @return boolean indicating whether this {@code BitSet} intersects
//     * the specified {@code BitSet}
//     * @since 1.4
//     */
//    public boolean intersects(BigDecimalBitSet set) {
//        for (int i = Math.min(wordsInUse, set.wordsInUse) - 1; i >= 0; i--)
//            if ((words[i] & set.words[i]) != 0)
//                return true;
//        return false;
//    }
//
//    /**
//     * Returns the number of bits set to {@code true} in this {@code BitSet}.
//     *
//     * @return the number of bits set to {@code true} in this {@code BitSet}
//     * @since 1.4
//     */
//    public BigDecimal cardinality() {
//        int sum = 0;
//        for (int i = 0; i < wordsInUse; i++)
//            sum += Long.bitCount(words[i]);
//        return sum;
//    }
//
//    /**
//     * Performs a logical <b>AND</b> of this target bit set with the
//     * argument bit set. This bit set is modified so that each bit in it
//     * has the value {@code true} if and only if it both initially
//     * had the value {@code true} and the corresponding bit in the
//     * bit set argument also had the value {@code true}.
//     *
//     * @param set a bit set
//     */
//    public void and(BigDecimalBitSet set) {
//        if (this == set)
//            return;
//
//        while (wordsInUse > set.wordsInUse)
//            words[--wordsInUse] = 0;
//
//        // Perform logical AND on words in common
//        for (int i = 0; i < wordsInUse; i++)
//            words[i] &= set.words[i];
//
//        recalculateWordsInUse();
//        checkInvariants();
//    }
//
//    /**
//     * Performs a logical <b>OR</b> of this bit set with the bit set
//     * argument. This bit set is modified so that a bit in it has the
//     * value {@code true} if and only if it either already had the
//     * value {@code true} or the corresponding bit in the bit set
//     * argument has the value {@code true}.
//     *
//     * @param set a bit set
//     */
//    public void or(BigDecimalBitSet set) {
//        if (this == set)
//            return;
//
//        int wordsInCommon = Math.min(wordsInUse, set.wordsInUse);
//
//        if (wordsInUse < set.wordsInUse) {
//            ensureCapacity(set.wordsInUse);
//            wordsInUse = set.wordsInUse;
//        }
//
//        // Perform logical OR on words in common
//        for (int i = 0; i < wordsInCommon; i++)
//            words[i] |= set.words[i];
//
//        // Copy any remaining words
//        if (wordsInCommon < set.wordsInUse)
//            System.arraycopy(set.words, wordsInCommon,
//                    words, wordsInCommon,
//                    wordsInUse - wordsInCommon);
//
//        // recalculateWordsInUse() is unnecessary
//        checkInvariants();
//    }
//
//    /**
//     * Performs a logical <b>XOR</b> of this bit set with the bit set
//     * argument. This bit set is modified so that a bit in it has the
//     * value {@code true} if and only if one of the following
//     * statements holds:
//     * <ul>
//     * <li>The bit initially has the value {@code true}, and the
//     *     corresponding bit in the argument has the value {@code false}.
//     * <li>The bit initially has the value {@code false}, and the
//     *     corresponding bit in the argument has the value {@code true}.
//     * </ul>
//     *
//     * @param set a bit set
//     */
//    public void xor(BigDecimalBitSet set) {
//        int wordsInCommon = Math.min(wordsInUse, set.wordsInUse);
//
//        if (wordsInUse < set.wordsInUse) {
//            ensureCapacity(set.wordsInUse);
//            wordsInUse = set.wordsInUse;
//        }
//
//        // Perform logical XOR on words in common
//        for (int i = 0; i < wordsInCommon; i++)
//            words[i] ^= set.words[i];
//
//        // Copy any remaining words
//        if (wordsInCommon < set.wordsInUse)
//            System.arraycopy(set.words, wordsInCommon,
//                    words, wordsInCommon,
//                    set.wordsInUse - wordsInCommon);
//
//        recalculateWordsInUse();
//        checkInvariants();
//    }
//
//    /**
//     * Clears all of the bits in this {@code BitSet} whose corresponding
//     * bit is set in the specified {@code BitSet}.
//     *
//     * @param set the {@code BitSet} with which to mask this
//     *            {@code BitSet}
//     * @since 1.2
//     */
//    public void andNot(BitSet set) {
//        // Perform logical (a & !b) on words in common
//        for (int i = Math.min(wordsInUse, set.wordsInUse) - 1; i >= 0; i--)
//            words[i] &= ~set.words[i];
//
//        recalculateWordsInUse();
//        checkInvariants();
//    }
//
//    /**
//     * Returns the hash code value for this bit set. The hash code depends
//     * only on which bits are set within this {@code BitSet}.
//     *
//     * <p>The hash code is defined to be the result of the following
//     * calculation:
//     * <pre> {@code
//     * public int hashCode() {
//     *     long h = 1234;
//     *     long[] words = toLongArray();
//     *     for (int i = words.length; --i >= 0; )
//     *         h ^= words[i] * (i + 1);
//     *     return (int)((h >> 32) ^ h);
//     * }}</pre>
//     * Note that the hash code changes if the set of bits is altered.
//     *
//     * @return the hash code value for this bit set
//     */
//    public int hashCode() {
//        long h = 1234;
//        for (int i = wordsInUse; --i >= 0; )
//            h ^= words[i] * (i + 1);
//
//        return (int) ((h >> 32) ^ h);
//    }
//
//    /**
//     * Returns the number of bits of space actually in use by this
//     * {@code BitSet} to represent bit values.
//     * The maximum element in the set is the size - 1st element.
//     *
//     * @return the number of bits currently in this bit set
//     */
//    public BigDecimal size() {
//        return words.length * BITS_PER_WORD;
//    }
//
//    /**
//     * Compares this object against the specified object.
//     * The result is {@code true} if and only if the argument is
//     * not {@code null} and is a {@code Bitset} object that has
//     * exactly the same set of bits set to {@code true} as this bit
//     * set. That is, for every nonnegative {@code int} index {@code k},
//     * <pre>((BitSet)obj).get(k) == this.get(k)</pre>
//     * must be true. The current sizes of the two bit sets are not compared.
//     *
//     * @param obj the object to compare with
//     * @return {@code true} if the objects are the same;
//     * {@code false} otherwise
//     * @see #size()
//     */
//    public boolean equals(Object obj) {
//        if (!(obj instanceof BitSet))
//            return false;
//        if (this == obj)
//            return true;
//
//        BitSet set = (BitSet) obj;
//
//        checkInvariants();
//        set.checkInvariants();
//
//        if (wordsInUse != set.wordsInUse)
//            return false;
//
//        // Check words in use by both BitSets
//        for (int i = 0; i < wordsInUse; i++)
//            if (words[i] != set.words[i])
//                return false;
//
//        return true;
//    }
//
//    /**
//     * Cloning this {@code BitSet} produces a new {@code BitSet}
//     * that is equal to it.
//     * The clone of the bit set is another bit set that has exactly the
//     * same bits set to {@code true} as this bit set.
//     *
//     * @return a clone of this bit set
//     * @see #size()
//     */
//    public Object clone() {
//        if (!sizeIsSticky)
//            trimToSize();
//
//        try {
//            BitSet result = (BitSet) super.clone();
//            result.words = words.clone();
//            result.checkInvariants();
//            return result;
//        } catch (CloneNotSupportedException e) {
//            throw new InternalError(e);
//        }
//    }
//
//    /**
//     * Attempts to reduce internal storage used for the bits in this bit set.
//     * Calling this method may, but is not required to, affect the value
//     * returned by a subsequent call to the {@link #size()} method.
//     */
//    private void trimToSize() {
//        if (wordsInUse != words.length) {
//            words = Arrays.copyOf(words, wordsInUse);
//            checkInvariants();
//        }
//    }
//
//    /**
//     * Save the state of the {@code BitSet} instance to a stream (i.e.,
//     * serialize it).
//     */
//    private void writeObject(ObjectOutputStream s)
//            throws IOException {
//
//        checkInvariants();
//
//        if (!sizeIsSticky)
//            trimToSize();
//
//        ObjectOutputStream.PutField fields = s.putFields();
//        fields.put("bits", words);
//        s.writeFields();
//    }
//
//    /**
//     * Reconstitute the {@code BitSet} instance from a stream (i.e.,
//     * deserialize it).
//     */
//    private void readObject(ObjectInputStream s)
//            throws IOException, ClassNotFoundException {
//
//        ObjectInputStream.GetField fields = s.readFields();
//        words = (long[]) fields.get("bits", null);
//
//        // Assume maximum length then find real length
//        // because recalculateWordsInUse assumes maintenance
//        // or reduction in logical size
//        wordsInUse = words.length;
//        recalculateWordsInUse();
//        sizeIsSticky = (words.length > 0 && words[words.length - 1] == 0L); // heuristic
//        checkInvariants();
//    }
//
//    /**
//     * Returns a string representation of this bit set. For every index
//     * for which this {@code BitSet} contains a bit in the set
//     * state, the decimal representation of that index is included in
//     * the result. Such indices are listed in order from lowest to
//     * highest, separated by ",&nbsp;" (a comma and a space) and
//     * surrounded by braces, resulting in the usual mathematical
//     * notation for a set of integers.
//     *
//     * <p>Example:
//     * <pre>
//     * BitSet drPepper = new BitSet();</pre>
//     * Now {@code drPepper.toString()} returns "{@code {}}".
//     * <pre>
//     * drPepper.set(2);</pre>
//     * Now {@code drPepper.toString()} returns "{@code {2}}".
//     * <pre>
//     * drPepper.set(4);
//     * drPepper.set(10);</pre>
//     * Now {@code drPepper.toString()} returns "{@code {2, 4, 10}}".
//     *
//     * @return a string representation of this bit set
//     */
//    public String toString() {
//        checkInvariants();
//
//        int numBits = (wordsInUse > 128) ?
//                cardinality() : wordsInUse * BITS_PER_WORD;
//        StringBuilder b = new StringBuilder(6 * numBits + 2);
//        b.append('{');
//
//        int i = nextSetBit(0);
//        if (i != -1) {
//            b.append(i);
//            while (true) {
//                if (++i < 0) break;
//                if ((i = nextSetBit(i)) < 0) break;
//                int endOfRun = nextClearBit(i);
//                do {
//                    b.append(", ").append(i);
//                }
//                while (++i != endOfRun);
//            }
//        }
//
//        b.append('}');
//        return b.toString();
//    }
//
//    /**
//     * Returns a stream of indices for which this {@code BitSet}
//     * contains a bit in the set state. The indices are returned
//     * in order, from lowest to highest. The size of the stream
//     * is the number of bits in the set state, equal to the value
//     * returned by the {@link #cardinality()} method.
//     *
//     * <p>The bit set must remain constant during the execution of the
//     * terminal stream operation.  Otherwise, the result of the terminal
//     * stream operation is undefined.
//     *
//     * @return a stream of integers representing set indices
//     * @since 1.8
//     */
//    public IntStream stream() {
//        class BitSetIterator implements PrimitiveIterator.OfInt {
//            int next = nextSetBit(0);
//
//            @Override
//            public boolean hasNext() {
//                return next != -1;
//            }
//
//            @Override
//            public int nextInt() {
//                if (next != -1) {
//                    int ret = next;
//                    next = nextSetBit(next + 1);
//                    return ret;
//                } else {
//                    throw new NoSuchElementException();
//                }
//            }
//        }
//
//        return StreamSupport.intStream(
//                () -> Spliterators.spliterator(
//                        new BitSetIterator(), cardinality(),
//                        Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.SORTED),
//                Spliterator.SIZED | Spliterator.SUBSIZED |
//                        Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.SORTED,
//                false);
//    }
//}
