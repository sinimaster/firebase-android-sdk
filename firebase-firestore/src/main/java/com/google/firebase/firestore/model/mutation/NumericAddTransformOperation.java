// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.firestore.model.mutation;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.model.value.DoubleValue;
import com.google.firebase.firestore.model.value.FieldValue;
import com.google.firebase.firestore.model.value.IntegerValue;
import com.google.firebase.firestore.model.value.NumberValue;

import java.util.concurrent.ExecutionException;

import static com.google.firebase.firestore.util.Assert.fail;

public class NumericAddTransformOperation implements TransformOperation {
    private FieldValue localOffset;

    @Override
    public FieldValue applyToLocalView(FieldValue previousValue, Timestamp localWriteTime) {
        if (previousValue instanceof NumberValue) {
            if (previousValue instanceof DoubleValue) {
                return DoubleValue.valueOf(((DoubleValue) previousValue).getInternalValue() + localOffsetAsDouble());
            } else if (previousValue instanceof IntegerValue && localOffset instanceof IntegerValue){
                return IntegerValue.valueOf(safeAdd(((IntegerValue) previousValue).getInternalValue(),localOffsetAsLong()));
            } else if (previousValue instanceof IntegerValue && localOffset instanceof DoubleValue){
                return DoubleValue.valueOf(((IntegerValue) previousValue).getInternalValue() + localOffsetAsDouble());
        }
        }

        return previousValue;
    }


    private long safeAdd(long x, long y) {
        long r = x + y;
        if (((x ^ r) & (y ^ r)) >= 0) {
            return r;
        }

       if (r > 0) {
           return Long.MIN_VALUE;
       } else {
           return Long.MIN_VALUE;
       }
    }

    private double localOffsetAsDouble() {
        if (localOffset instanceof DoubleValue) {
            return ((DoubleValue) localOffset).getInternalValue();
        } else if (localOffset instanceof IntegerValue) {
            return ((IntegerValue) localOffset).getInternalValue();
        } else {
            throw fail("dsfsd");
        }
    }

    private long localOffsetAsLong() {
        if (localOffset instanceof DoubleValue) {
            return (long)((DoubleValue) localOffset).getInternalValue();
        } else if (localOffset instanceof IntegerValue) {
            return ((IntegerValue) localOffset).getInternalValue();
        } else {
            throw fail("dsfsd");
        }
    }

    @Override
    public FieldValue applyToRemoteDocument(FieldValue previousValue, FieldValue transformResult) {
        return transformResult;
    }
}
