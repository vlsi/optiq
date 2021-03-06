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

package org.eigenbase.rel;

import java.util.AbstractList;
import java.util.List;

import org.eigenbase.rex.RexNode;

/**
 * Contains factory interface and default implementation for creating various
 * rel nodes.
 */
public class RelFactories {

  public static final ProjectFactory DEFAULT_PROJECT_FACTORY =
      new ProjectFactoryImpl();

  private RelFactories() {
  }

  /**
   * Can create a {@link org.eigenbase.rel.ProjectRel} of the appropriate type
   * for this rule's calling convention.
   */
  public interface ProjectFactory {
    /**
     * Can create a {@link org.eigenbase.rel.ProjectRel} of the appropriate type
     * for this rule's calling convention.
     */
    RelNode createProject(RelNode child, List<RexNode> childExprs,
        List<String> fieldNames);
  }

  /**
   * Implementation of {@link ProjectFactory} that returns vanilla
   * {@link ProjectRel}.
   */
  private static class ProjectFactoryImpl implements ProjectFactory {
    public RelNode createProject(RelNode child, List<RexNode> childExprs,
        List<String> fieldNames) {
      return CalcRel.createProject(child, childExprs, fieldNames);
    }
  }

  /**
   * Creates a relational expression that projects the given fields of the
   * input.
   * <p>
   * Optimizes if the fields are the identity projection.
   * </p>
   * @param factory
   *          ProjectFactory
   * @param child
   *          Input relational expression
   * @param posList
   *          Source of each projected field
   * @return Relational expression that projects given fields
   */
  public static RelNode createProject(final ProjectFactory factory,
      final RelNode child, final List<Integer> posList) {
    if (isIdentity(posList, child.getRowType().getFieldCount())) {
      return child;
    }
    return factory.createProject(child, new AbstractList<RexNode>() {
      public int size() {
        return posList.size();
      }

      public RexNode get(int index) {
        final int pos = posList.get(index);
        return child.getCluster().getRexBuilder().makeInputRef(
            child.getRowType().getFieldList().get(pos).getType(), pos);
      }
    }, null);
  }

  private static boolean isIdentity(List<Integer> list, int count) {
    if (list.size() != count) {
      return false;
    }
    for (int i = 0; i < count; i++) {
      final Integer o = list.get(i);
      if (o == null || o != i) {
        return false;
      }
    }
    return true;
  }

}
