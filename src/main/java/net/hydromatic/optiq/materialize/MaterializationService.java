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
import net.hydromatic.optiq.jdbc.OptiqPrepare;
import net.hydromatic.optiq.prepare.Prepare;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the table of materializations.
 */
public class MaterializationService {
  public static final MaterializationService INSTANCE =
      new MaterializationService();

  private final MaterializationActor actor = new MaterializationActor();

  /** Defines a new materialization. Returns its key. */
  public MaterializationKey defineMaterialization(
      Schema.TableFunctionInSchema tableFunctionInSchema, String sql) {
    final MaterializationKey key = new MaterializationKey();

    final OptiqPrepare.ParseResult parse =
        Schemas.parse(tableFunctionInSchema.schema, null, sql);
    final MaterializationActor.Materialization materialization =
        new MaterializationActor.Materialization(key, tableFunctionInSchema,
            sql, parse.rowType);
    actor.materializations.put(materialization.tablePath, materialization);
    actor.materializationsByKey.put(materialization.key, materialization);
    return key;
  }

  /** Returns whether a materialization is valid. */
  public boolean isValid(MaterializationKey key) {
    final MaterializationActor.Materialization materialization =
        actor.materializationsByKey.get(key);
    return materialization != null && materialization.valid;
  }

  public List<Prepare.Materialization> queryMaterializations() {
    final List<Prepare.Materialization> list =
        new ArrayList<Prepare.Materialization>();
    for (MaterializationActor.Materialization materialization
        : actor.materializationsByKey.values()) {
      list.add(
          new Prepare.Materialization(materialization.tableFunctionInSchema,
              materialization.sql));
    }
    return list;
  }
}

// End MaterializationService.java
