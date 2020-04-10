package com.lijun.rpc.core;

/**
 * Class Name RpcMap ...
 *
 * @author LiJun
 * Created on 2020/4/6 11:14
 */
public class RpcMap<K, V> {

    private K key;
    private V value;

    public RpcMap(){

    }

    public RpcMap(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
