package com.xxw.shop.starter.canal.support.processor;

import com.xxw.shop.starter.canal.model.ModelTable;
import com.xxw.shop.starter.canal.util.AssertUtils;
import com.xxw.shop.starter.canal.util.CollectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@NoArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
public class InMemoryCanalBinlogEventProcessorFactory implements CanalBinlogEventProcessorFactory {

    private final ConcurrentMap<ModelTable, List<BaseCanalBinlogEventProcessor<?>>> cache = new ConcurrentHashMap<>(16);

    @Override
    public void register(ModelTable modelTable, BaseCanalBinlogEventProcessor<?> processor) {
        synchronized (cache) {
            cache.putIfAbsent(modelTable, new LinkedList<>());
            cache.get(modelTable).add(processor);
        }
    }

    @Override
    public List<BaseCanalBinlogEventProcessor<?>> get(ModelTable modelTable) {
        List<BaseCanalBinlogEventProcessor<?>> processors = cache.get(modelTable);
        AssertUtils.X.isTrue(CollectionUtils.X.isNotEmpty(processors), String.format("Processor Not Found For %s",
                modelTable));
        return processors;
    }
}
