package com.example.cs_topics_project_test.function.tasks
/*package com.example.cs_topics_project_test.function.tasks

import com.example.cs_topics_project_test.function.DateAndTime
import org.w3c.dom.Node
import java.util.Queue

/**
 * Work on this approach later; for now just use an array list!
 */


/**
 * A symbol table implemented using a left-leaning red-black BST. This is the 2-3 version.
 * Adapted to Kotlin for the purposes of organizing and storing data related to the Task feature
 * Citation:
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

class RedBlackBST {
    private companion object {
        const val PINK : Boolean = true
        const val BLACK : Boolean = false;
        }

    private var root : Node = TODO()

    // BST helper node data type
    private class Node(
        var key: DateAndTime, // key
        var value : Task, // associated data
        var color: Boolean, // color of parent link
        var size: Int // subtree count
    ) {
        var left: Node? = null
        var right: Node? = null // links to left and right subtrees
    }

    init {}

    // node helper methods
    private fun isRed(x : Node) : Boolean {
        if (x == null) return false
        return x.color == PINK
    }

    private fun size(x : Node) : Int {
        if (x == null) return 0
        return x.size
    }

    fun size() : Int {
        return size(root)
    }

    fun isEmpty() : Boolean {
        return root == null
    }


    // Standard BST Search
    fun get(key : DateAndTime) : Task? {
        if (key == null) throw IllegalArgumentException("argument to get() is null");
        return get(root, key);
    }

    private fun get(x: Node, key: DateAndTime): Task? {
        var x: Node = x
        while (x != null) {
            val cmp: Int = key.compareTo(x.key)
            if (cmp < 0) x = x.left!!
            else if (cmp > 0) x = x.right!!
            else return x.value
        }
        return null
    }

    fun contains(key: DateAndTime): Boolean {
        return get(key) != null
    }

    // Pink-black tree insertion
    fun put(key: DateAndTime, value: Task) {
        requireNotNull(key) { "first argument to put() is null" }
        if (value == null) {
            delete(key)
            return
        }
        root = put(root, key, value)
        root.color = BLACK
        // assert check();
    }

    // insert the key-value pair in the subtree rooted at h
    private fun put(l: Node, key: DateAndTime, value: Task): Node {
        if (l == null) return Node(key, value, PINK, 1)

        var h = l
        val cmp: Int = key.compareTo(h.key)
        if (cmp < 0) h.left = put(h.left!!, key, value)
        else if (cmp > 0) h.right = put(h.right!!, key, value)
        else h.value = value

        // fix-up any right-leaning links
        if (isRed(h.right!!) && !isRed(h.left!!)) h = rotateLeft(h)
        if (isRed(h.left!!) && isRed(h.left!!.left!!)) h = rotateRight(h)
        if (isRed(h.left!!) && isRed(h.right!!)) flipColors(h)
        h.size = size(h.left!!) + size(h.right!!) + 1

        return h
    }

    // Pink-black tree deletion
    // For out purpose, no need deleteMin or deleteMax
    fun delete(key: DateAndTime) {
        requireNotNull(key) { "argument to delete() is null" }
        if (!contains(key)) return

        // if both children of root are black, set root to pink
        if (!isRed(root.left!!) && !isRed(root.right!!)) root.color = PINK

        root = delete(root, key)!!
        if (!isEmpty()) root.color = BLACK
        // assert check();
    }

    // delete the key-value pair with the given key rooted at h
    private fun delete(l: Node, key: DateAndTime): Node? {
        // assert get(h, key) != null;
        var h = l
        if (key.compareTo(h.key) < 0) {
            if (!isRed(h.left!!) && !isRed(h.left!!.left!!)) h = moveRedLeft(h)
            h.left = delete(h.left!!, key)
        } else {
            if (isRed(h.left!!)) h = rotateRight(h)
            if (key.compareTo(h.key) == 0 && (h.right == null)) return null
            if (!isRed(h.right!!) && !isRed(h.right!!.left!!)) h = moveRedRight(h)
            if (key.compareTo(h.key) == 0) {
                val x: Node = min(h.right!!)
                h.key = x.key
                h.value = x.value
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right)
            } else h.right = delete(h.right!!, key)
        }
        return balance(h)
    }

    // Pink-Black helped functions
    // make a left-leaning link lean to the right
    private fun rotateRight(h: Node): Node {
        assert((h != null) && isRed(h.left!!))
        // assert (h != null) && isRed(h.left) &&  !isRed(h.right);  // for insertion only
        val x: Node = h.left!!
        h.left = x.right
        x.right = h
        x.color = h.color
        h.color = PINK
        x.size = h.size
        h.size = size(h.left!!) + size(h.right!!) + 1
        return x
    }

    // make a right-leaning link lean to the left
    private fun rotateLeft(h: Node): Node {
        assert((h != null) && isRed(h.right!!))
        // assert (h != null) && isRed(h.right) && !isRed(h.left);  // for insertion only
        val x: Node = h.right!!
        h.right = x.left
        x.left = h
        x.color = h.color
        h.color = PINK
        x.size = h.size
        h.size = size(h.left!!) + size(h.right!!) + 1
        return x
    }

    // flip the colors of a node and its two children
    private fun flipColors(h: Node) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color
        h.left!!.color = !h.left!!.color
        h.right!!.color = !h.right!!.color
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private fun moveRedLeft(l: Node): Node {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);
        var h = l
        flipColors(h)
        if (isRed(h.right!!.left!!)) {
            h.right = rotateRight(h.right!!)
            h = rotateLeft(h)
            flipColors(h)
        }
        return h
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private fun moveRedRight(l: Node): Node {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        var h = l
        flipColors(h)
        if (isRed(h.left!!.left!!)) {
            h = rotateRight(h)
            flipColors(h)
        }
        return h
    }

    // restore red-black tree invariant
    private fun balance(l: Node): Node {
        // assert (h != null);

        var h = l
        if (isRed(h.right!!) && !isRed(h.left!!)) h = rotateLeft(h)
        if (isRed(h.left!!) && isRed(h.left!!.left!!)) h = rotateRight(h)
        if (isRed(h.left!!) && isRed(h.right!!)) flipColors(h)

        h.size = size(h.left!!) + size(h.right!!) + 1
        return h
    }

    // Ordered Symbol Table methods
    fun min(): DateAndTime {
        if (isEmpty()) throw NoSuchElementException("calls min() with empty symbol table")
        return min(root).key
    }

    // the smallest key in subtree rooted at x; null if no such key
    private fun min(x: Node): Node {
        // assert x != null;
        return if (x.left == null) x
        else min(x.left!!)
    }

    /**
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun max(): DateAndTime {
        if (isEmpty()) throw NoSuchElementException("calls max() with empty symbol table")
        return max(root).key
    }

    // the largest key in the subtree rooted at x; null if no such key
    private fun max(x: Node): Node {
        // assert x != null;
        return if (x.right == null) x
        else max(x.right!!)
    }

    // floor and ceiling

    fun keys(): Iterable<DateAndTime> {
        if (isEmpty()) return Queue<DateAndTime>
        return keys(min(), max())
    }

    /**
     * Returns all keys in the symbol table in the given range in ascending order,
     * as an `Iterable`.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the symbol table between `lo`
     * (inclusive) and `hi` (inclusive) in ascending order
     * @throws IllegalArgumentException if either `lo` or `hi`
     * is `null`
     */
    fun keys(lo: DateAndTime, hi: DateAndTime): Iterable<DateAndTime> {
        requireNotNull(lo) { "first argument to keys() is null" }
        requireNotNull(hi) { "second argument to keys() is null" }

        val queue: Queue<DateAndTime> = Queue<DateAndTime>
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi)
        return queue
    }
}*/