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

import com.reloadly.transaction.annotation.TransactionHandler;
import com.reloadly.transaction.entity.TransactionEntity;
import com.reloadly.transaction.exception.ReloadlyTxnProcessingException;
import com.reloadly.transaction.processor.TransactionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Hashtable;
import java.util.Map;

/**
 * The transaction manager is a faćade into the configured transaction processors. Each type of transaction can only
 * by handled by a specific {@link TransactionProcessor}.
 * <p>
 * The transaction manager locates the appropriate transaction processor and delegates.
 *
 * @author Arun Patra
 */
@Service
public class TransactionManagerImpl implements TransactionManager, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManagerImpl.class);
    private final ApplicationContext context;
    private final Map<String, TransactionProcessor> txnProcessorMap = new Hashtable<>();

    public TransactionManagerImpl(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        // Find out all beans available.
        Map<String, Object> myMap = context.getBeansWithAnnotation(TransactionHandler.class);

        context.getBeansWithAnnotation(TransactionHandler.class).entrySet().stream()
                .filter(e -> (e.getValue() instanceof TransactionProcessor))
                .forEach(e -> {
                    TransactionProcessor tp = (TransactionProcessor) e.getValue();
                    TransactionHandler txHandlerAnnotation = tp.getClass().getAnnotation(TransactionHandler.class);
                    txnProcessorMap.put(txHandlerAnnotation.value(), tp);
                });
        // Print
        printGafDetails();
    }

    /**
     * Delegates to a {@link TransactionProcessor}. If the transaction processor throws an exception, the operation is
     * rolled back. This operation thus must start a database transaction, or possibly a distributed transaction is XA
     * is enabled.
     *
     * @param txnEntity The transaction entity record.
     */
    @Override
    @Transactional
    public void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException {
        try {
            txnProcessorMap.get(txnEntity.getTransactionType().name()).processTransaction(txnEntity);
        } catch (Exception e) {
            LOGGER.info("Transaction processor has failed. Root cause: " + e.getMessage());
            throw new ReloadlyTxnProcessingException("Transaction processor has failed. Root cause: " + e.getMessage());
        }
    }

    private void printGafDetails() {
        LOGGER.info(String.format("%d Transaction processors(s) loaded.", txnProcessorMap.size()));
        if (txnProcessorMap.size() > 0) {
            System.out.println(getReportData());
        }
    }

    private String getReportData() {
        StringBuffer sb = new StringBuffer();
        sb
                .append("------------------------------------------------------------------------------------")
                .append("\nTransaction Processor List                                    ")
                .append("\n------------------------------------------------------------------------------------");
        txnProcessorMap.forEach((x, y) -> {
            sb.append("\nTransaction Processor Name: " + x + ", Class: " + y.getClass().getName());
        });
        sb.append("\n");
        return sb.toString();
    }
}
