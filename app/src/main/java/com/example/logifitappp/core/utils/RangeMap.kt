package com.example.logifitappp.core.utils

class RangeMap<K: Comparable<K>, V>(mode: Mode = Mode.LOWER_BOUND) {
    private val comparator: Comparator<K> = when (mode) {
        Mode.LOWER_BOUND -> Comparator { k1, k2 -> k1.compareTo(k2) }
        Mode.UPPER_BOUND -> Comparator { k1, k2 -> k2.compareTo(k1) }
    }

    private var isSorted = false
    private val list = mutableListOf<Pair<K, V>>()

    fun get(key: K): V? {
        if (!isSorted) {
            list.sortWith { a, b -> comparator.compare(a.first, b.first) }
            isSorted = true
        }

        for (i in list.size - 1 downTo 0) {
            if (comparator.compare(key, list[i].first) >= 0) {
                return list[i].second
            }
        }

        return null
    }

    fun isEmpty() = list.isEmpty()

    fun put(key: K, value: V) {
        list.add(Pair(key, value))
        isSorted = false
    }

    fun size() = list.size

    enum class Mode {
        LOWER_BOUND,
        UPPER_BOUND;
    }
}