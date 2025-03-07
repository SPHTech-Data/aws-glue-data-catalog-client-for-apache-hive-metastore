package org.apache.hadoop.hive.ql.metadata;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaHookLoader;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;

import java.util.concurrent.ConcurrentHashMap;

public interface HiveMetaStoreClientFactory {
    IMetaStoreClient createMetaStoreClient(
            HiveConf conf, HiveMetaHookLoader hookLoader,
            boolean allowEmbedded,
            ConcurrentHashMap<String, Long> concurrentHashMap
    ) throws MetaException;
}