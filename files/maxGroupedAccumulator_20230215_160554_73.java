//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.trino.$gen;

import io.trino.operator.GroupByIdBlock;
import io.trino.operator.aggregation.Accumulator;
import io.trino.operator.aggregation.AggregationUtils;
import io.trino.operator.aggregation.GroupedAccumulator;
import io.trino.spi.Page;
import io.trino.spi.block.Block;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.function.AccumulatorState;
import io.trino.spi.function.AccumulatorStateFactory;
import io.trino.spi.function.AccumulatorStateSerializer;
import io.trino.spi.function.GroupedAccumulatorState;
import io.trino.sql.gen.CompilerOperations;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public final class maxGroupedAccumulator_20230215_160554_73 implements GroupedAccumulator {
    private final AccumulatorStateSerializer stateSerializer_0;
    private final AccumulatorStateFactory stateFactory_0;
    private final GroupedAccumulatorState state_0;

    public maxGroupedAccumulator_20230215_160554_73(List<Supplier> lambdaProviders) {
        List var10000 = (List)Objects.requireNonNull((Object)lambdaProviders, "lambdaProviders is null");
        this.stateSerializer_0 = constant_0<invokedynamic>();
        AccumulatorStateSerializer var10001 = (AccumulatorStateSerializer)Objects.requireNonNull((Object)this.stateSerializer_0, "stateSerializer_0 is null");
        this.stateFactory_0 = constant_1<invokedynamic>();
        AccumulatorStateFactory var10002 = (AccumulatorStateFactory)Objects.requireNonNull((Object)this.stateFactory_0, "stateFactory_0 is null");
        this.state_0 = (GroupedAccumulatorState)this.stateFactory_0.createGroupedState();
        GroupedAccumulatorState var10003 = (GroupedAccumulatorState)Objects.requireNonNull((Object)this.state_0, "state_0 is null");
    }

    public maxGroupedAccumulator_20230215_160554_73(maxGroupedAccumulator_20230215_160554_73 source) {
        maxGroupedAccumulator_20230215_160554_73 var10000 = (maxGroupedAccumulator_20230215_160554_73)Objects.requireNonNull((Object)source, "source is null");
        this.stateSerializer_0 = source.stateSerializer_0;
        AccumulatorStateSerializer var10001 = (AccumulatorStateSerializer)Objects.requireNonNull((Object)this.stateSerializer_0, "stateSerializer_0 is null");
        this.stateFactory_0 = source.stateFactory_0;
        AccumulatorStateFactory var10002 = (AccumulatorStateFactory)Objects.requireNonNull((Object)this.stateFactory_0, "stateFactory_0 is null");
        this.state_0 = (GroupedAccumulatorState)source.state_0.copy();
        GroupedAccumulatorState var10003 = (GroupedAccumulatorState)Objects.requireNonNull((Object)this.state_0, "state_0 is null");
    }

    public Accumulator copy() {
        return new maxGroupedAccumulator_20230215_160554_73(this);
    }

    public void addInput(GroupByIdBlock groupIdsBlock, Page arguments, Optional mask) {
        this.state_0.ensureCapacity(groupIdsBlock.getGroupCount());
        Block masksBlock = (Block)mask.orElse((Object)null);
        Block block0 = arguments.getBlock(0);
        int rows = arguments.getPositionCount();
        int position = false;
        if (!AggregationUtils.maskGuaranteedToFilterAllRows(rows, masksBlock)) {
            int position;
            if (false || block0.mayHaveNull()) {
                for(position = 0; CompilerOperations.lessThan(position, rows); ++position) {
                    if (CompilerOperations.testMask(masksBlock, position) && !block0.isNull(position)) {
                        this.state_0.setGroupId(groupIdsBlock.getGroupId(position));
                        this.state_0.input<invokedynamic>(this.state_0, block0, position);
                    }
                }
            } else {
                for(position = 0; CompilerOperations.lessThan(position, rows); ++position) {
                    if (CompilerOperations.testMask(masksBlock, position)) {
                        this.state_0.setGroupId(groupIdsBlock.getGroupId(position));
                        this.state_0.input<invokedynamic>(this.state_0, block0, position);
                    }
                }
            }
        }

    }

    public long getEstimatedSize() {
        long estimatedSize = 0L;
        estimatedSize += this.state_0.getEstimatedSize();
        return estimatedSize;
    }

    public void addIntermediate(GroupByIdBlock groupIdsBlock, Block block) {
        AccumulatorState scratchState_0 = (AccumulatorState)this.stateFactory_0.createSingleState();
        this.state_0.ensureCapacity(groupIdsBlock.getGroupCount());
        int rows = block.getPositionCount();

        for(int position = 0; CompilerOperations.lessThan(position, rows); ++position) {
            if (!block.isNull(position) && !groupIdsBlock.isNull(position)) {
                this.state_0.setGroupId(groupIdsBlock.getGroupId(position));
                GroupedAccumulatorState var10000 = this.state_0;
                this.stateSerializer_0.deserialize(block, position, (AccumulatorState)scratchState_0);
                var10000.combine<invokedynamic>(var10000, scratchState_0);
            }
        }

    }

    public void evaluateIntermediate(int groupId, BlockBuilder out) {
        this.state_0.setGroupId((long)groupId);
        this.stateSerializer_0.serialize((AccumulatorState)this.state_0, out);
    }

    public void evaluateFinal(int groupId, BlockBuilder out) {
        this.state_0.setGroupId((long)groupId);
        this.state_0.output<invokedynamic>(this.state_0, out);
    }

    public void prepareFinal() {
    }
}
