package com.amazonaws.glue.shims;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.EnvironmentContext;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.metastore.utils.MetaStoreUtils;
import org.apache.hadoop.hive.metastore.utils.MetaStoreServerUtils;
import org.apache.hadoop.hive.ql.exec.SerializationUtilities;

import java.nio.charset.StandardCharsets;
import org.apache.hadoop.hive.ql.plan.ExprNodeGenericFuncDesc;
import org.apache.hadoop.hive.metastore.Warehouse;

import java.util.List;

final class AwsGlueHive3Shims implements AwsGlueHiveShims {

  private static final String HIVE_3_VERSION = "3.";

  static boolean supportsVersion(String version) {
    return version.startsWith(HIVE_3_VERSION);
  }

  @Override
  public ExprNodeGenericFuncDesc getDeserializeExpression(byte[] exprBytes) {
    return SerializationUtilities.deserializeExpression(new String(exprBytes, StandardCharsets.UTF_8));
  }

  @Override
  public byte[] getSerializeExpression(ExprNodeGenericFuncDesc expr) {
    return SerializationUtilities.serializeExpression(expr).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public Path getDefaultTablePath(Database db, String tableName, Warehouse warehouse) throws MetaException {
    return warehouse.getDefaultTablePath(db, tableName, false);
  }

  @Override
  public boolean deleteDir(Warehouse wh, Path path, boolean recursive, boolean ifPurge) throws MetaException {
    return wh.deleteDir(path, recursive, ifPurge, true);
  }

  @Override
  public boolean mkdirs(Warehouse wh, Path path) throws MetaException {
    return wh.mkdirs(path);
  }

  @Override
  public boolean validateTableName(String name, Configuration conf) {
    return MetaStoreUtils.validateName(name, conf);
  }

  @Override
  public boolean requireCalStats(
      Configuration conf,
      Partition oldPart,
      Partition newPart,
      Table tbl,
      EnvironmentContext environmentContext) {
    return MetaStoreServerUtils.requireCalStats(oldPart, newPart, tbl, environmentContext);
  }

  @Override
  public boolean updateTableStatsFast(
      Database db,
      Table tbl,
      Warehouse wh,
      boolean madeDir,
      boolean forceRecompute,
      EnvironmentContext environmentContext
  ) throws MetaException {
    MetaStoreServerUtils.updateTableStatsSlow(db, tbl, wh, madeDir, forceRecompute, environmentContext);
    return true;
  }

  @Override
  public String validateTblColumns(List<FieldSchema> cols) {
    return MetaStoreServerUtils.validateTblColumns(cols);
  }

}

