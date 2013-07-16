/*
// Licensed to Julian Hyde under one or more contributor license
// agreements. See the NOTICE file distributed with this work for
// additional information regarding copyright ownership.
//
// Julian Hyde licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except in
// compliance with the License. You may obtain a copy of the License at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
*/
package net.hydromatic.optiq.materialize;

import net.hydromatic.optiq.Schema;
import net.hydromatic.optiq.Schemas;

import org.eigenbase.reltype.RelDataType;

import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * Actor that manages the state of materializations in the system.
 */
class MaterializationActor {
  // Not an actor yet -- TODO make members private and add request/response
  // queues

  final Map<ImmutableList<String>, Materialization> materializations =
      new HashMap<ImmutableList<String>, Materialization>();

  final Map<MaterializationKey, Materialization> materializationsByKey =
      new HashMap<MaterializationKey, Materialization>();

  static class Materialization {
    final MaterializationKey key;
    final ImmutableList<String> tablePath;
    final Schema.TableFunctionInSchema tableFunctionInSchema;
    final String sql;
    final RelDataType rowType;
    /** Whether currently valid. */
    boolean valid;

    Materialization(MaterializationKey key,
        Schema.TableFunctionInSchema tableFunctionInSchema, String sql,
        RelDataType rowType) {
      this.key = key;
      this.tableFunctionInSchema = tableFunctionInSchema;
      this.tablePath = ImmutableList.copyOf(
          Schemas.path(tableFunctionInSchema.schema,
              tableFunctionInSchema.name));
      this.sql = sql;
      this.rowType = rowType;
    }
  }
}

// End MaterializationActor.java
