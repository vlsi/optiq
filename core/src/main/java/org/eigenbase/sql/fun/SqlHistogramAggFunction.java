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
package org.eigenbase.sql.fun;

import java.util.List;

import org.eigenbase.reltype.*;
import org.eigenbase.sql.*;
import org.eigenbase.sql.type.*;

import com.google.common.collect.ImmutableList;

/**
 * <code>HistogramAgg</code> is base operator which supports the Histogram
 * MIN/MAX aggregatoin functions. which returns the sum of the values which go
 * into it. It has precisely one argument of numeric type (<code>int</code>,
 * <code>long</code>, <code>float</code>, <code>double</code>) results are
 * retrived with (<code>HistogramMin</code>) and (<code>HistogramMax</code>)
 */
public class SqlHistogramAggFunction extends SqlAggFunction {
  //~ Instance fields --------------------------------------------------------

  private final RelDataType type;

  //~ Constructors -----------------------------------------------------------

  public SqlHistogramAggFunction(RelDataType type) {
    super(
        "$HISTOGRAM",
        SqlKind.OTHER_FUNCTION,
        ReturnTypes.HISTOGRAM,
        null,
        OperandTypes.NUMERIC_OR_STRING,
        SqlFunctionCategory.NUMERIC);
    this.type = type;
  }

  //~ Methods ----------------------------------------------------------------

  public List<RelDataType> getParameterTypes(RelDataTypeFactory typeFactory) {
    return ImmutableList.of(type);
  }

  public RelDataType getType() {
    return type;
  }

  public RelDataType getReturnType(RelDataTypeFactory typeFactory) {
    return type;
  }
}

// End SqlHistogramAggFunction.java
