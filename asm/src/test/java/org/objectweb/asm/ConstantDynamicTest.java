// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package org.objectweb.asm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ConstantDynamic}.
 *
 * @author Eric Bruneton
 */
class ConstantDynamicTest {

  @Test
  void testToString_notTruncated() {
    String s = constantDynamicDag(6).toString();

    assertTrue(s.length() < 16384);
    assertFalse(s.endsWith("..."));
  }

  @Test
  void testToString_truncated() {
    String s1 = constantDynamicDag(7).toString();
    String s2 = constantDynamicDag(256).toString();

    assertEquals(16384, s1.length());
    assertEquals(16384, s2.length());
    assertTrue(s1.endsWith("..."));
    assertTrue(s2.endsWith("..."));
  }

  @Test
  void testSubtractTreeSize_smallDag() {
    ConstantDynamic constant = constantDynamicDag(13);

    int size1 = constant.subtractTreeSize(10000);
    int size2 = constant.subtractTreeSize(1000);

    assertEquals(10000 - ((1 << 13) - 1), size1);
    assertEquals(0, size2);
  }

  @Test
  void testSubtractTreeSize_largeDag() {
    ConstantDynamic constant = constantDynamicDag(256);

    // The tree representation would have 2^256 - 1 nodes...
    int size = constant.subtractTreeSize(10000);

    assertEquals(0, size);
  }

  private ConstantDynamic constantDynamicDag(final int depth) {
    Handle handle =
        new Handle(
            Opcodes.H_INVOKESTATIC,
            "C",
            "bootstrapMethod",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;"
                + "Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
            false);
    ConstantDynamic constantDynamic = null;
    for (int i = 0; i < depth; i++) {
      // Two identical arguments referencing the previous constant (DAG)
      constantDynamic =
          new ConstantDynamic(
              "const_" + i,
              "Ljava/lang/Object;",
              handle,
              constantDynamic == null ? 0 : constantDynamic,
              constantDynamic == null ? 0 : constantDynamic);
    }
    return constantDynamic;
  }
}
