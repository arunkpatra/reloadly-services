/*
 * MIT License
 *
 * Copyright (c) 2021 Arun Patra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.reloadly.transaction.manager;

import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.processor.TransactionProcessor;

/**
 * The transaction manager is a faćade into the configured transaction processors. Each type of transaction can only
 * by handled by a specific {@link TransactionProcessor}.
 * <p>
 * The transaction manager locates the appropriate transaction processor and delegates.
 *
 * @author Arun Patra
 */
public interface TransactionManager {

    /**
     * Delegates to a {@link TransactionProcessor}. If the transaction processor throws an exception, the operation is
     * rolled back. This operation thus must start a database transaction, or possibly a distributed transaction is XA
     * is enabled.
     *
     * @param txnEntity The transaction entity record.
     */
    void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException;
}
