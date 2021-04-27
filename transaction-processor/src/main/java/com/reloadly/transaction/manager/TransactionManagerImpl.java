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

    @Override
    @Transactional
    public void handleTransaction(TransactionEntity txnEntity) throws ReloadlyTxnProcessingException {
        if (txnProcessorMap.containsKey(txnEntity.getTransactionType().name())) {
            try {
                txnProcessorMap.get(txnEntity.getTransactionType().name()).handleTransaction(txnEntity);
            } catch (Exception e) {
                LOGGER.info("Transaction processor has failed. Root cause: " + e.getMessage());
                throw e;
            }
        } else {
            LOGGER.error("No Transaction processor found to handle transactions of type {}.",
                    txnEntity.getTransactionType().name());
            throw new ReloadlyTxnProcessingException("No Transaction processor found to handle transactions of type {}." +
                    txnEntity.getTransactionType().name());
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
